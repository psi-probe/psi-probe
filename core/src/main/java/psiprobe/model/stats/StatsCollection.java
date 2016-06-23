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

package psiprobe.model.stats;

import com.thoughtworks.xstream.XStream;

import org.jfree.data.xy.XYDataItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import psiprobe.tools.UpdateCommitLock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Class StatsCollection.
 *
 * @author Vlad Ilyushchenko
 * @author Andy Shapoval
 * @author Mark Lewis
 */
public class StatsCollection implements InitializingBean, DisposableBean, ApplicationContextAware {

  /** The logger. */
  private static final Logger logger = LoggerFactory.getLogger(StatsCollection.class);

  /** The stats data. */
  private Map<String, List<XYDataItem>> statsData = new TreeMap<>();

  /** The swap file name. */
  private String swapFileName;

  /** The storage path. */
  private String storagePath;

  /** The context temp dir. */
  private File contextTempDir;

  /** The max files. */
  private int maxFiles = 2;

  /** The lock. */
  private final UpdateCommitLock lock = new UpdateCommitLock();

  /**
   * Gets the swap file name.
   *
   * @return the swap file name
   */
  public String getSwapFileName() {
    return swapFileName;
  }

  /**
   * Sets the swap file name.
   *
   * @param swapFileName the new swap file name
   */
  public void setSwapFileName(String swapFileName) {
    this.swapFileName = swapFileName;
  }

  /**
   * Gets the storage path.
   *
   * @return the storage path
   */
  public String getStoragePath() {
    return storagePath;
  }

  /**
   * Sets the storage path.
   *
   * @param storagePath the new storage path
   */
  public void setStoragePath(String storagePath) {
    this.storagePath = storagePath;
  }

  /**
   * Checks if is collected.
   *
   * @param statsName the stats name
   * @return true, if is collected
   */
  public boolean isCollected(String statsName) {
    return statsData.get(statsName) != null;
  }

  /**
   * Gets the max files.
   *
   * @return the max files
   */
  public int getMaxFiles() {
    return maxFiles;
  }

  /**
   * Sets the max files.
   *
   * @param maxFiles the new max files
   */
  public void setMaxFiles(int maxFiles) {
    this.maxFiles = maxFiles > 0 ? maxFiles : 2;
  }

  /**
   * New stats.
   *
   * @param name the name
   * @param maxElements the max elements
   * @return the list
   */
  public synchronized List<XYDataItem> newStats(String name, int maxElements) {
    List<XYDataItem> stats = Collections.synchronizedList(new ArrayList<XYDataItem>(maxElements));
    statsData.put(name, stats);
    return stats;
  }

  /**
   * Reset stats.
   *
   * @param name the name
   */
  public synchronized void resetStats(String name) {
    List<XYDataItem> stats = getStats(name);
    if (stats != null) {
      stats.clear();
    }
  }

  /**
   * Gets the stats.
   *
   * @param name the name
   * @return the stats
   */
  public synchronized List<XYDataItem> getStats(String name) {
    return statsData.get(name);
  }

  /**
   * Gets the last value for stat.
   *
   * @param statName the stat name
   * @return the last value for stat
   */
  public long getLastValueForStat(String statName) {
    long statValue = 0;

    List<XYDataItem> stats = getStats(statName);
    if (stats != null && !stats.isEmpty()) {
      XYDataItem xy = stats.get(stats.size() - 1);
      if (xy != null && xy.getY() != null) {
        statValue = xy.getY().longValue();
      }
    }

    return statValue;
  }

  /**
   * Returns series if stat name starts with the prefix.
   * 
   * @param statNamePrefix they key under which the stats are stored
   * @return a Map of matching stats. Map keys are stat names and map values are corresponding
   *         series.
   */
  public synchronized Map<String, List<XYDataItem>> getStatsByPrefix(String statNamePrefix) {
    Map<String, List<XYDataItem>> map = new HashMap<>();
    for (Map.Entry<String, List<XYDataItem>> en : statsData.entrySet()) {
      if (en.getKey().startsWith(statNamePrefix)) {
        map.put(en.getKey(), en.getValue());
      }
    }
    return map;
  }

