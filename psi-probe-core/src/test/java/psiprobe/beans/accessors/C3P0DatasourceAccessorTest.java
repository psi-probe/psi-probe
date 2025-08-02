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

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.jboss.C3P0PooledDataSource;

import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * The Class C3P0DatasourceAccessorTest.
 */
class C3P0DatasourceAccessorTest {

  /** The accessor. */
  C3P0DatasourceAccessor accessor;

  /** The source. */
  ComboPooledDataSource source;

  /** The bad source. */
  C3P0PooledDataSource badSource;

  /**
   * Before.
   */
  @BeforeEach
  void before() {
    accessor = new C3P0DatasourceAccessor();
    source = new ComboPooledDataSource();
    badSource = new C3P0PooledDataSource();
  }

  /**
   * Can map test.
   */
  @Test
  void canMapTest() {
    Assertions.assertTrue(accessor.canMap(source));
  }

  /**
   * Cannot map test.
   */
  @Test
  void cannotMapTest() {
    Assertions.assertFalse(accessor.canMap(badSource));
  }

  /**
   * Gets the info test.
   *
   * @throws SQLException the sql exception
   */
  @Test
  void infoTest() throws SQLException {
    Assertions.assertNotNull(accessor.getInfo(source));
  }

  /**
   * Gets the info with bad source test.
   *
   * @return the info with bad source test
   * @throws SQLException the SQL exception
   */
  @Test
  void getInfoWithBadSourceTest() throws SQLException {
    Assertions.assertNull(accessor.getInfo(badSource));
  }

  /**
   * Gets the info throws SQL exception test.
   *
   * @return the info throws SQL exception test
   * @throws Exception the exception
   */
  @Test
  void getInfoThrowsSQLExceptionTest() throws Exception {
    ComboPooledDataSource mockSource = Mockito.mock(ComboPooledDataSource.class);
    Mockito.when(mockSource.getNumConnectionsDefaultUser())
        .thenThrow(new SQLException("Test exception"));
    Assertions.assertNotNull(accessor.getInfo(mockSource));
  }

  /**
   * Reset test.
   */
  @Test
  void resetTest() {
    ComboPooledDataSource mockSource = Mockito.mock(ComboPooledDataSource.class);
    Assertions.assertDoesNotThrow(() -> accessor.reset(mockSource));
  }

}
