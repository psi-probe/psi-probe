package psiprobe.beans;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import oracle.jdbc.pool.OracleDataSource;

/**
 * The Class OracleDatasourceAccessorTest.
 */
public class OracleDatasourceAccessorTest {

    /** The accessor. */
    OracleDatasourceAccessor accessor;

    /** The source. */
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
        source = new OracleDataSource();
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
