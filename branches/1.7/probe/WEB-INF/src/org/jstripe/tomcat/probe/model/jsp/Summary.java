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

package org.jstripe.tomcat.probe.model.jsp;

import java.io.Serializable;
import java.util.Map;

public class Summary implements Serializable {
    private String name;
    private Map items;
    private int outOfDateCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getItems() {
        return items;
    }

    public void setItems(Map items) {
        this.items = items;
    }

    public int getOutOfDateCount() {
        return outOfDateCount;
    }

    public void setOutOfDateCount(int outOfDateCount) {
        this.outOfDateCount = outOfDateCount;
    }
}
