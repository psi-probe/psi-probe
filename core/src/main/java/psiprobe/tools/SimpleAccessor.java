/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * The Class SimpleAccessor.
 */
public class SimpleAccessor implements Accessor {

  /** The Constant Logger. */
  private static final Logger logger = LoggerFactory.getLogger(SimpleAccessor.class);

  @Override
  public Object get(Object obj, Field field) {
    boolean accessible = pre(field);
    try {
      return get0(obj, field);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      logger.trace("", e);
      return null;
    } finally {
      post(field, accessible);
    }
  }

  /**
   * Gets the 0.
   *
   * @param obj the obj
   * @param field the field
   * @return the 0
   * @throws IllegalArgumentException the illegal argument exception
   * @throws IllegalAccessException the illegal access exception
   */
  private Object get0(Object obj, Field field)
      throws IllegalArgumentException, IllegalAccessException {
    
    if (field.isAccessible()) {
      return field.get(obj);
    }
    return null;
  }

  /**
   * Pre.
   *
   * @param field the field
   * @return true, if successful
   */
  private boolean pre(Field field) {
    boolean accessible = field.isAccessible();
    if (!accessible) {
      try {
        field.setAccessible(true);
      } catch (SecurityException ex) {
        logger.trace("", ex);
      }
    }
    return accessible;
  }

  /**
   * Post.
   *
   * @param field the field
   * @param value the value
   */
  private void post(Field field, boolean value) {
    if (!value) {
      try {
        field.setAccessible(false);
      } catch (SecurityException ex) {
        logger.trace("", ex);
      }
    }
  }

}
