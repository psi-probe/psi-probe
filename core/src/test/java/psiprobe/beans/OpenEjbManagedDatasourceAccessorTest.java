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
