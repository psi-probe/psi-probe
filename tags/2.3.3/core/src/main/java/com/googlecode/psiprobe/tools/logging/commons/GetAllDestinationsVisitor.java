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

import com.googlecode.psiprobe.tools.logging.jdk.Jdk14LoggerAccessor;
import com.googlecode.psiprobe.tools.logging.log4j.Log4JLoggerAccessor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mark Lewis
 */
public class GetAllDestinationsVisitor extends LoggerAccessorVisitor {

    private List destinations = new ArrayList();

    public List getDestinations() {
        return destinations;
    }

    public void visit(Log4JLoggerAccessor accessor) {
        destinations.addAll(accessor.getAppenders());
    }

    public void visit(Jdk14LoggerAccessor accessor) {
        destinations.addAll(accessor.getHandlers());
    }

}
