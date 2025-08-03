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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import psiprobe.model.certificates.Cert;
import psiprobe.model.certificates.CertificateInfo;
import psiprobe.model.certificates.SslHostConfigInfo;

/**
 * The Class SslHostConfigInfoTest.
 */
class SslHostConfigInfoTest {

  /**
   * Test string properties.
   */
  @Test
  void testStringProperties() {
    SslHostConfigInfo info = new SslHostConfigInfo();

    info.setHostName("localhost");
    assertEquals("localhost", info.getHostName());

    info.setCertificateVerification("REQUIRED");
    assertEquals("REQUIRED", info.getCertificateVerification());

    info.setProtocols("TLSv1.2,TLSv1.3");
    assertEquals("TLSv1.2,TLSv1.3", info.getProtocols());

    info.setCiphers("AES128");
    assertEquals("AES128", info.getCiphers());

    info.setCertificateVerificationDepth("2");
    assertEquals("2", info.getCertificateVerificationDepth());

    info.setInsecureRenegotiation("false");
    assertEquals("false", info.getInsecureRenegotiation());

    info.setTruststoreFile("/tmp/truststore.jks");
    assertEquals("/tmp/truststore.jks", info.getTruststoreFile());

    info.setTruststorePassword("secret");
    assertEquals("secret", info.getTruststorePassword());

    info.setTruststoreProvider("SUN");
    assertEquals("SUN", info.getTruststoreProvider());

    info.setTruststoreType("JKS");
    assertEquals("JKS", info.getTruststoreType());

    info.setTruststoreAlgorithm("PKIX");
    assertEquals("PKIX", info.getTruststoreAlgorithm());
  }

  /**
   * Test trust store certs.
   */
  @Test
  void testTrustStoreCerts() {
    SslHostConfigInfo info = new SslHostConfigInfo();
    Cert cert1 = new Cert();
    Cert cert2 = new Cert();

    info.setTrustStoreCerts(Arrays.asList(cert1, cert2));
    List<Cert> certs = info.getTrustStoreCerts();
    assertEquals(2, certs.size());
    assertTrue(certs.contains(cert1));
    assertTrue(certs.contains(cert2));

    // Defensive copy
    certs.clear();
    assertEquals(2, info.getTrustStoreCerts().size());

    // Null handling
    info.setTrustStoreCerts(null);
    assertNotNull(info.getTrustStoreCerts());
    assertTrue(info.getTrustStoreCerts().isEmpty());
  }

  /**
   * Test certificate infos.
   */
  @Test
  void testCertificateInfos() {
    SslHostConfigInfo info = new SslHostConfigInfo();
    CertificateInfo ci1 = new CertificateInfo();
    CertificateInfo ci2 = new CertificateInfo();

    info.setCertificateInfos(Arrays.asList(ci1, ci2));
    List<CertificateInfo> infos = info.getCertificateInfos();
    assertEquals(2, infos.size());
    assertTrue(infos.contains(ci1));
    assertTrue(infos.contains(ci2));

    // Defensive copy
    infos.clear();
    assertEquals(2, info.getCertificateInfos().size());

    // Null handling
    info.setCertificateInfos(null);
    assertNotNull(info.getCertificateInfos());
    assertTrue(info.getCertificateInfos().isEmpty());
  }
}
