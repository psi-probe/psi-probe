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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.naming.NamingException;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.util.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import psiprobe.TomcatContainer;
import psiprobe.model.ApplicationResource;

/**
 * This class wires support for Tomcat "privileged" context functionality into Spring. If
 * application context is privileged Tomcat would always call servlet.setWrapper method on each
 * request. ContainerWrapperBean wires the passed wrapper to the relevant Tomcat container adapter
 * class, which in turn helps the Probe to interpret the wrapper.
 */
public class ContainerWrapperBean {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ContainerWrapperBean.class);

  /** The tomcat container. */
  private volatile TomcatContainer tomcatContainer;

  /** The lock. */
  private final Object lock = new Object();

  /** List of class names to adapt particular Tomcat implementation to TomcatContainer interface. */
  @Inject
  private List<String> adapterClasses;

  /** The resource resolver. */
  private ResourceResolver resourceResolver;

  /** The force first adapter. */
  private boolean forceFirstAdapter;

  /** The resource resolvers. */
  @Inject
  private Map<String, ResourceResolver> resourceResolvers;

  /**
   * Checks if is force first adapter.
   *
   * @return true, if is force first adapter
   */
  public boolean isForceFirstAdapter() {
    return forceFirstAdapter;
  }

  /**
   * Sets the force first adapter. Setting this property to true will override the server polling
   * each adapter performs to test for compatibility. Instead, it will use the first one in the
   * adapterClasses list.
   *
   * @param forceFirstAdapter the new force first adapter
   */
  // TODO We should make this configurable
  @Value("false")
  public void setForceFirstAdapter(boolean forceFirstAdapter) {
    this.forceFirstAdapter = forceFirstAdapter;
  }

  /**
   * Sets the wrapper.
   *
   * @param wrapper the new wrapper
   */
  public void setWrapper(Wrapper wrapper) {
    if (tomcatContainer == null) {

      synchronized (lock) {

        if (tomcatContainer == null) {

          String serverInfo = ServerInfo.getServerInfo();
          logger.info("Server info: {}", serverInfo);
          for (String className : adapterClasses) {
            try {
              Object obj = Class.forName(className).getDeclaredConstructor().newInstance();
              logger.debug("Testing container adapter: {}", className);
              if (obj instanceof TomcatContainer) {
                if (forceFirstAdapter || ((TomcatContainer) obj).canBoundTo(serverInfo)) {
                  logger.info("Using {}", className);
                  tomcatContainer = (TomcatContainer) obj;
                  tomcatContainer.setWrapper(wrapper);
                  break;
                }
                logger.debug("Cannot bind {} to {}", className, serverInfo);
              } else {
                logger.error("{} does not implement {}", className,
                    TomcatContainer.class.getName());
              }
            } catch (Exception e) {
              logger.debug("", e);
              logger.info("Failed to load {}", className);
            }
          }

          if (tomcatContainer == null) {
            logger.error("No suitable container adapter found!");
          }
        }
      }
    }

    try {
      if (tomcatContainer != null && wrapper == null) {
        logger.info("Unregistering container adapter");
        tomcatContainer.setWrapper(null);
      }
    } catch (Exception e) {
      logger.error("Could not unregister container adapter", e);
    }
  }

  /**
   * Gets the tomcat container.
   *
   * @return the tomcat container
   */
  public TomcatContainer getTomcatContainer() {
    return tomcatContainer;
  }

  /**
   * Gets the adapter classes.
   *
   * @return the adapter classes
   */
  public List<String> getAdapterClasses() {
    return adapterClasses;
  }

  /**
   * Sets the adapter classes.
   *
   * @param adapterClasses the new adapter classes
   */
  public void setAdapterClasses(List<String> adapterClasses) {
    this.adapterClasses = adapterClasses;
  }

  /**
   * Gets the resource resolver.
   *
   * @return the resource resolver
   */
  public ResourceResolver getResourceResolver() {
    if (resourceResolver == null) {
      if (System.getProperty("jboss.server.name") != null) {
        resourceResolver = resourceResolvers.get("jboss");
        logger.info("Using JBOSS resource resolver");
      } else {
        resourceResolver = resourceResolvers.get("default");
        logger.info("Using DEFAULT resource resolver");
      }
    }
    return resourceResolver;
  }

  /**
   * Gets the resource resolvers.
   *
   * @return the resource resolvers
   */
  public Map<String, ResourceResolver> getResourceResolvers() {
    return resourceResolvers;
  }

  /**
   * Sets the resource resolvers.
   *
   * @param resourceResolvers the resource resolvers
   */
  public void setResourceResolvers(Map<String, ResourceResolver> resourceResolvers) {
    this.resourceResolvers = resourceResolvers;
  }

  /**
   * Gets the data sources.
   *
   * @return the data sources
   *
   * @throws NamingException the naming exception
   */
  public List<ApplicationResource> getDataSources() throws NamingException {
    List<ApplicationResource> resources = new ArrayList<>(getPrivateDataSources());
    resources.addAll(getGlobalDataSources());
    return resources;
  }

  /**
   * Gets the private data sources.
   *
   * @return the private data sources
   *
   * @throws NamingException the naming exception
   */
  public List<ApplicationResource> getPrivateDataSources() throws NamingException {
    List<ApplicationResource> resources = new ArrayList<>();
    if (tomcatContainer != null && getResourceResolver().supportsPrivateResources()) {
      for (Context app : getTomcatContainer().findContexts()) {
        List<ApplicationResource> appResources =
            getResourceResolver().getApplicationResources(app, this);
        // add only those resources that have data source info
        filterDataSources(appResources, resources);
      }
    }
    return resources;
  }

  /**
   * Gets the global data sources.
   *
   * @return the global data sources
   *
   * @throws NamingException the naming exception
   */
  public List<ApplicationResource> getGlobalDataSources() throws NamingException {
    List<ApplicationResource> resources = new ArrayList<>();
    if (getResourceResolver().supportsGlobalResources()) {
      List<ApplicationResource> globalResources = getResourceResolver().getApplicationResources();
      // add only those resources that have data source info
      filterDataSources(globalResources, resources);
    }
    return resources;
  }

  /**
   * Filter data sources.
   *
   * @param resources the resources
   * @param dataSources the data sources
   */
  protected void filterDataSources(Iterable<ApplicationResource> resources,
      Collection<ApplicationResource> dataSources) {

    for (ApplicationResource res : resources) {
      if (res.getDataSourceInfo() != null) {
        dataSources.add(res);
      }
    }
  }

}
