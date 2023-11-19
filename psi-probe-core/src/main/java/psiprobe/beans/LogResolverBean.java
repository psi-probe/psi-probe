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
package psiprobe.beans;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.catalina.Context;
import org.apache.catalina.Loader;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;

import psiprobe.model.Application;
import psiprobe.model.DisconnectedLogDestination;
import psiprobe.tools.ApplicationUtils;
import psiprobe.tools.Instruments;
import psiprobe.tools.logging.FileLogAccessor;
import psiprobe.tools.logging.LogDestination;
import psiprobe.tools.logging.catalina.CatalinaLoggerAccessor;
import psiprobe.tools.logging.commons.CommonsLoggerAccessor;
import psiprobe.tools.logging.jdk.Jdk14LoggerAccessor;
import psiprobe.tools.logging.jdk.Jdk14ManagerAccessor;
import psiprobe.tools.logging.log4j.Log4JLoggerAccessor;
import psiprobe.tools.logging.log4j.Log4JManagerAccessor;
import psiprobe.tools.logging.log4j2.Log4J2AppenderAccessor;
import psiprobe.tools.logging.log4j2.Log4J2LoggerConfigAccessor;
import psiprobe.tools.logging.log4j2.Log4J2LoggerContextAccessor;
import psiprobe.tools.logging.log4j2.Log4J2WebLoggerContextUtilsAccessor;
import psiprobe.tools.logging.logback.LogbackFactoryAccessor;
import psiprobe.tools.logging.logback.LogbackLoggerAccessor;
import psiprobe.tools.logging.logback13.Logback13FactoryAccessor;
import psiprobe.tools.logging.logback13.Logback13LoggerAccessor;
import psiprobe.tools.logging.slf4jlogback.TomcatSlf4jLogbackFactoryAccessor;
import psiprobe.tools.logging.slf4jlogback.TomcatSlf4jLogbackLoggerAccessor;
import psiprobe.tools.logging.slf4jlogback13.TomcatSlf4jLogback13FactoryAccessor;
import psiprobe.tools.logging.slf4jlogback13.TomcatSlf4jLogback13LoggerAccessor;

/**
 * The Class LogResolverBean.
 */
public class LogResolverBean {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(LogResolverBean.class);

  /** The container wrapper. */
  @Inject
  private ContainerWrapperBean containerWrapper;

  /** The stdout files. */
  private List<String> stdoutFiles = new ArrayList<>();

  /**
   * Gets the container wrapper.
   *
   * @return the container wrapper
   */
  public ContainerWrapperBean getContainerWrapper() {
    return containerWrapper;
  }

  /**
   * Sets the container wrapper.
   *
   * @param containerWrapper the new container wrapper
   */
  public void setContainerWrapper(ContainerWrapperBean containerWrapper) {
    this.containerWrapper = containerWrapper;
  }

  /**
   * Gets the stdout files.
   *
   * @return the stdout files
   */
  public List<String> getStdoutFiles() {
    return stdoutFiles;
  }

  /**
   * Sets the stdout files.
   *
   * @param stdoutFiles the new stdout files
   */
  @Autowired
  public void setStdoutFiles(List<String> stdoutFiles) {
    logger.info("stdoutFiles {}", stdoutFiles);
    this.stdoutFiles = stdoutFiles;
  }

  /**
   * Gets the log destinations.
   *
   * @param all the all
   *
   * @return the log destinations
   */
  public List<LogDestination> getLogDestinations(boolean all) {
    List<LogDestination> allAppenders = getAllLogDestinations();

    if (allAppenders.isEmpty()) {
      return Collections.emptyList();
    }

    //
    // this list has to guarantee the order in which elements are added
    //
    List<LogDestination> uniqueList = new LinkedList<>();
    AbstractLogComparator cmp = new LogDestinationComparator(all);

    Collections.sort(allAppenders, cmp);
    for (LogDestination dest : allAppenders) {
      if (Collections.binarySearch(uniqueList, dest, cmp) < 0
          && (all || dest.getFile() == null || dest.getFile().exists())) {
        uniqueList.add(new DisconnectedLogDestination().builder(dest));
      }
    }
    return uniqueList;
  }

