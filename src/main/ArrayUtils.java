/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 11:31<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class ArrayUtils
{
    public static int getIndex(final String string, final String[] array)
    {
        for (int i = 0; i < array.length; ++i)
        {
            if (array[i].equals(string))
            {
                return i;
            }
        }

        return -1;
    }


    public static int sum(final int[] array)
    {
        int sum = 0;

        for (final int value : array)
        {
            sum += value;
        }

        return sum;
    }

    public static int getIndexWithMaxValue(int[] array)
    {
        int maxValue = Integer.MIN_VALUE;
        int maxIndex = -1;

        for (int i = 0; i < array.length; ++i)
        {
            if (array[i] > maxValue)
            {
                maxValue = array[i];
                maxIndex = i;
            }
        }

        return maxIndex;
    }
}
