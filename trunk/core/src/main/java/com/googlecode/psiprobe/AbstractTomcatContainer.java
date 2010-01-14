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

import com.googlecode.psiprobe.model.jsp.Item;
import com.googlecode.psiprobe.model.jsp.Summary;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.core.StandardContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jasper.EmbeddedServletOptions;
import org.apache.jasper.JasperException;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.Options;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.apache.naming.resources.ResourceAttributes;

/**
 * Abstration layer to implement some functionality, which is common between different container adaptors.
 * <p/>
 * Author: Vlad Ilyushchenko
 */
public abstract class AbstractTomcatContainer implements TomcatContainer {

    protected Log logger = LogFactory.getLog(getClass());

    public boolean installContext(String contextName) throws Exception {

        if (!contextName.startsWith("/")) {
            contextName = "/" + contextName;
        }

        File f = new File(getConfigBase(), contextName + ".xml");
        installContextInternal(contextName, f);
        return findContext(contextName) != null;
    }

    public void remove(String contextName) throws Exception {
        if (!contextName.startsWith("/")) {
            contextName = "/" + contextName;
        }
        Context ctx = findContext(contextName);

        //
        // special treatment for root context.
        //
        if (ctx == null && "/".equals(contextName)) {
            contextName = "";
            ctx = findContext(contextName);
        }

        if (ctx != null) {

            try {
                stop(contextName);
            } catch (Throwable e) {
                logger.info("Stopping " + contextName + " threw this exception:", e);
                //
                // make sure we always re-throw ThreadDeath
                //
                if (e instanceof ThreadDeath) {
                    throw (ThreadDeath) e;
                }
            }

            File appDir;
            File docBase = new File(ctx.getDocBase());

            if (!docBase.isAbsolute()) {
                appDir = new File(getAppBase(), ctx.getDocBase());
            } else {
                appDir = docBase;
            }

            logger.debug("Deleting " + appDir.getAbsolutePath());
            Utils.delete(appDir);

            String contextBase = "/".equals(contextName) || "".equals(contextName) ? null : contextName.substring("/".length());
            if (contextBase != null) {
                File warFile = new File(getAppBase(), contextBase + ".war");
                logger.debug("Deleting " + warFile.getAbsolutePath());
                Utils.delete(warFile);
            } else {
                logger.debug("Root application (/). Cannot assume a war file");
            }


            if (ctx.getConfigFile() != null) {
                File configFile = new File(ctx.getConfigFile());
                logger.debug("Deleting " + configFile.getAbsolutePath());
                Utils.delete(configFile);
            }

            removeInternal(contextName);
        }
    }

    public void discardWorkDir(Context context) {
        if (context instanceof StandardContext) {
            StandardContext standardContext = (StandardContext) context;
            logger.info("Discarding " + standardContext.getWorkPath());
            Utils.delete(new File(standardContext.getWorkPath(), "org"));
        } else {
            logger.error("context " + context.getName() + " is not an instance of " + context.getClass().getName() + ", expected StandardContext");
        }
    }

    public String getServletFileNameForJsp(Context context, String jspName) {
        String servletName = null;

        ServletConfig servletConfig = (ServletConfig) context.findChild("jsp");
        if (servletConfig != null) {
            ServletContext sctx = context.getServletContext();
            Options opt = new EmbeddedServletOptions(servletConfig, sctx);
            JspRuntimeContext jrctx = new JspRuntimeContext(sctx, opt);
            JspCompilationContext jcctx = new JspCompilationContext(jspName, false, opt, sctx, null, jrctx);
            servletName = jcctx.getServletJavaFileName();
        } else {
            logger.error("Context " + context.getName() + " does not have \"jsp\" servlet");
        }
        return servletName;
    }

    /**
     * Compiles a list of JSPs. Names of JSP files are expected to be relative to the webapp root. The method
     * updates summary with compilation details.
     *
     * @param context
     * @param summary
     * @param names
     */
    public void recompileJsps(Context context, Summary summary, List names) {
        ServletConfig servletConfig = (ServletConfig) context.findChild("jsp");
        if (servletConfig != null) {
            if (summary != null) {
                synchronized (servletConfig) {
                    ServletContext sctx = context.getServletContext();
                    Options opt = new EmbeddedServletOptions(servletConfig, sctx);

                    JspRuntimeContext jrctx = new JspRuntimeContext(sctx, opt);
                    try {
                        //
                        // we need to pass context classloader here, so the jsps can reference /WEB-INF/classes and
                        // /WEB-INF/lib. JspCompilationContext would only take URLClassLoader, so we fake it
                        //
                        URLClassLoader classLoader = new URLClassLoader(new URL[]{}, context.getLoader().getClassLoader());
                        for (Iterator it = names.iterator(); it.hasNext();) {
                            String name = (String) it.next();
                            long time = System.currentTimeMillis();
                            JspCompilationContext jcctx = new JspCompilationContext(name, false, opt, sctx, null, jrctx);
                            jcctx.setClassLoader(classLoader);
                            ClassLoader cl = Thread.currentThread().getContextClassLoader();
                            Thread.currentThread().setContextClassLoader(classLoader);
                            try {
                                Item item = (Item) summary.getItems().get(name);
                                if (item != null) {
                                    try {
                                        org.apache.jasper.compiler.Compiler c = jcctx.createCompiler();
                                        c.compile();
                                        item.setState(Item.STATE_READY);
                                        item.setException(null);
                                        logger.info("Compiled " + name + ": OK");
                                    } catch (Exception e) {
                                        item.setState(Item.STATE_FAILED);
                                        item.setException(e);
                                        logger.info("Compiled " + name + ": FAILED", e);
                                    }
                                    item.setCompileTime(System.currentTimeMillis() - time);
                                } else {
                                    logger.error(name + " is not on the summary list, ignored");
                                }
                            } finally {
                                Thread.currentThread().setContextClassLoader(cl);
                            }
                        }
                    } finally {
                        jrctx.destroy();
                    }
                }
            } else {
                logger.error("summary is null for " + context.getName() + ", request ignored");
            }
        } else {
            logger.error("Context " + context.getName() + " does not have \"jsp\" servlet");
        }
    }

