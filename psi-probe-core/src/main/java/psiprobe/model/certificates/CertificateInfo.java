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
 * Certificate Information Class.
 */
public class CertificateInfo implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 8528148811876736528L;

  /** The key store certs. */
  private List<Cert> keyStoreCerts;

  /** The certificate key alias. */
  private String certificateKeyAlias;

  /** The certificate keystore file. */
  private String certificateKeystoreFile;

  /** The certificate keystore password. */
  private String certificateKeystorePassword;

  /** The certificate keystore provider. */
  private String certificateKeystoreProvider;

  /** The certificate keystore type. */
  private String certificateKeystoreType;

  /**
   * Gets the key store certs.
   *
   * @return the key store certs
   */
  public List<Cert> getKeyStoreCerts() {
    return keyStoreCerts == null ? null : new ArrayList<>(keyStoreCerts);
  }

  /**
   * Sets the key store certs.
   *
   * @param keyStoreCerts the new key store certs
   */
  public void setKeyStoreCerts(List<Cert> keyStoreCerts) {
    this.keyStoreCerts = keyStoreCerts == null ? null : new ArrayList<>(keyStoreCerts);
  }

  /**
   * Gets the certificate key alias.
   *
   * @return the certificate key alias
   */
  public String getKeyAlias() {
    return certificateKeyAlias;
  }

  /**
   * Sets the certificate key alias.
   *
   * @param keyAlias the new certificate key alias
   */
  public void setKeyAlias(String keyAlias) {
    this.certificateKeyAlias = keyAlias;
  }

  /**
   * Gets the certificate keystore file.
   *
   * @return the certificate keystore file
   */
  public String getCertificateKeystoreFile() {
    return certificateKeystoreFile;
  }

  /**
   * Sets the certificate keystore file.
   *
   * @param keystoreFile the new certificate keystore file
   */
  public void setCertificateKeystoreFile(String keystoreFile) {
    this.certificateKeystoreFile = keystoreFile;
  }

  /**
   * Gets the certificate keystore password.
   *
   * @return the certificate keystore password
   */
  public String getCertificateKeystorePassword() {
    return certificateKeystorePassword;
  }

  /**
   * Sets the certificate keystore password.
   *
   * @param keystorePass the new certificate keystore password
   */
  public void setCertificateKeystorePassword(String keystorePass) {
    this.certificateKeystorePassword = keystorePass;
  }

  /**
   * Gets the certificate keystore provider.
   *
   * @return the certificate keystore provider
   */
  public String getCertificateKeystoreProvider() {
    return certificateKeystoreProvider;
  }

  /**
   * Sets the certificate keystore provider.
   *
   * @param keystoreProvider the new certificate keystore provider
   */
  public void setCertificateKeystoreProvider(String keystoreProvider) {
    this.certificateKeystoreProvider = keystoreProvider;
  }

  /**
   * Gets the certificate keystore type.
   *
   * @return the certificate keystore type
   */
  public String getCertificateKeystoreType() {
    return certificateKeystoreType;
  }

  /**
   * Sets the certificate keystore type.
   *
   * @param keystoreType the new certificate keystore type
   */
  public void setCertificateKeystoreType(String keystoreType) {
    this.certificateKeystoreType = keystoreType;
  }

}
