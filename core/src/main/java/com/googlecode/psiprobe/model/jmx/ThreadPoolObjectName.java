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
package com.googlecode.psiprobe.model.jmx;

import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;

/**
 * This bean represens a hierarchy of Tomcat's ObjectNames necessary to display
 * real-time connection information on "status" tab.
 *
 * @author Vlad Ilyushchenko
 */
public class ThreadPoolObjectName  {

    private ObjectName threadPoolName;
    private ObjectName globalRequestProcessorName;
    private List requestProcessorNames = new ArrayList();

    public ObjectName getThreadPoolName() {
        return threadPoolName;
    }

    public ObjectName getGlobalRequestProcessorName() {
        return globalRequestProcessorName;
    }

    public List getRequestProcessorNames() {
        return requestProcessorNames;
    }

    public void setThreadPoolName(ObjectName threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    public void setGlobalRequestProcessorName(ObjectName globalRequestProcessorName) {
        this.globalRequestProcessorName = globalRequestProcessorName;
    }

    public void setRequestProcessorNames(List requestProcessorNames) {
        this.requestProcessorNames = requestProcessorNames;
    }
}
