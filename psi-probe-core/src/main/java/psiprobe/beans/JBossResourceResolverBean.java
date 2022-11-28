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
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import psiprobe.model.ApplicationResource;
import psiprobe.model.DataSourceInfo;

/**
 * An Adapter to convert information retrieved from JBoss JMX beans into internal resource model.
 */
public class JBossResourceResolverBean implements ResourceResolver {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(JBossResourceResolverBean.class);

  @Override
  public MBeanServer getMBeanServer() {
    for (MBeanServer server : MBeanServerFactory.findMBeanServer(null)) {
      if ("jboss".equals(server.getDefaultDomain())
          || "DefaultDomain".equals(server.getDefaultDomain())) {
        return server;
      }
    }
    return null;
  }

  @Override
  public boolean supportsPrivateResources() {
    return false;
  }

  @Override
  public boolean supportsGlobalResources() {
    return true;
  }

  @Override
  public boolean supportsDataSourceLookup() {
    return false;
  }

  @Override
  public List<ApplicationResource> getApplicationResources() throws NamingException {

    List<ApplicationResource> resources = new ArrayList<>();

    MBeanServer server = getMBeanServer();
    if (server != null) {
      try {
        Set<ObjectName> dsNames =
            server.queryNames(new ObjectName("jboss.jca:service=ManagedConnectionPool,*"), null);
        for (ObjectName managedConnectionPoolOName : dsNames) {
          ApplicationResource resource = new ApplicationResource();
          resource.setName(managedConnectionPoolOName.getKeyProperty("name"));
          resource.setType("jboss");
          String criteria = (String) server.getAttribute(managedConnectionPoolOName, "Criteria");
          if ("ByApplication".equals(criteria)) {
            resource.setAuth("Application");
          } else if ("ByContainerAndApplication".equals(criteria)) {
            resource.setAuth("Both");
          } else {
            resource.setAuth("Container");
          }
          DataSourceInfo dsInfo = new DataSourceInfo();
          dsInfo.setMaxConnections(
              (Integer) server.getAttribute(managedConnectionPoolOName, "MaxSize"));
          dsInfo.setEstablishedConnections(
              (Integer) server.getAttribute(managedConnectionPoolOName, "ConnectionCount"));
          dsInfo.setBusyConnections(
              ((Long) server.getAttribute(managedConnectionPoolOName, "InUseConnectionCount"))
                  .intValue());
          ObjectName connectionFactoryOName = new ObjectName(
              "jboss.jca:service=ManagedConnectionFactory,name=" + resource.getName());
          Element elm = (Element) server.getAttribute(connectionFactoryOName,
              "ManagedConnectionFactoryProperties");

          if (elm != null) {
            NodeList nl = elm.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
              Node node = nl.item(i);
              Node na = node.getAttributes().getNamedItem("name");
              if (na != null) {
                if ("ConnectionURL".equals(na.getNodeValue())) {
                  dsInfo.setJdbcUrl(node.getFirstChild().getNodeValue());
                }

                if ("UserName".equals(na.getNodeValue())) {
                  dsInfo.setUsername(node.getFirstChild().getNodeValue());
                }

                // JMS datasource
                if ("JmsProviderAdapterJNDI".equals(na.getNodeValue())) {
                  dsInfo.setJdbcUrl(node.getFirstChild().getNodeValue());
                  resource.setType("jms");
                }
              }
            }
          }

          dsInfo.setResettable(true);

          resource.setDataSourceInfo(dsInfo);
          resources.add(resource);
        }
      } catch (Exception e) {
        logger.error("There was an error querying JBoss JMX server:", e);
      }
    }
    return resources;
  }

  /**
   * Gets the application resources.
   *
   * @param context the context
   *
   * @return the application resources
   *
   * @throws NamingException the naming exception
   */
  public List<ApplicationResource> getApplicationResources(Context context) throws NamingException {
    return new ArrayList<>();
  }

  @Override
  public List<ApplicationResource> getApplicationResources(Context context,
      ContainerWrapperBean containerWrapper) throws NamingException {

    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean resetResource(Context context, String resourceName,
      ContainerWrapperBean containerWrapper) throws NamingException {
    try {
      ObjectName poolOName =
          new ObjectName("jboss.jca:service=ManagedConnectionPool,name=" + resourceName);
      MBeanServer server = getMBeanServer();
      if (server != null) {
        try {
          server.invoke(poolOName, "stop", null, null);
          server.invoke(poolOName, "start", null, null);
          return true;
        } catch (Exception e) {
          logger.error("Could not reset resource '{}'", resourceName, e);
        }
      }
      return false;
    } catch (MalformedObjectNameException e) {
      logger.trace("", e);
      throw new NamingException(
          "Resource name: \"" + resourceName + "\" makes a malformed ObjectName");
    }
  }

  @Override
  public DataSource lookupDataSource(Context context, String resourceName,
      ContainerWrapperBean containerWrapper) throws NamingException {
    throw new UnsupportedOperationException(
        "This feature has not been implemented for JBoss server yet.");
  }

}
