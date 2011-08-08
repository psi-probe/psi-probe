package com.googlecode.psiprobe.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

/**
 *
 * @author Mark Lewis
 */
public class MailMessage {

    private List/*String*/ to = new ArrayList();
    private List/*String*/ cc = new ArrayList();
    private List/*String*/ bcc = new ArrayList();
    private List/*DataSource*/ attachments = new ArrayList();
    private String subject = "";
    private String body = "";
    private boolean bodyHtml;

    public MailMessage(String to, String subject, String body) {
        addRecipientTo(to);
        setSubject(subject);
        setBody(body);
    }

    public String[] getTo() {
        return (String[]) to.toArray(new String[to.size()]);
    }

    public String[] getCc() {
        return (String[]) cc.toArray(new String[cc.size()]);
    }

    public String[] getBcc() {
        return (String[]) bcc.toArray(new String[bcc.size()]);
    }

    public DataSource[] getAttachments() {
        return (DataSource[]) attachments.toArray(new DataSource[attachments.size()]);
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public boolean isBodyHtml() {
        return bodyHtml;
    }

    public MailMessage addRecipientTo(String address) {
        to.add(address);
        return this;
    }

    public MailMessage addRecipientCc(String address) {
        cc.add(address);
        return this;
    }

    public MailMessage addRecipientBcc(String address) {
        bcc.add(address);
        return this;
    }

    public MailMessage addAttachment(File attachment) {
        FileDataSource ds = new FileDataSource(attachment);
        return addAttachment(ds);
    }

    public MailMessage addAttachment(DataSource attachment) {
        attachments.add(attachment);
        return this;
    }

    public MailMessage clearRecipientsTo() {
        to.clear();
        return this;
    }

    public MailMessage clearRecipientsCc() {
        cc.clear();
        return this;
    }

    public MailMessage clearRecipientsBcc() {
        bcc.clear();
        return this;
    }

    public MailMessage clearAttachments() {
        attachments.clear();
        return this;
    }

    public MailMessage setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public MailMessage setBody(String body) {
        this.body = body;
        return this;
    }

    public MailMessage setBodyHtml(boolean bodyHtml) {
        this.bodyHtml = bodyHtml;
        return this;
    }

}
