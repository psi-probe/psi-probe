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
package com.googlecode.psiprobe;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Simple servlet that registeres its startup time in the ServletContext object.
 *
 * @author Vlad Ilyushchenko
 */

public class UptimeServlet extends HttpServlet {
    public void init(ServletConfig servletConfig) throws ServletException {
        servletConfig.getServletContext().setAttribute("UPTIME_START", new Long(System.currentTimeMillis()));
        super.init(servletConfig);
    }
}
