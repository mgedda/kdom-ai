package kingdominoplayer.plot;

import kingdominoplayer.datastructures.Position;
import kingdominoplayer.datastructures.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 19:17<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class GridImage
{
    private final int[] iData;

    private final int iCellWidth = 32;
    private final int iCellHeight = 32;

    private final int iXSize;
    private final int iYSize;

    private ArrayList<Label> iLabels = new ArrayList<>();

    private class Label
    {
        final String iText;
        final int iCellPosX;
        final int iCellPosY;
        final int iColor;
        final int iFontSize;

        private Label(final String text, final int cellPosX, final int cellPosY, int color, int fontSize)
        {
            iText = text;
            iCellPosX = cellPosX;
            iCellPosY = cellPosY;
            iColor = color;
            iFontSize = fontSize;
        }
    }

    public GridImage(final int numCellsX, final int numCellsY)
    {
        iXSize = numCellsX * iCellWidth;
        iYSize = numCellsY * iCellHeight;

        iData = new int[iXSize * iYSize];

        clear();
    }


    public void markArea(final Position position1, final Position position2, final int color)
    {
        final int borderWidth = 2;

        final int row1 = position1.getRow();
        final int col1 = position1.getColumn();

        final int row2 = position2.getRow();
        final int col2 = position2.getColumn();

        final int rowMin = Math.min(row1, row2);
        final int rowMax = Math.max(row1, row2);

        final int colMin = Math.min(col1, col2);
        final int colMax = Math.max(col1, col2);

        // top
        {
            final int yMin = rowMin * iCellHeight;
            final int yMax = yMin + borderWidth;

            final int xMin = colMin * iCellWidth;
            final int xMax = colMax * iCellWidth + iCellWidth;

            paintArea(yMin, yMax, xMin, xMax, color);
        }

        // bottom
        {
            final int yMin = rowMax * iCellHeight + iCellHeight;
            final int yMax = yMin + borderWidth;

            final int xMin = colMin * iCellWidth;
            final int xMax = colMax * iCellWidth + iCellWidth + borderWidth;

            paintArea(yMin, yMax, xMin, xMax, color);
        }

        // left
        {
            final int yMin = rowMin * iCellHeight;
            final int yMax = rowMax * iCellHeight + iCellHeight;

            final int xMin = colMin * iCellWidth;
            final int xMax = xMin + borderWidth;

            paintArea(yMin, yMax, xMin, xMax, color);
        }

        // right
        {
            final int yMin = rowMin * iCellHeight;
            final int yMax = rowMax * iCellHeight + iCellHeight + borderWidth;

            final int xMin = colMax * iCellWidth + iCellWidth;
            final int xMax = xMin + borderWidth;

            paintArea(yMin, yMax, xMin, xMax, color);
        }
    }

    private void paintArea(final int yMin, final int yMax, final int xMin, final int xMax, final int color)
    {
        for (int y = yMin; y <= yMax; ++y)
        {
            for (int x = xMin; x <= xMax; ++x)
            {
                iData[x + y * iXSize] = color;
            }
        }
    }

    public void drawTile(final Position position, final Tile tile)
    {
        drawTile(position.getColumn(), position.getRow(), TileType.from(tile), tile.getCrowns());
    }

    public void drawTile(final Position position, final TileType tileType)
    {
        drawTile(position.getColumn(), position.getRow(), tileType, 0);
    }

    public void drawTile(final int cellPosX, final int cellPosY, final Tile tile)
    {
        drawTile(cellPosX, cellPosY, TileType.from(tile), tile.getCrowns());
    }

    public void drawTile(final int cellPosX, final int cellPosY, final TileType tileType)
    {
        drawTile(cellPosX, cellPosY, tileType, 0);
    }

    public void drawTile(final int cellPosX, final int cellPosY, final TileType tileType, final int numCrowns)
    {
        final int yMin = cellPosY * iCellHeight + 3;
        final int yMax = yMin + iCellHeight - 3;

        final int xMin = cellPosX * iCellWidth + 3;
        final int xMax = xMin + iCellWidth - 3;

        paintArea(yMin, yMax, xMin, xMax, tileType.getColor());

        if (numCrowns > 0)
        {
            final int textColor = tileType.getTextColor();
            iLabels.add(new Label(Integer.toString(numCrowns), cellPosX, cellPosY, textColor, 14));
        }
    }

    public void drawHeader(final String string, final int cellPosX, final int cellPosY)
    {
        iLabels.add(new Label(string, cellPosX, cellPosY, 0xFF000000, 16));
    }

    public void drawLabel(final String string, final int cellPosX, final int cellPosY)
    {
        iLabels.add(new Label(string, cellPosX, cellPosY, 0xFF000000, 12));
    }

    public void clear()
    {
        Arrays.fill(iData, 0xFFFFFFFF);
        iLabels = new ArrayList<>();
    }


    public BufferedImage toBufferedImage()
    {
        BufferedImage image = new BufferedImage(iXSize, iYSize, BufferedImage.TYPE_INT_RGB);
        final WritableRaster raster = image.getRaster();
        raster.setDataElements(0, 0, iXSize, iYSize, iData);

        for (final Label label : iLabels)
        {
            final int ARGBValue = label.iColor;
            final int r = ARGBValue >> 16 & 0xFF;
            final int g = ARGBValue >> 8 & 0xFF;
            final int b = ARGBValue & 0xFF;

            final float[] hsbVals = Color.RGBtoHSB(r, g, b, null);

            image = BufferedImageUtils.textOverlay(
                    image,
                    label.iText,
                    label.iCellPosX * iCellWidth + 6,
                    label.iCellPosY * iCellHeight + iCellHeight - 4,
                    Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]),
                    label.iFontSize);
        }

        return image;
    }

}
