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
package com.googlecode.psiprobe.beans.stats.listeners;

import org.jfree.data.xy.XYDataItem;

/**
 *
 * @author Mark Lewis
 */
public class StatsCollectionEvent {

    private String name;
    private XYDataItem data;

    public StatsCollectionEvent() {
    }

    public StatsCollectionEvent(String name, XYDataItem data) {
        this.name = name;
        this.data = data;
    }

    public StatsCollectionEvent(String name, long x, long y) {
        this(name, new XYDataItem(x, y));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public XYDataItem getData() {
        return data;
    }

    public void setData(XYDataItem data) {
        this.data = data;
    }

    public long getValue() {
        return getData().getY().longValue();
    }

    public long getTime() {
        return getData().getX().longValue();
    }

}
