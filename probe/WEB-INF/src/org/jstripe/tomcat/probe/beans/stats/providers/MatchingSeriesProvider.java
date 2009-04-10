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
import org.jfree.data.xy.XYDataItem;
import org.jstripe.tomcat.probe.beans.ContainerWrapperBean;
import org.jstripe.tomcat.probe.model.stats.StatsCollection;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Retrieves stats series with names that match the statNamePattern regular expression.
 * Either all matching series or only "top" N ones can be retrieved.
 * Determines the top series based on a max of each series moving avg value.
 * Derrives legend entries from series names by removing matches of the removePattern regular expression.
 * Ignores series param (sp) and legend (s...l) request parameters.
 *
 * @author Andy Shapoval
 */
public class MatchingSeriesProvider extends AbstractSeriesProvider {
    private ContainerWrapperBean containerWrapper;
    private String statNamePattern;
    private String removePattern;
    private int top = 0;
    private int moovingAvgFrame = 0;

    public ContainerWrapperBean getContainerWrapper() {
        return containerWrapper;
    }

    public void setContainerWrapper(ContainerWrapperBean containerWrapper) {
        this.containerWrapper = containerWrapper;
    }

    public String getStatNamePattern() {
        return statNamePattern;
    }

    /**
     * @param statNamePattern - stat names are matched against this regular expression.
     * Only series with matching names are retrieved.
     */
    public void setStatNamePattern(String statNamePattern) {
        this.statNamePattern = statNamePattern;
    }

    public String getRemovePattern() {
        return removePattern;
    }

    /**
     * @param removePattern - matches of this regular expression will be removed from stat names
     * in order to derive a legend entries.
     */
    public void setRemovePattern(String removePattern) {
        this.removePattern = removePattern;
    }

    public int getTop() {
        return top;
    }

    /**
     * @param top - An number of "top" series to retrieve.
     * If this value less than equals to 0, all series will be retrieved.
     */
    public void setTop(int top) {
        this.top = top;
    }

    public int getMoovingAvgFrame() {
        return moovingAvgFrame;
    }

    /**
     * @param moovingAvgFrame - if this value is greater than 0, a moving avg will be calculated for each series using
     * every Nth value, where N % moovingAvgFrame == 0. Top series will be identified based on a max moving avg
     * value of each series. If the moovingAvgFrame equals to 0, moving avg will not be calculated and top series
     * will be determined based on an avg of all series values.
     */
    public void setMoovingAvgFrame(int moovingAvgFrame) {
        this.moovingAvgFrame = moovingAvgFrame;
    }

    public void populate(DefaultTableXYDataset dataset, StatsCollection statsCollection, HttpServletRequest request) {
        if (getStatNamePattern() != null) {
            // a helper class to store each series and some relevant data
            class Entry {
                String key;
                List stats;
                double avg = 0;

                public Entry(Map.Entry me) {
                    // can pottentially create multiple identical legend entries or blank legends
                    // if the removePattern is not sellective enough
                    if ((getRemovePattern() == null)) {
                        key = (String) me.getKey();
                    } else {
                        key = ((String) me.getKey()).replaceAll(getRemovePattern(), "");
                    }

                    stats = (List) me.getValue();

                    // calculating an avg value to be used as a criteria when identifing top series.
                    // assumption: all series values are >= 0
                    if (getTop() > 0) {
                        long sum = 0;
                        int count = 1;
                        synchronized (me.getValue()) {
                            boolean moovingAvg = getMoovingAvgFrame() > 0 && getMoovingAvgFrame() < stats.size();
                            for (Iterator i = stats.iterator(); i.hasNext();) {
                                XYDataItem xy = (XYDataItem) i.next();
                                sum += xy.getY().longValue();
                                if ((moovingAvg && count % getMoovingAvgFrame() == 0) || ! i.hasNext()) {
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
            };

            Map stats = statsCollection.getMatchingStats(getStatNamePattern());
            List series1 = new ArrayList(), series2;

            for (Iterator i = stats.entrySet().iterator(); i.hasNext();) {
                series1.add(new Entry((Map.Entry) i.next()));
            }

            // identifying top series
            if (getTop() <= 0 || getTop() >= series1.size()) {
                series2 = series1;
            } else {
                Collections.sort(series1, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        Entry e1 = (Entry) o1;
                        Entry e2 = (Entry) o2;
                        return e1.avg == e2.avg ? e1.key.compareTo(e2.key) : (e1.avg > e2.avg ? -1 : 1);
                    }
                });
                series2 = series1.subList(0, getTop());
            }

            // sorting the resulting list of series by legend to make the ordering predictable
            Collections.sort(series2, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return (((Entry)o1).key).compareTo(((Entry)o2).key);
                }
            });

            for (Iterator i = series2.iterator(); i.hasNext();) {
                Entry en = (Entry) i.next();
                dataset.addSeries(toSeries(en.key, en.stats));
            }
        }
    }
}
