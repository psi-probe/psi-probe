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

package com.googlecode.psiprobe.tools.logging.jdk;

import com.googlecode.psiprobe.tools.logging.DefaultAccessor;
import com.googlecode.psiprobe.tools.logging.LogDestination;

import org.apache.commons.beanutils.MethodUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class Jdk14LoggerAccessor.
 *
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class Jdk14LoggerAccessor extends DefaultAccessor {

  /** The context. */
  private boolean context;

  /**
   * Gets the handlers.
   *
   * @return the handlers
   */
  public List<LogDestination> getHandlers() {
    List<LogDestination> handlerAccessors = new ArrayList<LogDestination>();
    try {
      Object[] handlers = (Object[]) MethodUtils.invokeMethod(getTarget(), "getHandlers", null);
      for (int h = 0; h < handlers.length; h++) {
        Object handler = handlers[h];
        Jdk14HandlerAccessor handlerAccessor = wrapHandler(handler, h);
        if (handlerAccessor != null) {
          handlerAccessors.add(handlerAccessor);
        }
      }
    } catch (Exception e) {
      log.error(getTarget().getClass().getName() + "#handlers inaccessible", e);
    }
    return handlerAccessors;
  }

  /**
   * Checks if is context.
   *
   * @return true, if is context
   */
  public boolean isContext() {
    return context;
  }

  /**
   * Sets the context.
   *
   * @param context the new context
   */
  public void setContext(boolean context) {
    this.context = context;
  }

  /**
   * Checks if is root.
   *
   * @return true, if is root
   */
  public boolean isRoot() {
    return "".equals(getName()) || isJuliRoot();
  }

  /**
   * Checks if is juli root.
   *
   * @return true, if is juli root
   */
  public boolean isJuliRoot() {
    return "org.apache.juli.ClassLoaderLogManager$RootLogger".equals(getTargetClass());
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return (String) getProperty(getTarget(), "name", null);
  }

  /**
   * Gets the handler.
   *
   * @param logIndex the log index
   * @return the handler
   */
  public Jdk14HandlerAccessor getHandler(String logIndex) {
    int index = 0;
    try {
      index = Integer.parseInt(logIndex);
    } catch (Exception e) {
      log.info("Could not parse integer from: " + logIndex + ".  Assuming 0.");
    }
    return getHandler(index);
  }

  /**
   * Gets the handler.
   *
   * @param index the index
   * @return the handler
   */
  public Jdk14HandlerAccessor getHandler(int index) {
    try {
      Object[] handlers = (Object[]) MethodUtils.invokeMethod(getTarget(), "getHandlers", null);
      return wrapHandler(handlers[index], index);
    } catch (Exception e) {
      log.error(getTarget().getClass().getName() + "#handlers inaccessible", e);
    }
    return null;
  }

  /**
   * Gets the level.
   *
   * @return the level
   */
  public String getLevel() {
    try {
      Object level = null;
      Object target = getTarget();
      while (level == null && target != null) {
        level = getLevelInternal(target);
        target = MethodUtils.invokeMethod(target, "getParent", null);
      }
      if (level == null && isJuliRoot()) {
        return "INFO";
      }
      return (String) MethodUtils.invokeMethod(level, "getName", null);
    } catch (Exception e) {
      log.error(getTarget().getClass().getName() + "#getLevel() failed", e);
    }
    return null;
  }

  /**
   * Sets the level.
   *
   * @param newLevelStr the new level
   */
  public void setLevel(String newLevelStr) {
    try {
      Class levelClass =
          getTarget().getClass().getClassLoader().loadClass("java.util.logging.Level");
      Method parse = MethodUtils.getAccessibleMethod(levelClass, "parse", String.class);
      Object newLevel = parse.invoke(null, new Object[] {newLevelStr});
      MethodUtils.invokeMethod(getTarget(), "setLevel", newLevel);
    } catch (Exception e) {
      log.error(getTarget().getClass().getName() + "#setLevel(\"" + newLevelStr + "\") failed", e);
    }
  }

  /**
   * Gets the level internal.
   *
   * @param target the target
   * @return the level internal
   * @throws Exception the exception
   */
  private Object getLevelInternal(Object target) throws Exception {
    return MethodUtils.invokeMethod(target, "getLevel", null);
  }

  /**
   * Wrap handler.
   *
   * @param handler the handler
   * @param index the index
   * @return the jdk14 handler accessor
   */
  private Jdk14HandlerAccessor wrapHandler(Object handler, int index) {
    try {
      if (handler == null) {
        throw new IllegalArgumentException("handler is null");
      }
      Jdk14HandlerAccessor handlerAccessor = null;
      if ("org.apache.juli.FileHandler".equals(handler.getClass().getName())) {
        handlerAccessor = new JuliHandlerAccessor();
      } else if ("java.util.logging.ConsoleHandler".equals(handler.getClass().getName())) {
        handlerAccessor = new Jdk14HandlerAccessor();
      }

      if (handlerAccessor != null) {
        handlerAccessor.setLoggerAccessor(this);
        handlerAccessor.setTarget(handler);
        handlerAccessor.setIndex(Integer.toString(index));
        handlerAccessor.setApplication(getApplication());
      }
      return handlerAccessor;
    } catch (Exception e) {
      log.error("Could not wrap handler: " + handler, e);
    }
    return null;
  }

}