  /**
   * Gets the log sources.
   *
   * @param logFile the log file
   *
   * @return the log sources
   */
  public List<LogDestination> getLogSources(File logFile) {
    List<LogDestination> filtered = new LinkedList<>();
    List<LogDestination> sources = getLogSources();
    for (LogDestination dest : sources) {
      if (logFile.equals(dest.getFile())) {
        filtered.add(dest);
      }
    }
    return filtered;
  }

  /**
   * Gets the log sources.
   *
   * @return the log sources
   */
  public List<LogDestination> getLogSources() {
    List<LogDestination> sources = new LinkedList<>();

    List<LogDestination> allAppenders = getAllLogDestinations();
    if (!allAppenders.isEmpty()) {
      AbstractLogComparator cmp = new LogSourceComparator();

      Collections.sort(allAppenders, cmp);
      for (LogDestination dest : allAppenders) {
        if (Collections.binarySearch(sources, dest, cmp) < 0) {
          sources.add(new DisconnectedLogDestination().builder(dest));
        }
      }
    }
    return sources;
  }

  /**
   * Gets the all log destinations.
   *
   * @return the all log destinations
   */
  private List<LogDestination> getAllLogDestinations() {
    if (!Instruments.isInitialized()) {
      return Collections.emptyList();
    }

    List<LogDestination> allAppenders = new ArrayList<>();

    //
    // interrogate classloader hierarchy
    //
    ClassLoader cl2 = Thread.currentThread().getContextClassLoader().getParent();
    while (cl2 != null) {
      interrogateClassLoader(cl2, null, allAppenders);
      cl2 = cl2.getParent();
    }

    //
    // check for known stdout files, such as "catalina.out"
    //
    interrogateStdOutFiles(allAppenders);

    //
    // interrogate webapp classloaders and available loggers
    //
    List<Context> contexts = getContainerWrapper().getTomcatContainer().findContexts();
    for (Context ctx : contexts) {
      interrogateContext(ctx, allAppenders);
    }

    return allAppenders;
  }

  /**
   * Gets the log destination.
   *
   * @param logType the log type
   * @param webapp the webapp
   * @param context the context
   * @param root the root
   * @param logName the log name
   * @param logIndex the log index
   *
   * @return the log destination
   */
  public LogDestination getLogDestination(String logType, String webapp, boolean context,
      boolean root, String logName, String logIndex) {

    LogDestination result = null;
    Context ctx = null;
    Application application = null;
    if (webapp != null) {
      ctx = getContainerWrapper().getTomcatContainer().findContext(webapp);
      if (ctx != null) {
        application = ApplicationUtils.getApplication(ctx, getContainerWrapper());
      }
    }

    // Supported loggers
    List<String> loggers = new ArrayList<>();
    loggers.add("jdk");
    loggers.add("log4j");
    loggers.add("log4j2");
    loggers.add("logback");
    loggers.add("logback13");
    loggers.add("tomcatSlf4jLogback");
    loggers.add("tomcatSlf4jLogback13");

    if (logName != null && "stdout".equals(logType)) {
      result = getStdoutLogDestination(logName);
    } else if (ctx != null && "catalina".equals(logType)) {
      result = getCatalinaLogDestination(ctx, application);
    } else if (logIndex != null && loggers.contains(logType)) {
      if (context && ctx != null && !"log4j2".equals(logType)) {
        result = getCommonsLogDestination(ctx, application, logIndex);
      } else if (ctx != null && "log4j2".equals(logType)) {
        result = getLog4J2LogDestination(ctx, application, root, logName, logIndex);
      } else {
        ClassLoader cl;
        ClassLoader prevCl = null;
        if (ctx != null) {
          cl = ctx.getLoader().getClassLoader();
          prevCl = ClassUtils.overrideThreadContextClassLoader(cl);
        } else {
          cl = Thread.currentThread().getContextClassLoader().getParent();
        }
        try {
          if (root || logName != null) {
            if ("jdk".equals(logType)) {
              result = getJdk14LogDestination(cl, application, root, logName, logIndex);
            } else if ("log4j".equals(logType)) {
              result = getLog4JLogDestination(cl, application, root, logName, logIndex);
            } else if ("logback".equals(logType)) {
              result = getLogbackLogDestination(cl, application, root, logName, logIndex);
            } else if ("logback13".equals(logType)) {
              result = getLogback13LogDestination(cl, application, root, logName, logIndex);
            } else if ("tomcatSlf4jLogback".equals(logType)) {
              result = getLogbackTomcatJuliLogDestination(cl, application, root, logName, logIndex);
            } else if ("tomcatSlf4jLogback13".equals(logType)) {
              result =
                  getLogback13TomcatJuliLogDestination(cl, application, root, logName, logIndex);
            }
          }
        } finally {
          if (prevCl != null) {
            ClassUtils.overrideThreadContextClassLoader(prevCl);
          }
        }
      }
    }
    return result;
  }

