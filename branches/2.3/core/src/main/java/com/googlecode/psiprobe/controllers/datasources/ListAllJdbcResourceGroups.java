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
package com.googlecode.psiprobe.controllers.datasources;

import com.googlecode.psiprobe.controllers.TomcatContainerController;
import com.googlecode.psiprobe.model.ApplicationResource;
import com.googlecode.psiprobe.model.DataSourceInfo;
import com.googlecode.psiprobe.model.DataSourceInfoGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Produces a list of all datasources configured within the container grouped by
 * JDBC URL.
 *
 * @author Andy Shapoval
 */

public class ListAllJdbcResourceGroups extends TomcatContainerController {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List dataSourceGroups = new ArrayList();
        List dataSources = new ArrayList();
        
        List privateResources = getContainerWrapper().getPrivateDataSources();
        List globalResources = getContainerWrapper().getGlobalDataSources();

        // filter out anything that is not a datasource
        // and use only those datasources that are properly configured
        // as aggregated totals would not make any sense otherwise
        filterValidDataSources(privateResources, dataSources);
        filterValidDataSources(globalResources, dataSources);

        // sort datasources by JDBC URL
        Collections.sort(dataSources, new Comparator() {
            public int compare(Object o1, Object o2) {
                String jdbcURL1 = ((DataSourceInfo) o1).getJdbcURL();
                String jdbcURL2 = ((DataSourceInfo) o2).getJdbcURL();

                // here we rely on the the filter not to add any
                // datasources with a null jdbcUrl to the list

                return jdbcURL1.compareToIgnoreCase(jdbcURL2);
            }
        });

        // group datasources by JDBC URL and calculate aggregated totals
        DataSourceInfoGroup dsGroup = null;
        for (Iterator i = dataSources.iterator(); i.hasNext();) {
            DataSourceInfo ds = (DataSourceInfo) i.next();

            if (dsGroup == null || !dsGroup.getJdbcURL().equalsIgnoreCase(ds.getJdbcURL())) {
                dsGroup = new DataSourceInfoGroup(ds);
                dataSourceGroups.add(dsGroup);
            } else {
                dsGroup.addDataSourceInfo(ds);
            }
        }

        return new ModelAndView(getViewName(), "dataSourceGroups", dataSourceGroups);
    }

    protected void filterValidDataSources(List resources, List dataSources) {
        for (Iterator i = resources.iterator(); i.hasNext(); ) {
            ApplicationResource res = (ApplicationResource) i.next();
            if (res.isLookedUp()
                    && res.getDataSourceInfo() != null
                    && res.getDataSourceInfo().getJdbcURL() != null) {
                dataSources.add(res.getDataSourceInfo());
            }
        }
    }

}
