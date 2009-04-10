/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://probe.jstripe.com/d/license.shtml
 *
 *  THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 *  WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jstripe.tomcat.probe.model.stats;

import com.thoughtworks.xstream.XStream;

import java.util.*;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.jfree.data.xy.XYDataItem;

public class StatsCollection implements InitializingBean, DisposableBean, ApplicationContextAware {

    private Log logger = LogFactory.getLog(this.getClass());

    private Map statsData = new TreeMap();
    private String swapFileName;
    private String storagePath = null;
    private File contextTempDir;
    private int maxFiles = 2;

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

    public List newStats(String name, int maxElements) {
        List stats = Collections.synchronizedList(new ArrayList(maxElements));
        statsData.put(name, stats);
        return stats;
    }

    public List getStats(String name) {
        return (List) statsData.get(name);
    }

    private File makeFile() {
        return storagePath == null ? new File(contextTempDir, swapFileName) : new File(storagePath, swapFileName);
    }

    private void shiftFiles(int index) {
        if (index >= maxFiles-1) {
            new File(makeFile().getAbsolutePath()+"."+index).delete();
        } else {
            shiftFiles(index + 1);
            File srcFile = index == 0 ? makeFile() : new File(makeFile().getAbsolutePath()+"."+index);
            File destFile = new File(makeFile().getAbsolutePath()+"."+ (index + 1));
            srcFile.renameTo(destFile);
        }
    }

    /**
     * Writes stats data to file on disk.
     *
     * @throws IOException
     */
    public void serialize() throws IOException {
        long t = System.currentTimeMillis();
        try {
            shiftFiles(0);
            OutputStream os = new FileOutputStream(makeFile());
            try {
                new XStream().toXML(statsData, os);
            } finally {
                os.close();
            }
        } catch(Exception e) {
            logger.error("Could not write stats data to "+makeFile().getAbsolutePath(), e);
        } finally {
            logger.info("stats serialized in "+(System.currentTimeMillis() - t)+"ms.");
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

                        for (Iterator it = stats.keySet().iterator(); it.hasNext(); ) {
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
                logger.info("stats data read in "+(System.currentTimeMillis() - t)+"ms.");
            } catch (Throwable e) {
                logger.error("Could not read stats data from "+f.getAbsolutePath(), e);
            }
        }

        return stats;
    }

    /**
     * Reads stats data from file on disk.
     *
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        int index = 0;
        Map stats;

        while (true) {
            File f = index == 0 ? makeFile() : new File(makeFile().getAbsolutePath()+"."+index);
            stats = deserialize(f);
            index += 1;
            if (stats != null || index >= maxFiles -1) break;
        }

        if (stats != null) {
            statsData = stats;
        } else {
            logger.info("Stats data file not found. Empty file assumed.");
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