  /**
   * Interrogate context.
   *
   * @param ctx the ctx
   * @param allAppenders the all appenders
   */
  private void interrogateContext(Context ctx, List<LogDestination> allAppenders) {
    Application application = ApplicationUtils.getApplication(ctx, getContainerWrapper());
    ClassLoader cl = ctx.getLoader().getClassLoader();
    Object contextLogger = ctx.getLogger();
    if (contextLogger != null) {
      if (contextLogger.getClass().getName().startsWith("org.apache.commons.logging")) {
        CommonsLoggerAccessor commonsAccessor = new CommonsLoggerAccessor();
        commonsAccessor.setTarget(contextLogger);
        commonsAccessor.setApplication(application);
        allAppenders.addAll(commonsAccessor.getDestinations());
      } else if (contextLogger.getClass().getName().startsWith("org.apache.catalina.logger")) {
        CatalinaLoggerAccessor catalinaAccessor = new CatalinaLoggerAccessor();
        catalinaAccessor.setApplication(application);
        catalinaAccessor.setTarget(contextLogger);
        allAppenders.add(catalinaAccessor);
      }

      ServletContext servletContext = ctx.getServletContext();
      try {
        Log4J2LoggerContextAccessor loggerContextAccessor = null;
        try {
          Log4J2WebLoggerContextUtilsAccessor webLoggerContextUtilsAccessor =
              new Log4J2WebLoggerContextUtilsAccessor(cl);
          loggerContextAccessor = webLoggerContextUtilsAccessor.getWebLoggerContext(servletContext);
        } catch (Exception e) {
          logger.debug("Log4J2LoggerContextAccessor instantiation failed", e);
        }
        List<Object> loggerContexts = getLoggerContexts(cl);
        for (Object loggerContext : loggerContexts) {
          Map<String, Object> loggerConfigs = getLoggerConfigs(loggerContext);
          for (Object loggerConfig : loggerConfigs.values()) {
            Log4J2LoggerConfigAccessor logConfigAccessor = new Log4J2LoggerConfigAccessor();
            logConfigAccessor.setTarget(loggerConfig);
            logConfigAccessor.setApplication(application);
            logConfigAccessor.setContext(true);
            logConfigAccessor.setLoggerContext(loggerContextAccessor);
            Method getAppenders =
                MethodUtils.getAccessibleMethod(loggerConfig.getClass(), "getAppenders");
            @SuppressWarnings("unchecked")
            Map<String, Object> appenders = (Map<String, Object>) getAppenders.invoke(loggerConfig);
            for (Object appender : appenders.values()) {
              Log4J2AppenderAccessor appenderAccessor = new Log4J2AppenderAccessor();
              appenderAccessor.setTarget(appender);
              appenderAccessor.setLoggerAccessor(logConfigAccessor);
              appenderAccessor.setApplication(application);
              allAppenders.add(appenderAccessor);
            }
          }
        }
      } catch (Exception e) {
        logger.debug("getting appenders failed", e);
      }
    }

    if (application.isAvailable()) {
      ClassLoader prevCl = ClassUtils.overrideThreadContextClassLoader(cl);
      try {
        interrogateClassLoader(cl, application, allAppenders);
      } catch (Exception e) {
        logger.error(
            "Could not interrogate classloader loggers for {}. Enable debug logging to see the trace stack",
            ctx.getName());
        logger.debug("", e);
      } finally {
        if (prevCl != null) {
          ClassUtils.overrideThreadContextClassLoader(prevCl);
        }
      }
    }
  }

