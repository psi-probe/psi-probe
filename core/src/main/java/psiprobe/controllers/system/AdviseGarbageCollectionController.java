/**
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Advises Java to run GC asap.
 */
public class AdviseGarbageCollectionController extends ParameterizableViewController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(AdviseGarbageCollectionController.class);

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
  public void setReplacePattern(String replacePattern) {
    this.replacePattern = replacePattern;
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    boolean finalization = ServletRequestUtils.getBooleanParameter(request, "fin", false);

    String referer = request.getHeader("Referer");
    String redirectUrl;
    if (referer != null) {
      redirectUrl = referer.replaceAll(replacePattern, "");
    } else {
      redirectUrl = request.getContextPath() + getViewName();
    }
    if (finalization) {
      Runtime.getRuntime().runFinalization();
      logger.debug("Advised finalization");
    } else {
      Runtime.getRuntime().gc();
      logger.debug("Advised Garbage Collection");
    }
    logger.debug("Redirected to {}", redirectUrl);
    return new ModelAndView(new RedirectView(redirectUrl));
  }

}
