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
package com.googlecode.psiprobe.tools.logging.commons;

import com.googlecode.psiprobe.tools.Instruments;
import com.googlecode.psiprobe.tools.logging.DefaultAccessor;
import com.googlecode.psiprobe.tools.logging.jdk.Jdk14LoggerAccessor;
import com.googlecode.psiprobe.tools.logging.log4j.Log4JLoggerAccessor;
import java.util.ArrayList;
import java.util.List;

public class CommonsLoggerAccessor extends DefaultAccessor {

    public List getDestinations() {
        Object logger = Instruments.getField(getTarget(), "logger");

        List destinations = new ArrayList();
        
        if (logger != null) {
            if ("org.apache.log4j.Logger".equals(logger.getClass().getName())) {

                while (logger != null) {
                    Log4JLoggerAccessor accessor = new Log4JLoggerAccessor();
                    accessor.setTarget(logger);
                    accessor.setApplication(getApplication());
                    destinations.addAll(accessor.getAppenders());
                    logger = invokeMethod(logger, "getParent", null, null);
                }

            } else if ("java.util.logging.Logger".equals(logger.getClass().getName())) {

                while (logger != null) {
                    Jdk14LoggerAccessor accessor = new Jdk14LoggerAccessor();
                    accessor.setTarget(logger);
                    accessor.setApplication(getApplication());
                    destinations.addAll(accessor.getHandlers());
                    logger = invokeMethod(logger, "getParent", null, null);
                }
            }
        }

        return destinations;
    }

}
