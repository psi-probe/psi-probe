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

import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import com.thoughtworks.xstream.XStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.SimpleAttributes2GrantedAuthoritiesMapper;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.security.web.authentication.preauth.j2ee.J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.j2ee.J2eePreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.j2ee.WebXmlMappableAttributesRetriever;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * The Class ProbeSecurityConfig.
 */
@Configuration
@EnableWebSecurity
public class ProbeSecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * Gets the filter chain proxy.
   *
   * @return the filter chain proxy
   */
  @Bean(name = "filterChainProxy")
  public FilterChainProxy getFilterChainProxy() {
    SecurityFilterChain chain = new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"),
        getSecurityContextPersistenceFilter(), getJ2eePreAuthenticatedProcessingFilter(),
        getLogoutFilter(), getExceptionTranslationFilter(), getFilterSecurityInterceptor());
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
  @Bean(name = "sif")
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
  @Bean(name = "preAuthenticatedUserDetailsService")
  public PreAuthenticatedGrantedAuthoritiesUserDetailsService getPreAuthenticatedGrantedAuthoritiesUserDetailsService() {
    return new PreAuthenticatedGrantedAuthoritiesUserDetailsService();
  }

  /**
   * Gets the j 2 ee pre authenticated processing filter.
   *
   * @return the j 2 ee pre authenticated processing filter
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
   * Gets the http 403 forbidden entry point.
   *
   * @return the http 403 forbidden entry point
   */
  @Bean(name = "preAuthenticatedProcessingFilterEntryPoint")
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
   * Gets the j 2 ee based pre authenticated web authentication details source.
   *
   * @return the j 2 ee based pre authenticated web authentication details source
   */
  @Bean(name = "authenticationDetailsSource")
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
  @Bean(name = "j2eeUserRoles2GrantedAuthoritiesMapper")
  public SimpleAttributes2GrantedAuthoritiesMapper getSimpleAttributes2GrantedAuthoritiesMapper() {
    SimpleAttributes2GrantedAuthoritiesMapper mapper =
        new SimpleAttributes2GrantedAuthoritiesMapper();
    mapper.setConvertAttributeToUpperCase(true);
    return mapper;
  }

  /**
   * Gets the web xml mappable attributes retriever.
   *
   * @return the web xml mappable attributes retriever
   */
  @Bean(name = "j2eeMappableRolesRetriever")
  public WebXmlMappableAttributesRetriever getWebXmlMappableAttributesRetriever() {
    return new WebXmlMappableAttributesRetriever();
  }

  /**
   * Gets the exception translation filter.
   *
   * @return the exception translation filter
   */
  @Bean(name = "etf")
  public ExceptionTranslationFilter getExceptionTranslationFilter() {
    return new ExceptionTranslationFilter(getHttp403ForbiddenEntryPoint());
  }

  /**
   * Gets the affirmative based.
   *
   * @return the affirmative based
   */
  @Bean(name = "httpRequestAccessDecisionManager")
  public AffirmativeBased getAffirmativeBased() {
    List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
    decisionVoters.add(getRoleVoter());

    AffirmativeBased based = new AffirmativeBased(decisionVoters);
    based.setAllowIfAllAbstainDecisions(false);
    return based;
  }

  /**
   * Gets the filter security interceptor.
   *
   * @return the filter security interceptor
   */
  @Bean(name = "fsi")
  public FilterSecurityInterceptor getFilterSecurityInterceptor() {
    FilterSecurityInterceptor interceptor = new FilterSecurityInterceptor();
    interceptor.setAuthenticationManager(getProviderManager());
    interceptor.setAccessDecisionManager(getAffirmativeBased());

    LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
    requestMap.put(new AntPathRequestMatcher("/adm/**"),
        SecurityConfig.createListFromCommaDelimitedString("ROLE_MANAGER,ROLE_MANAGER-GUI"));
    requestMap.put(new AntPathRequestMatcher("/adm/restartvm.ajax"), SecurityConfig
        .createListFromCommaDelimitedString("ROLE_POWERUSERPLUS,ROLE_MANAGER,ROLE_MANAGER-GUI"));
    requestMap.put(new AntPathRequestMatcher("/sql/**"), SecurityConfig
        .createListFromCommaDelimitedString("ROLE_POWERUSERPLUS,ROLE_MANAGER,ROLE_MANAGER-GUI"));
    requestMap.put(new AntPathRequestMatcher("/app/**"),
        SecurityConfig.createListFromCommaDelimitedString(
            "ROLE_POWERUSER,ROLE_POWERUSERPLUS,ROLE_MANAGER,ROLE_MANAGER-GUI"));
    requestMap.put(new AntPathRequestMatcher("/**"),
        SecurityConfig.createListFromCommaDelimitedString(
            "ROLE_PROBEUSER,ROLE_POWERUSER,ROLE_POWERUSERPLUS,ROLE_MANAGER,ROLE_MANAGER-GUI"));

    interceptor
        .setSecurityMetadataSource(new DefaultFilterInvocationSecurityMetadataSource(requestMap));
    return interceptor;
  }

  /**
   * Gets the role voter.
   *
   * @return the role voter
   */
  @Bean(name = "roleVoter")
  public RoleVoter getRoleVoter() {
    return new RoleVoter();
  }

  /**
   * Gets the security context holder aware request filter.
   *
   * @return the security context holder aware request filter
   */
  @Bean(name = "securityContextHolderAwareRequestFilter")
  public SecurityContextHolderAwareRequestFilter getSecurityContextHolderAwareRequestFilter() {
    return new SecurityContextHolderAwareRequestFilter();
  }

  /**
   * Gets the http session request cache.
   *
   * @return the http session request cache
   */
  @Bean(name = "httpSessionRequestCache")
  public HttpSessionRequestCache getHttpSessionRequestCache() {
    HttpSessionRequestCache cache = new HttpSessionRequestCache();
    cache.setCreateSessionAllowed(false);
    return cache;
  }

  /**
   * Gets the xstream.
   *
   * @return the xstream
   */
  @Bean(name = "xstream")
  public XStream getXstream() {
    XStream xstream = new XStream();
    // clear out existing permissions and start a whitelist
    xstream.addPermission(NoTypePermission.NONE);
    // allow some basics
    xstream.addPermission(NullPermission.NULL);
    xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
    xstream.allowTypeHierarchy(Collection.class);
    xstream.allowTypeHierarchy(String.class);
    xstream.allowTypeHierarchy(TreeMap.class);
    xstream.allowTypesByWildcard(new String[] {
        "org.jfree.data.xy.**",
        "psiprobe.controllers.**",
        "psiprobe.model.**",
        "psiprobe.model.stats.**"
    });
    return xstream;
  }

}