  /**
   * Interrogate class loader.
   *
   * @param cl the cl
   * @param application the application
   * @param appenders the appenders
   */
  private void interrogateClassLoader(ClassLoader cl, Application application,
      List<LogDestination> appenders) {

    String applicationName =
        application != null ? "application \"" + application.getName() + "\"" : "server";

    // check for JDK loggers
    try {
      Jdk14ManagerAccessor jdk14accessor = new Jdk14ManagerAccessor(cl);
      jdk14accessor.setApplication(application);
      appenders.addAll(jdk14accessor.getHandlers());
    } catch (Exception e) {
      logger.debug("Could not resolve JDK loggers for '{}'", applicationName, e);
    }

    // check for Log4J loggers
    try {
      Log4JManagerAccessor log4JAccessor = new Log4JManagerAccessor(cl);
      log4JAccessor.setApplication(application);
      appenders.addAll(log4JAccessor.getAppenders());
    } catch (Exception e) {
      logger.debug("Could not resolve log4j loggers for '{}'", applicationName, e);
    }

    // check for Logback loggers
    try {
      LogbackFactoryAccessor logbackAccessor = new LogbackFactoryAccessor(cl);
      logbackAccessor.setApplication(application);
      appenders.addAll(logbackAccessor.getAppenders());
    } catch (Exception e) {
      logger.debug("Could not resolve logback loggers for '{}'", applicationName, e);
    }

    // check for Logback 1.3 loggers
    try {
      Logback13FactoryAccessor logback13Accessor = new Logback13FactoryAccessor(cl);
      logback13Accessor.setApplication(application);
      appenders.addAll(logback13Accessor.getAppenders());
    } catch (Exception e) {
      logger.debug("Could not resolve logback 1.3 loggers for '{}'", applicationName, e);
    }

    // check for tomcat-slf4j-logback loggers
    try {
      TomcatSlf4jLogbackFactoryAccessor tomcatSlf4jLogbackAccessor =
          new TomcatSlf4jLogbackFactoryAccessor(cl);
      tomcatSlf4jLogbackAccessor.setApplication(application);
      appenders.addAll(tomcatSlf4jLogbackAccessor.getAppenders());
    } catch (Exception e) {
      logger.debug("Could not resolve tomcat-slf4j-logback loggers for '{}'", applicationName, e);
    }

    // check for tomcat-slf4j-logback 1.3 loggers
    try {
      TomcatSlf4jLogback13FactoryAccessor tomcatSlf4jLogback13Accessor =
          new TomcatSlf4jLogback13FactoryAccessor(cl);
      tomcatSlf4jLogback13Accessor.setApplication(application);
      appenders.addAll(tomcatSlf4jLogback13Accessor.getAppenders());
    } catch (Exception e) {
      logger.debug("Could not resolve tomcat-slf4j-logback 1.3 loggers for '{}'", applicationName,
          e);
    }
  }

  /**
   * Interrogate std out files.
   *
   * @param appenders the appenders
   */
  private void interrogateStdOutFiles(List<LogDestination> appenders) {
    for (String fileName : stdoutFiles) {
      FileLogAccessor fla = resolveStdoutLogDestination(fileName);
      if (fla != null) {
        appenders.add(fla);
      }
    }
  }

  /**
   * Gets the stdout log destination.
   *
   * @param logName the log name
   *
   * @return the stdout log destination
   */
  private LogDestination getStdoutLogDestination(String logName) {
    for (String fileName : stdoutFiles) {
      if (fileName.equals(logName)) {
        FileLogAccessor fla = resolveStdoutLogDestination(fileName);
        if (fla != null) {
          return fla;
        }
      }
    }
    return null;
  }

