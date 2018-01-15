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
package psiprobe.controllers.apps;

import org.apache.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Reloads application context.
 */
public class BaseReloadContextController extends AbstractNoSelfContextHandlerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(BaseReloadContextController.class);

  @Override
  protected void executeAction(String contextName) throws Exception {
    Context context = getContainerWrapper().getTomcatContainer().findContext(contextName);
    if (context != null) {
      context.reload();

      // Logging action
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      // get username logger
      String name = auth.getName();
      logger.info(getMessageSourceAccessor().getMessage("probe.src.log.reload"), name, contextName);
    }
  }

}
