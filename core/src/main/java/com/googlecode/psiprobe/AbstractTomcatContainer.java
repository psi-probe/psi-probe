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

package com.googlecode.psiprobe;

import com.googlecode.psiprobe.model.jsp.Item;
import com.googlecode.psiprobe.model.jsp.Summary;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.core.StandardContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jasper.EmbeddedServletOptions;
import org.apache.jasper.JasperException;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.Options;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.apache.naming.ContextBindings;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.net.URI;
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

/**
 * Abstraction layer to implement some functionality, which is common between different container
 * adaptors.
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public abstract class AbstractTomcatContainer implements TomcatContainer {

  protected Log logger = LogFactory.getLog(getClass());

  /**
   * Deploys a context, assuming an context descriptor file exists on the server already.
   * 
   * @param contextName the context path, which should match the filename
   * @return {@code} true if deployment was successful
   * @throws Exception if deployment fails spectacularly
   */
  public boolean installContext(String contextName) throws Exception {
    contextName = formatContextName(contextName);
    String contextFilename = formatContextFilename(contextName);
    File contextFile = new File(getConfigBase(), contextFilename + ".xml");
    installContextInternal(contextName, contextFile);
    return findContext(contextName) != null;
  }

  /**
   * Undeploys a context.
   * 
   * @param contextName the context path
   * @throws Exception if undeployment fails spectacularly
   */
  public void remove(String contextName) throws Exception {
    contextName = formatContextName(contextName);
    Context ctx = findContext(contextName);

    if (ctx != null) {

      try {
        stop(contextName);
      } catch (Throwable e) {
        logger.info("Stopping " + contextName + " threw this exception:", e);
        // make sure we always re-throw ThreadDeath
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

      String warFilename = formatContextFilename(contextName);
      File warFile = new File(getAppBase(), warFilename + ".war");
      logger.debug("Deleting " + warFile.getAbsolutePath());
      Utils.delete(warFile);

      File configFile = getConfigFile(ctx);
      if (configFile != null) {
        logger.debug("Deleting " + configFile.getAbsolutePath());
        Utils.delete(configFile);
      }

      removeInternal(contextName);
    }
  }

  /**
   * Binds a naming context to the current thread's classloader.
   * 
   * @param context the catalina context
   * @throws NamingException if binding fails
   */
  public void bindToContext(Context context) throws NamingException {
    Object token = null;
    ContextBindings.bindClassLoader(context, token, Thread.currentThread().getContextClassLoader());
  }

  /**
   * Unbinds a naming context from the current thread's classloader.
   * 
   * @param context the catalina context
   * @throws NamingException if unbinding fails
   */
  public void unbindFromContext(Context context) throws NamingException {
    Object token = null;
    ContextBindings.unbindClassLoader(context, token, Thread.currentThread()
        .getContextClassLoader());
  }

  /**
   * Finds a context based on its path.
   * 
   * @param name the context path
   * @return the context deployed to that path
   */
  public Context findContext(String name) {
    String safeName = formatContextName(name);
    if (safeName == null) {
      return null;
    }
    Context result = findContextInternal(safeName);
    if (result == null && "".equals(safeName)) {
      result = findContextInternal("/");
    }
    return result;
  }

  /**
   * Formats a context name to a path that the container will recognize. Usually this means
   * prepending a {@code /} character, although there is special behavior for the root context.
   * 
   * @param name the context name
   * @return the context name formatted as the container expects
   */
  public String formatContextName(String name) {
    if (name == null) {
      return null;
    }
    String result = name.trim();
    if (!result.startsWith("/")) {
      result = "/" + result;
    }
    if ("/".equals(result) || "/ROOT".equals(result)) {
      result = "";
    }
    return result;
  }

  /**
   * Formats a context name so that it can be used as a step for the context descriptor .xml or
   * deployed .war file. Usually this means stripping a leading {@code /} character, although there
   * is special behavior for the root context.
   * 
   * @param contextName the context name
   * @return the filename stem for this context
   */
  public String formatContextFilename(String contextName) {
    if (contextName == null) {
      return null;
    } else if ("".equals(contextName)) {
      return "ROOT";
    } else if (contextName.startsWith("/")) {
      return contextName.substring(1);
    } else {
      return contextName;
    }
  }

  /**
   * Deletes the "work" directory of the given context.
   * 
   * @param context the context
   */
  public void discardWorkDir(Context context) {
    if (context instanceof StandardContext) {
      StandardContext standardContext = (StandardContext) context;
      logger.info("Discarding " + standardContext.getWorkPath());
      Utils.delete(new File(standardContext.getWorkPath(), "org"));
    } else {
      logger.error("context " + context.getName() + " is not an instance of "
          + context.getClass().getName() + ", expected StandardContext");
    }
  }

  /**
   * Returns the JSP servlet filename for the given JSP file.
   * 
   * @param context the context
   * @param jspName the JSP filename
   * @return the name of the JSP servlet
   */
  public String getServletFileNameForJsp(Context context, String jspName) {
    String servletName = null;

    ServletConfig servletConfig = (ServletConfig) context.findChild("jsp");
    if (servletConfig != null) {
      ServletContext sctx = context.getServletContext();
      Options opt = new EmbeddedServletOptions(servletConfig, sctx);
      JspRuntimeContext jrctx = new JspRuntimeContext(sctx, opt);
      JspCompilationContext jcctx =
          createJspCompilationContext(jspName, opt, sctx, jrctx, null);
      servletName = jcctx.getServletJavaFileName();
    } else {
      logger.error("Context " + context.getName() + " does not have \"jsp\" servlet");
    }
    return servletName;
  }

  /**
   * Compiles a list of JSPs. Names of JSP files are expected to be relative to the webapp root. The
   * method updates summary with compilation details.
   *
   * @param context the context
   * @param summary the summary in which the output is stored
   * @param names the list of JSPs to compile
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
            /*
             * we need to pass context classloader here, so the jsps can reference /WEB-INF/classes
             * and /WEB-INF/lib. JspCompilationContext would only take URLClassLoader, so we fake it
             */
            URLClassLoader classLoader =
                new URLClassLoader(new URL[] {}, context.getLoader().getClassLoader());
            for (Iterator it = names.iterator(); it.hasNext();) {
              String name = (String) it.next();
              long time = System.currentTimeMillis();
              JspCompilationContext jcctx =
                  createJspCompilationContext(name, opt, sctx, jrctx, classLoader);
              ClassLoader prevCl = ClassUtils.overrideThreadContextClassLoader(classLoader);
              try {
                Item item = (Item) summary.getItems().get(name);
                if (item != null) {
                  try {
                    org.apache.jasper.compiler.Compiler compiler = jcctx.createCompiler();
                    compiler.compile();
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
                ClassUtils.overrideThreadContextClassLoader(prevCl);
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
   * Lists and optionally compiles all JSPs for the given context. Compilation details are added to
   * the summary.
   *
   * @param context the context
   * @param summary the summary in which the output is stored
   * @param compile whether to compile all of the JSPs or not
   */
  public void listContextJsps(Context context, Summary summary, boolean compile) {
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

          /*
           * mark all items as missing
           */
          for (Iterator it = summary.getItems().keySet().iterator(); it.hasNext();) {
            Item item = (Item) summary.getItems().get(it.next());
            item.setMissing(true);
          }

          /*
           * we need to pass context classloader here, so the jsps can reference /WEB-INF/classes
           * and /WEB-INF/lib. JspCompilationContext would only take URLClassLoader, so we fake it
           */
          URLClassLoader urlcl = new URLClassLoader(
              new URL[] {}, context.getLoader().getClassLoader());
          
          compileItem("/", opt, context, jrctx, summary, urlcl, 0, compile);
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
   * Returns the context descriptor filename for the given context.
   * 
   * @param context the context
   * @return the context descriptor filename, or {@code null}
   */
  public File getConfigFile(Context context) {
    URL configUrl = context.getConfigFile();
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

  /**
   * Returns the path of where the context descriptors are stored.
   * 
   * @param container the container
   * @return the path where context descriptors are stored
   */
  protected String getConfigBase(Container container) {
    File configBase = new File(System.getProperty("catalina.base"), "conf");
    Container baseHost = null;
    Container baseEngine = null;
    while (container != null) {
      if (container instanceof Host) {
        baseHost = container;
      }
      if (container instanceof Engine) {
        baseEngine = container;
      }
      container = container.getParent();
    }
    if (baseEngine != null) {
      configBase = new File(configBase, baseEngine.getName());
    }
    if (baseHost != null) {
      configBase = new File(configBase, baseHost.getName());
    }
    return configBase.getAbsolutePath();
  }

  /**
   * Lists and optionally compiles a directory recursively.
   *
   * @param jspName name of JSP file or directory to be listed and compiled.
   * @param opt the JSP compiler options
   * @param ctx the context
   * @param jrctx the runtime context used to create the compilation context
   * @param summary the summary in which the output is stored
   * @param classLoader the classloader used by the compiler
   * @param level the depth in the tree at which the item was encountered
   * @param compile whether or not to compile the item or just to check whether it's out of date
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
          isJsp = name.endsWith(".jsp") || name.endsWith(".jspx")
              || opt.getJspConfig().isJspPage(name);
        } catch (JasperException e) {
          logger.info("isJspPage() thrown an error for " + name, e);
        }

        if (isJsp) {
          JspCompilationContext jcctx =
              createJspCompilationContext(name, opt, sctx, jrctx, classLoader);
          ClassLoader prevCl = ClassUtils.overrideThreadContextClassLoader(classLoader);
          try {
            Item item = (Item) summary.getItems().get(name);

            if (item == null) {
              item = new Item();
              item.setName(name);
            }

            item.setLevel(level);
            item.setCompileTime(-1);

            Long[] objects = this.getResourceAttributes(name, ctx);
            item.setSize(objects[0]);
            item.setLastModified(objects[1]);

            long time = System.currentTimeMillis();
            try {
              org.apache.jasper.compiler.Compiler compiler = jcctx.createCompiler();
              if (compile) {
                compiler.compile();
                item.setState(Item.STATE_READY);
                item.setException(null);
              } else {
                if (!compiler.isOutDated()) {
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
            ClassUtils.overrideThreadContextClassLoader(prevCl);
          }
        } else {
          compileItem(name, opt, ctx, jrctx, summary, classLoader, level + 1, compile);
        }
      }
    } else {
      logger.debug("getResourcePaths() is null for " + jspName + ". Empty dir? Or Tomcat bug?");
    }
  }

  protected abstract JspCompilationContext createJspCompilationContext(String name, Options opt,
      ServletContext sctx, JspRuntimeContext jrctx, ClassLoader classLoader);

  protected abstract void removeInternal(String name) throws Exception;

  protected abstract void installContextInternal(String contextName, File config) throws Exception;

  protected abstract Context findContextInternal(String contextName);

}
