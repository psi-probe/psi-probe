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
package psiprobe.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Test;

/**
 * The Class DataSourceInfoGroupTest.
 */
class DataSourceInfoGroupTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(DataSourceInfoGroup.class).loadData().test();
  }

  @Test
  void testDefaultConstructor() {
    DataSourceInfoGroup group = new DataSourceInfoGroup();
    assertEquals(0, group.getBusyConnections());
    assertEquals(0, group.getEstablishedConnections());
    assertEquals(0, group.getMaxConnections());
    assertEquals(0, group.getDataSourceCount());
  }

  @Test
  void testBuilderFromDataSourceInfo() {
    DataSourceInfo info = new DataSourceInfo();
    info.setJdbcUrl("jdbc:h2:mem:test");
    info.setBusyConnections(3);
    info.setEstablishedConnections(5);
    info.setMaxConnections(10);

    DataSourceInfoGroup group = new DataSourceInfoGroup().builder(info);
    assertEquals("jdbc:h2:mem:test", group.getJdbcUrl());
    assertEquals(3, group.getBusyConnections());
    assertEquals(5, group.getEstablishedConnections());
    assertEquals(10, group.getMaxConnections());
    assertEquals(1, group.getDataSourceCount());
  }

  @Test
  void testAddMethods() {
    DataSourceInfoGroup group = new DataSourceInfoGroup();
    group.addBusyConnections(5);
    group.addEstablishedConnections(8);
    group.addMaxConnections(20);
    group.addDataSourceCount(1);

    assertEquals(5, group.getBusyConnections());
    assertEquals(8, group.getEstablishedConnections());
    assertEquals(20, group.getMaxConnections());
    assertEquals(1, group.getDataSourceCount());
  }

  @Test
  void testAddDataSourceInfo() {
    DataSourceInfoGroup group = new DataSourceInfoGroup();
    group.setBusyConnections(2);
    group.setEstablishedConnections(4);
    group.setMaxConnections(10);
    group.setDataSourceCount(1);

    DataSourceInfo info = new DataSourceInfo();
    info.setBusyConnections(3);
    info.setEstablishedConnections(6);
    info.setMaxConnections(15);

    group.addDataSourceInfo(info);

    assertEquals(5, group.getBusyConnections());
    assertEquals(10, group.getEstablishedConnections());
    assertEquals(25, group.getMaxConnections());
    assertEquals(2, group.getDataSourceCount());
  }
}
