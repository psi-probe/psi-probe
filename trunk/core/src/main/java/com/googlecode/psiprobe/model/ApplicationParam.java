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
package com.googlecode.psiprobe.model;

/**
 * A model class representing an application initialization parameter
 * 
 * @author Andy Shapoval
 */
public class ApplicationParam {
    private String name;
    private Object value;
    /**
     * denotes whether the value is taken from a deployment descriptor
     */
    public boolean fromDeplDescr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isFromDeplDescr() {
        return fromDeplDescr;
    }

    public void setFromDeplDescr(boolean fromDeplDescr) {
        this.fromDeplDescr = fromDeplDescr;
    }
}
