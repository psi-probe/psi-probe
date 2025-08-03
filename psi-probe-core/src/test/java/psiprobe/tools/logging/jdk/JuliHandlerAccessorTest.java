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
package psiprobe.tools.logging.jdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import psiprobe.tools.Instruments;

/**
 * The Class JuliHandlerAccessorTest.
 */
class JuliHandlerAccessorTest {

  /** The accessor. */
  private JuliHandlerAccessor accessor;

  /**
   * Sets the up.
   */
  @BeforeEach
  void setUp() {
    accessor = spy(new JuliHandlerAccessor());
  }


  /**
   * Test get file all fields present.
   */
  @Test
  void testGetFile_AllFieldsPresent() {
    Object target = new Object();
    doReturn(target).when(accessor).getTarget();

    try (MockedStatic<Instruments> instrumentsMock = mockStatic(Instruments.class)) {
      instrumentsMock.when(() -> Instruments.getField(target, "directory")).thenReturn("logs");
      instrumentsMock.when(() -> Instruments.getField(target, "prefix")).thenReturn("app-");
      instrumentsMock.when(() -> Instruments.getField(target, "suffix")).thenReturn(".log");
      instrumentsMock.when(() -> Instruments.getField(target, "date")).thenReturn("20240607");

      File expected = Path.of("logs", "app-20240607.log").toFile();
      File actual = accessor.getFile();

      assertEquals(expected, actual);
    }
  }

  /**
   * Test get file missing field returns stdout file.
   */
  @Test
  void testGetFile_MissingField_ReturnsStdoutFile() {
    Object target = new Object();
    doReturn(target).when(accessor).getTarget();

    try (MockedStatic<Instruments> instrumentsMock = mockStatic(Instruments.class)) {
      instrumentsMock.when(() -> Instruments.getField(target, "directory")).thenReturn(null);
      instrumentsMock.when(() -> Instruments.getField(target, "prefix")).thenReturn("app-");
      instrumentsMock.when(() -> Instruments.getField(target, "suffix")).thenReturn(".log");
      instrumentsMock.when(() -> Instruments.getField(target, "date")).thenReturn("20240607");

      Path stdoutFile = Path.of("stdout.log");
      doReturn(stdoutFile.toFile()).when(accessor).getStdoutFile();

      File actual = accessor.getFile();

      assertEquals(stdoutFile.toFile(), actual);
    }
  }

}
