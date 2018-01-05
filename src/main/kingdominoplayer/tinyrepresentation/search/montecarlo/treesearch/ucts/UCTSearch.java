package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts;

/*
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-07-04<br>
 * Time: 21:44<br><br>
 */

import kingdominoplayer.SearchParameters;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.search.montecarlo.MonteCarloMethods;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinySimulationStrategy;
import kingdominoplayer.utils.Random;
import kingdominoplayer.utils.Util;
import kingdominoplayer.utils.plot.BufferedImageUtils;
import kingdominoplayer.utils.plot.BufferedImageViewer;
import kingdominoplayer.utils.plot.ColorUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;


public class UCTSearch
{
    private static final double EXPLORE_FACTOR = 1.0 / Math.sqrt(2.0);


    /**
     * Number of times a node must have been visited
     * before any of its children are expanded.
     */
    private static final int MIN_VISITS_BEFORE_EXPAND_CHILD = 0;

    private final String CLASS_STRING = "[" + getClass().getSimpleName() + "]";

    private final String iPlayerName;
    private final TinySimulationStrategy iSimulationStrategy;
    private final SearchParameters iSearchParameters;

    private double iNumPlayoutsPerSecond = -1;

    public UCTSearch(final String playerName,
                     final TinySimulationStrategy simulationStrategy,
                     final SearchParameters searchParameters)
    {
        iPlayerName = playerName;
        iSimulationStrategy = simulationStrategy;
        iSearchParameters = searchParameters;
    }


    /**
     * @param gameState
     * @param moves
     * @return the max scoring move
     */
    public byte[] evaluate(final TinyGameState gameState, final byte[] moves)
    {
        DEBUG.println(CLASS_STRING + " " + iPlayerName + " searching...");

        final UCTSNode root = new UCTSNode(gameState, null, new byte[0], iPlayerName);

        final int numMoves = moves.length / TinyConst.MOVE_ELEMENT_SIZE;
        final long searchStartTime = System.nanoTime();
        while (root.visits <= iSearchParameters.getMaxNumPlayouts()
                && getSeconds(System.nanoTime() - searchStartTime) < iSearchParameters.getMaxSearchTime()
                )
        {
            final UCTSNode node = treePolicy(root);
            final String winner = defaultPolicy(node);
            backupResult(node, winner);

            if (root.visits == 20)
            {
                DEBUG.plotFlameGraph(root);
                Util.noop();
            }
        }

        final long searchEndTime = System.nanoTime();
        final double searchDurationSeconds = (searchEndTime - searchStartTime) / 1e9d;
        final String searchDurationString = String.format("%.3f", searchDurationSeconds);

        iNumPlayoutsPerSecond = (root.visits - 1) / searchDurationSeconds;

        final UCTSNode selectedChild = bestChild(root, 0.0);

        printSearchResult(root, numMoves, searchDurationString, selectedChild);
        DEBUG.plotFlameGraph(root);

        //noinspection UnnecessaryLocalVariable
        final byte[] selectedMove = selectedChild.move;

        return selectedMove;
    }

    private void printSearchResult(final UCTSNode root,
                                   final int numMoves,
                                   final String searchDurationString,
                                   final UCTSNode selectedNode)
    {
        DEBUG.println(CLASS_STRING + " Search finished! (moves: " + Integer.toString(numMoves) +
                ", playouts: " + Long.toString(root.visits - 1) +
                ", time: " + searchDurationString + "s)" +
                ", playouts/s: " + String.format("%.3f", iNumPlayoutsPerSecond));

        DEBUG.println(CLASS_STRING + "------------------------------------------------------------");
        int counter = 0;
        for (final UCTSNode child : root.children)
        {
            final ArrayList<Integer> depthIndices = new ArrayList<>(1);
            depthIndices.add(counter);
            final String markString = child == selectedNode ? " (*)" : "";
            DEBUG.println(CLASS_STRING + UCTSTreeUtils.getNodeString(child, depthIndices, EXPLORE_FACTOR) + markString);
            counter++;
        }
        DEBUG.println(CLASS_STRING + "------------------------------------------------------------");
    }


    private UCTSNode treePolicy(UCTSNode node)
    {
        while (! node.isTerminal())
        {
            if (node.visits <= MIN_VISITS_BEFORE_EXPAND_CHILD && node.parent != null)
            {
                return node;
            }

            if (! node.isFullyExpanded())
            {
                return expand(node);
            }
            else
            {
                //noinspection UnnecessaryLocalVariable
                final UCTSNode bestChild = bestChild(node, EXPLORE_FACTOR);
                node = bestChild;
            }
        }

        return node;
    }

    private UCTSNode bestChild(final UCTSNode node, final double exploreFactor)
    {
        double maxUCB = 0;
        final ArrayList<UCTSNode> bestChildren = new ArrayList<>(10);

        for (final UCTSNode child : node.children)
        {
            final double upperConfidenceBound = UCTSTreeUtils.getUCB(child, exploreFactor);

            if (upperConfidenceBound == maxUCB)
            {
                bestChildren.add(child);
            }

            if (upperConfidenceBound > maxUCB)
            {
                bestChildren.clear();
                bestChildren.add(child);

                maxUCB = upperConfidenceBound;
            }
        }

        if (bestChildren.size() > 1)
        {
            final int numBestChildren = bestChildren.size();
            int randomIndex = Random.getInt(numBestChildren);

            return bestChildren.get(randomIndex);
        }
        else
        {
            return bestChildren.get(0);
        }
    }

