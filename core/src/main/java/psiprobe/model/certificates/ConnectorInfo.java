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

public class ConnectorInfo implements Serializable {

  private static final long serialVersionUID = 5927447793822367835L;

  private List<Cert> keyStoreCerts;

  private List<Cert> trustStoreCerts;

  private String name;

  private String clientAuth;

  private String algorithm;

  private String sslProtocol;

  private String ciphers;

  private String keyAlias;

  private String trustMaxCertLength;

  private String allowUnsafeLegacyRenegotiation;

  private String sslImplementationName;

  private String sessionCacheSize;

  private String crlFile;

  private String protocolType;

  private String keystoreFile;

  private String keystorePass;

  private String keystoreProvider;

  private String keystoreType;

  private String truststoreFile;

  private String truststorePass;

  private String truststoreProvider;

  private String truststoreType;

  private String truststoreAlgorithm;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getClientAuth() {
    return clientAuth;
  }

  public void setClientAuth(String clientAuth) {
    this.clientAuth = clientAuth;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public String getSslProtocol() {
    return sslProtocol;
  }

  public void setSslProtocol(String sslProtocol) {
    this.sslProtocol = sslProtocol;
  }

  public String getCiphers() {
    return ciphers;
  }

  public void setCiphers(String ciphers) {
    this.ciphers = ciphers;
  }

  public String getKeyAlias() {
    return keyAlias;
  }

  public void setKeyAlias(String keyAlias) {
    this.keyAlias = keyAlias;
  }

  public String getTrustMaxCertLength() {
    return trustMaxCertLength;
  }

  public void setTrustMaxCertLength(String trustMaxCertLength) {
    this.trustMaxCertLength = trustMaxCertLength;
  }

  public String getAllowUnsafeLegacyRenegotiation() {
    return allowUnsafeLegacyRenegotiation;
  }

  public void setAllowUnsafeLegacyRenegotiation(String allowUnsafeLegacyRenegotiation) {
    this.allowUnsafeLegacyRenegotiation = allowUnsafeLegacyRenegotiation;
  }

  public String getSslImplementationName() {
    return sslImplementationName;
  }

  public void setSslImplementationName(String sslImplementationName) {
    this.sslImplementationName = sslImplementationName;
  }

  public String getSessionCacheSize() {
    return sessionCacheSize;
  }

  public void setSessionCacheSize(String sessionCacheSize) {
    this.sessionCacheSize = sessionCacheSize;
  }

  public String getCrlFile() {
    return crlFile;
  }

  public void setCrlFile(String crlFile) {
    this.crlFile = crlFile;
  }

  public String getProtocolType() {
    return protocolType;
  }

  public void setProtocolType(String protocolType) {
    this.protocolType = protocolType;
  }

  public String getKeystoreFile() {
    return keystoreFile;
  }

  public void setKeystoreFile(String keystoreFile) {
    this.keystoreFile = keystoreFile;
  }

  public String getKeystorePass() {
    return keystorePass;
  }

  public void setKeystorePass(String keystorePass) {
    this.keystorePass = keystorePass;
  }

  public String getKeystoreProvider() {
    return keystoreProvider;
  }

  public void setKeystoreProvider(String keystoreProvider) {
    this.keystoreProvider = keystoreProvider;
  }

  public String getKeystoreType() {
    return keystoreType;
  }

  public void setKeystoreType(String keystoreType) {
    this.keystoreType = keystoreType;
  }

  public String getTruststoreFile() {
    return truststoreFile;
  }

  public void setTruststoreFile(String truststoreFile) {
    this.truststoreFile = truststoreFile;
  }

  public String getTruststorePass() {
    return truststorePass;
  }

  public void setTruststorePass(String truststorePass) {
    this.truststorePass = truststorePass;
  }

  public String getTruststoreProvider() {
    return truststoreProvider;
  }

  public void setTruststoreProvider(String truststoreProvider) {
    this.truststoreProvider = truststoreProvider;
  }

  public String getTruststoreType() {
    return truststoreType;
  }

  public void setTruststoreType(String truststoreType) {
    this.truststoreType = truststoreType;
  }

  public String getTruststoreAlgorithm() {
    return truststoreAlgorithm;
  }

  public void setTruststoreAlgorithm(String truststoreAlgorithm) {
    this.truststoreAlgorithm = truststoreAlgorithm;
  }

  public List<Cert> getKeyStoreCerts() {
    return keyStoreCerts;
  }

  public void setKeyStoreCerts(List<Cert> keyStoreCerts) {
    this.keyStoreCerts = keyStoreCerts;
  }

  public List<Cert> getTrustStoreCerts() {
    return trustStoreCerts;
  }

  public void setTrustStoreCerts(List<Cert> trustStoreCerts) {
    this.trustStoreCerts = trustStoreCerts;
  }

}
