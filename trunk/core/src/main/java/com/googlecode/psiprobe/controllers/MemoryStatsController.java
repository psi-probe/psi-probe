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

import com.googlecode.psiprobe.beans.JvmMemoryInfoAccessorBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class MemoryStatsController extends ParameterizableViewController {
    private JvmMemoryInfoAccessorBean jvmMemoryInfoAccessorBean;

    public JvmMemoryInfoAccessorBean getJvmMemoryInfoAccessorBean() {
        return jvmMemoryInfoAccessorBean;
    }

    public void setJvmMemoryInfoAccessorBean(JvmMemoryInfoAccessorBean jvmMemoryInfoAccessorBean) {
        this.jvmMemoryInfoAccessorBean = jvmMemoryInfoAccessorBean;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView(getViewName(), "pools", jvmMemoryInfoAccessorBean.getPools());
    }
}
