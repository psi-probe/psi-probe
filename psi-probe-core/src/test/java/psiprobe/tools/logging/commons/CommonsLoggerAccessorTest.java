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

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import psiprobe.tools.logging.LogDestination;

/** Tests for commons logging visitors and accessor behavior with JUL-backed loggers. */
class CommonsLoggerAccessorTest {

  @Test
  void getDestinationsCollectsDestinationsFromLoggerHierarchy() {
    Logger parent = Logger.getLogger("psiprobe.parent." + UUID.randomUUID());
    Logger child = Logger.getLogger("psiprobe.child." + UUID.randomUUID());

    parent.setUseParentHandlers(false);
    child.setUseParentHandlers(false);
    child.setParent(parent);

    parent.addHandler(new ConsoleHandler());
    child.addHandler(new ConsoleHandler());

    CommonsLoggerAccessor accessor = new CommonsLoggerAccessor();
    accessor.setTarget(new LoggerHolder(child));

    List<LogDestination> destinations = accessor.getDestinations();

    assertTrue(destinations.size() >= 2);
  }

  @Test
  void getDestinationReturnsIndexedDestinationWhenPresent() {
    Logger logger = Logger.getLogger("psiprobe.single." + UUID.randomUUID());
    logger.setUseParentHandlers(false);
    logger.addHandler(new ConsoleHandler());

    CommonsLoggerAccessor accessor = new CommonsLoggerAccessor();
    accessor.setTarget(new LoggerHolder(logger));

    LogDestination destination = accessor.getDestination("0");

    assertNotNull(destination);
  }

  @Test
  void getDestinationReturnsNullWhenIndexDoesNotExist() {
    Logger logger = Logger.getLogger("psiprobe.invalid." + UUID.randomUUID());
    logger.setUseParentHandlers(false);
    logger.addHandler(new ConsoleHandler());

    CommonsLoggerAccessor accessor = new CommonsLoggerAccessor();
    accessor.setTarget(new LoggerHolder(logger));

    LogDestination destination = accessor.getDestination("5");

    assertNull(destination);
  }

  @Test
  void getDestinationsReturnsEmptyForUnknownOrMissingLogger() {
    CommonsLoggerAccessor unknownLoggerAccessor = new CommonsLoggerAccessor();
    unknownLoggerAccessor.setTarget(new LoggerHolder(new Object()));
    assertTrue(unknownLoggerAccessor.getDestinations().isEmpty());

    CommonsLoggerAccessor nullLoggerAccessor = new CommonsLoggerAccessor();
    nullLoggerAccessor.setTarget(new LoggerHolder(null));
    assertTrue(nullLoggerAccessor.getDestinations().isEmpty());
    assertNull(nullLoggerAccessor.getDestination("0"));
  }

  private static final class LoggerHolder {
    @SuppressWarnings("unused")
    private final Object logger;

    private LoggerHolder(Object logger) {
      this.logger = logger;
    }
  }
}
