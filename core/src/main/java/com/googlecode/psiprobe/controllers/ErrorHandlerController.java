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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * The ErrorHandlerController will show two different views depending on whether
 * the failed request was AJAX or not.
 *
 * @author Vlad Ilyushchenko.
 */
public class ErrorHandlerController extends AbstractController {
    private String viewName;
    private String ajaxViewName;
    private String ajaxExtension = ".ajax";

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getAjaxViewName() {
        return ajaxViewName;
    }

    public void setAjaxViewName(String ajaxViewName) {
        this.ajaxViewName = ajaxViewName;
    }

    public String getAjaxExtension() {
        return ajaxExtension;
    }

    public void setAjaxExtension(String ajaxExtension) {
        this.ajaxExtension = ajaxExtension;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String originalURI = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (originalURI != null && originalURI.endsWith(ajaxExtension)) {
            return new ModelAndView(ajaxViewName);
        } else {
            return new ModelAndView(viewName);
        }
    }
}
