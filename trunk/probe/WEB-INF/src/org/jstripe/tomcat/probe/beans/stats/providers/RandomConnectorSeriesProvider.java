/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://probe.jstripe.com/d/license.shtml
 *
 *  THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 *  WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jstripe.tomcat.probe.beans.stats.providers;

import org.jfree.data.xy.DefaultTableXYDataset;
import org.jstripe.tomcat.probe.model.stats.StatsCollection;
import org.springframework.web.bind.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class RandomConnectorSeriesProvider extends AbstractSeriesProvider {

    public void populate(DefaultTableXYDataset dataset, StatsCollection statsCollection, HttpServletRequest request) {
        //
        // get Connector name from the request
        //
        String connectorName = RequestUtils.getStringParameter(request, "cn", null);
        //
        // type of statistic to be displayed
        //
        String statType = RequestUtils.getStringParameter(request, "st", null);
        //
        // Series legend
        //
        String series1Legend = RequestUtils.getStringParameter(request, "sl", "");


        if (connectorName != null && statType != null) {
            List l = statsCollection.getStats("stat.connector." + connectorName + "."+statType);
            if (l != null) {
                dataset.addSeries(toSeries(series1Legend, l));
            }
        }
    }
}
