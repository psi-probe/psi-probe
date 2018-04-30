/**
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.vibur.dbcp.ViburDBCPDataSource;
import mockit.Expectations;
import mockit.Mocked;

/**
 * The Class ViburCpDatasourceAccessorTest.
 */
public class ViburCpDatasourceAccessorTest {

  /** The accessor. */
  ViburCpDatasourceAccessor accessor;

  /** The source. */
  @Mocked
  ViburDBCPDataSource source;

  /** The bad source. */
  ComboPooledDataSource badSource;

  /**
   * Before.
   */
  @BeforeEach
  public void before() {
    accessor = new ViburCpDatasourceAccessor();
    badSource = new ComboPooledDataSource();
  }

  /**
   * Can map test.
   */
  @Test
  public void canMapTest() {
    Assertions.assertTrue(accessor.canMap(source));
  }

  /**
   * Cannot map test.
   */
  @Test
  public void cannotMapTest() {
    Assertions.assertFalse(accessor.canMap(badSource));
  }

  /**
   * Gets the info test.
   *
   * @throws Exception the exception
   */
  @Disabled
  @Test
  public void getInfoTest() throws Exception {
    new Expectations() {
      {
        source.getJmxName();
        result = "viburJmx";
      }
    };
    accessor.getInfo(source);
  }

}
