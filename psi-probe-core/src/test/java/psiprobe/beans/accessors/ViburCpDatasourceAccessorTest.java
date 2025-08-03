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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vibur.dbcp.ViburDBCPDataSource;
import org.vibur.objectpool.PoolService;

/**
 * The Class ViburCpDatasourceAccessorTest.
 */
@ExtendWith(MockitoExtension.class)
class ViburCpDatasourceAccessorTest {

  /** The accessor. */
  ViburCpDatasourceAccessor accessor;

  /** The source. */
  @Mock
  ViburDBCPDataSource source;

  /** The bad source. */
  HikariDataSource badSource;

  /**
   * Before.
   */
  @BeforeEach
  void before() {
    accessor = new ViburCpDatasourceAccessor();
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
  @SuppressWarnings("unchecked")
  @Test
  void getInfoTest() throws SQLException {
    var poolService = Mockito.mock(PoolService.class);
    Mockito.when(source.getPool()).thenReturn(poolService);
    Mockito.when(poolService.taken()).thenReturn(1);
    Mockito.when(poolService.remainingCreated()).thenReturn(1);

    Assertions.assertNotNull(accessor.getInfo(source));
  }

}
