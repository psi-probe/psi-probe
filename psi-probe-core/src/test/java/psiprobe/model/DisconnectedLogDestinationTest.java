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
package psiprobe.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.codebox.bean.JavaBeanTester;

import java.io.File;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import psiprobe.tools.logging.LogDestination;

/**
 * The Class DisconnectedLogDestinationTest.
 */
class DisconnectedLogDestinationTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(DisconnectedLogDestination.class).loadData().skipStrictSerializable()
        .test();
  }

  @Test
  void testBuilderFromDestination() {
    LogDestination dest = Mockito.mock(LogDestination.class);
    Application app = new Application();
    Mockito.when(dest.getApplication()).thenReturn(app);
    Mockito.when(dest.isRoot()).thenReturn(true);
    Mockito.when(dest.isContext()).thenReturn(false);
    Mockito.when(dest.getName()).thenReturn("testLogger");
    Mockito.when(dest.getIndex()).thenReturn("0");
    Mockito.when(dest.getTargetClass()).thenReturn("com.example.Logger");
    Mockito.when(dest.getConversionPattern()).thenReturn("%d %m%n");
    Mockito.when(dest.getFile()).thenReturn(new File("/tmp/test.log"));
    Mockito.when(dest.getLogType()).thenReturn("logback");
    Mockito.when(dest.getSize()).thenReturn(1024L);
    Mockito.when(dest.getLastModified()).thenReturn(new Timestamp(1000L));
    Mockito.when(dest.getLevel()).thenReturn("INFO");
    Mockito.when(dest.getValidLevels()).thenReturn(new String[] {"DEBUG", "INFO", "WARN"});
    Mockito.when(dest.getEncoding()).thenReturn("UTF-8");

    DisconnectedLogDestination dld = new DisconnectedLogDestination().builder(dest);

    assertEquals(app, dld.getApplication());
    assertEquals(true, dld.isRoot());
    assertEquals(false, dld.isContext());
    assertEquals("testLogger", dld.getName());
    assertEquals("0", dld.getIndex());
    assertEquals("com.example.Logger", dld.getTargetClass());
    assertEquals("%d %m%n", dld.getConversionPattern());
    assertEquals("/tmp/test.log", dld.getFile().getPath());
    assertEquals("logback", dld.getLogType());
    assertEquals(1024L, dld.getSize());
    assertNotNull(dld.getLastModified());
    assertEquals("INFO", dld.getLevel());
    assertEquals(3, dld.getValidLevels().length);
    assertEquals("UTF-8", dld.getEncoding());
  }

  @Test
  void testGetLastModifiedWhenNull() {
    DisconnectedLogDestination dld = new DisconnectedLogDestination();
    assertNull(dld.getLastModified());
  }

  @Test
  void testGetValidLevelsWhenNull() {
    DisconnectedLogDestination dld = new DisconnectedLogDestination();
    assertEquals(0, dld.getValidLevels().length);
  }

  @Test
  void testGetLastModifiedReturnsDefensiveCopy() {
    LogDestination dest = Mockito.mock(LogDestination.class);
    Timestamp ts = new Timestamp(5000L);
    Mockito.when(dest.getLastModified()).thenReturn(ts);
    Mockito.when(dest.getValidLevels()).thenReturn(new String[0]);

    DisconnectedLogDestination dld = new DisconnectedLogDestination().builder(dest);
    Timestamp retrieved = dld.getLastModified();
    // Should be a defensive copy, not the same instance
    assertEquals(ts.getTime(), retrieved.getTime());
    assertEquals(5000L, retrieved.getTime());
  }
}
