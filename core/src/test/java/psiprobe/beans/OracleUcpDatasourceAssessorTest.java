package psiprobe.beans;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jolbox.bonecp.BoneCPDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import oracle.ucp.jdbc.PoolDataSourceImpl;
import oracle.ucp.jdbc.PoolXADataSourceImpl;

/**
 * The Class OracleUcpDatasourceAssessorTest.
 */
public class OracleUcpDatasourceAssessorTest {

    /** The accessor. */
    OracleUcpDatasourceAssessor accessor;

    /** The source. */
    PoolDataSourceImpl source;

    PoolXADataSourceImpl xaSource;
    
    /** The bad source. */
    ComboPooledDataSource badSource;

    /**
     * Before.
     */
    @Before
    public void before() {
        accessor = new OracleUcpDatasourceAssessor();
        source = new PoolDataSourceImpl();
        xaSource = new PoolXADataSourceImpl();
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
     * Can map XA test.
     */
    @Test
    public void canMapXATest() {
        Assert.assertTrue(accessor.canMap(xaSource));
    }

    /**
     * Cannot map test.
     */
    @Test
    public void cannotMapTest() {
        Assert.assertFalse(accessor.canMap(badSource));
    }
    
}
