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
package psiprobe.tools;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.openmbean.CompositeData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class JmxTools.
 */
public final class JmxTools {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(JmxTools.class);

  /**
   * Prevent Instantiation.
   */
  private JmxTools() {
    // Prevent Instantiation
  }

  /**
   * Gets the attribute. All exceptions default to null.
   *
   * @param mbeanServer the mbean server
   * @param objectName the object name
   * @param attributeName the attribute name
   *
   * @return the attribute
   */
  public static Object getAttribute(MBeanServer mbeanServer, ObjectName objectName,
      String attributeName) {
    try {
      return mbeanServer.getAttribute(objectName, attributeName);
    } catch (AttributeNotFoundException e) {
      logger.error("MBean Object '{}' does not have '{}' attribute", objectName, attributeName);
      logger.trace("", e);
    } catch (RuntimeOperationsException e) {
      logger.error("MBean Object '{}' or Attribute '{}' are null", objectName, attributeName);
      logger.trace("", e);
    } catch (InstanceNotFoundException e) {
      logger.error("MBean Object '{}' not registered", objectName);
      logger.trace("", e);
    } catch (MBeanException | ReflectionException e) {
      logger.error("MBean Object '{}' not accessible", objectName);
      logger.trace("", e);
    }
    return null;
  }

  /**
   * Invoke mbean server method. All exceptions default to null.
   *
   * @param mbeanServer the mbean server
   * @param objectName the object name
   * @param method the method
   * @param parameters the parameters
   * @param signatures the signatures
   *
   * @return the object
   */
  public static Object invoke(MBeanServer mbeanServer, ObjectName objectName, String method,
      Object[] parameters, String[] signatures) {
    try {
      return mbeanServer.invoke(objectName, method, parameters, signatures);
    } catch (InstanceNotFoundException e) {
      logger.error("MBean Object '{}' not registered", objectName);
      logger.trace("", e);
    } catch (MBeanException | ReflectionException e) {
      logger.error("MBean Object '{}' not accessible", objectName);
      logger.trace("", e);
    }
    return null;
  }

  /**
   * Gets the long attribute. Default value used if null or any exceptions.
   *
   * @param mbeanServer the mbean server
   * @param objectName the object name
   * @param attributeName the attribute name
   * @param defaultValue the default value
   *
   * @return the long attr
   */
  public static long getLongAttr(MBeanServer mbeanServer, ObjectName objectName,
      String attributeName, long defaultValue) {
    Object object = JmxTools.getAttribute(mbeanServer, objectName, attributeName);
    return object == null ? defaultValue : (Long) object;
  }

  /**
   * Gets the long attribute. Default value '0' if null or any exceptions.
   *
   * @param compositeData the composite data
   * @param name the name
   *
   * @return the long attribute
   */
  public static long getLongAttr(CompositeData compositeData, String name) {
    Object object = compositeData.get(name);
    return object instanceof Long ? (Long) object : 0;
  }

  /**
   * Gets the long attribute. Default value '0' if null or any exceptions.
   *
   * @param mbeanServer the mbean server
   * @param objectName the object name
   * @param attributeName the attribute name
   *
   * @return the long attribute
   */
  public static long getLongAttr(MBeanServer mbeanServer, ObjectName objectName,
      String attributeName) {
    Object object = JmxTools.getAttribute(mbeanServer, objectName, attributeName);
    return object == null ? 0 : (Long) object;
  }

  /**
   * Gets the int attribute. Default value '0' if null or any exceptions.
   *
   * @param mbeanServer the mbean server
   * @param objectName the object name
   * @param attributeName the attribute name
   *
   * @return the int attribute
   */
  public static int getIntAttr(MBeanServer mbeanServer, ObjectName objectName,
      String attributeName) {
    Object object = JmxTools.getAttribute(mbeanServer, objectName, attributeName);
    return object == null ? 0 : (Integer) object;
  }

  /**
   * Gets the int attribute. Default to default value if not found.
   *
   * @param compositeData the composite data
   * @param name the name
   * @param defaultValue the default value
   *
   * @return the int attribute
   */
  public static int getIntAttr(CompositeData compositeData, String name, int defaultValue) {
    Object object = compositeData.get(name);
    return object instanceof Integer ? (Integer) object : defaultValue;
  }

  /**
   * Gets the string attribute. All exceptions default to null.
   *
   * @param mbeanServer the mbean server
   * @param objectName the object name
   * @param attributeName the attribute name
   *
   * @return the string attribute
   */
  public static String getStringAttr(MBeanServer mbeanServer, ObjectName objectName,
      String attributeName) {
    Object object = JmxTools.getAttribute(mbeanServer, objectName, attributeName);
    return object == null ? null : object.toString();
  }

  /**
   * Gets the string attribute.
   *
   * @param compositeData the composite data
   * @param name the name
   *
   * @return the string attribute
   */
  public static String getStringAttr(CompositeData compositeData, String name) {
    Object object = compositeData.get(name);
    return object == null ? null : object.toString();
  }

  /**
   * Gets the boolean attribute. All exceptions default to false.
   *
   * @param mbeanServer the mbean server
   * @param objectName the object name
   * @param attributeName the attribute name
   *
   * @return the string attribute
   */
  public static boolean getBooleanAttr(MBeanServer mbeanServer, ObjectName objectName,
      String attributeName) {
    Object object = JmxTools.getAttribute(mbeanServer, objectName, attributeName);
    return object instanceof Boolean && (Boolean) object;
  }

  /**
   * Gets the boolean attribute.
   *
   * @param compositeData the composite data
   * @param name the name
   *
   * @return the boolean attribute
   */
  public static boolean getBooleanAttr(CompositeData compositeData, String name) {
    Object object = compositeData.get(name);
    return object instanceof Boolean && (Boolean) object;
  }

  /**
   * Checks for attribute. All exceptions default to false.
   *
   * @param server the server
   * @param mbean the mbean
   * @param attributeName the attribute name
   *
   * @return true, if successful
   */
  public static boolean hasAttribute(MBeanServer server, ObjectName mbean, String attributeName) {
    try {
      MBeanInfo info = server.getMBeanInfo(mbean);
      for (MBeanAttributeInfo attributeInfo : info.getAttributes()) {
        if (attributeInfo.getName().equals(attributeName)) {
          return true;
        }
      }
    } catch (InstanceNotFoundException e) {
      logger.error("MBean Object '{}' not registered", mbean);
      logger.trace("", e);
    } catch (IntrospectionException | ReflectionException e) {
      logger.error("MBean Object '{}' not accessible", mbean);
      logger.trace("", e);
    }
    return false;
  }

}
