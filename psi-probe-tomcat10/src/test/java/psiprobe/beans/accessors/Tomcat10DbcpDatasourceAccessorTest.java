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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.SQLException;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Test;

/**
 * The Class Tomcat10DbcpDatasourceAccessorTest.
 */
class Tomcat10DbcpDatasourceAccessorTest {

  /**
   * Tomcat 10 dbcp datasource accessor.
   *
   * @throws SQLException the sql exception
   */
  @Test
  void Tomcat10DbcpDatasourceAccessor() throws SQLException {
    Tomcat10DbcpDatasourceAccessor accessor = new Tomcat10DbcpDatasourceAccessor();
    try (BasicDataSource source = new BasicDataSource()) {
      assertEquals("tomcat-dbcp2", accessor.getInfo(source).getType());
    }
  }

  /**
   * Tomcat 10 dbcp datasource accessor invalid.
   *
   * @throws SQLException the sql exception
   */
  @Test
  void Tomcat10DbcpDatasourceAccessorInvalid() throws SQLException {
    Tomcat10DbcpDatasourceAccessor accessor = new Tomcat10DbcpDatasourceAccessor();
    assertNull(accessor.getInfo(new Object()));
  }

  /**
   * Reset.
   */
  @Test
  void reset() {
    Tomcat10DbcpDatasourceAccessor accessor = new Tomcat10DbcpDatasourceAccessor();
    assertFalse(accessor.reset(new Object()));
  }

}
