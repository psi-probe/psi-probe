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

package com.googlecode.psiprobe.controllers.datasources;

import com.googlecode.psiprobe.controllers.TomcatContainerController;
import com.googlecode.psiprobe.model.ApplicationResource;

import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Creates a list of all configured datasources for all web applications within the container.
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class ListAllJdbcResourcesController extends TomcatContainerController {

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse httpServletResponse) throws Exception {

    boolean supportsGlobal = getContainerWrapper().getResourceResolver().supportsGlobalResources();
    boolean supportsPrivate =
        getContainerWrapper().getResourceResolver().supportsPrivateResources();
    boolean supportsDataSourceLookup =
        getContainerWrapper().getResourceResolver().supportsDataSourceLookup();
    List<ApplicationResource> privateResources = getContainerWrapper().getPrivateDataSources();
    List<ApplicationResource> globalResources = getContainerWrapper().getGlobalDataSources();
    return new ModelAndView(getViewName())
        .addObject("supportsGlobal", supportsGlobal)
        .addObject("supportsPrivate", supportsPrivate)
        .addObject("supportsDSLookup", supportsDataSourceLookup)
        .addObject("privateResources", privateResources)
        .addObject("globalResources", globalResources);
  }

}
