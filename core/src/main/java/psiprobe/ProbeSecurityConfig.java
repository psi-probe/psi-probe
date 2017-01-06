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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

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

@Configuration
@EnableWebSecurity
public class ProbeSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean(name = "filterChainProxy")
  public FilterChainProxy getFilterChainProxy() {
    SecurityFilterChain chain = new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"),
        getSecurityContextPersistenceFilter(), getJ2eePreAuthenticatedProcessingFilter(),
        getLogoutFilter(), getExceptionTranslationFilter(), getFilterSecurityInterceptor());
    return new FilterChainProxy(chain);
  }

  @Bean(name = "authenticationManager")
  public ProviderManager getProviderManager() {
      List<AuthenticationProvider> providers = new ArrayList<>();
      providers.add(getPreAuthenticatedAuthenticationProvider());
      return new ProviderManager(providers);
  }

  @Bean(name = "sif")
  public SecurityContextPersistenceFilter getSecurityContextPersistenceFilter() {
    return new SecurityContextPersistenceFilter();
  }

  @Bean(name = "preAuthenticatedAuthenticationProvider")
  public PreAuthenticatedAuthenticationProvider getPreAuthenticatedAuthenticationProvider() {
    PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
    provider.setPreAuthenticatedUserDetailsService(getPreAuthenticatedGrantedAuthoritiesUserDetailsService());
    return provider;
  }

  @Bean(name = "preAuthenticatedUserDetailsService")
  public PreAuthenticatedGrantedAuthoritiesUserDetailsService getPreAuthenticatedGrantedAuthoritiesUserDetailsService() {
    return new PreAuthenticatedGrantedAuthoritiesUserDetailsService();
  }

  @Bean(name = "j2eePreAuthenticatedProcessingFilter")
  public J2eePreAuthenticatedProcessingFilter getJ2eePreAuthenticatedProcessingFilter() {
    J2eePreAuthenticatedProcessingFilter filter = new J2eePreAuthenticatedProcessingFilter();
    filter.setAuthenticationManager(getProviderManager());
    filter.setAuthenticationDetailsSource(getJ2eeBasedPreAuthenticatedWebAuthenticationDetailsSource());
    return filter;
  }

  @Bean(name = "preAuthenticatedProcessingFilterEntryPoint")
  public Http403ForbiddenEntryPoint getHttp403ForbiddenEntryPoint() {
    return new Http403ForbiddenEntryPoint();
  }

  @Bean(name = "logoutFilter")
  public LogoutFilter getLogoutFilter() {
    return new LogoutFilter("/", getSecurityContextLogoutHandler());
  }

  @Bean(name = "securityContextLogoutHandler")
  public SecurityContextLogoutHandler getSecurityContextLogoutHandler() {
    return new SecurityContextLogoutHandler();
  }

  @Bean(name = "authenticationDetailsSource")
  public J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource getJ2eeBasedPreAuthenticatedWebAuthenticationDetailsSource() {
    J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource source = new J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource();
    source.setMappableRolesRetriever(getWebXmlMappableAttributesRetriever());
    source.setUserRoles2GrantedAuthoritiesMapper(getSimpleAttributes2GrantedAuthoritiesMapper());
    return source;
  }

  @Bean(name = "j2eeUserRoles2GrantedAuthoritiesMapper")
  public SimpleAttributes2GrantedAuthoritiesMapper getSimpleAttributes2GrantedAuthoritiesMapper() {
    SimpleAttributes2GrantedAuthoritiesMapper mapper = new SimpleAttributes2GrantedAuthoritiesMapper();
    mapper.setConvertAttributeToUpperCase(true);
    return mapper;
  }

  @Bean(name = "j2eeMappableRolesRetriever")
  public WebXmlMappableAttributesRetriever getWebXmlMappableAttributesRetriever() {
    return new WebXmlMappableAttributesRetriever();
  }

  @Bean(name = "etf")
  public ExceptionTranslationFilter getExceptionTranslationFilter() {
     return new ExceptionTranslationFilter(getHttp403ForbiddenEntryPoint());
  }

  @Bean(name = "httpRequestAccessDecisionManager")
  public AffirmativeBased getAffirmativeBased() {
    List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
    decisionVoters.add(getRoleVoter());

    AffirmativeBased based = new AffirmativeBased(decisionVoters);
    based.setAllowIfAllAbstainDecisions(false);
    return based;
  }

  @Bean(name = "fsi")
  public FilterSecurityInterceptor getFilterSecurityInterceptor() {
    FilterSecurityInterceptor interceptor = new FilterSecurityInterceptor();
    interceptor.setAuthenticationManager(getProviderManager());
    interceptor.setAccessDecisionManager(getAffirmativeBased());

    LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
    requestMap.put(new AntPathRequestMatcher("/adm/**"),
        SecurityConfig.createListFromCommaDelimitedString("ROLE_MANAGER,ROLE_MANAGER-GUI"));
    requestMap.put(new AntPathRequestMatcher("/adm/restartvm.ajax"),
        SecurityConfig.createListFromCommaDelimitedString("ROLE_POWERUSERPLUS,ROLE_MANAGER,ROLE_MANAGER-GUI"));
    requestMap.put(new AntPathRequestMatcher("/sql/**"),
        SecurityConfig.createListFromCommaDelimitedString("ROLE_POWERUSERPLUS,ROLE_MANAGER,ROLE_MANAGER-GUI"));
    requestMap.put(new AntPathRequestMatcher("/app/**"),
        SecurityConfig.createListFromCommaDelimitedString("ROLE_POWERUSER,ROLE_POWERUSERPLUS,ROLE_MANAGER,ROLE_MANAGER-GUI"));
    requestMap.put(new AntPathRequestMatcher("/**"),
        SecurityConfig.createListFromCommaDelimitedString("ROLE_PROBEUSER,ROLE_POWERUSER,ROLE_POWERUSERPLUS,ROLE_MANAGER,ROLE_MANAGER-GUI"));

    interceptor.setSecurityMetadataSource(new DefaultFilterInvocationSecurityMetadataSource(requestMap));
    return interceptor;
  }

  @Bean(name = "roleVoter")
  public RoleVoter getRoleVoter() {
    return new RoleVoter();
  }

  @Bean(name = "securityContextHolderAwareRequestFilter")
  public SecurityContextHolderAwareRequestFilter getSecurityContextHolderAwareRequestFilter() {
    return new SecurityContextHolderAwareRequestFilter();
  }

  @Bean(name = "httpSessionRequestCache")
  public HttpSessionRequestCache getHttpSessionRequestCache() {
    HttpSessionRequestCache cache = new HttpSessionRequestCache();
    cache.setCreateSessionAllowed(false);
    return cache;
  }

}