    /**
     * Lists and optionally compiles all JSPs for the given context. Compilation details are added to the summary.
     *
     * @param context
     * @param summary
     * @param compile
     * @throws Exception
     */
    public void listContextJsps(Context context, Summary summary, boolean compile) throws Exception {
        ServletConfig servletConfig = (ServletConfig) context.findChild("jsp");
        if (servletConfig != null) {
            synchronized (servletConfig) {
                ServletContext sctx = context.getServletContext();
                Options opt = new EmbeddedServletOptions(servletConfig, sctx);

                JspRuntimeContext jrctx = new JspRuntimeContext(sctx, opt);
                try {
                    if (summary.getItems() == null) {
                        summary.setItems(new HashMap());
                    }

                    //
                    // mark all items as missing
                    //
                    for (Iterator it = summary.getItems().keySet().iterator(); it.hasNext();) {
                        Item item = (Item) summary.getItems().get(it.next());
                        item.setMissing(true);
                    }

                    //
                    // we need to pass context classloader here, so the jsps can reference /WEB-INF/classes and
                    // /WEB-INF/lib. JspCompilationContext would only take URLClassLoader, so we fake it
                    //
                    compileItem("/", opt, context, jrctx, summary,
                            new URLClassLoader(new URL[]{}, context.getLoader().getClassLoader()), 0, compile);
                } finally {
                    jrctx.destroy();
                }
            }

            //
            // delete "missing" items by keeping "not missing" ones
            //
            Map hashMap = new HashMap();
            for (Iterator it = summary.getItems().keySet().iterator(); it.hasNext();) {
                Object key = it.next();
                Item item = (Item) summary.getItems().get(key);
                if (!item.isMissing()) {
                    hashMap.put(key, item);
                }
            }

            summary.setItems(hashMap);
        } else {
            logger.error("Context " + context.getName() + " does not have \"jsp\" servlet");
        }
    }

    /**
     * Lists and optionally compiles a directory recursively.
     *
     * @param jspName     name of JSP file or directory to be listed and compiled.
     * @param opt
     * @param ctx
     * @param jrctx
     * @param summary
     * @param classLoader
     * @param level
     * @param compile
     */
    protected void compileItem(String jspName, Options opt, Context ctx, JspRuntimeContext jrctx,
                               Summary summary, URLClassLoader classLoader, int level, boolean compile) {
        ServletContext sctx = ctx.getServletContext();
        Set paths = sctx.getResourcePaths(jspName);

        if (paths != null) {
            for (Iterator it = paths.iterator(); it.hasNext();) {
                String name = (String) it.next();
                boolean isJsp = false;

                try {
                    isJsp = name.endsWith(".jsp") || name.endsWith(".jspx") || opt.getJspConfig().isJspPage(name);
                } catch (JasperException e) {
                    logger.info("isJspPage() thrown an error for "+name, e);
                }

                if (isJsp) {
                    JspCompilationContext jcctx = new JspCompilationContext(name, false, opt, sctx, null, jrctx);
                    jcctx.setClassLoader(classLoader);
                    ClassLoader cl = Thread.currentThread().getContextClassLoader();

                    Thread.currentThread().setContextClassLoader(classLoader);

                    try {
                        Item item = (Item) summary.getItems().get(name);

                        if (item == null) {
                            item = new Item();
                            item.setName(name);
                        }

                        item.setLevel(level);
                        item.setCompileTime(-1);
                        try {
                            ResourceAttributes jspAttributes = (ResourceAttributes) ctx.getResources().getAttributes(name);
                            item.setSize(jspAttributes.getContentLength());
                            item.setLastModified(jspAttributes.getLastModified());
                        } catch (NamingException e) {
                            logger.error("Cannot lookup attributes for " + name);
                        }


                        long time = System.currentTimeMillis();
                        try {
                            org.apache.jasper.compiler.Compiler c = jcctx.createCompiler();
                            if (compile) {
                                c.compile();
                                item.setState(Item.STATE_READY);
                                item.setException(null);
                            } else {
                                if (!c.isOutDated()) {
                                    item.setState(Item.STATE_READY);
                                    item.setException(null);
                                } else if (item.getState() != Item.STATE_FAILED) {
                                    item.setState(Item.STATE_OOD);
                                    item.setException(null);
                                }
                            }
                            logger.info("Compiled " + name + ": OK");
                        } catch (Exception e) {
                            item.setState(Item.STATE_FAILED);
                            item.setException(e);
                            logger.info("Compiled " + name + ": FAILED", e);
                        }
                        if (compile) {
                            item.setCompileTime(System.currentTimeMillis() - time);
                        }
                        item.setMissing(false);
                        summary.getItems().put(name, item);
                    } finally {
                        Thread.currentThread().setContextClassLoader(cl);
                    }
                } else {
                    compileItem(name, opt, ctx, jrctx, summary, classLoader, level + 1, compile);
                }
            }
        } else {
            logger.debug("getResourcePaths() is null for " + jspName + ". Empty dir? Or Tomcat bug?");
        }
    }

    protected abstract void removeInternal(String name) throws Exception;

    protected abstract void installContextInternal(String contextName, File f) throws Exception;
}
