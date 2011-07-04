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
package com.googlecode.psiprobe.beans.stats.providers;

import com.googlecode.psiprobe.model.stats.StatsCollection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.springframework.web.bind.ServletRequestUtils;

public class ConnectorSeriesProvider extends AbstractSeriesProvider {

    public void populate(DefaultTableXYDataset dataset, StatsCollection statsCollection, HttpServletRequest request) {
        //
        // get Connector name from the request
        //
        String connectorName = ServletRequestUtils.getStringParameter(request, "cn", null);
        //
        // type of statistic to be displayed
        //
        String statType = ServletRequestUtils.getStringParameter(request, "st", null);
        //
        // Series legend
        //
        String series1Legend = ServletRequestUtils.getStringParameter(request, "sl", "");


        if (connectorName != null && statType != null) {
            List l = statsCollection.getStats("stat.connector." + connectorName + "." + statType);
            if (l != null) {
                dataset.addSeries(toSeries(series1Legend, l));
            }
        }
    }
}
