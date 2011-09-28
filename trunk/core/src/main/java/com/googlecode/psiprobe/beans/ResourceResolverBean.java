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

import com.googlecode.psiprobe.model.ApplicationResource;
import com.googlecode.psiprobe.model.DataSourceInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.catalina.Context;
import org.apache.catalina.deploy.ContextResource;
import org.apache.catalina.deploy.ContextResourceLink;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.modeler.Registry;
import org.apache.naming.ContextBindings;

public class ResourceResolverBean implements ResourceResolver {

    private Log logger = LogFactory.getLog(getClass());

    /**
     * The default resource prefix for JNDI objects in the global scope:
     * <code>java:</code>.
     */
    public static final String DEFAULT_GLOBAL_RESOURCE_PREFIX = "java:";

    /**
     * The default resource prefix for objects in a private application scope:
     * <code>java:comp/env/</code>.
     */
    public static final String DEFAULT_RESOURCE_PREFIX = DEFAULT_GLOBAL_RESOURCE_PREFIX + "comp/env/";

    private List datasourceMappers;

    public List getApplicationResources() throws NamingException {
        logger.info("Reading GLOBAL resources");
        List resources = new ArrayList();

        MBeanServer server = getMBeanServer();
        if (server != null) {
            try {
                Set dsNames = server.queryNames(new ObjectName("Catalina:type=Resource,resourcetype=Global,*"), null);
                for (Iterator it = dsNames.iterator(); it.hasNext();) {
                    ObjectName objectName = (ObjectName) it.next();
                    ApplicationResource resource = new ApplicationResource();

                    logger.info("reading resource: " + objectName);
                    resource.setName(getStringAttribute(server, objectName, "name"));
                    resource.setType(getStringAttribute(server, objectName, "type"));
                    resource.setScope(getStringAttribute(server, objectName, "scope"));
                    resource.setAuth(getStringAttribute(server, objectName, "auth"));
                    resource.setDescription(getStringAttribute(server, objectName, "description"));

                    lookupResource(resource, true, true);

                    resources.add(resource);
                }
            } catch (Exception e) {
                logger.error("There was an error querying JMX server:", e);
            }
        }
        return resources;
    }

    public synchronized List getApplicationResources(Context context) throws NamingException {

        List resourceList = new ArrayList();

        if (context.getAvailable()) {

            logger.info("Reading CONTEXT " + context.getName());

            boolean contextBound = false;

            try {
                ContextBindings.bindClassLoader(context, null, Thread.currentThread().getContextClassLoader());
                contextBound = true;
            } catch (NamingException e) {
                logger.error("Cannot bind to context. useNaming=false ?");
                logger.debug(e);
            }

            try {
                ContextResource[] resources = context.getNamingResources().findResources();
                for (int i = 0; i < resources.length; i++) {
                    ContextResource contextResource = resources[i];
                    ApplicationResource resource = new ApplicationResource();

                    logger.info("reading resource: " + contextResource.getName());
                    resource.setApplicationName(context.getName());
                    resource.setName(contextResource.getName());
                    resource.setType(contextResource.getType());
                    resource.setScope(contextResource.getScope());
                    resource.setAuth(contextResource.getAuth());
                    resource.setDescription(contextResource.getDescription());

                    lookupResource(resource, contextBound, false);

                    resourceList.add(resource);
                }

                ContextResourceLink[] resourceLinks = context.getNamingResources().findResourceLinks();
                for (int i = 0; i < resourceLinks.length; i++) {
                    ContextResourceLink link = resourceLinks[i];

                    ApplicationResource resource = new ApplicationResource();
                    logger.debug("reading resourceLink: " + link.getName());
                    resource.setApplicationName(context.getName());
                    resource.setName(link.getName());
                    resource.setType(link.getType());
                    resource.setLinkTo(link.getGlobal());

                    lookupResource(resource, contextBound, false);
                    
                    resourceList.add(resource);
                }
            } finally {

                if (contextBound) {
                    ContextBindings.unbindClassLoader(context, null, Thread.currentThread().getContextClassLoader());
                }
            }
        }

        return resourceList;
    }

