/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.controllers.system;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import psiprobe.beans.JvmMemoryInfoAccessorBean;

/**
 * The Class BaseMemoryStatsController.
 */
public class BaseMemoryStatsController extends ParameterizableViewController {

  /** The jvm memory info accessor bean. */
  @Inject
  private JvmMemoryInfoAccessorBean jvmMemoryInfoAccessorBean;

  /** The collection period. */
  private long collectionPeriod;

  /**
   * Gets the jvm memory info accessor bean.
   *
   * @return the jvm memory info accessor bean
   */
  public JvmMemoryInfoAccessorBean getJvmMemoryInfoAccessorBean() {
    return jvmMemoryInfoAccessorBean;
  }

  /**
   * Sets the jvm memory info accessor bean.
   *
   * @param jvmMemoryInfoAccessorBean the new jvm memory info accessor bean
   */
  public void setJvmMemoryInfoAccessorBean(JvmMemoryInfoAccessorBean jvmMemoryInfoAccessorBean) {
    this.jvmMemoryInfoAccessorBean = jvmMemoryInfoAccessorBean;
  }

  /**
   * Gets the collection period.
   *
   * @return the collection period
   */
  public long getCollectionPeriod() {
    return collectionPeriod;
  }

  /**
   * Sets the collection period.
   *
   * @param collectionPeriod the new collection period
   */
  public void setCollectionPeriod(long collectionPeriod) {
    this.collectionPeriod = collectionPeriod;
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    ModelAndView mv = new ModelAndView(getViewName());
    mv.addObject("pools", getJvmMemoryInfoAccessorBean().getPools());
    mv.addObject("collectionPeriod", getCollectionPeriod());
    return mv;
  }

}
