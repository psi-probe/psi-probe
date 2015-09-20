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

package com.googlecode.psiprobe.controllers.system;

import com.googlecode.psiprobe.beans.RuntimeInfoAccessorBean;
import com.googlecode.psiprobe.controllers.TomcatContainerController;
import com.googlecode.psiprobe.model.SystemInformation;
import com.googlecode.psiprobe.tools.SecurityUtils;

import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Creates an instance of SystemInformation POJO.
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class SysInfoController extends TomcatContainerController {

  private List<String> filterOutKeys = new ArrayList<String>();
  private RuntimeInfoAccessorBean runtimeInfoAccessor;
  private long collectionPeriod;

  public List<String> getFilterOutKeys() {
    return filterOutKeys;
  }

  public void setFilterOutKeys(List<String> filterOutKeys) {
    this.filterOutKeys = filterOutKeys;
  }

  public RuntimeInfoAccessorBean getRuntimeInfoAccessor() {
    return runtimeInfoAccessor;
  }

  public void setRuntimeInfoAccessor(RuntimeInfoAccessorBean runtimeInfoAccessor) {
    this.runtimeInfoAccessor = runtimeInfoAccessor;
  }

  public long getCollectionPeriod() {
    return collectionPeriod;
  }

  public void setCollectionPeriod(long collectionPeriod) {
    this.collectionPeriod = collectionPeriod;
  }

  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    SystemInformation systemInformation = new SystemInformation();
    systemInformation.setAppBase(getContainerWrapper().getTomcatContainer().getAppBase()
        .getAbsolutePath());
    systemInformation.setConfigBase(getContainerWrapper().getTomcatContainer().getConfigBase());

    Map<String, String> sysProps = new HashMap<String, String>();
    for (String propertyName : System.getProperties().stringPropertyNames()) {
      String propertyValue = System.getProperties().getProperty(propertyName);
      sysProps.put(propertyName, propertyValue);
    }

    if (!SecurityUtils.hasAttributeValueRole(getServletContext(), request)) {
      for (String key : filterOutKeys) {
        sysProps.remove(key);
      }
    }

    systemInformation.setSystemProperties(sysProps);

    ModelAndView mv = new ModelAndView(getViewName());
    mv.addObject("systemInformation", systemInformation);
    mv.addObject("runtime", getRuntimeInfoAccessor().getRuntimeInformation());
    mv.addObject("collectionPeriod", getCollectionPeriod());
    return mv;
  }

}
