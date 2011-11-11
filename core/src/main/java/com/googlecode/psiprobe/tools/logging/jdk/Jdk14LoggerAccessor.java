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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;

public class Jdk14LoggerAccessor extends DefaultAccessor {

    private boolean context = false;

    public List getHandlers() {
        List handlerAccessors = new ArrayList();
        try {
            Object handlers[] = (Object[]) PropertyUtils.getProperty(getTarget(), "handlers");
            for (int h = 0; h < handlers.length; h++) {
                Object handler = handlers[h];
                Jdk14HandlerAccessor handlerAccessor = wrapHandler(handler, h);
                if (handlerAccessor != null) {
                    handlerAccessors.add(handlerAccessor);
                }
            }
        } catch (Exception e) {
            log.error(getTarget() + "#handlers inaccessible", e);
        }
        return handlerAccessors;
    }

    public boolean isContext() {
        return context;
    }

    public void setContext(boolean context) {
        this.context = context;
    }

    public boolean isRoot() {
        return "".equals(getName())
                || isJuliRoot();
    }

    public boolean isJuliRoot() {
        return "org.apache.juli.ClassLoaderLogManager$RootLogger".equals(getTargetClass());
    }

    public String getName() {
        return (String) getProperty(getTarget(), "name", null);
    }

    public Jdk14HandlerAccessor getHandler(String logIndex) {
        int index = 0;
        try {
            index = Integer.parseInt(logIndex);
        } catch (Exception e) {
            log.info("Could not parse integer from: " + logIndex + ".  Assuming 0.");
        }
        return getHandler(index);
    }

    public Jdk14HandlerAccessor getHandler(int index) {
        try {
            Object handlers[] = (Object[]) PropertyUtils.getProperty(getTarget(), "handlers");
            return wrapHandler(handlers[index], index);
        } catch (Exception e) {
            log.error(getTarget() + "#handlers inaccessible", e);
        }
        return null;
    }

    public String getLevel() {
        try {
            Object level = null;
            Object target = getTarget();
            while (level == null && target != null) {
                level = getLevelInternal(target);
                target = MethodUtils.invokeMethod(target, "getParent", null);
            }
            if (level == null && isJuliRoot()) {
                return "INFO";
            } else {
                return (String) MethodUtils.invokeMethod(level, "getName", null);
            }
        } catch (Exception e) {
            log.error(getTarget() + ".getLevel() failed", e);
        }
        return null;
    }

    public void setLevel(String newLevelStr) {
        try {
            Class levelClass = getTarget().getClass().getClassLoader().loadClass("java.util.logging.Level");
            Method parse = MethodUtils.getAccessibleMethod(levelClass, "parse", String.class);
            Object newLevel = parse.invoke(null, new Object[] {newLevelStr});
            MethodUtils.invokeMethod(getTarget(), "setLevel", newLevel);
        } catch (Exception e) {
            log.error(getTarget() + ".setLevel(\"" + newLevelStr + "\") failed", e);
        }
    }

    private Object getLevelInternal(Object target) throws Exception {
        return MethodUtils.invokeMethod(target, "getLevel", null);
    }

    private Jdk14HandlerAccessor wrapHandler(Object handler, int index) {
        try {
            if (handler == null) {
                throw new IllegalArgumentException("handler is null");
            }
            Jdk14HandlerAccessor handlerAccessor = null;
            if ("org.apache.juli.FileHandler".equals(handler.getClass().getName())) {
                handlerAccessor = new JuliHandlerAccessor();
            } else if ("java.util.logging.ConsoleHandler".equals(handler.getClass().getName())){
                handlerAccessor = new Jdk14HandlerAccessor();
            }

            if (handlerAccessor != null) {
                handlerAccessor.setLoggerAccessor(this);
                handlerAccessor.setTarget(handler);
                handlerAccessor.setIndex(Integer.toString(index));
                handlerAccessor.setApplication(getApplication());
            }
            return handlerAccessor;
        } catch (Exception e) {
            log.error("Could not wrap handler: " + handler, e);
        }
        return null;
    }

}
