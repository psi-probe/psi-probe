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

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

public abstract class AbstractSeriesProvider implements SeriesProvider {

    protected Log logger = LogFactory.getLog(getClass());

    protected XYSeries toSeries(String legend, List stats) {
        XYSeries xySeries = new XYSeries(legend, true, false);
        synchronized (stats) {
            for (int i = 0; i < stats.size(); i++) {
                XYDataItem item = (XYDataItem) stats.get(i);
                xySeries.addOrUpdate(item.getX(), item.getY());
            }
        }
        return xySeries;
    }

}
