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

import com.googlecode.psiprobe.controllers.TomcatContainerController;
import com.googlecode.psiprobe.model.java.ThreadModel;
import com.googlecode.psiprobe.tools.Instruments;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.springframework.web.servlet.ModelAndView;

public class ListThreadsController extends TomcatContainerController {
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //
        // create a list of webapp classloaders
        // this will help us to associate threads with applications.
        //
        List contexts = getContainerWrapper().getTomcatContainer().findContexts();
        Map classLoaderMap = new TreeMap();
        for (int i = 0; i < contexts.size(); i++) {
            Context context = (Context) contexts.get(i);
            if (context.getLoader() != null && context.getLoader().getClassLoader() != null) {
                classLoaderMap.put(toUID(context.getLoader().getClassLoader()), context.getName());
            }
        }

        return new ModelAndView(getViewName(), "threads", enumerateThreads(classLoaderMap));
    }

    private List enumerateThreads(final Map classLoaderMap) {

        //
        // get top ThreadGroup
        //
        ThreadGroup masterGroup = Thread.currentThread().getThreadGroup();
        while (masterGroup.getParent() != null) {
            masterGroup = masterGroup.getParent();
        }

        //
        // enumerate all Threads starting from top
        //
        List threadList = new ArrayList();

        Thread[] threads = new Thread[masterGroup.activeCount()];
        int numThreads = masterGroup.enumerate(threads);

        for (int i = 0; i < numThreads; i++) {
            ThreadModel threadModel = new ThreadModel();
            threadModel.setThreadClass(threads[i].getClass().getName());
            threadModel.setName(threads[i].getName());
            threadModel.setPriority(threads[i].getPriority());
            threadModel.setDaemon(threads[i].isDaemon());
            threadModel.setInterrupted(threads[i].isInterrupted());
            if (threads[i].getThreadGroup() != null) {
                threadModel.setGroupName(threads[i].getThreadGroup().getName());
            }
            Object target = Instruments.getField(threads[i], "target");
            if (target != null) {
                threadModel.setRunnableClassName(target.getClass().getName());
            }

            ClassLoader cl = threads[i].getContextClassLoader();
            if (cl != null) {
                if (classLoaderMap != null) {
                    threadModel.setAppName((String) classLoaderMap.get(toUID(cl)));
                }
                threadModel.setClassLoader(toUID(cl));
            }
            threadList.add(threadModel);
        }
        return threadList;
    }

    private static String toUID(Object o) {
        return o.getClass().getName() + "@" + o.hashCode();
    }
}