  /**
   * Resolve stdout log destination.
   *
   * @param fileName the file name
   *
   * @return the file log accessor
   */
  private FileLogAccessor resolveStdoutLogDestination(String fileName) {
    File stdout = new File(System.getProperty("catalina.base"), "logs/" + fileName);
    if (stdout.exists()) {
      FileLogAccessor fla = new FileLogAccessor();
      fla.setName(fileName);
      fla.setFile(stdout);
      return fla;
    }
    return null;
  }

  /**
   * Gets the catalina log destination.
   *
   * @param ctx the ctx
   * @param application the application
   *
   * @return the catalina log destination
   */
  private LogDestination getCatalinaLogDestination(Context ctx, Application application) {
    Object log = ctx.getLogger();
    if (log != null) {
      CatalinaLoggerAccessor logAccessor = new CatalinaLoggerAccessor();
      logAccessor.setTarget(log);
      logAccessor.setApplication(application);
      if (logAccessor.getFile().exists()) {
        return logAccessor;
      }
    }
    return null;
  }

  /**
   * Gets the commons log destination.
   *
   * @param ctx the ctx
   * @param application the application
   * @param logIndex the log index
   *
   * @return the commons log destination
   */
  private LogDestination getCommonsLogDestination(Context ctx, Application application,
      String logIndex) {
    Object contextLogger = ctx.getLogger();
    CommonsLoggerAccessor commonsAccessor = new CommonsLoggerAccessor();
    commonsAccessor.setTarget(contextLogger);
    commonsAccessor.setApplication(application);
    return commonsAccessor.getDestination(logIndex);
  }

  /**
   * Gets the jdk14 log destination.
   *
   * @param cl the cl
   * @param application the application
   * @param root the root
   * @param logName the log name
   * @param handlerIndex the handler index
   *
   * @return the jdk14 log destination
   */
  private LogDestination getJdk14LogDestination(ClassLoader cl, Application application,
      boolean root, String logName, String handlerIndex) {

    try {
      Jdk14ManagerAccessor manager = new Jdk14ManagerAccessor(cl);
      manager.setApplication(application);
      Jdk14LoggerAccessor log = root ? manager.getRootLogger() : manager.getLogger(logName);
      if (log != null) {
        return log.getHandler(handlerIndex);
      }
    } catch (Exception e) {
      logger.debug("getJdk14LogDestination failed", e);
    }
    return null;
  }

  /**
   * Gets the log4j log destination.
   *
   * @param cl the cl
   * @param application the application
   * @param root the root
   * @param logName the log name
   * @param appenderName the appender name
   *
   * @return the log4j log destination
   */
  private LogDestination getLog4JLogDestination(ClassLoader cl, Application application,
      boolean root, String logName, String appenderName) {

    try {
      Log4JManagerAccessor manager = new Log4JManagerAccessor(cl);
      manager.setApplication(application);
      Log4JLoggerAccessor log = root ? manager.getRootLogger() : manager.getLogger(logName);
      if (log != null) {
        return log.getAppender(appenderName);
      }
    } catch (Exception e) {
      logger.debug("getLog4JLogDestination failed", e);
    }
    return null;
  }

