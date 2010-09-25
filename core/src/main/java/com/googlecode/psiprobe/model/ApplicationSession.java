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
package com.googlecode.psiprobe.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * POJO representing HTTP session.
 * 
 * @author Vlad Ilyushchenko
 */
public class ApplicationSession {

    public static final String LAST_ACCESSED_BY_IP = "__psiprobe_la_ip";

    private String id;
    private String applicationName;
    private Date creationTime;
    private Date lastAccessTime;
    private int maxIdleTime;
    private boolean valid;
    private boolean serializable;
    private long objectCount;
    private String info;
    private String managerType;
    private List attributes = new ArrayList();
    private long size;
    private boolean allowedToViewValues = false;
    private String lastAccessedIP;
    private Locale lastAccessedIPLocale;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public long getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(long objectCount) {
        this.objectCount = objectCount;
    }

    public List getAttributes() {
        return attributes;
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Attribute sa) {
        attributes.add(sa);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getManagerType() {
        return managerType;
    }

    public void setManagerType(String managerType) {
        this.managerType = managerType;
    }

    public long getAge() {
        if (creationTime == null) {
            return 0;
        } else {
            return System.currentTimeMillis() - creationTime.getTime();
        }
    }

    public long getIdleTime() {
        if (lastAccessTime == null) {
            return getAge();
        } else {
            return System.currentTimeMillis() - lastAccessTime.getTime();
        }
    }

    public Date getExpiryTime() {
        if (getMaxIdleTime() <= 0) {
            return null;
        } else {
            return new Date(System.currentTimeMillis() + getMaxIdleTime() - getIdleTime());
        }
    }

    public boolean isSerializable() {
        return serializable;
    }

    public void setSerializable(boolean serializable) {
        this.serializable = serializable;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isAllowedToViewValues() {
        return allowedToViewValues;
    }

    public void setAllowedToViewValues(boolean allowedToViewValues) {
        this.allowedToViewValues = allowedToViewValues;
    }

    public String getLastAccessedIP() {
        return lastAccessedIP;
    }

    public void setLastAccessedIP(String lastAccessedIP) {
        this.lastAccessedIP = lastAccessedIP;
    }

    public Locale getLastAccessedIPLocale() {
        return lastAccessedIPLocale;
    }

    public void setLastAccessedIPLocale(Locale lastAccessedIPLocale) {
        this.lastAccessedIPLocale = lastAccessedIPLocale;
    }
}
