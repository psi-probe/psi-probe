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
package psiprobe.tools.logging.log4j2;

import java.util.Map;
import psiprobe.tools.logging.DefaultAccessor;

/**
 * The Class Log4J2LoggerContextAccessor.
 */
public class Log4J2LoggerContextAccessor extends DefaultAccessor {

  /**
   * Gets the loggers.
   *
   * @return the loggers
   */
  public Map<String, Object> getLoggers() {
    Map<String, Object> loggers = null;
    Object configuration = null;
    try {
      configuration = invokeMethod(getTarget(), "getConfiguration", null, null);
    } catch (Exception e) {
      logger.error("exception invoking getConfiguration", e);
      throw e;
    }
    if (configuration != null) {
      try {
        loggers = (Map<String, Object>) invokeMethod(configuration, "getLoggers", null, null);
      } catch (Exception e) {
        logger.error("exception invoking getLoggers", e);
        throw e;
      }
    }
    return loggers;
  }

  /**
   * Update log4j2 loggers.
   */
  public void updateLoggers() {
    try {
      invokeMethod(getTarget(), "updateLoggers", null, null);
    } catch (Exception e) {
      logger.error("exception invoking updateLoggers", e);
      throw e;
    }
  }
}
