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
package psiprobe.controllers.threads;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import psiprobe.model.ThreadStackElement;
import psiprobe.tools.JmxTools;

/**
 * The Class ThreadStackController.
 */
@Controller
public class ThreadStackController extends ParameterizableViewController {

  /** The stack element count. */
  private int stackElementCount = 20;

  /**
   * Gets the stack element count.
   *
   * @return the stack element count
   */
  public int getStackElementCount() {
    return stackElementCount;
  }

  /**
   * Sets the stack element count.
   *
   * @param stackElementCount the new stack element count
   */
  @Value("100")
  public void setStackElementCount(int stackElementCount) {
    this.stackElementCount = stackElementCount;
  }

  @RequestMapping(path = "/app/threadstack.ajax")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletRequestBindingException, MalformedObjectNameException {

    long threadId = ServletRequestUtils.getLongParameter(request, "id", -1);
    String threadName = ServletRequestUtils.getStringParameter(request, "name");

    MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
    ObjectName objectNameThreading = new ObjectName("java.lang:type=Threading");

    if (threadId == -1 && threadName != null) {
      // find thread by name
      for (long id : (long[]) JmxTools.getAttribute(mbeanServer, objectNameThreading,
          "AllThreadIds")) {
        CompositeData cd = (CompositeData) JmxTools.invoke(mbeanServer, objectNameThreading,
            "getThreadInfo", new Object[] {id}, new String[] {"long"});
        String name = JmxTools.getStringAttr(cd, "threadName");
        if (threadName.equals(name)) {
          threadId = id;
          break;
        }
      }
    }

    List<ThreadStackElement> stack = null;
    if (mbeanServer.queryMBeans(objectNameThreading, null) != null && threadId != -1) {

      CompositeData cd =
          (CompositeData) JmxTools.invoke(mbeanServer, objectNameThreading, "getThreadInfo",
              new Object[] {threadId, stackElementCount}, new String[] {"long", "int"});
      if (cd != null) {
        CompositeData[] elements = (CompositeData[]) cd.get("stackTrace");
        threadName = JmxTools.getStringAttr(cd, "threadName");

        stack = new ArrayList<>(elements.length);

        for (CompositeData cd2 : elements) {
          ThreadStackElement tse = new ThreadStackElement();
          tse.setClassName(JmxTools.getStringAttr(cd2, "className"));
          tse.setFileName(JmxTools.getStringAttr(cd2, "fileName"));
          tse.setMethodName(JmxTools.getStringAttr(cd2, "methodName"));
          tse.setLineNumber(JmxTools.getIntAttr(cd2, "lineNumber", -1));
          tse.setNativeMethod(JmxTools.getBooleanAttr(cd2, "nativeMethod"));
          stack.add(tse);
        }
      }
    }

    return new ModelAndView(getViewName(), "stack", stack).addObject("threadName", threadName);
  }

  @Value("ajax/ThreadStack")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
