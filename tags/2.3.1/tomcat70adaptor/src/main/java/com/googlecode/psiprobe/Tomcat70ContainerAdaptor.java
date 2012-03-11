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
package com.googlecode.psiprobe;

import com.googlecode.psiprobe.model.FilterMapping;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;
import org.apache.catalina.deploy.FilterMap;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.modeler.Registry;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.Options;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.apache.jasper.servlet.JspServletWrapper;

/**
 *
 * @author Mark Lewis
 */
public class Tomcat70ContainerAdaptor extends AbstractTomcatContainer {

    private Host host;
    private ObjectName deployerOName;
    private MBeanServer mBeanServer;
    private Valve valve = new Tomcat70AgentValve();

    public void setWrapper(Wrapper wrapper) {
        if (wrapper != null) {
            host = (Host) wrapper.getParent().getParent();
            try {
                deployerOName = new ObjectName(host.getParent().getName() + ":type=Deployer,host=" + host.getName());
            } catch (MalformedObjectNameException e) {
                // do nothing here
            }
            host.getPipeline().addValve(valve);
            mBeanServer = Registry.getRegistry(null, null).getMBeanServer();
        } else if (host != null) {
            host.getPipeline().removeValve(valve);
        }
    }

    public boolean canBoundTo(String binding) {
        return binding != null && (binding.startsWith("Apache Tomcat/7.0")
                || binding.startsWith("JBoss Web/3.0")
                || binding.startsWith("JBoss Web/7.0"));
    }

    protected Context findContextInternal(String name) {
        return (Context) host.findChild(name);
    }

    public List findContexts() {
        Container[] containers = host.findChildren();
        return Arrays.asList(containers);
    }

    public void stop(String name) throws Exception {
        Context ctx = findContext(name);
        if (ctx != null) {
            ((Lifecycle) ctx).stop();
        }
    }

    public void start(String name) throws Exception {
        Context ctx = findContext(name);
        if (ctx != null) {
            ((Lifecycle) ctx).start();
        }
    }

    private void checkChanges(String name) throws Exception {
        Boolean result = (Boolean) mBeanServer.invoke(deployerOName,
                        "isServiced", new String[]{name}, new String[]{"java.lang.String"});
        if (!result.booleanValue()) {
            mBeanServer.invoke(deployerOName, "addServiced",
                    new String[]{name}, new String[]{"java.lang.String"});
            try {
                mBeanServer.invoke(deployerOName, "check",
                        new String[]{name}, new String[]{"java.lang.String"});
            } finally {
                mBeanServer.invoke(deployerOName, "removeServiced",
                        new String[]{name}, new String[]{"java.lang.String"});
            }
        }
    }

    public void removeInternal(String name) throws Exception {
        checkChanges(name);
    }

    public void installWar(String name, URL url) throws Exception {
        checkChanges(name);
    }

    public void installContextInternal(String name, File config) throws Exception {
        checkChanges(name);
    }

    public File getAppBase() {
        File base = new File(host.getAppBase());
        if (! base.isAbsolute()) {
            base = new File(System.getProperty("catalina.base"), host.getAppBase());
        }
        return base;
    }

    public String getConfigBase() {
        return getConfigBase(host);
    }

    public Object getLogger(Context context) {
        return context.getLogger();
    }

    public String getHostName() {
        return host.getName();
    }

    public String getName() {
        return host.getParent().getName();
    }

    @Override
    protected List getFilterMappings(FilterMap fmap, String dm, String filterClass) {
        String[] urls = fmap.getURLPatterns();
        String[] servlets = fmap.getServletNames();
        List filterMappings = new ArrayList(urls.length + servlets.length);
        for (int i = 0; i < urls.length; i++) {
            FilterMapping fm = new FilterMapping();
            fm.setUrl(urls[i]);
            fm.setFilterName(fmap.getFilterName());
            fm.setDispatcherMap(dm);
            fm.setFilterClass(filterClass);
            filterMappings.add(fm);
        }
        for (int i = 0; i < servlets.length; i++) {
            FilterMapping fm = new FilterMapping();
            fm.setServletName(servlets[i]);
            fm.setFilterName(fmap.getFilterName());
            fm.setDispatcherMap(dm);
            fm.setFilterClass(filterClass);
            filterMappings.add(fm);
        }
        return filterMappings;
    }

    @Override
    public File getConfigFile(Context ctx) {
        URL configUrl = ctx.getConfigFile();
        if (configUrl != null) {
            try {
                URI configUri = configUrl.toURI();
                if ("file".equals(configUri.getScheme())) {
                    return new File(configUri.getPath());
                }
            } catch (Exception ex) {
                logger.error("Could not convert URL to URI: " + configUrl, ex);
            }
        }
        return null;
    }

    @Override
    protected JspCompilationContext createJspCompilationContext(String name, boolean isErrPage, Options opt, ServletContext sctx, JspRuntimeContext jrctx, ClassLoader cl) {
        JspCompilationContext jcctx;
        try {
            jcctx = new JspCompilationContext(name, opt, sctx, null, jrctx);
        } catch (NoSuchMethodError err) {
            /*
             * The above constructor's signature changed in Tomcat 7.0.16:
             * http://svn.apache.org/viewvc?view=revision&revision=1124719
             * 
             * If we reach this point, we are running on a prior version of
             * Tomcat 7 and must use reflection to create this object.
             */
            try {
                jcctx = (JspCompilationContext) ConstructorUtils.invokeConstructor(
                        JspCompilationContext.class,
                        new Object[] {name, false, opt, sctx, null, jrctx},
                        new Class[] {
                            String.class,
                            Boolean.TYPE,
                            Options.class,
                            ServletContext.class,
                            JspServletWrapper.class,
                            JspRuntimeContext.class
                        });
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (cl != null) {
            jcctx.setClassLoader(cl);
        }
        return jcctx;
    }

}
