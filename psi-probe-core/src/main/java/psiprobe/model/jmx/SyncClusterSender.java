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
 * The Class SyncClusterSender.
 */
public class SyncClusterSender extends ClusterSender {

  /** The data failure counter. */
  private long dataFailureCounter;

  /** The data resend counter. */
  private long dataResendCounter;

  /** The socket open counter. */
  private long socketOpenCounter;

  /** The socket close counter. */
  private long socketCloseCounter;

  /** The socket open failure counter. */
  private long socketOpenFailureCounter;

  /**
   * Gets the data failure counter.
   *
   * @return the data failure counter
   */
  public long getDataFailureCounter() {
    return dataFailureCounter;
  }

  /**
   * Sets the data failure counter.
   *
   * @param dataFailureCounter the new data failure counter
   */
  public void setDataFailureCounter(long dataFailureCounter) {
    this.dataFailureCounter = dataFailureCounter;
  }

  /**
   * Gets the data resend counter.
   *
   * @return the data resend counter
   */
  public long getDataResendCounter() {
    return dataResendCounter;
  }

  /**
   * Sets the data resend counter.
   *
   * @param dataResendCounter the new data resend counter
   */
  public void setDataResendCounter(long dataResendCounter) {
    this.dataResendCounter = dataResendCounter;
  }

  /**
   * Gets the socket open counter.
   *
   * @return the socket open counter
   */
  public long getSocketOpenCounter() {
    return socketOpenCounter;
  }

  /**
   * Sets the socket open counter.
   *
   * @param socketOpenCounter the new socket open counter
   */
  public void setSocketOpenCounter(long socketOpenCounter) {
    this.socketOpenCounter = socketOpenCounter;
  }

  /**
   * Gets the socket close counter.
   *
   * @return the socket close counter
   */
  public long getSocketCloseCounter() {
    return socketCloseCounter;
  }

  /**
   * Sets the socket close counter.
   *
   * @param socketCloseCounter the new socket close counter
   */
  public void setSocketCloseCounter(long socketCloseCounter) {
    this.socketCloseCounter = socketCloseCounter;
  }

  /**
   * Gets the socket open failure counter.
   *
   * @return the socket open failure counter
   */
  public long getSocketOpenFailureCounter() {
    return socketOpenFailureCounter;
  }

  /**
   * Sets the socket open failure counter.
   *
   * @param socketOpenFailureCounter the new socket open failure counter
   */
  public void setSocketOpenFailureCounter(long socketOpenFailureCounter) {
    this.socketOpenFailureCounter = socketOpenFailureCounter;
  }

}
