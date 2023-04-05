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
package psiprobe.model;

import jakarta.servlet.http.HttpServletRequest;

/**
 * The Class IpInfo.
 */
public class IpInfo {

  /** The address. */
  private String address;

  /** The forwarded. */
  private boolean forwarded;

  /**
   * Instantiates a new ip info.
   *
   * @param request the request
   *
   * @return the ip info
   */
  public IpInfo builder(HttpServletRequest request) {
    this.address = getClientAddress(request);
    if (!address.equals(request.getRemoteAddr())) {
      this.forwarded = true;
    }
    return this;
  }

  /**
   * Checks if is forwarded.
   *
   * @return true, if is forwarded
   */
  public boolean isForwarded() {
    return forwarded;
  }

  /**
   * Sets the forwarded.
   *
   * @param forwarded the new forwarded
   */
  public void setForwarded(boolean forwarded) {
    this.forwarded = forwarded;
  }

  /**
   * Gets the address.
   *
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the address.
   *
   * @param address the new address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return address;
  }

  /**
   * Gets the client address.
   *
   * @param request the request
   *
   * @return the client address
   */
  public static String getClientAddress(HttpServletRequest request) {
    String addr = request.getRemoteAddr();
    String fwdHeader = request.getHeader("X-Forwarded-For");
    if (fwdHeader != null) {
      addr = fwdHeader.split(",", -1)[0];
    }
    return addr;
  }

}
