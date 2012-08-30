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

import com.googlecode.psiprobe.beans.ResourceResolver;
import com.googlecode.psiprobe.model.Application;
import com.googlecode.psiprobe.model.ApplicationParam;
import com.googlecode.psiprobe.model.ApplicationResource;
import com.googlecode.psiprobe.model.ApplicationSession;
import com.googlecode.psiprobe.model.Attribute;
import com.googlecode.psiprobe.model.FilterInfo;
import com.googlecode.psiprobe.model.ServletInfo;
import com.googlecode.psiprobe.model.ServletMapping;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import net.sf.javainetlocator.InetAddressLocator;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Session;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardWrapper;
import org.apache.catalina.deploy.ApplicationParameter;
import org.apache.catalina.deploy.FilterDef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;

/**
 * @author Vlad Ilyushchenko
 * @author Andy Shapoval
 */
public class ApplicationUtils {

    private static Log logger = LogFactory.getLog(ApplicationUtils.class);

    public static Application getApplication(Context context) {
        return getApplication(context, null, false);
    }

    /**
     * Creates Application instance from Tomcat Context object. If ResourceResolver is passed the method will also collect additional information
     * about the application such as session count, session attribute count, application attribute count, servlet count, servlet stats summary and
     * datasource usage summary. Collecting additional information can be CPU intensive and time consuming so this should be avoided unless absolutely
     * required. Some datasource implementations (c3p0) are known to be prone to internal deadlocks, so this method can also hang is datasource usage
     * stats is to be collected.
     *
     * @param context
     * @param resourceResolver
     * @param calcSize
     * @return Application object
     */
    public static Application getApplication(Context context, ResourceResolver resourceResolver, boolean calcSize) {

        logger.debug("Querying webapp: " + context.getName());

        Application app = new Application();
        app.setName(context.getName().length() > 0 ? context.getName() : "/");
        app.setDocBase(context.getDocBase());
        app.setDisplayName(context.getDisplayName());
        app.setAvailable(context.getAvailable());
        app.setDistributable(context.getDistributable());
        app.setSessionTimeout(context.getSessionTimeout());
        app.setServletVersion(context.getServletContext().getMajorVersion() + "." + context.getServletContext().getMinorVersion());

        if (resourceResolver != null) {
            logger.debug("counting servlet attributes");

            int ctxAttrCount = 0;
            for (Enumeration e = context.getServletContext().getAttributeNames(); e.hasMoreElements(); e.nextElement()) {
                ctxAttrCount++;
            }
            app.setContextAttributeCount(ctxAttrCount);

            if (app.isAvailable()) {
                logger.debug("collecting session information");

                app.setSessionCount(context.getManager().findSessions().length);

                boolean serializable = true;
                int sessionAttributeCount = 0;
                long size = 0;

                Session[] sessions = context.getManager().findSessions();
                for (int i = 0; i < sessions.length; i++) {
                    ApplicationSession appSession = getApplicationSession(sessions[i], calcSize, false);
                    if (appSession != null) {
                        sessionAttributeCount += appSession.getObjectCount();
                        serializable = serializable && appSession.isSerializable();
                        size += appSession.getSize();
                    }
                }
                app.setSerializable(serializable);
                app.setSessionAttributeCount(sessionAttributeCount);
                app.setSize(size);
            }

            logger.debug("aggregating servlet stats");

            collectApplicationServletStats(context, app);

            if (resourceResolver.supportsPrivateResources() && app.isAvailable()) {
                int[] scores = getApplicationDataSourceUsageScores(context, resourceResolver);
                app.setDataSourceBusyScore(scores[0]);
                app.setDataSourceEstablishedScore(scores[1]);
            }
        }

        return app;
    }

    /**
     * Calculates Sum of requestCount, errorCount and processingTime for all servlets for the
     * give application. It also works out minimum value of minTime and maximum value for maxTime for
     * all servlets.
     *
     * @param context
     * @param app
     */

    public static void collectApplicationServletStats(Context context, Application app) {
        int svltCount = 0;
        int reqCount = 0;
        int errCount = 0;
        long procTime = 0;
        long minTime = Long.MAX_VALUE;
        long maxTime = 0;

        Container[] cns = context.findChildren();
        for (int i = 0; i < cns.length; i++) {
            if (cns[i] instanceof StandardWrapper) {
                StandardWrapper sw = (StandardWrapper) cns[i];
                svltCount++;
                reqCount += sw.getRequestCount();
                errCount += sw.getErrorCount();
                procTime += sw.getProcessingTime();
                if (sw.getRequestCount() > 0) {
                    minTime = Math.min(minTime, sw.getMinTime());
                }
                maxTime = Math.max(maxTime, sw.getMaxTime());
            }
        }
        app.setServletCount(svltCount);
        app.setRequestCount(reqCount);
        app.setErrorCount(errCount);
        app.setProcessingTime(procTime);
        app.setMinTime(minTime == Long.MAX_VALUE ? 0 : minTime);
        app.setMaxTime(maxTime);
    }

