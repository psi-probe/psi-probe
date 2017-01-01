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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.jboss.C3P0PooledDataSource;

/**
 * The Class C3P0DatasourceAccessorTest.
 */
public class C3P0DatasourceAccessorTest {

    /** The accessor. */
    C3P0DatasourceAccessor accessor;

    /** The source. */
    ComboPooledDataSource source;

    /** The bad source. */
    C3P0PooledDataSource badSource;

    /**
     * Before.
     */
    @Before
    public void before() {
        accessor = new C3P0DatasourceAccessor();
        source = new ComboPooledDataSource();
        badSource = new C3P0PooledDataSource();
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
