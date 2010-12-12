/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.mappers;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.mapper.AbstractDecoratorMapper;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;

/**
 * The AjaxDecoratorMapper will exclude all "ajax" requests from being decorated. It will also make sure
 * that error pages rendered during ajax request execution are not decorated.
 *
 * @author Vlad Ilyushchenko
 */
public class AjaxDecoratorMapper extends AbstractDecoratorMapper {
    private String ajaxExtension = ".ajax";

    public void init(Config config, Properties properties, DecoratorMapper decoratorMapper) throws InstantiationException {
        super.init(config, properties, decoratorMapper);
        if (properties.get("ajaxExtension") != null) {
            ajaxExtension = (String) properties.get("ajaxExtension");
        }
    }

    public Decorator getDecorator(HttpServletRequest request, Page page) {

        boolean callMapperChain;
        String originalURI = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (originalURI != null) {
            //
            // cut off the query string
            //
            int qIdx = originalURI.indexOf("?");
            if (qIdx != -1) {
                originalURI = originalURI.substring(0, qIdx);
            }
        }
        callMapperChain =  (originalURI == null || !originalURI.endsWith(ajaxExtension)) &&
                (!request.getServletPath().endsWith(ajaxExtension));

        return callMapperChain ? super.getDecorator(request, page) : null;
    }
}
