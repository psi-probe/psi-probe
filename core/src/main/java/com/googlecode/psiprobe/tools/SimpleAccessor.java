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

package com.googlecode.psiprobe.tools;

import java.lang.reflect.Field;

/**
 *
 * @author Mark Lewis
 */
public class SimpleAccessor implements Accessor {

  public Object get(Object obj, Field field) {
    boolean accessible = pre(field);
    try {
      return get0(obj, field);
    } catch (Exception e) {
      return null;
    } finally {
      post(field, accessible);
    }
  }

  private Object get0(Object obj, Field field)
      throws IllegalArgumentException, IllegalAccessException {
    
    if (field.isAccessible()) {
      return field.get(obj);
    } else {
      return null;
    }
  }

  private boolean pre(Field field) {
    boolean accessible = field.isAccessible();
    if (!accessible) {
      try {
        field.setAccessible(true);
      } catch (SecurityException ex) {
        // ignore
      }
    }
    return accessible;
  }

  private void post(Field field, boolean value) {
    if (!value) {
      try {
        field.setAccessible(false);
      } catch (SecurityException ex) {
        // ignore
      }
    }
  }

}
