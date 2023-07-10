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

import java.sql.SQLException;
import java.util.Properties;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import oracle.jdbc.pool.OracleDataSource;

/**
 * The Class OracleDatasourceAccessorTest.
 */
class OracleDatasourceAccessorTest {

  /** The accessor. */
  OracleDatasourceAccessor accessor;

  /** The source. */
  @Mocked
  OracleDataSource source;

  /** The bad source. */
  ComboPooledDataSource badSource;

  /**
   * Before.
   *
   * @throws SQLException the SQL exception
   */
  @BeforeEach
  void before() throws SQLException {
    accessor = new OracleDatasourceAccessor();
    badSource = new ComboPooledDataSource();
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
   * @throws Exception the exception
   */
  @Test
  void getInfoTest() throws Exception {
    new Expectations() {
      {
        source.getConnectionCacheProperties();
        result = new Properties();
      }
    };
    accessor.getInfo(source);
  }

}
