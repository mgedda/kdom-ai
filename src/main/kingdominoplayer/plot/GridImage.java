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

    private final int iCellWidth;    // width of a cell in pixels
    private final int iCellHeight;   // height of a cell in pixels

    private final int iCellBorderWidth = 1;   // must be < min(iCellWidth/2, iCellHeight/2)

    private final int iXSize;
    private final int iYSize;

    private ArrayList<Label> iLabels = new ArrayList<>();
    private ArrayList<Rectangle> iRectangles = new ArrayList<>();

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

    private class Rectangle
    {
        final Position iTopLeft;
        final Position iBottomRight;
        final int iColor;

        private Rectangle(Position topLeft, Position bottomRight, int color)
        {
            iTopLeft = topLeft;
            iBottomRight = bottomRight;
            iColor = color;
        }
    }

    public GridImage(final int numCellsX, final int numCellsY)
    {
        this(numCellsX, numCellsY, 32, 32);
    }

    public GridImage(final int numCellsX, final int numCellsY, final int cellWidth, final int cellHeight)
    {
        iCellWidth = cellWidth;
        iCellHeight = cellHeight;

        iXSize = numCellsX * cellWidth;
        iYSize = numCellsY * cellHeight;

        iData = new int[iXSize * iYSize];

        clear();
    }


    public void markArea(final Position position1, final Position position2, final int color)
    {
        final int borderWidth = 4;   // must be > 0
        final int indent = 1;

        //noinspection ConstantConditions
        assert borderWidth > 0 : "zero-valued border";

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
            final int yMin = rowMin * iCellHeight + indent;
            final int yMax = rowMin * iCellHeight + (borderWidth - 1) + indent;

            final int xMin = colMin * iCellWidth + indent;
            final int xMax = colMax * iCellWidth + (iCellWidth - 1) - indent;

            paintArea(yMin, yMax, xMin, xMax, color);
        }

        // bottom
        {
            final int yMin = rowMax * iCellHeight + (iCellHeight - 1) - (borderWidth - 1) - indent;
            final int yMax = rowMax * iCellHeight + (iCellHeight - 1) - indent;

            final int xMin = colMin * iCellWidth + indent;
            final int xMax = colMax * iCellWidth + (iCellWidth - 1) - indent;

            paintArea(yMin, yMax, xMin, xMax, color);
        }

        // left
        {
            final int yMin = rowMin * iCellHeight + indent;
            final int yMax = rowMax * iCellHeight + (iCellHeight - 1) - indent;

            final int xMin = colMin * iCellWidth + indent;
            final int xMax = colMin * iCellWidth + (borderWidth - 1) + indent;

            paintArea(yMin, yMax, xMin, xMax, color);
        }

        // right
        {
            final int yMin = rowMin * iCellHeight + indent;
            final int yMax = rowMax * iCellHeight + (iCellHeight - 1) - indent;

            final int xMin = colMax * iCellWidth + (iCellWidth - 1) - (borderWidth - 1) - indent;
            final int xMax = colMax * iCellWidth + (iCellWidth - 1) - indent;

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
        final int yMin = cellPosY * iCellHeight + iCellBorderWidth;
        final int yMax = cellPosY * iCellHeight + (iCellHeight - 1) - iCellBorderWidth;

        final int xMin = cellPosX * iCellWidth + iCellBorderWidth;
        final int xMax = cellPosX * iCellWidth + (iCellWidth - 1) - iCellBorderWidth;

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

    public void drawRectangle(final Position topLeft, final Position bottomRight, final int color)
    {
        iRectangles.add(new Rectangle(topLeft, bottomRight, color));
    }

    public BufferedImage toBufferedImage()
    {
        BufferedImage image = new BufferedImage(iXSize, iYSize, BufferedImage.TYPE_INT_RGB);
        final WritableRaster raster = image.getRaster();
        raster.setDataElements(0, 0, iXSize, iYSize, iData);

        for (final Rectangle rectangle : iRectangles)
        {
            final int ARGBValue = rectangle.iColor;
            final Color color = ColorUtils.toAWTColor(ARGBValue);

            image = BufferedImageUtils.rectangleOverlay(
                    image,
                    rectangle.iTopLeft.getColumn() * iCellWidth,
                    rectangle.iTopLeft.getRow() * iCellHeight,
                    (Math.abs(rectangle.iBottomRight.getColumn() - rectangle.iTopLeft.getColumn()) + 1) * iCellWidth,
                    (Math.abs(rectangle.iBottomRight.getRow() - rectangle.iTopLeft.getRow()) + 1) * iCellHeight + 1,
                    color);
        }

        for (final Label label : iLabels)
        {
            final int ARGBValue = label.iColor;
            final Color color = ColorUtils.toAWTColor(ARGBValue);

            image = BufferedImageUtils.textOverlay(
                    image,
                    label.iText,
                    label.iCellPosX * iCellWidth + (iCellWidth / 3),
                    label.iCellPosY * iCellHeight + (iCellHeight - 1) - (iCellWidth / 3),
                    color,
                    label.iFontSize);
        }

        return image;
    }


}
