/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.model.jmx;

/**
 * The Class PooledClusterSender.
 */
public class PooledClusterSender extends ClusterSender {

  /** The max pool socket limit. */
  private int maxPoolSocketLimit;

  /**
   * Gets the max pool socket limit.
   *
   * @return the max pool socket limit
   */
  public int getMaxPoolSocketLimit() {
    return maxPoolSocketLimit;
  }

  /**
   * Sets the max pool socket limit.
   *
   * @param maxPoolSocketLimit the new max pool socket limit
   */
  public void setMaxPoolSocketLimit(int maxPoolSocketLimit) {
    this.maxPoolSocketLimit = maxPoolSocketLimit;
  }

}
