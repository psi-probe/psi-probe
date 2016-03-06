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

  private String CrlFile;

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
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getClientAuth() {
    return this.clientAuth;
  }

  public void setClientAuth(String clientAuth) {
    this.clientAuth = clientAuth;
  }

  public String getAlgorithm() {
    return this.algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public String getSslProtocol() {
    return this.sslProtocol;
  }

  public void setSslProtocol(String sslProtocol) {
    this.sslProtocol = sslProtocol;
  }

  public String getCiphers() {
    return this.ciphers;
  }

  public void setCiphers(String ciphers) {
    this.ciphers = ciphers;
  }

  public String getKeyAlias() {
    return this.keyAlias;
  }

  public void setKeyAlias(String keyAlias) {
    this.keyAlias = keyAlias;
  }

  public String getTrustMaxCertLength() {
    return this.trustMaxCertLength;
  }

  public void setTrustMaxCertLength(String trustMaxCertLength) {
    this.trustMaxCertLength = trustMaxCertLength;
  }

  public String getAllowUnsafeLegacyRenegotiation() {
    return this.allowUnsafeLegacyRenegotiation;
  }

  public void setAllowUnsafeLegacyRenegotiation(String allowUnsafeLegacyRenegotiation) {
    this.allowUnsafeLegacyRenegotiation = allowUnsafeLegacyRenegotiation;
  }

  public String getSslImplementationName() {
    return this.sslImplementationName;
  }

  public void setSslImplementationName(String sslImplementationName) {
    this.sslImplementationName = sslImplementationName;
  }

  public String getSessionCacheSize() {
    return this.sessionCacheSize;
  }

  public void setSessionCacheSize(String sessionCacheSize) {
    this.sessionCacheSize = sessionCacheSize;
  }

  public String getCrlFile() {
    return this.CrlFile;
  }

  public void setCrlFile(String crlFile) {
    this.CrlFile = crlFile;
  }

  public String getProtocolType() {
    return this.protocolType;
  }

  public void setProtocolType(String protocolType) {
    this.protocolType = protocolType;
  }

  public String getKeystoreFile() {
    return this.keystoreFile;
  }

  public void setKeystoreFile(String keystoreFile) {
    this.keystoreFile = keystoreFile;
  }

  public String getKeystorePass() {
    return this.keystorePass;
  }

  public void setKeystorePass(String keystorePass) {
    this.keystorePass = keystorePass;
  }

  public String getKeystoreProvider() {
    return this.keystoreProvider;
  }

  public void setKeystoreProvider(String keystoreProvider) {
    this.keystoreProvider = keystoreProvider;
  }

  public String getKeystoreType() {
    return this.keystoreType;
  }

  public void setKeystoreType(String keystoreType) {
    this.keystoreType = keystoreType;
  }

  public String getTruststoreFile() {
    return this.truststoreFile;
  }

  public void setTruststoreFile(String truststoreFile) {
    this.truststoreFile = truststoreFile;
  }

  public String getTruststorePass() {
    return this.truststorePass;
  }

  public void setTruststorePass(String truststorePass) {
    this.truststorePass = truststorePass;
  }

  public String getTruststoreProvider() {
    return this.truststoreProvider;
  }

  public void setTruststoreProvider(String truststoreProvider) {
    this.truststoreProvider = truststoreProvider;
  }

  public String getTruststoreType() {
    return this.truststoreType;
  }

  public void setTruststoreType(String truststoreType) {
    this.truststoreType = truststoreType;
  }

  public String getTruststoreAlgorithm() {
    return this.truststoreAlgorithm;
  }

  public void setTruststoreAlgorithm(String truststoreAlgorithm) {
    this.truststoreAlgorithm = truststoreAlgorithm;
  }

  public List<Cert> getKeyStoreCerts() {
    return this.keyStoreCerts;
  }

  public void setKeyStoreCerts(List<Cert> keyStoreCerts) {
    this.keyStoreCerts = keyStoreCerts;
  }

  public List<Cert> getTrustStoreCerts() {
    return this.trustStoreCerts;
  }

  public void setTrustStoreCerts(List<Cert> trustStoreCerts) {
    this.trustStoreCerts = trustStoreCerts;
  }

}
