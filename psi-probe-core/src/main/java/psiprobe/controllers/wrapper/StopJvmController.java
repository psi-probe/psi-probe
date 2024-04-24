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
package psiprobe.controllers.wrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.tanukisoftware.wrapper.WrapperManager;

import psiprobe.PostParameterizableViewController;

/**
 * The Class StopJvmController.
 */
@Controller
public class StopJvmController extends PostParameterizableViewController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(StopJvmController.class);

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

  @RequestMapping(path = "/adm/stopvm.ajax")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    boolean done = false;
    try {
      Class.forName("org.tanukisoftware.wrapper.WrapperManager");
      logger.info("JVM is STOPPED by {}", request.getRemoteAddr());
      WrapperManager.stop(stopExitCode);
      done = true;
    } catch (ClassNotFoundException e) {
      logger.info("WrapperManager not found. Do you have wrapper.jar in the classpath?");
      logger.trace("", e);
    }
    return new ModelAndView(getViewName(), "done", done);
  }

  @Value("ajax/jvm_stopped")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
