/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package com.googlecode.psiprobe.beans;

import com.googlecode.psiprobe.TomcatContainer;
import com.googlecode.psiprobe.model.ApplicationResource;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.util.ServerInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class wires support for Tomcat "privileged" context functionality into Spring. If
 * application context is privileged Tomcat would always call servlet.setWrapper method on each
 * request. ContainerWrapperBean wires the passed wrapper to the relevant Tomcat container adaptor
 * class, which in turn helps the Probe to interpret the wrapper. Container adaptors are required
 * because internal wrapper structure is quite different between Tomcat 5.5.x and Tomcat 5.0.x
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class ContainerWrapperBean {

  /** The logger. */
  private final Log logger = LogFactory.getLog(getClass());

  /** The tomcat container. */
  private TomcatContainer tomcatContainer = null;
  
  /** The lock. */
  private final Object lock = new Object();

  /** List of class names to adapt particular Tomcat implementation to TomcatContainer interface. */
  private List<String> adaptorClasses;

  /** The resource resolver. */
  private ResourceResolver resourceResolver;

  /** The force first adaptor. */
  private boolean forceFirstAdaptor = false;

  /** The resource resolvers. */
  private Map<String, ResourceResolver> resourceResolvers;

  /**
   * Checks if is force first adaptor.
   *
   * @return true, if is force first adaptor
   */
  public boolean isForceFirstAdaptor() {
    return forceFirstAdaptor;
  }

  /**
   * Sets the force first adaptor.
   *
   * @param forceFirstAdaptor the new force first adaptor
   */
  public void setForceFirstAdaptor(boolean forceFirstAdaptor) {
    this.forceFirstAdaptor = forceFirstAdaptor;
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
          logger.info("Server info: " + serverInfo);
          for (String className : adaptorClasses) {
            try {
              Object obj = Class.forName(className).newInstance();
              logger.debug("Testing container adaptor: " + className);
              if (obj instanceof TomcatContainer) {
                if (forceFirstAdaptor || ((TomcatContainer) obj).canBoundTo(serverInfo)) {
                  logger.info("Using " + className);
                  tomcatContainer = (TomcatContainer) obj;
                  tomcatContainer.setWrapper(wrapper);
                  break;
                } else {
                  logger.debug("Cannot bind " + className + " to " + serverInfo);
                }
              } else {
                logger.error(className + " does not implement " + TomcatContainer.class.getName());
              }
            } catch (Exception e) {
              if (logger.isDebugEnabled()) {
                logger.debug("Failed to load " + className, e);
              } else {
                logger.info("Failed to load " + className);
              }
            }
          }

          if (tomcatContainer == null) {
            logger.fatal("No suitable container adaptor found!");
          }
        }
      }
    }

    try {
      if (tomcatContainer != null && wrapper == null) {
        logger.info("Unregistering container adaptor");
        tomcatContainer.setWrapper(null);
      }
    } catch (Throwable e) {
      logger.error("Could not unregister container adaptor", e);
      //
      // make sure we always re-throw ThreadDeath
      //
      if (e instanceof ThreadDeath) {
        throw (ThreadDeath) e;
      }
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
   * Gets the adaptor classes.
   *
   * @return the adaptor classes
   */
  public List<String> getAdaptorClasses() {
    return adaptorClasses;
  }

  /**
   * Sets the adaptor classes.
   *
   * @param adaptorClasses the new adaptor classes
   */
  public void setAdaptorClasses(List<String> adaptorClasses) {
    this.adaptorClasses = adaptorClasses;
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
   * @throws Exception the exception
   */
  public List<ApplicationResource> getDataSources() throws Exception {
    List<ApplicationResource> resources = new ArrayList<ApplicationResource>();
    resources.addAll(getPrivateDataSources());
    resources.addAll(getGlobalDataSources());
    return resources;
  }

  /**
   * Gets the private data sources.
   *
   * @return the private data sources
   * @throws Exception the exception
   */
  public List<ApplicationResource> getPrivateDataSources() throws Exception {
    List<ApplicationResource> resources = new ArrayList<ApplicationResource>();
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
   * @throws Exception the exception
   */
  public List<ApplicationResource> getGlobalDataSources() throws Exception {
    List<ApplicationResource> resources = new ArrayList<ApplicationResource>();
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
  protected void filterDataSources(List<ApplicationResource> resources,
      List<ApplicationResource> dataSources) {

    for (ApplicationResource res : resources) {
      if (res.getDataSourceInfo() != null) {
        dataSources.add(res);
      }
    }
  }

}
