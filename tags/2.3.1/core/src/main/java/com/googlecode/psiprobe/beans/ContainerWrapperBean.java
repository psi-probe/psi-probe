/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.beans;

import com.googlecode.psiprobe.TomcatContainer;
import com.googlecode.psiprobe.model.ApplicationResource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.util.ServerInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class wires support for Tomcat "privileged" context functionality into
 * Spring. If application context is privileged Tomcat would always call
 * servlet.setWrapper method on each request. ContainerWrapperBean wires the
 * passed wrapper to the relevant Tomcat container adaptor class, which in turn
 * helps the Probe to interpret the wrapper. Container adaptors are required
 * because internal wrapper structure is quite different between Tomcat 5.5.x
 * and Tomcat 5.0.x
 * 
 * @author Vlad Ilyushchenko
 */
public class ContainerWrapperBean {

    private Log logger = LogFactory.getLog(getClass());

    private TomcatContainer tomcatContainer = null;
    private final Object lock = new Object();

    /**
     * List of class names to adapt particular Tomcat implementation to TomcatContainer interface
     */
    private List adaptorClasses;

    private ResourceResolver resourceResolver;

    private boolean forceFirstAdaptor = false;

    private Map resourceResolvers;

    public boolean isForceFirstAdaptor() {
        return forceFirstAdaptor;
    }

    public void setForceFirstAdaptor(boolean forceFirstAdaptor) {
        this.forceFirstAdaptor = forceFirstAdaptor;
    }

    public void setWrapper(Wrapper wrapper) {
        if (tomcatContainer == null) {

            synchronized (lock) {

                if (tomcatContainer == null) {

                    String serverInfo = ServerInfo.getServerInfo();
                    logger.info("Server info: " + serverInfo);
                    for (int i = 0; i < adaptorClasses.size(); i++) {
                        String className = (String) adaptorClasses.get(i);
                        try {
                            Object o = Class.forName(className).newInstance();
                            logger.debug("Testing container adaptor: " + className);
                            if (o instanceof TomcatContainer) {
                                if (forceFirstAdaptor || ((TomcatContainer) o).canBoundTo(serverInfo)) {
                                    logger.info("Using " + className);
                                    tomcatContainer = (TomcatContainer) o;
                                    tomcatContainer.setWrapper(wrapper);
                                    break;
                                } else {
                                    logger.debug("Cannot bind " + className + " to " + serverInfo);
                                }
                            } else {
                                logger.error(className + " does not implement " + TomcatContainer.class.getName());
                            }
                        } catch (Throwable e) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Failed to load " + className, e);
                            } else {
                                logger.info("Failed to load " + className);
                            }
                            //
                            // make sure we always re-throw ThreadDeath
                            //
                            if (e instanceof ThreadDeath) {
                                throw (ThreadDeath) e;
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

    public TomcatContainer getTomcatContainer() {
        return tomcatContainer;
    }

    public List getAdaptorClasses() {
        return adaptorClasses;
    }

    public void setAdaptorClasses(List adaptorClasses) {
        this.adaptorClasses = adaptorClasses;
    }

    public ResourceResolver getResourceResolver() {
        if (resourceResolver == null) {
            if (System.getProperty("jboss.server.name") != null) {
                resourceResolver = (ResourceResolver) resourceResolvers.get("jboss");
                logger.info("Using JBOSS resource resolver");
            } else {
                resourceResolver = (ResourceResolver) resourceResolvers.get("default");
                logger.info("Using DEFAULT resource resolver");
            }
        }
        return resourceResolver;
    }

    public Map getResourceResolvers() {
        return resourceResolvers;
    }

    public void setResourceResolvers(Map resourceResolvers) {
        this.resourceResolvers = resourceResolvers;
    }

    public List getDataSources() throws Exception {
        List resources = new ArrayList();
        resources.addAll(getPrivateDataSources());
        resources.addAll(getGlobalDataSources());
        return resources;
    }

    public List getPrivateDataSources() throws Exception {
        List resources = new ArrayList();
        if (tomcatContainer != null && getResourceResolver().supportsPrivateResources()) {
            List apps = getTomcatContainer().findContexts();

            for (int i = 0; i < apps.size(); i++) {
                List appResources = getResourceResolver().getApplicationResources((Context) apps.get(i));
                // add only those resources that have data source info
                filterDataSources(appResources, resources);
            }
        }
        return resources;
    }

    public List getGlobalDataSources() throws Exception {
        List resources = new ArrayList();
        if (getResourceResolver().supportsGlobalResources()) {
            List globalResources = getResourceResolver().getApplicationResources();
            // add only those resources that have data source info
            filterDataSources(globalResources, resources);
        }
        return resources;
    }

    protected void filterDataSources(List resources, List dataSources) {
        for (Iterator it = resources.iterator(); it.hasNext(); ) {
            ApplicationResource res = (ApplicationResource) it.next();
            if (res.getDataSourceInfo() != null) {
                dataSources.add(res);
            }
        }
    }

}
