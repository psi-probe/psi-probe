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
package psiprobe.tools.logging.jdk;

import com.google.common.base.Strings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;

import psiprobe.tools.logging.DefaultAccessor;
import psiprobe.tools.logging.LogDestination;

/**
 * The Class Jdk14LoggerAccessor.
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
    List<LogDestination> handlerAccessors = new ArrayList<>();
    try {
      Object[] handlers = (Object[]) MethodUtils.invokeMethod(getTarget(), "getHandlers");
      for (int h = 0; h < handlers.length; h++) {
        Object handler = handlers[h];
        Jdk14HandlerAccessor handlerAccessor = wrapHandler(handler, h);
        if (handlerAccessor != null) {
          handlerAccessors.add(handlerAccessor);
        }
      }
    } catch (Exception e) {
      logger.error("{}#handlers inaccessible", getTarget().getClass().getName(), e);
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
    return Strings.isNullOrEmpty(getName()) || isJuliRoot();
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
   *
   * @return the handler
   */
  public Jdk14HandlerAccessor getHandler(String logIndex) {
    int index = 0;
    try {
      index = Integer.parseInt(logIndex);
    } catch (Exception e) {
      logger.info("Could not parse integer from: {}.  Assuming 0.", logIndex);
      logger.trace("", e);
    }
    return getHandler(index);
  }

  /**
   * Gets the handler.
   *
   * @param index the index
   *
   * @return the handler
   */
  public Jdk14HandlerAccessor getHandler(int index) {
    try {
      Object[] handlers = (Object[]) MethodUtils.invokeMethod(getTarget(), "getHandlers");
      return wrapHandler(handlers[index], index);
    } catch (Exception e) {
      logger.error("{}#handlers inaccessible", getTarget().getClass().getName(), e);
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
        target = MethodUtils.invokeMethod(target, "getParent");
      }
      if (level == null && isJuliRoot()) {
        return "INFO";
      }
      return (String) MethodUtils.invokeMethod(level, "getName");
    } catch (Exception e) {
      logger.error("{}#getLevel() failed", getTarget().getClass().getName(), e);
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
      Class<?> levelClass =
          getTarget().getClass().getClassLoader().loadClass("java.util.logging.Level");
      Method parse = MethodUtils.getAccessibleMethod(levelClass, "parse", String.class);
      Object newLevel = parse.invoke(null, newLevelStr);
      MethodUtils.invokeMethod(getTarget(), "setLevel", newLevel);
    } catch (Exception e) {
      logger.error("{}#setLevel('{}') failed", getTarget().getClass().getName(), newLevelStr, e);
    }
  }

  /**
   * Gets the level internal.
   *
   * @param target the target
   *
   * @return the level internal
   *
   * @throws Exception the exception
   */
  private Object getLevelInternal(Object target) throws Exception {
    return MethodUtils.invokeMethod(target, "getLevel");
  }

  /**
   * Wrap handler.
   *
   * @param handler the handler
   * @param index the index
   *
   * @return the jdk14 handler accessor
   */
  private Jdk14HandlerAccessor wrapHandler(Object handler, int index) {
    try {
      if (handler == null) {
        throw new IllegalArgumentException("handler is null");
      }
      Jdk14HandlerAccessor handlerAccessor = null;
      String className = handler.getClass().getName();
      if ("org.apache.juli.FileHandler".equals(className)) {
        handlerAccessor = new JuliHandlerAccessor();
      } else if ("java.util.logging.ConsoleHandler".equals(className)) {
        handlerAccessor = new Jdk14HandlerAccessor();
      } else if ("java.util.logging.FileHandler".equals(className)) {
        handlerAccessor = new Jdk14FileHandlerAccessor();
      }

      if (handlerAccessor != null) {
        handlerAccessor.setLoggerAccessor(this);
        handlerAccessor.setTarget(handler);
        handlerAccessor.setIndex(Integer.toString(index));
        handlerAccessor.setApplication(getApplication());
      }
      return handlerAccessor;
    } catch (Exception e) {
      logger.error("Could not wrap handler: '{}'", handler, e);
    }
    return null;
  }

}
