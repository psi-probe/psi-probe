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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.vladmihalcea.flexypool.FlexyPoolDataSource;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import psiprobe.model.DataSourceInfo;

/**
 * The Class FlexyPoolDatasourceAccessorTest.
 */
class FlexyPoolDatasourceAccessorTest {

  /** The accessor. */
  private FlexyPoolDatasourceAccessor accessor;

  /**
   * Sets the up.
   */
  @BeforeEach
  void setUp() {
    accessor = new FlexyPoolDatasourceAccessor();
  }

  /**
   * Test can map with flexy pool wrapper.
   */
  @Test
  void testCanMapWithFlexyPoolWrapper() {
    assertTrue(accessor.canMap(new FlexyPoolDataSource<>(new Object())));
  }

  /**
   * Test can map with invalid resource.
   */
  @Test
  void testCanMapWithInvalidResource() {
    assertFalse(accessor.canMap(new Object()));
  }

  /**
   * Test get info delegates to wrapped datasource accessor.
   *
   * @throws SQLException the sql exception
   */
  @Test
  void testGetInfoDelegatesToWrappedDatasourceAccessor() throws SQLException {
    HikariDataSource source = mock(HikariDataSource.class);
    HikariPoolMXBean poolMxBean = mock(HikariPoolMXBean.class);
    when(source.getHikariPoolMXBean()).thenReturn(poolMxBean);
    when(poolMxBean.getActiveConnections()).thenReturn(2);
    when(poolMxBean.getTotalConnections()).thenReturn(5);
    when(source.getMaximumPoolSize()).thenReturn(10);
    when(source.getJdbcUrl()).thenReturn("jdbc:h2:mem:test");
    when(source.getUsername()).thenReturn("user");

    DataSourceInfo info = accessor.getInfo(new FlexyPoolDataSource<>(source));

    assertNotNull(info);
    assertEquals(2, info.getBusyConnections());
    assertEquals(5, info.getEstablishedConnections());
    assertEquals(10, info.getMaxConnections());
    assertEquals("jdbc:h2:mem:test", info.getJdbcUrl());
    assertEquals("user", info.getUsername());
    assertEquals("hikari", info.getType());
  }

  /**
   * Test get info with unsupported wrapped datasource.
   *
   * @throws SQLException the sql exception
   */
  @Test
  void testGetInfoWithUnsupportedWrappedDatasource() throws SQLException {
    assertNull(accessor.getInfo(new FlexyPoolDataSource<>(new Object())));
  }

  /**
   * Test reset delegates to wrapped datasource accessor.
   *
   * @throws SQLException the sql exception
   */
  @Test
  void testResetDelegatesToWrappedDatasourceAccessor() throws SQLException {
    ComboPooledDataSource source = mock(ComboPooledDataSource.class);

    assertTrue(accessor.reset(new FlexyPoolDataSource<>(source)));

    verify(source).hardReset();
  }
}
