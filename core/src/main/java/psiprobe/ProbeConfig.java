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

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.theme.FixedThemeResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * The Class ProbeConfig.
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"psiprobe"})
public class ProbeConfig extends WebMvcConfigurerAdapter {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ProbeConfig.class);

  /**
   * Gets the internal resource view resolver.
   *
   * @return the internal resource view resolver
   */
  @Bean(name = "jspViewResolver")
  public ViewResolver getViewResolver() {
    logger.info("Instantiated internalResourceViewResolver");
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    resolver.setViewClass(JstlView.class);
    resolver.setPrefix("/WEB-INF/jsp/");
    resolver.setSuffix(".jsp");
    return resolver;
  }

  /**
   * Gets the fixed theme resolver.
   *
   * @return the fixed theme resolver
   */
  @Bean(name = "themeResolver")
  public ThemeResolver getThemeResolver() {
    logger.info("Instantiated fixedThemeResolver");
    FixedThemeResolver resolver = new FixedThemeResolver();
    resolver.setDefaultThemeName("theme-classic");
    return resolver;
  }

  /**
   * Gets the reloadable resource bundle message source.
   *
   * @return the reloadable resource bundle message source
   */
  @Bean(name = "messageSource")
  public MessageSource getMessageSource() {
    logger.info("Instantiated reloadableResourceBundleMessageSource");
    ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
    source.setBasename("/WEB-INF/messages");
    source.setCacheSeconds(1);
    source.setFallbackToSystemLocale(false);
    return source;
  }

  /**
   * Gets the cookie locale resolver.
   *
   * @return the cookie locale resolver
   */
  @Bean(name = "localeResolver")
  public LocaleResolver getLocaleResolver() {
    logger.info("Instantiated cookieLocaleResolver");
    CookieLocaleResolver resolver = new CookieLocaleResolver();
    resolver.setDefaultLocale(Locale.ENGLISH);
    return resolver;
  }

  /**
   * Gets the bean name url handler mapping.
   *
   * @return the bean name url handler mapping
   */
  @Bean(name = "handlerMapping")
  public HandlerMapping getHandlerMapping(@Autowired LocaleChangeInterceptor interceptor) {
    logger.info("Instantiated beanNameUrlHandlerMapping");
    BeanNameUrlHandlerMapping mapping = new BeanNameUrlHandlerMapping();
    mapping.setAlwaysUseFullPath(true);
    mapping.setInterceptors(new Object[] {interceptor});
    return mapping;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(getLocaleChangeInterceptor());
  }

  /**
   * Gets the locale change interceptor.
   *
   * @return the locale change interceptor
   */
  @Bean(name = "localeChangeInterceptor")
  public LocaleChangeInterceptor getLocaleChangeInterceptor() {
    logger.info("Instantiated localeChangeInterceptor");
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang");
    return interceptor;
  }

}
