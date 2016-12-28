/**
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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionTrackingMode;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.opensymphony.sitemesh.webapp.SiteMeshFilter;

/**
 * The class ProbeInitializer.
 */
public class ProbeInitializer implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {

    // Set spring security config location 
    servletContext.setInitParameter("contextConfigLocation", "/WEB-INF/spring-probe-security.xml");

    // Set context loader listener
    servletContext.addListener(new ContextLoaderListener());

    // Set probe servlet
    ServletRegistration.Dynamic probe = servletContext.addServlet("probe", ProbeServlet.class);

    Map<String, String> initParameters = new HashMap<>();
    initParameters.put("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
    initParameters.put("contextConfigLocation", "psiprobe.ProbeConfig");
    probe.setInitParameters(initParameters);

    probe.setLoadOnStartup(0);
    probe.addMapping("*.htm");
    probe.addMapping("*.ajax");
    probe.addMapping("/logs/*");
    probe.addMapping("/chart.png");

    // Set sitemesh filter
    FilterRegistration.Dynamic sitemesh = servletContext.addFilter("sitemesh", SiteMeshFilter.class);
    sitemesh.addMappingForUrlPatterns(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST, DispatcherType.ERROR), false, "/*");

    // Set security filter
    FilterRegistration.Dynamic security = servletContext.addFilter("filterChainProxy", DelegatingFilterProxy.class);
    security.addMappingForUrlPatterns(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST, DispatcherType.ERROR), false, "/*");

    // Set session cookie config
    servletContext.getSessionCookieConfig().setHttpOnly(true);
    /**
     *  Disable secure cookie until http session issues can be resolved. While tomcat
     *  will handle this normally with http, spring has been shown to continually create new
     *  sessions.  This may not be related to spring security but rather spring web mvc.
     */
    // servletContext.getSessionCookieConfig().setSecure(true);

    // Set session tracking mode
    Set<SessionTrackingMode> trackingMode = new HashSet<>();
    trackingMode.add(SessionTrackingMode.COOKIE);
    servletContext.setSessionTrackingModes(trackingMode);
  }

}
