package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts;

/*
 * Copyright 2018 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2018-01-05<br>
 * Time: 09:17<br><br>
 */

import kingdominoplayer.utils.plot.BufferedImageUtils;
import kingdominoplayer.utils.plot.BufferedImageViewer;
import kingdominoplayer.utils.plot.ColorUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/*package*/ class FlameGraphPlotter
{
    private static int cRectCounter = 0;
    private static int[] cColors = {
            0xFFFFB14E,
            0xFFFFA534,
            0xFFE48309,
            0xFFAE6406,
    };


    private static class Rectangle
    {
        final int iColor;
        final int iX1;
        final int iX2;
        final int iY1;
        final int iY2;
        final Label iLabel;

        private Rectangle(final int x1, final int x2, final int y1, final int y2, final int color, final Label label)
        {
            iColor = color;
            iX1 = x1;
            iX2 = x2;
            iY1 = y1;
            iY2 = y2;
            iLabel = label;
        }
    }


    private static class Label
    {
        final String iText;
        final int iPosX;
        final int iPosY;
        final int iColor;
        final int iFontSize;

        private Label(final String text, final int posX, final int posY, int color, int fontSize)
        {
            iText = text;
            iPosX = posX;
            iPosY = posY;
            iColor = color;
            iFontSize = fontSize;
        }
    }


    public static void plot(final UCTSNode root)
    {
        final int xSize = 1000;
        final int ySize = 800;
        final int borderSize = 10;

        final int pixels[] = new int[xSize * ySize];

        final int maxDepth = getMaxDepth(root, 1);
        final int boxHeight = (ySize - 2 * borderSize) / maxDepth;

        final int rootWidth = (xSize - 2 * borderSize);

        final int rootVisits = root.getVisits();
        final double visitWidth = rootWidth / (double) rootVisits;

        final int x0 = borderSize;
        final int y0 = ySize - borderSize;

        final ArrayList<Rectangle> rectangles = getRectangles(root, 0, 0, x0, y0, visitWidth, boxHeight, borderSize);

        for (final Rectangle rectangle : rectangles)
        {
            for (int y = rectangle.iY1; y < rectangle.iY2; y++)
            {
                for (int x = rectangle.iX1; x < rectangle.iX2; x++)
                {
                    pixels[x + y * xSize] = rectangle.iColor;
                }
            }
        }

        BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        final WritableRaster raster = image.getRaster();
        raster.setDataElements(0, 0, xSize, ySize, pixels);

        for (final Rectangle rectangle : rectangles)
        {
            final Label label = rectangle.iLabel;
            final int ARGBValue = label.iColor;
            final Color color = ColorUtils.toAWTColor(ARGBValue);

            image = BufferedImageUtils.textOverlay(
                    image,
                    label.iText,
                    label.iPosX,
                    label.iPosY,
                    color,
                    label.iFontSize);
        }


        BufferedImageViewer.displayImage(image, "FlameGraph");

        System.out.println("");

        /*
        for (final GridImage.Label label : iLabels)
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
        */
    }

    private static ArrayList<Rectangle> getRectangles(final UCTSNode node,
                                                      final int childIndex,
                                                      final int treeDepth,
                                                      final int x,
                                                      final int y,
                                                      final double visitWidth,
                                                      final int boxHeight,
                                                      final int borderSize)
    {
        final int rectWidth = (int) (node.getVisits() * visitWidth);
        final int x1 = x + borderSize;
        final int x2 = x + rectWidth + borderSize;
        final int y1 = y - boxHeight;
        final int y2 = y;
        final int rectColor = cColors[cRectCounter % cColors.length];

        final String labelString = "depth=" + treeDepth + ", i=" + childIndex + ", visits=" + node.getVisits();
        final Label label = new Label(labelString, x1, y1, 0xFF000000, 12);

        final Rectangle nodeRect = new Rectangle(x1, x2, y1, y2, rectColor, label);
        cRectCounter++;

        final ArrayList<Rectangle> rectangles = new ArrayList<>();
        rectangles.add(nodeRect);

        int accVisits = 0;
        int childCounter = 0;
        for (UCTSNode child : node.getChildren())
        {
            rectangles.addAll(getRectangles(child, childCounter++, treeDepth + 1, (int) (accVisits * visitWidth), y - boxHeight, visitWidth, boxHeight, borderSize));
            accVisits += child.getVisits();
        }

        return rectangles;
    }


    private static int getMaxDepth(final UCTSNode node, final int depth)
    {
        if (node.getChildren().isEmpty())
        {
            return depth;
        }

        int maxDepth = depth;
        for (UCTSNode child : node.getChildren())
        {
            maxDepth = Math.max(maxDepth, getMaxDepth(child, depth + 1));
        }

        return maxDepth;
    }
}
