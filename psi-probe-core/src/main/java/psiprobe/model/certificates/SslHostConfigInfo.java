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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SSL Host Configuration Information Class.
 */
public class SslHostConfigInfo implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 8264467511525154728L;

  /** The host name. */
  private String hostName;

  /** The trust store certs. */
  private List<Cert> trustStoreCerts;

  /** Certificate infos. */
  private List<CertificateInfo> certificateInfos;

  /** The ssl protocol. */
  private String protocols;

  /** The ciphers. */
  private String ciphers;

  /** The certificate verification. */
  private String certificateVerification;

  /** The certificate verification depth. */
  private String certificateVerificationDepth;

  /** The insecure renegotiation. */
  private String insecureRenegotiation;

  /** The truststore file. */
  private String truststoreFile;

  /** The truststore password. */
  private String truststorePassword;

  /** The truststore provider. */
  private String truststoreProvider;

  /** The truststore type. */
  private String truststoreType;

  /** The truststore algorithm. */
  private String truststoreAlgorithm;

  /**
   * Gets the hostName.
   *
   * @return the hostName
   */
  public String getHostName() {
    return hostName;
  }

  /**
   * Sets the hostName.
   *
   * @param hostName the new the hostName
   */
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  /**
   * Gets the certificate verification.
   *
   * @return the certificate verification
   */
  public String getCertificateVerification() {
    return certificateVerification;
  }

  /**
   * Sets the certificate verification.
   *
   * @param certificateVerification the new certificate verification
   */
  public void setCertificateVerification(String certificateVerification) {
    this.certificateVerification = certificateVerification;
  }

  /**
   * Gets protocols.
   *
   * @return protocols
   */
  public String getProtocols() {
    return protocols;
  }

  /**
   * Sets protocols.
   *
   * @param protocols new protocols
   */
  public void setProtocols(String protocols) {
    this.protocols = protocols;
  }

  /**
   * Gets the ciphers.
   *
   * @return the ciphers
   */
  public String getCiphers() {
    return ciphers;
  }

  /**
   * Sets the ciphers.
   *
   * @param ciphers the new ciphers
   */
  public void setCiphers(String ciphers) {
    this.ciphers = ciphers;
  }

  /**
   * Gets the certificate verification depth.
   *
   * @return the certificate verification depth
   */
  public String getCertificateVerificationDepth() {
    return certificateVerificationDepth;
  }

  /**
   * Sets the certificate verification depth.
   *
   * @param certificateVerificationDepth the new certificate verification depth
   */
  public void setCertificateVerificationDepth(String certificateVerificationDepth) {
    this.certificateVerificationDepth = certificateVerificationDepth;
  }

  /**
   * Gets the insecure renegotiation.
   *
   * @return the insecure renegotiation
   */
  public String getInsecureRenegotiation() {
    return insecureRenegotiation;
  }

  /**
   * Sets the insecure renegotiation.
   *
   * @param insecureRenegotiation the new insecure renegotiation
   */
  public void setInsecureRenegotiation(String insecureRenegotiation) {
    this.insecureRenegotiation = insecureRenegotiation;
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
  }

  /**
   * Gets the truststore pass.
   *
   * @return the truststore pass
   */
  public String getTruststorePassword() {
    return truststorePassword;
  }

  /**
   * Sets the truststore password.
   *
   * @param truststorePassword the new truststore password
   */
  public void setTruststorePassword(String truststorePassword) {
    this.truststorePassword = truststorePassword;
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
  }

  /**
   * Gets the truststore algorithm.
   *
   * @return the truststore algorithm
   */
  public String getTruststoreAlgorithm() {
    return truststoreAlgorithm;
  }

  /**
   * Sets the truststore algorithm.
   *
   * @param truststoreAlgorithm the new truststore algorithm
   */
  public void setTruststoreAlgorithm(String truststoreAlgorithm) {
    this.truststoreAlgorithm = truststoreAlgorithm;
  }

  /**
   * Gets the trust store certs.
   *
   * @return the trust store certs
   */
  public List<Cert> getTrustStoreCerts() {
    return new ArrayList<>(trustStoreCerts);
  }

  /**
   * Sets the trust store certs.
   *
   * @param trustStoreCerts the new trust store certs
   */
  public void setTrustStoreCerts(List<Cert> trustStoreCerts) {
    this.trustStoreCerts = new ArrayList<>(trustStoreCerts);
  }

  /**
   * Gets certificate infos.
   *
   * @return certificate infos
   */
  public List<CertificateInfo> getCertificateInfos() {
    return new ArrayList<>(certificateInfos);
  }

  /**
   * Sets certificate infos.
   *
   * @param certificateInfos new certificate infos
   */
  public void setCertificateInfos(List<CertificateInfo> certificateInfos) {
    this.certificateInfos = new ArrayList<>(certificateInfos);
  }

}
