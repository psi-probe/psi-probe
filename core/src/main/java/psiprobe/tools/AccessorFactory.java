/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package psiprobe.tools;

/**
 * A factory for creating Accessor objects.
 *
 * @author Mark Lewis
 */
public class AccessorFactory {

  /**
   * Prevent Instantiation of accessor factory.
   */
  private AccessorFactory() {
    // Prevent Instantiation
  }

  /**
   * Gets the single instance of AccessorFactory.
   *
   * @return single instance of AccessorFactory
   */
  public static Accessor getInstance() {
    return getSimple();
  }

  /**
   * Gets the reflective.
   *
   * @return the reflective
   */
  @SuppressWarnings("unused")
  private static Accessor getReflective() {
    try {
      return new ReflectiveAccessor();
    } catch (ClassNotFoundException e) {
      return null;
    } catch (InstantiationException e) {
      return null;
    } catch (IllegalAccessException e) {
      return null;
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  /**
   * Gets the simple.
   *
   * @return the simple
   */
  private static Accessor getSimple() {
    return new SimpleAccessor();
  }

}