  /**
   * Gets the log4j2 log destination.
   *
   * @param ctx the ctx
   * @param application the application
   * @param root the root
   * @param logName the log name
   * @param appenderName the appender name
   *
   * @return the log4j2 log destination
   */
  private LogDestination getLog4J2LogDestination(Context ctx, Application application, boolean root,
      String logName, String appenderName) {

    Log4J2AppenderAccessor result = null;
    try {
      Loader loader = ctx.getLoader();
      ClassLoader classLoader = loader.getClassLoader();
      Log4J2WebLoggerContextUtilsAccessor webLoggerContextUtilsAccessor =
          new Log4J2WebLoggerContextUtilsAccessor(classLoader);
      Log4J2LoggerContextAccessor loggerContextAccessor =
          webLoggerContextUtilsAccessor.getWebLoggerContext(ctx.getServletContext());
      List<Object> loggerContexts = getLoggerContexts(classLoader);
      Object loggerConfig = null;
      for (Object loggerContext : loggerContexts) {
        Map<String, Object> loggerConfigs = getLoggerConfigs(loggerContext);
        loggerConfig = loggerConfigs.get(root ? "" : logName);
        if (loggerConfig != null) {
          break;
        }
      }
      if (loggerConfig != null) {
        Log4J2LoggerConfigAccessor accessor = new Log4J2LoggerConfigAccessor();
        accessor.setTarget(loggerConfig);
        accessor.setApplication(application);
        accessor.setContext(true);
        accessor.setLoggerContext(loggerContextAccessor);
        result = accessor.getAppender(appenderName);
      }
    } catch (Exception e) {
      logger.debug("getLog4J2LogDestination failed", e);
    }
    logger.debug("getLog4J2LogDestination(): OUT: result={}", result);

    return result;
  }

  /**
   * Gets the logger configs.
   *
   * @param loggerContext the logger context
   *
   * @return the logger configs
   *
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   */
  private Map<String, Object> getLoggerConfigs(Object loggerContext)
      throws IllegalAccessException, InvocationTargetException {
    Method getConfiguration =
        MethodUtils.getAccessibleMethod(loggerContext.getClass(), "getConfiguration");
    Object configuration = getConfiguration.invoke(loggerContext);
    Method getLoggerConfigs =
        MethodUtils.getAccessibleMethod(configuration.getClass(), "getLoggers");
    return (Map<String, Object>) getLoggerConfigs.invoke(configuration);
  }

  /**
   * Gets the logger contexts.
   *
   * @param cl the cl
   *
   * @return the logger contexts
   *
   * @throws ClassNotFoundException the class not found exception
   * @throws InstantiationException the instantiation exception
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   * @throws IllegalArgumentException the illegal argument exception
   * @throws NoSuchMethodException the no such method exception
   * @throws SecurityException the security exception
   */
  private List<Object> getLoggerContexts(ClassLoader cl) throws ClassNotFoundException,
      InstantiationException, IllegalAccessException, InvocationTargetException,
      IllegalArgumentException, NoSuchMethodException, SecurityException {
    Class<?> clazz =
        cl.loadClass("org.apache.logging.log4j.core.selector.ClassLoaderContextSelector");
    Object classLoaderContextSelector = clazz.getDeclaredConstructor().newInstance();
    Method getLoggerContexts = MethodUtils.getAccessibleMethod(clazz, "getLoggerContexts");
    return (List<Object>) getLoggerContexts.invoke(classLoaderContextSelector);
  }

  /**
   * Gets the logback log destination.
   *
   * @param cl the cl
   * @param application the application
   * @param root the root
   * @param logName the log name
   * @param appenderName the appender name
   *
   * @return the logback log destination
   */
  private LogDestination getLogbackLogDestination(ClassLoader cl, Application application,
      boolean root, String logName, String appenderName) {

    try {
      LogbackFactoryAccessor manager = new LogbackFactoryAccessor(cl);
      manager.setApplication(application);
      LogbackLoggerAccessor log = root ? manager.getRootLogger() : manager.getLogger(logName);
      if (log != null) {
        return log.getAppender(appenderName);
      }
    } catch (Exception e) {
      logger.debug("getLogbackLogDestination failed", e);
    }
    return null;
  }

  /**
   * Gets the logback log destination.
   *
   * @param cl the cl
   * @param application the application
   * @param root the root
   * @param logName the log name
   * @param appenderName the appender name
   *
   * @return the logback log destination
   */
  private LogDestination getLogback13LogDestination(ClassLoader cl, Application application,
      boolean root, String logName, String appenderName) {

    try {
      Logback13FactoryAccessor manager = new Logback13FactoryAccessor(cl);
      manager.setApplication(application);
      Logback13LoggerAccessor log = root ? manager.getRootLogger() : manager.getLogger(logName);
      if (log != null) {
        return log.getAppender(appenderName);
      }
    } catch (Exception e) {
      logger.debug("getLogback13LogDestination failed", e);
    }
    return null;
  }

