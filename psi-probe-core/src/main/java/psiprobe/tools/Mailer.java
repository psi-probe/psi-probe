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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Facade for sending emails with the JavaMail API.
 */
public class Mailer {

  /** The Constant PROPERTY_KEY_SMTP. */
  public static final String PROPERTY_KEY_SMTP = "mail.smtp.host";

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(Mailer.class);

  /** The from. */
  private String from;

  /** The smtp. */
  private String smtp;

  /** The default to. */
  private String defaultTo;

  /** The subject prefix. */
  private String subjectPrefix;

  /**
   * Instantiates a new mailer.
   */
  public Mailer() {
    this(null);
  }

  /**
   * Instantiates a new mailer.
   *
   * @param from the from
   */
  public Mailer(String from) {
    this(from, null);
  }

  /**
   * Instantiates a new mailer.
   *
   * @param from the from
   * @param smtp the smtp
   */
  public Mailer(String from, String smtp) {
    this.smtp = smtp;
    this.from = from;
  }

  /**
   * Gets the from.
   *
   * @return the from
   */
  public String getFrom() {
    return from;
  }

  /**
   * Gets the smtp.
   *
   * @return the smtp
   */
  public String getSmtp() {
    if (smtp == null) {
      return System.getProperty(PROPERTY_KEY_SMTP);
    }
    return smtp;
  }

  /**
   * Sets the from.
   *
   * @param from the new from
   */
  public void setFrom(String from) {
    this.from = from;
  }

  /**
   * Sets the smtp.
   *
   * @param smtp the new smtp
   */
  public void setSmtp(String smtp) {
    this.smtp = smtp;
  }

  /**
   * Gets the default to.
   *
   * @return the default to
   */
  public String getDefaultTo() {
    return defaultTo;
  }

  /**
   * Sets the default to.
   *
   * @param defaultTo the new default to
   */
  @Value("${psiprobe.tools.mail.to}")
  public void setDefaultTo(String defaultTo) {
    this.defaultTo = defaultTo;
  }

  /**
   * Gets the subject prefix.
   *
   * @return the subject prefix
   */
  public String getSubjectPrefix() {
    return subjectPrefix;
  }

  /**
   * Sets the subject prefix.
   *
   * @param subjectPrefix the new subject prefix
   */
  @Value("${psiprobe.tools.mail.subjectPrefix}")
  public void setSubjectPrefix(String subjectPrefix) {
    this.subjectPrefix = subjectPrefix;
  }

  /**
   * Send.
   *
   * @param mailMessage the mail message
   *
   * @throws MessagingException the messaging exception
   */
  public void send(MailMessage mailMessage) throws MessagingException {
    Properties props = (Properties) System.getProperties().clone();
    if (smtp != null) {
      props.put(PROPERTY_KEY_SMTP, smtp);
    }

    try (PrintStream debugOut =
        LogOutputStream.createPrintStream(logger, LogOutputStream.LEVEL_DEBUG)) {
      Session session = Session.getDefaultInstance(props);
      session.setDebug(true);
      session.setDebugOut(debugOut);

      MimeMessage message = createMimeMessage(session, mailMessage);
      logger.debug("Sending message");
      Transport.send(message);
    }
  }

  /**
   * Creates the mime message.
   *
   * @param session the session
   * @param mailMessage the mail message
   *
   * @return the mime message
   *
   * @throws MessagingException the messaging exception
   */
  private MimeMessage createMimeMessage(Session session, MailMessage mailMessage)
      throws MessagingException {

    String subject = mailMessage.getSubject();
    if (subjectPrefix != null && !subjectPrefix.isEmpty()) {
      subject = subjectPrefix + " " + subject;
    }

    MimeMultipart content = new MimeMultipart("related");

    // Create attachments
    DataSource[] attachments = mailMessage.getAttachmentsArray();
    for (DataSource attachment : attachments) {
      MimeBodyPart attachmentPart = createAttachmentPart(attachment);
      content.addBodyPart(attachmentPart);
    }

    // Create message body
    MimeBodyPart bodyPart = createMessageBodyPart(mailMessage.getBody(), mailMessage.isBodyHtml());
    content.addBodyPart(bodyPart);

    MimeMessage message = new MimeMessage(session);
    if (from == null) {
      // Uses mail.from property
      message.setFrom();
    } else {
      message.setFrom(new InternetAddress(from));
    }

    InternetAddress[] to = createAddresses(mailMessage.getToArray());
    if (to.length == 0) {
      to = InternetAddress.parse(defaultTo);
    }
    message.setRecipients(Message.RecipientType.TO, to);

    InternetAddress[] cc = createAddresses(mailMessage.getCcArray());
    message.setRecipients(Message.RecipientType.CC, cc);

    InternetAddress[] bcc = createAddresses(mailMessage.getBccArray());
    message.setRecipients(Message.RecipientType.BCC, bcc);

    message.setSubject(subject);
    message.setContent(content);
    return message;
  }

  /**
   * Creates the addresses.
   *
   * @param addresses the addresses
   *
   * @return the Internet address[]
   *
   * @throws AddressException the address exception
   */
  private static InternetAddress[] createAddresses(String[] addresses) throws AddressException {
    List<InternetAddress> result = new ArrayList<>(addresses.length);
    for (String address : addresses) {
      InternetAddress[] parsedAddresses = InternetAddress.parse(address);
      result.addAll(Arrays.asList(parsedAddresses));
    }
    return result.toArray(new InternetAddress[result.size()]);
  }

  /**
   * Creates the attachment part.
   *
   * @param attachment the attachment
   *
   * @return the mime body part
   *
   * @throws MessagingException the messaging exception
   */
  private static MimeBodyPart createAttachmentPart(DataSource attachment)
      throws MessagingException {

    MimeBodyPart attachmentPart = new MimeBodyPart();
    attachmentPart.setDataHandler(new DataHandler(attachment));
    attachmentPart.setDisposition(Part.ATTACHMENT);
    attachmentPart.setFileName(attachment.getName());
    return attachmentPart;
  }

  /**
   * Creates the message body part.
   *
   * @param body the body
   * @param html the html
   *
   * @return the mime body part
   *
   * @throws MessagingException the messaging exception
   */
  private static MimeBodyPart createMessageBodyPart(String body, boolean html)
      throws MessagingException {
    MimeBodyPart bodyPart = new MimeBodyPart();
    bodyPart.setText(body);
    bodyPart.setHeader("content-type", html ? "text/html" : "text/plain");
    return bodyPart;
  }

}
