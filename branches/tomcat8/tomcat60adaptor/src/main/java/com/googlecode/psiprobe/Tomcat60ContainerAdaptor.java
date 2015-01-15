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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;
import org.apache.catalina.deploy.ApplicationParameter;
import org.apache.catalina.deploy.ContextResource;
import org.apache.catalina.deploy.ContextResourceLink;
import org.apache.catalina.deploy.FilterDef;
import org.apache.catalina.deploy.FilterMap;
import org.apache.catalina.deploy.NamingResources;
import org.apache.commons.modeler.Registry;
import org.apache.naming.resources.ResourceAttributes;

import com.googlecode.psiprobe.model.ApplicationParam;
import com.googlecode.psiprobe.model.ApplicationResource;
import com.googlecode.psiprobe.model.FilterInfo;
import com.googlecode.psiprobe.model.FilterMapping;

/**
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class Tomcat60ContainerAdaptor extends AbstractTomcatContainer {

    private Host host;
    private ObjectName deployerOName;
    private MBeanServer mBeanServer;
    private Valve valve = new Tomcat60AgentValve();

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
        return binding != null && (binding.startsWith("Apache Tomcat/6.0")
                || binding.startsWith("JBossWeb/2.0")
                || binding.startsWith("JBoss Web/2.1")
                || (binding.startsWith("SpringSource tc") && binding.contains("/6.0"))
                || (binding.startsWith("VMware vFabric tc") && binding.contains("/6.0")));
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

	public boolean getAvailable(Context context) {
		return context.getAvailable();
	}
	
	public void addContextResourceLink(Context context, List resourceList,
			boolean contextBound) {
		ContextResourceLink[] resourceLinks = context.getNamingResources().findResourceLinks();
		for (int i = 0; i < resourceLinks.length; i++) {
		    ContextResourceLink link = resourceLinks[i];

		    ApplicationResource resource = new ApplicationResource();
		    logger.debug("reading resourceLink: " + link.getName());
		    resource.setApplicationName(context.getName());
		    resource.setName(link.getName());
		    resource.setType(link.getType());
		    resource.setLinkTo(link.getGlobal());

		   // lookupResource(resource, contextBound, false);
		    
		    resourceList.add(resource);
		}
	}

	public void addContextResource(Context context, List resourceList,
			boolean contextBound) {
		NamingResources namingResources = context.getNamingResources();
		ContextResource[] resources = namingResources.findResources();
            
		for (int i = 0; i < resources.length; i++) {
		    ContextResource contextResource = resources[i];
		    ApplicationResource resource = new ApplicationResource();

		    logger.info("reading resource: " + contextResource.getName());
		    resource.setApplicationName(context.getName());
		    resource.setName(contextResource.getName());
		    resource.setType(contextResource.getType());
		    resource.setScope(contextResource.getScope());
		    resource.setAuth(contextResource.getAuth());
		    resource.setDescription(contextResource.getDescription());

		    //lookupResource(resource, contextBound, false);

		    resourceList.add(resource);
		}
	}

	public List getApplicationFilterMaps(Context context) {
        FilterMap[] fms = context.findFilterMaps();
        List filterMaps = new ArrayList(fms.length);
        for (int i = 0; i < fms.length; i++) {
            if (fms[i] != null) {
                String dm;
                switch(fms[i].getDispatcherMapping()) {
                    case FilterMap.ERROR: dm = "ERROR"; break;
                    case FilterMap.FORWARD: dm = "FORWARD"; break;
                    case FilterMap.FORWARD_ERROR: dm = "FORWARD,ERROR"; break;
                    case FilterMap.INCLUDE: dm = "INCLUDE"; break;
                    case FilterMap.INCLUDE_ERROR: dm = "INCLUDE,ERROR"; break;
                    case FilterMap.INCLUDE_ERROR_FORWARD: dm = "INCLUDE,ERROR,FORWARD"; break;
                    case FilterMap.INCLUDE_FORWARD: dm = "INCLUDE,FORWARD"; break;
                    case FilterMap.REQUEST: dm = "REQUEST"; break;
                    case FilterMap.REQUEST_ERROR: dm = "REQUEST,ERROR"; break;
                    case FilterMap.REQUEST_ERROR_FORWARD: dm = "REQUEST,ERROR,FORWARD"; break;
                    case FilterMap.REQUEST_ERROR_FORWARD_INCLUDE: dm = "REQUEST,ERROR,FORWARD,INCLUDE"; break;
                    case FilterMap.REQUEST_ERROR_INCLUDE: dm = "REQUEST,ERROR,INCLUDE"; break;
                    case FilterMap.REQUEST_FORWARD: dm = "REQUEST,FORWARD"; break;
                    case FilterMap.REQUEST_INCLUDE: dm = "REQUEST,INCLUDE"; break;
                    case FilterMap.REQUEST_FORWARD_INCLUDE: dm = "REQUEST,FORWARD,INCLUDE"; break;
                    default: dm = "";
                }

                String filterClass = "";
                FilterDef fd = context.findFilterDef(fms[i].getFilterName());
                if (fd != null) {
                    filterClass = fd.getFilterClass();
                }

                List filterMappings = getFilterMappings(fms[i], dm, filterClass);
                filterMaps.addAll(filterMappings);
            }
        }
        return filterMaps;
    }
	
	 public List getApplicationFilters(Context context) {
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
	 
	 private static FilterInfo getFilterInfo(FilterDef fd) {
	        FilterInfo fi = new FilterInfo();
	        fi.setFilterName(fd.getFilterName());
	        fi.setFilterClass(fd.getFilterClass());
	        fi.setFilterDesc(fd.getDescription());
	        return fi;
	    }
	
	 public List getApplicationInitParams(Context context) {
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
	 
	 public Long[] getResourceAttributes(String name, Context context) {
		 Long result[] = new Long[2];
			try {
				ResourceAttributes resource = (ResourceAttributes) context.getResources().getAttributes(name);
				result[0] = resource.getContentLength();
				result[1] = resource.getLastModified();
			} catch (NamingException e) {
				//Don't care
			}
		
			return result;
		}


}
