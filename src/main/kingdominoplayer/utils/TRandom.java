package kingdominoplayer.utils;

import java.util.Random;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-09<br>
 * Time: 17:08<br><br>
 */
public abstract class TRandom<TRandomSubType extends TRandom<TRandomSubType>>
{
    /**
     * Scale for transforming a 53 bit value to a double value in [0..1]
     */
    protected static final double DOUBLE_SCALE = 1. / ( 1L << 53 );
    /**
     * Scale for transforming a 24 bit value to a float value in [0..1]
     */
    protected static final double FLOAT_SCALE = 1. / ( 1L << 24 );

    /**
     *
     * @param seed A seed value
     * @param stateSize
     * @return A state array suitable for this
     */
    public static long[] seedToState(final long seed, final int stateSize)
    {
        final long first = murmurHash3Finalisation(seed == 0 ? Long.MIN_VALUE : seed);
        final long[] result = new long[stateSize];
        result[0] = first;
        for (int i = 1; i < stateSize; ++i)
        {
            result[i] = murmurHash3Finalisation(result[i-1]);
        }
        return result;
    }

    /**
     *
     * @param values First value to combined
     * @return A combination of the two values into a new value with scrambled bits
     */
    public static int combinedValues(final int... values)
    {
        if (values.length == 0)
        {
            return 0;
        }

        int combination = murmurHash3Finalisation(values[0]);
        for (int i = 1; i < values.length; ++i)
        {
            combination = murmurHash3Finalisation(combination ^ values[i]);
        }

        return combination;
    }


    /**
     *
     * @param values First value to combined
     * @return A combination of the two values into a new value with scrambled bits
     */
    public static long combinedValues(final long... values)
    {
        if (values.length == 0)
        {
            return 0;
        }

        long combination = murmurHash3Finalisation(values[0]);
        for (int i = 1; i < values.length; ++i)
        {
            combination = murmurHash3Finalisation(combination ^ values[i]);
        }

        return combination;
    }

    /**
     *
     * @param value A long sized value
     * @return The long sized value combined and hashed into an int sized value.
     */
    public static int longAsIntHashed(final long value)
    {
        return combinedValues((int) value, (int) (value >> 32));
    }




    /**
     * This is the finalisation step from murmurHash3 for 32-bit values.
     *
     * @param value an integer.
     * @return a deeply scrambled value with all bits spread out
     */
    private static int murmurHash3Finalisation(final int value)
    {
        int result = value;

        result ^= result >>> 16;
        result *= 0x85ebca6b;
        result ^= result >>> 13;
        result *= 0xc2b2ae35;
        result ^= result >>> 16;

        return result;
    }

    /**
     * This is the finalisation step from murmurHash3 for 64-bit values.
     *
     * @param value an integer.
     * @return a deeply scrambled value with all bits spread out
     */
    private static long murmurHash3Finalisation(long value)
    {
        long result = value;

        result ^= result >>> 33;
        result *= 0xff51afd7ed558ccdL;
        result ^= result >>> 33;
        result *= 0xc4ceb9fe1a85ec53L;
        result ^= result >>> 33;

        return result;
    }

    public abstract long nextLong();

    /**
     *
     * @return Next int value from this generator
     */
    public int nextInt()
    {
        return (int)nextLong();
    }

    /**
     *
     * @param n Max value to generate
     * @return Next int value in [0..n) from this generator
     */
    public int nextInt( final int n )
    {
        return (int)nextLong( n );
    }

    /**
     *
     * @param n Max value to generate
     * @return Next long value in [0..n) from this generator
     */
    public long nextLong( final long n )
    {
        assert n > 0 : "pre: n must be positive.";

        while (true)
        {
            final long bits = nextLong() >>> 1;
            final long value = bits % n;

            boolean valueOkToUse = bits - value + (n - 1) >= 0;

            if (valueOkToUse)
            {
                return value;
            }
        }
    }

    /**
     *
     * @return Next double value in [0..1] from this generator.
     */
    public double nextDouble()
    {
        // Double bits contain 53 bits of random information, equal to the number of bits in the mantissa of a
        // double-size floating point number
        final long doubleBits = nextLong() >>> 11;

        final double result = doubleBits * TRandom.DOUBLE_SCALE;

        return result;
    }

    /**
     *
     * @return Next float value in [0..1] from this generator.
     */
    public float nextFloat()
    {
        // Float bits contain 24 bits of random information, equal to the number of bits in the mantissa of a
        // float-size floating point number
        final long floatBits = nextLong() >>> 40;

        final float result = (float) (floatBits * TRandom.FLOAT_SCALE);

        return result;
    }

