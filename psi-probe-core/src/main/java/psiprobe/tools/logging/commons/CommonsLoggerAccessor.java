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
package psiprobe.tools.logging.commons;

import java.util.List;

import psiprobe.tools.logging.DefaultAccessor;
import psiprobe.tools.logging.LogDestination;

/**
 * The Class CommonsLoggerAccessor.
 */
public class CommonsLoggerAccessor extends DefaultAccessor {

  /**
   * Gets the destinations.
   *
   * @return the destinations
   */
  public List<LogDestination> getDestinations() {
    GetAllDestinationsVisitor visitor = new GetAllDestinationsVisitor();
    visitor.setTarget(getTarget());
    visitor.setApplication(getApplication());
    visitor.visit();
    return visitor.getDestinations();
  }

  /**
   * Gets the destination.
   *
   * @param logIndex the log index
   *
   * @return the destination
   */
  public LogDestination getDestination(String logIndex) {
    GetSingleDestinationVisitor visitor = new GetSingleDestinationVisitor(logIndex);
    visitor.setTarget(getTarget());
    visitor.setApplication(getApplication());
    visitor.visit();
    return visitor.getDestination();
  }

}
