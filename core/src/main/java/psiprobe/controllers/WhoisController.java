/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package psiprobe.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import psiprobe.tools.Whois;

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

/**
 * The Class WhoisController.
 *
 * @author Vlad Ilyushchenko
 */
public class WhoisController extends ParameterizableViewController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(WhoisController.class);

  /** The lookup timeout. */
  private long lookupTimeout = 5;

  /** The default server. */
  private String defaultServer = "whois.arin.net";

  /** The default port. */
  private int defaultPort = 43;

  /**
   * Gets the lookup timeout.
   *
   * @return the lookup timeout
   */
  public long getLookupTimeout() {
    return lookupTimeout;
  }

  /**
   * Sets the lookup timeout.
   *
   * @param lookupTimeout the new lookup timeout
   */
  public void setLookupTimeout(long lookupTimeout) {
    this.lookupTimeout = lookupTimeout;
  }

  /**
   * Gets the default server.
   *
   * @return the default server
   */
  public String getDefaultServer() {
    return defaultServer;
  }

  /**
   * Sets the default server.
   *
   * @param defaultServer the new default server
   */
  public void setDefaultServer(String defaultServer) {
    this.defaultServer = defaultServer;
  }

  /**
   * Gets the default port.
   *
   * @return the default port
   */
  public int getDefaultPort() {
    return defaultPort;
  }

  /**
   * Sets the default port.
   *
   * @param defaultPort the new default port
   */
  public void setDefaultPort(int defaultPort) {
    this.defaultPort = defaultPort;
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    List<String> lines = null;
    boolean timeout = false;
    String reverseName = null;

    String ipAddress = ServletRequestUtils.getStringParameter(request, "ip", null);

    Whois.Response wh = null;
    try {
      wh = Whois.lookup(getDefaultServer(), getDefaultPort(), ipAddress, getLookupTimeout());
    } catch (IOException e) {
      timeout = true;
      logger.trace("", e);
    }

    if (wh != null) {
      lines = new ArrayList<>(50);
      try (BufferedReader br =
          new BufferedReader(new InputStreamReader(new ByteArrayInputStream(wh.getSummary()
              .getBytes())))) {
        String line;
        while ((line = br.readLine()) != null) {
          lines.add(line);
        }
      }
    }

    if (ipAddress != null) {
      try {
        reverseName = InetAddress.getByName(ipAddress).getCanonicalHostName();
      } catch (UnknownHostException e) {
        logger.error("could not run a DNS query on {}", ipAddress);
        logger.trace("", e);
      }
    }
    return new ModelAndView(getViewName(), "result", lines)
        .addObject("timeout", timeout)
        .addObject("whoisServer",
            wh != null ? wh.getServer() + ":" + wh.getPort() : defaultServer + ":" + defaultPort)
        .addObject("domainName", reverseName);
  }
}
