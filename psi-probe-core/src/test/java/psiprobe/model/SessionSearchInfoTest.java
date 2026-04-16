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
package psiprobe.model;

import com.codebox.bean.JavaBeanTester;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class SessionSearchInfoTest.
 */
class SessionSearchInfoTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(SessionSearchInfo.class).loadData().skipStrictSerializable().test();
  }

  /**
   * Session search flags and defaults test.
   */
  @Test
  void sessionSearchDefaultsAndFlagsTest() {
    SessionSearchInfo info = new SessionSearchInfo();

    Assertions.assertEquals(SessionSearchInfo.ACTION_NONE, info.getSearchAction());
    Assertions.assertTrue(info.isEmpty());
    Assertions.assertTrue(info.isValid());
    Assertions.assertFalse(info.isApply());
    Assertions.assertFalse(info.isClear());
    Assertions.assertFalse(info.isUseSearch());
    Assertions.assertFalse(info.isUseAttr());
    Assertions.assertFalse(info.isUseSessionId());
    Assertions.assertFalse(info.isUseAttrName());
    Assertions.assertFalse(info.isUseAgeFrom());
    Assertions.assertFalse(info.isUseAgeTo());
    Assertions.assertFalse(info.isUseIdleTimeFrom());
    Assertions.assertFalse(info.isUseIdleTimeTo());
    Assertions.assertFalse(info.isUseLastIp());

    info.setSearchAction(SessionSearchInfo.ACTION_APPLY);
    info.setSessionId("abc");
    Assertions.assertTrue(info.isApply());
    Assertions.assertFalse(info.isClear());
    Assertions.assertTrue(info.isUseSearch());
    Assertions.assertTrue(info.isUseSessionId());

    info.setSearchAction(SessionSearchInfo.ACTION_CLEAR);
    Assertions.assertFalse(info.isApply());
    Assertions.assertTrue(info.isClear());
    Assertions.assertFalse(info.isUseSearch());

    info.setSearchAction(null);
    Assertions.assertFalse(info.isApply());
    Assertions.assertFalse(info.isClear());
  }

  /**
   * Session id and attribute pattern validation test.
   */
  @Test
  void patternValidationTest() {
    SessionSearchInfo info = new SessionSearchInfo();

    info.setSessionId("test.*");
    Assertions.assertNull(info.getSessionIdMsg());
    Assertions.assertNotNull(info.getSessionIdPattern());
    Assertions.assertTrue(info.isSessionIdValid());

    info.setSessionId("(");
    Assertions.assertNull(info.getSessionIdPattern());
    Assertions.assertNotNull(info.getSessionIdMsg());
    Assertions.assertFalse(info.isSessionIdValid());
    Assertions.assertFalse(info.isUseSessionId());

    info.setAttrName("user.*,token");
    Assertions.assertTrue(info.isAttrNameValid());
    Assertions.assertTrue(info.isUseAttrName());
    Assertions.assertEquals(2, info.getAttrNamePatterns().size());
    Assertions.assertTrue(info.getAttrNameMsgs().isEmpty());

    info.setAttrName("(");
    Assertions.assertFalse(info.isAttrNameValid());
    Assertions.assertFalse(info.isUseAttrName());
    Assertions.assertEquals(1, info.getAttrNameMsgs().size());
  }

  /**
   * Numeric and list behavior test.
   */
  @Test
  void numericAndListBehaviorTest() {
    SessionSearchInfo info = new SessionSearchInfo();

    info.setAgeFrom("10");
    info.setAgeTo("20");
    info.setIdleTimeFrom("30");
    info.setIdleTimeTo("40");
    info.setLastIp("127.0.0.1");
    info.setInfoMessage("ok");

    Assertions.assertTrue(info.isAgeFromValid());
    Assertions.assertTrue(info.isAgeToValid());
    Assertions.assertTrue(info.isIdleTimeFromValid());
    Assertions.assertTrue(info.isIdleTimeToValid());
    Assertions.assertTrue(info.isUseAgeFrom());
    Assertions.assertTrue(info.isUseAgeTo());
    Assertions.assertTrue(info.isUseIdleTimeFrom());
    Assertions.assertTrue(info.isUseIdleTimeTo());
    Assertions.assertTrue(info.isUseLastIp());
    Assertions.assertEquals(Integer.valueOf(10), info.getAgeFromSec());
    Assertions.assertEquals(Integer.valueOf(20), info.getAgeToSec());
    Assertions.assertEquals(Integer.valueOf(30), info.getIdleTimeFromSec());
    Assertions.assertEquals(Integer.valueOf(40), info.getIdleTimeToSec());
    Assertions.assertEquals("ok", info.getInfoMessage());

    info.setAgeFrom("x");
    info.setAgeTo("x");
    info.setIdleTimeFrom("x");
    info.setIdleTimeTo("x");
    Assertions.assertFalse(info.isAgeFromValid());
    Assertions.assertFalse(info.isAgeToValid());
    Assertions.assertFalse(info.isIdleTimeFromValid());
    Assertions.assertFalse(info.isIdleTimeToValid());
    Assertions.assertFalse(info.isValid());

    info.addErrorMessage("err");
    List<String> messages = info.getErrorMessages();
    Assertions.assertEquals(1, messages.size());
    messages.clear();
    Assertions.assertEquals(1, info.getErrorMessages().size());
  }

  /**
   * Empty and valid branch coverage test.
   */
  @Test
  void emptyAndValidBranchCoverageTest() {
    SessionSearchInfo withSessionId = new SessionSearchInfo();
    withSessionId.setSessionId("x");
    Assertions.assertFalse(withSessionId.isEmpty());

    SessionSearchInfo withAttrName = new SessionSearchInfo();
    withAttrName.setAttrName("x");
    Assertions.assertFalse(withAttrName.isEmpty());

    SessionSearchInfo withAgeFrom = new SessionSearchInfo();
    withAgeFrom.setAgeFrom("1");
    Assertions.assertFalse(withAgeFrom.isEmpty());

    SessionSearchInfo withAgeTo = new SessionSearchInfo();
    withAgeTo.setAgeTo("1");
    Assertions.assertFalse(withAgeTo.isEmpty());

    SessionSearchInfo withIdleFrom = new SessionSearchInfo();
    withIdleFrom.setIdleTimeFrom("1");
    Assertions.assertFalse(withIdleFrom.isEmpty());

    SessionSearchInfo withIdleTo = new SessionSearchInfo();
    withIdleTo.setIdleTimeTo("1");
    Assertions.assertFalse(withIdleTo.isEmpty());

    SessionSearchInfo withLastIp = new SessionSearchInfo();
    withLastIp.setLastIp("127.0.0.1");
    Assertions.assertFalse(withLastIp.isEmpty());

    SessionSearchInfo invalidSession = new SessionSearchInfo();
    invalidSession.setSessionId("(");
    Assertions.assertFalse(invalidSession.isValid());

    SessionSearchInfo invalidAttr = new SessionSearchInfo();
    invalidAttr.setAttrName("(");
    Assertions.assertFalse(invalidAttr.isValid());

    SessionSearchInfo invalidAgeFrom = new SessionSearchInfo();
    invalidAgeFrom.setAgeFrom("x");
    Assertions.assertFalse(invalidAgeFrom.isValid());

    SessionSearchInfo invalidAgeTo = new SessionSearchInfo();
    invalidAgeTo.setAgeTo("x");
    Assertions.assertFalse(invalidAgeTo.isValid());

    SessionSearchInfo invalidIdleFrom = new SessionSearchInfo();
    invalidIdleFrom.setIdleTimeFrom("x");
    Assertions.assertFalse(invalidIdleFrom.isValid());

    SessionSearchInfo invalidIdleTo = new SessionSearchInfo();
    invalidIdleTo.setIdleTimeTo("x");
    Assertions.assertFalse(invalidIdleTo.isValid());

    SessionSearchInfo info = new SessionSearchInfo();
    info.setSearchAction(SessionSearchInfo.ACTION_APPLY);
    info.setAttrName("user");
    Assertions.assertTrue(info.isUseAttr());
    info.setAttrName(null);
    info.setSessionId(null);
    Assertions.assertFalse(info.isUseAttr());
  }

}
