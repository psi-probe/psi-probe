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
package psiprobe.beans.accessors;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import mockit.Mocked;

/**
 * The Class TomcatJdbcPoolDatasourceAccessorTest.
 */
public class TomcatJdbcPoolDatasourceAccessorTest {

    /** The accessor. */
    TomcatJdbcPoolDatasourceAccessor accessor;

    /** The source. */
    @Mocked
    DataSource source;

    /** The bad source. */
    ComboPooledDataSource badSource;

    /**
     * Before.
     */
    @Before
    public void before() {
        accessor = new TomcatJdbcPoolDatasourceAccessor();
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

    /**
     * Gets the info test.
     *
     * @return the info test
     * @throws Exception the exception
     */
    @Test
    public void getInfoTest() throws Exception {
        accessor.getInfo(source);
    }

}
