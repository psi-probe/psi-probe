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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codebox.bean.JavaBeanTester;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * The Class TransportableModelTest.
 */
class TransportableModelTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(TransportableModel.class).loadData().test();
  }

  @Test
  void testGetItemsWhenNull() {
    TransportableModel model = new TransportableModel();
    // items is null by default, should return empty map
    assertNotNull(model.getItems());
    assertTrue(model.getItems().isEmpty());
  }

  @Test
  void testGetItemsWhenSet() {
    TransportableModel model = new TransportableModel();
    Map<String, Object> items = new HashMap<>();
    items.put("key1", "value1");
    model.setItems(items);
    assertEquals(1, model.getItems().size());
    assertEquals("value1", model.getItems().get("key1"));
  }

  @Test
  void testPutAll() {
    TransportableModel model = new TransportableModel();
    Map<String, Object> items = new HashMap<>();
    items.put("a", 1);
    items.put("b", 2);
    model.putAll(items);
    assertEquals(2, model.getItems().size());
  }

  @Test
  void testPutAllWithNull() {
    TransportableModel model = new TransportableModel();
    // Should not throw
    model.putAll(null);
    assertTrue(model.getItems().isEmpty());
  }

  @Test
  void testGetItemsReturnsDefensiveCopy() {
    TransportableModel model = new TransportableModel();
    Map<String, Object> items = new HashMap<>();
    items.put("key", "val");
    model.setItems(items);
    Map<String, Object> retrieved = model.getItems();
    retrieved.put("extra", "value");
    // original items should not be modified
    assertEquals(1, model.getItems().size());
  }
}
