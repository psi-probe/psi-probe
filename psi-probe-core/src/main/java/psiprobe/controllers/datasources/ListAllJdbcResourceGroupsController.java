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
package psiprobe.controllers.datasources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.AbstractTomcatContainerController;
import psiprobe.model.ApplicationResource;
import psiprobe.model.DataSourceInfo;
import psiprobe.model.DataSourceInfoGroup;

/**
 * Produces a list of all datasources configured within the container grouped by JDBC URL.
 */
@Controller
public class ListAllJdbcResourceGroupsController extends AbstractTomcatContainerController {

  @RequestMapping(path = "/datasourcegroups.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    List<DataSourceInfoGroup> dataSourceGroups = new ArrayList<>();
    List<DataSourceInfo> dataSources = new ArrayList<>();

    List<ApplicationResource> privateResources = getContainerWrapper().getPrivateDataSources();
    List<ApplicationResource> globalResources = getContainerWrapper().getGlobalDataSources();

    // filter out anything that is not a datasource
    // and use only those datasources that are properly configured
    // as aggregated totals would not make any sense otherwise
    filterValidDataSources(privateResources, dataSources);
    filterValidDataSources(globalResources, dataSources);

    // sort datasources by JDBC URL
    Collections.sort(dataSources, (ds1, ds2) -> {
      String jdbcUrl1 = ds1.getJdbcUrl();
      String jdbcUrl2 = ds2.getJdbcUrl();

      // here we rely on the the filter not to add any datasources with a null jdbcUrl to the list

      return jdbcUrl1.compareToIgnoreCase(jdbcUrl2);
    });

    // group datasources by JDBC URL and calculate aggregated totals
    DataSourceInfoGroup dsGroup = null;
    for (DataSourceInfo ds : dataSources) {
      if (dsGroup == null || !dsGroup.getJdbcUrl().equalsIgnoreCase(ds.getJdbcUrl())) {
        dsGroup = new DataSourceInfoGroup().builder(ds);
        dataSourceGroups.add(dsGroup);
      } else {
        dsGroup.addDataSourceInfo(ds);
      }
    }

    return new ModelAndView(getViewName(), "dataSourceGroups", dataSourceGroups);
  }

  /**
   * Filter valid data sources.
   *
   * @param resources the resources
   * @param dataSources the data sources
   */
  protected void filterValidDataSources(Collection<ApplicationResource> resources,
      Collection<DataSourceInfo> dataSources) {

    for (ApplicationResource res : resources) {
      if (res.isLookedUp() && res.getDataSourceInfo() != null
          && res.getDataSourceInfo().getJdbcUrl() != null) {
        dataSources.add(res.getDataSourceInfo());
      }
    }
  }

  @Value("datasourcegroup")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
