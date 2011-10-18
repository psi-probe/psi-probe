/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.tools;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JmxTools {

    private static Log logger = LogFactory.getLog(JmxTools.class);

    public static Object getAttribute(MBeanServer mBeanServer, ObjectName oName, String attrName) throws Exception {
        try {
            return mBeanServer.getAttribute(oName, attrName);
        } catch (AttributeNotFoundException e) {
            logger.error(oName + " does not have \""+attrName+"\" attribute");
            return null;
        }
    }

    public static long getLongAttr(MBeanServer mBeanServer, ObjectName oName, String attrName, long defaultValue) {
        try {
            Object o = mBeanServer.getAttribute(oName, attrName);
            return o == null ? defaultValue : ((Long) o).longValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long getLongAttr(MBeanServer mBeanServer, ObjectName oName, String attrName) throws Exception {
        return ((Long) mBeanServer.getAttribute(oName, attrName)).longValue();
    }

    public static int getIntAttr(MBeanServer mBeanServer, ObjectName oName, String attrName) throws Exception {
        return ((Integer) mBeanServer.getAttribute(oName, attrName)).intValue();
    }

    public static String getStringAttr(MBeanServer mBeanServer, ObjectName oName, String attrName) throws Exception {
        Object o = getAttribute(mBeanServer, oName, attrName);
        return o == null ? null : o.toString();
    }

    public static long getLongAttr(CompositeData cds, String name) {
        Object o = cds.get(name);
        if (o != null && o instanceof Long) {
            return ((Long)o).longValue();
        } else {
            return 0;
        }
    }

    public static String getStringAttr(CompositeData cds, String name) {
        Object o = cds.get(name);
        return o != null ? o.toString() : null;
    }

    public static boolean getBooleanAttr(CompositeData cds, String name) {
        Object o = cds.get(name);
        return o != null && o instanceof Boolean && ((Boolean) o).booleanValue();
    }

    public static int getIntAttr(CompositeData cds, String name, int defaultValue) {
        Object o = cds.get(name);

        if (o != null && o instanceof Integer) {
            return ((Integer)o).intValue();
        } else {
            return defaultValue;
        }
    }

    public static boolean hasAttribute(MBeanServer server, ObjectName mbean, String attrName) throws Exception {
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
