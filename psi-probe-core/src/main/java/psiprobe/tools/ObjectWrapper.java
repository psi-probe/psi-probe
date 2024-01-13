/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.tools;

/**
 * Wraps an object that may have overridden the {@link Object#equals(Object) equals()} and
 * {@link Object#hashCode() hashCode()} methods so it reverts to the default behavior for
 * {@link Object} instead.
 *
 * <p>
 * This allows us to (1) use {@link java.util.Collection#contains(Object)} to filter out unique
 * instances when calculating object sizes and (2) call {@link Object#hashCode() hashCode()} without
 * fear of an exception or infinite loop.
 * </p>
 *
 * @see Instruments
 */
class ObjectWrapper {

  /** The wrapped object. */
  private final Object wrappedObject;

  /**
   * Instantiates a new object wrapper.
   *
   * @param obj the obj
   */
  public ObjectWrapper(Object obj) {
    this.wrappedObject = obj;
  }

  @Override
  public boolean equals(Object o1) {
    if (wrappedObject == null) {
      return o1 == null;
    }
    if (this.getClass() != o1.getClass()) {
      return false;
    }
    ObjectWrapper ow = (ObjectWrapper) o1;
    /*
     * I know, this condition may seem strange, but if "equals" is left in, sizeOf() may run into an
     * infinite loop on some objects
     */
    return ow.wrappedObject == wrappedObject;// || o.equals(ow.o);
  }

  @Override
  public int hashCode() {
    return System.identityHashCode(wrappedObject);
  }

}
