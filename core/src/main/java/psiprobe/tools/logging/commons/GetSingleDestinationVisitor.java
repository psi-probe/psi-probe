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
package psiprobe.tools.logging.commons;

import psiprobe.tools.logging.LogDestination;
import psiprobe.tools.logging.jdk.Jdk14LoggerAccessor;
import psiprobe.tools.logging.log4j.Log4JLoggerAccessor;

/**
 * The Class GetSingleDestinationVisitor.
 */
public class GetSingleDestinationVisitor extends AbstractLoggerAccessorVisitor {

  /** The log index. */
  private final String logIndex;

  /** The destination. */
  private LogDestination destination;

  /**
   * Instantiates a new gets the single destination visitor.
   *
   * @param logIndex the log index
   */
  public GetSingleDestinationVisitor(String logIndex) {
    this.logIndex = logIndex;
  }

  /**
   * Gets the destination.
   *
   * @return the destination
   */
  public LogDestination getDestination() {
    return destination;
  }

  @Override
  public void visit(Log4JLoggerAccessor accessor) {
    LogDestination dest = accessor.getAppender(logIndex);
    if (dest != null) {
      destination = dest;
    }
  }

  @Override
  public void visit(Jdk14LoggerAccessor accessor) {
    LogDestination dest = accessor.getHandler(logIndex);
    if (dest != null) {
      destination = dest;
    }
  }

}
