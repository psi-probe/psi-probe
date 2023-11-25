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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.theme.FixedThemeResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import psiprobe.beans.ClusterWrapperBean;
import psiprobe.beans.ContainerListenerBean;
import psiprobe.beans.ContainerWrapperBean;
import psiprobe.beans.JBossResourceResolverBean;
import psiprobe.beans.JvmMemoryInfoAccessorBean;
import psiprobe.beans.LogResolverBean;
import psiprobe.beans.ResourceResolver;
import psiprobe.beans.ResourceResolverBean;
import psiprobe.beans.RuntimeInfoAccessorBean;
import psiprobe.tools.Mailer;

/**
 * The Class ProbeConfig.
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"psiprobe"})
public class ProbeConfig implements WebMvcConfigurer {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ProbeConfig.class);

  /**
   * Gets the container listener bean.
   *
   * @return the container listener bean
   */
  @Bean(name = "containerListener")
  public ContainerListenerBean getContainerListenerBean() {
    logger.debug("Instantiated containerListener");
    return new ContainerListenerBean();
  }

  /**
   * Gets the container wrapper bean.
   *
   * @return the container wrapper bean
   */
  @Bean(name = "containerWrapper")
  public ContainerWrapperBean getContainerWrapperBean() {
    logger.debug("Instantiated containerWrapper");
    return new ContainerWrapperBean();
  }

  /**
   * Gets the cluster wrapper bean.
   *
   * @return the cluster wrapper bean
   */
  @Bean(name = "clusterWrapper")
  public ClusterWrapperBean getClusterWrapperBean() {
    logger.debug("Instantiated clusterWrapper");
    return new ClusterWrapperBean();
  }

  /**
   * Gets the mailer.
   *
   * @return the mailer
   */
  @Bean(name = "mailer")
  public Mailer getMailer() {
    logger.debug("Instantiated mailer");
    return new Mailer();
  }

  /**
   * Gets the default res.
   *
   * @return the default res
   */
  @Bean(name = "datasourceMappers")
  public List<String> getDefaultRes() {
    logger.debug("Instantiated datasourceMappers");
    List<String> list = new ArrayList<>();
    list.add("psiprobe.beans.accessors.C3P0DatasourceAccessor");
    list.add("psiprobe.beans.accessors.Dbcp2DatasourceAccessor");
    list.add("psiprobe.beans.accessors.HikariCpDatasourceAccessor");
    list.add("psiprobe.beans.accessors.OracleDatasourceAccessor");
    list.add("psiprobe.beans.accessors.OracleUcpDatasourceAccessor");
    list.add("psiprobe.beans.accessors.OpenEjbBasicDatasourceAccessor");
    list.add("psiprobe.beans.accessors.OpenEjbManagedDatasourceAccessor");
    list.add("psiprobe.beans.accessors.Tomcat85DbcpDatasourceAccessor");
    list.add("psiprobe.beans.accessors.Tomcat9DbcpDatasourceAccessor");
    /* For jakarta
      list.add("psiprobe.beans.accessors.Tomcat10DbcpDatasourceAccessor");
      list.add("psiprobe.beans.accessors.Tomcat11DbcpDatasourceAccessor");
    */
    list.add("psiprobe.beans.accessors.TomcatJdbcPoolDatasourceAccessor");
    list.add("psiprobe.beans.accessors.TomEeJdbcPoolDatasourceAccessor");
    list.add("psiprobe.beans.accessors.ViburCpDatasourceAccessor");
    return list;
  }

  /**
   * Gets the resource resolver bean.
   *
   * @return the resource resolver bean
   */
  @Bean(name = "default")
  public ResourceResolverBean getResourceResolverBean() {
    logger.debug("Instantiated default resourceResolverBean");
    return new ResourceResolverBean();
  }

  /**
   * Gets the jboss resource resolver bean.
   *
   * @return the jboss resource resolver bean
   */
  @Bean(name = "jboss")
  public JBossResourceResolverBean getJBossResourceResolverBean() {
    logger.debug("Instantiated jbossResourceResolverBean");
    return new JBossResourceResolverBean();
  }

  /**
   * Gets the resource resolvers.
   *
   * @param jbossResourceResolverBean the jboss resource resolver bean
   * @param resourceResolverBean the resource resolver bean
   *
   * @return the resource resolvers
   */
  @Bean(name = "resourceResolvers")
  public Map<String, ResourceResolver> getResourceResolvers(
      @Autowired JBossResourceResolverBean jbossResourceResolverBean,
      @Autowired ResourceResolverBean resourceResolverBean) {
    logger.debug("Instantiated resourceResolvers");
    Map<String, ResourceResolver> map = new HashMap<>();
    map.put("jboss", jbossResourceResolverBean);
    map.put("default", resourceResolverBean);
    return map;
  }

  /**
   * Gets the adapter classes.
   *
   * @return the adapter classes
   */
  @Bean(name = "adapterClasses")
  public List<String> getAdapterClasses() {
    logger.debug("Instantiated adapterClasses");
    List<String> list = new ArrayList<>();
    try {
      Properties properties = adapters().getObject();
      if (properties == null) {
        return Collections.emptyList();
      }
      for (Object adapter : properties.values()) {
        list.add((String) adapter);
      }
    } catch (Exception e) {
      logger.error("", e);
    }
    return list;
  }

  /**
   * Gets the stdout files. Any file added to this list will be displayed.
   *
   * @return the stdout files
   */
  @Bean(name = "stdoutFiles")
  public List<String> getStdoutFiles() {
    logger.debug("Instantiated stdoutFiles");
    List<String> list = new ArrayList<>();
    try {
      Properties properties = stdout().getObject();
      if (properties == null) {
        return Collections.emptyList();
      }
      for (Object stdout : properties.values()) {
        list.add((String) stdout);
      }
    } catch (Exception e) {
      logger.error("", e);
    }
    return list;
  }

  /**
   * Standard Out Properties.
   *
   * @return the properties factory bean for standard out
   */
  @Bean(name = "stdout")
  public FactoryBean<Properties> stdout() {
    logger.debug("Instantiated stdout");
    PropertiesFactoryBean bean = new PropertiesFactoryBean();
    bean.setLocation(new ClassPathResource("stdout.properties"));
    return bean;
  }

  /**
   * Adapters Properties.
   *
   * @return the properties factory bean for adaptors
   */
  @Bean(name = "adapters")
  public FactoryBean<Properties> adapters() {
    logger.debug("Instantiated adapters");
    PropertiesFactoryBean bean = new PropertiesFactoryBean();
    bean.setLocation(new ClassPathResource("adapters.properties"));
    return bean;
  }

  /**
   * Gets the log resolver bean.
   *
   * @return the log resolver bean
   */
  @Bean(name = "logResolver")
  public LogResolverBean getLogResolverBean() {
    logger.debug("Instantiated logResolver");
    return new LogResolverBean();
  }

  /**
   * Gets the jvm memory info accessor bean.
   *
   * @return the jvm memory info accessor bean
   */
  @Bean(name = "jvmMemoryInfoAccessor")
  public JvmMemoryInfoAccessorBean getJvmMemoryInfoAccessorBean() {
    logger.debug("Instantiated jvmMemoryInfoAccessorBean");
    return new JvmMemoryInfoAccessorBean();
  }

  /**
   * Gets the runtime info accessor bean.
   *
   * @return the runtime info accessor bean
   */
  @Bean(name = "runtimeInfoAccessor")
  public RuntimeInfoAccessorBean getRuntimeInfoAccessorBean() {
    logger.debug("Instantiated runtimeInfoAccessorBean");
    return new RuntimeInfoAccessorBean();
  }

  /**
   * Gets the internal resource view resolver.
   *
   * @return the internal resource view resolver
   */
  @Bean(name = "jspViewResolver")
  public ViewResolver getViewResolver() {
    logger.debug("Instantiated internalResourceViewResolver");
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
    logger.debug("Instantiated fixedThemeResolver");
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
    logger.debug("Instantiated reloadableResourceBundleMessageSource");
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
    logger.debug("Instantiated cookieLocaleResolver");
    CookieLocaleResolver resolver = new CookieLocaleResolver();
    resolver.setDefaultLocale(Locale.ENGLISH);
    resolver.setCookieSecure(true);
    resolver.setCookieHttpOnly(true);
    return resolver;
  }

  /**
   * Gets the bean name url handler mapping.
   *
   * @param interceptor the interceptor
   *
   * @return the bean name url handler mapping
   */
  @Bean(name = "handlerMapping")
  public HandlerMapping getHandlerMapping(@Autowired LocaleChangeInterceptor interceptor) {
    logger.debug("Instantiated beanNameUrlHandlerMapping");
    BeanNameUrlHandlerMapping mapping = new BeanNameUrlHandlerMapping();
    mapping.setAlwaysUseFullPath(true);
    mapping.setInterceptors(interceptor);
    return mapping;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    logger.debug("Registering localeChangeInterceptor");
    registry.addInterceptor(getLocaleChangeInterceptor());
  }

  /**
   * Gets the locale change interceptor.
   *
   * @return the locale change interceptor
   */
  @Bean(name = "localeChangeInterceptor")
  public LocaleChangeInterceptor getLocaleChangeInterceptor() {
    logger.debug("Instantiated localeChangeInterceptor");
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang");
    return interceptor;
  }

  /**
   * Gets the property sources placeholder configurer.
   *
   * @return the property sources placeholder configurer
   */
  @Bean(name = "propertySourcesPlaceholderConfigurer")
  public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
    logger.debug("Instantiated propertySourcesPlaceholderConfigurer");
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    configurer.setLocation(new ClassPathResource("stats.properties"));
    configurer.setNullValue("NULL");

    Properties properties = new Properties();
    properties.put("psiprobe.tools.mail.to", "NULL");
    properties.put("psiprobe.tools.mail.subjectPrefix", "[PSI Probe]");
    configurer.setProperties(properties);
    return configurer;
  }

  /**
   * Version.
   *
   * @return the properties factory bean
   */
  @Bean(name = "version")
  public PropertiesFactoryBean version() {
    logger.debug("Instantiated version");
    PropertiesFactoryBean bean = new PropertiesFactoryBean();
    bean.setLocation(new ClassPathResource("version.properties"));
    return bean;
  }

}
