import java.lang.reflect.Array;
import java.util.Collection;

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

    public static int[] toArray(final Collection<Integer> values)
    {
        final int[] array = new int[values.size()];

        int index = 0;
        for (final Integer value : values)
        {
            array[index++] = value;
        }

        return array;
    }


    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(T[] originalType, int length)
    {
        return (T[]) Array.newInstance(originalType.getClass().getComponentType(), length);
    }


    public static<T> T[] append(final T[] array, T element)
    {
        final T[] appendedArray = newArray(array, array.length+1);

        System.arraycopy(array, 0, appendedArray,0, array.length);
        appendedArray[array.length] = element;

        return appendedArray;
    }
}
