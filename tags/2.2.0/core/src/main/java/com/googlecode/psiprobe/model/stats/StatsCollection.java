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
package com.googlecode.psiprobe.model.stats;

import com.googlecode.psiprobe.tools.UpdateCommitLock;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.data.xy.XYDataItem;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

public class StatsCollection implements InitializingBean, DisposableBean, ApplicationContextAware {

    private Log logger = LogFactory.getLog(this.getClass());

    private Map statsData = new TreeMap();
    private String swapFileName;
    private String storagePath = null;
    private File contextTempDir;
    private int maxFiles = 2;
    private final UpdateCommitLock lock = new UpdateCommitLock();

    public String getSwapFileName() {
        return swapFileName;
    }

    public void setSwapFileName(String swapFileName) {
        this.swapFileName = swapFileName;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public boolean isCollected(String statsName) {
        return statsData.get(statsName) != null;
    }

    public int getMaxFiles() {
        return maxFiles;
    }

    public void setMaxFiles(int maxFiles) {
        this.maxFiles = maxFiles > 0 ? maxFiles : 2;
    }

    public synchronized List newStats(String name, int maxElements) {
        List stats = Collections.synchronizedList(new ArrayList(maxElements));
        statsData.put(name, stats);
        return stats;
    }

    public synchronized void resetStats(String name) {
        List stats = getStats(name);
        if (stats != null) {
            stats.clear();
        }
    }

    public synchronized List getStats(String name) {
        return (List) statsData.get(name);
    }

    public long getLastValueForStat(String statName) {
        long statValue = 0;

        List stats = getStats(statName);
        if (stats != null && ! stats.isEmpty()) {
            XYDataItem xy = (XYDataItem) stats.get(stats.size() - 1);
            if (xy != null && xy.getY() != null) {
                statValue = xy.getY().longValue();
            }
        }

        return statValue;
    }

    /**
     * Returns series if stat name starts with the prefix.
     * @param statNamePrefix
     * @return a Map of matching stats. Map keys are stat names and map values are corresponding series.
     */
    public synchronized Map getStatsByPrefix(String statNamePrefix) {
        Map map = new HashMap();
        for (Iterator i = statsData.entrySet().iterator(); i.hasNext();) {
            Map.Entry en = (Map.Entry) i.next();
            if (((String)en.getKey()).startsWith(statNamePrefix)) {
                map.put(en.getKey(), en.getValue());
            }
        }
        return map;
    }

    private File makeFile() {
        return storagePath == null ? new File(contextTempDir, swapFileName) : new File(storagePath, swapFileName);
    }

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
     * @throws IOException
     * @throws InterruptedException
     */
    public synchronized void serialize() throws IOException, InterruptedException {
        lock.lockForCommit();
        try {
            long t = System.currentTimeMillis();
            try {
                shiftFiles(0);
                OutputStream os = new FileOutputStream(makeFile());
                try {
                    new XStream().toXML(statsData, os);
                } finally {
                    os.close();
                }
            } catch (Exception e) {
                logger.error("Could not write stats data to " + makeFile().getAbsolutePath(), e);
            } finally {
                logger.debug("stats serialized in " + (System.currentTimeMillis() - t) + "ms.");
            }
        } finally{
            lock.releaseCommitLock();
        }
    }

    private Map deserialize(File f) {
        Map stats = null;
        if (f.exists() && f.canRead()) {
            long t = System.currentTimeMillis();
            try {
                FileInputStream fis = new FileInputStream(f);
                try {
                    stats = (Map) (new XStream().fromXML(fis));

                    if (stats != null) {
                        // adjust stats data so that charts look realistic.
                        // we do that by ending the previous stats group with 0 value
                        // and starting the current stats group also with 0
                        // thus giving the chart nice plunge to zero indicating downtime
                        //
                        // and lets not bother about rotating stats;
                        // regular stats collection cycle will do it

                        for (Iterator it = stats.keySet().iterator(); it.hasNext();) {
                            List l = (List) stats.get(it.next());
                            if (l.size() > 0) {
                                XYDataItem xy = (XYDataItem) l.get(l.size() - 1);
                                l.add(new XYDataItem(xy.getX().longValue() + 1, 0));
                                l.add(new XYDataItem(System.currentTimeMillis(), 0));
                            }
                        }
                    }
                } finally {
                    fis.close();
                }
                logger.debug("stats data read in " + (System.currentTimeMillis() - t) + "ms.");
            } catch (Throwable e) {
                logger.error("Could not read stats data from " + f.getAbsolutePath(), e);
                //
                // make sure we always re-throw ThreadDeath
                //
                if (e instanceof ThreadDeath) {
                    throw (ThreadDeath) e;
                }
            }
        }

        return stats;
    }

    public void lockForUpdate() throws InterruptedException {
        lock.lockForUpdate();
    }

    public void releaseLock() {
        lock.releaseUpdateLock();
    }

    /**
     * Reads stats data from file on disk.
     *
     * @throws Exception
     */
    public synchronized void afterPropertiesSet() throws Exception {
        int index = 0;
        Map stats;

        while (true) {
            File f = index == 0 ? makeFile() : new File(makeFile().getAbsolutePath() + "." + index);
            stats = deserialize(f);
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


    public void destroy() throws Exception {
        serialize();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebApplicationContext wac = (WebApplicationContext) applicationContext;
        contextTempDir = (File) wac.getServletContext().getAttribute("javax.servlet.context.tempdir");
    }
}
