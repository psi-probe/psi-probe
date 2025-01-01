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
package psiprobe.controllers.certificates;

import static org.assertj.core.api.Assertions.assertThat;

import com.codebox.bean.JavaBeanTester;

import jakarta.inject.Inject;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import psiprobe.ProbeInitializer;
import psiprobe.model.certificates.Cert;

/**
 * The Class ListCertificatesControllerTest.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ProbeInitializer.class)
@WebAppConfiguration
class ListCertificatesControllerTest {

  /** The ctx. */
  @Inject
  private WebApplicationContext ctx;

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(ListCertificatesController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Test get certificates.
   *
   * @throws Exception the exception
   */
  @Test
  void certificatesTest() throws Exception {
    ListCertificatesController controller = new ListCertificatesController();

    String storeType = "jks";
    File storeFile = ctx.getResource("classpath:certs/localhost-truststore.jks").getFile();
    String storePassword = "123456";

    List<Cert> certs = controller.getCertificates(storeType, storeFile.toString(), storePassword);

    assertThat(certs).doesNotContainNull().hasSize(2);
    assertThat(certs.get(0).getAlias()).isEqualTo("*.google.com");
    assertThat(certs.get(1).getAlias()).isEqualTo("google_g2_2017");
  }

  /**
   * Test get certificates relative.
   *
   * @throws Exception the exception
   */
  @Test
  void certificatesRelativeTest() throws Exception {
    ListCertificatesController controller = new ListCertificatesController();

    String storeType = "jks";
    File certFolder = ctx.getResource("classpath:certs").getFile();
    System.setProperty("catalina.base", certFolder.getPath());

    String storePassword = "123456";

    List<Cert> certs =
        controller.getCertificates(storeType, "localhost-truststore.jks", storePassword);

    assertThat(certs).doesNotContainNull().hasSize(2);
    assertThat(certs.get(0).getAlias()).isEqualTo("*.google.com");
    assertThat(certs.get(1).getAlias()).isEqualTo("google_g2_2017");
  }

  /**
   * Test get certificates relative uri.
   *
   * @throws Exception the exception
   */
  @Test
  void certificatesRelativeUriTest() throws Exception {
    ListCertificatesController controller = new ListCertificatesController();

    String storeType = "jks";
    File storeFile = ctx.getResource("classpath:certs/localhost-truststore.jks").getFile();
    File certFolder = ctx.getResource("classpath:certs").getFile();
    System.setProperty("catalina.base", certFolder.getPath());

    String storePassword = "123456";

    List<Cert> certs = controller.getCertificates(storeType, storeFile.getPath(), storePassword);

    assertThat(certs).doesNotContainNull().hasSize(2);
    assertThat(certs.get(0).getAlias()).isEqualTo("*.google.com");
    assertThat(certs.get(1).getAlias()).isEqualTo("google_g2_2017");
  }

  /**
   * Test get certificates absolute uri.
   *
   * @throws Exception the exception
   */
  @Test
  void certificatesAbsoluteUriTest() throws Exception {
    ListCertificatesController controller = new ListCertificatesController();

    String storeType = "jks";
    File certFolder = ctx.getResource("classpath:certs").getFile();
    System.setProperty("catalina.base", certFolder.getPath());

    String storePassword = "123456";

    List<Cert> certs =
        controller.getCertificates(storeType, "./localhost-truststore.jks", storePassword);

    assertThat(certs).doesNotContainNull().hasSize(2);
    assertThat(certs.get(0).getAlias()).isEqualTo("*.google.com");
    assertThat(certs.get(1).getAlias()).isEqualTo("google_g2_2017");
  }

}
