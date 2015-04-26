package com.googlecode.psiprobe.jsp;

import junit.framework.TestCase;

public class VisualScoreTagTest extends TestCase {

	public void testRangeScanFullBlocks8() {
		// As usade in appRuntimeInfo.jsp
		doTestRangeScan(8, 5);
		
		// As usade in memory_pools.jsp
		doTestRangeScan(10, 5);
		
		// As usade in application.jsp
		doTestRangeScan(10, 5);
		
		// As usade in datasourcegroup.jsp
		doTestRangeScan(10, 5);
		
		// As usade in datasources_table.jsp
		doTestRangeScan(10, 5);
		
		// As usade in resources.jsp
		doTestRangeScan(10, 5);
		
		// As usade in sysinfo.jsp
		doTestRangeScan(10, 5);
		
	}
	
	private void doTestRangeScan(int fullBlocks, int partialBlocks) {
		int value = 0;
		int value2 = 0;
		for (int i = 0; i <= 100; i++) {
			value = i;
			for (int j = 0; j <= 100; j++) {
				value2 = j;
				String[] split = callCalculateSuffix(value, value2, fullBlocks, partialBlocks);
				for (int k = 0; k < split.length; k++) {
					// System.out.println(split[k]);
					String[] values = split[k].split("\\+");
	
					if (values.length > 1) {
						value = Integer.valueOf(values[0]).intValue();
						value2 = Integer.valueOf(values[1]).intValue();
	
						if (value > 5 || value2 > 5) {
							fail("Found incorrect value " + split[k]);
						}
					}
				}
			}
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
