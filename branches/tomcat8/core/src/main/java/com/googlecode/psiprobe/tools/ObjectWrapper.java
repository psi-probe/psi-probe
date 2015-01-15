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

/**
 * Wraps an object that may have overridden the
 * {@link Object#equals(Object) equals()} and
 * {@link Object#hashCode() hashCode()} methods so it reverts to the default
 * behavior for {@link Object} instead.
 * 
 * This allows us to (1) use {@link java.util.Collection#contains(Object)} to
 * filter out unique instances when calculating object sizes and (2) call
 * {@link Object#hashCode() hashCode()} without fear of an exception or infinite
 * loop.
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 * 
 * @see Instruments
 */
class ObjectWrapper {
    private Object o;

    public ObjectWrapper(Object o) {
        this.o = o;
    }

    public boolean equals(Object o1) {
        if (o == null) {
            return o1 == null;
        } else {
            ObjectWrapper ow = (ObjectWrapper) o1;
            // I know, this condition may seem strange, but if "equals" is left out 
            // sizeOf() may run into an infinite loop on some objects
            return ow.o == o;// || o.equals(ow.o);
        }
    }

    public int hashCode() {
        return System.identityHashCode(o);
    }

}
