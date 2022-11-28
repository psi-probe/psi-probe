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
package psiprobe.jsp;

/**
 * The Class Functions.
 */
public final class Functions {

  /**
   * Prevent Instantiation.
   */
  private Functions() {
    // Prevent Instantiation
  }

  /**
   * Safe cookie name.
   *
   * @param cookieName the cookie name
   *
   * @return the string
   */
  public static String safeCookieName(String cookieName) {
    return cookieName.replace("\"", "");
  }

}
