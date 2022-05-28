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
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Test;

/**
 * The Class Tomcat101DbcpDatasourceAccessorTest.
 */
class Tomcat101DbcpDatasourceAccessorTest {

  /**
   * Tomcat 101 dbcp datasource accessor.
   *
   * @throws Exception the exception
   */
  @Test
  void Tomcat101DbcpDatasourceAccessor() throws Exception {
    Tomcat101DbcpDatasourceAccessor accessor = new Tomcat101DbcpDatasourceAccessor();
    try (BasicDataSource source = new BasicDataSource()) {
      assertEquals("tomcat-dbcp2", accessor.getInfo(source).getType());
    }
  }

  /**
   * Tomcat 101 dbcp datasource accessor invalid.
   *
   * @throws Exception the exception
   */
  @Test
  void Tomcat101DbcpDatasourceAccessorInvalid() throws Exception {
    Tomcat101DbcpDatasourceAccessor accessor = new Tomcat101DbcpDatasourceAccessor();
    assertNull(accessor.getInfo(new Object()));
  }

  /**
   * Reset.
   *
   * @throws Exception the exception
   */
  @Test
  void reset() throws Exception {
    Tomcat101DbcpDatasourceAccessor accessor = new Tomcat101DbcpDatasourceAccessor();
    assertFalse(accessor.reset(new Object()));
  }

}
