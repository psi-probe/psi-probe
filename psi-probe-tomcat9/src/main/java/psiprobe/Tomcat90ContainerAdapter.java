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
package psiprobe;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.catalina.Context;
import org.apache.catalina.Valve;
import org.apache.catalina.WebResource;
import org.apache.catalina.deploy.NamingResourcesImpl;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.Options;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.apache.naming.ContextAccessController;
import org.apache.tomcat.util.descriptor.web.ApplicationParameter;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.apache.tomcat.util.descriptor.web.ContextResourceLink;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import psiprobe.model.ApplicationParam;
import psiprobe.model.ApplicationResource;
import psiprobe.model.FilterInfo;
import psiprobe.model.FilterMapping;

/**
 * The Class Tomcat90ContainerAdapter.
 */
public class Tomcat90ContainerAdapter extends AbstractTomcatContainer {

  @Override
  protected Valve createValve() {
    return new Tomcat90AgentValve();
  }

  @Override
  public boolean canBoundTo(String binding) {
    if (binding == null) {
      return false;
    }
    return binding.startsWith("Apache Tomcat/9.0")
        || binding.startsWith("Apache Tomcat (TomEE)/9.0") || binding.startsWith("Pivotal tc")
        || binding.startsWith("Vmware tc") && binding.contains("/9.0");
  }

  /**
   * Gets the filter mappings.
   *
   * @param fmap the fmap
   * @param dm the dm
   * @param filterClass the filter class
   *
   * @return the filter mappings
   */
  protected List<FilterMapping> getFilterMappings(FilterMap fmap, String dm, String filterClass) {
    String[] urls = fmap.getURLPatterns();
    String[] servlets = fmap.getServletNames();
    List<FilterMapping> results = new ArrayList<>(urls.length + servlets.length);
    addFilterMapping(fmap.getFilterName(), dm, filterClass, urls, results, FilterMapType.URL);
    addFilterMapping(fmap.getFilterName(), dm, filterClass, servlets, results,
        FilterMapType.SERVLET_NAME);
    return results;
  }

  @Override
  protected JspCompilationContext createJspCompilationContext(String name, Options opt,
      ServletContext sctx, JspRuntimeContext jrctx, ClassLoader classLoader) {

    JspCompilationContext jcctx = new JspCompilationContext(name, opt, sctx, null, jrctx);
    jcctx.setClassLoader(classLoader);
    return jcctx;
  }

  @Override
  public void addContextResourceLink(Context context, List<ApplicationResource> resourceList) {

    NamingResourcesImpl namingResources = context.getNamingResources();
    for (ContextResourceLink link : namingResources.findResourceLinks()) {
      ApplicationResource resource = new ApplicationResource();

      logger.debug("reading resourceLink: {}", link.getName());
      resource.setApplicationName(context.getName());
      resource.setName(link.getName());
      resource.setType(link.getType());
      resource.setLinkTo(link.getGlobal());

      registerGlobalResourceAccess(link);

      resourceList.add(resource);
    }
  }

  @Override
  public void addContextResource(Context context, List<ApplicationResource> resourceList) {
    NamingResourcesImpl namingResources = context.getNamingResources();
    for (ContextResource contextResource : namingResources.findResources()) {
      ApplicationResource resource = new ApplicationResource();

      logger.info("reading resource: {}", contextResource.getName());
      resource.setApplicationName(context.getName());
      resource.setName(contextResource.getName());
      resource.setType(contextResource.getType());
      resource.setScope(contextResource.getScope());
      resource.setAuth(contextResource.getAuth());
      resource.setDescription(contextResource.getDescription());

      resourceList.add(resource);
    }
  }

