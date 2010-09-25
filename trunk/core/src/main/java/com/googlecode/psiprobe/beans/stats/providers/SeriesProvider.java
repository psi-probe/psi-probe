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
import javax.servlet.http.HttpServletRequest;
import org.jfree.data.xy.DefaultTableXYDataset;

/**
 * Classes implementing this interface can be wired up with
 * RenderChartController to provide Series data based on StatsCollection
 * instance.
 *
 * @author Vlad Ilyushchenko
 */
public interface SeriesProvider {
    void populate(DefaultTableXYDataset dataset, StatsCollection statsCollection, HttpServletRequest request);
}
