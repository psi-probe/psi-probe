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

import java.util.ArrayList;
import java.util.List;
import psiprobe.tools.logging.LogDestination;
import psiprobe.tools.logging.jdk.Jdk14LoggerAccessor;
import psiprobe.tools.logging.log4j.Log4JLoggerAccessor;

/**
 * The Class GetAllDestinationsVisitor.
 */
public class GetAllDestinationsVisitor extends AbstractLoggerAccessorVisitor {

  /** The destinations. */
  private final List<LogDestination> destinations = new ArrayList<>();

  /**
   * Gets the destinations.
   *
   * @return the destinations
   */
  public List<LogDestination> getDestinations() {
    return destinations;
  }

  @Override
  public void visit(Log4JLoggerAccessor accessor) {
    destinations.addAll(accessor.getAppenders());
  }

  @Override
  public void visit(Jdk14LoggerAccessor accessor) {
    destinations.addAll(accessor.getHandlers());
  }

}
