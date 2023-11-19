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

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.Server;
import org.apache.catalina.core.StandardServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import psiprobe.beans.accessors.DatasourceAccessor;
import psiprobe.model.ApplicationResource;
import psiprobe.model.DataSourceInfo;

/**
 * The Class ResourceResolverBean.
 */
public class ResourceResolverBean implements ResourceResolver {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ResourceResolverBean.class);

  /**
   * The default resource prefix for JNDI objects in the global scope: <code>java:</code>.
   */
  public static final String DEFAULT_GLOBAL_RESOURCE_PREFIX = "";

  /**
   * The default resource prefix for objects in a private application scope:
   * <code>java:comp/env/</code>.
   */
  public static final String DEFAULT_RESOURCE_PREFIX =
      DEFAULT_GLOBAL_RESOURCE_PREFIX + "java:comp/env/";

  /** The datasource mappers. */
  @Inject
  private List<String> datasourceMappers;

  @Override
  public List<ApplicationResource> getApplicationResources() throws NamingException {
    logger.debug("Reading GLOBAL resources");
    List<ApplicationResource> resources = new ArrayList<>();

    MBeanServer server = getMBeanServer();
    if (server != null) {
      try {
        Set<ObjectName> dsNames =
            server.queryNames(new ObjectName("Catalina:type=Resource,resourcetype=Global,*"), null);
        for (ObjectName objectName : dsNames) {
          ApplicationResource resource = new ApplicationResource();

          logger.debug("reading resource: {}", objectName);
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

  @Override
  public synchronized List<ApplicationResource> getApplicationResources(Context context,
      ContainerWrapperBean containerWrapper) throws NamingException {

    List<ApplicationResource> resourceList = new ArrayList<>();

    boolean contextAvailable = containerWrapper.getTomcatContainer().getAvailable(context);
    if (contextAvailable) {

      logger.debug("Reading CONTEXT {}", context.getName());

      boolean contextBound = false;

      try {
        containerWrapper.getTomcatContainer().bindToContext(context);
        contextBound = true;
      } catch (NamingException e) {
        logger.error("Cannot bind to context. useNaming=false ?");
        logger.debug("", e);
      }

      try {
        containerWrapper.getTomcatContainer().addContextResource(context, resourceList);

        containerWrapper.getTomcatContainer().addContextResourceLink(context, resourceList);

        for (ApplicationResource resourceList1 : resourceList) {
          lookupResource(resourceList1, contextBound, false);
        }

      } finally {
        if (contextBound) {
          containerWrapper.getTomcatContainer().unbindFromContext(context);
        }
      }
    }

    return resourceList;
  }

  /**
   * Lookup resource.
   *
   * @param resource the resource
   * @param contextBound the context bound
   * @param global the global
   */
  public void lookupResource(ApplicationResource resource, boolean contextBound, boolean global) {
    DataSourceInfo dataSourceInfo = null;
    if (contextBound) {
      try {
        javax.naming.Context ctx = !global ? new InitialContext() : getGlobalNamingContext();
        if (ctx == null) {
          logger.error(
              "Unable to find context. This may indicate invalid setup. Check global resources versus requested resources");
          resource.setLookedUp(false);
          return;
        }
        String jndiName = resolveJndiName(resource.getName(), global);
        logger.debug("reading resource jndi name: {}", jndiName);
        Object obj = ctx.lookup(jndiName);
        resource.setLookedUp(true);
        for (String accessorString : datasourceMappers) {
          logger.debug("Looking up datasource adapter: {}", accessorString);
          DatasourceAccessor accessor = Class.forName(accessorString)
              .asSubclass(DatasourceAccessor.class).getDeclaredConstructor().newInstance();
          dataSourceInfo = accessor.getInfo(obj);
          if (dataSourceInfo != null) {
            break;
          }
        }
      } catch (Exception e) {
        resource.setLookedUp(false);
        dataSourceInfo = null;
        logger.error("Failed to lookup: '{}'", resource.getName(), e);
      }
    } else {
      resource.setLookedUp(false);
    }

    if (resource.isLookedUp() && dataSourceInfo != null) {
      resource.setDataSourceInfo(dataSourceInfo);
    }
  }

  @Override
  public synchronized boolean resetResource(final Context context, String resourceName,
      ContainerWrapperBean containerWrapper) throws NamingException {

    if (context != null) {
      containerWrapper.getTomcatContainer().bindToContext(context);
    }
    try {
      javax.naming.Context ctx = context != null ? new InitialContext() : getGlobalNamingContext();
      String jndiName = resolveJndiName(resourceName, context == null);
      try {
        for (String accessorString : datasourceMappers) {
          logger.debug("Resetting datasource adapter: {}", accessorString);
          DatasourceAccessor accessor = Class.forName(accessorString)
              .asSubclass(DatasourceAccessor.class).getDeclaredConstructor().newInstance();
          if (ctx == null) {
            return false;
          }
          Object obj = ctx.lookup(jndiName);
          if (accessor.reset(obj)) {
            return true;
          }
        }
        return false;
      } catch (Exception e) {
        logger.trace("", e);
        return false;
      }
    } finally {
      if (context != null) {
        containerWrapper.getTomcatContainer().unbindFromContext(context);
      }
    }
  }

  @Override
  public synchronized DataSource lookupDataSource(final Context context, String resourceName,
      ContainerWrapperBean containerWrapper) throws NamingException {

    if (context != null) {
      containerWrapper.getTomcatContainer().bindToContext(context);
    }
    try {
      javax.naming.Context ctx = context != null ? new InitialContext() : getGlobalNamingContext();
      String jndiName = resolveJndiName(resourceName, context == null);
      if (ctx == null) {
        return null;
      }
      Object obj = ctx.lookup(jndiName);

      if (obj instanceof DataSource) {
        return (DataSource) obj;
      }
      return null;
    } finally {
      if (context != null) {
        containerWrapper.getTomcatContainer().unbindFromContext(context);
      }
    }
  }

  /**
   * Gets the datasource mappers.
   *
   * @return the datasource mappers
   */
  public List<String> getDatasourceMappers() {
    return datasourceMappers;
  }

  /**
   * Sets the datasource mappers.
   *
   * @param datasourceMappers the new datasource mappers
   */
  public void setDatasourceMappers(List<String> datasourceMappers) {
    this.datasourceMappers = datasourceMappers;
  }

  @Override
  public boolean supportsPrivateResources() {
    return true;
  }

  @Override
  public boolean supportsGlobalResources() {
    return true;
  }

  @Override
  public boolean supportsDataSourceLookup() {
    return true;
  }

  @Override
  public MBeanServer getMBeanServer() {
    return ManagementFactory.getPlatformMBeanServer();
  }

  /**
   * Resolves a JNDI resource name by prepending the scope-appropriate prefix.
   *
   * @param name the JNDI name of the resource
   * @param global whether to use the global prefix
   *
   * @return the JNDI resource name with the prefix appended
   *
   * @see #DEFAULT_GLOBAL_RESOURCE_PREFIX
   * @see #DEFAULT_RESOURCE_PREFIX
   */
  protected static String resolveJndiName(String name, boolean global) {
    return (global ? DEFAULT_GLOBAL_RESOURCE_PREFIX : DEFAULT_RESOURCE_PREFIX) + name;
  }

  /**
   * Gets the string attribute.
   *
   * @param server the server
   * @param objectName the object name
   * @param attributeName the attribute name
   *
   * @return the string attribute
   */
  private String getStringAttribute(MBeanServer server, ObjectName objectName,
      String attributeName) {

    try {
      return (String) server.getAttribute(objectName, attributeName);
    } catch (Exception e) {
      logger.error("Error getting attribute '{}' from '{}'", attributeName, objectName, e);
      return null;
    }
  }

  /**
   * Returns the Server's global naming context.
   *
   * @return the global JNDI context
   */
  public static javax.naming.Context getGlobalNamingContext() {

    javax.naming.Context globalContext = null;
    MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
    if (mbeanServer != null) {
      for (String domain : mbeanServer.getDomains()) {

        ObjectName name;
        try {
          name = new ObjectName(domain + ":type=Server");
        } catch (MalformedObjectNameException e) {
          logger.error("", e);
          return null;
        }

        Server server = null;
        try {
          server = (Server) mbeanServer.getAttribute(name, "managedResource");
        } catch (AttributeNotFoundException | InstanceNotFoundException | MBeanException
            | ReflectionException e) {
          logger.trace("JMX objectName {} does not contain any managedResource", name, e);
        }

        // Get Global Naming Context
        if (server instanceof StandardServer) {
          globalContext = server.getGlobalNamingContext();
          break;
        }
      }
    }

    return globalContext;
  }
}
