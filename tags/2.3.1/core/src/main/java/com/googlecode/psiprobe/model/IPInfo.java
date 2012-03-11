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

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Mark Lewis
 */
public class IPInfo {

    private String address;
    private boolean forwarded;

    public IPInfo() {
    }

    public IPInfo(HttpServletRequest request) {
        this.address = getClientAddress(request);
        if (!address.equals(request.getRemoteAddr())) {
            this.forwarded = true;
        }
    }

    public boolean isForwarded() {
        return forwarded;
    }

    public void setForwarded(boolean forwarded) {
        this.forwarded = forwarded;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString() {
        return address;
    }

    public static String getClientAddress(HttpServletRequest request) {
        String addr = request.getRemoteAddr();
        String fwdHeader = request.getHeader("X-Forwarded-For");
        if (fwdHeader != null) {
            addr = fwdHeader.split(",")[0];
        }
        return addr;
    }
}
