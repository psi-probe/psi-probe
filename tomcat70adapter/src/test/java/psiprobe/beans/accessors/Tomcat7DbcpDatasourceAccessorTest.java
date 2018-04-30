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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.junit.jupiter.api.Test;

/**
 * The Class Tomcat7DbcpDatasourceAccessorTest.
 */
public class Tomcat7DbcpDatasourceAccessorTest {

  /**
   * Tomcat 7 dbcp datasource accessor.
   *
   * @throws Exception the exception
   */
  @Test
  public void Tomcat7DbcpDatasourceAccessor() throws Exception {
    Tomcat7DbcpDatasourceAccessor accessor = new Tomcat7DbcpDatasourceAccessor();
    assertEquals("tomcat-dbcp", accessor.getInfo(new BasicDataSource()).getType());
  }

  /**
   * Tomcat 7 dbcp datasource accessor invalid.
   *
   * @throws Exception the exception
   */
  @Test
  public void Tomcat7DbcpDatasourceAccessorInvalid() throws Exception {
    Tomcat7DbcpDatasourceAccessor accessor = new Tomcat7DbcpDatasourceAccessor();
    assertNull(accessor.getInfo(new Object()));
  }

  /**
   * Reset.
   *
   * @throws Exception the exception
   */
  @Test
  public void reset() throws Exception {
    Tomcat7DbcpDatasourceAccessor accessor = new Tomcat7DbcpDatasourceAccessor();
    assertFalse(accessor.reset(new Object()));
  }

}
