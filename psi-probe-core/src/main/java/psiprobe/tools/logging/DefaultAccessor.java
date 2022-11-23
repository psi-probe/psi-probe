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
package psiprobe.tools.logging;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import psiprobe.model.Application;

/**
 * The Class DefaultAccessor.
 */
public class DefaultAccessor {

  /** The Constant logger. */
  protected static final Logger logger = LoggerFactory.getLogger(DefaultAccessor.class);

  /** The application. */
  private Application application;

  /** The target. */
  private Object target;

  /**
   * Gets the application.
   *
   * @return the application
   */
  public Application getApplication() {
    return application;
  }

  /**
   * Sets the application.
   *
   * @param application the new application
   */
  public void setApplication(Application application) {
    this.application = application;
  }

  /**
   * Gets the target.
   *
   * @return the target
   */
  public Object getTarget() {
    return target;
  }

  /**
   * Sets the target.
   *
   * @param target the new target
   */
  public void setTarget(Object target) {
    this.target = target;
  }

  /**
   * Gets the target class.
   *
   * @return the target class
   */
  public String getTargetClass() {
    return getTarget().getClass().getName();
  }

  /**
   * Gets the property.
   *
   * @param obj the obj
   * @param name the name
   * @param defaultValue the default value
   * @param forced whether or not to force access to the field
   * @return the property
   */
  protected Object getProperty(Object obj, String name, Object defaultValue, boolean forced) {
    try {
      if (forced) {
        return FieldUtils.readField(obj, name, forced);
      }
      return PropertyUtils.isReadable(obj, name) ? PropertyUtils.getProperty(obj, name)
          : defaultValue;
    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException
        | NoSuchMethodException e) {
      logger.error("", e);
    }
    logger.debug("Could not access property '{}' of object '{}'", name, obj);
    return defaultValue;
  }

  /**
   * Gets the property.
   *
   * @param obj the obj
   * @param name the name
   * @param defaultValue the default value
   * @return the property
   */
  protected Object getProperty(Object obj, String name, Object defaultValue) {
    return getProperty(obj, name, defaultValue, false);
  }

  /**
   * Invoke method.
   *
   * @param object the object
   * @param name the name
   * @param param the param
   * @param defaultValue the default value
   * @return the object
   */
  protected Object invokeMethod(Object object, String name, Object param, Object defaultValue) {
    try {
      if (param == null) {
        return MethodUtils.invokeMethod(object, name);
      }
      return MethodUtils.invokeMethod(object, name, param);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      logger.error("", e);
    }
    return defaultValue;
  }

}
