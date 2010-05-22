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
