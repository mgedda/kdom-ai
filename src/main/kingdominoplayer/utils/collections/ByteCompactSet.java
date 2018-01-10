package kingdominoplayer.utils.collections;

import it.unimi.dsi.fastutil.bytes.AbstractByteSet;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteSet;

/*
 * User: zayenz<br/>
 * Date: 2018-01-08<br/>
 * Time: 14:22<br/>
 */

/**
 * A compact representation for a set of bytes.
 */
public class ByteCompactSet extends AbstractByteSet implements ByteSet
{
    private long iData0;
    private long iData1;
    private long iData2;
    private long iData3;
    private int iSize;

    public ByteCompactSet()
    {
        iData0 = 0;
        iData1 = 0;
        iData2 = 0;
        iData3 = 0;
        iSize = 0;
    }

    @Override
    public boolean remove(final byte k)
    {
        if (!contains(k))
        {
            return false;
        }

        final int part = partIndex(k);
        final int index = index(k);
        final long mask = ~(1L << index);
        switch (part)
        {
            case 0:
                iData0 &= mask;
                break;
            case 1:
                iData1 &= mask;
                break;
            case 2:
                iData2 &= mask;
                break;
            case 3:
                iData3 &= mask;
                break;
        }
        --iSize;
        return true;
    }

    @Override
    public boolean add(final byte k)
    {
        if (contains(k))
        {
            return false;
        }

        final int part = partIndex(k);
        final int index = index(k);
        final long mask = 1L << index;
        switch (part)
        {
            case 0:
                iData0 |= mask;
                break;
            case 1:
                iData1 |= mask;
                break;
            case 2:
                iData2 |= mask;
                break;
            case 3:
                iData3 |= mask;
                break;
        }
        ++iSize;
        return true;
    }

    @Override
    public boolean contains(final byte k)
    {
        final long part = part(k);
        final long index = index(k);
        return (part & (1L << index)) != 0;
    }

    /**
     *
     * @param k 
     * @return The part that contains the bit for this value (not usable for updates, only for checks)
     */
    private long part(final byte k)
    {
        if (k < -64)
        {
            return iData0;
        }
        else if (k < 0)
        {
            return iData1;
        }
        else if (k < 64)
        {
            return iData2;
        }
        else
        {
            return iData3;
        }
    }

    /**
     *
     * @param k
     * @return The index of the part that contains the bit for this value
     */
    private int partIndex(final byte k)
    {
        if (k < -64)
        {
            return 0;
        }
        else if (k < 0)
        {
            return 1;
        }
        else if (k < 64)
        {
            return 2;
        }
        else
        {
            return 3;
        }
    }

    /**
     * @param k
     * @return The index of the bit for the value in its relevant part
     */
    private int index(final int k)
    {
        return (k + 128) % 64;
    }

    /**
     *
     * @param exclusiveLowerBound
     * @return The minimum value that is present in the set, and that is larger than exclusiveLowerBound.
     *         When no such element exists, return value larger than Byte.MAX_VALUE.
     */
    private int nextPresentValue(final int exclusiveLowerBound)
    {
        if (iSize == 0)
        {
            return ((int) Byte.MAX_VALUE) + 1;
        }

        int next = exclusiveLowerBound + 1;
        while (next <= Byte.MAX_VALUE && !contains((byte) next))
        {
            ++next;
        }

        return next;
    }

    @Override
    public ByteIterator iterator()
    {
        final int beforeMinByte = ((int) Byte.MIN_VALUE) - 1;
        final int minElement = nextPresentValue(beforeMinByte);
        return new ByteIterator()
        {
            private int iNextValue = minElement;

            @Override
            public boolean hasNext()
            {
                return iNextValue <= Byte.MAX_VALUE;
            }

            @Override
            public byte nextByte()
            {
                final byte result = (byte) iNextValue;
                iNextValue = nextPresentValue(iNextValue);
                return result;
            }
        };
    }

    @Override
    public int size()
    {
        return iSize;
    }
}
