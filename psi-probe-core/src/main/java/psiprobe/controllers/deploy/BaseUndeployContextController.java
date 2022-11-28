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
package psiprobe.controllers.deploy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;

import psiprobe.controllers.AbstractContextHandlerController;

/**
 * Undeploys a web application.
 */
public class BaseUndeployContextController extends AbstractContextHandlerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(BaseUndeployContextController.class);

  /** The failure view name. */
  private String failureViewName;

  /**
   * Gets the failure view name.
   *
   * @return the failure view name
   */
  public String getFailureViewName() {
    return failureViewName;
  }

  /**
   * Sets the failure view name.
   *
   * @param failureViewName the new failure view name
   */
  public void setFailureViewName(String failureViewName) {
    this.failureViewName = failureViewName;
  }

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      if (request.getContextPath().equals(contextName)) {
        throw new IllegalStateException(
            getMessageSourceAccessor().getMessage("probe.src.contextAction.cannotActOnSelf"));
      }

      getContainerWrapper().getTomcatContainer().remove(contextName);
      // Logging action
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      // get username logger
      String name = auth.getName();
      logger.info(getMessageSourceAccessor().getMessage("probe.src.log.undeploy"), name,
          contextName);

    } catch (Exception e) {
      request.setAttribute("errorMessage", e.getMessage());
      logger.error("Error during undeploy of '{}'", contextName, e);
      return new ModelAndView(new InternalResourceView(
          getFailureViewName() == null ? getViewName() : getFailureViewName()));
    }
    return new ModelAndView(new RedirectView(request.getContextPath() + getViewName()));
  }

  /**
   * Execute action.
   *
   * @param contextName the context name
   *
   * @throws Exception the exception
   */
  protected void executeAction(String contextName) throws Exception {
    // Not Implemented
  }

}
