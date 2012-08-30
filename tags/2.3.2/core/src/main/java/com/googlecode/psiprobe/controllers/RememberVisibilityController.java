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
package com.googlecode.psiprobe.controllers;

import com.googlecode.psiprobe.jsp.Functions;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class RememberVisibilityController extends AbstractController {

    private final SimpleDateFormat sdf = new SimpleDateFormat("E, d-MMM-yyyy HH:mm:ss zz");

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String cookieName = ServletRequestUtils.getStringParameter(request, "cn");
        String state = ServletRequestUtils.getStringParameter(request, "state");
        if (cookieName != null && state != null) {
            cookieName = Functions.safeCookieName(cookieName);
            //
            // expire the cookis at the current date + 10years (roughly, nevermind leap years)
            //
            response.addHeader("Set-Cookie", cookieName + "=" + state + "; Expires="+
                    sdf.format(new Date(System.currentTimeMillis() + 315360000000L)));
        }
        return null;
    }
}
