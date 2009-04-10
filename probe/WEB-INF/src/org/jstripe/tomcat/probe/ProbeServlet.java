/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jstripe.tomcat.probe;

import org.apache.catalina.ContainerServlet;
import org.apache.catalina.Wrapper;
import org.jstripe.tomcat.probe.beans.ContainerWrapperBean;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Main dispatcher servlet. Spring default dispatcher servlet had to be superceeded to handle "privileged" application
 * context features. The actual requirement is to capture passed Wrapper instance into ContainerWrapperBean. Wrapper
 * instance is our gateway to Tomcat.
 *
 * Author: Vlad Ilyushchenko
 *
 */
public class ProbeServlet extends DispatcherServlet implements ContainerServlet {

    private Wrapper wrapper;

    public Wrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(Wrapper wrapper) {
        this.wrapper = wrapper;
        logger.info("setWrapper() called");
    }

    protected void doDispatch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletRequest.setCharacterEncoding("UTF-8");
        ContainerWrapperBean containerWrapper = (ContainerWrapperBean) getWebApplicationContext().getBean("containerWrapper");
        if (containerWrapper != null) {
            containerWrapper.setWrapper(getWrapper());
        }
        super.doDispatch(httpServletRequest, httpServletResponse);
    }
}
