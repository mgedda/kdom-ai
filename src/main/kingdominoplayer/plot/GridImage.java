package kingdominoplayer.plot;

import kingdominoplayer.datastructures.Position;

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

    private final int iCellWidth = 20;
    private final int iCellHeight = 20;

    private final int iNumCellsX;
    private final int iNumCellsY;
    private final int iXSize;
    private final int iYSize;

    private ArrayList<Label> iLabels = new ArrayList<>();

    private class Label
    {
        final String iText;
        final int iCellPosX;
        final int iCellPosY;
        final int iFontSize;

        private Label(final String text, final int cellPosX, final int cellPosY, int fontSize)
        {
            iText = text;
            iCellPosX = cellPosX;
            iCellPosY = cellPosY;
            iFontSize = fontSize;
        }
    }

    public GridImage(final int numCellsX, final int numCellsY)
    {
        iNumCellsX = numCellsX;
        iNumCellsY = numCellsY;

        iXSize = iNumCellsX * iCellWidth;
        iYSize = iNumCellsY * iCellHeight;

        iData = new int[iXSize * iYSize];

        clear();
    }

    public void drawCell(final Position position, final TileType tileType)
    {
        drawCell(position.getColumn(), position.getRow(), tileType);
    }

    public void drawCell(final int cellPosX, final int cellPosY, final TileType tileType)
    {
        final int yMin = cellPosY * iCellHeight + 3;
        final int yMax = yMin + iCellHeight - 3;

        final int xMin = cellPosX * iCellWidth + 3;
        final int xMax = xMin + iCellWidth - 3;

        for (int y = yMin; y <= yMax; ++y)
        {
            for (int x = xMin; x <= xMax; ++x)
            {
                iData[x + y * iXSize] = tileType.getColor();
            }
        }
    }

    public void drawHeader(final String string, final int cellPosX, final int cellPosY)
    {
        iLabels.add(new Label(string, cellPosX, cellPosY, 16));
    }

    public void drawLabel(final String string, final int cellPosX, final int cellPosY)
    {
        iLabels.add(new Label(string, cellPosX, cellPosY, 12));
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
            image = BufferedImageUtils.textOverlay(
                    image,
                    label.iText,
                    label.iCellPosX * iCellWidth + 4,
                    label.iCellPosY * iCellHeight + iCellHeight - 4,
                    Color.black,
                    label.iFontSize);
        }

        return image;
    }

}
