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
package psiprobe.controllers.certificates;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.codebox.bean.JavaBeanTester;

import psiprobe.ProbeConfig;
import psiprobe.model.certificates.Cert;

/**
 * The Class ListCertificatesControllerTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ProbeConfig.class)
@WebAppConfiguration
public class ListCertificatesControllerTest {

  /** The ctx. */
  @Inject
  private ApplicationContext ctx;

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ListCertificatesController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Test get certificates.
   *
   * @throws Exception the exception
   */
  @Test
  public void testGetCertificates() throws Exception {
    ListCertificatesController controller = new ListCertificatesController();

    String storeType = "jks";
    File storeFile = ctx.getResource("classpath:certs/localhost-truststore.jks").getFile();
    String storePassword = "123456";

    List<Cert> certs = controller.getCertificates(storeType, storeFile.toString(), storePassword);

    assertThat(certs, notNullValue());
    assertThat(certs.size(), is(2));
    assertThat(certs.get(0).getAlias(), is("google internet authority g2"));
    assertThat(certs.get(1).getAlias(), is("*.google.com"));
  }

  /**
   * Test get certificates relative.
   *
   * @throws Exception the exception
   */
  @Test
  public void testGetCertificatesRelative() throws Exception {
    ListCertificatesController controller = new ListCertificatesController();

    String storeType = "jks";
    File certFolder = ctx.getResource("classpath:certs").getFile();
    System.setProperty("catalina.base", certFolder.getPath());

    String storePassword = "123456";
    
    List<Cert> certs = controller.getCertificates(storeType, "localhost-truststore.jks", storePassword);
    
    assertThat(certs, notNullValue());
    assertThat(certs.size(), is(2));
    assertThat(certs.get(0).getAlias(), is("google internet authority g2"));
    assertThat(certs.get(1).getAlias(), is("*.google.com"));
  }

  /**
   * Test get certificates relative uri.
   *
   * @throws Exception the exception
   */
  @Test
  public void testGetCertificatesRelativeUri() throws Exception {
    ListCertificatesController controller = new ListCertificatesController();

    String storeType = "jks";
    File storeFile = ctx.getResource("classpath:certs/localhost-truststore.jks").getFile();
    File certFolder = ctx.getResource("classpath:certs").getFile();
    System.setProperty("catalina.base", certFolder.getPath());

    String storePassword = "123456";

    List<Cert> certs = controller.getCertificates(storeType, storeFile.toURI().toString(), storePassword);

    assertThat(certs, notNullValue());
    assertThat(certs.size(), is(2));
    assertThat(certs.get(0).getAlias(), is("google internet authority g2"));
    assertThat(certs.get(1).getAlias(), is("*.google.com"));
  }

  /**
   * Test get certificates absolute uri.
   *
   * @throws Exception the exception
   */
  @Test
  public void testGetCertificatesAbsoluteUri() throws Exception {
    ListCertificatesController controller = new ListCertificatesController();

    String storeType = "jks";
    File certFolder = ctx.getResource("classpath:certs").getFile();
    System.setProperty("catalina.base", certFolder.getPath());

    String storePassword = "123456";

    List<Cert> certs = controller.getCertificates(storeType, "./localhost-truststore.jks", storePassword);

    assertThat(certs, notNullValue());
    assertThat(certs.size(), is(2));
    assertThat(certs.get(0).getAlias(), is("google internet authority g2"));
    assertThat(certs.get(1).getAlias(), is("*.google.com"));
  }

}
