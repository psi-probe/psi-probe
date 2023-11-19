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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import psiprobe.controllers.AbstractTomcatContainerController;

/**
 * Forces Tomcat to install a pre-configured context name.
 */
@Controller
public class DeployContextController extends AbstractTomcatContainerController {

  @RequestMapping(path = "/adm/deploycontext.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  public ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    String contextName = ServletRequestUtils.getStringParameter(request, "context");

    if (contextName != null) {
      try {
        if (getContainerWrapper().getTomcatContainer().installContext(contextName)) {
          request.setAttribute("successMessage", getMessageSourceAccessor()
              .getMessage("probe.src.deploy.context.success", new Object[] {contextName}));
          // Logging action
          Authentication auth = SecurityContextHolder.getContext().getAuthentication();
          // get username logger
          String name = auth.getName();
          logger.info(getMessageSourceAccessor().getMessage("probe.src.log.deploycontext"), name,
              contextName);
        } else {
          request.setAttribute("errorMessage", getMessageSourceAccessor()
              .getMessage("probe.src.deploy.context.failure", new Object[] {contextName}));
        }
      } catch (Exception e) {
        request.setAttribute("errorMessage", e.getMessage());
        logger.trace("", e);
      }
    }

    return new ModelAndView(new InternalResourceView(getViewName()));
  }

  @Value("/adm/deploy.htm")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
