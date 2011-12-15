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
package com.googlecode.psiprobe.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class ObjectWrapper {
    private Object o;

    public ObjectWrapper(Object o) {
        this.o = o;
    }

    public boolean equals(Object o1) {
        if (o == null && o1 == null) {
            return true;
        } else if (o == null) {
            return false;
        } else {
            ObjectWrapper ow = (ObjectWrapper) o1;
            // I know, this condition may seem strange, but if "equals" is left out 
            // sizeOf() may run into an infinite loop on some objects
            return ow.o == o;// || o.equals(ow.o);
        }
    }

    public int hashCode() {
        return o.hashCode();
    }

}
