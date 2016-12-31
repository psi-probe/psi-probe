package psiprobe.beans;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jolbox.bonecp.BoneCPDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * The Class BoneCpDatasourceAccessorTest.
 */
public class BoneCpDatasourceAccessorTest {

    /** The accessor. */
    BoneCpDatasourceAccessor accessor;

    /** The source. */
    BoneCPDataSource source;

    /** The bad source. */
    ComboPooledDataSource badSource;

    /**
     * Before.
     */
    @Before
    public void before() {
        accessor = new BoneCpDatasourceAccessor();
        source = new BoneCPDataSource();
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
