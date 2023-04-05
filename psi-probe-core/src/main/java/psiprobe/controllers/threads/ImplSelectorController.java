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
package psiprobe.controllers.threads;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import psiprobe.Utils;

/**
 * The Class ImplSelectorController.
 */
@Controller
public class ImplSelectorController extends AbstractController {

  /** The impl1 controller. */
  private String impl1Controller;

  /** The impl2 controller. */
  private String impl2Controller;

  /**
   * Gets the impl1 controller.
   *
   * @return the impl1 controller
   */
  public String getImpl1Controller() {
    return impl1Controller;
  }

  /**
   * Sets the impl1 controller.
   *
   * @param impl1Controller the new impl1 controller
   */
  @Value("forward:/th_impl1.htm")
  public void setImpl1Controller(String impl1Controller) {
    this.impl1Controller = impl1Controller;
  }

  /**
   * Gets the impl2 controller.
   *
   * @return the impl2 controller
   */
  public String getImpl2Controller() {
    return impl2Controller;
  }

  /**
   * Sets the impl2 controller.
   *
   * @param impl2Controller the new impl2 controller
   */
  @Value("forward:/th_impl2.htm")
  public void setImpl2Controller(String impl2Controller) {
    this.impl2Controller = impl2Controller;
  }

  @RequestMapping(path = "/threads.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    boolean forceOld = ServletRequestUtils.getBooleanParameter(request, "forceold", false);
    if (!forceOld && Utils.isThreadingEnabled()) {
      return new ModelAndView(impl2Controller);
    }
    return new ModelAndView(impl1Controller);
  }

}
