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
package psiprobe.controllers.connectors;

import com.codebox.bean.JavaBeanTester;
import org.junit.jupiter.api.Test;

/**
 * The Class GetConnectorControllerTest.
 */
public class GetConnectorControllerTest {

  /**
   * Javabean tester proc time.
   */
  @Test
  public void javabeanTesterProcTime() {
    JavaBeanTester.builder(GetConnectorProcTimeController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester request.
   */
  @Test
  public void javabeanTesterRequest() {
    JavaBeanTester.builder(GetConnectorRequestController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester traffic.
   */
  @Test
  public void javabeanTesterTraffic() {
    JavaBeanTester.builder(GetConnectorTrafficController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
