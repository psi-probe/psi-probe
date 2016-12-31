package psiprobe.beans;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * The Class Dbcp2DatasourceAccessorTest.
 */
public class Dbcp2DatasourceAccessorTest {

    /** The accessor. */
    Dbcp2DatasourceAccessor accessor;

    /** The source. */
    BasicDataSource source;

    /** The bad source. */
    ComboPooledDataSource badSource;

    /**
     * Before.
     */
    @Before
    public void before() {
        accessor = new Dbcp2DatasourceAccessor();
        source = new BasicDataSource();
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
