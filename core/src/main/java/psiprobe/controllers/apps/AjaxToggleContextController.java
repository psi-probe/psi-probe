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

package psiprobe.controllers.apps;

import org.apache.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.ContextHandlerController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Stops a web application.
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class AjaxToggleContextController extends ContextHandlerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(AjaxReloadContextController.class);

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    if (!request.getContextPath().equals(contextName) && context != null) {
      try {
        if (context.getState().isAvailable()) {
          logger.info("{} requested STOP of {}", request.getRemoteAddr(), contextName);
          getContainerWrapper().getTomcatContainer().stop(contextName);
        } else {
          logger.info("{} requested START of {}", request.getRemoteAddr(), contextName);
          getContainerWrapper().getTomcatContainer().start(contextName);
        }
      } catch (Exception e) {
        logger.error("Error during ajax request to START/STOP of '{}'", contextName, e);
      }
    }
    return new ModelAndView(getViewName(), "available", context != null
        && getContainerWrapper().getTomcatContainer().getAvailable(context));
  }

}
