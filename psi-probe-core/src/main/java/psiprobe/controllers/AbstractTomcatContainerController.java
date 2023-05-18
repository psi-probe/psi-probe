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

import javax.inject.Inject;

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
}
