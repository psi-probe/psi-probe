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
package psiprobe.model.certificates;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class OldConnectorInfo.
 */
public class OldConnectorInfo extends ConnectorInfo {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -6912444731533511435L;

  /** The keystore file. */
  private String keystoreFile;

  /** The keystore pass. */
  private String keystorePass;

  /** The keystore provider. */
  private String keystoreProvider;

  /** The keystore type. */
  private String keystoreType;

  /** The truststore file. */
  private String truststoreFile;

  /** The truststore pass. */
  private String truststorePass;

  /** The truststore provider. */
  private String truststoreProvider;

  /** The truststore type. */
  private String truststoreType;

  /**
   * Recent version of tomcat have changed how certificate setup works. As such this class supports
   * the Old Connecter Info configuration.
   */
  public OldConnectorInfo() {
    SslHostConfigInfo configInfo = new SslHostConfigInfo();
    List<CertificateInfo> certificateInfos = new ArrayList<>(1);
    certificateInfos.add(new CertificateInfo());
    configInfo.setCertificateInfos(certificateInfos);
    List<SslHostConfigInfo> sslHostConfigInfos = new ArrayList<>(1);
    sslHostConfigInfos.add(configInfo);
    setSslHostConfigInfos(sslHostConfigInfos);
  }

  /**
   * Gets the internal ssl host config info.
   *
   * @return the internal ssl host config info
   */
  private SslHostConfigInfo getInternalSslHostConfigInfo() {
    return getSslHostConfigInfos().get(0);
  }

  /**
   * Gets the internal certificate info.
   *
   * @return the internal certificate info
   */
  private CertificateInfo getInternalCertificateInfo() {
    return getInternalSslHostConfigInfo().getCertificateInfos().get(0);
  }

  /**
   * Gets the keystore file.
   *
   * @return the keystore file
   */
  public String getKeystoreFile() {
    return keystoreFile;
  }

  /**
   * Sets the keystore file.
   *
   * @param keystoreFile the new keystore file
   */
  public void setKeystoreFile(String keystoreFile) {
    this.keystoreFile = keystoreFile;
    getInternalCertificateInfo().setCertificateKeystoreFile(keystoreFile);
  }

  /**
   * Gets the keystore pass.
   *
   * @return the keystore pass
   */
  public String getKeystorePass() {
    return keystorePass;
  }

  /**
   * Sets the keystore pass.
   *
   * @param keystorePass the new keystore pass
   */
  public void setKeystorePass(String keystorePass) {
    this.keystorePass = keystorePass;
    getInternalCertificateInfo().setCertificateKeystorePassword(keystorePass);
  }

  /**
   * Gets the keystore provider.
   *
   * @return the keystore provider
   */
  public String getKeystoreProvider() {
    return keystoreProvider;
  }

  /**
   * Sets the keystore provider.
   *
   * @param keystoreProvider the new keystore provider
   */
  public void setKeystoreProvider(String keystoreProvider) {
    this.keystoreProvider = keystoreProvider;
    getInternalCertificateInfo().setCertificateKeystoreProvider(keystoreProvider);
  }

  /**
   * Gets the keystore type.
   *
   * @return the keystore type
   */
  public String getKeystoreType() {
    return keystoreType;
  }

  /**
   * Sets the keystore type.
   *
   * @param keystoreType the new keystore type
   */
  public void setKeystoreType(String keystoreType) {
    this.keystoreType = keystoreType;
    getInternalCertificateInfo().setCertificateKeystoreType(keystoreType);
  }

  /**
   * Gets the truststore file.
   *
   * @return the truststore file
   */
  public String getTruststoreFile() {
    return truststoreFile;
  }

  /**
   * Sets the truststore file.
   *
   * @param truststoreFile the new truststore file
   */
  public void setTruststoreFile(String truststoreFile) {
    this.truststoreFile = truststoreFile;
    getInternalSslHostConfigInfo().setTruststoreFile(truststoreFile);
  }

  /**
   * Gets the truststore pass.
   *
   * @return the truststore pass
   */
  public String getTruststorePass() {
    return truststorePass;
  }

  /**
   * Sets the truststore pass.
   *
   * @param truststorePass the new truststore pass
   */
  public void setTruststorePass(String truststorePass) {
    this.truststorePass = truststorePass;
    getInternalSslHostConfigInfo().setTruststorePassword(truststorePass);
  }

  /**
   * Gets the truststore provider.
   *
   * @return the truststore provider
   */
  public String getTruststoreProvider() {
    return truststoreProvider;
  }

  /**
   * Sets the truststore provider.
   *
   * @param truststoreProvider the new truststore provider
   */
  public void setTruststoreProvider(String truststoreProvider) {
    this.truststoreProvider = truststoreProvider;
    getInternalSslHostConfigInfo().setTruststoreProvider(truststoreProvider);
  }

  /**
   * Gets the truststore type.
   *
   * @return the truststore type
   */
  public String getTruststoreType() {
    return truststoreType;
  }

  /**
   * Sets the truststore type.
   *
   * @param truststoreType the new truststore type
   */
  public void setTruststoreType(String truststoreType) {
    this.truststoreType = truststoreType;
    getInternalSslHostConfigInfo().setTruststoreType(truststoreType);
  }

}
