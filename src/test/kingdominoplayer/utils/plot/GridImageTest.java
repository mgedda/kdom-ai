package kingdominoplayer.utils.plot;

import kingdominoplayer.naiverepresentation.datastructures.Position;
import kingdominoplayer.utils.Random;
import kingdominoplayer.utils.Util;
import org.testng.annotations.Test;


/*
 * Copyright (c) 2017 Magnus Gedda<br>
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
        int castleX = 12;
        int castleY = 12;

        final GridImage gridImage = new GridImage(20, 20);

        plotRandomKingdom(castleX, castleY, gridImage);

        gridImage.markArea(new Position(castleY - 1, castleX -2), new Position(castleY, castleX - 2), 0xFFFF0000);

        BufferedImageViewer.displayImage(gridImage.toBufferedImage(), "TestImage");

        Util.noop();
    }

    @Test
    public void testRectangleOverlay() throws Exception
    {
        int castleX = 12;
        int castleY = 12;

        final GridImage gridImage = new GridImage(20, 20);

        plotRandomKingdom(castleX, castleY, gridImage);

        gridImage.drawRectangle(new Position(castleY - 1, castleX -2), new Position(castleY, castleX - 2), 0x40FF0000);

        BufferedImageViewer.displayImage(gridImage.toBufferedImage(), "TestImage");

        Util.noop();
    }


    @Test
    public void testVariousCellSizesImage() throws Exception
    {
        final GridImage gridImage10 = new GridImage(11, 11, 10, 10);
        final GridImage gridImage15 = new GridImage(11, 11, 15, 15);
        final GridImage gridImage20 = new GridImage(11, 11, 20, 20);
        final GridImage gridImage25 = new GridImage(11, 11, 25, 25);
        final GridImage gridImage30 = new GridImage(11, 11, 30, 30);

        plotRandomKingdom(5, 5, gridImage10);
        plotRandomKingdom(5, 5, gridImage15);
        plotRandomKingdom(5, 5, gridImage20);
        plotRandomKingdom(5, 5, gridImage25);
        plotRandomKingdom(5, 5, gridImage30);

        BufferedImageViewer.displayImage(gridImage10.toBufferedImage(), "Cell size 10x10");
        BufferedImageViewer.displayImage(gridImage15.toBufferedImage(), "Cell size 15x15");
        BufferedImageViewer.displayImage(gridImage20.toBufferedImage(), "Cell size 20x20");
        BufferedImageViewer.displayImage(gridImage25.toBufferedImage(), "Cell size 25x25");
        BufferedImageViewer.displayImage(gridImage30.toBufferedImage(), "Cell size 30x30");

        Util.noop();

    }




    private void plotRandomKingdom(final int castleX, final int castleY, final GridImage gridImage)
    {
        final TileType[] tileTypes = TileType.values();

        for (int cellPosY = castleY - 2; cellPosY < castleY + 3; ++cellPosY)
        {
            for (int cellPosX = castleX - 2; cellPosX < castleX + 3; ++cellPosX)
            {
                final boolean isCastleTile = cellPosX == castleX && cellPosY == castleY;

                final int numCrowns = isCastleTile ? 0 : Random.getInt(3);
                final int tileIndex = isCastleTile ? 1 : Random.getInt(2, tileTypes.length);

                gridImage.drawTile(cellPosX, cellPosY, tileTypes[tileIndex], numCrowns);
            }
        }
    }

}
