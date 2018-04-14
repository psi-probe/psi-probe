/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.beans.stats.providers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataItem;
import org.springframework.web.bind.ServletRequestUtils;
import psiprobe.model.stats.StatsCollection;

/**
 * The Class ConnectorSeriesProvider.
 */
public class ConnectorSeriesProvider extends AbstractSeriesProvider {

  @Override
  public void populate(DefaultTableXYDataset dataset, StatsCollection statsCollection,
      HttpServletRequest request) {

    // get Connector name from the request
    String connectorName = ServletRequestUtils.getStringParameter(request, "cn", null);

    // type of statistic to be displayed
    String statType = ServletRequestUtils.getStringParameter(request, "st", null);

    // Series legend
    String series1Legend = ServletRequestUtils.getStringParameter(request, "sl", "");

    if (connectorName != null && statType != null) {
      List<XYDataItem> stats =
          statsCollection.getStats("stat.connector." + connectorName + "." + statType);
      if (stats != null) {
        dataset.addSeries(toSeries(series1Legend, stats));
      }
    }
  }
}
