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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codebox.bean.JavaBeanTester;

import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

/**
 * The Class MailMessageTest.
 */
class MailMessageTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(MailMessage.class).loadData().test();
  }

  @Test
  void testConstructorAndGetters() {
    MailMessage msg = new MailMessage("to@example.com", "Subject", "Body");
    assertEquals(1, msg.getToArray().length);
    assertEquals("to@example.com", msg.getToArray()[0]);
    assertEquals("Subject", msg.getSubject());
    assertEquals("Body", msg.getBody());
    assertFalse(msg.isBodyHtml());
  }

  @Test
  void testAddRecipientToAndClear() {
    MailMessage msg = new MailMessage("a@b.com", "s", "b");
    msg.addRecipientTo("c@d.com");
    assertEquals(2, msg.getToArray().length);
    msg.clearRecipientsTo();
    assertEquals(0, msg.getToArray().length);
  }

  @Test
  void testAddRecipientCc() {
    MailMessage msg = new MailMessage("a@b.com", "s", "b");
    msg.addRecipientCc("cc@example.com");
    assertEquals(1, msg.getCcArray().length);
    msg.clearRecipientsCc();
    assertEquals(0, msg.getCcArray().length);
  }

  @Test
  void testAddRecipientBcc() {
    MailMessage msg = new MailMessage("a@b.com", "s", "b");
    msg.addRecipientBcc("bcc@example.com");
    assertEquals(1, msg.getBccArray().length);
    msg.clearRecipientsBcc();
    assertEquals(0, msg.getBccArray().length);
  }

  @Test
  void testAddNullRecipient() {
    MailMessage msg = new MailMessage("a@b.com", "s", "b");
    msg.addRecipientTo(null);
    // null should not be added
    assertEquals(1, msg.getToArray().length);
  }

  @Test
  void testAddAttachmentFile() throws IOException {
    File file = Files.createTempFile("mail-attachment-", ".txt").toFile();
    try {
      MailMessage msg = new MailMessage("a@b.com", "s", "b");
      msg.addAttachment(file);
      assertEquals(1, msg.getAttachmentsArray().length);
      msg.clearAttachments();
      assertEquals(0, msg.getAttachmentsArray().length);
    } finally {
      Files.deleteIfExists(file.toPath());
    }
  }

  @Test
  void testAddAttachmentDataSource() {
    MailMessage msg = new MailMessage("a@b.com", "s", "b");
    DataSource ds = new FileDataSource("/tmp/test.txt");
    msg.addAttachment(ds);
    assertEquals(1, msg.getAttachmentsArray().length);
  }

  @Test
  void testSetBodyHtml() {
    MailMessage msg = new MailMessage("a@b.com", "s", "b");
    msg.setBodyHtml(true);
    assertTrue(msg.isBodyHtml());
  }

  @Test
  void testSetSubjectAndBody() {
    MailMessage msg = new MailMessage("a@b.com", "original", "original body");
    msg.setSubject("updated subject");
    msg.setBody("updated body");
    assertEquals("updated subject", msg.getSubject());
    assertEquals("updated body", msg.getBody());
  }

  @Test
  void testChaining() {
    MailMessage msg = new MailMessage("a@b.com", "s", "b");
    // Verify methods return 'this' for chaining
    MailMessage result = msg.addRecipientTo("x@y.com").addRecipientCc("cc@y.com")
        .addRecipientBcc("bcc@y.com").setSubject("sub").setBody("body").setBodyHtml(true);
    assertEquals(msg, result);
  }
}
