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
package psiprobe;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * The Class PostParameterizableViewController.
 */
public class PostParameterizableViewController extends ParameterizableViewController {

  /**
   * Instantiates a new post parameterizable view controller. As of spring 4.3.x, the
   * ParameterizableViewController no longer allows POST but only GET/HEAD. This patch restores POST
   * functionality.
   */
  public PostParameterizableViewController() {
    setSupportedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.HEAD.name());
  }

}