    public static int[] getApplicationDataSourceUsageScores(Context context, ResourceResolver resolver) {
        logger.debug("Calculating datasource usage score");

        int[] scores = new int[] {0, 0};
        List appResources;
        try {
            appResources = resolver.getApplicationResources(context);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        for (Iterator it = appResources.iterator(); it.hasNext();) {
            ApplicationResource appResource = (ApplicationResource) it.next();
            if (appResource.getDataSourceInfo() != null) {
                scores[0] = Math.max(scores[0], appResource.getDataSourceInfo().getBusyScore());
                scores[1] = Math.max(scores[1], appResource.getDataSourceInfo().getEstablishedScore());
            }
        }
        return scores;
    }

    public static ApplicationSession getApplicationSession(Session session, boolean calcSize, boolean addAttributes) {
        ApplicationSession sbean = null;
        if (session != null && session.isValid()) {
            sbean = new ApplicationSession();

            sbean.setId(session.getId());
            sbean.setCreationTime(new Date(session.getCreationTime()));
            sbean.setLastAccessTime(new Date(session.getLastAccessedTime()));
            sbean.setMaxIdleTime(session.getMaxInactiveInterval() * 1000);
            sbean.setManagerType(session.getManager().getClass().getName());
            sbean.setInfo(session.getInfo());

            boolean sessionSerializable = true;
            int attributeCount = 0;
            long size = 0;

            HttpSession httpSession = session.getSession();
            Set processedObjects = new HashSet(1000);
            try {
                for (Enumeration e = httpSession.getAttributeNames(); e.hasMoreElements();) {
                    String name = (String) e.nextElement();
                    Object o = httpSession.getAttribute(name);
                    sessionSerializable = sessionSerializable && o instanceof Serializable;

                    long oSize = 0;
                    if (calcSize) {
                        try {
                            oSize += Instruments.sizeOf(name, processedObjects);
                            oSize += Instruments.sizeOf(o, processedObjects);
                        } catch (Throwable th) {
                            logger.error("Cannot estimate size of attribute \"" + name + "\"", th);
                            //
                            // make sure we always re-throw ThreadDeath
                            //
                            if (e instanceof ThreadDeath) {
                                throw (ThreadDeath) e;
                            }
                        }
                    }

                    if (addAttributes) {
                        Attribute saBean = new Attribute();
                        saBean.setName(name);
                        saBean.setType(ClassUtils.getQualifiedName(o.getClass()));
                        saBean.setValue(o);
                        saBean.setSize(oSize);
                        saBean.setSerializable(o instanceof Serializable);
                        sbean.addAttribute(saBean);
                    }
                    attributeCount++;
                    size += oSize;
                }
                String lastAccessedIP = (String) httpSession.getAttribute(ApplicationSession.LAST_ACCESSED_BY_IP);
                if (lastAccessedIP != null) {
                    sbean.setLastAccessedIP(lastAccessedIP);
                }
                try {
                    sbean.setLastAccessedIPLocale(InetAddressLocator.getLocale(InetAddress.getByName(lastAccessedIP).getAddress()));
                } catch (Throwable e) {
                    logger.error("Cannot determine Locale of "+lastAccessedIP);
                    //
                    // make sure we always re-throw ThreadDeath
                    //
                    if (e instanceof ThreadDeath) {
                        throw (ThreadDeath) e;
                    }
                }


            } catch (IllegalStateException e) {
                logger.info("Session appears to be invalidated, ignore");
            }

            sbean.setObjectCount(attributeCount);
            sbean.setSize(size);
            sbean.setSerializable(sessionSerializable);
        }

        return sbean;
    }

    public static List getApplicationAttributes(Context context) {
        List attrs = new ArrayList();
        ServletContext servletCtx = context.getServletContext();
        for (Enumeration e = servletCtx.getAttributeNames(); e.hasMoreElements(); ) {
            String attrName = (String) e.nextElement();
            Object attrValue = servletCtx.getAttribute(attrName);

            Attribute attr = new Attribute();
            attr.setName(attrName);
            attr.setValue(attrValue);
            attr.setType(ClassUtils.getQualifiedName(attrValue.getClass()));
            attrs.add(attr);
        }
        return attrs;
    }

    public static List getApplicationInitParams(Context context) {
        // We'll try to determine if a parameter value comes from a deployment descriptor or a context descriptor.
        // assumption: Context.findParameter() returns only values of parameters that are declared in a deployment descriptor.
        // If a parameter is declared in a context descriptor with override=false and redeclared in a deployment descriptor,
        // Context.findParameter() still returns its value, even though the value is taken from a context descriptor.
        // context.findApplicationParameters() returns all parameters that are declared in a context descriptor regardless
        // of whether they are overridden in a deployment descriptor or not or not.

        // creating a set of parameter names that are declared in a context descriptor
        // and can not be ovevridden in a deployment descriptor.
        Set nonOverridableParams = new HashSet();
        ApplicationParameter[] appParams = context.findApplicationParameters();
        for (int i = 0; i < appParams.length; i++) {
            if (appParams[i] != null && ! appParams[i].getOverride()) {
                nonOverridableParams.add(appParams[i].getName());
            }
        }

        List initParams = new ArrayList();
        ServletContext servletCtx = context.getServletContext();
        for (Enumeration e = servletCtx.getInitParameterNames(); e.hasMoreElements(); ) {
            String paramName = (String) e.nextElement();

            ApplicationParam param = new ApplicationParam();
            param.setName(paramName);
            param.setValue(servletCtx.getInitParameter(paramName));
            // if the parameter is declared in a deployment descriptor
            // and it is not declared in a context descriptor with override=false,
            // the value comes from the deployment descriptor
            param.setFromDeplDescr(context.findParameter(paramName) != null && ! nonOverridableParams.contains(paramName));
            initParams.add(param);
        }

        return initParams;
    }

    public static ServletInfo getApplicationServlet(Context context, String servletName) {
        Container c = context.findChild(servletName);

        if (c instanceof Wrapper) {
            Wrapper w = (Wrapper) c;
            return getServletInfo(w, context.getName());
        } else {
            return null;
        }
    }

    private static ServletInfo getServletInfo(Wrapper w, String contextName) {
        ServletInfo si = new ServletInfo();
        si.setApplicationName(contextName.length() > 0 ? contextName : "/");
        si.setServletName(w.getName());
        si.setServletClass(w.getServletClass());
        si.setAvailable(! w.isUnavailable());
        si.setLoadOnStartup(w.getLoadOnStartup());
        si.setRunAs(w.getRunAs());
        String[] ms = w.findMappings();
        for (int i = 0; i < ms.length; i++) {
            si.getMappings().add(ms[i]);
        }
        if (w instanceof StandardWrapper) {
            StandardWrapper sw = (StandardWrapper) w;
            si.setAllocationCount(sw.getCountAllocated());
            si.setErrorCount(sw.getErrorCount());
            si.setLoadTime(sw.getLoadTime());
            si.setMaxInstances(sw.getMaxInstances());
            si.setMaxTime(sw.getMaxTime());
            si.setMinTime(sw.getMinTime() == Long.MAX_VALUE ? 0 : sw.getMinTime());
            si.setProcessingTime(sw.getProcessingTime());
            si.setRequestCount(sw.getRequestCount());
            si.setSingleThreaded(sw.isSingleThreadModel());
        }
        return si;
    }

    public static List getApplicationServlets(Context context) {
        Container[] cns = context.findChildren();
        List servlets = new ArrayList(cns.length);
        for (int i = 0; i < cns.length; i++) {
            if (cns[i] instanceof Wrapper) {
                Wrapper w = (Wrapper) cns[i];
                servlets.add(getServletInfo(w, context.getName()));
            }
        }
        return servlets;
    }

    public static List getApplicationServletMaps(Context context) {
        String[] sms = context.findServletMappings();
        List servletMaps = new ArrayList(sms.length);
        for(int i = 0; i < sms.length; i++) {
            if (sms[i] != null) {
                String sn = context.findServletMapping(sms[i]);
                if (sn != null) {
                    ServletMapping sm = new ServletMapping();
                    sm.setApplicationName(context.getName().length() > 0 ? context.getName() : "/");
                    sm.setUrl(sms[i]);
                    sm.setServletName(sn);
                    Container c = context.findChild(sn);
                    if (c instanceof Wrapper) {
                        Wrapper w = (Wrapper) c;
                        sm.setServletClass(w.getServletClass());
                        sm.setAvailable(! w.isUnavailable());
                    }
                    servletMaps.add(sm);
                }
            }
        }
        return servletMaps;
    }

    public static FilterInfo getApplicationFilter(Context context, String filterName) {
        FilterDef fd = context.findFilterDef(filterName);
        if (fd != null) {
            return getFilterInfo(fd);
        } else {
            return null;
        }
    }

    private static FilterInfo getFilterInfo(FilterDef fd) {
        FilterInfo fi = new FilterInfo();
        fi.setFilterName(fd.getFilterName());
        fi.setFilterClass(fd.getFilterClass());
        fi.setFilterDesc(fd.getDescription());
        return fi;
    }

    public static List getApplicationFilters(Context context) {
        FilterDef[] fds = context.findFilterDefs();
        List filterDefs = new ArrayList(fds.length);
        for(int i = 0; i < fds.length; i++) {
            if (fds[i] != null) {
                FilterInfo fi = getFilterInfo(fds[i]);
                filterDefs.add(fi);
            }
        }
        return filterDefs;
    }

}
