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

import java.net.URLClassLoader;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import psiprobe.Utils;

/**
 * The Class GetClassLoaderUrlsController.
 */
@Controller
public class GetClassLoaderUrlsController extends ParameterizableViewController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(GetClassLoaderUrlsController.class);

  @RequestMapping(path = "/cldetails.ajax")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    String threadName = ServletRequestUtils.getStringParameter(request, "thread");

    Thread thread = Utils.getThreadByName(threadName);

    if (thread != null) {
      ClassLoader cl = thread.getContextClassLoader();
      if (cl instanceof URLClassLoader) {
        try {
          request.setAttribute("urls", Arrays.asList(((URLClassLoader) cl).getURLs()));
        } catch (Exception e) {
          logger.error("There was an exception querying classloader for thread '{}'", threadName,
              e);
        }
      }
    }

    return new ModelAndView(getViewName());
  }

  @Value("ajax/classLoaderDetails")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
