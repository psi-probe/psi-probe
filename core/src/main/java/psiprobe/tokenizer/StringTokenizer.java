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
package psiprobe.tokenizer;

import java.io.IOException;
import java.io.StringReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class StringTokenizer.
 */
public class StringTokenizer extends Tokenizer {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(StringTokenizer.class);

  /**
   * Instantiates a new string tokenizer.
   */
  public StringTokenizer() {
    // Required due to override
  }

  /**
   * Instantiates a new string tokenizer.
   *
   * @param str the str
   */
  public StringTokenizer(final String str) {
    setString(str);
  }

  /**
   * Sets the string.
   *
   * @param str the new string
   */
  public void setString(final String str) {
    setReader(new StringReader(str));
  }

  @Override
  public boolean hasMore() {
    try {
      return super.hasMore();
    } catch (IOException e) {
      logger.trace("", e);
      return false;
    }
  }

  @Override
  public Token getToken() {
    try {
      return super.getToken();
    } catch (IOException e) {
      logger.trace("", e);
      return null;
    }
  }

  @Override
  public Token nextToken() {
    try {
      return super.nextToken();
    } catch (IOException e) {
      logger.trace("", e);
      return null;
    }
  }

}
