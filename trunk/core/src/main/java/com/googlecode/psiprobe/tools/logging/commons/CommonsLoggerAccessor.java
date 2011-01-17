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

import com.googlecode.psiprobe.tools.logging.DefaultAccessor;
import com.googlecode.psiprobe.tools.logging.LogDestination;
import java.util.List;

public class CommonsLoggerAccessor extends DefaultAccessor {

    public List getDestinations() {
        GetAllDestinationsVisitor v = new GetAllDestinationsVisitor();
        v.setTarget(getTarget());
        v.setApplication(getApplication());
        v.visit();
        return v.getDestinations();
    }

    public LogDestination getDestination(String logIndex) {
        GetSingleDestinationVisitor v = new GetSingleDestinationVisitor(logIndex);
        v.setTarget(getTarget());
        v.setApplication(getApplication());
        v.visit();
        return v.getDestination();
    }

}
