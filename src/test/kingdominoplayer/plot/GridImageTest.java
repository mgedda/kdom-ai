package kingdominoplayer.plot;

import kingdominoplayer.datastructures.Position;
import kingdominoplayer.utils.Util;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadLocalRandom;

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

        gridImage.drawTile(5, 10, TileType.CASTLE);
        gridImage.drawTile(5, 11, TileType.WATER);
        gridImage.drawTile(5, 12, TileType.FOREST);
        gridImage.drawTile(5, 13, TileType.FIELD);
        gridImage.drawTile(5, 14, TileType.MINE);
        gridImage.drawTile(5, 15, TileType.PASTURE);
        gridImage.drawTile(5, 16, TileType.CLAY);

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

    @Test
    public void testMarkArea() throws Exception
    {
        final GridImage gridImage = new GridImage(60, 40);

        final TileType[] tileTypes = TileType.values();

        int castleX = 12;
        int castleY = 12;

        for (int cellPosY = castleY - 2; cellPosY < castleY + 3; ++cellPosY)
        {
            for (int cellPosX = castleX - 2; cellPosX < castleX + 3; ++cellPosX)
            {
                final boolean isCastleTile = cellPosX == castleX && cellPosY == castleY;

                final int numCrowns = isCastleTile ? 0 : ThreadLocalRandom.current().nextInt(0, 3);
                final int tileIndex = isCastleTile ? 1 : ThreadLocalRandom.current().nextInt(2, tileTypes.length);

                gridImage.drawTile(cellPosX, cellPosY, tileTypes[tileIndex], numCrowns);
            }
        }

        gridImage.markArea(new Position(castleY - 1, castleX -2), new Position(castleY, castleX - 2), 0xFFFF0000);


        BufferedImageViewer.displayImage(gridImage.toBufferedImage(), "TestImage");

        Util.noop();
    }

}
