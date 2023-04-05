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
import com.opensymphony.module.sitemesh.mapper.AbstractDecoratorMapper;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Properties;

/**
 * The AjaxDecoratorMapper will exclude all "ajax" requests from being decorated. It will also make
 * sure that error pages rendered during ajax request execution are not decorated.
 */
public class AjaxDecoratorMapper extends AbstractDecoratorMapper {

  /** The ajax extension. */
  private String ajaxExtension = ".ajax";

  @Override
  public void init(Config config, Properties properties, DecoratorMapper decoratorMapper)
      throws InstantiationException {

    super.init(config, properties, decoratorMapper);
    if (properties.get("ajaxExtension") != null) {
      ajaxExtension = (String) properties.get("ajaxExtension");
    }
  }

  @Override
  public Decorator getDecorator(HttpServletRequest request, Page page) {

    boolean callMapperChain;
    String originalUri = (String) request.getAttribute("javax.servlet.error.request_uri");
    if (originalUri != null) {
      //
      // cut off the query string
      //
      int queryStringIndex = originalUri.indexOf('?');
      if (queryStringIndex != -1) {
        originalUri = originalUri.substring(0, queryStringIndex);
      }
    }
    callMapperChain = (originalUri == null || !originalUri.endsWith(ajaxExtension))
        && !request.getServletPath().endsWith(ajaxExtension);

    return callMapperChain ? super.getDecorator(request, page) : null;
  }

}
