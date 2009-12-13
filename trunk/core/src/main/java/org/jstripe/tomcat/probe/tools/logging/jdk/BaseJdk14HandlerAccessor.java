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
package org.jstripe.tomcat.probe.tools.logging.jdk;

import org.jstripe.tomcat.probe.tools.logging.DefaultAccessor;
import org.jstripe.tomcat.probe.tools.logging.LogDestination;

public abstract class BaseJdk14HandlerAccessor extends DefaultAccessor implements LogDestination {

    private Object logger;

    public Object getLogger() {
        return logger;
    }

    public void setLogger(Object logger) {
        this.logger = logger;
    }

    public String getType() {
        return getTarget().getClass().getName();
    }
}
