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

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Locale;

import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.AbstractController;

import psiprobe.beans.ContainerWrapperBean;

/**
 * Base class for controllers requiring access to ContainerWrapperBean.
 */
public abstract class AbstractTomcatContainerController extends AbstractController {

  /** The logger. */
  // We are hiding this as we use better logger and don't care to use springs jcl variation
  @SuppressWarnings("HidingField")
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /** The container wrapper. */
  @Inject
  private ContainerWrapperBean containerWrapper;

  /** The view name. */
  private String viewName;

  /** Part of HTTP content type header. */
  private static final String MULTIPART = "multipart/";

  /** Constant for HTTP POST method. */
  private static final String POST_METHOD = "POST";

  /**
   * Gets the container wrapper.
   *
   * @return the container wrapper
   */
  public ContainerWrapperBean getContainerWrapper() {
    return containerWrapper;
  }

  /**
   * Sets the container wrapper.
   *
   * @param containerWrapper the new container wrapper
   */
  public void setContainerWrapper(ContainerWrapperBean containerWrapper) {
    this.containerWrapper = containerWrapper;
  }

  /**
   * Gets the view name.
   *
   * @return the view name
   */
  public String getViewName() {
    return viewName;
  }

  /**
   * Sets the view name.
   *
   * @param viewName the new view name
   */
  public void setViewName(String viewName) {
    this.viewName = viewName;
  }

  /**
   * Utility method that determines whether the request contains multipart content. Borrowed and
   * modified from tomcat as they removed it in 9.0.88 and 10.1.x lines.
   *
   * @param request The request context to be evaluated. Must be non-null.
   *
   * @return {@code true} if the request is multipart; {@code false} otherwise.
   */
  public boolean isMultipartContent(final HttpServletRequest request) {
    if (!POST_METHOD.equalsIgnoreCase(request.getMethod())) {
      return false;
    }
    final String contentType = new ServletRequestContext(request).getContentType();
    if (contentType == null) {
      return false;
    }
    return contentType.toLowerCase(Locale.ENGLISH).startsWith(MULTIPART);
  }

}
