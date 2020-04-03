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
package psiprobe.beans.stats.listeners;

/**
 * The listener interface for receiving statsCollection events. The class that is interested in
 * processing a statsCollection event implements this interface, and the object created with that
 * class is registered with a component using the component's {@code addStatsCollectionListener}
 * method. When the statsCollection event occurs, that object's appropriate method is invoked.
 */
public interface StatsCollectionListener {

  /**
   * Stats collected.
   *
   * @param sce the sce
   */
  void statsCollected(StatsCollectionEvent sce);

  /**
   * Checks if is enabled.
   *
   * @return true, if is enabled
   */
  boolean isEnabled();

}
