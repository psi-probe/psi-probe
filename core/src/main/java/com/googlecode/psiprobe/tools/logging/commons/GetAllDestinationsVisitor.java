/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package com.googlecode.psiprobe.tools.logging.commons;

import com.googlecode.psiprobe.tools.logging.LogDestination;
import com.googlecode.psiprobe.tools.logging.jdk.Jdk14LoggerAccessor;
import com.googlecode.psiprobe.tools.logging.log4j.Log4JLoggerAccessor;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class GetAllDestinationsVisitor.
 *
 * @author Mark Lewis
 */
public class GetAllDestinationsVisitor extends LoggerAccessorVisitor {

  /** The destinations. */
  private List<LogDestination> destinations = new ArrayList<LogDestination>();

  /**
   * Gets the destinations.
   *
   * @return the destinations
   */
  public List<LogDestination> getDestinations() {
    return destinations;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.commons.LoggerAccessorVisitor#visit(com.googlecode.psiprobe.tools.logging.log4j.Log4JLoggerAccessor)
   */
  public void visit(Log4JLoggerAccessor accessor) {
    destinations.addAll(accessor.getAppenders());
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.commons.LoggerAccessorVisitor#visit(com.googlecode.psiprobe.tools.logging.jdk.Jdk14LoggerAccessor)
   */
  public void visit(Jdk14LoggerAccessor accessor) {
    destinations.addAll(accessor.getHandlers());
  }

}
