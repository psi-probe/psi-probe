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
package psiprobe.tools;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Session;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardWrapper;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import psiprobe.beans.ContainerWrapperBean;
import psiprobe.beans.ResourceResolver;
import psiprobe.model.Application;
import psiprobe.model.ApplicationParam;
import psiprobe.model.ApplicationResource;
import psiprobe.model.ApplicationSession;
import psiprobe.model.Attribute;
import psiprobe.model.FilterInfo;
import psiprobe.model.ServletInfo;
import psiprobe.model.ServletMapping;

/**
 * The Class ApplicationUtils.
 */
public final class ApplicationUtils {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ApplicationUtils.class);

  /**
   * Prevent Instantiation.
   */
  private ApplicationUtils() {
    // Prevent Instantiation
  }

  /**
   * Gets the application.
   *
   * @param context the context
   * @param containerWrapper the container wrapper
   *
   * @return the application
   */
  public static Application getApplication(Context context, ContainerWrapperBean containerWrapper) {
    return getApplication(context, null, false, containerWrapper);
  }

  /**
   * Creates Application instance from Tomcat Context object. If ResourceResolver is passed the
   * method will also collect additional information about the application such as session count,
   * session attribute count, application attribute count, servlet count, servlet stats summary and
   * datasource usage summary. Collecting additional information can be CPU intensive and time
   * consuming so this should be avoided unless absolutely required. Some datasource implementations
   * (c3p0) are known to be prone to internal deadlocks, so this method can also hang is datasource
   * usage stats is to be collected.
   *
   * @param context the context from which to create the Application
   * @param resourceResolver the resolver to use for resources associated with the given context
   * @param calcSize flag which controls whether to calculate session size
   * @param containerWrapper the wrapper for the context's root containing server
   *
   * @return Application object
   */
  public static Application getApplication(Context context, ResourceResolver resourceResolver,
      boolean calcSize, ContainerWrapperBean containerWrapper) {

    // ContainerWrapperBean containerWrapper
    logger.debug("Querying webapp: {}", context.getName());

    Application app = new Application();
    app.setName(context.getName().length() > 0 ? context.getName() : "/");
    app.setDocBase(context.getDocBase());
    app.setDisplayName(context.getDisplayName());

    app.setAvailable(containerWrapper.getTomcatContainer().getAvailable(context));
    app.setDistributable(context.getDistributable());
    app.setSessionTimeout(context.getSessionTimeout());
    app.setServletVersion(context.getServletContext().getMajorVersion() + "."
        + context.getServletContext().getMinorVersion());

    if (resourceResolver != null) {
      logger.debug("counting servlet attributes");

      app.setContextAttributeCount(
          Collections.list(context.getServletContext().getAttributeNames()).size());

      if (app.isAvailable()) {
        logger.debug("collecting session information");

        app.setSessionCount(context.getManager().findSessions().length);

        boolean serializable = true;
        long sessionAttributeCount = 0;
        long size = 0;

        for (Session session : context.getManager().findSessions()) {
          ApplicationSession appSession = getApplicationSession(session, calcSize, false);
          if (appSession != null) {
            sessionAttributeCount += appSession.getObjectCount();
            serializable = serializable && appSession.isSerializable();
            size += appSession.getSize();
          }
        }
        app.setSerializable(serializable);
        app.setSessionAttributeCount(sessionAttributeCount);
        app.setSize(size);
      }

      logger.debug("aggregating servlet stats");

      collectApplicationServletStats(context, app);

      if (resourceResolver.supportsPrivateResources() && app.isAvailable()) {
        int[] scores =
            getApplicationDataSourceUsageScores(context, resourceResolver, containerWrapper);
        app.setDataSourceBusyScore(scores[0]);
        app.setDataSourceEstablishedScore(scores[1]);
      }
    }

    return app;
  }

  /**
   * Calculates Sum of requestCount, errorCount and processingTime for all servlets for the given
   * application. It also works out minimum value of minTime and maximum value for maxTime for all
   * servlets.
   *
   * @param context the context whose stats will be collected
   * @param app the application in which to store the collected stats
   */
  public static void collectApplicationServletStats(Context context, Application app) {
    int svltCount = 0;
    long reqCount = 0;
    long errCount = 0;
    long procTime = 0;
    long minTime = Long.MAX_VALUE;
    long maxTime = 0;

    for (Container container : context.findChildren()) {
      if (container instanceof StandardWrapper) {
        StandardWrapper sw = (StandardWrapper) container;
        svltCount++;

        // Get Request Count (bridge between tomcat 9/10 and 11 using int vs long
        Object requestCount = null;
        try {
          requestCount = MethodUtils.invokeMethod(sw, "getRequestCount");
          if (requestCount instanceof Long) {
            // tomcat 11+
            reqCount += (long) requestCount;
          } else {
            // tomcat 9/10
            reqCount += (int) requestCount;
          }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
          logger.error("Unable to find getRequestCount", e);
        }

        // Get Error Count (bridge between tomcat 10 and 11 using int vs long
        try {
          Object errorCount = MethodUtils.invokeMethod(sw, "getErrorCount");
          if (errorCount instanceof Long) {
            // Tomcat 11+
            errCount += (long) errorCount;
          } else {
            // Tomcat 9/10
            errCount += (int) errorCount;
          }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
          logger.error("Unable to find getErrorCount", e);
        }

        procTime += sw.getProcessingTime();
        if (requestCount != null) {
          if (requestCount instanceof Long && (long) requestCount > 0) {
            // Tomcat 11+
            minTime = Math.min(minTime, sw.getMinTime());
          } else if (requestCount instanceof Integer && (int) requestCount > 0) {
            // Tomcat 9/10
            minTime = Math.min(minTime, sw.getMinTime());
          }
        }
        maxTime = Math.max(maxTime, sw.getMaxTime());
      }
    }
    app.setServletCount(svltCount);
    app.setRequestCount(reqCount);
    app.setErrorCount(errCount);
    app.setProcessingTime(procTime);
    app.setMinTime(minTime == Long.MAX_VALUE ? 0 : minTime);
    app.setMaxTime(maxTime);
  }

  /**
   * Gets the application data source usage scores.
   *
   * @param context the context
   * @param resolver the resolver
   * @param containerWrapper the container wrapper
   *
   * @return the application data source usage scores
   */
  public static int[] getApplicationDataSourceUsageScores(Context context,
      ResourceResolver resolver, ContainerWrapperBean containerWrapper) {

    logger.debug("Calculating datasource usage score");

    int[] scores = {0, 0};
    List<ApplicationResource> appResources;
    try {
      appResources = resolver.getApplicationResources(context, containerWrapper);
    } catch (NamingException e) {
      throw new RuntimeException(e);
    }
    for (ApplicationResource appResource : appResources) {
      if (appResource.getDataSourceInfo() != null) {
        scores[0] = Math.max(scores[0], appResource.getDataSourceInfo().getBusyScore());
        scores[1] = Math.max(scores[1], appResource.getDataSourceInfo().getEstablishedScore());
      }
    }
    return scores;
  }

  /**
   * Gets the application session.
   *
   * @param session the session
   * @param calcSize the calc size
   * @param addAttributes the add attributes
   *
   * @return the application session
   */
  public static ApplicationSession getApplicationSession(Session session, boolean calcSize,
      boolean addAttributes) {

    ApplicationSession sbean = null;
    if (session != null && session.isValid()) {
      sbean = new ApplicationSession();

      sbean.setId(session.getId());
      sbean.setCreationTime(new Date(session.getCreationTime()));
      sbean.setLastAccessTime(new Date(session.getLastAccessedTime()));
      sbean.setMaxIdleTime(session.getMaxInactiveInterval() * 1000);
      sbean.setManagerType(session.getManager().getClass().getName());
      sbean.setInfo(session.getClass().getSimpleName());

      boolean sessionSerializable = true;
      int attributeCount = 0;
      long size = 0;

      HttpSession httpSession = session.getSession();
      Set<Object> processedObjects = new HashSet<>(1000);

      // Exclude references back to the session itself
      processedObjects.add(httpSession);
      try {
        for (String name : Collections.list(httpSession.getAttributeNames())) {
          Object obj = httpSession.getAttribute(name);
          sessionSerializable = sessionSerializable && obj instanceof Serializable;

          long objSize = 0;
          if (calcSize) {
            try {
              objSize += Instruments.sizeOf(name, processedObjects);
              objSize += Instruments.sizeOf(obj, processedObjects);
            } catch (Exception ex) {
              logger.error("Cannot estimate size of attribute '{}'", name, ex);
            }
          }

          if (addAttributes) {
            Attribute saBean = new Attribute();
            saBean.setName(name);
            saBean.setType(ClassUtils.getQualifiedName(obj.getClass()));
            saBean.setValue(obj);
            saBean.setSize(objSize);
            saBean.setSerializable(obj instanceof Serializable);
            sbean.addAttribute(saBean);
          }
          attributeCount++;
          size += objSize;
        }
        String lastAccessedIp =
            (String) httpSession.getAttribute(ApplicationSession.LAST_ACCESSED_BY_IP);
        if (lastAccessedIp != null) {
          sbean.setLastAccessedIp(lastAccessedIp);
          sbean.setLastAccessedIpLocale(
              (Locale) httpSession.getAttribute(ApplicationSession.LAST_ACCESSED_LOCALE));
        }

      } catch (IllegalStateException e) {
        logger.info("Session appears to be invalidated, ignore");
        logger.trace("", e);
      }

      sbean.setObjectCount(attributeCount);
      sbean.setSize(size);
      sbean.setSerializable(sessionSerializable);
    }

    return sbean;
  }

  /**
   * Gets the application attributes.
   *
   * @param context the context
   *
   * @return the application attributes
   */
  public static List<Attribute> getApplicationAttributes(Context context) {
    List<Attribute> attrs = new ArrayList<>();
    ServletContext servletCtx = context.getServletContext();
    for (String attrName : Collections.list(servletCtx.getAttributeNames())) {
      Object attrValue = servletCtx.getAttribute(attrName);

      Attribute attr = new Attribute();
      attr.setName(attrName);
      attr.setValue(attrValue);
      attr.setType(ClassUtils.getQualifiedName(attrValue.getClass()));
      attrs.add(attr);
    }
    return attrs;
  }

  /**
   * Gets the application init params.
   *
   * @param context the context
   * @param containerWrapper the container wrapper
   *
   * @return the application init params
   */
  public static List<ApplicationParam> getApplicationInitParams(Context context,
      ContainerWrapperBean containerWrapper) {

    return containerWrapper.getTomcatContainer().getApplicationInitParams(context);
  }

  /**
   * Gets the application servlet.
   *
   * @param context the context
   * @param servletName the servlet name
   *
   * @return the application servlet
   */
  public static ServletInfo getApplicationServlet(Context context, String servletName) {
    Container container = context.findChild(servletName);

    if (container instanceof Wrapper) {
      Wrapper wrapper = (Wrapper) container;
      return getServletInfo(wrapper, context.getName());
    }
    return null;
  }

  /**
   * Gets the servlet info.
   *
   * @param wrapper the wrapper
   * @param contextName the context name
   *
   * @return the servlet info
   */
  private static ServletInfo getServletInfo(Wrapper wrapper, String contextName) {
    ServletInfo si = new ServletInfo();
    si.setApplicationName(contextName.length() > 0 ? contextName : "/");
    si.setServletName(wrapper.getName());
    si.setServletClass(wrapper.getServletClass());
    si.setAvailable(!wrapper.isUnavailable());
    si.setLoadOnStartup(wrapper.getLoadOnStartup());
    si.setRunAs(wrapper.getRunAs());
    si.getMappings().addAll(Arrays.asList(wrapper.findMappings()));
    if (wrapper instanceof StandardWrapper) {
      StandardWrapper sw = (StandardWrapper) wrapper;
      si.setAllocationCount(sw.getCountAllocated());

      // Get Error Count (bridge between tomcat 9/10 and 11 using int vs long
      try {
        Object errorCount = MethodUtils.invokeMethod(sw, "getErrorCount");
        if (errorCount instanceof Long) {
          // Tomcat 11+
          si.setErrorCount((long) errorCount);
        } else {
          // Tomcat 9/10
          si.setErrorCount((int) errorCount);
        }
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        logger.error("Unable to find getErrorCount", e);
      }

      si.setLoadTime(sw.getLoadTime());
      // XXX: Remove with tomcat 10.1
      si.setMaxInstances(sw.getMaxInstances());
      si.setMaxTime(sw.getMaxTime());
      si.setMinTime(sw.getMinTime() == Long.MAX_VALUE ? 0 : sw.getMinTime());
      si.setProcessingTime(sw.getProcessingTime());

      // Get Request Count (bridge between tomcat 9/10 and 11 using int vs long
      try {
        Object requestCount = MethodUtils.invokeMethod(sw, "getRequestCount");
        if (requestCount instanceof Long) {
          // Tomcat 11+
          si.setRequestCount((long) requestCount);
        } else {
          // Tomcat 9/10
          si.setRequestCount((int) requestCount);
        }
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        logger.error("Unable to find getRequestCount", e);
      }

      // XXX: Remove with tomcat 10.1
      si.setSingleThreaded(Boolean.TRUE.equals(sw.isSingleThreadModel()));
    }
    return si;
  }

  /**
   * Gets the application servlets.
   *
   * @param context the context
   *
   * @return the application servlets
   */
  public static List<ServletInfo> getApplicationServlets(Context context) {
    Container[] cns = context.findChildren();
    List<ServletInfo> servlets = new ArrayList<>(cns.length);
    for (Container container : cns) {
      if (container instanceof Wrapper) {
        Wrapper wrapper = (Wrapper) container;
        servlets.add(getServletInfo(wrapper, context.getName()));
      }
    }
    return servlets;
  }

  /**
   * Gets the application servlet maps.
   *
   * @param context the context
   *
   * @return the application servlet maps
   */
  public static List<ServletMapping> getApplicationServletMaps(Context context) {
    String[] sms = context.findServletMappings();
    List<ServletMapping> servletMaps = new ArrayList<>(sms.length);
    for (String servletMapping : sms) {
      if (servletMapping != null) {
        String sn = context.findServletMapping(servletMapping);
        if (sn != null) {
          ServletMapping sm = new ServletMapping();
          sm.setApplicationName(context.getName().length() > 0 ? context.getName() : "/");
          sm.setUrl(servletMapping);
          sm.setServletName(sn);
          Container container = context.findChild(sn);
          if (container instanceof Wrapper) {
            Wrapper wrapper = (Wrapper) container;
            sm.setServletClass(wrapper.getServletClass());
            sm.setAvailable(!wrapper.isUnavailable());
          }
          servletMaps.add(sm);
        }
      }
    }
    return servletMaps;
  }

  /**
   * Gets the application filters.
   *
   * @param context the context
   * @param containerWrapper the container wrapper
   *
   * @return the application filters
   */
  public static List<FilterInfo> getApplicationFilters(Context context,
      ContainerWrapperBean containerWrapper) {
    return containerWrapper.getTomcatContainer().getApplicationFilters(context);
  }

}
