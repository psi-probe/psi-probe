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

package psiprobe.beans.stats.listeners;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;

import psiprobe.tools.MailMessage;
import psiprobe.tools.Mailer;

import javax.mail.MessagingException;

/**
 * The listener interface for receiving memoryPoolMailing events.
 * The class that is interested in processing a memoryPoolMailing
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's {@code addMemoryPoolMailingListener} method. When
 * the memoryPoolMailing event occurs, that object's appropriate
 * method is invoked.
 *
 * @author Mark Lewis
 */
public class MemoryPoolMailingListener extends FlapListener implements MessageSourceAware,
    InitializingBean {

  /** The Constant BASE_PROPERTY. */
  private static final String BASE_PROPERTY = "probe.src.stats.listener.memory.pool.";

  /** The message source accessor. */
  private MessageSourceAccessor messageSourceAccessor;

  /** The mailer. */
  private Mailer mailer;

  /**
   * Gets the message source accessor.
   *
   * @return the message source accessor
   */
  public MessageSourceAccessor getMessageSourceAccessor() {
    return messageSourceAccessor;
  }

  @Override
  public void setMessageSource(MessageSource messageSource) {
    this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
  }

  /**
   * Gets the mailer.
   *
   * @return the mailer
   */
  public Mailer getMailer() {
    return mailer;
  }

  /**
   * Sets the mailer.
   *
   * @param mailer the new mailer
   */
  public void setMailer(Mailer mailer) {
    this.mailer = mailer;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (getMailer().getSmtp() == null) {
      logger.info("Mailer SMTP host is not set.  Disabling listener.");
      setEnabled(false);
    } else if (getMailer().getDefaultTo() == null) {
      logger.info("Mailer default recipient is not set.  Disabling listener.");
      setEnabled(false);
    }
  }

  @Override
  protected void flappingStarted(StatsCollectionEvent sce) {
    sendMail(sce, "flappingStart", false);
  }

  @Override
  protected void aboveThresholdFlappingStopped(StatsCollectionEvent sce) {
    sendMail(sce, "aboveThreshold", true);
  }

  @Override
  protected void belowThresholdFlappingStopped(StatsCollectionEvent sce) {
    sendMail(sce, "belowThreshold", true);
  }

  @Override
  protected void aboveThresholdNotFlapping(StatsCollectionEvent sce) {
    sendMail(sce, "aboveThreshold", false);
  }

  @Override
  protected void belowThresholdNotFlapping(StatsCollectionEvent sce) {
    sendMail(sce, "belowThreshold", false);
  }

  /**
   * Send mail.
   *
   * @param sce the sce
   * @param message the message
   * @param flappingStop the flapping stop
   */
  protected void sendMail(StatsCollectionEvent sce, String message, boolean flappingStop) {
    String name = sce.getName();
    if (isSeriesDisabled(name)) {
      return;
    }
    Long value = sce.getValue();
    Long threshold = this.getThreshold(name);
    String subjectInfix = "";
    String bodyPrefix = "";
    if (flappingStop) {
      subjectInfix =
          getMessageSourceAccessor().getMessage(BASE_PROPERTY + "flappingStop.subject.infix");
      bodyPrefix =
          getMessageSourceAccessor().getMessage(BASE_PROPERTY + "flappingStop.body.prefix");
    }
    String subject =
        getMessageSourceAccessor().getMessage(BASE_PROPERTY + message + ".subject",
            new Object[] {subjectInfix, name, value, threshold});
    String body =
        getMessageSourceAccessor().getMessage(BASE_PROPERTY + message + ".body",
            new Object[] {bodyPrefix, name, value, threshold});
    MailMessage mail = new MailMessage(null, subject, body);
    try {
      getMailer().send(mail);
    } catch (MessagingException ex) {
      logger.error("Cannot send message", ex);
    }
  }

}
