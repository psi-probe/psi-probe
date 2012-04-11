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
package com.googlecode.psiprobe.beans;

import com.googlecode.psiprobe.model.Application;
import com.googlecode.psiprobe.model.DisconnectedLogDestination;
import com.googlecode.psiprobe.tools.ApplicationUtils;
import com.googlecode.psiprobe.tools.Instruments;
import com.googlecode.psiprobe.tools.logging.DefaultAccessor;
import com.googlecode.psiprobe.tools.logging.FileLogAccessor;
import com.googlecode.psiprobe.tools.logging.LogDestination;
import com.googlecode.psiprobe.tools.logging.catalina.CatalinaLoggerAccessor;
import com.googlecode.psiprobe.tools.logging.commons.CommonsLoggerAccessor;
import com.googlecode.psiprobe.tools.logging.jdk.Jdk14LoggerAccessor;
import com.googlecode.psiprobe.tools.logging.jdk.Jdk14ManagerAccessor;
import com.googlecode.psiprobe.tools.logging.log4j.Log4JLoggerAccessor;
import com.googlecode.psiprobe.tools.logging.log4j.Log4JManagerAccessor;
import com.googlecode.psiprobe.tools.logging.logback.LogbackLoggerAccessor;
import com.googlecode.psiprobe.tools.logging.logback.LogbackFactoryAccessor;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.catalina.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;

/**
 *
 * @author Mark Lewis
 */
public class LogResolverBean {

    protected final Log logger = LogFactory.getLog(getClass());

    private ContainerWrapperBean containerWrapper;
    private List stdoutFiles = new ArrayList();

    public ContainerWrapperBean getContainerWrapper() {
        return containerWrapper;
    }

    public void setContainerWrapper(ContainerWrapperBean containerWrapper) {
        this.containerWrapper = containerWrapper;
    }

    public List getStdoutFiles() {
        return stdoutFiles;
    }

    public void setStdoutFiles(List stdoutFiles) {
        this.stdoutFiles = stdoutFiles;
    }

    public List getLogDestinations(boolean all) {
        List allAppenders = getAllLogDestinations();

        if (allAppenders != null) {
            //
            // this list has to guarantee the order in which elements are added
            //
            List uniqueList = new LinkedList();
            Comparator cmp = new LogDestinationComparator(all);

            Collections.sort(allAppenders, cmp);
            for (int i = 0; i < allAppenders.size(); i++) {
                LogDestination dest = (LogDestination) allAppenders.get(i);
                if (Collections.binarySearch(uniqueList, dest, cmp) < 0) {
                    if (all || dest.getFile() == null || dest.getFile().exists()) {
                        uniqueList.add(new DisconnectedLogDestination(dest));
                    }
                }
            }
            return uniqueList;
        }
        return null;
    }

    public List getLogSources(File logFile) {
        List filtered = new LinkedList();
        List sources = getLogSources();
        for (int i = 0; i < sources.size(); i++) {
            LogDestination dest = (LogDestination) sources.get(i);
            if (logFile.equals(dest.getFile())) {
                filtered.add(dest);
            }
        }
        return filtered;
    }

    public List getLogSources() {
        List sources = new LinkedList();

        List allAppenders = getAllLogDestinations();
        if (allAppenders != null) {
            Comparator cmp = new LogSourceComparator();

            Collections.sort(allAppenders, cmp);
            for (int i = 0; i < allAppenders.size(); i++) {
                LogDestination dest = (LogDestination) allAppenders.get(i);
                if (Collections.binarySearch(sources, dest, cmp) < 0) {
                    sources.add(new DisconnectedLogDestination(dest));
                }
            }
        }
        return sources;
    }

    private List getAllLogDestinations() {
        if (Instruments.isInitialized()) {
            List allAppenders = new ArrayList();

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
            // interrogate webapp classloaders and avilable loggers
            //
            List contexts = getContainerWrapper().getTomcatContainer().findContexts();
            for (int i = 0; i < contexts.size(); i++) {
                Context ctx = (Context) contexts.get(i);
                interrogateContext(ctx, allAppenders);
            }

            return allAppenders;
        }
        return null;
    }

    public LogDestination getLogDestination(String logType, String webapp, boolean context, boolean root, String logName, String logIndex) {
        Context ctx = null;
        Application application = null;
        if (webapp != null) {
            ctx = getContainerWrapper().getTomcatContainer().findContext(webapp);
            if (ctx != null) {
                application = ApplicationUtils.getApplication(ctx);
            }
        }

        if ("stdout".equals(logType) && logName != null) {
            return getStdoutLogDestination(logName);
        } else if ("catalina".equals(logType) && ctx != null) {
            return getCatalinaLogDestination(ctx, application);
        } else if (logIndex != null
                && ("jdk".equals(logType)
                || "log4j".equals(logType)
                || "logback".equals(logType))) {
            if (context && ctx != null) {
                return getCommonsLogDestination(ctx, application, logIndex);
            }
            ClassLoader cl;
            ClassLoader prevCl = null;
            if (ctx != null) {
                cl = ctx.getLoader().getClassLoader();
                prevCl = ClassUtils.overrideThreadContextClassLoader(cl);
            } else {
                cl = Thread.currentThread().getContextClassLoader().getParent();
            }
            try {
                if ((root || logName != null) && logIndex != null) {
                    if ("jdk".equals(logType)) {
                        return getJdk14LogDestination(cl, application, root, logName, logIndex);
                    } else if ("log4j".equals(logType)) {
                        return getLog4JLogDestination(cl, application, root, logName, logIndex);
                    } else if ("logback".equals(logType)) {
                        return getLogbackLogDestination(cl, application, root, logName, logIndex);
                    }
                }
            } finally {
                if (prevCl != null) {
                    ClassUtils.overrideThreadContextClassLoader(prevCl);
                }
            }
        }
        return null;
    }

