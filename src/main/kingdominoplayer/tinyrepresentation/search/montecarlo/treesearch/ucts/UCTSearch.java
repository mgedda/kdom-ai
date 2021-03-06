package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
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

import java.util.ArrayList;


public class UCTSearch
{
    /**
     * Number of times a node must have been visited
     * before any of its children are expanded.
     */
    private static final int MIN_VISITS_BEFORE_EXPAND_CHILD = 0;

    private final String CLASS_STRING = "[" + getClass().getSimpleName() + "]";

    private final String iPlayerName;
    private final TinySimulationStrategy iSimulationStrategy;
    private final SearchParameters iSearchParameters;
    private final UCB iUCB;

    private double iNumPlayoutsPerSecond = -1;

    public UCTSearch(final String playerName,
                     final TinySimulationStrategy simulationStrategy,
                     final SearchParameters searchParameters,
                     final UCB ucb)
    {
        iPlayerName = playerName;
        iSimulationStrategy = simulationStrategy;
        iSearchParameters = searchParameters;
        iUCB = ucb;
    }


    /**
     * @param gameState
     * @param moves
     * @return the max scoring move
     */
    public byte[] evaluate(final TinyGameState gameState, final byte[] moves)
    {
        DEBUG.println(CLASS_STRING + " " + iPlayerName + " searching...");

        assert gameState.getPlayerTurn().equals(iPlayerName) : "Player mismatch";
        final UCTSNode root = new UCTSNode(gameState, null, new byte[0]);

        final int numMoves = moves.length / TinyConst.MOVE_ELEMENT_SIZE;
        final long searchStartTime = System.nanoTime();
        while (root.getVisits() <= iSearchParameters.getMaxNumPlayouts()
                && getSeconds(System.nanoTime() - searchStartTime) < iSearchParameters.getMaxSearchTime()
                )
        {
            final UCTSNode node = treePolicy(root);
            final String winner = defaultPolicy(node);
            backupResult(node, winner);

            if (root.getVisits() == 20)
            {
                //FlameGraphPlotter.plot(root);
                Util.noop();
            }
        }

        final long searchEndTime = System.nanoTime();
        final double searchDurationSeconds = (searchEndTime - searchStartTime) / 1e9d;
        final String searchDurationString = String.format("%.3f", searchDurationSeconds);

        iNumPlayoutsPerSecond = (root.getVisits() - 1) / searchDurationSeconds;

        final UCTSNode selectedChild = bestChild(root, new UCB(0.0));

        printSearchResult(root, numMoves, searchDurationString, selectedChild);
        //FlameGraphPlotter.plot(root);

        //noinspection UnnecessaryLocalVariable
        final byte[] selectedMove = selectedChild.getMove();

        return selectedMove;
    }

    private void printSearchResult(final UCTSNode root,
                                   final int numMoves,
                                   final String searchDurationString,
                                   final UCTSNode selectedNode)
    {
        DEBUG.println(CLASS_STRING + " Search finished! (moves: " + Integer.toString(numMoves) +
                ", playouts: " + Long.toString(root.getVisits() - 1) +
                ", time: " + searchDurationString + "s)" +
                ", playouts/s: " + String.format("%.3f", iNumPlayoutsPerSecond));

        DEBUG.println(CLASS_STRING + "------------------------------------------------------------");
        int counter = 0;
        for (final UCTSNode child : root.getChildren())
        {
            final ArrayList<Integer> depthIndices = new ArrayList<>(1);
            depthIndices.add(counter);
            final String markString = child == selectedNode ? " (*)" : "";
            DEBUG.println(CLASS_STRING + child.toStringNestedBrackets(depthIndices, iUCB) + markString);
            counter++;
        }
        DEBUG.println(CLASS_STRING + "------------------------------------------------------------");
    }


    private UCTSNode treePolicy(UCTSNode node)
    {
        while (! node.isTerminal())
        {
            final boolean isRoot = node.getParent() == null;
            if (node.getVisits() <= MIN_VISITS_BEFORE_EXPAND_CHILD && !isRoot)
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
                final UCTSNode bestChild = bestChild(node, iUCB);
                node = bestChild;
            }
        }

        return node;
    }

    private UCTSNode bestChild(final UCTSNode node, final UCB ucb)
    {
        double maxUCB = -Double.MAX_VALUE;
        final ArrayList<UCTSNode> bestChildren = new ArrayList<>(10);

        for (final UCTSNode child : node.getChildren())
        {
            final double upperConfidenceBound = ucb.getUCB(child);

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
            final ArrayList<UCTSNode> childrenWithHighestPlayerScore = new ArrayList<>();
            int maxScore = 0;

            for (final UCTSNode bestChild : bestChildren)
            {
                final int score = bestChild.getGameState().getScore(iPlayerName);
                if (score > maxScore)
                {
                    maxScore = score;
                    childrenWithHighestPlayerScore.clear();
                    childrenWithHighestPlayerScore.add(bestChild);
                }
                else if (score == maxScore)
                {
                    childrenWithHighestPlayerScore.add(bestChild);
                }
            }

            final int randomIndex = Random.getInt(childrenWithHighestPlayerScore.size());

            return childrenWithHighestPlayerScore.get(randomIndex);
        }
        else
        {
            return bestChildren.get(0);
        }
    }

    private UCTSNode expand(final UCTSNode node)
    {
        assert !node.isFullyExpanded() : "Node is already fully expanded";

        final byte[] availableMoves = node.getGameState().getAvailableMoves(node.getPlayerTurn());
        final int moveIndex = node.getChildren().size();

        final byte[] move = new byte[TinyConst.MOVE_ELEMENT_SIZE];
        System.arraycopy(availableMoves, moveIndex * TinyConst.MOVE_ELEMENT_SIZE, move, 0, TinyConst.MOVE_ELEMENT_SIZE);

        final TinyGameState gameState = node.getGameState().makeMove(node.getPlayerTurn(), move);
        final UCTSNode child = new UCTSNode(gameState, node, move);
        node.addChild(child);

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

            if (node.getParent() != null && node.getParent().getPlayerTurn().equals(winner))
            {
                node.increaseWins();
            }
            node.increaseVisits();

            node = node.getParent();
        }
    }



    private String playout(final UCTSNode node)
    {
        TinyGameState gameState = node.getGameState();

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
