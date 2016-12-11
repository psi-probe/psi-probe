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
import java.util.Date;

public class Cert implements Serializable {

  private static final long serialVersionUID = -727528588030989042L;

  private String alias;

  private String subjectDistinguishedName;

  private String issuerDistinguishedName;

  private Date notBefore;

  private Date notAfter;

  public String getSubjectDistinguishedName() {
    return subjectDistinguishedName;
  }

  public void setSubjectDistinguishedName(String distinguishedName) {
    this.subjectDistinguishedName = distinguishedName;
  }

  public Date getNotBefore() {
    return notBefore == null ? null : new Date(notBefore.getTime());
  }

  public void setNotBefore(Date notBefore) {
    this.notBefore = notBefore == null ? null : new Date(notBefore.getTime());
  }

  public Date getNotAfter() {
    return notAfter == null ? null : new Date(notAfter.getTime());
  }

  public void setNotAfter(Date notAfter) {
    this.notAfter = notAfter == null ? null : new Date(notAfter.getTime());
  }

  public String getIssuerDistinguishedName() {
    return issuerDistinguishedName;
  }

  public void setIssuerDistinguishedName(String issuerDistinguishedName) {
    this.issuerDistinguishedName = issuerDistinguishedName;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

}
