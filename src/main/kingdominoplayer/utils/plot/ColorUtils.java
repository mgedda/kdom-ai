package kingdominoplayer.utils.plot;

import java.awt.*;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-13<br>
 * Time: 20:14<br><br>
 */
public class ColorUtils
{
    @SuppressWarnings("WeakerAccess")
    public static Color toAWTColor(int ARGBValue)
    {
        final int a = ARGBValue >> 24 & 0xFF;
        final int r = ARGBValue >> 16 & 0xFF;
        final int g = ARGBValue >> 8 & 0xFF;
        final int b = ARGBValue & 0xFF;

        return new Color(r, g, b, a);
    }
}
