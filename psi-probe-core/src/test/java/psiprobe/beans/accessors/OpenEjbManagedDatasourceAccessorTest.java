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

import mockit.Mocked;
import mockit.Tested;

import org.apache.openejb.resource.jdbc.managed.local.ManagedDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * The Class OpenEjbManagedDatasourceAccessorTest.
 */
class OpenEjbManagedDatasourceAccessorTest {

  /** The accessor. */
  @Tested
  OpenEjbManagedDatasourceAccessor accessor;

  /** The source. */
  @Mocked
  ManagedDataSource source;

  /** The bad source. */
  HikariDataSource badSource;

  /**
   * Before.
   */
  @BeforeEach
  void before() {
    accessor = new OpenEjbManagedDatasourceAccessor();
    badSource = new HikariDataSource();
  }

  /**
   * Can map test.
   */
  @Disabled
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
    accessor.getInfo(source);
  }

}
