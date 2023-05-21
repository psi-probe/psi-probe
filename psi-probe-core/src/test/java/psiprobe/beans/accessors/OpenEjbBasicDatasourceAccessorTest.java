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

import mockit.Mocked;
import mockit.Tested;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * The Class OpenEjbBasicDatasourceAccessorTest.
 */
class OpenEjbBasicDatasourceAccessorTest {

  /** The accessor. */
  @Tested
  OpenEjbBasicDatasourceAccessor accessor;

  /** The source. */
  @Mocked
  BasicDataSource source;

  /** The bad source. */
  ComboPooledDataSource badSource;

  /**
   * Before.
   */
  @BeforeEach
  void before() {
    accessor = new OpenEjbBasicDatasourceAccessor();
    badSource = new ComboPooledDataSource();
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
   * @throws Exception the exception
   */
  @Test
  void getInfoTest() throws Exception {
    accessor.getInfo(source);
  }

}
