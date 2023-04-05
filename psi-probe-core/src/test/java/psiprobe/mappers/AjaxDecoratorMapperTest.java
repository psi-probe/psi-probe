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
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Page;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Properties;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Class AjaxDecoratorMapperTest.
 */
class AjaxDecoratorMapperTest {

  /** The mapper. */
  AjaxDecoratorMapper mapper;

  /** The config. */
  @Mocked
  Config config;

  /** The properties. */
  Properties properties;

  /** The decorator mapper. */
  @Mocked
  DecoratorMapper decoratorMapper;

  /** The request. */
  @Mocked
  HttpServletRequest request;

  /** The page. */
  @Mocked
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

    new Expectations() {
      {
        request.getAttribute("javax.servlet.error.request_uri");
        result = "https://localhost:8443/probe";

        request.getServletPath();
        result = "probe/ws";
      }
    };
    Assertions.assertNotNull(mapper.getDecorator(request, page));
  }

}
