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
package psiprobe.tools.url;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class UrlParser.
 */
public class UrlParser {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(UrlParser.class);

  /** The protocol. */
  private String protocol;

  /** The host. */
  private String host;

  /** The port. */
  private int port = -1;

  /** The path. */
  private String path;

  /**
   * Instantiates a new url parser.
   *
   * @param url the url
   * @throws MalformedURLException the malformed URL exception
   */
  public UrlParser(String url) throws MalformedURLException {
    if (url == null || url.length() <= 0) {
      throw new MalformedURLException("Empty URL");
    }
    int ppos = url.indexOf("://");

    // get protocol first
    if (ppos >= 0) {
      protocol = url.substring(0, ppos);
      url = url.substring(ppos + 3);
    }

    String hostport;

    ppos = url.indexOf('/');
    if (ppos >= 0) {
      hostport = url.substring(0, ppos);
      path = url.substring(ppos + 1);
    } else {
      hostport = url;
    }

    ppos = hostport.indexOf(':');
    if (ppos >= 0) {
      host = hostport.substring(0, ppos);
      String portString = hostport.substring(ppos + 1);
      try {
        this.port = Integer.parseInt(portString);
      } catch (NumberFormatException e) {
        logger.trace("", e);
        throw new MalformedURLException("Invalid port " + portString);
      }
    } else {
      host = hostport;
    }
  }

  /**
   * Gets the protocol.
   *
   * @return the protocol
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * Sets the protocol.
   *
   * @param protocol the new protocol
   */
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  /**
   * Gets the host.
   *
   * @return the host
   */
  public String getHost() {
    return host;
  }

  /**
   * Sets the host.
   *
   * @param host the new host
   */
  public void setHost(String host) {
    this.host = host;
  }

  /**
   * Gets the port.
   *
   * @return the port
   */
  public int getPort() {
    return port;
  }

  /**
   * Sets the port.
   *
   * @param port the new port
   */
  public void setPort(int port) {
    this.port = port;
  }

  /**
   * Gets the path.
   *
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * Sets the path.
   *
   * @param path the new path
   */
  public void setPath(String path) {
    this.path = path;
  }

}
