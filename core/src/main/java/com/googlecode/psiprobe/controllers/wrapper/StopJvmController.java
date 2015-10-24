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

package com.googlecode.psiprobe.controllers.wrapper;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.tanukisoftware.wrapper.WrapperManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class StopJvmController.
 *
 * @author Vlad Ilyushchenko
 */
public class StopJvmController extends ParameterizableViewController {

  /** The stop exit code. */
  private int stopExitCode = 1;

  /**
   * Gets the stop exit code.
   *
   * @return the stop exit code
   */
  public int getStopExitCode() {
    return stopExitCode;
  }

  /**
   * Sets the stop exit code.
   *
   * @param stopExitCode the new stop exit code
   */
  public void setStopExitCode(int stopExitCode) {
    this.stopExitCode = stopExitCode;
  }

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    boolean done = false;
    try {
      Class.forName("org.tanukisoftware.wrapper.WrapperManager");
      logger.info("JVM is STOPPED by " + request.getRemoteAddr());
      WrapperManager.stop(stopExitCode);
      done = true;
    } catch (ClassNotFoundException e) {
      logger.info("WrapperManager not found. Do you have wrapper.jar in the classpath?");
    }
    return new ModelAndView(getViewName(), "done", done);
  }

}
