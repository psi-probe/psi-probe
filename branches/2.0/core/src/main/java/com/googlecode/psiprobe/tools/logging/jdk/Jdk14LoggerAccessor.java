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

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import com.googlecode.psiprobe.model.Application;
import com.googlecode.psiprobe.tools.logging.DefaultAccessor;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Jdk14LoggerAccessor extends DefaultAccessor {

    public List getHandlers() {

        List allHandlers = new ArrayList();

        try {
            Enumeration ee = (Enumeration) MethodUtils.invokeMethod(getTarget(), "getLoggerNames", null);

            List loggerNames = new ArrayList();
            while (ee.hasMoreElements()) {
                loggerNames.add(ee.nextElement());
            }


            for (int j = 0; j < loggerNames.size(); j++) {
                String name = (String) loggerNames.get(j);
                getHandlersForLogger(MethodUtils.invokeMethod(getTarget(), "getLogger", name), getApplication(),
                        allHandlers, "jdk");
            }
        } catch (Exception e) {
            //
        }
        return allHandlers;
    }

    public static void getHandlersForLogger(Object logger, Application application, List handlerAccessors,
                                            String logClass) {
        try {
            Object handlers[] = (Object[]) PropertyUtils.getProperty(logger, "handlers");
            for (int h = 0; h < handlers.length; h++) {
                Object handler = handlers[h];
                BaseJdk14HandlerAccessor handlerAccessor = null;
                if ("org.apache.juli.FileHandler".equals(handler.getClass().getName())) {
                    handlerAccessor = new JuliHandlerAccessor();
                } else if ("java.util.logging.ConsoleHandler".equals(handler.getClass().getName())){
                    handlerAccessor = new Jdk14HandlerAccessor();
                }

                if (handlerAccessor != null) {
                    handlerAccessor.setLogClass(logClass);
                    handlerAccessor.setLogger(logger);
                    handlerAccessor.setTarget(handlers[h]);
                    handlerAccessor.setApplication(application);
                    handlerAccessors.add(handlerAccessor);
                }
            }
        } catch (Exception e) {
            //
        }
    }
}
