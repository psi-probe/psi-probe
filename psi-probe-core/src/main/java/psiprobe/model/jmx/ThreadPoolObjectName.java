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
package psiprobe.model.jmx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.ObjectName;

/**
 * This bean represens a hierarchy of Tomcat's ObjectNames necessary to display real-time connection
 * information on "status" tab.
 */
public class ThreadPoolObjectName {

  /** The thread pool name. */
  private ObjectName threadPoolName;

  /** The global request processor name. */
  private ObjectName globalRequestProcessorName;

  /** The request processor names. */
  private List<ObjectName> requestProcessorNames = new ArrayList<>();

  /**
   * Gets the thread pool name.
   *
   * @return the thread pool name
   */
  public ObjectName getThreadPoolName() {
    return threadPoolName;
  }

  /**
   * Gets the global request processor name.
   *
   * @return the global request processor name
   */
  public ObjectName getGlobalRequestProcessorName() {
    return globalRequestProcessorName;
  }

  /**
   * Gets the request processor names.
   *
   * @return the request processor names
   */
  public List<ObjectName> getRequestProcessorNames() {
    return requestProcessorNames == null ? Collections.emptyList()
        : new ArrayList<>(requestProcessorNames);
  }

  /**
   * Sets the thread pool name.
   *
   * @param threadPoolName the new thread pool name
   */
  public void setThreadPoolName(ObjectName threadPoolName) {
    this.threadPoolName = threadPoolName;
  }

  /**
   * Sets the global request processor name.
   *
   * @param globalRequestProcessorName the new global request processor name
   */
  public void setGlobalRequestProcessorName(ObjectName globalRequestProcessorName) {
    this.globalRequestProcessorName = globalRequestProcessorName;
  }

  /**
   * Sets the request processor names.
   *
   * @param requestProcessorNames the new request processor names
   */
  public void setRequestProcessorNames(List<ObjectName> requestProcessorNames) {
    this.requestProcessorNames = requestProcessorNames == null ? Collections.emptyList()
        : new ArrayList<>(requestProcessorNames);
  }

}
