/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package com.googlecode.psiprobe.controllers.system;

import com.googlecode.psiprobe.beans.JvmMemoryInfoAccessorBean;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class MemoryStatsController extends ParameterizableViewController {

  private JvmMemoryInfoAccessorBean jvmMemoryInfoAccessorBean;
  private long collectionPeriod;

  public JvmMemoryInfoAccessorBean getJvmMemoryInfoAccessorBean() {
    return jvmMemoryInfoAccessorBean;
  }

  public void setJvmMemoryInfoAccessorBean(JvmMemoryInfoAccessorBean jvmMemoryInfoAccessorBean) {
    this.jvmMemoryInfoAccessorBean = jvmMemoryInfoAccessorBean;
  }

  public long getCollectionPeriod() {
    return collectionPeriod;
  }

  public void setCollectionPeriod(long collectionPeriod) {
    this.collectionPeriod = collectionPeriod;
  }

  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    ModelAndView mv = new ModelAndView(getViewName());
    mv.addObject("pools", getJvmMemoryInfoAccessorBean().getPools());
    mv.addObject("collectionPeriod", new Long(getCollectionPeriod()));
    return mv;
  }

}
