package com.googlecode.psiprobe.jsp;

import junit.framework.TestCase;

public class VisualScoreTagTest extends TestCase {

  public void testRangeScan() {
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

  private void doTestRangeScan(int fullBlocks, int partialBlocks, boolean invertLoopIndexes) {
    int value = 0;
    int value2 = 0;
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
        for (int k = 0; k < split.length; k++) {
          // System.out.println(split[k]);
          String[] values = split[k].split("\\+");

          if (values.length > 1) {
            value = Integer.valueOf(values[0]).intValue();
            value2 = Integer.valueOf(values[1]).intValue();

            if (value > 5 || value2 > 5) {
              count++;
              StringBuffer msg = new StringBuffer();
              msg.append("Found incorrect value ");
              msg.append(split[k]);
              msg.append(". value = ");
              msg.append(invertLoopIndexes ? j : i);
              msg.append(" value2 = ");
              msg.append(invertLoopIndexes ? i : j);
              msg.append(" fullBlocks = ");
              msg.append(fullBlocks);
              msg.append(" partialBlocks = ");
              msg.append(partialBlocks);
              // System.out.println(msg.toString());
            }
          }
        }
      }
    }
    if (count > 0) {
      fail("Incorrect values were founded " + count + " times");
    }
  }

  private String[] callCalculateSuffix(int value, int value2, int fullBlocks, int partialBlocks) {
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

    StringBuffer buf = visualScoreTag.calculateSuffix(body);

    return buf.toString().split(" ");
  }

}
