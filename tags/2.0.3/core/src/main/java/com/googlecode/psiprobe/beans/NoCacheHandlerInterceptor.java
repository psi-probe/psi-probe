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
package com.googlecode.psiprobe.beans;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoCacheHandlerInterceptor extends HandlerInterceptorAdapter {

    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss zz");
        response.setHeader("cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "Mon, 26 Jul 1997 05:00:00 GMT");
        response.setHeader("Last-Modified", sdf.format(new Date()));
    }
}
