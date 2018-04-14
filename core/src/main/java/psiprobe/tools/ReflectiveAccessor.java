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
package psiprobe.tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ReflectiveAccessor.
 */
public class ReflectiveAccessor implements Accessor {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ReflectiveAccessor.class);

  /** The reflection factory. */
  private static Object reflectionFactory;

  /** The new field accessor. */
  private static Method newFieldAccessor;

  /** The get. */
  private static Method get;

  /**
   * Instantiates a new reflective accessor.
   *
   * @throws ClassNotFoundException the class not found exception
   * @throws InstantiationException the instantiation exception
   * @throws IllegalAccessException the illegal access exception
   * @throws NoSuchMethodException the no such method exception
   */
  ReflectiveAccessor() throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, NoSuchMethodException {

    init();
  }

  @Override
  public Object get(Object obj, Field field) {
    try {
      Object fieldAccessor = getFieldAccessor(field);
      if (fieldAccessor != null) {
        return get.invoke(fieldAccessor, obj);
      }
    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
      logger.trace("", e);
    }
    return null;
  }

  /**
   * Gets the field accessor.
   *
   * @param field the field
   * @return the field accessor
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   */
  private static Object getFieldAccessor(Field field)
      throws IllegalAccessException, InvocationTargetException {

    if (newFieldAccessor.getParameterTypes().length == 1) {
      return newFieldAccessor.invoke(reflectionFactory, field);
    }
    return newFieldAccessor.invoke(reflectionFactory, field, Boolean.TRUE);
  }

  /**
   * Inits the.
   *
   * @throws ClassNotFoundException the class not found exception
   * @throws InstantiationException the instantiation exception
   * @throws IllegalAccessException the illegal access exception
   * @throws NoSuchMethodException the no such method exception
   */
  private static void init() throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, NoSuchMethodException {

    String vmVendor = System.getProperty("java.vm.vendor");
    if (vmVendor != null
        && (vmVendor.contains("Sun Microsystems") || vmVendor.contains("Apple Computer")
            || vmVendor.contains("Apple Inc.") || vmVendor.contains("IBM Corporation"))) {

      reflectionFactory = getReflectionFactory();
      Class<?>[] paramTypes = new Class[] {Field.class, Boolean.TYPE};
      newFieldAccessor = reflectionFactory.getClass().getMethod("newFieldAccessor", paramTypes);
      get = newFieldAccessor.getReturnType().getMethod("get", Object.class);
    }
  }

  /**
   * Gets the reflection factory.
   *
   * @return the reflection factory
   * @throws ClassNotFoundException the class not found exception
   * @throws InstantiationException the instantiation exception
   * @throws IllegalAccessException the illegal access exception
   */
  private static Object getReflectionFactory()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {

    Class<?> getReflectionFactoryActionClass =
        Class.forName("sun.reflect.ReflectionFactory$GetReflectionFactoryAction");
    PrivilegedAction<?> getReflectionFactoryAction =
        (PrivilegedAction<?>) getReflectionFactoryActionClass.newInstance();
    return AccessController.doPrivileged(getReflectionFactoryAction);
  }

}
