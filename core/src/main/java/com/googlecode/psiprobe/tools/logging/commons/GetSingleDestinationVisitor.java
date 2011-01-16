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

import com.googlecode.psiprobe.tools.logging.LogDestination;
import com.googlecode.psiprobe.tools.logging.jdk.Jdk14LoggerAccessor;
import com.googlecode.psiprobe.tools.logging.log4j.Log4JLoggerAccessor;

/**
 *
 * @author Mark Lewis
 */
public class GetSingleDestinationVisitor extends LoggerAccessorVisitor {

    private String logIndex;
    private LogDestination destination;

    public GetSingleDestinationVisitor(String logIndex) {
        this.logIndex = logIndex;
    }

    public LogDestination getDestination() {
        return destination;
    }

    public void visit(Log4JLoggerAccessor accessor) {
        LogDestination dest = accessor.getAppender(logIndex);
        if (dest != null) {
            destination = dest;
        }
    }

    public void visit(Jdk14LoggerAccessor accessor) {
        LogDestination dest = accessor.getHandler(logIndex);
        if (dest != null) {
            destination = dest;
        }
    }

}
