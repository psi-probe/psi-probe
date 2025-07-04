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

import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * The Class OracleDatasourceAccessorTest.
 */
@ExtendWith(MockitoExtension.class)
class OracleDatasourceAccessorTest {

  /** The accessor. */
  OracleDatasourceAccessor accessor;

  /** The source. */
  @Mock
  OracleDataSource source;

  /** The bad source. */
  HikariDataSource badSource;

  /**
   * Before.
   *
   * @throws SQLException the SQL exception
   */
  @BeforeEach
  void before() throws SQLException {
    accessor = new OracleDatasourceAccessor();
    badSource = new HikariDataSource();
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
  void getInfoTest() throws SQLException {
    Mockito.when(source.getConnectionCacheProperties()).thenReturn(new Properties());
    Assertions.assertNotNull(accessor.getInfo(source));
  }

}
