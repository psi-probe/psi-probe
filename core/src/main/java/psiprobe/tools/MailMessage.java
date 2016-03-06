/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package psiprobe.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

/**
 * The Class MailMessage.
 *
 * @author Mark Lewis
 */
public class MailMessage {

  /** The to. */
  private final List<String> to = new ArrayList<>();
  
  /** The cc. */
  private final List<String> cc = new ArrayList<>();
  
  /** The bcc. */
  private final List<String> bcc = new ArrayList<>();
  
  /** The attachments. */
  private final List<DataSource> attachments = new ArrayList<>();
  
  /** The subject. */
  private String subject = "";
  
  /** The body. */
  private String body = "";
  
  /** The body html. */
  private boolean bodyHtml;

  /**
   * Instantiates a new mail message.
   *
   * @param to the to
   * @param subject the subject
   * @param body the body
   */
  public MailMessage(String to, String subject, String body) {
    addRecipientTo(to);
    setSubject(subject);
    setBody(body);
  }

  /**
   * Gets the to array.
   *
   * @return the to array
   */
  public String[] getToArray() {
    return this.to.toArray(new String[this.to.size()]);
  }

  /**
   * Gets the cc array.
   *
   * @return the cc array
   */
  public String[] getCcArray() {
    return this.cc.toArray(new String[this.cc.size()]);
  }

  /**
   * Gets the bcc array.
   *
   * @return the bcc array
   */
  public String[] getBccArray() {
    return this.bcc.toArray(new String[this.bcc.size()]);
  }

  /**
   * Gets the attachments array.
   *
   * @return the attachments array
   */
  public DataSource[] getAttachmentsArray() {
    return this.attachments.toArray(new DataSource[this.attachments.size()]);
  }

  /**
   * Gets the subject.
   *
   * @return the subject
   */
  public String getSubject() {
    return this.subject;
  }

  /**
   * Gets the body.
   *
   * @return the body
   */
  public String getBody() {
    return this.body;
  }

  /**
   * Checks if is body html.
   *
   * @return true, if is body html
   */
  public boolean isBodyHtml() {
    return this.bodyHtml;
  }

  /**
   * Adds the recipient to.
   *
   * @param address the address
   * @return the mail message
   */
  public MailMessage addRecipientTo(String address) {
    if (address != null) {
      this.to.add(address);
    }
    return this;
  }

  /**
   * Adds the recipient cc.
   *
   * @param address the address
   * @return the mail message
   */
  public MailMessage addRecipientCc(String address) {
    if (address != null) {
      this.cc.add(address);
    }
    return this;
  }

  /**
   * Adds the recipient bcc.
   *
   * @param address the address
   * @return the mail message
   */
  public MailMessage addRecipientBcc(String address) {
    if (address != null) {
      this.bcc.add(address);
    }
    return this;
  }

  /**
   * Adds the attachment.
   *
   * @param attachment the attachment
   * @return the mail message
   */
  public MailMessage addAttachment(File attachment) {
    FileDataSource ds = new FileDataSource(attachment);
    return addAttachment(ds);
  }

  /**
   * Adds the attachment.
   *
   * @param attachment the attachment
   * @return the mail message
   */
  public MailMessage addAttachment(DataSource attachment) {
    if (attachment != null) {
      this.attachments.add(attachment);
    }
    return this;
  }

  /**
   * Clear recipients to.
   *
   * @return the mail message
   */
  public MailMessage clearRecipientsTo() {
    this.to.clear();
    return this;
  }

  /**
   * Clear recipients cc.
   *
   * @return the mail message
   */
  public MailMessage clearRecipientsCc() {
    this.cc.clear();
    return this;
  }

  /**
   * Clear recipients bcc.
   *
   * @return the mail message
   */
  public MailMessage clearRecipientsBcc() {
    this.bcc.clear();
    return this;
  }

  /**
   * Clear attachments.
   *
   * @return the mail message
   */
  public MailMessage clearAttachments() {
    this.attachments.clear();
    return this;
  }

  /**
   * Sets the subject.
   *
   * @param subject the subject
   * @return the mail message
   */
  public MailMessage setSubject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Sets the body.
   *
   * @param body the body
   * @return the mail message
   */
  public MailMessage setBody(String body) {
    this.body = body;
    return this;
  }

  /**
   * Sets the body html.
   *
   * @param bodyHtml the body html
   * @return the mail message
   */
  public MailMessage setBodyHtml(boolean bodyHtml) {
    this.bodyHtml = bodyHtml;
    return this;
  }

  /**
   * Gets the to.
   *
   * @return the to
   */
  protected List<String> getTo() {
    return this.to;
  }

  /**
   * Gets the cc.
   *
   * @return the cc
   */
  protected List<String> getCc() {
    return this.cc;
  }

  /**
   * Gets the bcc.
   *
   * @return the bcc
   */
  protected List<String> getBcc() {
    return this.bcc;
  }

  /**
   * Gets the attachments.
   *
   * @return the attachments
   */
  protected List<DataSource> getAttachments() {
    return this.attachments;
  }

}
