/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.jstripe.tomcat.probe.beans.stats.providers;

import org.jfree.data.xy.DefaultTableXYDataset;
import org.jstripe.tomcat.probe.model.stats.StatsCollection;

import javax.servlet.http.HttpServletRequest;

/**
 * Classes implementing this interface can be wired up with RenderChartController to provide Series data based on
 * StatsCollection instance.
 *
 * Author: Vlad Ilyushchenko
 */
public interface SeriesProvider {
    void populate(DefaultTableXYDataset dataset, StatsCollection statsCollection, HttpServletRequest request);
}
