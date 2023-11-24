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
package psiprobe.controllers.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import psiprobe.beans.RuntimeInfoAccessorBean;
import psiprobe.controllers.AbstractTomcatContainerController;
import psiprobe.model.SystemInformation;
import psiprobe.tools.SecurityUtils;

/**
 * Creates an instance of SystemInformation.
 */
public class BaseSysInfoController extends AbstractTomcatContainerController {

  /** The filter out keys. */
  private List<String> filterOutKeys = new ArrayList<>();

  /** The runtime info accessor. */
  @Inject
  private RuntimeInfoAccessorBean runtimeInfoAccessor;

  /** The collection period. */
  private long collectionPeriod;

  /**
   * Gets the filter out keys.
   *
   * @return the filter out keys
   */
  public List<String> getFilterOutKeys() {
    return filterOutKeys;
  }

  /**
   * Sets the filter out keys.
   *
   * @param filterOutKeys the new filter out keys
   */
  public void setFilterOutKeys(List<String> filterOutKeys) {
    this.filterOutKeys = filterOutKeys;
  }

  /**
   * Gets the runtime info accessor.
   *
   * @return the runtime info accessor
   */
  public RuntimeInfoAccessorBean getRuntimeInfoAccessor() {
    return runtimeInfoAccessor;
  }

  /**
   * Sets the runtime info accessor.
   *
   * @param runtimeInfoAccessor the new runtime info accessor
   */
  public void setRuntimeInfoAccessor(RuntimeInfoAccessorBean runtimeInfoAccessor) {
    this.runtimeInfoAccessor = runtimeInfoAccessor;
  }

  /**
   * Gets the collection period.
   *
   * @return the collection period
   */
  public long getCollectionPeriod() {
    return collectionPeriod;
  }

  /**
   * Sets the collection period.
   *
   * @param collectionPeriod the new collection period
   */
  public void setCollectionPeriod(long collectionPeriod) {
    this.collectionPeriod = collectionPeriod;
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    SystemInformation systemInformation = new SystemInformation();
    systemInformation
        .setAppBase(getContainerWrapper().getTomcatContainer().getAppBase().getAbsolutePath());
    systemInformation.setConfigBase(getContainerWrapper().getTomcatContainer().getConfigBase());

    Map<String, String> sysProps = new HashMap<>();
    for (String propertyName : System.getProperties().stringPropertyNames()) {
      String propertyValue = System.getProperty(propertyName);
      sysProps.put(propertyName, propertyValue);
    }

    if (!SecurityUtils.hasAttributeValueRole(getServletContext())) {
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
