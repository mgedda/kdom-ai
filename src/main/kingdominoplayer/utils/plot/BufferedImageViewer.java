package kingdominoplayer.utils.plot;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 19:08<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class BufferedImageViewer
{
    public static void displayImage(final BufferedImage image, final String title)
    {
        if (GraphicsEnvironment.isHeadless())
        {
            return;
        }

        final JFrame frame = new ImageFrame(image);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle(title);
    }

    @SuppressWarnings("WeakerAccess")
    private static class ImageFrame extends JFrame
    {
        public ImageFrame(final BufferedImage image)
        {
            final JLabel label = new JLabel(new ImageIcon(image));
            final JPanel panel = new JPanel();
            getContentPane().add(panel);
            panel.add(label);
        }
    }

}
