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
package com.googlecode.psiprobe.tools.logging.jdk;

import com.googlecode.psiprobe.tools.logging.DefaultAccessor;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;

public class Jdk14LoggerAccessor extends DefaultAccessor {

    public List getHandlers() {
        List handlerAccessors = new ArrayList();
        try {
            Object handlers[] = (Object[]) PropertyUtils.getProperty(getTarget(), "handlers");
            for (int h = 0; h < handlers.length; h++) {
                Object handler = handlers[h];
                BaseJdk14HandlerAccessor handlerAccessor = null;
                if ("org.apache.juli.FileHandler".equals(handler.getClass().getName())) {
                    handlerAccessor = new JuliHandlerAccessor();
                } else if ("java.util.logging.ConsoleHandler".equals(handler.getClass().getName())){
                    handlerAccessor = new Jdk14HandlerAccessor();
                }

                if (handlerAccessor != null) {
                    handlerAccessor.setLogClass("jdk");
                    handlerAccessor.setLogger(getTarget());
                    handlerAccessor.setTarget(handlers[h]);
                    handlerAccessor.setApplication(getApplication());
                    handlerAccessors.add(handlerAccessor);
                }
            }
        } catch (Exception e) {
            //
        }
        return handlerAccessors;
    }

    public String getLevel() {
        try {
            Object level = MethodUtils.invokeMethod(getTarget(), "getLevel", null);
            return (String) MethodUtils.invokeMethod(level, "getName", null);
        } catch (Exception e) {
            log.error(getTarget() + ".getLevel() failed", e);
        }
        return null;
    }

    public void setLevel(String newLevelStr) {
        try {
            Object level = MethodUtils.invokeMethod(getTarget(), "getLevel", null);
            Object newLevel = MethodUtils.invokeMethod(level, "parse", newLevelStr);
            MethodUtils.invokeMethod(getTarget(), "setLevel", newLevel);
        } catch (Exception e) {
            log.error(getTarget() + ".setLevel() failed", e);
        }
    }

}
