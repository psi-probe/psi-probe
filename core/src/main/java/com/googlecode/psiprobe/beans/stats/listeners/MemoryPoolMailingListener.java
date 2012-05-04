/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.beans.stats.listeners;

import com.googlecode.psiprobe.tools.MailMessage;
import com.googlecode.psiprobe.tools.Mailer;
import javax.mail.MessagingException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;

/**
 *
 * @author Mark Lewis
 */
public class MemoryPoolMailingListener extends FlapListener implements MessageSourceAware, InitializingBean {
    
    private static final String BASE_PROPERTY = "probe.src.stats.listener.memory.pool.";
    
    private MessageSourceAccessor messageSourceAccessor;
    private Mailer mailer;
    
    public MemoryPoolMailingListener() {
    }

    public MessageSourceAccessor getMessageSourceAccessor() {
        return messageSourceAccessor;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
    }

    public Mailer getMailer() {
        return mailer;
    }

    public void setMailer(Mailer mailer) {
        this.mailer = mailer;
    }

    public void afterPropertiesSet() throws Exception {
        if (getMailer().getSmtp() == null) {
            logger.info("Mailer SMTP host is not set.  Disabling listener.");
            setEnabled(false);
        } else if (getMailer().getDefaultTo() == null) {
            logger.info("Mailer default recipient is not set.  Disabling listener.");
            setEnabled(false);
        }
    }

    protected void flappingStarted(StatsCollectionEvent sce) {
        sendMail(sce, "flappingStart", false);
    }

    protected void aboveThresholdFlappingStopped(StatsCollectionEvent sce) {
        sendMail(sce, "aboveThreshold", true);
    }

    protected void belowThresholdFlappingStopped(StatsCollectionEvent sce) {
        sendMail(sce, "belowThreshold", true);
    }

    protected void aboveThresholdNotFlapping(StatsCollectionEvent sce) {
        sendMail(sce, "aboveThreshold", false);
    }

    protected void belowThresholdNotFlapping(StatsCollectionEvent sce) {
        sendMail(sce, "belowThreshold", false);
    }

    protected void sendMail(StatsCollectionEvent sce, String message, boolean flappingStop) {
        String name = sce.getName();
        if (isSeriesDisabled(name)) {
            return;
        }
        Long value = new Long(sce.getValue());
        Long threshold = new Long(this.getThreshold(name));
        String subjectInfix = "";
        String bodyPrefix = "";
        if (flappingStop) {
            subjectInfix = getMessageSourceAccessor().getMessage(BASE_PROPERTY + "flappingStop.subject.infix");
            bodyPrefix   = getMessageSourceAccessor().getMessage(BASE_PROPERTY + "flappingStop.body.prefix");
        }
        String subject = getMessageSourceAccessor().getMessage(BASE_PROPERTY + message + ".subject", new Object[] {subjectInfix, name, value, threshold});
        String body    = getMessageSourceAccessor().getMessage(BASE_PROPERTY + message + ".body",    new Object[] {bodyPrefix, name, value, threshold});
        MailMessage mail = new MailMessage(null, subject, body);
        try {
            getMailer().send(mail);
        } catch (MessagingException ex) {
            logger.error("Cannot send message", ex);
        }
    }

}
