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
package psiprobe;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import psiprobe.beans.ContainerWrapperBean;

/**
 * The Class ProbeConfigTest.
 */
@Configuration
@ComponentScan(basePackages = "psiprobe.controllers.certificates")
public class ProbeConfigTest {

  /**
   * Gets the container wrapper bean.
   *
   * @return the container wrapper bean
   */
  @Bean
  public ContainerWrapperBean getContainerWrapperBean() {
    return new ContainerWrapperBean();
  }

}
