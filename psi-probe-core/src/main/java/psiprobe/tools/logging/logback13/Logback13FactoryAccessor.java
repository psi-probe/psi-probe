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
package psiprobe.tools.logging.logback13;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;

import psiprobe.tools.logging.DefaultAccessor;

/**
 * Wraps a Logback logger factory from a given web application class loader.
 *
 * <p>
 * All Logback classes are loaded via the given class loader and not via psi-probe's own class
 * loader. For this reasons, all methods on Logback objects are invoked via reflection.
 * </p>
 * <p>
 * This way, we can even handle different versions of Logback embedded in different WARs.
 * </p>
 */
public class Logback13FactoryAccessor extends DefaultAccessor {

  /**
   * Attempts to initialize a Logback logger factory via the given class loader.
   *
   * @param cl the ClassLoader to use when fetching the factory
   *
   * @throws ClassNotFoundException the class not found exception
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   * @throws NoSuchMethodException the no such method exception
   * @throws SecurityException the security exception
   * @throws IllegalArgumentException the illegal argument exception
   */
  public Logback13FactoryAccessor(ClassLoader cl)
      throws ClassNotFoundException, IllegalAccessException, InvocationTargetException,
      NoSuchMethodException, SecurityException, IllegalArgumentException {

    // Get the SLF4J provider binding, which may or may not be Logback, depending on the binding.
    final List<?> providers = findServiceProviders(cl);
    if (providers.isEmpty()) {
      throw new RuntimeException("The SLF4J provider binding was not Logback");
    }

    // Get the service provider
    Object provider = providers.get(0);

    // Initialize the service provider
    Method initialize = MethodUtils.getAccessibleMethod(provider.getClass(), "initialize");
    initialize.invoke(provider);

    // Call the logger factory
    Method getLoggerFactory =
        MethodUtils.getAccessibleMethod(provider.getClass(), "getLoggerFactory");
    Object loggerFactory = getLoggerFactory.invoke(provider);

    // Check if the binding is indeed Logback
    Class<?> loggerFactoryClass = cl.loadClass("ch.qos.logback.classic.LoggerContext");
    if (!loggerFactoryClass.isInstance(loggerFactory)) {
      throw new RuntimeException("The SLF4J provider binding was not Logback");
    }
    setTarget(loggerFactory);
  }

  /**
   * Returns the Logback root logger.
   *
   * @return the root logger
   */
  public Logback13LoggerAccessor getRootLogger() {
    // Logback has no dedicated getRootLogger() method, so we simply access the root logger
    // by its well-defined name.
    return getLogger("ROOT");
  }

  /**
   * Returns the Logback logger with a given name.
   *
   * @param name the name
   *
   * @return the Logger with the given name
   */
  public Logback13LoggerAccessor getLogger(String name) {
    try {
      Class<? extends Object> clazz = getTarget().getClass();
      Method getLogger = MethodUtils.getAccessibleMethod(clazz, "getLogger", String.class);

      Object logger = getLogger.invoke(getTarget(), name);
      if (logger == null) {
        throw new NullPointerException(getTarget() + ".getLogger(\"" + name + "\") returned null");
      }
      Logback13LoggerAccessor accessor = new Logback13LoggerAccessor();
      accessor.setTarget(logger);
      accessor.setApplication(getApplication());
      return accessor;

    } catch (Exception e) {
      logger.error("{}.getLogger('{}') failed", getTarget(), name, e);
    }
    return null;
  }

  /**
   * Returns a list of wrappers for all Logback appenders that have an associated logger.
   *
   * @return a list of {@link Logback13AppenderAccessor}s representing all appenders that are in use
   */
  @SuppressWarnings("unchecked")
  public List<Logback13AppenderAccessor> getAppenders() {
    List<Logback13AppenderAccessor> appenders = new ArrayList<>();
    try {
      Class<? extends Object> clazz = getTarget().getClass();
      Method getLoggerList = MethodUtils.getAccessibleMethod(clazz, "getLoggerList");

      List<Object> loggers = (List<Object>) getLoggerList.invoke(getTarget());
      for (Object logger : loggers) {
        Logback13LoggerAccessor accessor = new Logback13LoggerAccessor();
        accessor.setTarget(logger);
        accessor.setApplication(getApplication());

        appenders.addAll(accessor.getAppenders());
      }
    } catch (Exception e) {
      logger.error("{}.getLoggerList() failed", getTarget(), e);
    }
    return appenders;
  }

  /**
   * Find service providers.
   *
   * @param cl the cl
   *
   * @return the list
   *
   * @throws NoSuchMethodException the no such method exception
   * @throws SecurityException the security exception
   * @throws ClassNotFoundException the class not found exception
   * @throws IllegalAccessException the illegal access exception
   * @throws IllegalArgumentException the illegal argument exception
   * @throws InvocationTargetException the invocation target exception
   */
  private static List<?> findServiceProviders(final ClassLoader cl)
      throws NoSuchMethodException, SecurityException, ClassNotFoundException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    final Class<?> loggerFactory = cl.loadClass("org.slf4j.LoggerFactory");
    final Method findServiceProviders = loggerFactory.getDeclaredMethod("findServiceProviders");
    // Make package protected accessible
    findServiceProviders.setAccessible(true);
    final List<?> providers = (List<?>) findServiceProviders.invoke(null);
    findServiceProviders.setAccessible(false);
    return providers;
  }

}
