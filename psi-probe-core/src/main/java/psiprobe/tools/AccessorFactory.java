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
package psiprobe.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory for creating Accessor objects.
 */
public class AccessorFactory {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(AccessorFactory.class);

  /**
   * Prevent Instantiation of accessor factory.
   */
  private AccessorFactory() {
    // Prevent Instantiation
  }

  /**
   * Gets the single instance of AccessorFactory.
   *
   * @return single instance of AccessorFactory
   */
  public static Accessor getInstance() {
    return getSimple();
  }

  /**
   * Gets the simple.
   *
   * @return the simple
   */
  private static Accessor getSimple() {
    return new SimpleAccessor();
  }

}
