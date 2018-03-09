package kingdominoplayer.utils;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-09<br>
 * Time: 17:09<br><br>
 */
public class TRandomXorshift128Plus extends TRandom<TRandomXorshift128Plus>
{

    /**
     * The first part of the internal state of the algorithm.
     */
    private long iS1;

    /**
     * The second part of the internal state of the algorithm.
     */
    private long iS0;


    public TRandomXorshift128Plus(final long seed)
    {
        setSeed(seed);
    }

    protected TRandomXorshift128Plus(final long[] state)
    {
        setState(state);
    }

    /**
     *
     * @return Next long value from this generator.
     */
    @Override
    public long nextLong() {
        /*
         * This is the core of the xorshift128+ algorithm.
         */
        long nextState = iS0;
        final long oldS1 = iS1;

        nextState ^= nextState << 23;

        iS0 = oldS1;
        iS1 = (nextState ^ oldS1 ^ (nextState >>> 17) ^ (oldS1 >>> 26));

        final long result = iS1 + iS0;

        return result;
    }

    @Override
    protected TRandomXorshift128Plus create(final long[] state)
    {
        return new TRandomXorshift128Plus(state);
    }

    /**
     *
     * @param state The new state to use, assuming that it is two longs that are not both zero.
     */
    @Override
    protected void setStateCore(final long[] state)
    {
        iS0 = state[0];
        iS1 = state[1];
    }

    public long[] getState()
    {
        final long[] result = new long[2];
        result[0] = iS0;
        result[1] = iS1;

        return result;
    }

    protected int getStateSize()
    {
        return 2;
    }
}