  @Override
  public List<FilterMapping> getApplicationFilterMaps(Context context) {
    FilterMap[] fms = context.findFilterMaps();
    List<FilterMapping> filterMaps = new ArrayList<>(fms.length);
    for (FilterMap filterMap : fms) {
      if (filterMap != null) {
        String dm;
        switch (filterMap.getDispatcherMapping()) {
          case FilterMap.ASYNC:
            dm = "ASYNC";
            break;
          case FilterMap.ERROR:
            dm = "ERROR";
            break;
          case FilterMap.FORWARD:
            dm = "FORWARD";
            break;
          case FilterMap.INCLUDE:
            dm = "INCLUDE";
            break;
          case FilterMap.REQUEST:
            dm = "REQUEST";
            break;
          default:
            dm = "";
        }

        String filterClass = "";
        FilterDef fd = context.findFilterDef(filterMap.getFilterName());
        if (fd != null) {
          filterClass = fd.getFilterClass();
        }

        List<FilterMapping> filterMappings = getFilterMappings(filterMap, dm, filterClass);
        filterMaps.addAll(filterMappings);
      }
    }
    return filterMaps;
  }

  @Override
  public List<FilterInfo> getApplicationFilters(Context context) {
    FilterDef[] fds = context.findFilterDefs();
    List<FilterInfo> filterDefs = new ArrayList<>(fds.length);
    for (FilterDef filterDef : fds) {
      if (filterDef != null) {
        FilterInfo fi = getFilterInfo(filterDef);
        filterDefs.add(fi);
      }
    }
    return filterDefs;
  }

  /**
   * Gets the filter info.
   *
   * @param fd the fd
   *
   * @return the filter info
   */
  private static FilterInfo getFilterInfo(FilterDef fd) {
    FilterInfo fi = new FilterInfo();
    fi.setFilterName(fd.getFilterName());
    fi.setFilterClass(fd.getFilterClass());
    fi.setFilterDesc(fd.getDescription());
    return fi;
  }

  @Override
  public List<ApplicationParam> getApplicationInitParams(Context context) {
    /*
     * We'll try to determine if a parameter value comes from a deployment descriptor or a context
     * descriptor.
     *
     * Assumption: context.findParameter() returns only values of parameters that are declared in a
     * deployment descriptor.
     *
     * If a parameter is declared in a context descriptor with override=false and redeclared in a
     * deployment descriptor, context.findParameter() still returns its value, even though the value
     * is taken from a context descriptor.
     *
     * context.findApplicationParameters() returns all parameters that are declared in a context
     * descriptor regardless of whether they are overridden in a deployment descriptor or not or
     * not.
     */
    /*
     * creating a set of parameter names that are declared in a context descriptor and can not be
     * overridden in a deployment descriptor.
     */
    Set<String> nonOverridableParams = new HashSet<>();
    for (ApplicationParameter appParam : context.findApplicationParameters()) {
      if (appParam != null && !appParam.getOverride()) {
        nonOverridableParams.add(appParam.getName());
      }
    }
    List<ApplicationParam> initParams = new ArrayList<>(20);
    ServletContext servletCtx = context.getServletContext();
    for (String paramName : Collections.list(servletCtx.getInitParameterNames())) {
      ApplicationParam param = new ApplicationParam();
      param.setName(paramName);
      param.setValue(servletCtx.getInitParameter(paramName));
      /*
       * if the parameter is declared in a deployment descriptor and it is not declared in a context
       * descriptor with override=false, the value comes from the deployment descriptor
       */
      param.setFromDeplDescr(
          context.findParameter(paramName) != null && !nonOverridableParams.contains(paramName));
      initParams.add(param);
    }
    return initParams;
  }

  @Override
  public boolean resourceExists(String name, Context context) {
    return context.getResources().getResource(name) != null;
  }

  @Override
  public InputStream getResourceStream(String name, Context context) throws IOException {
    WebResource resource = context.getResources().getResource(name);
    return resource.getInputStream();
  }

  @Override
  public Long[] getResourceAttributes(String name, Context context) {
    Long[] result = new Long[2];
    WebResource resource = context.getResources().getResource(name);
    result[0] = resource.getContentLength();
    result[1] = resource.getLastModified();
    return result;
  }

  /**
   * Returns the security token required to bind to a naming context.
   *
   * @param context the catalina context
   *
   * @return the security token for use with <code>ContextBindings</code>
   */
  @Override
  protected Object getNamingToken(Context context) {
    // Used by NamingContextListener when setting up JNDI context
    Object token = context.getNamingToken();
    if (!ContextAccessController.checkSecurityToken(context, token)) {
      logger.error("Couldn't get a valid security token. ClassLoader binding will fail.");
    }
    return token;
  }

}