    public void lookupResource(ApplicationResource resource, boolean contextBound, boolean global) {
        DataSourceInfo dataSourceInfo = null;
        if (contextBound) {
            try {
                String jndiName = resolveJndiName(resource.getName(), global);
                Object o = new InitialContext().lookup(jndiName);
                resource.setLookedUp(true);
                for (Iterator it = datasourceMappers.iterator(); it.hasNext();) {
                    DatasourceAccessor accessor = (DatasourceAccessor) it.next();
                    dataSourceInfo = accessor.getInfo(o);
                    if (dataSourceInfo != null) {
                        break;
                    }
                }

            } catch (Throwable e) {
                resource.setLookedUp(false);
                dataSourceInfo = null;
                logger.error("Failed to lookup: " + resource.getName(), e);
                //
                // make sure we always re-throw ThreadDeath
                //
                if (e instanceof ThreadDeath) {
                    throw (ThreadDeath) e;
                }
            }
        } else {
            resource.setLookedUp(false);
        }

        //
        // Tomcat 5.0.x DBCP datasources would have URL set to null if they incorrectly configured
        // so we need to deal with this little feature
        //
        if (dataSourceInfo != null && dataSourceInfo.getJdbcURL() == null) {
            resource.setLookedUp(false);
        }

        if (resource.isLookedUp() && dataSourceInfo != null) {
            resource.setDataSourceInfo(dataSourceInfo);
        }
    }

    public synchronized boolean resetResource(final Context context, String resourceName) throws NamingException {
        if (context != null) {
            ContextBindings.bindClassLoader(context, null, Thread.currentThread().getContextClassLoader());
        }
        try {
            String jndiName = resolveJndiName(resourceName, (context == null));
            Object o = new InitialContext().lookup(jndiName);
            try {
                for (Iterator it = datasourceMappers.iterator(); it.hasNext();) {
                    DatasourceAccessor accessor = (DatasourceAccessor) it.next();
                    if (accessor.reset(o)) {
                        return true;
                    }
                }
                return false;
            } catch (Throwable e) {
                //
                // make sure we always re-throw ThreadDeath
                //
                if (e instanceof ThreadDeath) {
                    throw (ThreadDeath) e;
                }
                return false;
            }
        } finally {
            if (context != null) {
                ContextBindings.unbindClassLoader(context, null, Thread.currentThread().getContextClassLoader());
            }
        }
    }

    public synchronized DataSource lookupDataSource(final Context context, String resourceName) throws NamingException {
        if (context != null) {
            ContextBindings.bindClassLoader(context, null, Thread.currentThread().getContextClassLoader());
        }
        try {
            String jndiName = resolveJndiName(resourceName, (context == null));
            Object o = new InitialContext().lookup(jndiName);

            if (o instanceof DataSource) {
                return (DataSource) o;
            } else {
                return null;
            }
        } finally {
            if (context != null) {
                ContextBindings.unbindClassLoader(context, null, Thread.currentThread().getContextClassLoader());
            }
        }
    }

    public List getDatasourceMappers() {
        return datasourceMappers;
    }

    public void setDatasourceMappers(List datasourceMappers) {
        this.datasourceMappers = datasourceMappers;
    }

    public boolean supportsPrivateResources() {
        return true;
    }

    public boolean supportsGlobalResources() {
        return true;
    }

    public boolean supportsDataSourceLookup() {
        return true;
    }

    public MBeanServer getMBeanServer() {
        return new Registry().getMBeanServer();
    }

    /**
     * Resolves a JNDI resource name by prepending the scope-appropriate prefix.
     *
     * @param global whether to use the global prefix
     * @param name the JNDI name of the resource
     * 
     * @return the JNDI resource name with the prefix appended
     *
     * @see #DEFAULT_GLOBAL_RESOURCE_PREFIX
     * @see #DEFAULT_RESOURCE_PREFIX
     */
    protected static String resolveJndiName(String name, boolean global) {
        return (global ? DEFAULT_GLOBAL_RESOURCE_PREFIX : DEFAULT_RESOURCE_PREFIX) + name;
    }

    private String getStringAttribute(MBeanServer server, ObjectName objectName, String attributeName) {
        try {
            return (String) server.getAttribute(objectName, attributeName);
        } catch (Exception e) {
            logger.error("Error getting attribute '" + attributeName + "' from '" + objectName + "'", e);
            return null;
        }
    }

}
