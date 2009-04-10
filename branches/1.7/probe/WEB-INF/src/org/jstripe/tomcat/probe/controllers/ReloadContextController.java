/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.jstripe.tomcat.probe.controllers;

import org.apache.catalina.Context;

/**
 * Reloads application context.
 * <p/>
 * Author: Vlad Ilyushchenko
 */
public class ReloadContextController extends NoSelfContextHandlerController {

    protected void executeAction(String contextName) throws Exception {
        Context context = getContainerWrapper().getTomcatContainer().findContext(contextName);
        if (context != null) {
            context.reload();
        }
    }
}
