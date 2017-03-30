package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch;

import kingdominoplayer.SearchParameters;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.search.montecarlo.MonteCarloMethods;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategy;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinySimulationStrategy;
import kingdominoplayer.utils.Random;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-20<br>
 * Time: 14:55<br><br>
 */
public class TinyMonteCarloTreeSearchAlgorithm
{
    private static final int NODE_VISITS_BEFORE_EXPAND_CHILD = 0;   // number of times a node must have been visited
                                                                     // before any of its children are expanded.

    private final String CLASS_STRING = "[" + getClass().getSimpleName() + "]";

    private final String iPlayerName;
    private final TinySimulationStrategy iSimulationStrategy;
    private final SearchParameters iSearchParameters;

    private double iNumPlayoutsPerSecond = -1;

    public TinyMonteCarloTreeSearchAlgorithm(final String playerName,
                                             final TinySimulationStrategy simulationStrategy,
                                             final SearchParameters searchParameters)
    {
        iPlayerName = playerName;
        iSimulationStrategy = simulationStrategy;
        iSearchParameters = searchParameters;
    }


    /**
     *
     * @param gameState
     * @param moves
     * @return the max scoring move
     */
    public byte[] evaluate(final TinyGameState gameState, final byte[] moves)
    {
        DEBUG.println(CLASS_STRING + " " + iPlayerName + " searching...");

        final MCTSNode root = new MCTSNode(gameState, null, new byte[0], iPlayerName);
        root.expand();

        final int numMoves = moves.length / TinyConst.MOVE_ELEMENT_SIZE;
        final long searchStartTime = System.nanoTime();
        while (root.getVisits() <= iSearchParameters.getMaxNumPlayouts()
                && getSeconds(System.nanoTime() - searchStartTime) < iSearchParameters.getMaxSearchTime()
                )
        {
            final MCTSResult result = applyMCTS(root);
            root.updateResult(result);
        }

        final long searchEndTime = System.nanoTime();
        final double searchDurationSeconds = (searchEndTime - searchStartTime) / 1e9d;
        final String searchDurationString = String.format("%.3f", searchDurationSeconds);

        iNumPlayoutsPerSecond = root.getVisits() / searchDurationSeconds;

        final MCTSNode selectedChild = getChildWithHighestWinsToVisitsRatio(root);

        printSearchResult(root, numMoves, searchDurationString, selectedChild);

        //noinspection UnnecessaryLocalVariable
        final byte[] selectedMove = selectedChild.getMove();

        return selectedMove;
    }

    private void printSearchResult(final MCTSNode root,
                                   final int numMoves,
                                   final String searchDurationString,
                                   final MCTSNode selectedNode)
    {
        DEBUG.println(CLASS_STRING + " Search finished! (moves: " + Integer.toString(numMoves) +
                ", playouts: " + Long.toString(root.getVisits()) +
                ", time: " + searchDurationString + "s)" +
                ", playouts/s: " + String.format("%.3f", iNumPlayoutsPerSecond));

        DEBUG.println(CLASS_STRING + "------------------------------------------------------------");
        int counter = 0;
        for (final MCTSNode child : root.getChildren())
        {
            final ArrayList<Integer> depthIndices = new ArrayList<>(1);
            depthIndices.add(counter);
            final String markString = child == selectedNode ? " (*)" : "";
            DEBUG.println(CLASS_STRING + getNodeString(child, depthIndices) + markString);
            counter++;
        }
        DEBUG.println(CLASS_STRING + "------------------------------------------------------------");
    }

    private MCTSNode getChildWithHighestWinsToVisitsRatio(final MCTSNode node)
    {
        final ArrayList<MCTSNode> maxScoreChildren = new ArrayList<>(10);
        double maxScore = 0;

        for (final MCTSNode child : node.getChildren())
        {
            if (! child.isExpanded())
            {
                continue;
            }

            final double score = (double) child.getWins() / (double) child.getVisits();

            if (score == maxScore)
            {
                maxScoreChildren.add(child);
            }

            if (score > maxScore)
            {
                maxScoreChildren.clear();

                maxScore = score;
                maxScoreChildren.add(child);
            }
        }


        if (maxScoreChildren.size() > 1)
        {
            final int numBestChildren = maxScoreChildren.size();
            int randomIndex = Random.getInt(numBestChildren);

            return maxScoreChildren.get(randomIndex);
        }
        else
        {
            return maxScoreChildren.get(0);
        }
    }


