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
package com.vladmihalcea.flexypool;

/**
 * Test stub for Flexy Pool datasource wrappers.
 *
 * @param <T> the generic type
 */
public class FlexyPoolDataSource<T> {

  /** The target datasource. */
  private final T targetDataSource;

  /**
   * Instantiates a new flexy pool datasource.
   *
   * @param targetDataSource the target datasource
   */
  public FlexyPoolDataSource(T targetDataSource) {
    this.targetDataSource = targetDataSource;
  }

  /**
   * Gets the target datasource.
   *
   * @return the target datasource
   */
  public T getTargetDataSource() {
    return targetDataSource;
  }
}
