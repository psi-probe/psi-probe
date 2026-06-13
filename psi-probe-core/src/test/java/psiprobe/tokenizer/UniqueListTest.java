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
package psiprobe.tokenizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link UniqueList}.
 */
class UniqueListTest {

  @Test
  void testAddUnique() {
    UniqueList<Integer> list = new UniqueList<>();
    assertTrue(list.add(3));
    assertTrue(list.add(1));
    assertTrue(list.add(2));
    assertEquals(3, list.size());
    // Should be sorted
    assertEquals(1, (int) list.get(0));
    assertEquals(2, (int) list.get(1));
    assertEquals(3, (int) list.get(2));
  }

  @Test
  void testAddDuplicateIgnored() {
    UniqueList<Integer> list = new UniqueList<>();
    assertTrue(list.add(5));
    assertFalse(list.add(5)); // duplicate should not be added
    assertEquals(1, list.size());
  }

  @Test
  void testAddAtIndexIgnoresIndex() {
    UniqueList<Integer> list = new UniqueList<>();
    list.add(0, 3);
    list.add(0, 1);
    list.add(0, 2);
    assertEquals(3, list.size());
    // Should be sorted regardless of index passed
    assertEquals(1, (int) list.get(0));
    assertEquals(2, (int) list.get(1));
    assertEquals(3, (int) list.get(2));
  }

  @Test
  void testAddAll() {
    UniqueList<Integer> list = new UniqueList<>();
    List<Integer> input = Arrays.asList(5, 3, 1, 3, 5);
    list.addAll(input);
    assertEquals(3, list.size());
    assertEquals(1, (int) list.get(0));
    assertEquals(3, (int) list.get(1));
    assertEquals(5, (int) list.get(2));
  }

  @Test
  void testAddAllWithSelf() {
    UniqueList<Integer> list = new UniqueList<>();
    list.add(1);
    list.add(2);
    // addAll with self should return false
    assertFalse(list.addAll(list));
    assertEquals(2, list.size());
  }

  @Test
  void testAddToEmpty() {
    UniqueList<String> list = new UniqueList<>();
    assertTrue(list.add("hello"));
    assertEquals(1, list.size());
    assertEquals("hello", list.get(0));
  }

  @Test
  void testAddBeyondEnd() {
    UniqueList<Integer> list = new UniqueList<>();
    list.add(1);
    list.add(2);
    list.add(10); // should go at end
    assertEquals(3, list.size());
    assertEquals(10, (int) list.get(2));
  }

  @Test
  void testSortedInsertion() {
    UniqueList<String> list = new UniqueList<>();
    list.add("charlie");
    list.add("alpha");
    list.add("beta");
    assertEquals("alpha", list.get(0));
    assertEquals("beta", list.get(1));
    assertEquals("charlie", list.get(2));
  }
}
