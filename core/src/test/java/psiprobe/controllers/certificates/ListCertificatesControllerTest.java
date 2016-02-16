package psiprobe.controllers.certificates;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.certificates.Cert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/spring/test-controllers.xml")
public class ListCertificatesControllerTest {

  @Autowired
  private ApplicationContext ctx;
  
  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ListCertificatesController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  @Test
  public void testGetCertificates() throws Exception {
    ListCertificatesController controller = new ListCertificatesController();

    String storeType = "jks";
    File storeFile = ctx.getResource("classpath:certs/localhost-truststore.jks").getFile();
    String storePassword = "123456";

    List<Cert> certs = controller.getCertificates(storeType, storeFile, storePassword);

    assertThat(certs, notNullValue());
    assertThat(certs.size(), is(2));
    assertThat(certs.get(0).getAlias(), is("google internet authority g2"));
    assertThat(certs.get(1).getAlias(), is("*.google.com"));
  }

}
