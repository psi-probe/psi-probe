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

package com.googlecode.psiprobe.tools;

import com.googlecode.psiprobe.tools.url.UrlParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Class Whois.
 *
 * @author Vlad Ilyushchenko
 */
public class Whois {

  /**
   * Lookup.
   *
   * @param server the server
   * @param port the port
   * @param query the query
   * @return the response
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Response lookup(String server, int port, String query) throws IOException {
    return lookup(server, port, query, 5);
  }

  /**
   * Lookup.
   *
   * @param server the server
   * @param port the port
   * @param query the query
   * @param timeout the timeout
   * @return the response
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Response lookup(String server, int port, String query, long timeout)
      throws IOException {

    return lookup(server, port, query, timeout, System.getProperty("line.separator"));
  }

  /**
   * Lookup.
   *
   * @param server the server
   * @param port the port
   * @param query the query
   * @param timeout the timeout
   * @param lineSeparator the line separator
   * @return the response
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Response lookup(String server, int port, String query, long timeout,
      String lineSeparator) throws IOException {

    if (query == null) {
      return null;
    }

    Response response = new Response();

    response.server = server;
    response.port = port;

    Socket connection = AsyncSocketFactory.createSocket(server, port, timeout);
    try {
      PrintStream out = new PrintStream(connection.getOutputStream());
      try {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        try {
          out.println(query);
          StringBuilder sb = new StringBuilder();

          String line;
          while ((line = in.readLine()) != null) {
            sb.append(line).append(lineSeparator);
            line = line.trim();
            if (!line.startsWith("%") && !line.startsWith("#")) {
              int fs = line.indexOf(":");
              if (fs > 0) {
                String name = line.substring(0, fs);
                String value = line.substring(fs + 1).trim();
                response.data.put(name, value);
              }
            }
          }
          response.summary = sb.toString();

          Response newResponse = null;
          String referral = (String) response.getData().get("ReferralServer");

          if (referral != null) {
            try {
              UrlParser url = new UrlParser(referral);
              if ("whois".equals(url.getProtocol())) {
                newResponse =
                    lookup(url.getHost(), url.getPort() == -1 ? 43 : url.getPort(), query, timeout,
                        lineSeparator);
              }
            } catch (IOException e) {
              // System.out.println("Could not contact " + referral);

            }
          }
          if (newResponse != null) {
            response = newResponse;
          }
        } finally {
          in.close();
        }
      } finally {
        out.close();
      }
    } finally {
      connection.close();
    }

    return response;
  }

  /**
   * The Class Response.
   */
  public static class Response {

    /** The summary. */
    private String summary;
    
    /** The data. */
    private Map<String, String> data = new TreeMap<String, String>();
    
    /** The server. */
    private String server;
    
    /** The port. */
    private int port;

    /**
     * Gets the summary.
     *
     * @return the summary
     */
    public String getSummary() {
      return summary;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public Map<String, String> getData() {
      return data;
    }

    /**
     * Gets the server.
     *
     * @return the server
     */
    public String getServer() {
      return server;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public int getPort() {
      return port;
    }

  }

}
