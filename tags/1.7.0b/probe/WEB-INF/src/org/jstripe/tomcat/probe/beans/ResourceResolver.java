/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://probe.jstripe.com/d/license.shtml
 *
 *  THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 *  WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jstripe.tomcat.probe.beans;

import org.apache.catalina.Context;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.List;

/**
 * Interface of beans that retrieve information about "resources" of application server. Typically those resources would
 * be datasources.
 */
public interface ResourceResolver {

    /**
     * Standalone Tomcat supports declaration of application-local resources. In that case it makes sense to associate
     * display of resource/datasource information with the owner application. JBoss on other hand can only declate "global"
     * resources, which alters the way resource information is displayed (and accessed).
     *
     * @return true if datasources can be associated with applications, otherwise false.
     */
    boolean supportsPrivateResources();

    List getApplicationResources() throws NamingException;

    List getApplicationResources (Context context) throws NamingException;

    boolean resetResource (Context context, String resourceName) throws NamingException;

    DataSource lookupDataSource(Context context, String resourceName) throws NamingException;
}
