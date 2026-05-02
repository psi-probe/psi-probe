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
package psiprobe.beans;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import psiprobe.model.jmx.RuntimeInformation;

/**
 * Tests for {@link RuntimeInfoAccessorBean}.
 */
class RuntimeInfoAccessorBeanTest {

  @Test
  void testGetRuntimeInformation() {
    RuntimeInfoAccessorBean bean = new RuntimeInfoAccessorBean();
    RuntimeInformation ri = bean.getRuntimeInformation();
    // Should return non-null runtime information from the JMX platform
    assertNotNull(ri);
    // Should have valid start time
    assertNotNull(ri.getStartTime());
  }
}
