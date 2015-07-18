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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

/**
 * 
 * @author Vlad Ilyushchenko
 */
public class JmxTools {

  private static Log logger = LogFactory.getLog(JmxTools.class);

  public static Object getAttribute(MBeanServer mbeanServer, ObjectName objName, String attrName)
      throws Exception {

    try {
      return mbeanServer.getAttribute(objName, attrName);
    } catch (AttributeNotFoundException e) {
      logger.error(objName + " does not have \"" + attrName + "\" attribute");
      return null;
    }
  }

  public static long getLongAttr(MBeanServer mbeanServer, ObjectName objName, String attrName,
      long defaultValue) {

    try {
      Object obj = mbeanServer.getAttribute(objName, attrName);
      return obj == null ? defaultValue : ((Long) obj).longValue();
    } catch (Exception e) {
      return defaultValue;
    }
  }

  public static long getLongAttr(CompositeData cds, String name) {
    Object obj = cds.get(name);
    if (obj != null && obj instanceof Long) {
      return ((Long) obj).longValue();
    } else {
      return 0;
    }
  }

  public static long getLongAttr(MBeanServer mbeanServer, ObjectName objName, String attrName)
      throws Exception {

    return ((Long) mbeanServer.getAttribute(objName, attrName)).longValue();
  }

  public static int getIntAttr(MBeanServer mbeanServer, ObjectName objName, String attrName)
      throws Exception {

    return ((Integer) mbeanServer.getAttribute(objName, attrName)).intValue();
  }

  public static int getIntAttr(CompositeData cds, String name, int defaultValue) {
    Object obj = cds.get(name);

    if (obj != null && obj instanceof Integer) {
      return ((Integer) obj).intValue();
    } else {
      return defaultValue;
    }
  }

  public static String getStringAttr(MBeanServer mbeanServer, ObjectName objName, String attrName)
      throws Exception {

    Object obj = getAttribute(mbeanServer, objName, attrName);
    return obj == null ? null : obj.toString();
  }

  public static String getStringAttr(CompositeData cds, String name) {
    Object obj = cds.get(name);
    return obj != null ? obj.toString() : null;
  }

  public static boolean getBooleanAttr(CompositeData cds, String name) {
    Object obj = cds.get(name);
    return obj != null && obj instanceof Boolean && ((Boolean) obj).booleanValue();
  }

  public static boolean hasAttribute(MBeanServer server, ObjectName mbean, String attrName)
      throws Exception {

    MBeanInfo info = server.getMBeanInfo(mbean);
    MBeanAttributeInfo[] ai = info.getAttributes();
    for (int i = 0; i < ai.length; i++) {
      if (ai[i].getName().equals(attrName)) {
        return true;
      }
    }
    return false;
  }

}
