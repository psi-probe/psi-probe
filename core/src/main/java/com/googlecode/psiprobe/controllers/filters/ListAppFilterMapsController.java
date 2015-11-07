/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package com.googlecode.psiprobe.controllers.filters;

import com.googlecode.psiprobe.controllers.ContextHandlerController;
import com.googlecode.psiprobe.model.FilterMapping;

import org.apache.catalina.Context;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Retrieves a list of web application filter mappings.
 * 
 * @author Andy Shapoval
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class ListAppFilterMapsController extends ContextHandlerController {

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    List<FilterMapping> filterMaps = getContainerWrapper().getTomcatContainer()
        .getApplicationFilterMaps(context);
    
    return new ModelAndView(getViewName(), "filterMaps", filterMaps);
  }

}
