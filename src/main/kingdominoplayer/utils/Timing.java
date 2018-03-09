package kingdominoplayer.utils;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 09:23<br><br>
 */
public class Timing
{
    @SuppressWarnings("SameParameterValue")
    public static void sleep(final int milliSeconds)
    {
        try
        {
            Thread.sleep(milliSeconds);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
}