    private UCTSNode expand(final UCTSNode node)
    {
        assert !node.isFullyExpanded() : "Node is already fully expanded";

        final byte[] availableMoves = node.gameState.getAvailableMoves(node.playerName);
        final int moveIndex = node.children.size();

        final byte[] move = new byte[TinyConst.MOVE_ELEMENT_SIZE];
        System.arraycopy(availableMoves, moveIndex * TinyConst.MOVE_ELEMENT_SIZE, move, 0, TinyConst.MOVE_ELEMENT_SIZE);

        final TinyGameState gameState = node.gameState.makeMove(node.playerName, move);
        final UCTSNode child = new UCTSNode(gameState, node, move);
        node.children.add(child);

        return child;
    }

    private String defaultPolicy(final UCTSNode node)
    {
        return playout(node);
    }


    private void backupResult(UCTSNode node, final String winner)
    {
        while (node != null)
        {
            //node.reward = node.reward.add(reward);

            if (node.parent != null && node.parent.playerName.equals(winner))
            {
                node.wins++;
            }
            node.visits++;

            node = node.parent;
        }
    }



    private String playout(final UCTSNode node)
    {
        TinyGameState gameState = node.gameState;

        while (! gameState.isGameOver())
        {
            final String player = gameState.getPlayerTurn();
            final byte[] move = getPlayoutMove(player, gameState);
            gameState = gameState.makeMove(player, move);
        }

        //noinspection UnnecessaryLocalVariable
        //final Reward result = evaluateResult(gameState);
        //noinspection UnnecessaryLocalVariable
        final String winner = gameState.getPlayerWithHighestScore();

        return winner;
    }

    private UCTSReward evaluateResult(final TinyGameState gameState)
    {
        final int[] scores = gameState.getScoresIndexed();
        final double[] result = MonteCarloMethods.getWinDrawLossArrayFromIndexedScores(scores);

        return new UCTSReward(result);
    }

    private byte[] getPlayoutMove(final String player, final TinyGameState gameState)
    {
        final byte[] availableMoves = gameState.getAvailableMoves(player);

        //noinspection UnnecessaryLocalVariable
        final byte[] move = iSimulationStrategy.selectMove(iPlayerName, player, availableMoves, gameState);

        return move;
    }


    public double getNumPlayoutsPerSecond()
    {
        return iNumPlayoutsPerSecond;
    }

    private double getSeconds(final long nanoTime)
    {
        return nanoTime / 1e9d;
    }






    private static class DEBUG
    {
        private static final boolean DEBUG = true;

        private static int cRectCounter = 0;
        private static int[] cColors = {
                0xFFFFB14E,
                0xFFFFA534,
                0xFFE48309,
                0xFFAE6406,
        };


        private static void print(final String string)
        {
            if (DEBUG)
            {
                System.out.print(string);
            }
        }

        private static void println(final String string)
        {
            print(string + "\n");
        }


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


        private static void plotFlameGraph(final UCTSNode root)
        {
            if (DEBUG)
            {
                final int xSize = 1000;
                final int ySize = 800;
                final int borderSize = 10;

                final int pixels[] = new int[xSize * ySize];

                final int maxDepth = getMaxDepth(root, 1);
                final int boxHeight = (ySize - 2 * borderSize) / maxDepth;

                final int rootWidth = (xSize - 2 * borderSize);

                final int rootVisits = root.visits;
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
            final int rectWidth = (int) (node.visits * visitWidth);
            final int x1 = x + borderSize;
            final int x2 = x + rectWidth + borderSize;
            final int y1 = y - boxHeight;
            final int y2 = y;
            final int rectColor = cColors[cRectCounter % cColors.length];

            final String labelString = "depth=" + treeDepth + ", i=" + childIndex + ", visits=" + node.visits;
            final Label label = new Label(labelString, x1, y1, 0xFF000000, 12);

            final Rectangle nodeRect = new Rectangle(x1, x2, y1, y2, rectColor, label);
            cRectCounter++;

            final ArrayList<Rectangle> rectangles = new ArrayList<>();
            rectangles.add(nodeRect);

            int accVisits = 0;
            int childCounter = 0;
            for (UCTSNode child : node.children)
            {
                rectangles.addAll(getRectangles(child, childCounter++, treeDepth + 1, (int) (accVisits * visitWidth), y - boxHeight, visitWidth, boxHeight, borderSize));
                accVisits += child.visits;
            }

            return rectangles;
        }


        private static int getMaxDepth(final UCTSNode node, final int depth)
        {
            if (node.children.isEmpty())
            {
                return depth;
            }

            int maxDepth = depth;
            for (UCTSNode child : node.children)
            {
                maxDepth = Math.max(maxDepth, getMaxDepth(child, depth + 1));
            }

            return maxDepth;
        }
    }

}
