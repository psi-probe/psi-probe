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
    return notBefore;
  }

  public void setNotBefore(Date notBefore) {
    this.notBefore = notBefore;
  }

  public Date getNotAfter() {
    return notAfter;
  }

  public void setNotAfter(Date notAfter) {
    this.notAfter = notAfter;
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
