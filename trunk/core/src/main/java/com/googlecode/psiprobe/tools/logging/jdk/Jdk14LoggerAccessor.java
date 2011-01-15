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
import org.apache.commons.beanutils.PropertyUtils;

public class Jdk14LoggerAccessor extends DefaultAccessor {

    public List getHandlers() {
        List handlerAccessors = new ArrayList();
        try {
            Object handlers[] = (Object[]) PropertyUtils.getProperty(getTarget(), "handlers");
            for (int h = 0; h < handlers.length; h++) {
                Object handler = handlers[h];
                BaseJdk14HandlerAccessor handlerAccessor = wrapHandler(handler, h);
                if (handlerAccessor != null) {
                    handlerAccessors.add(handlerAccessor);
                }
            }
        } catch (Exception e) {
            log.error(getTarget() + "#handlers inaccessible", e);
        }
        return handlerAccessors;
    }

    public boolean isRoot() {
        return "".equals(getName())
                || "org.apache.juli.ClassLoaderLogManager#RootLogger".equals(getType());
    }

    public String getName() {
        return (String) getProperty(getTarget(), "name", null);
    }

    public BaseJdk14HandlerAccessor getHandler(int index) {
        try {
            Object handlers[] = (Object[]) PropertyUtils.getProperty(getTarget(), "handlers");
            return wrapHandler(handlers[index], index);
        } catch (Exception e) {
            log.error(getTarget() + "#handlers inaccessible", e);
        }
        return null;
    }

    private BaseJdk14HandlerAccessor wrapHandler(Object handler, int index) {
        BaseJdk14HandlerAccessor handlerAccessor = null;
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
    }

}
