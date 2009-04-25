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

package org.jstripe.tomcat.probe;

import org.apache.catalina.*;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public Context findContext(String name) {
        return deployer.findDeployedApp(name);
    }

    public List findContexts() {
        String names[] = deployer.findDeployedApps();
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
        File configBase = new File(System.getProperty("catalina.base"), "conf");
        Container container = (Container) deployer;
        Container host = null;
        Container engine = null;
        while (container != null) {
            if (container instanceof Host)
                host = container;
            if (container instanceof Engine)
                engine = container;
            container = container.getParent();
        }
        if (engine != null) {
            configBase = new File(configBase, engine.getName());
        }
        if (host != null) {
            configBase = new File(configBase, host.getName());
        }
        return configBase.getAbsolutePath();
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
