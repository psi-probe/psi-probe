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
package com.googlecode.psiprobe.controllers.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Force is a wrong term here. Advises Java to run GC asap.
 *
 * @author Vlad Ilyushchenko
 */
public class AdviseGCController extends ParameterizableViewController {

    private String replacePattern;

    public String getReplacePattern() {
        return replacePattern;
    }

    public void setReplacePattern(String replacePattern) {
        this.replacePattern = replacePattern;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean finalization = ServletRequestUtils.getBooleanParameter(request, "fin", false);

        String referer = request.getHeader("Referer");
        String redirectURL;
        if (referer != null) {
            redirectURL = referer.replaceAll(replacePattern, "");
        } else {
            redirectURL = request.getContextPath() + getViewName();
        }
        if (finalization) {
            Runtime.getRuntime().runFinalization();
            logger.debug("Advised finalization");
        } else {
            Runtime.getRuntime().gc();
            logger.debug("Advised Garbage Collection");
        }
        logger.debug("Redirected to "+redirectURL);
        return new ModelAndView(new RedirectView(redirectURL));
    }
}
