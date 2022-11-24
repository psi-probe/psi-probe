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
import java.time.Instant;
import java.util.Date;

/**
 * The Class Cert.
 */
public class Cert implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -727528588030989042L;

  /** The alias. */
  private String alias;

  /** The subject distinguished name. */
  private String subjectDistinguishedName;

  /** The issuer distinguished name. */
  private String issuerDistinguishedName;

  /** The not before. */
  private Instant notBefore;

  /** The not after. */
  private Instant notAfter;

  /**
   * Gets the subject distinguished name.
   *
   * @return the subject distinguished name
   */
  public String getSubjectDistinguishedName() {
    return subjectDistinguishedName;
  }

  /**
   * Sets the subject distinguished name.
   *
   * @param distinguishedName the new subject distinguished name
   */
  public void setSubjectDistinguishedName(String distinguishedName) {
    this.subjectDistinguishedName = distinguishedName;
  }

  /**
   * Gets the not before.
   *
   * @return the not before
   */
  public Date getNotBefore() {
    return notBefore == null ? null : Date.from(notBefore);
  }

  /**
   * Sets the not before.
   *
   * @param notBefore the new not before
   */
  public void setNotBefore(Instant notBefore) {
    this.notBefore = notBefore;
  }

  /**
   * Gets the not after.
   *
   * @return the not after
   */
  public Date getNotAfter() {
    return notAfter == null ? null : Date.from(notAfter);
  }

  /**
   * Sets the not after.
   *
   * @param notAfter the new not after
   */
  public void setNotAfter(Instant notAfter) {
    this.notAfter = notAfter;
  }

  /**
   * Gets the issuer distinguished name.
   *
   * @return the issuer distinguished name
   */
  public String getIssuerDistinguishedName() {
    return issuerDistinguishedName;
  }

  /**
   * Sets the issuer distinguished name.
   *
   * @param issuerDistinguishedName the new issuer distinguished name
   */
  public void setIssuerDistinguishedName(String issuerDistinguishedName) {
    this.issuerDistinguishedName = issuerDistinguishedName;
  }

  /**
   * Gets the alias.
   *
   * @return the alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * Sets the alias.
   *
   * @param alias the new alias
   */
  public void setAlias(String alias) {
    this.alias = alias;
  }

}
