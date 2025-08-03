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
package psiprobe.model.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codebox.bean.JavaBeanTester;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * The Class DataSourceTestInfoTest.
 */
class DataSourceTestInfoTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(DataSourceTestInfo.class).loadData().skipStrictSerializable().test();
  }

  /**
   * Test add query to history.
   */
  @Test
  void testAddQueryToHistory() {
    DataSourceTestInfo info = new DataSourceTestInfo();
    info.setHistorySize(3);

    info.addQueryToHistory("SELECT 1");
    info.addQueryToHistory("SELECT 2");
    info.addQueryToHistory("SELECT 3");

    List<String> history = info.getQueryHistory();
    assertEquals(3, history.size());
    assertEquals("SELECT 3", history.get(0));
    assertEquals("SELECT 2", history.get(1));
    assertEquals("SELECT 1", history.get(2));

    // Add duplicate, should move to front
    info.addQueryToHistory("SELECT 2");
    history = info.getQueryHistory();
    assertEquals(3, history.size());
    assertEquals("SELECT 2", history.get(0));
    assertEquals("SELECT 3", history.get(1));
    assertEquals("SELECT 1", history.get(2));

    // Add new, should remove oldest
    info.addQueryToHistory("SELECT 4");
    history = info.getQueryHistory();
    assertEquals(3, history.size());
    assertEquals("SELECT 4", history.get(0));
    assertEquals("SELECT 2", history.get(1));
    assertEquals("SELECT 3", history.get(2));
  }

}
