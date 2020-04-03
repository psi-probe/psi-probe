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
package psiprobe.model;

/**
 * POJO representing "quick check" report.
 */
public class TomcatTestReport {

  /** The Constant TEST_UNKNOWN. */
  public static final int TEST_UNKNOWN = 0;

  /** The Constant TEST_PASSED. */
  public static final int TEST_PASSED = 1;

  /** The Constant TEST_FAILED. */
  public static final int TEST_FAILED = 2;

  /**
   * The default memory size.
   *
   * {@value #DEFAULT_MEMORY_SIZE}
   */
  public static final int DEFAULT_MEMORY_SIZE = 1024 * 1024;

  /** The default file count. */
  private static final int DEFAULT_FILE_COUNT = 10;

  /** The context name. */
  private String contextName;

  /** The data source name. */
  private String dataSourceName;

  /** The datasource usage score. */
  private int datasourceUsageScore;

  /** The max service time. */
  private long maxServiceTime;

  /** The datasource test. */
  private int datasourceTest = TEST_UNKNOWN;

  /** The file test. */
  private int fileTest = TEST_UNKNOWN;

  /** The memory test. */
  private int memoryTest = TEST_UNKNOWN;

  /** The webapp availability test. */
  private int webappAvailabilityTest = TEST_UNKNOWN;

  /** The test duration. */
  private long testDuration;

  /**
   * Gets the context name.
   *
   * @return the context name
   */
  public String getContextName() {
    return contextName;
  }

  /**
   * Sets the context name.
   *
   * @param contextName the new context name
   */
  public void setContextName(String contextName) {
    this.contextName = contextName;
  }

  /**
   * Gets the datasource usage score.
   *
   * @return the datasource usage score
   */
  public int getDatasourceUsageScore() {
    return datasourceUsageScore;
  }

  /**
   * Sets the datasource usage score.
   *
   * @param datasourceUsageScore the new datasource usage score
   */
  public void setDatasourceUsageScore(int datasourceUsageScore) {
    this.datasourceUsageScore = datasourceUsageScore;
  }

  /**
   * Gets the data source name.
   *
   * @return the data source name
   */
  public String getDataSourceName() {
    return dataSourceName;
  }

  /**
   * Sets the data source name.
   *
   * @param dataSourceName the new data source name
   */
  public void setDataSourceName(String dataSourceName) {
    this.dataSourceName = dataSourceName;
  }

  /**
   * Gets the datasource test.
   *
   * @return the datasource test
   */
  public int getDatasourceTest() {
    return datasourceTest;
  }

  /**
   * Sets the datasource test.
   *
   * @param datasourceTest the new datasource test
   */
  public void setDatasourceTest(int datasourceTest) {
    this.datasourceTest = datasourceTest;
  }

  /**
   * Gets the file test.
   *
   * @return the file test
   */
  public int getFileTest() {
    return fileTest;
  }

  /**
   * Sets the file test.
   *
   * @param fileTest the new file test
   */
  public void setFileTest(int fileTest) {
    this.fileTest = fileTest;
  }

  /**
   * Gets the memory test.
   *
   * @return the memory test
   */
  public int getMemoryTest() {
    return memoryTest;
  }

  /**
   * Sets the memory test.
   *
   * @param memoryTest the new memory test
   */
  public void setMemoryTest(int memoryTest) {
    this.memoryTest = memoryTest;
  }

  /**
   * Gets the default file count.
   *
   * @return the default file count
   */
  public int getDefaultFileCount() {
    return DEFAULT_FILE_COUNT;
  }

  /**
   * Gets the test duration.
   *
   * @return the test duration
   */
  public long getTestDuration() {
    return testDuration;
  }

  /**
   * Sets the test duration.
   *
   * @param testDuration the new test duration
   */
  public void setTestDuration(long testDuration) {
    this.testDuration = testDuration;
  }

  /**
   * Gets the max processing time.
   *
   * @return the max processing time
   */
  public long getMaxProcessingTime() {
    return maxServiceTime;
  }

  /**
   * Sets the max service time.
   *
   * @param maxProcessingTime the new max service time
   */
  public void setMaxServiceTime(long maxProcessingTime) {
    this.maxServiceTime = maxProcessingTime;
  }

  /**
   * Gets the webapp availability test.
   *
   * @return the webapp availability test
   */
  public int getWebappAvailabilityTest() {
    return webappAvailabilityTest;
  }

  /**
   * Sets the webapp availability test.
   *
   * @param webappAvailabilityTest the new webapp availability test
   */
  public void setWebappAvailabilityTest(int webappAvailabilityTest) {
    this.webappAvailabilityTest = webappAvailabilityTest;
  }

}
