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
package psiprobe.beans.accessors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.tomee.jdbc.TomEEDataSourceCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import psiprobe.model.DataSourceInfo;

/**
 * The Class TomEeJdbcPoolDatasourceAccessorTest.
 */
class TomEeJdbcPoolDatasourceAccessorTest {

  /** The accessor. */
  private TomEeJdbcPoolDatasourceAccessor accessor;

  /**
   * Sets the up.
   */
  @BeforeEach
  void setUp() {
    accessor = new TomEeJdbcPoolDatasourceAccessor();
  }

  /**
   * Test get info with valid resource.
   */
  @Test
  void testGetInfoWithValidResource() {
    TomEEDataSourceCreator.TomEEDataSource ds = mock(TomEEDataSourceCreator.TomEEDataSource.class);
    when(ds.getNumActive()).thenReturn(2);
    when(ds.getNumIdle()).thenReturn(3);
    when(ds.getMaxActive()).thenReturn(10);
    when(ds.getUrl()).thenReturn("jdbc:h2:mem:test");
    when(ds.getUsername()).thenReturn("user");

    DataSourceInfo info = accessor.getInfo(ds);

    assertNotNull(info);
    assertEquals(2, info.getBusyConnections());
    assertEquals(5, info.getEstablishedConnections());
    assertEquals(10, info.getMaxConnections());
    assertEquals("jdbc:h2:mem:test", info.getJdbcUrl());
    assertEquals("user", info.getUsername());
    assertFalse(info.isResettable());
    assertEquals("tomcat-jdbc", info.getType());
  }

  /**
   * Test get info with invalid resource.
   */
  @Test
  void testGetInfoWithInvalidResource() {
    Object notDs = new Object();
    DataSourceInfo info = accessor.getInfo(notDs);
    assertNull(info);
  }

  /**
   * Test reset always returns false.
   */
  @Test
  void testResetAlwaysReturnsFalse() {
    Object any = new Object();
    assertFalse(accessor.reset(any));
  }

  /**
   * Test can map with valid resource.
   */
  @Test
  void testCanMapWithValidResource() {
    TomEEDataSourceCreator.TomEEDataSource ds = mock(TomEEDataSourceCreator.TomEEDataSource.class);
    assertTrue(accessor.canMap(ds));
  }

  /**
   * Test can map with invalid resource.
   */
  @Test
  void testCanMapWithInvalidResource() {
    Object notDs = new Object();
    assertFalse(accessor.canMap(notDs));
  }

}
