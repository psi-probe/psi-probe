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
package com.googlecode.psiprobe.controllers;

import com.googlecode.psiprobe.tools.Whois;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class WhoisController extends ParameterizableViewController {

    private long lookupTimeout = 5;
    private String defaultServer = "whois.arin.net";
    private int defaultPort = 43;

    public long getLookupTimeout() {
        return lookupTimeout;
    }

    public void setLookupTimeout(long lookupTimeout) {
        this.lookupTimeout = lookupTimeout;
    }

    public String getDefaultServer() {
        return defaultServer;
    }

    public void setDefaultServer(String defaultServer) {
        this.defaultServer = defaultServer;
    }

    public int getDefaultPort() {
        return defaultPort;
    }

    public void setDefaultPort(int defaultPort) {
        this.defaultPort = defaultPort;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List lines = null;
        boolean timeout = false;
        String reverseName = null;

        String theIP = ServletRequestUtils.getStringParameter(request, "ip", null);

        Whois.Response wh = null;
        try {
            wh = Whois.lookup(getDefaultServer(), getDefaultPort(), theIP, getLookupTimeout());
        } catch (IOException e) {
            timeout = true;
        }

        if (wh != null) {
            lines = new ArrayList(50);
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(wh.getSummary().getBytes())));
            try {
                String line;
                while ( (line = br.readLine()) != null) {
                    lines.add(line);
                }
            } finally {
                br.close();
            }
        }

        if (theIP != null) {
            try {
                reverseName = InetAddress.getByName(theIP).getCanonicalHostName();
            } catch (UnknownHostException e) {
                logger.error("could not run a DNS query on "+theIP);
            }
        }
        return new ModelAndView(getViewName(), "result", lines).
                addObject("timeout", Boolean.valueOf(timeout)).
                addObject("whoisServer", wh != null ? wh.getServer() + ":"+ wh.getPort() : defaultServer + ":" + defaultPort).
                addObject("domainName", reverseName);
    }
}
