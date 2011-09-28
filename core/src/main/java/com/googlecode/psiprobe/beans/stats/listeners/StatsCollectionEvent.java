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
