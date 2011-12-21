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

/**
 *
 * @author Mark Lewis
 */
public abstract class AbstractStatsCollectionListener implements StatsCollectionListener {

    private String propertyCategory;

    protected String getPropertyValue(String name, String attribute) {
        String value = getPropertyValue(getPropertyKey(name, attribute));
        if (value == null) {
            value = getPropertyValue(getPropertyKey(null, attribute));
        }
        if (value == null) {
            value = getPropertyValue(getPropertyKey(null, null, attribute));
        }
        return value;
    }

    protected String getPropertyKey(String name, String attribute) {
        return getPropertyKey(getPropertyCategory(), name, attribute);
    }

    private String getPropertyKey(String category, String name, String attribute) {
        String result = AbstractStatsCollectionListener.class.getPackage().getName();
        if (category != null) {
            result += '.' + category;
        }
        if (name != null) {
            result += '.' + name;
        }
        if (attribute != null) {
            result += '.' + attribute;
        } else {
            throw new IllegalArgumentException("key cannot be null");
        }
        return result;
    }
    
    protected String getPropertyValue(String key) {
        return System.getProperty(key);
    }

    public void reset() {
    }

    public String getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(String propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

}
