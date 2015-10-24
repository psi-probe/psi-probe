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

package com.googlecode.psiprobe;

import com.googlecode.psiprobe.model.ApplicationParam;
import com.googlecode.psiprobe.model.ApplicationResource;
import com.googlecode.psiprobe.model.FilterInfo;
import com.googlecode.psiprobe.model.FilterMapping;
import com.googlecode.psiprobe.model.jsp.Summary;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Part of Tomcat container version abstraction layer.
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public interface TomcatContainer {

  /**
   * Find context.
   *
   * @param name the name
   * @return the context
   */
  Context findContext(String name);

  /**
   * Format context name.
   *
   * @param name the name
   * @return the string
   */
  String formatContextName(String name);

  /**
   * Format context filename.
   *
   * @param contextName the context name
   * @return the string
   */
  String formatContextFilename(String contextName);

  /**
   * Find contexts.
   *
   * @return the list
   */
  List<Context> findContexts();

  /**
   * Stop.
   *
   * @param name the name
   * @throws Exception the exception
   */
  void stop(String name) throws Exception;

  /**
   * Start.
   *
   * @param name the name
   * @throws Exception the exception
   */
  void start(String name) throws Exception;

  /**
   * Removes the.
   *
   * @param name the name
   * @throws Exception the exception
   */
  void remove(String name) throws Exception;

  /**
   * Installs .war file at the given context name.
   *
   * @param name the name of the context
   * @param url pointer to .war file to be deployed
   * @throws Exception if installing the .war fails spectacularly
   */
  void installWar(String name, URL url) throws Exception;

  /**
   * This method always returns absolute path, no matter what Tomcat is up to.
   *
   * @return absolute path to applications base (normally "webapps")
   */
  File getAppBase();

  /**
   * Gets the config file.
   *
   * @param ctx the ctx
   * @return the config file
   */
  File getConfigFile(Context ctx);

  /**
   * Gets the config base.
   *
   * @return the config base
   */
  String getConfigBase();

  /**
   * Sets the wrapper.
   *
   * @param wrapper the new wrapper
   */
  void setWrapper(Wrapper wrapper);

  /**
   * Can bound to.
   *
   * @param binding the binding
   * @return true, if successful
   */
  boolean canBoundTo(String binding);

  /**
   * Install context.
   *
   * @param contextName the context name
   * @return true, if successful
   * @throws Exception the exception
   */
  boolean installContext(String contextName) throws Exception;

  /**
   * List context jsps.
   *
   * @param context the context
   * @param summary the summary
   * @param compile the compile
   * @throws Exception the exception
   */
  void listContextJsps(Context context, Summary summary, boolean compile) throws Exception;

  /**
   * Recompile jsps.
   *
   * @param context the context
   * @param summary the summary
   * @param names the names
   */
  void recompileJsps(Context context, Summary summary, List<String> names);

  /**
   * Discard work dir.
   *
   * @param context the context
   */
  void discardWorkDir(Context context);

  /**
   * Gets the logger.
   *
   * @param context the context
   * @return the logger
   */
  Object getLogger(Context context);

  /**
   * Gets the host name.
   *
   * @return the host name
   */
  String getHostName();

  /**
   * Gets the name.
   *
   * @return the name
   */
  String getName();

  /**
   * Gets the servlet file name for jsp.
   *
   * @param context the context
   * @param jspName the jsp name
   * @return the servlet file name for jsp
   */
  String getServletFileNameForJsp(Context context, String jspName);

  /**
   * Gets the application filter maps.
   *
   * @param context the context
   * @return the application filter maps
   */
  List<FilterMapping> getApplicationFilterMaps(Context context);

  /**
   * Gets the available.
   *
   * @param context the context
   * @return the available
   */
  boolean getAvailable(Context context);

  /**
   * Adds the context resource.
   *
   * @param context the context
   * @param resourceList the resource list
   * @param contextBound the context bound
   */
  void addContextResource(Context context, List<ApplicationResource> resourceList,
      boolean contextBound);

  /**
   * Adds the context resource link.
   *
   * @param context the context
   * @param resourceList the resource list
   * @param contextBound the context bound
   */
  void addContextResourceLink(Context context, List<ApplicationResource> resourceList,
      boolean contextBound);

  /**
   * Gets the application filters.
   *
   * @param context the context
   * @return the application filters
   */
  List<FilterInfo> getApplicationFilters(Context context);

  /**
   * Gets the application init params.
   *
   * @param context the context
   * @return the application init params
   */
  List<ApplicationParam> getApplicationInitParams(Context context);

  /**
   * Resource exists.
   *
   * @param name the name
   * @param context the context
   * @return true, if successful
   */
  boolean resourceExists(String name, Context context);

  /**
   * Gets the resource stream.
   *
   * @param name the name
   * @param context the context
   * @return the resource stream
   * @throws IOException Signals that an I/O exception has occurred.
   */
  InputStream getResourceStream(String name, Context context) throws IOException;

  /**
   * Gets the resource attributes.
   *
   * @param name the name
   * @param context the context
   * @return the resource attributes
   */
  Long[] getResourceAttributes(String name, Context context);
}
