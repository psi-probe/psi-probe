package com.googlecode.psiprobe.jsp;

/**
 *
 * @author Mark Lewis
 */
public class Functions {

    public static String safeCookieName(String cookieName) {
        return cookieName.replaceAll("\"", "");
    }

}
