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

import java.sql.SQLException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import mockit.Expectations;
import mockit.Mocked;
import oracle.jdbc.pool.OracleDataSource;
import psiprobe.beans.accessors.OracleDatasourceAccessor;

/**
 * The Class OracleDatasourceAccessorTest.
 */
public class OracleDatasourceAccessorTest {

    /** The accessor. */
    OracleDatasourceAccessor accessor;

    /** The source. */
    @Mocked
    OracleDataSource source;

    /** The bad source. */
    ComboPooledDataSource badSource;

    /**
     * Before.
     *
     * @throws SQLException the SQL exception
     */
    @Before
    public void before() throws SQLException {
        accessor = new OracleDatasourceAccessor();
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
        new Expectations() {
            {
                source.getConnectionCacheProperties();
                result = new Properties();
            }
        };
        accessor.getInfo(source);
    }

}
