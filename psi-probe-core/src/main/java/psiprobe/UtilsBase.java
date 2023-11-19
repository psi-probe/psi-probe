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
package psiprobe;

import java.util.Scanner;

/**
 * Misc. static helper methods.
 */
public final class UtilsBase {

  /**
   * Prevent Instantiation.
   */
  private UtilsBase() {
    // Prevent Instantiation
  }

  /**
   * Calc pool usage score.
   *
   * @param max the max
   * @param value the value
   * @return the int
   */
  public static int calcPoolUsageScore(int max, int value) {
    return max > 0 ? Math.max(0, value) * 100 / max : 0;
  }

  /**
   * To int.
   *
   * @param num the num
   * @param defaultValue the default value
   * @return the int
   */
  public static int toInt(String num, int defaultValue) {
    if (num != null && !num.contains(" ")) {
      try (Scanner scanner = new Scanner(num)) {
        if (scanner.hasNextInt()) {
          return Integer.parseInt(num);
        }
      }
    }
    return defaultValue;
  }

}