    private void interrogateContext(Context ctx, List allAppenders) {
        Application application = ApplicationUtils.getApplication(ctx);
        ClassLoader cl = ctx.getLoader().getClassLoader();

        try {
            Object contextLogger = getContainerWrapper().getTomcatContainer().getLogger(ctx);
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
            }
        } catch (Throwable e) {
            logger.error("Could not interrogate context logger for " + ctx.getName() + ". Enable debug logging to see the trace stack");
            logger.debug(e);
            //
            // make sure we always re-throw ThreadDeath
            //
            if (e instanceof ThreadDeath) {
                throw (ThreadDeath) e;
            }
        }

        if (application.isAvailable()) {
            ClassLoader prevCl = ClassUtils.overrideThreadContextClassLoader(cl);
            try {
                interrogateClassLoader(cl, application, allAppenders);
            } catch (Exception e) {
                logger.error("Could not interrogate classloader loggers for " + ctx.getName() + ". Enable debug logging to see the trace stack");
                logger.debug(e);
            } finally {
                if (prevCl != null) {
                    ClassUtils.overrideThreadContextClassLoader(prevCl);
                }
            }
        }
    }

    private void interrogateClassLoader(ClassLoader cl, Application application, List appenders) {
        String applicationName = (application != null ? "application \"" + application.getName() + "\"" : "server");
        
        //check for JDK loggers
        try {
            Jdk14ManagerAccessor jdk14accessor = new Jdk14ManagerAccessor(cl);
            jdk14accessor.setApplication(application);
            appenders.addAll(jdk14accessor.getHandlers());
        } catch (Throwable t) {
            //
            // make sure we always re-throw ThreadDeath
            //
            if (t instanceof ThreadDeath) {
                throw (ThreadDeath) t;
            }
            logger.debug("Could not resolve JDK loggers for " + applicationName, t);
        }

        // check for Log4J loggers
        try {
            Log4JManagerAccessor log4JAccessor = new Log4JManagerAccessor(cl);
            log4JAccessor.setApplication(application);
            appenders.addAll(log4JAccessor.getAppenders());
        } catch (Throwable t) {
            //
            // make sure we always re-throw ThreadDeath
            //
            if (t instanceof ThreadDeath) {
                throw (ThreadDeath) t;
            }
            logger.debug("Could not resolve JDK loggers for " + applicationName, t);
        }

        // check for Logback loggers
        try {
            LogbackFactoryAccessor logbackAccessor = new LogbackFactoryAccessor(cl);
            logbackAccessor.setApplication(application);
            appenders.addAll(logbackAccessor.getAppenders());
        } catch (Throwable t) {
            //
            // make sure we always re-throw ThreadDeath
            //
            if (t instanceof ThreadDeath) {
                throw (ThreadDeath) t;
            }
            logger.debug("Could not resolve JDK loggers for " + applicationName, t);
        }
    }

    private void interrogateStdOutFiles(List appenders) {
        for (Iterator it = stdoutFiles.iterator(); it.hasNext(); ) {
            String fileName = (String) it.next();
            FileLogAccessor fla = resolveStdoutLogDestination(fileName);
            if (fla != null) {
                appenders.add(fla);
            }
        }
    }

    private LogDestination getStdoutLogDestination(String logName) {
        for (Iterator it = stdoutFiles.iterator(); it.hasNext(); ) {
            String fileName = (String) it.next();
            if (fileName.equals(logName)) {
                FileLogAccessor fla = resolveStdoutLogDestination(fileName);
                if (fla != null) {
                    return fla;
                }
            }
        }
        return null;
    }

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

    private LogDestination getCatalinaLogDestination(Context ctx, Application application) {
        Object log = getContainerWrapper().getTomcatContainer().getLogger(ctx);
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

    private LogDestination getCommonsLogDestination(Context ctx, Application application, String logIndex) {
        Object contextLogger = getContainerWrapper().getTomcatContainer().getLogger(ctx);
        CommonsLoggerAccessor commonsAccessor = new CommonsLoggerAccessor();
        commonsAccessor.setTarget(contextLogger);
        commonsAccessor.setApplication(application);
        return commonsAccessor.getDestination(logIndex);
    }

    private LogDestination getJdk14LogDestination(ClassLoader cl, Application application, boolean root, String logName, String handlerIndex) {
        try {
            Jdk14ManagerAccessor manager = new Jdk14ManagerAccessor(cl);
            manager.setApplication(application);
            Jdk14LoggerAccessor log = (root ? manager.getRootLogger() : manager.getLogger(logName));
            if (log != null) {
                return log.getHandler(handlerIndex);
            }
        } catch (Throwable t) {
            //always re-throw ThreadDeath when catching Throwable
            if (t instanceof ThreadDeath) {
                throw (ThreadDeath) t;
            }
            logger.debug("getJdk14LogDestination failed", t);
        }
        return null;
    }

    private LogDestination getLog4JLogDestination(ClassLoader cl, Application application, boolean root, String logName, String appenderName) {
        try {
            Log4JManagerAccessor manager = new Log4JManagerAccessor(cl);
            manager.setApplication(application);
            Log4JLoggerAccessor log = (root ? manager.getRootLogger() : manager.getLogger(logName));
            if (log != null) {
                return log.getAppender(appenderName);
            }
        } catch (Throwable t) {
            //always re-throw ThreadDeath when catching Throwable
            if (t instanceof ThreadDeath) {
                throw (ThreadDeath) t;
            }
            logger.debug("getLog4JLogDestination failed", t);
        }
        return null;
    }

    private LogDestination getLogbackLogDestination(ClassLoader cl, Application application, boolean root, String logName, String appenderName) {
        try {
            LogbackFactoryAccessor manager = new LogbackFactoryAccessor(cl);
            manager.setApplication(application);
            LogbackLoggerAccessor log = (root ? manager.getRootLogger() : manager.getLogger(logName));
            if (log != null) {
                return log.getAppender(appenderName);
            }
        } catch (Throwable t) {
            //always re-throw ThreadDeath when catching Throwable
            if (t instanceof ThreadDeath) {
                throw (ThreadDeath) t;
            }
            logger.debug("getLogbackLogDestination failed", t);
        }
        return null;
    }

    private static class LogDestinationComparator implements Comparator {

        private boolean all;

        public LogDestinationComparator(boolean all) {
            this.all = all;
        }

        public int compare(Object o1, Object o2) {
            LogDestination d1 = (LogDestination) o1;
            LogDestination d2 = (LogDestination) o2;
            File f1 = d1.getFile();
            File f2 = d2.getFile();
            String name1 = (f1 == null ? "" : f1.getAbsolutePath());
            String name2 = (f2 == null ? "" : f2.getAbsolutePath());
            if (all) {
                Application a1 = d1.getApplication();
                Application a2 = d2.getApplication();
            if (a1 == null || a2 == null) {
                a1 = null;
                a2 = null;
            }
                String appName1 = (a1 == null ? "" : a1.getName());
                String appName2 = (a2 == null ? "" : a2.getName());
                String context1 = (d1.isContext() ? "is" : "not");
                String context2 = (d2.isContext() ? "is" : "not");
                String root1 = (d1.isRoot() ? "is" : "not");
                String root2 = (d2.isRoot() ? "is" : "not");
                String logType1 = d1.getLogType();
                String logType2 = d1.getLogType();
                char delim = '!';
                name1 = appName1 + delim + context1 + delim + root1 + delim + logType1 + delim + name1;
                name2 = appName2 + delim + context2 + delim + root2 + delim + logType2 + delim + name2;
            }
            return name1.compareTo(name2);
        }
    }

    private static class LogSourceComparator implements Comparator {

        private static final char DELIM = '!';

        public int compare(Object o1, Object o2) {
            if (o1 instanceof DefaultAccessor && o2 instanceof DefaultAccessor) {
                DefaultAccessor da1 = (DefaultAccessor) o1;
                DefaultAccessor da2 = (DefaultAccessor) o2;
                if (da1.getTarget() == da2.getTarget()) {
                    return 0;
                }
            }
            LogDestination d1 = (LogDestination) o1;
            LogDestination d2 = (LogDestination) o2;
            boolean eitherAppIsNull = (d1.getApplication() == null || d2.getApplication() == null);
            String name1 = convertToString(d1, eitherAppIsNull);
            String name2 = convertToString(d2, eitherAppIsNull);
            return name1.compareTo(name2);
        }

        private String convertToString(LogDestination dest, boolean eitherAppIsNull) {
            File file = dest.getFile();
            String fileName = (file == null ? "" : file.getAbsolutePath());
            Application app = dest.getApplication();
            if (eitherAppIsNull) {
                app = null;
            }
            String appName = (app == null ? "" : app.getName());
            String logType = dest.getLogType();
            String context = (dest.isContext() ? "is" : "not");
            String root = (dest.isRoot() ? "is" : "not");
            String logName = dest.getName();
            //String logIndex1 = d1.getIndex();
            return appName + DELIM + logType + DELIM + context + DELIM + root + DELIM + logName + DELIM + fileName;
        }

    }

}
