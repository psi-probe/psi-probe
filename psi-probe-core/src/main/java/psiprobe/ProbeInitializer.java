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

import com.opensymphony.sitemesh.webapp.SiteMeshFilter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionTrackingMode;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * The class ProbeInitializer.
 */
public class ProbeInitializer implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {

    // Set spring config location
    try (AnnotationConfigWebApplicationContext rootContext =
        new AnnotationConfigWebApplicationContext()) {
      rootContext.register(ProbeConfig.class);

      // Set context loader listener
      servletContext.addListener(new ContextLoaderListener(rootContext));
    }

    // Set probe servlet
    ServletRegistration.Dynamic probe = servletContext.addServlet("probe", ProbeServlet.class);

    // Set Role that can view session attribute values
    servletContext.setInitParameter("attribute.value.roles", "ROLE_MANAGER,ROLE_MANAGER-GUI");

    // Set Initial Parameters
    Map<String, String> initParameters = new HashMap<>();
    initParameters.put("contextConfigLocation", "");
    probe.setInitParameters(initParameters);

    // Set load on startup
    probe.setLoadOnStartup(0);

    // Add Mapping
    probe.addMapping("*.htm");
    probe.addMapping("*.ajax");
    probe.addMapping("/logs/*");
    probe.addMapping("/chart.png");

    // Set sitemesh filter
    FilterRegistration.Dynamic sitemesh =
        servletContext.addFilter("sitemesh", SiteMeshFilter.class);
    sitemesh.addMappingForUrlPatterns(
        EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST, DispatcherType.ERROR), false,
        "/*");

    // Set security filter
    FilterRegistration.Dynamic security =
        servletContext.addFilter("filterChainProxy", DelegatingFilterProxy.class);
    security.addMappingForUrlPatterns(
        EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST, DispatcherType.ERROR), false,
        "/*");

    // Set session cookie config
    servletContext.getSessionCookieConfig().setHttpOnly(true);
    servletContext.getSessionCookieConfig().setSecure(true);

    // Set session tracking mode
    EnumSet<SessionTrackingMode> trackingMode = EnumSet.of(SessionTrackingMode.COOKIE);
    servletContext.setSessionTrackingModes(trackingMode);
  }

}
