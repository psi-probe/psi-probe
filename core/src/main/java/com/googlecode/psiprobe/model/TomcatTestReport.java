/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.model;

/**
 * POJO representing "quick check" report.
 *
 * @author Vlad Ilyushchenko
 */
public class TomcatTestReport {
    public static final int TEST_UNKNOWN = 0;
    public static final int TEST_PASSED = 1;
    public static final int TEST_FAILED = 2;

    private int defaultMemorySize = 1024*1024; // 1MB
    private int defaultFileCount = 10;

    private String contextName;
    private String dataSourceName;
    private int datasourceUsageScore;
    private long maxServiceTime;
    private int datasourceTest = TEST_UNKNOWN;
    private int fileTest = TEST_UNKNOWN;
    private int memoryTest = TEST_UNKNOWN;
    private int webappAvailabilityTest = TEST_UNKNOWN;
    private long testDuration;

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public int getDatasourceUsageScore() {
        return datasourceUsageScore;
    }

    public void setDatasourceUsageScore(int datasourceUsageScore) {
        this.datasourceUsageScore = datasourceUsageScore;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public int getDatasourceTest() {
        return datasourceTest;
    }

    public void setDatasourceTest(int datasourceTest) {
        this.datasourceTest = datasourceTest;
    }

    public int getFileTest() {
        return fileTest;
    }

    public void setFileTest(int fileTest) {
        this.fileTest = fileTest;
    }

    public int getMemoryTest() {
        return memoryTest;
    }

    public void setMemoryTest(int memoryTest) {
        this.memoryTest = memoryTest;
    }

    public int getDefaultMemorySize() {
        return defaultMemorySize;
    }

    public int getDefaultFileCount() {
        return defaultFileCount;
    }

    public long getTestDuration() {
        return testDuration;
    }

    public void setTestDuration(long testDuration) {
        this.testDuration = testDuration;
    }

    public long getMaxProcessingTime() {
        return maxServiceTime;
    }

    public void setMaxServiceTime(long maxProcessingTime) {
        this.maxServiceTime = maxProcessingTime;
    }

    public int getWebappAvailabilityTest() {
        return webappAvailabilityTest;
    }

    public void setWebappAvailabilityTest(int webappAvailabilityTest) {
        this.webappAvailabilityTest = webappAvailabilityTest;
    }
}
