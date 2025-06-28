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
package psiprobe.mappers;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Page;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * The Class AjaxDecoratorMapperTest.
 */
@ExtendWith(MockitoExtension.class)
class AjaxDecoratorMapperTest {

  /** The mapper. */
  AjaxDecoratorMapper mapper;

  /** The config. */
  @Mock
  Config config;

  /** The properties. */
  Properties properties;

  /** The decorator mapper. */
  @Mock
  DecoratorMapper decoratorMapper;

  /** The request. */
  @Mock
  HttpServletRequest request;

  /** The page. */
  @Mock
  Page page;

  /**
   * Before.
   */
  @BeforeEach
  void before() {
    mapper = new AjaxDecoratorMapper();
    properties = new Properties();
  }

  /**
   * Ajax decorator mapper test.
   *
   * @throws InstantiationException the instantiation exception
   */
  @Test
  void ajaxDecoratorMapperTest() throws InstantiationException {
    properties.setProperty("ajaxExtension", ".ajax");
    mapper.init(config, properties, decoratorMapper);

    Mockito.when(request.getAttribute("jakarta.servlet.error.request_uri"))
        .thenReturn("https://localhost:8443/probe");
    Mockito.when(request.getServletPath()).thenReturn("probe/ws");

    // Mock the decoratorMapper to return a non-null decorator
    var decorator = Mockito.mock(Decorator.class);
    Mockito.when(decoratorMapper.getDecorator(request, page)).thenReturn(decorator);

    Assertions.assertNotNull(mapper.getDecorator(request, page));
  }

  /**
   * Test without ajax extension property.
   *
   * @throws InstantiationException the instantiation exception
   */
  @Test
  void testWithoutAjaxExtensionProperty() throws InstantiationException {
    mapper.init(config, properties, decoratorMapper);

    Mockito.when(request.getAttribute("jakarta.servlet.error.request_uri"))
        .thenReturn("https://localhost:8443/probe");
    Mockito.when(request.getServletPath()).thenReturn("probe/ws");

    // Mock the decoratorMapper to return a non-null decorator
    var decorator = Mockito.mock(Decorator.class);
    Mockito.when(decoratorMapper.getDecorator(request, page)).thenReturn(decorator);

    Assertions.assertNotNull(mapper.getDecorator(request, page));
  }

  /**
   * Test null request uri.
   *
   * @throws InstantiationException the instantiation exception
   */
  @Test
  void testNullRequestUri() throws InstantiationException {
    properties.setProperty("ajaxExtension", ".ajax");
    mapper.init(config, properties, decoratorMapper);

    Mockito.when(request.getAttribute("jakarta.servlet.error.request_uri")).thenReturn(null);
    Mockito.when(request.getServletPath()).thenReturn("probe/ws");

    // Mock the decoratorMapper to return a non-null decorator
    var decorator = Mockito.mock(Decorator.class);
    Mockito.when(decoratorMapper.getDecorator(request, page)).thenReturn(decorator);

    Assertions.assertNotNull(mapper.getDecorator(request, page));
  }

}
