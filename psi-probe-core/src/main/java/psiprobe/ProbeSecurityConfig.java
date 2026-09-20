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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAttributes2GrantedAuthoritiesMapper;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.security.web.authentication.preauth.j2ee.J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.j2ee.J2eePreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.j2ee.WebXmlMappableAttributesRetriever;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

/**
 * The Class ProbeSecurityConfig.
 */
@Configuration
@EnableWebSecurity
public class ProbeSecurityConfig {

  /**
   * Gets the filter chain proxy.
   *
   * @return the filter chain proxy
   */
  @Bean(name = "filterChainProxy")
  public FilterChainProxy getFilterChainProxy() {
    SecurityFilterChain chain = new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"),
        getSecurityContextPersistenceFilter(), getJ2eePreAuthenticatedProcessingFilter(),
        getLogoutFilter(), getExceptionTranslationFilter(), getAuthorizationFilter());

    return new FilterChainProxy(chain);
  }

  /**
   * Gets the provider manager.
   *
   * @return the provider manager
   */
  @Bean(name = "authenticationManager")
  public ProviderManager getProviderManager() {
    List<AuthenticationProvider> providers = new ArrayList<>();
    providers.add(getPreAuthenticatedAuthenticationProvider());

    return new ProviderManager(providers);
  }

  /**
   * Gets the security context persistence filter.
   *
   * @return the security context persistence filter
   */
  // NOTE While deprecated, tomcat 9 does not use listener in same way to work, stay with this for
  // tomcat 9
  @Bean(name = "securityContextPersistenceFilter")
  public SecurityContextPersistenceFilter getSecurityContextPersistenceFilter() {
    return new SecurityContextPersistenceFilter();
  }

  /**
   * Gets the pre authenticated authentication provider.
   *
   * @return the pre authenticated authentication provider
   */
  @Bean(name = "preAuthenticatedAuthenticationProvider")
  public PreAuthenticatedAuthenticationProvider getPreAuthenticatedAuthenticationProvider() {
    PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();

    provider.setPreAuthenticatedUserDetailsService(
        getPreAuthenticatedGrantedAuthoritiesUserDetailsService());

    return provider;
  }

  /**
   * Gets the pre authenticated granted authorities user details service.
   *
   * @return the pre authenticated granted authorities user details service
   */
  @Bean(name = "preAuthenticatedGrantedAuthoritiesUserDetailsService")
  public PreAuthenticatedGrantedAuthoritiesUserDetailsService getPreAuthenticatedGrantedAuthoritiesUserDetailsService() {
    return new PreAuthenticatedGrantedAuthoritiesUserDetailsService();
  }

  /**
   * Gets the J2EE pre authenticated processing filter.
   *
   * @return the J2EE pre authenticated processing filter
   */
  @Bean(name = "j2eePreAuthenticatedProcessingFilter")
  public J2eePreAuthenticatedProcessingFilter getJ2eePreAuthenticatedProcessingFilter() {
    J2eePreAuthenticatedProcessingFilter filter = new J2eePreAuthenticatedProcessingFilter();

    filter.setAuthenticationManager(getProviderManager());
    filter.setAuthenticationDetailsSource(
        getJ2eeBasedPreAuthenticatedWebAuthenticationDetailsSource());

    return filter;
  }

  /**
   * Gets the HTTP 403 forbidden entry point.
   *
   * @return the HTTP 403 forbidden entry point
   */
  @Bean(name = "http403ForbiddenEntryPoint")
  public Http403ForbiddenEntryPoint getHttp403ForbiddenEntryPoint() {
    return new Http403ForbiddenEntryPoint();
  }

  /**
   * Gets the logout filter.
   *
   * @return the logout filter
   */
  @Bean(name = "logoutFilter")
  public LogoutFilter getLogoutFilter() {
    return new LogoutFilter("/", getSecurityContextLogoutHandler());
  }

  /**
   * Gets the security context logout handler.
   *
   * @return the security context logout handler
   */
  @Bean(name = "securityContextLogoutHandler")
  public SecurityContextLogoutHandler getSecurityContextLogoutHandler() {
    return new SecurityContextLogoutHandler();
  }

  /**
   * Gets the J2EE based pre authenticated web authentication details source.
   *
   * @return the J2EE based pre authenticated web authentication details source
   */
  @Bean(name = "j2eeBasedPreAuthenticatedWebAuthenticationDetailsSource")
  public J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource getJ2eeBasedPreAuthenticatedWebAuthenticationDetailsSource() {
    J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource source =
        new J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource();

    source.setMappableRolesRetriever(getWebXmlMappableAttributesRetriever());
    source.setUserRoles2GrantedAuthoritiesMapper(getSimpleAttributes2GrantedAuthoritiesMapper());

    return source;
  }

  /**
   * Gets the simple attributes 2 granted authorities mapper.
   *
   * @return the simple attributes 2 granted authorities mapper
   */
  @Bean(name = "simpleAttributes2GrantedAuthoritiesMapper")
  public SimpleAttributes2GrantedAuthoritiesMapper getSimpleAttributes2GrantedAuthoritiesMapper() {
    SimpleAttributes2GrantedAuthoritiesMapper mapper =
        new SimpleAttributes2GrantedAuthoritiesMapper();

    mapper.setConvertAttributeToUpperCase(true);

    return mapper;
  }

  /**
   * Gets the web XML mappable attributes retriever.
   *
   * @return the web XML mappable attributes retriever
   */
  @Bean(name = "webXmlMappableAttributesRetriever")
  public WebXmlMappableAttributesRetriever getWebXmlMappableAttributesRetriever() {
    return new WebXmlMappableAttributesRetriever();
  }

  /**
   * Gets the exception translation filter.
   *
   * @return the exception translation filter
   */
  @Bean(name = "exceptionTranslationFilter")
  public ExceptionTranslationFilter getExceptionTranslationFilter() {
    return new ExceptionTranslationFilter(getHttp403ForbiddenEntryPoint());
  }

  /**
   * Gets the authorization filter.
   *
   * @return the authorization filter
   */
  @Bean(name = "authorizationFilter")
  public AuthorizationFilter getAuthorizationFilter() {
    return new AuthorizationFilter(getAuthorizationManager());
  }

  /**
   * Gets the authorization manager.
   *
   * @return the authorization manager
   */
  @Bean(name = "authorizationManager")
  public RequestMatcherDelegatingAuthorizationManager getAuthorizationManager() {
    RequestMatcherDelegatingAuthorizationManager.Builder manager =
        RequestMatcherDelegatingAuthorizationManager.builder();

    manager.add(new AntPathRequestMatcher("/adm/**"),
        AuthorityAuthorizationManager.hasAnyAuthority("ROLE_MANAGER", "ROLE_MANAGER-GUI"));

    manager.add(new AntPathRequestMatcher("/adm/restartvm.ajax"), AuthorityAuthorizationManager
        .hasAnyAuthority("ROLE_POWERUSERPLUS", "ROLE_MANAGER", "ROLE_MANAGER-GUI"));

    manager.add(new AntPathRequestMatcher("/sql/**"), AuthorityAuthorizationManager
        .hasAnyAuthority("ROLE_POWERUSERPLUS", "ROLE_MANAGER", "ROLE_MANAGER-GUI"));

    manager.add(new AntPathRequestMatcher("/app/**"), AuthorityAuthorizationManager.hasAnyAuthority(
        "ROLE_POWERUSER", "ROLE_POWERUSERPLUS", "ROLE_MANAGER", "ROLE_MANAGER-GUI"));

    manager.add(AnyRequestMatcher.INSTANCE,
        AuthorityAuthorizationManager.hasAnyAuthority("ROLE_PROBEUSER", "ROLE_POWERUSER",
            "ROLE_POWERUSERPLUS", "ROLE_MANAGER", "ROLE_MANAGER-GUI"));

    return manager.build();
  }

  /**
   * Gets the XStream.
   *
   * @return the XStream
   */
  @Bean(name = "xstream")
  public XStream getXstream() {
    XStream xstream = new XStream();

    // Clear out existing permissions and start a whitelist.
    xstream.addPermission(NoTypePermission.NONE);

    // Allow some basics.
    xstream.addPermission(NullPermission.NULL);
    xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
    xstream.allowTypeHierarchy(Collection.class);
    xstream.allowTypeHierarchy(String.class);
    xstream.allowTypeHierarchy(TreeMap.class);

    xstream.allowTypesByWildcard(new String[] {"org.jfree.data.xy.**", "psiprobe.controllers.**",
        "psiprobe.model.**", "psiprobe.model.stats.**"});

    return xstream;
  }

}
