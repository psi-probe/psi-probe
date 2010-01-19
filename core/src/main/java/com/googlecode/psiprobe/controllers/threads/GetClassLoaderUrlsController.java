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

import com.googlecode.psiprobe.Utils;
import java.net.URLClassLoader;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class GetClassLoaderUrlsController extends ParameterizableViewController {
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String threadName = ServletRequestUtils.getStringParameter(request, "thread", null);

        Thread thread = Utils.getThreadByName(threadName);

        if (thread != null) {
            ClassLoader cl = thread.getContextClassLoader();
            if (cl != null && cl instanceof URLClassLoader) {
                try {
                    request.setAttribute("urls", Arrays.asList(((URLClassLoader) cl).getURLs()));
                } catch (Exception e) {
                    logger.error("There was an exception querying classloader for thread \"" + threadName + "\"", e);
                }
            }
        }

        return new ModelAndView(getViewName());
    }
}
