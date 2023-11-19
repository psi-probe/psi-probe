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
package psiprobe.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Base class for all controllers requiring "webapp" request parameter.
 */
public abstract class AbstractContextHandlerController extends AbstractTomcatContainerController {

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    String contextName = ServletRequestUtils.getStringParameter(request, "webapp");
    Context context = null;
    if (contextName != null) {
      contextName = getContainerWrapper().getTomcatContainer().formatContextName(contextName);
      context = getContainerWrapper().getTomcatContainer().findContext(contextName);
    }

    if (context != null || isContextOptional()) {
      return handleContext(contextName, context, request, response);
    }
    if (contextName != null) {
      request.setAttribute("errorMessage", getMessageSourceAccessor()
          .getMessage("probe.src.contextDoesntExist", new Object[] {contextName}));
    }

    return new ModelAndView("errors/paramerror");
  }

  /**
   * Checks if is context optional.
   *
   * @return true, if is context optional
   */
  protected boolean isContextOptional() {
    return false;
  }

  /**
   * Handle context.
   *
   * @param contextName the context name
   * @param context the context
   * @param request the request
   * @param response the response
   *
   * @return the model and view
   *
   * @throws Exception the exception
   */
  protected abstract ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception;
}
