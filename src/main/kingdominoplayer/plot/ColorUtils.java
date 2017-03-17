package kingdominoplayer.plot;

import java.awt.*;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-13<br>
 * Time: 20:14<br><br>
 */
/*package*/ class ColorUtils
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
