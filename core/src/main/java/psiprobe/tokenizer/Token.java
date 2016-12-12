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

/**
 * The Interface Token.
 */
public interface Token {

  /**
   * Gets the text.
   *
   * @return the text
   */
  String getText();

  /**
   * Gets the inner text.
   *
   * @return the inner text
   */
  String getInnerText();

  /**
   * Gets the name.
   *
   * @return the name
   */
  String getName();

  /**
   * Gets the type.
   *
   * @return the type
   */
  int getType();

  /**
   * Gets the line.
   *
   * @return the line
   */
  int getLine();

  /**
   * Gets the col.
   *
   * @return the col
   */
  int getCol();

}
