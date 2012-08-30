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
package com.googlecode.psiprobe.controllers.logs;

import com.googlecode.psiprobe.beans.LogResolverBean;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class ListLogsController extends ParameterizableViewController {

    private String errorView;
    private LogResolverBean logResolver;

    public String getErrorView() {
        return errorView;
    }

    public void setErrorView(String errorView) {
        this.errorView = errorView;
    }

    public LogResolverBean getLogResolver() {
        return logResolver;
    }

    public void setLogResolver(LogResolverBean logResolver) {
        this.logResolver = logResolver;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        boolean showAll = ServletRequestUtils.getBooleanParameter(request, "apps", false);
        List uniqueList = logResolver.getLogDestinations(showAll);
        if (uniqueList != null) {
            return new ModelAndView(getViewName())
                    .addObject("logs", uniqueList);
        } else {
            return new ModelAndView(errorView);
        }
    }

}
