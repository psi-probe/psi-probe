package psiprobe.beans;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jolbox.bonecp.BoneCPDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * The Class TomcatJdbcPoolDatasourceAccessorTest.
 */
public class TomcatJdbcPoolDatasourceAccessorTest {

    /** The accessor. */
    TomcatJdbcPoolDatasourceAccessor accessor;

    /** The source. */
    DataSource source;

    /** The bad source. */
    ComboPooledDataSource badSource;

    /**
     * Before.
     */
    @Before
    public void before() {
        accessor = new TomcatJdbcPoolDatasourceAccessor();
        source = new DataSource();
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
