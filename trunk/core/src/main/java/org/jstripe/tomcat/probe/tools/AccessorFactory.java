/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.jstripe.tomcat.probe.tools;

public class AccessorFactory {
    
	private AccessorFactory() {
	}

    public static Accessor getInstance() {
        String vmVer = System.getProperty("java.runtime.version");
        String vmVendor = System.getProperty("java.vm.vendor");
        if (vmVendor != null && (
                vmVendor.indexOf("Sun Microsystems") != -1
                || vmVendor.indexOf("Apple Computer") != -1
                || vmVendor.indexOf("Apple Inc.") != -1
                || vmVendor.indexOf("IBM Corporation") != -1)) {
            try {
                if (vmVer.startsWith("1.4")) {
                    return (Accessor) Class.forName("org.jstripe.instruments.Java14Accessor").newInstance();
                } else {
                    return (Accessor) Class.forName("org.jstripe.instruments.Java15Accessor").newInstance();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

}
