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
package com.googlecode.psiprobe.controllers.threads;

import com.googlecode.psiprobe.model.ThreadStackElement;
import com.googlecode.psiprobe.tools.JmxTools;
import java.util.ArrayList;
import java.util.List;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.modeler.Registry;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class ThreadStackController extends ParameterizableViewController {
    private int stackElementCount = 20;

    public int getStackElementCount() {
        return stackElementCount;
    }

    public void setStackElementCount(int stackElementCount) {
        this.stackElementCount = stackElementCount;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        long threadID = ServletRequestUtils.getLongParameter(request, "id", -1);
        String threadName = ServletRequestUtils.getStringParameter(request, "name", null);

        List stack = null;
        MBeanServer mBeanServer = new Registry().getMBeanServer();
        ObjectName threadingOName = new ObjectName("java.lang:type=Threading");


        if (threadID == -1 && threadName != null) {
            // find thread by name
            long[] allIds = (long[]) mBeanServer.getAttribute(threadingOName, "AllThreadIds");
            for (int i = 0; i < allIds.length; i++) {
                CompositeData cd = (CompositeData) mBeanServer.invoke(threadingOName, "getThreadInfo",
                        new Object[]{new Long(allIds[i])}, new String[] {"long"});
                String name = JmxTools.getStringAttr(cd, "threadName");
                if (threadName.equals(name)) {
                    threadID = allIds[i];
                    break;
                }
            }
        }

        if (mBeanServer.queryMBeans(threadingOName, null) != null && threadID != -1) {

            CompositeData cd = (CompositeData) mBeanServer.invoke(threadingOName, "getThreadInfo",
                    new Object[]{new Long(threadID), new Integer(stackElementCount)}, new String[] {"long", "int"});
            if (cd != null) {
                CompositeData[] elements = (CompositeData[]) cd.get("stackTrace");
                threadName = JmxTools.getStringAttr(cd, "threadName");

                stack = new ArrayList(elements.length);

                for (int i = 0; i < elements.length; i++) {
                    CompositeData cd2 = elements[i];
                    ThreadStackElement tse = new ThreadStackElement();
                    tse.setClassName(JmxTools.getStringAttr(cd2, "className"));
                    tse.setFileName(JmxTools.getStringAttr(cd2, "fileName"));
                    tse.setMethodName(JmxTools.getStringAttr(cd2, "methodName"));
                    tse.setLineNumber(JmxTools.getIntAttr(cd2, "lineNumber", -1));
                    tse.setNativeMethod(JmxTools.getBooleanAttr(cd2, "nativeMethod"));
                    stack.add(tse);
                }
            }
        }

        return new ModelAndView(getViewName(), "stack", stack).addObject("threadName", threadName);
    }
}
