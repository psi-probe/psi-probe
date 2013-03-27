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

    public String[] getToArray() {
        return (String[]) to.toArray(new String[to.size()]);
    }

    public String[] getCcArray() {
        return (String[]) cc.toArray(new String[cc.size()]);
    }

    public String[] getBccArray() {
        return (String[]) bcc.toArray(new String[bcc.size()]);
    }

    public DataSource[] getAttachmentsArray() {
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
        if (address != null) {
            to.add(address);
        }
        return this;
    }

    public MailMessage addRecipientCc(String address) {
        if (address != null) {
            cc.add(address);
        }
        return this;
    }

    public MailMessage addRecipientBcc(String address) {
        if (address != null) {
            bcc.add(address);
        }
        return this;
    }

    public MailMessage addAttachment(File attachment) {
        FileDataSource ds = new FileDataSource(attachment);
        return addAttachment(ds);
    }

    public MailMessage addAttachment(DataSource attachment) {
        if (attachment != null) {
            attachments.add(attachment);
        }
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

    protected List getTo() {
        return to;
    }

    protected List getCc() {
        return cc;
    }

    protected List getBcc() {
        return bcc;
    }

    protected List getAttachments() {
        return attachments;
    }

}
