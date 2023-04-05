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
package psiprobe.controllers.apps;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.AbstractContextHandlerController;

/**
 * Stops a web application.
 */
@Controller
public class AjaxToggleContextController extends AbstractContextHandlerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(AjaxToggleContextController.class);

  @RequestMapping(path = "/app/toggle.ajax")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    if (context != null && !request.getContextPath().equals(contextName)) {
      try {
        // Logging action
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // get username logger
        String name = auth.getName();
        if (context.getState().isAvailable()) {
          logger.info("{} requested STOP of {}", request.getRemoteAddr(), contextName);
          getContainerWrapper().getTomcatContainer().stop(contextName);
          logger.info(getMessageSourceAccessor().getMessage("probe.src.log.stop"), name,
              contextName);
        } else {
          logger.info("{} requested START of {}", request.getRemoteAddr(), contextName);
          getContainerWrapper().getTomcatContainer().start(contextName);
          logger.info(getMessageSourceAccessor().getMessage("probe.src.log.start"), name,
              contextName);
        }
      } catch (Exception e) {
        logger.error("Error during ajax request to START/STOP of '{}'", contextName, e);
      }
    }
    return new ModelAndView(getViewName(), "available",
        context != null && getContainerWrapper().getTomcatContainer().getAvailable(context));
  }

  @Value("ajax/context_status")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
