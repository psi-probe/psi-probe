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
package psiprobe.controllers.datasources;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import psiprobe.controllers.AbstractContextHandlerController;

/**
 * Resets datasource if the datasource supports it.
 */
@Controller
public class ResetDataSourceController extends AbstractContextHandlerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ResetDataSourceController.class);

  /** The replace pattern. */
  private String replacePattern;

  /**
   * Gets the replace pattern.
   *
   * @return the replace pattern
   */
  public String getReplacePattern() {
    return replacePattern;
  }

  /**
   * Sets the replace pattern.
   *
   * @param replacePattern the new replace pattern
   */
  @Value("^http(s)?://[a-zA-Z\\-\\.0-9]+(:[0-9]+)?")
  public void setReplacePattern(String replacePattern) {
    this.replacePattern = replacePattern;
  }

  @RequestMapping(path = "/app/resetds.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    String resourceName = ServletRequestUtils.getStringParameter(request, "resource", null);
    String referer = request.getHeader("Referer");
    String redirectUrl;
    if (referer != null) {
      redirectUrl = referer.replaceAll(replacePattern, "");
    } else {
      redirectUrl = request.getContextPath() + getViewName();
    }

    if (resourceName != null && resourceName.length() > 0) {
      boolean reset = false;
      try {
        reset = getContainerWrapper().getResourceResolver().resetResource(context, resourceName,
            getContainerWrapper());
      } catch (NamingException e) {
        request.setAttribute("errorMessage", getMessageSourceAccessor()
            .getMessage("probe.src.reset.datasource.notfound", new Object[] {resourceName}));
        logger.trace("", e);
      }
      if (!reset) {
        request.setAttribute("errorMessage",
            getMessageSourceAccessor().getMessage("probe.src.reset.datasource"));
      }

    }
    logger.debug("Redirected to {}", redirectUrl);
    return new ModelAndView(new RedirectView(redirectUrl));
  }

  @Override
  protected boolean isContextOptional() {
    return !getContainerWrapper().getResourceResolver().supportsPrivateResources();
  }

  @Value("/resources.htm")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
