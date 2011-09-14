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
package com.googlecode.psiprobe;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Deployer;
import org.apache.catalina.Host;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;

public class Tomcat50ContainerAdaptor extends AbstractTomcatContainer {

    private Deployer deployer;
    private Valve valve = new Tomcat50AgentValve();

    public void setWrapper(Wrapper wrapper) {
        if (wrapper != null) {
            this.deployer = (Deployer) wrapper.getParent().getParent();
            ((Host)this.deployer).getPipeline().addValve(valve);
        } else if (deployer != null ){
            ((Host)this.deployer).getPipeline().removeValve(valve);
        }
    }

    public boolean canBoundTo(String binding) {
        return binding != null && binding.startsWith("Apache Tomcat/5.0");
    }

    protected Context findContextInternal(String name) {
        return deployer.findDeployedApp(name);
    }

    public List findContexts() {
        String[] names = deployer.findDeployedApps();
        List result = new ArrayList(names.length);

        for (int i = 0; i < names.length; i++) {
            result.add(deployer.findDeployedApp(names[i]));
        }

        return result;
    }

    public void stop(String name) throws IOException {
        deployer.stop(name);
    }

    public void start(String name) throws IOException {
        deployer.start(name);
    }

    public void removeInternal(String name) throws IOException {
        deployer.remove(name, true);
    }

    public void installContextInternal(String name, File config) throws IOException {
        deployer.install(new URL("file:"+config.getAbsolutePath()), null);
    }

    public void installWar(String name, URL url) throws IOException {
        deployer.install(name, url);
    }

    public File getAppBase() {
        String strBase = ((Host)deployer).getAppBase();
        File base = new File(strBase);
        if (! base.isAbsolute()) {
            base = new File(System.getProperty("catalina.base"), strBase);
        }
        return base;
    }

    public String getConfigBase() {
        return getConfigBase((Container) deployer);
    }

    public Object getLogger(Context context) {
        return context.getLogger();
    }

    public String getHostName() {
        return deployer.getName();
    }

    public String getName() {
        return ((Host)this.deployer).getParent().getName();
    }

}
