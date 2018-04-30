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
package psiprobe.controllers.cluster;

import com.codebox.bean.JavaBeanTester;
import org.junit.jupiter.api.Test;

/**
 * The Class ClusterStatsControllerTest.
 */
public class ClusterStatsControllerTest {

  /**
   * Javabean tester cluster stats.
   */
  @Test
  public void javabeanTesterClusterStats() {
    JavaBeanTester.builder(ClusterStatsController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester cluster stats members.
   */
  @Test
  public void javabeanTesterClusterStatsMembers() {
    JavaBeanTester.builder(ClusterMembersStatsController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester cluster stats requests.
   */
  @Test
  public void javabeanTesterClusterStatsRequests() {
    JavaBeanTester.builder(ClusterRequestsStatsController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester cluster stats traffic.
   */
  @Test
  public void javabeanTesterClusterStatsTraffic() {
    JavaBeanTester.builder(ClusterTrafficStatsController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
