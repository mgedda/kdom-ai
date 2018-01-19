package kingdominoplayer.utils.collections;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import org.testng.Assert;
import org.testng.annotations.Test;

/*
 * User: zayenz<br/>
 * Date: 2018-01-19<br/>
 * Time: 16:43<br/>
 */
public class ByteMapTest
{

    private static final class DataValue {
        final byte iValue;

        private DataValue(final byte value)
        {
            iValue = value;
        }

        @Override
        public boolean equals(final Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final DataValue dataValue = (DataValue) o;

            return iValue == dataValue.iValue;
        }

        @Override
        public int hashCode()
        {
            return (int) iValue;
        }
    }

    /**
     * The test values include all boundary values, as well as a middle value for each part.
     */
    private static final byte[] TEST_KEYS = new byte[]{
            -128, -127, -100, -66, -65,
            -64, -63, -27, -2, -1,
            0, 1, 42, 62, 63, 64,
            65, 100, 126, 127
    };

    /**
     * The test values include all boundary values, as well as a middle value for each part.
     */
    private static final byte[] POSITIVE_TEST_KEYS = new byte[]{
            0, 1, 42, 62, 63, 64,
            65, 100, 126, 127
    };

    /**
     * The test values include all boundary values, as well as a middle value for each part.
     */
    private static final byte[] SMALL_POSITIVE_TEST_KEYS = new byte[]{
            0, 1, 42, 62, 63, 64
    };

    private static DataValue[] makeTestData(final byte[] keys)
    {
        final DataValue[] result = new DataValue[keys.length];
        for (int i = 0; i < keys.length; i++)
        {
            result[i] = new DataValue(keys[i]);
        }
        return result;
    }


    @Test
    public void testAddThenContains() throws Exception
    {
        testAddThenContains(new ByteFullArrayLinkedMap<>(), TEST_KEYS);
        testAddThenContains(new ByteFullArrayLinkedMap<>(), POSITIVE_TEST_KEYS);
        testAddThenContains(new ByteFullArrayLinkedMap<>(), SMALL_POSITIVE_TEST_KEYS);
    }

    private void testAddThenContains(final Byte2ObjectMap<DataValue> map, final byte[] keys)
    {
        final DataValue[] values = makeTestData(keys);
        for (int i = 0; i < values.length; i++)
        {
            map.put(keys[i], values[i]);
        }

        Assert.assertEquals(map.size(), values.length);

        for (int i = 0; i < values.length; i++)
        {
            Assert.assertEquals(map.get(keys[i]), values[i]);
        }
    }

    @Test
    public void testAddThenRemove() throws Exception
    {
        testAddThenRemove(new ByteFullArrayLinkedMap<>(), TEST_KEYS);
        testAddThenRemove(new ByteFullArrayLinkedMap<>(), POSITIVE_TEST_KEYS);
        testAddThenRemove(new ByteFullArrayLinkedMap<>(), SMALL_POSITIVE_TEST_KEYS);
    }

    private void testAddThenRemove(final Byte2ObjectMap<DataValue> map, final byte[] keys)
    {
        final DataValue[] values = makeTestData(keys);
        for (int i = 0; i < values.length; i++)
        {
            map.put(keys[i], values[i]);
        }

        Assert.assertEquals(map.size(), values.length);

        for (int i = 0; i < values.length; i++)
        {
            Assert.assertEquals(map.remove(keys[i]), values[i]);
        }

        Assert.assertEquals(map.size(), 0);

        for (int i = 0; i < values.length; i++)
        {
            Assert.assertEquals(map.remove(keys[i]), null);
        }

        Assert.assertEquals(map.size(), 0);
    }
}
