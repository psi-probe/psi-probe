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

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class VisualScoreTagTest.
 */
class VisualScoreTagTest {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(VisualScoreTagTest.class);

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(VisualScoreTag.class).loadData().skipStrictSerializable().test();
  }

  /**
   * Test range scan.
   */
  @Test
  void rangeScanTest() {
    // As used in appRuntimeInfo.jsp
    doTestRangeScan(8, 5, false);
    doTestRangeScan(8, 5, true);

    /*
     * As used in memory_pools.jsp, application.jsp, datasourcegroup.jsp, datasources_table.jsp,
     * resources.jsp and sysinfo.jsp
     */
    doTestRangeScan(10, 5, false);
    doTestRangeScan(10, 5, true);
  }

  /**
   * Do test range scan.
   *
   * @param fullBlocks the full blocks
   * @param partialBlocks the partial blocks
   * @param invertLoopIndexes the invert loop indexes
   */
  private static void doTestRangeScan(int fullBlocks, int partialBlocks,
      boolean invertLoopIndexes) {
    int value;
    int value2;
    int count = 0;
    for (int i = 0; i <= 100; i++) {
      for (int j = 0; j <= 100; j++) {
        if (invertLoopIndexes) {
          value = j;
          value2 = i;
        } else {
          value = i;
          value2 = j;
        }
        String[] split = callCalculateSuffix(value, value2, fullBlocks, partialBlocks);
        for (String suffix : split) {
          logger.trace(suffix);
          String[] values = suffix.split("\\+", -1);
          if (values.length > 1) {
            try {
              value = Integer.parseInt(values[0]);
              value2 = Integer.parseInt(values[1]);
            } catch (NumberFormatException e) {
              Assertions.fail("NumberFormatException should never occur here");
            }
            // TODO JWL 12/12/2016 This never occurs so why do we care?
            if (value > 5 || value2 > 5) {
              count++;
              StringBuilder msg = new StringBuilder();
              msg.append("Found incorrect value ");
              msg.append(suffix);
              msg.append(". value = ");
              msg.append(invertLoopIndexes ? j : i);
              msg.append(" value2 = ");
              msg.append(invertLoopIndexes ? i : j);
              msg.append(" fullBlocks = ");
              msg.append(fullBlocks);
              msg.append(" partialBlocks = ");
              msg.append(partialBlocks);
              logger.trace(msg.toString());
            }
          }
        }
      }
    }
    Assertions.assertFalse(count > 0, "Incorrect values were founded " + count + " times");
  }

  /**
   * Call calculate suffix.
   *
   * @param value the value
   * @param value2 the value2
   * @param fullBlocks the full blocks
   * @param partialBlocks the partial blocks
   *
   * @return the string[]
   */
  private static String[] callCalculateSuffix(int value, int value2, int fullBlocks,
      int partialBlocks) {
    String body = "{0} ";

    VisualScoreTag visualScoreTag = new VisualScoreTag();

    // Values are based on datasources_table.jsp
    visualScoreTag.setFullBlocks(fullBlocks);
    visualScoreTag.setPartialBlocks(partialBlocks);
    visualScoreTag.setShowEmptyBlocks(true);
    visualScoreTag.setShowA(true);
    visualScoreTag.setShowB(true);

    visualScoreTag.setValue(value);
    visualScoreTag.setValue2(value2);

    String buf = visualScoreTag.calculateSuffix(body);

    return buf.split(" ", -1);
  }

}
