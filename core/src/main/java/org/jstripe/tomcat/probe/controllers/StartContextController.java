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
package org.jstripe.tomcat.probe.controllers;

/**
 * Starts a web application.
 *
 * Author: Vlad Ilyushchenko
 */
public class StartContextController extends NoSelfContextHandlerController {

    protected void executeAction(String contextName) throws Exception {
        getContainerWrapper().getTomcatContainer().start(contextName);
    }
}
