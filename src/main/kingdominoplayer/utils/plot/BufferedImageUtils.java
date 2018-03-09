package kingdominoplayer.utils.plot;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 19:13<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class BufferedImageUtils
{
    /**
     * Add a text string to the image.
     *
     * @param image the image
     * @param text the text string
     * @param x text x-position
     * @param y text y-position
     * @param color text color
     * @param fontSize text font size
     * @return image with text overlay
     */
    public static BufferedImage textOverlay(final BufferedImage image, final String text, final int x, final int y, final Color color, final int fontSize)
    {
        Graphics2D gO = image.createGraphics();
        gO.setColor(color);
        gO.setFont(new Font( "SansSerif", Font.BOLD, fontSize ));
        gO.drawString(text, x, y);
        gO.dispose();

        return image;
    }

    public static BufferedImage rectangleOverlay(final BufferedImage image, final int x, final int y, final int width, final int height, final Color color)
    {
        Graphics2D gO = image.createGraphics();
        gO.setColor(color);
        gO.setStroke(new BasicStroke(2));
        //gO.fillRect(x, y, width, height);
        gO.drawRect(x, y, width, height);
        gO.dispose();

        return image;
    }
}