    /**
     *
     * @return Next Boolean value from this generator.
     */
    public boolean nextBoolean()
    {
        final boolean result = (nextLong() & 1) != 0;

        return result;
    }

    /**
     *
     * @param bytes Array to fill in with random bytes from this generator
     */
    public void nextBytes(final byte[] bytes)
    {
        // Loop with stride 8, generating a long-value each stride and filling from that
        for (int fillFromPosition = 0; fillFromPosition < bytes.length; fillFromPosition += 8)
        {
            final int bytesLeftToFill = Math.min(bytes.length - fillFromPosition, 8);
            long bitsBlock = nextLong();
            // Loop over bytes in bitsBlock, until either all bytes have been used or the array ends
            for (int byteInBits = 0; byteInBits < bytesLeftToFill; ++byteInBits, bitsBlock >>= 8)
            {
                // Cast truncates to first byte
                bytes[fillFromPosition + byteInBits] = (byte) bitsBlock;
            }
        }
    }

    /** Sets the seed of this generator with murmur hash 3 scrambling for handling low integer value seeds gracefully.
     *
     * @param seed seed to use.
     */
    public void setSeed(final long seed)
    {
        setStateCore(seedToState(seed, getStateSize()));
    }

    /**
     *
     * @param state
     * @return New instance with the specified state
     */
    protected abstract TRandomSubType create(final long[] state);

    /**
     *
     * @param seed A seed to use for additional state
     * @return A new TRandom object of the correct type with the seed mixed into the state.
     */
    public TRandomSubType withAddedSeed(final long seed)
    {
        final long[] currentState = getState();
        final long[] stateToAdd = seedToState(seed, getStateSize());
        final long[] newState = new long[getStateSize()];
        for (int i = 0; i < getStateSize(); ++i)
        {
            newState[i] = combinedValues(currentState[i], stateToAdd[i]);
        }

        return create(newState);
    }

    /**
     *
     * @param state The new state to use
     */
    public void setState(final long[] state)
    {
        assert state.length == getStateSize() : "pre: state must be correct number of longs";

        final boolean stateIsValid = !isStateAllZeroes(state);
        if (stateIsValid)
        {
            setStateCore(state);
        }
        else
        {
            setStateCore(seedToState(Long.MIN_VALUE, getStateSize()));
        }
    }

    /**
     *
     * @param state
     * @return True iff all elements in state are 0
     */
    private boolean isStateAllZeroes(final long[] state)
    {
        for (final long stateElement : state)
        {
            if (stateElement != 0)
            {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return Number of longs in the correct state
     */
    protected abstract int getStateSize();

    protected abstract void setStateCore(long[] state);

    /**
     *
     * @return The internal state of this generator.
     */
    public abstract long[] getState();

    /**
     *
     * @return A java.util.Random object that relies on this as a source for random numbers.
     */
    public Random asJavaRandom()
    {
        return new TRandomAsJavaRandom(this);
    }

    private class TRandomAsJavaRandom extends Random
    {
        private final TRandom<TRandomSubType> iTRandom;

        public TRandomAsJavaRandom(final TRandom<TRandomSubType> tRandom)
        {
            super(0);
            iTRandom = tRandom;
        }

        @Override
        public synchronized void setSeed(final long seed)
        {
            if (iTRandom != null)
            {
                iTRandom.setSeed(seed);
            }
        }

        @Override
        protected int next(final int bits)
        {
            return iTRandom.next(bits);
        }

        @Override
        public void nextBytes(final byte[] bytes)
        {
            iTRandom.nextBytes(bytes);
        }

        @Override
        public int nextInt()
        {
            return iTRandom.nextInt();
        }

        @Override
        public int nextInt(final int n)
        {
            return iTRandom.nextInt(n);
        }

        @Override
        public long nextLong()
        {
            return iTRandom.nextLong();
        }

        @Override
        public boolean nextBoolean()
        {
            return iTRandom.nextBoolean();
        }

        @Override
        public float nextFloat()
        {
            return iTRandom.nextFloat();
        }

        @Override
        public double nextDouble()
        {
            return iTRandom.nextDouble();
        }
    }

    private int next(final int bits)
    {
        if (bits > 0 && bits <= 32)
        {
            return (int)(nextLong() >>> (64 - bits));
        }
        else
        {
            return nextInt();
        }
    }
}
