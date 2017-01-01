/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.beans;

import org.apache.openejb.resource.jdbc.managed.local.ManagedDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * The Class OpenEjbManagedDatasourceAccessorTest.
 */
@Ignore
public class OpenEjbManagedDatasourceAccessorTest {

    /** The accessor. */
    OpenEjbManagedDatasourceAccessor accessor;

    /** The source. */
    ManagedDataSource source;

    /** The bad source. */
    ComboPooledDataSource badSource;

    /**
     * Before.
     */
    @Before
    public void before() {
        accessor = new OpenEjbManagedDatasourceAccessor();
        // TODO We need mocking to test this
        source = new ManagedDataSource(null, null, null);
        badSource = new ComboPooledDataSource();
    }

    /**
     * Can map test.
     */
    @Test
    public void canMapTest() {
        Assert.assertTrue(accessor.canMap(source));
    }

    /**
     * Cannot map test.
     */
    @Test
    public void cannotMapTest() {
        Assert.assertFalse(accessor.canMap(badSource));
    }
    
}
