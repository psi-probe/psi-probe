/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.beans;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.RuntimeOperationsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import psiprobe.model.Connector;
import psiprobe.model.RequestProcessor;
import psiprobe.model.ThreadPool;
import psiprobe.model.jmx.ThreadPoolObjectName;
import psiprobe.tools.JmxTools;

/**
 * This class interfaces Tomcat JMX functionality to read connection status. The class essentially
 * provides and maintains the list of connection ThreadPools.
 */
public class ContainerListenerBean implements NotificationListener {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ContainerListenerBean.class);

  /** The allowed operation. */
  private final Set<String> allowedOperation =
      new HashSet<>(Arrays.asList("start", "stop", "pause", "resume"));

  /** The pool names. */
  private List<ThreadPoolObjectName> poolNames;

  /** The executor names. */
  private List<ObjectName> executorNames;

  /** Used to obtain required {@link MBeanServer} instance. */
  @Inject
  private ContainerWrapperBean containerWrapper;

  /**
   * Gets the container wrapper.
   *
   * @return the container wrapper
   */
  public ContainerWrapperBean getContainerWrapper() {
    return containerWrapper;
  }

  /**
   * Sets the container wrapper.
   *
   * @param containerWrapper the new container wrapper
   */
  public void setContainerWrapper(ContainerWrapperBean containerWrapper) {
    this.containerWrapper = containerWrapper;
  }

  /**
   * Checks if is initialized.
   *
   * @return true, if is initialized
   */
  private boolean isInitialized() {
    return poolNames != null && !poolNames.isEmpty();
  }

  /**
   * Finds ThreadPoolObjectName by its string name.
   *
   * @param name - pool name
   *
   * @return null if the input name is null or ThreadPoolObjectName is not found
   */
  private ThreadPoolObjectName findPool(String name) {
    if (name != null && isInitialized()) {
      for (ThreadPoolObjectName threadPoolObjectName : poolNames) {
        if (name.equals(threadPoolObjectName.getThreadPoolName().getKeyProperty("name"))) {
          return threadPoolObjectName;
        }
      }
    }
    return null;
  }

  /**
   * Handles creation and deletion of new "worker" threads.
   *
   * @param notification the notification
   * @param object the object
   */
  @Override
  public synchronized void handleNotification(Notification notification, Object object) {
    if (!(notification instanceof MBeanServerNotification)) {
      return;
    }

    if (MBeanServerNotification.REGISTRATION_NOTIFICATION.equals(notification.getType())
        || MBeanServerNotification.UNREGISTRATION_NOTIFICATION.equals(notification.getType())) {

      ObjectName objectName = ((MBeanServerNotification) notification).getMBeanName();
      if ("RequestProcessor".equals(objectName.getKeyProperty("type"))) {
        ThreadPoolObjectName threadPoolObjectName = findPool(objectName.getKeyProperty("worker"));
        if (threadPoolObjectName != null) {
          if (MBeanServerNotification.REGISTRATION_NOTIFICATION.equals(notification.getType())) {
            threadPoolObjectName.getRequestProcessorNames().add(objectName);
          } else {
            threadPoolObjectName.getRequestProcessorNames().remove(objectName);
          }
        }
      }
    }
  }

  /**
   * Load ObjectNames for the relevant MBeans so they can be queried at a later stage without
   * searching MBean server over and over again.
   *
   * @throws Exception - this method does not handle any of the exceptions that may be thrown when
   *         querying MBean server.
   */
  private synchronized void initialize() throws Exception {

    MBeanServer server = getContainerWrapper().getResourceResolver().getMBeanServer();
    String serverName = getContainerWrapper().getTomcatContainer().getName();
    Set<ObjectInstance> threadPools =
        server.queryMBeans(new ObjectName(serverName + ":type=ThreadPool,name=\"*\""), null);
    poolNames = new ArrayList<>(threadPools.size());
    for (ObjectInstance threadPool : threadPools) {

      ThreadPoolObjectName threadPoolObjectName = new ThreadPoolObjectName();
      ObjectName threadPoolName = threadPool.getObjectName();

      String name = threadPoolName.getKeyProperty("name");

      threadPoolObjectName.setThreadPoolName(threadPoolName);
      ObjectName grpName = server
          .getObjectInstance(new ObjectName(
              threadPoolName.getDomain() + ":type=GlobalRequestProcessor,name=" + name))
          .getObjectName();
      threadPoolObjectName.setGlobalRequestProcessorName(grpName);

      /*
       * unfortunately exact workers could not be found at the time of testing so we filter out the
       * relevant workers within the loop
       */
      Set<ObjectInstance> workers = server.queryMBeans(
          new ObjectName(threadPoolName.getDomain() + ":type=RequestProcessor,*"), null);

      for (ObjectInstance worker : workers) {
        ObjectName wrkName = worker.getObjectName();
        if (name.equals(wrkName.getKeyProperty("worker"))) {
          threadPoolObjectName.getRequestProcessorNames().add(wrkName);
        }
      }

      poolNames.add(threadPoolObjectName);
    }

    Set<ObjectInstance> executors =
        server.queryMBeans(new ObjectName(serverName + ":type=Executor,*"), null);
    executorNames = new ArrayList<>(executors.size());
    for (ObjectInstance executor : executors) {
      ObjectName executorName = executor.getObjectName();
      executorNames.add(executorName);
    }

    // Register with MBean server
    server.addNotificationListener(new ObjectName("JMImplementation:type=MBeanServerDelegate"),
        this, null, null);

  }

  /**
   * Gets the thread pools.
   *
   * @return the thread pools
   *
   * @throws Exception the exception
   */
  public synchronized List<ThreadPool> getThreadPools() throws Exception {
    if (!isInitialized()) {
      initialize();
    }

    List<ThreadPool> threadPools = new ArrayList<>(poolNames.size());

    MBeanServer server = getContainerWrapper().getResourceResolver().getMBeanServer();

    for (ObjectName executorName : executorNames) {
      ThreadPool threadPool = new ThreadPool();
      threadPool.setName(executorName.getKeyProperty("name"));
      threadPool.setMaxThreads(JmxTools.getIntAttr(server, executorName, "maxThreads"));
      threadPool.setMaxSpareThreads(JmxTools.getIntAttr(server, executorName, "largestPoolSize"));
      threadPool.setMinSpareThreads(JmxTools.getIntAttr(server, executorName, "minSpareThreads"));
      threadPool.setCurrentThreadsBusy(JmxTools.getIntAttr(server, executorName, "activeCount"));
      threadPool.setCurrentThreadCount(JmxTools.getIntAttr(server, executorName, "poolSize"));
      threadPools.add(threadPool);
    }

    for (ThreadPoolObjectName threadPoolObjectName : poolNames) {
      try {
        ObjectName poolName = threadPoolObjectName.getThreadPoolName();

        ThreadPool threadPool = new ThreadPool();
        threadPool.setName(poolName.getKeyProperty("name"));
        threadPool.setMaxThreads(JmxTools.getIntAttr(server, poolName, "maxThreads"));

        if (JmxTools.hasAttribute(server, poolName, "maxSpareThreads")) {
          threadPool.setMaxSpareThreads(JmxTools.getIntAttr(server, poolName, "maxSpareThreads"));
          threadPool.setMinSpareThreads(JmxTools.getIntAttr(server, poolName, "minSpareThreads"));
        }

        threadPool
            .setCurrentThreadsBusy(JmxTools.getIntAttr(server, poolName, "currentThreadsBusy"));
        threadPool
            .setCurrentThreadCount(JmxTools.getIntAttr(server, poolName, "currentThreadCount"));

        /*
         * Tomcat will return -1 for maxThreads if the connector uses an executor for its threads.
         * In this case, don't add its ThreadPool to the results.
         */
        if (threadPool.getMaxThreads() > -1) {
          threadPools.add(threadPool);
        }
      } catch (InstanceNotFoundException e) {
        logger.error("Failed to query entire thread pool {}", threadPoolObjectName);
        logger.debug("", e);
      }
    }
    return threadPools;
  }

  /**
   * Toggle connector status.
   *
   * @param operation the operation
   * @param port the port
   *
   * @throws Exception the exception
   */
  public synchronized void toggleConnectorStatus(String operation, String port) throws Exception {

    if (!allowedOperation.contains(operation)) {
      logger.error("operation {} not supported", operation);
      throw new IllegalArgumentException("Not support operation");
    }

    ObjectName objectName = new ObjectName("Catalina:type=Connector,port=" + port);

    MBeanServer server = getContainerWrapper().getResourceResolver().getMBeanServer();

    JmxTools.invoke(server, objectName, operation, null, null);

    logger.info("operation {} on Connector {} invoked success", operation, objectName);
  }

  /**
   * Gets the connectors.
   *
   * @param includeRequestProcessors the include request processors
   *
   * @return the connectors
   *
   * @throws Exception the exception
   */
  public synchronized List<Connector> getConnectors(boolean includeRequestProcessors)
      throws Exception {

    boolean workerThreadNameSupported = true;

    if (!isInitialized()) {
      initialize();
    }

    List<Connector> connectors = new ArrayList<>(poolNames.size());

    MBeanServer server = getContainerWrapper().getResourceResolver().getMBeanServer();

    for (ThreadPoolObjectName threadPoolObjectName : poolNames) {
      try {
        ObjectName poolName = threadPoolObjectName.getThreadPoolName();

        Connector connector = new Connector();

        String name = poolName.getKeyProperty("name");

        connector.setProtocolHandler(poolName.getKeyProperty("name"));

        if (name.startsWith("\"") && name.endsWith("\"")) {
          name = name.substring(1, name.length() - 1);
        }

        String[] arr = name.split("-", -1);
        String port = "-1";
        if (arr.length == 3) {
          port = arr[2];
        }

        if (!"-1".equals(port)) {
          String str = "Catalina:type=Connector,port=" + port;

          ObjectName objectName = new ObjectName(str);

          // add some useful information for connector list
          connector.setStatus(JmxTools.getStringAttr(server, objectName, "stateName"));
          connector.setProtocol(JmxTools.getStringAttr(server, objectName, "protocol"));
          connector.setSecure(
              Boolean.parseBoolean(JmxTools.getStringAttr(server, objectName, "secure")));
          connector.setPort(JmxTools.getIntAttr(server, objectName, "port"));
          connector.setLocalPort(JmxTools.getIntAttr(server, objectName, "localPort"));
          connector.setSchema(JmxTools.getStringAttr(server, objectName, "schema"));
        }

        ObjectName grpName = threadPoolObjectName.getGlobalRequestProcessorName();

        connector.setMaxTime(JmxTools.getLongAttr(server, grpName, "maxTime"));
        connector.setProcessingTime(JmxTools.getLongAttr(server, grpName, "processingTime"));
        connector.setBytesReceived(JmxTools.getLongAttr(server, grpName, "bytesReceived"));
        connector.setBytesSent(JmxTools.getLongAttr(server, grpName, "bytesSent"));
        connector.setRequestCount(JmxTools.getIntAttr(server, grpName, "requestCount"));
        connector.setErrorCount(JmxTools.getIntAttr(server, grpName, "errorCount"));

        if (includeRequestProcessors) {
          List<ObjectName> wrkNames = threadPoolObjectName.getRequestProcessorNames();
          for (ObjectName wrkName : wrkNames) {
            try {
              RequestProcessor rp = new RequestProcessor();
              rp.setName(wrkName.getKeyProperty("name"));
              rp.setStage(JmxTools.getIntAttr(server, wrkName, "stage"));
              rp.setProcessingTime(JmxTools.getLongAttr(server, wrkName, "requestProcessingTime"));
              rp.setBytesSent(JmxTools.getLongAttr(server, wrkName, "requestBytesSent"));
              rp.setBytesReceived(JmxTools.getLongAttr(server, wrkName, "requestBytesReceived"));
              try {
                rp.setRemoteAddr(JmxTools.getStringAttr(server, wrkName, "remoteAddr"));
              } catch (RuntimeOperationsException ex) {
                logger.trace("", ex);
              }

              if (rp.getRemoteAddr() != null) {
                // Show flag as defined in jvm for localhost
                if (InetAddress.getByName(rp.getRemoteAddr()).isLoopbackAddress()) {
                  rp.setRemoteAddrLocale(new Locale(System.getProperty("user.language"),
                      System.getProperty("user.country")));
                } else {
                  // Show flag for non-localhost using geo lite
                  try (DatabaseReader reader = new DatabaseReader.Builder(new File(
                      getClass().getClassLoader().getResource("GeoLite2-Country.mmdb").toURI()))
                      .withCache(new CHMCache()).build()) {
                    CountryResponse response =
                        reader.country(InetAddress.getByName(rp.getRemoteAddr()));
                    Country country = response.getCountry();
                    rp.setRemoteAddrLocale(new Locale("", country.getIsoCode()));
                  } catch (AddressNotFoundException e) {
                    logger.debug("Address Not Found: {}", e.getMessage());
                    logger.trace("", e);
                  }
                }
              }

              rp.setVirtualHost(JmxTools.getStringAttr(server, wrkName, "virtualHost"));
              rp.setMethod(JmxTools.getStringAttr(server, wrkName, "method"));
              rp.setCurrentUri(JmxTools.getStringAttr(server, wrkName, "currentUri"));
              rp.setCurrentQueryString(
                  JmxTools.getStringAttr(server, wrkName, "currentQueryString"));
              rp.setProtocol(JmxTools.getStringAttr(server, wrkName, "protocol"));

              // Relies on https://issues.apache.org/bugzilla/show_bug.cgi?id=41128
              if (workerThreadNameSupported
                  && JmxTools.hasAttribute(server, wrkName, "workerThreadName")) {

                rp.setWorkerThreadName(JmxTools.getStringAttr(server, wrkName, "workerThreadName"));
                rp.setWorkerThreadNameSupported(true);
              } else {
                /*
                 * attribute should consistently either exist or be missing across all the workers
                 * so it does not make sense to check attribute existence if we have found once that
                 * it is not supported
                 */
                rp.setWorkerThreadNameSupported(false);
                workerThreadNameSupported = false;
              }
              connector.addRequestProcessor(rp);
            } catch (InstanceNotFoundException e) {
              logger.info("Failed to query RequestProcessor {}", wrkName);
              logger.debug("", e);
            }
          }
        }

        connectors.add(connector);
      } catch (InstanceNotFoundException e) {
        logger.error("Failed to query entire thread pool {}", threadPoolObjectName);
        logger.debug("  Stack trace:", e);
      }
    }
    return connectors;
  }

}
