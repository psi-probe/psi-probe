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
package com.googlecode.psiprobe.beans;

import com.googlecode.psiprobe.model.DataSourceInfo;

/**
 * Part of datasource type abstraction layer. Allows to extent Probe
 * functionality to any kind of datasources.
 *
 * @author Vlad Ilyushchenko
 *
 */
public interface DatasourceAccessor {
    DataSourceInfo getInfo(Object resource) throws Exception;
    boolean reset(Object resource) throws Exception;
    boolean canMap(Object resource);
}
