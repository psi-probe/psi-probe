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
package com.googlecode.psiprobe.controllers;

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
import org.apache.catalina.Context;
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

        // as of now detailed datasource info including URL is available for private resources only

        if (getContainerWrapper().getResourceResolver().supportsPrivateResources()) {
            List dataSources = new ArrayList();
            List apps = getContainerWrapper().getTomcatContainer().findContexts();

            for (Iterator i = apps.iterator(); i.hasNext(); ) {
                Context app = (Context) i.next();
                List appResources = getContainerWrapper().getResourceResolver().getApplicationResources(app);

                // filter out anything that is not a datasource
                // and use only those datasources that are properly configured
                // as aggregated totals would not make any sence otherwise

                for (Iterator j = appResources.iterator(); j.hasNext(); ) {
                    ApplicationResource res = (ApplicationResource) j.next();

                    if (res.getDataSourceInfo() != null && res.isLookedUp() &&
                        res.getDataSourceInfo().getJdbcURL() != null) {
                        dataSources.add(res.getDataSourceInfo());
                    }
                }
            }

            Collections.sort(dataSources, new Comparator() {
                public int compare(Object o1, Object o2) {
                    String jdbcURL1 = ((DataSourceInfo) o1).getJdbcURL();
                    String jdbcURL2 = ((DataSourceInfo) o2).getJdbcURL();

                    // here we rely on the the code above for not to add any
                    // datasources with jdbcUrl == null to the list

                    return jdbcURL1.compareToIgnoreCase(jdbcURL2);
                }
            });

            // group datasources by JdbsURL and calculate aggregated totals

            DataSourceInfoGroup dsGroup = null;
            boolean firstGroup = true;

            for (Iterator i = dataSources.iterator(); i.hasNext();) {
                DataSourceInfo ds = (DataSourceInfo) i.next();

                if (firstGroup) {
                    dsGroup = new DataSourceInfoGroup(ds);
                    dataSourceGroups.add(dsGroup);
                    firstGroup = false;
                } else {
                    if (dsGroup.getJdbcURL().equalsIgnoreCase(ds.getJdbcURL())) {
                        dsGroup.addDataSourceInfo(ds);
                    } else {
                        dsGroup = new DataSourceInfoGroup(ds);
                        dataSourceGroups.add(dsGroup);
                    }
                }
            }
        }

        return new ModelAndView(getViewName(), "dataSourceGroups", dataSourceGroups);
    }
}
