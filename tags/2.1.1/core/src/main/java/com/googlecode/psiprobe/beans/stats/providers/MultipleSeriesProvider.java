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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataItem;

/**
 * Retrieves stats series with names that start with the statNamePrefix.
 * Either all matching series or only "top" N ones can be retrieved.
 * Determines top series by comparing max moving avg values.
 * Derrives legend entries from series names by removing the statNamePrefix.
 * Ignores series param (sp) and legend (s...l) request parameters.
 *
 * @author Andy Shapoval
 */
public class MultipleSeriesProvider extends AbstractSeriesProvider {
    private String statNamePrefix;
    private int top = 0;
    private int movingAvgFrame = 0;

    public String getStatNamePrefix() {
        return statNamePrefix;
    }

    /**
     * @param statNamePrefix - only series with names that start with statNamePrefix are retrieved.
     */
    public void setStatNamePrefix(String statNamePrefix) {
        this.statNamePrefix = statNamePrefix;
    }

    public int getTop() {
        return top;
    }

    /**
     * @param top - the number of top series to retrieve. If this value is greater than 0,
     * only this many series with the greatest max moving avg values are retrieved.
     */
    public void setTop(int top) {
        this.top = top;
    }

    public int getMovingAvgFrame() {
        return movingAvgFrame;
    }

    /**
     * @param movingAvgFrame - if this value is greater than 0, a moving avg value is calculated for every series using
     * every Nth value, where N % movingAvgFrame == 0. Top series are identified based on a max moving avg
     * value of each series. If the movingAvgFrame equals to 0, top series are determined based on a simple avg
     * of all series values.
     */
    public void setMovingAvgFrame(int movingAvgFrame) {
        this.movingAvgFrame = movingAvgFrame;
    }

    public void populate(DefaultTableXYDataset dataset, StatsCollection statsCollection, HttpServletRequest request) {
        Map statMap = statsCollection.getStatsByPrefix(statNamePrefix);
        boolean useTop = getTop() > 0 && getTop() < statMap.size();
        List seriesList = new ArrayList();

        for(Iterator i = statMap.entrySet().iterator(); i.hasNext();) {
            Series ser = new Series((Map.Entry) i.next());
            if (useTop) {
                ser.calculateAvg();
            }
            seriesList.add(ser);
        }

        if (useTop) {
            // sorting stats by the avg value to identify the top series
            Collections.sort(seriesList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Series s1 = (Series) o1;
                    Series s2 = (Series) o2;
                    return s1.avg == s2.avg ? s1.key.compareTo(s2.key) : (s1.avg > s2.avg ? -1 : 1);
                }
            });

            // keeping only the top series in the list
            for (ListIterator i = seriesList.listIterator(getTop()); i.hasNext();) {
                i.next();
                i.remove();
            }
        }

        // sorting the remaining series by name
        Collections.sort(seriesList, new Comparator() {
            public int compare(Object o1, Object o2) {
                return (((Series)o1).key).compareTo(((Series)o2).key);
            }
        });

        for (Iterator i = seriesList.iterator(); i.hasNext();) {
            Series ser = (Series) i.next();
            dataset.addSeries(toSeries(ser.key, ser.stats));
        }
    }

    // a helper class that holds series and calculates an avg value
    private class Series {
        final String key;
        final List stats;
        double avg = 0;

        Series(Map.Entry en) {
            key = ((String) en.getKey()).substring(statNamePrefix.length());
            stats = (List) en.getValue();
        }

        // calculating an avg value that is used for identifying the top series
        void calculateAvg() {
            long sum = 0;
            int count = 1;

            synchronized (stats) {
                boolean useMovingAvg = getMovingAvgFrame() > 0 && getMovingAvgFrame() < stats.size();

                for (Iterator i = stats.iterator(); i.hasNext();) {
                    XYDataItem xy = (XYDataItem) i.next();
                    sum += xy.getY().longValue();

                    if ((useMovingAvg && count % getMovingAvgFrame() == 0) || ! i.hasNext()) {
                        double a = (double) sum / count;
                        if (a > avg) {
                            avg = a;
                        }
                        sum = 0;
                        count = 1;
                    } else {
                        count++;
                    }
                }
            }
        }
    }
}
