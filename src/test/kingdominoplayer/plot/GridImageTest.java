package kingdominoplayer.plot;

import kingdominoplayer.Util;
import org.testng.annotations.Test;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 20:18<br><br>
 */
public class GridImageTest
{
    @Test
    public void testImage() throws Exception
    {
        final GridImage gridImage = new GridImage(60, 40);

        gridImage.drawCell(5, 10, TileType.CASTLE);
        gridImage.drawCell(5, 11, TileType.WATER);
        gridImage.drawCell(5, 12, TileType.FOREST);
        gridImage.drawCell(5, 13, TileType.FIELD);
        gridImage.drawCell(5, 14, TileType.MINE);
        gridImage.drawCell(5, 15, TileType.PASTURE);
        gridImage.drawCell(5, 16, TileType.CLAY);

        gridImage.drawLabel("Castle", 6, 10);
        gridImage.drawLabel("Water", 6, 11);
        gridImage.drawLabel("Forest", 6, 12);
        gridImage.drawLabel("Field", 6, 13);
        gridImage.drawLabel("Mine", 6, 14);
        gridImage.drawLabel("Pasture", 6, 15);
        gridImage.drawLabel("Clay", 6, 16);

        BufferedImageViewer.displayImage(gridImage.toBufferedImage(), "TestImage");

        Util.noop();
    }

}
