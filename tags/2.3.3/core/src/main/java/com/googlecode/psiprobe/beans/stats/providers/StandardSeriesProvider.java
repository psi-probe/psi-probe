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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.springframework.web.bind.ServletRequestUtils;

public class StandardSeriesProvider extends AbstractSeriesProvider {

    private List statNames = new ArrayList(2);

    public List getStatNames() {
        return statNames;
    }

    public void setStatNames(List statNames) {
        this.statNames = statNames;
    }

    public void populate(DefaultTableXYDataset dataset, StatsCollection statsCollection, HttpServletRequest request) {
        String seriesParam = ServletRequestUtils.getStringParameter(request, "sp", null);
        for (int i = 0; i < statNames.size(); i++) {
            String statName = (String) statNames.get(i);
            if (seriesParam != null) {
                statName = MessageFormat.format(statName, new Object[]{seriesParam});
            }
            List l = statsCollection.getStats(statName);
            if (l != null) {
                dataset.addSeries(
                        toSeries(ServletRequestUtils.getStringParameter(request, "s" + (i + 1) + "l", "series" + i), l)
                );
            }
        }
    }
}
