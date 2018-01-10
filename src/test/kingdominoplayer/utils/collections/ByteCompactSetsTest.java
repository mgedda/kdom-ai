package kingdominoplayer.utils.collections;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Copyright 2018 Tomologic AB<br/>
 * User: zayenz<br/>
 * Date: 2018-01-08<br/>
 * Time: 15:10<br/>
 */
public class ByteCompactSetsTest
{
    /**
     * The test values include all boundary values, as well as a middle value for each part.
     */
    private static final byte[] TEST_VALUES = new byte[]{
            -128, -127, -100, -66, -65,
            -64, -63, -27, -2, -1,
            0, 1, 42, 62, 63, 64,
            65, 100, 126, 127};

    @Test
    public void testEmptySet()
    {
        testEmptySet(new ByteCompactSet());
    }

    @Test
    public void testAddValues()
    {
        testAddValues(new ByteCompactSet());
    }

    @Test
    public void testAddRemoveValues()
    {
        testAddRemoveValues(new ByteCompactSet());
    }

    @Test
    public void testEmptyLinkedSet()
    {
        testEmptySet(new ByteCompactLinkedSet());
    }

    @Test
    public void testAddValuesLinked()
    {
        testAddValues(new ByteCompactLinkedSet());
    }

    @Test
    public void testAddRemoveValuesLinked()
    {
        testAddRemoveValues(new ByteCompactLinkedSet());
    }

    private void testEmptySet(final ByteSet set)
    {
        Assert.assertTrue(set.isEmpty());

        //noinspection ConstantConditions
        Assert.assertTrue(set.size() == 0);

        Assert.assertFalse(set.iterator().hasNext());
    }

    private void testAddValues(final ByteSet set)
    {
        final ByteList valuesList = new ByteArrayList(TEST_VALUES);

        set.addAll(valuesList);

        Assert.assertEquals(valuesList, set);
    }

    private void testAddRemoveValues(final ByteSet set)
    {
        final ByteList valuesList = new ByteArrayList(TEST_VALUES);

        set.addAll(valuesList);
        for (int i = TEST_VALUES.length - 1; i >= 0; i--)
        {
            byte testValue = TEST_VALUES[i];
            set.remove(testValue);
        }

        Assert.assertTrue(set.isEmpty());
    }
}
