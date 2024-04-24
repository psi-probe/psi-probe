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
package psiprobe.tools;

import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class MailMessage.
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
    return to.toArray(new String[to.size()]);
  }

  /**
   * Gets the cc array.
   *
   * @return the cc array
   */
  public String[] getCcArray() {
    return cc.toArray(new String[cc.size()]);
  }

  /**
   * Gets the bcc array.
   *
   * @return the bcc array
   */
  public String[] getBccArray() {
    return bcc.toArray(new String[bcc.size()]);
  }

  /**
   * Gets the attachments array.
   *
   * @return the attachments array
   */
  public DataSource[] getAttachmentsArray() {
    return attachments.toArray(new DataSource[attachments.size()]);
  }

  /**
   * Gets the subject.
   *
   * @return the subject
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Gets the body.
   *
   * @return the body
   */
  public String getBody() {
    return body;
  }

  /**
   * Checks if is body html.
   *
   * @return true, if is body html
   */
  public boolean isBodyHtml() {
    return bodyHtml;
  }

  /**
   * Adds the recipient to.
   *
   * @param address the address
   *
   * @return the mail message
   */
  public MailMessage addRecipientTo(String address) {
    if (address != null) {
      to.add(address);
    }
    return this;
  }

  /**
   * Adds the recipient cc.
   *
   * @param address the address
   *
   * @return the mail message
   */
  public MailMessage addRecipientCc(String address) {
    if (address != null) {
      cc.add(address);
    }
    return this;
  }

  /**
   * Adds the recipient bcc.
   *
   * @param address the address
   *
   * @return the mail message
   */
  public MailMessage addRecipientBcc(String address) {
    if (address != null) {
      bcc.add(address);
    }
    return this;
  }

  /**
   * Adds the attachment.
   *
   * @param attachment the attachment
   *
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
   *
   * @return the mail message
   */
  public MailMessage addAttachment(DataSource attachment) {
    if (attachment != null) {
      attachments.add(attachment);
    }
    return this;
  }

  /**
   * Clear recipients to.
   *
   * @return the mail message
   */
  public MailMessage clearRecipientsTo() {
    to.clear();
    return this;
  }

  /**
   * Clear recipients cc.
   *
   * @return the mail message
   */
  public MailMessage clearRecipientsCc() {
    cc.clear();
    return this;
  }

  /**
   * Clear recipients bcc.
   *
   * @return the mail message
   */
  public MailMessage clearRecipientsBcc() {
    bcc.clear();
    return this;
  }

  /**
   * Clear attachments.
   *
   * @return the mail message
   */
  public MailMessage clearAttachments() {
    attachments.clear();
    return this;
  }

  /**
   * Sets the subject.
   *
   * @param subject the subject
   *
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
   *
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
   *
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
    return to;
  }

  /**
   * Gets the cc.
   *
   * @return the cc
   */
  protected List<String> getCc() {
    return cc;
  }

  /**
   * Gets the bcc.
   *
   * @return the bcc
   */
  protected List<String> getBcc() {
    return bcc;
  }

  /**
   * Gets the attachments.
   *
   * @return the attachments
   */
  protected List<DataSource> getAttachments() {
    return attachments;
  }

}