    private MCTSResult applyMCTS(final MCTSNode node)
    {
        if (node.getGameState().isGameOver())
        {
            return playout(node);
        }

        final ArrayList<MCTSNode> children = getChildren(node);
        final MCTSNode bestChild = getHighestUCBChild(children);

        final MCTSResult result;
        if (! bestChild.isExpanded())
        {
            if (node.getVisits() > NODE_VISITS_BEFORE_EXPAND_CHILD)
            {
                bestChild.expand();
            }
            result = playout(node);
        }
        else
        {
            result = applyMCTS(bestChild);
            bestChild.updateResult(result);
        }

        return result;
    }


    public MCTSNode getHighestUCBChild(final ArrayList<MCTSNode> children)
    {
        double maxUCB = 0;
        final ArrayList<MCTSNode> bestChildren = new ArrayList<>(10);

        for (final MCTSNode child : children)
        {
            final double upperConfidenceBound = getUpperConfidenceBound(child);

            if (upperConfidenceBound == maxUCB)
            {
                bestChildren.add(child);
            }

            if (upperConfidenceBound > maxUCB)
            {
                bestChildren.clear();

                maxUCB = upperConfidenceBound;
                bestChildren.add(child);
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


    private ArrayList<MCTSNode> getChildren(final MCTSNode node)
    {
        final ArrayList<MCTSNode> children;
        if (node.getChildren().isEmpty())
        {
            final TinyGameState gameState = node.getGameState();
            final String playerName = node.getPlayerName();
            final byte[] availableMoves = gameState.getAvailableMoves(playerName);

            final int numAvailableMoves = availableMoves.length / TinyConst.MOVE_ELEMENT_SIZE;

            children = new ArrayList<>(numAvailableMoves);

            for (int i = 0; i < numAvailableMoves; ++i)
            {
                final byte[] move = TinyGameState.getRow(availableMoves, i, TinyConst.MOVE_ELEMENT_SIZE);
                final TinyGameState nextGameState = gameState.makeMove(playerName, move);
                final MCTSNode child = new MCTSNode(nextGameState, node, move);

                children.add(child);
            }

            node.setChildren(children);
        }
        else
        {
            children = node.getChildren();
        }
        return children;
    }


    private double getUpperConfidenceBound(final MCTSNode node)
    {
        final int parentVisits = node.getParent().getVisits();

        if (! node.isExpanded())
        {
            // Make UCB same as if node would have 1 visit.
            //
            return Math.sqrt(2.0 * Math.log(parentVisits));
        }

        final double averageScore = (double) node.getWins() / (double) node.getVisits();

        return averageScore + Math.sqrt(2.0 * Math.log(parentVisits) / (double) node.getVisits());
    }


    private MCTSResult playout(final MCTSNode node)
    {
        TinyGameState gameState = node.getGameState();

        while (! gameState.isGameOver())
        {
            final String player = gameState.getPlayerTurn();
            final byte[] move = getPlayoutMove(player, gameState);
            gameState = gameState.makeMove(player, move);
        }

        //noinspection UnnecessaryLocalVariable
        final MCTSResult result = evaluateResult(gameState);

        return result;
    }

    private MCTSResult evaluateResult(final TinyGameState gameState)
    {
        final int[] scores = gameState.getScoresIndexed();
        final double[] result = MonteCarloMethods.getWinDrawLossArrayFromIndexedScores(scores);

        return new MCTSResult(result);
    }

    private byte[] getPlayoutMove(final String player, final TinyGameState gameState)
    {
        final byte[] availableMoves = gameState.getAvailableMoves(player);

        //noinspection UnnecessaryLocalVariable
        final byte[] move = iSimulationStrategy.selectMove(player, availableMoves, gameState);

        return move;
    }


    private double getSeconds(final long nanoTime)
    {
        return nanoTime / 1e9d;
    }

    public double getNumPlayoutsPerSecond()
    {
        return iNumPlayoutsPerSecond;
    }


    private void printTree(final MCTSNode root)
    {
        System.out.println("Monte-Carlo Search Tree:");
        printTree(root, new ArrayList<>());
    }


    private void printChildren(final MCTSNode node)
    {
        int counter = 0;
        for (final MCTSNode child : node.getChildren())
        {
            final ArrayList<Integer> depthIndices = new ArrayList<>();
            depthIndices.add(counter++);
            System.out.println(getNodeString(child, depthIndices));
        }
    }

    private void printTree(final MCTSNode node, final ArrayList<Integer> depthIndices)
    {
        if (! node.isExpanded())
        {
            return;
        }

        String nodeString = getNodeString(node, depthIndices);

        System.out.println(nodeString);


        final ArrayList<MCTSNode> children = node.getChildren();
        for (int i = 0; i < children.size(); ++i)
        {
            final ArrayList<Integer> indices = new ArrayList<>(depthIndices.size() + 1);
            indices.addAll(depthIndices);
            indices.add(i);

            printTree(children.get(i), indices);
        }
    }

    private void printTreeBFS(final MCTSNode root)
    {
        printTreeBFS(root, -1);
    }

    private void printTreeBFS(final MCTSNode root, final long numNodes)
    {
        final ArrayDeque<NodeDepthIndicesPair> nodeQueue = new ArrayDeque<>(1000);

        nodeQueue.add(new NodeDepthIndicesPair(root, new ArrayList<>()));

        long counter = 0;
        final long maxCount = numNodes == -1 ? Long.MAX_VALUE : numNodes;

        while (! nodeQueue.isEmpty() && counter++ < maxCount)
        {
            final NodeDepthIndicesPair current = nodeQueue.pop();

            final String nodeString = getNodeString(current.iNode, current.iDepthIndices);
            System.out.println(nodeString);

            final ArrayList<MCTSNode> children = current.iNode.getChildren();
            for (int i = 0; i < children.size(); ++i)
            {
                final ArrayList<Integer> depthIndices = new ArrayList<>();
                depthIndices.addAll(current.iDepthIndices);
                depthIndices.add(i);

                nodeQueue.add(new NodeDepthIndicesPair(children.get(i), depthIndices));
            }
        }
    }


    private class NodeDepthIndicesPair
    {
        MCTSNode iNode;
        ArrayList<Integer> iDepthIndices;

        public NodeDepthIndicesPair(final MCTSNode node, final ArrayList<Integer> depthIndices)
        {
            iNode = node;
            iDepthIndices = depthIndices;
        }
    }


    private String getNodeString(final MCTSNode node, final ArrayList<Integer> depthIndices)
    {
        String nodeString = "";

        if (! depthIndices.isEmpty())
        {
            nodeString = nodeString.concat("{");

            for (int i = 0; i < depthIndices.size(); ++i)
            {
                nodeString = nodeString.concat(Integer.toString(depthIndices.get(i)));

                if (i < depthIndices.size() - 1)
                {
                    nodeString = nodeString.concat(", ");
                }
            }
            nodeString = nodeString.concat("}");
        }

        final int wins = node.getWins();
        final int visits = node.getVisits();
        nodeString = nodeString.concat(" ").concat(Integer.toString(wins)).concat("/").concat(Integer.toString(visits));
        nodeString = visits > 0
                ? nodeString.concat(", Avg: ").concat(String.format("%.5f", wins/(double)visits))
                : nodeString.concat(", Avg: 0");

        if (node.getParent() != null)
        {
            final double upperConfidenceBound = getUpperConfidenceBound(node);
            nodeString = nodeString.concat(", UCB: ").concat(String.format("%.5f", upperConfidenceBound));
        }

        final String playerName = node.getPlayerName();
        nodeString = nodeString.concat(" [player: ").concat(playerName).concat("]");
        return nodeString;
    }


    private static class DEBUG
    {
        private static final boolean DEBUG = true;

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

    }
}
