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
package psiprobe.beans.stats.listeners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link AbstractStatsCollectionListener}.
 */
class AbstractStatsCollectionListenerTest {

  /** A minimal concrete implementation for testing */
  static class TestListener extends AbstractStatsCollectionListener {
    @Override
    public void statsCollected(StatsCollectionEvent sce) {
      // no-op
    }

    // Expose protected methods for testing
    String testGetPropertyValue(String name, String attribute) {
      return getPropertyValue(name, attribute);
    }

    String testGetPropertyKey(String name, String attribute) {
      return getPropertyKey(name, attribute);
    }

    boolean testIsEnabled() {
      return isEnabled();
    }

    void testSetEnabled(boolean enabled) {
      setEnabled(enabled);
    }
  }

  private TestListener listener;
  private static final String TEST_CATEGORY = "test.category";
  private static final String TEST_PROP_KEY = "psiprobe.beans.stats.listeners.test.category.myname.threshold";

  @BeforeEach
  void setUp() {
    listener = new TestListener();
    listener.setPropertyCategory(TEST_CATEGORY);
    // Clean up any system properties
    System.clearProperty(TEST_PROP_KEY);
  }

  @Test
  void testIsEnabledDefault() {
    assertTrue(listener.testIsEnabled());
  }

  @Test
  void testSetEnabled() {
    listener.testSetEnabled(false);
    assertTrue(!listener.testIsEnabled());
    listener.testSetEnabled(true);
    assertTrue(listener.testIsEnabled());
  }

  @Test
  void testGetPropertyCategory() {
    assertEquals(TEST_CATEGORY, listener.getPropertyCategory());
  }

  @Test
  void testGetPropertyKeyWithNameAndAttribute() {
    String key = listener.testGetPropertyKey("myname", "threshold");
    assertNotNull(key);
    assertTrue(key.contains("threshold"));
    assertTrue(key.contains("myname"));
  }

  @Test
  void testGetPropertyKeyAttributeNull() {
    assertThrows(IllegalArgumentException.class, () -> listener.testGetPropertyKey("name", null));
  }

  @Test
  void testGetPropertyValueFromSystemProperty() {
    System.setProperty(TEST_PROP_KEY, "500");
    try {
      String value = listener.testGetPropertyValue("myname", "threshold");
      assertEquals("500", value);
    } finally {
      System.clearProperty(TEST_PROP_KEY);
    }
  }

  @Test
  void testGetPropertyValueNullWhenNotDefined() {
    String value = listener.testGetPropertyValue("undefined.series", "threshold");
    assertNull(value);
  }

  @Test
  void testGetPropertyValueFallbackWithNullName() {
    // When value not found for specific name, should try key with null name
    String pkgKey = "psiprobe.beans.stats.listeners.test.category.threshold";
    System.setProperty(pkgKey, "fallback-value");
    try {
      String value = listener.testGetPropertyValue("myname", "threshold");
      assertEquals("fallback-value", value);
    } finally {
      System.clearProperty(pkgKey);
    }
  }

  @Test
  void testReset() {
    // Default reset is a no-op, but should not throw
    listener.reset();
    assertTrue(listener.testIsEnabled());
  }
}
