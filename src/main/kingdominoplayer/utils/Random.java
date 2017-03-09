package kingdominoplayer.utils;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-09<br>
 * Time: 17:13<br><br>
 */
public class Random
{
    private static final TRandomXorshift128Plus cInstance = new TRandomXorshift128Plus(System.nanoTime());

    public static int getInt(final int maxValExcluded)
    {
        return cInstance.nextInt(maxValExcluded);
    }

    public static int getInt(final int min, final int maxValExcluded)
    {
        return getInt(maxValExcluded - min) + min;
    }
}
