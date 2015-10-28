/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package com.googlecode.psiprobe.tokenizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * <code>UniqueList</code> is a successor of <code>java.util.Vector</code> to provide a collection
 * that contains no duplicate elements, more formally such that e1.compareTo(e2) == 0.
 * 
 * <p>
 * The collection is kept ordered whenever elements added or removed and besides uniqueness it is to
 * provide fast element search based again on e1.compareTo(e2) values.
 * </p>
 *
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 * @param <T> the generic type
 */
public class UniqueList<T extends Comparable<? super T>> extends ArrayList<T> {

  /* (non-Javadoc)
   * @see java.util.ArrayList#add(java.lang.Object)
   */
  @Override
  public synchronized boolean add(T obj) {
    return add(obj, null);
  }

  /**
   * Adds the.
   *
   * @param obj the obj
   * @param comp the comp
   * @return true, if successful
   */
  protected synchronized boolean add(T obj, Comparator<? super T> comp) {
    if (size() == 0) {
      return super.add(obj);
    } else {
      int index;
      index = comp == null
          ? Collections.binarySearch(this, obj)
          : Collections.binarySearch(this, obj, comp);
      if (index < 0) {
        int insertionPoint = -index - 1;
        if (insertionPoint >= size()) {
          super.add(obj);
        } else {
          super.add(insertionPoint, obj);
        }
      }
      return index < 0;
    }
  }

  /* (non-Javadoc)
   * @see java.util.ArrayList#add(int, java.lang.Object)
   */
  @Override
  public synchronized void add(int index, T obj) {
    add(obj);
  }

  /* (non-Javadoc)
   * @see java.util.ArrayList#addAll(java.util.Collection)
   */
  @Override
  public synchronized boolean addAll(Collection<? extends T> comp) {
    boolean ok = this != comp;
    if (ok) {
      Iterator<? extends T> iterator = comp.iterator();
      while (iterator.hasNext()) {
        ok &= this.add(iterator.next());
      }
    }
    return ok;
  }

}
