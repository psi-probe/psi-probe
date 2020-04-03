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
package psiprobe.model.jsp;

/**
 * The Class CompilerException.
 */
public class CompilerException extends Exception {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new compiler exception.
   */
  public CompilerException() {
    // Required due to override
  }

  /**
   * Instantiates a new compiler exception.
   *
   * @param message the message
   */
  public CompilerException(String message) {
    super(message);
  }

  /**
   * Instantiates a new compiler exception.
   *
   * @param cause the cause
   */
  public CompilerException(Throwable cause) {
    super(cause);
  }

  /**
   * Instantiates a new compiler exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public CompilerException(String message, Throwable cause) {
    super(message, cause);
  }

}
