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
package com.googlecode.psiprobe;

import com.googlecode.psiprobe.model.jsp.Summary;
import java.io.File;
import java.net.URL;
import java.util.List;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;

/**
 * Part of Tomcat container version abstraction layer.
 *
 * @author Vlad Ilyushchenko
 */

public interface TomcatContainer {

    Context findContext(String name);

    String formatContextName(String name);

    String formatContextFilename(String contextName);

    List findContexts();

    void stop(String name) throws Exception;

    void start(String name) throws Exception;

    void remove(String name) throws Exception;

    /**
     * Installs .war file at the given context name.
     *
     * @param name the name of the context
     * @param url pointer to .war file to be deployed
     * @throws Exception
     */
    void installWar(String name, URL url) throws Exception;

    /**
     * This method always returns absolute path, no matter what Tomcat is up to.
     *
     * @return absolute path to applications base (normally "webapps")
     */
    File getAppBase();

    File getConfigFile(Context ctx);

    String getConfigBase();

    void setWrapper(Wrapper wrapper);

    boolean canBoundTo(String binding);

    boolean installContext(String contextName) throws Exception;

    void listContextJsps(Context context, Summary summary, boolean compile) throws Exception;

    void recompileJsps(Context context, Summary summary, List names);

    void discardWorkDir(Context context);

    Object getLogger(Context context);

    String getHostName();

    String getName();

    String getServletFileNameForJsp(Context context, String jspName);

    List getApplicationFilterMaps(Context context);
}