  /**
   * Gets the logback tomcat juli log destination.
   *
   * @param cl the cl
   * @param application the application
   * @param root the root
   * @param logName the log name
   * @param appenderName the appender name
   *
   * @return the logback tomcat juli log destination
   */
  private LogDestination getLogbackTomcatJuliLogDestination(ClassLoader cl, Application application,
      boolean root, String logName, String appenderName) {

    try {
      TomcatSlf4jLogbackFactoryAccessor manager = new TomcatSlf4jLogbackFactoryAccessor(cl);
      manager.setApplication(application);
      TomcatSlf4jLogbackLoggerAccessor log =
          root ? manager.getRootLogger() : manager.getLogger(logName);
      if (log != null) {
        return log.getAppender(appenderName);
      }
    } catch (Exception e) {
      logger.debug("getTomcatSlf4jLogbackLogDestination failed", e);
    }
    return null;
  }

  /**
   * Gets the logback tomcat juli log destination.
   *
   * @param cl the cl
   * @param application the application
   * @param root the root
   * @param logName the log name
   * @param appenderName the appender name
   *
   * @return the logback tomcat juli log destination
   */
  private LogDestination getLogback13TomcatJuliLogDestination(ClassLoader cl,
      Application application, boolean root, String logName, String appenderName) {

    try {
      TomcatSlf4jLogback13FactoryAccessor manager = new TomcatSlf4jLogback13FactoryAccessor(cl);
      manager.setApplication(application);
      TomcatSlf4jLogback13LoggerAccessor log =
          root ? manager.getRootLogger() : manager.getLogger(logName);
      if (log != null) {
        return log.getAppender(appenderName);
      }
    } catch (Exception e) {
      logger.debug("getTomcatSlf4jLogback13LogDestination failed", e);
    }
    return null;
  }

  /**
   * The Class AbstractLogComparator.
   */
  private abstract static class AbstractLogComparator
      implements Comparator<LogDestination>, Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant DELIM. */
    protected static final char DELIM = '!';

    @Override
    public final int compare(LogDestination o1, LogDestination o2) {
      String name1 = convertToString(o1);
      String name2 = convertToString(o2);
      return name1.compareTo(name2);
    }

    /**
     * Convert to string.
     *
     * @param d1 the d1
     *
     * @return the string
     */
    protected abstract String convertToString(LogDestination d1);

  }

  /**
   * The Class LogDestinationComparator.
   */
  private static class LogDestinationComparator extends AbstractLogComparator
      implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The all. */
    private final boolean all;

    /**
     * Instantiates a new log destination comparator.
     *
     * @param all the all
     */
    public LogDestinationComparator(boolean all) {
      this.all = all;
    }

    @Override
    protected String convertToString(LogDestination dest) {
      File file = dest.getFile();
      String fileName = file == null ? "" : file.getAbsolutePath();
      String name;
      if (all) {
        Application app = dest.getApplication();
        String appName = app == null ? Character.toString(DELIM) : app.getName();
        String context = dest.isContext() ? "is" : "not";
        String root = dest.isRoot() ? "is" : "not";
        String logType = dest.getLogType();
        name = appName + DELIM + context + DELIM + root + DELIM + logType + DELIM + fileName;
      } else {
        name = fileName;
      }
      return name;
    }

  }

  /**
   * The Class LogSourceComparator.
   */
  private static class LogSourceComparator extends AbstractLogComparator implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    @Override
    protected String convertToString(LogDestination dest) {
      File file = dest.getFile();
      String fileName = file == null ? "" : file.getAbsolutePath();
      Application app = dest.getApplication();
      String appName = app == null ? Character.toString(DELIM) : app.getName();
      String logType = dest.getLogType();
      String context = dest.isContext() ? "is" : "not";
      String root = dest.isRoot() ? "is" : "not";
      String logName = dest.getName();
      return appName + DELIM + logType + DELIM + context + DELIM + root + DELIM + logName + DELIM
          + fileName;
    }

  }

}
