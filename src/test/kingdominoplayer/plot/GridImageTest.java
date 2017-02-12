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
        final GridImage gridImage = new GridImage(20, 20);

        int counter = 0;
        for (final TileType tileType : TileType.values())
        {
            gridImage.drawTile(5, 5 + counter, tileType, 1);
            gridImage.drawLabel(tileType.toString(), 6, 5 + counter);
            counter++;
        }
        BufferedImageViewer.displayImage(gridImage.toBufferedImage(), "TestImage");

        Util.noop();
    }

    @Test
    public void testMarkArea() throws Exception
    {
        final GridImage gridImage = new GridImage(20, 20);

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