  /**
   * Make file.
   *
   * @return the file
   */
  private File makeFile() {
    return storagePath == null ? new File(contextTempDir, swapFileName) : new File(storagePath,
        swapFileName);
  }

  /**
   * Shift files.
   *
   * @param index the index
   */
  private void shiftFiles(int index) {
    if (index >= maxFiles - 1) {
      new File(makeFile().getAbsolutePath() + "." + index).delete();
    } else {
      shiftFiles(index + 1);
      File srcFile = index == 0 ? makeFile() : new File(makeFile().getAbsolutePath() + "." + index);
      File destFile = new File(makeFile().getAbsolutePath() + "." + (index + 1));
      srcFile.renameTo(destFile);
    }
  }

  /**
   * Writes stats data to file on disk.
   *
   * @throws InterruptedException if a lock cannot be obtained
   */
  public synchronized void serialize() throws InterruptedException {
    lock.lockForCommit();
    long start = System.currentTimeMillis();
    try {
      shiftFiles(0);
      try (OutputStream os = new FileOutputStream(makeFile())) {
        new XStream().toXML(statsData, os);
      }
    } catch (Exception e) {
      logger.error("Could not write stats data to '{}'", makeFile().getAbsolutePath(), e);
    } finally {
      lock.releaseCommitLock();
      logger.debug("stats serialized in {}ms", (System.currentTimeMillis() - start));
    }
  }

  /**
   * Deserialize.
   *
   * @param file the file
   * @return the map
   */
  private Map<String, List<XYDataItem>> deserialize(File file) {
    Map<String, List<XYDataItem>> stats = null;
    if (file.exists() && file.canRead()) {
      long start = System.currentTimeMillis();
      try {
        try (FileInputStream fis = new FileInputStream(file)) {
          stats = (Map<String, List<XYDataItem>>) (new XStream().fromXML(fis));

          if (stats != null) {
            // adjust stats data so that charts look realistic.
            // we do that by ending the previous stats group with 0 value
            // and starting the current stats group also with 0
            // thus giving the chart nice plunge to zero indicating downtime
            //
            // and lets not bother about rotating stats;
            // regular stats collection cycle will do it

            for (String key : stats.keySet()) {
              List<XYDataItem> list = stats.get(key);
              if (!list.isEmpty()) {
                XYDataItem xy = list.get(list.size() - 1);
                list.add(new XYDataItem(xy.getX().longValue() + 1, 0));
                list.add(new XYDataItem(System.currentTimeMillis(), 0));
              }
            }
          }
        }
        logger.debug("stats data read in {}ms", (System.currentTimeMillis() - start));
      } catch (Exception e) {
        logger.error("Could not read stats data from '{}'", file.getAbsolutePath(), e);
      }
    }

    return stats;
  }

  /**
   * Lock for update.
   *
   * @throws InterruptedException the interrupted exception
   */
  public void lockForUpdate() throws InterruptedException {
    lock.lockForUpdate();
  }

  /**
   * Release lock.
   */
  public void releaseLock() {
    lock.releaseUpdateLock();
  }

  /**
   * Reads stats data from file on disk.
   */
  @Override
  public synchronized void afterPropertiesSet() {
    int index = 0;
    Map<String, List<XYDataItem>> stats;

    while (true) {
      File file = index == 0 ? makeFile() : new File(makeFile().getAbsolutePath() + "." + index);
      stats = deserialize(file);
      index += 1;
      if (stats != null || index >= maxFiles - 1) {
        break;
      }
    }

    if (stats != null) {
      statsData = stats;
    } else {
      logger.debug("Stats data file not found. Empty file assumed.");
    }

  }

  @Override
  public void destroy() throws Exception {
    serialize();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    WebApplicationContext wac = (WebApplicationContext) applicationContext;
    contextTempDir = (File) wac.getServletContext().getAttribute("javax.servlet.context.tempdir");
  }

}
