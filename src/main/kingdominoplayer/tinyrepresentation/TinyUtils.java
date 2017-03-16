package kingdominoplayer.tinyrepresentation;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 12:41<br><br>
 */
public class TinyUtils
{
    public static String to2DArrayString(final byte[] byteArray, final int xSize)
    {
        String result = "{";

        final int ySize = byteArray.length / xSize;
        for (int y = 0; y < ySize; ++y)
        {
            for (int x = 0; x < xSize; ++x)
            {
                result = result.concat(" ".concat(Byte.toString(byteArray[y * xSize + x])));
                if (x < xSize - 1)
                {
                    result = result.concat(",");
                }
            }

            if (y == ySize - 1)
            {
                result = result.concat("}");
            }
            else
            {
                result = result.concat("\n");
            }
        }

        return result;
    }
}
