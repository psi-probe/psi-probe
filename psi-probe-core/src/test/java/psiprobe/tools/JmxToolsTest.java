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
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JmxToolsTest {

  private MBeanServer mbeanServer;
  private ObjectName objectName;
  private CompositeData compositeData;

  @BeforeEach
  void setUp() throws Exception {
    mbeanServer = Mockito.mock(MBeanServer.class);
    objectName = new ObjectName("domain:type=Test");
    compositeData = Mockito.mock(CompositeData.class);
  }

  @Test
  void testGetAttribute_success() throws Exception {
    Mockito.when(mbeanServer.getAttribute(objectName, "attr")).thenReturn("value");
    Assertions.assertEquals("value", JmxTools.getAttribute(mbeanServer, objectName, "attr"));
  }

  @Test
  void testGetAttribute_AttributeNotFoundException() throws Exception {
    Mockito.when(mbeanServer.getAttribute(objectName, "attr"))
        .thenThrow(new AttributeNotFoundException());
    Assertions.assertNull(JmxTools.getAttribute(mbeanServer, objectName, "attr"));
  }

  @Test
  void testInvoke_success() throws Exception {
    Mockito.when(mbeanServer.invoke(objectName, "method", null, null)).thenReturn("result");
    Assertions.assertEquals("result",
        JmxTools.invoke(mbeanServer, objectName, "method", null, null));
  }

  @Test
  void testInvoke_InstanceNotFoundException() throws Exception {
    Mockito.when(mbeanServer.invoke(objectName, "method", null, null))
        .thenThrow(new InstanceNotFoundException());
    Assertions.assertNull(JmxTools.invoke(mbeanServer, objectName, "method", null, null));
  }

  @Test
  void testGetLongAttr_MBeanServer() throws InstanceNotFoundException, AttributeNotFoundException,
      ReflectionException, MBeanException {
    Mockito.when(mbeanServer.getAttribute(Mockito.any(), Mockito.any())).thenReturn(42L);
    Assertions.assertEquals(42L, JmxTools.getLongAttr(mbeanServer, objectName, "attr", 10L));
  }

  @Test
  void testGetLongAttr_MBeanServer_default() {
    Assertions.assertEquals(10L, JmxTools.getLongAttr(mbeanServer, objectName, "attr", 10L));
  }

  @Test
  void testGetLongAttr_CompositeData() {
    Mockito.when(compositeData.get("longAttr")).thenReturn(123L);
    Assertions.assertEquals(123L, JmxTools.getLongAttr(compositeData, "longAttr"));
  }

  @Test
  void testGetLongAttr_CompositeData_default() {
    Mockito.when(compositeData.get("longAttr")).thenReturn(null);
    Assertions.assertEquals(0L, JmxTools.getLongAttr(compositeData, "longAttr"));
  }

  @Test
  void testGetIntAttr_MBeanServer() throws InstanceNotFoundException, AttributeNotFoundException,
      ReflectionException, MBeanException {
    Mockito.when(mbeanServer.getAttribute(Mockito.any(), Mockito.any())).thenReturn(7);
    Assertions.assertEquals(7, JmxTools.getIntAttr(mbeanServer, objectName, "attr"));
  }

  @Test
  void testGetIntAttr_CompositeData() {
    Mockito.when(compositeData.get("intAttr")).thenReturn(5);
    Assertions.assertEquals(5, JmxTools.getIntAttr(compositeData, "intAttr", 2));
  }

  @Test
  void testGetIntAttr_CompositeData_default() {
    Mockito.when(compositeData.get("intAttr")).thenReturn(null);
    Assertions.assertEquals(2, JmxTools.getIntAttr(compositeData, "intAttr", 2));
  }

  @Test
  void testGetStringAttr_MBeanServer() throws InstanceNotFoundException, AttributeNotFoundException,
      ReflectionException, MBeanException {
    Mockito.when(mbeanServer.getAttribute(Mockito.any(), Mockito.any())).thenReturn("str");
    Assertions.assertEquals("str", JmxTools.getStringAttr(mbeanServer, objectName, "attr"));
  }

  @Test
  void testGetStringAttr_CompositeData() {
    Mockito.when(compositeData.get("strAttr")).thenReturn("hello");
    Assertions.assertEquals("hello", JmxTools.getStringAttr(compositeData, "strAttr"));
  }

  @Test
  void testGetBooleanAttr_MBeanServer() throws InstanceNotFoundException,
      AttributeNotFoundException, ReflectionException, MBeanException {
    Mockito.when(mbeanServer.getAttribute(Mockito.any(), Mockito.any())).thenReturn(true);
    Assertions.assertTrue(JmxTools.getBooleanAttr(mbeanServer, objectName, "attr"));
  }

  @Test
  void testGetBooleanAttr_CompositeData() {
    Mockito.when(compositeData.get("boolAttr")).thenReturn(Boolean.TRUE);
    Assertions.assertTrue(JmxTools.getBooleanAttr(compositeData, "boolAttr"));
  }

  @Test
  void testHasAttribute_true() throws Exception {
    MBeanAttributeInfo attrInfo =
        new MBeanAttributeInfo("attr", "java.lang.String", "", true, false, false);
    MBeanInfo mbeanInfo =
        new MBeanInfo("class", "", new MBeanAttributeInfo[] {attrInfo}, null, null, null);
    Mockito.when(mbeanServer.getMBeanInfo(objectName)).thenReturn(mbeanInfo);
    Assertions.assertTrue(JmxTools.hasAttribute(mbeanServer, objectName, "attr"));
  }

  @Test
  void testHasAttribute_false() throws Exception {
    MBeanInfo mbeanInfo = new MBeanInfo("class", "", new MBeanAttributeInfo[0], null, null, null);
    Mockito.when(mbeanServer.getMBeanInfo(objectName)).thenReturn(mbeanInfo);
    Assertions.assertFalse(JmxTools.hasAttribute(mbeanServer, objectName, "attr"));
  }
}
