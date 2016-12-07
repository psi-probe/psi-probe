/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.model.certificates;

import java.io.Serializable;
import java.util.List;

/**
 * The Class ConnectorInfo.
 */
public class ConnectorInfo implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 5927447793822367835L;

  /** The key store certs. */
  private List<Cert> keyStoreCerts;

  /** The trust store certs. */
  private List<Cert> trustStoreCerts;

  /** The name. */
  private String name;

  /** The client auth. */
  private String clientAuth;

  /** The algorithm. */
  private String algorithm;

  /** The ssl protocol. */
  private String sslProtocol;

  /** The ciphers. */
  private String ciphers;

  /** The key alias. */
  private String keyAlias;

  /** The trust max cert length. */
  private String trustMaxCertLength;

  /** The allow unsafe legacy renegotiation. */
  private String allowUnsafeLegacyRenegotiation;

  /** The ssl implementation name. */
  private String sslImplementationName;

  /** The session cache size. */
  private String sessionCacheSize;

  /** The crl file. */
  private String crlFile;

  /** The protocol type. */
  private String protocolType;

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

  /** The truststore algorithm. */
  private String truststoreAlgorithm;

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the client auth.
   *
   * @return the client auth
   */
  public String getClientAuth() {
    return clientAuth;
  }

  /**
   * Sets the client auth.
   *
   * @param clientAuth the new client auth
   */
  public void setClientAuth(String clientAuth) {
    this.clientAuth = clientAuth;
  }

  /**
   * Gets the algorithm.
   *
   * @return the algorithm
   */
  public String getAlgorithm() {
    return algorithm;
  }

  /**
   * Sets the algorithm.
   *
   * @param algorithm the new algorithm
   */
  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  /**
   * Gets the ssl protocol.
   *
   * @return the ssl protocol
   */
  public String getSslProtocol() {
    return sslProtocol;
  }

  /**
   * Sets the ssl protocol.
   *
   * @param sslProtocol the new ssl protocol
   */
  public void setSslProtocol(String sslProtocol) {
    this.sslProtocol = sslProtocol;
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
   * Gets the key alias.
   *
   * @return the key alias
   */
  public String getKeyAlias() {
    return keyAlias;
  }

  /**
   * Sets the key alias.
   *
   * @param keyAlias the new key alias
   */
  public void setKeyAlias(String keyAlias) {
    this.keyAlias = keyAlias;
  }

  /**
   * Gets the trust max cert length.
   *
   * @return the trust max cert length
   */
  public String getTrustMaxCertLength() {
    return trustMaxCertLength;
  }

  /**
   * Sets the trust max cert length.
   *
   * @param trustMaxCertLength the new trust max cert length
   */
  public void setTrustMaxCertLength(String trustMaxCertLength) {
    this.trustMaxCertLength = trustMaxCertLength;
  }

  /**
   * Gets the allow unsafe legacy renegotiation.
   *
   * @return the allow unsafe legacy renegotiation
   */
  public String getAllowUnsafeLegacyRenegotiation() {
    return allowUnsafeLegacyRenegotiation;
  }

  /**
   * Sets the allow unsafe legacy renegotiation.
   *
   * @param allowUnsafeLegacyRenegotiation the new allow unsafe legacy renegotiation
   */
  public void setAllowUnsafeLegacyRenegotiation(String allowUnsafeLegacyRenegotiation) {
    this.allowUnsafeLegacyRenegotiation = allowUnsafeLegacyRenegotiation;
  }

  /**
   * Gets the ssl implementation name.
   *
   * @return the ssl implementation name
   */
  public String getSslImplementationName() {
    return sslImplementationName;
  }

  /**
   * Sets the ssl implementation name.
   *
   * @param sslImplementationName the new ssl implementation name
   */
  public void setSslImplementationName(String sslImplementationName) {
    this.sslImplementationName = sslImplementationName;
  }

  /**
   * Gets the session cache size.
   *
   * @return the session cache size
   */
  public String getSessionCacheSize() {
    return sessionCacheSize;
  }

  /**
   * Sets the session cache size.
   *
   * @param sessionCacheSize the new session cache size
   */
  public void setSessionCacheSize(String sessionCacheSize) {
    this.sessionCacheSize = sessionCacheSize;
  }

  /**
   * Gets the crl file.
   *
   * @return the crl file
   */
  public String getCrlFile() {
    return crlFile;
  }

  /**
   * Sets the crl file.
   *
   * @param crlFile the new crl file
   */
  public void setCrlFile(String crlFile) {
    this.crlFile = crlFile;
  }

  /**
   * Gets the protocol type.
   *
   * @return the protocol type
   */
  public String getProtocolType() {
    return protocolType;
  }

  /**
   * Sets the protocol type.
   *
   * @param protocolType the new protocol type
   */
  public void setProtocolType(String protocolType) {
    this.protocolType = protocolType;
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
   * Gets the key store certs.
   *
   * @return the key store certs
   */
  public List<Cert> getKeyStoreCerts() {
    return keyStoreCerts;
  }

  /**
   * Sets the key store certs.
   *
   * @param keyStoreCerts the new key store certs
   */
  public void setKeyStoreCerts(List<Cert> keyStoreCerts) {
    this.keyStoreCerts = keyStoreCerts;
  }

  /**
   * Gets the trust store certs.
   *
   * @return the trust store certs
   */
  public List<Cert> getTrustStoreCerts() {
    return trustStoreCerts;
  }

  /**
   * Sets the trust store certs.
   *
   * @param trustStoreCerts the new trust store certs
   */
  public void setTrustStoreCerts(List<Cert> trustStoreCerts) {
    this.trustStoreCerts = trustStoreCerts;
  }

}
