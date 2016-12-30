/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * POJO representing HTTP session.
 */
public class ApplicationSession {

  /** The Constant LAST_ACCESSED_BY_IP. */
  public static final String LAST_ACCESSED_BY_IP = "__psiprobe_la_ip";

  /** The Constant LAST_ACCESSED_LOCALE. */
  public static final String LAST_ACCESSED_LOCALE = "__psiprobe_la_local";

  /** The id. */
  private String id;

  /** The application name. */
  private String applicationName;

  /** The creation time. */
  private Date creationTime;

  /** The last access time. */
  private Date lastAccessTime;

  /** The max idle time. */
  private int maxIdleTime;

  /** The valid. */
  private boolean valid;

  /** The serializable. */
  private boolean serializable;

  /** The object count. */
  private long objectCount;

  /** The info. */
  private String info;

  /** The manager type. */
  private String managerType;

  /** The attributes. */
  private List<Attribute> attributes = new ArrayList<>();

  /** The size. */
  private long size;

  /** The allowed to view values. */
  private boolean allowedToViewValues;

  /** The last accessed ip. */
  private String lastAccessedIp;

  /** The last accessed ip locale. */
  private Locale lastAccessedIpLocale;

  /**
   * Gets the id.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the application name.
   *
   * @return the application name
   */
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * Sets the application name.
   *
   * @param applicationName the new application name
   */
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  /**
   * Gets the creation time.
   *
   * @return the creation time
   */
  public Date getCreationTime() {
    return creationTime == null ? null : new Date(creationTime.getTime());
  }

  /**
   * Sets the creation time.
   *
   * @param creationTime the new creation time
   */
  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime == null ? null : new Date(creationTime.getTime());
  }

  /**
   * Gets the last access time.
   *
   * @return the last access time
   */
  public Date getLastAccessTime() {
    return lastAccessTime == null ? null : new Date(lastAccessTime.getTime());
  }

  /**
   * Sets the last access time.
   *
   * @param lastAccessTime the new last access time
   */
  public void setLastAccessTime(Date lastAccessTime) {
    this.lastAccessTime = lastAccessTime == null ? null : new Date(lastAccessTime.getTime());
  }

  /**
   * Gets the max idle time.
   *
   * @return the max idle time
   */
  public int getMaxIdleTime() {
    return maxIdleTime;
  }

  /**
   * Sets the max idle time.
   *
   * @param maxIdleTime the new max idle time
   */
  public void setMaxIdleTime(int maxIdleTime) {
    this.maxIdleTime = maxIdleTime;
  }

  /**
   * Checks if is valid.
   *
   * @return true, if is valid
   */
  public boolean isValid() {
    return valid;
  }

  /**
   * Sets the valid.
   *
   * @param valid the new valid
   */
  public void setValid(boolean valid) {
    this.valid = valid;
  }

  /**
   * Gets the object count.
   *
   * @return the object count
   */
  public long getObjectCount() {
    return objectCount;
  }

  /**
   * Sets the object count.
   *
   * @param objectCount the new object count
   */
  public void setObjectCount(long objectCount) {
    this.objectCount = objectCount;
  }

  /**
   * Gets the attributes.
   *
   * @return the attributes
   */
  public List<Attribute> getAttributes() {
    return attributes;
  }

  /**
   * Sets the attributes.
   *
   * @param attributes the new attributes
   */
  public void setAttributes(List<Attribute> attributes) {
    this.attributes = attributes;
  }

  /**
   * Adds the attribute.
   *
   * @param sa the sa
   */
  public void addAttribute(Attribute sa) {
    attributes.add(sa);
  }

  /**
   * Gets the info.
   *
   * @return the info
   */
  public String getInfo() {
    return info;
  }

  /**
   * Sets the info.
   *
   * @param info the new info
   */
  public void setInfo(String info) {
    this.info = info;
  }

  /**
   * Gets the manager type.
   *
   * @return the manager type
   */
  public String getManagerType() {
    return managerType;
  }

  /**
   * Sets the manager type.
   *
   * @param managerType the new manager type
   */
  public void setManagerType(String managerType) {
    this.managerType = managerType;
  }

  /**
   * Gets the age.
   *
   * @return the age
   */
  public long getAge() {
    if (creationTime == null) {
      return 0;
    }
    return System.currentTimeMillis() - creationTime.getTime();
  }

  /**
   * Gets the idle time.
   *
   * @return the idle time
   */
  public long getIdleTime() {
    if (lastAccessTime == null) {
      return getAge();
    }
    return System.currentTimeMillis() - lastAccessTime.getTime();
  }

  /**
   * Gets the expiry time.
   *
   * @return the expiry time
   */
  public Date getExpiryTime() {
    if (getMaxIdleTime() <= 0) {
      return null;
    }
    return new Date(System.currentTimeMillis() + getMaxIdleTime() - getIdleTime());
  }

  /**
   * Checks if is serializable.
   *
   * @return true, if is serializable
   */
  public boolean isSerializable() {
    return serializable;
  }

  /**
   * Sets the serializable.
   *
   * @param serializable the new serializable
   */
  public void setSerializable(boolean serializable) {
    this.serializable = serializable;
  }

  /**
   * Gets the size.
   *
   * @return the size
   */
  public long getSize() {
    return size;
  }

  /**
   * Sets the size.
   *
   * @param size the new size
   */
  public void setSize(long size) {
    this.size = size;
  }

  /**
   * Checks if is allowed to view values.
   *
   * @return true, if is allowed to view values
   */
  public boolean isAllowedToViewValues() {
    return allowedToViewValues;
  }

  /**
   * Sets the allowed to view values.
   *
   * @param allowedToViewValues the new allowed to view values
   */
  public void setAllowedToViewValues(boolean allowedToViewValues) {
    this.allowedToViewValues = allowedToViewValues;
  }

  /**
   * Gets the last accessed ip.
   *
   * @return the last accessed ip
   */
  public String getLastAccessedIp() {
    return lastAccessedIp;
  }

  /**
   * Sets the last accessed ip.
   *
   * @param lastAccessedIp the new last accessed ip
   */
  public void setLastAccessedIp(String lastAccessedIp) {
    this.lastAccessedIp = lastAccessedIp;
  }

  /**
   * Gets the last accessed ip locale.
   *
   * @return the last accessed ip locale
   */
  public Locale getLastAccessedIpLocale() {
    return lastAccessedIpLocale;
  }

  /**
   * Sets the last accessed ip locale.
   *
   * @param lastAccessedIpLocale the new last accessed ip locale
   */
  public void setLastAccessedIpLocale(Locale lastAccessedIpLocale) {
    this.lastAccessedIpLocale = lastAccessedIpLocale;
  }

}
