/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://probe.jstripe.com/d/license.shtml
 *
 *  THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 *  WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jstripe.tomcat.probe.controllers.connectors;

import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RememberVisibilityController extends AbstractController {

    private final SimpleDateFormat sdf = new SimpleDateFormat("E, d-MMM-yyyy HH:mm:ss zz");

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String cookieName = RequestUtils.getStringParameter(request, "cn");
        String state = RequestUtils.getStringParameter(request, "state");
        if (cookieName != null && state != null) {
            //
            // expire the cookis at the current date + 10years (roughly, nevermind leap years)
            //
            response.addHeader("Set-Cookie", cookieName + "=" + state + "; Expires="+
                    sdf.format(new Date(System.currentTimeMillis() + 315360000000L)));
        }
        return null;
    }
}
