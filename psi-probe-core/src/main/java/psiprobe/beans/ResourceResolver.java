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

import java.util.List;

import javax.management.MBeanServer;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;

import psiprobe.model.ApplicationResource;

/**
 * Interface of beans that retrieve information about "resources" of application server. Typically
 * those resources would be datasources.
 */
public interface ResourceResolver {

  /**
   * Standalone Tomcat supports declaration of application-local resources. In that case it makes
   * sense to associate display of resource/datasource information with the owner application. JBoss
   * on other hand can only declare "global" resources, which alters the way resource information is
   * displayed (and accessed).
   *
   * @return true if datasources can be associated with applications, otherwise false.
   *
   * @see #getApplicationResources(Context, ContainerWrapperBean)
   */
  boolean supportsPrivateResources();

  /**
   * Most servlet containers support global resources, but for those that do not, this returns
   * false.
   *
   * @return true if the servlet container supports global resources, otherwise false.
   *
   * @see #getApplicationResources()
   */
  boolean supportsGlobalResources();

  /**
   * Indicates whether this servlet container exposes datasources via
   * {@link #lookupDataSource(Context, String, ContainerWrapperBean) JNDI}.
   *
   * @return true if the servlet container supports datasource lookup
   */
  boolean supportsDataSourceLookup();

  /**
   * Gets the application resources.
   *
   * @return the application resources
   *
   * @throws NamingException the naming exception
   */
  List<ApplicationResource> getApplicationResources() throws NamingException;

  /**
   * Gets the application resources.
   *
   * @param context the context
   * @param containerWrapper the container wrapper
   *
   * @return the application resources
   *
   * @throws NamingException the naming exception
   */
  List<ApplicationResource> getApplicationResources(Context context,
      ContainerWrapperBean containerWrapper) throws NamingException;

  /**
   * Reset resource.
   *
   * @param context the context
   * @param resourceName the resource name
   * @param containerWrapper the container wrapper
   *
   * @return true, if successful
   *
   * @throws NamingException the naming exception
   */
  boolean resetResource(Context context, String resourceName, ContainerWrapperBean containerWrapper)
      throws NamingException;

  /**
   * Lookup data source.
   *
   * @param context the context
   * @param resourceName the resource name
   * @param containerWrapper the container wrapper
   *
   * @return the data source
   *
   * @throws NamingException the naming exception
   */
  DataSource lookupDataSource(Context context, String resourceName,
      ContainerWrapperBean containerWrapper) throws NamingException;

  /**
   * Method that gets {@link MBeanServer} instance that is "default" for the current environment. It
   * is preferably to use this method to locate the "default" {@link MBeanServer} implementation.
   *
   * @return "default" {@link MBeanServer} instance for the current environment
   */
  MBeanServer getMBeanServer();
}
