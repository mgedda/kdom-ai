package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch;

import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.strategies.TinyStrategy;
import kingdominoplayer.utils.Random;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-20<br>
 * Time: 14:55<br><br>
 */
public class TinyMonteCarloTreeSearchAlgorithm
{
    private static final double MAX_SIMULATION_TIME_SECONDS = 10d;   // maximum time for one move
    private static final long PLAYOUT_FACTOR = 1000000;              // number of desired playouts per move

    private final String CLASS_STRING = "[" + getClass().getSimpleName() + "]";

    private final String iPlayerName;
    private final TinyStrategy iPlayerStrategy;
    private final TinyStrategy iOpponentStrategy;

    public TinyMonteCarloTreeSearchAlgorithm(final String playerName,
                                             final TinyStrategy playerStrategy,
                                             final TinyStrategy opponentStrategy)
    {
        iPlayerName = playerName;
        iPlayerStrategy = playerStrategy;
        iOpponentStrategy = opponentStrategy;
    }


    /**
     *
     * @param gameState
     * @param moves
     * @return the max scoring move
     */
    public byte[] evaluate(final TinyGameState gameState, final byte[] moves)
    {
        DEBUG.println("\n" + CLASS_STRING + " " + iPlayerName + " searching...");

        final MCTSNode root = new MCTSNode(gameState, null, new byte[0]);

        final long searchStartTime = System.nanoTime();

        final int numMoves = moves.length / TinyConst.MOVE_ELEMENT_SIZE;
        final long numPlayOuts = PLAYOUT_FACTOR * numMoves;  // max X playouts per move
        long playOutCounter = 1;
        while (playOutCounter <= numPlayOuts
                && getSeconds(System.nanoTime() - searchStartTime) < MAX_SIMULATION_TIME_SECONDS)
        {

            final MCTSResult result = applyMCTS(root);
            root.updateResult(result);
        }

        final long searchEndTime = System.nanoTime();
        final double searchDurationSeconds = (searchEndTime - searchStartTime) / 1e9d;
        final String searchDurationString = String.format("%.3f", searchDurationSeconds);

        DEBUG.println(CLASS_STRING + " Search finished! (moves: " + Integer.toString(numMoves) +
                ", playouts: " + Long.toString(playOutCounter - 1) +
                ", time: " + searchDurationString + "s)" +
                ", playouts/s: " + String.format("%.3f", (playOutCounter - 1) / searchDurationSeconds));


        //noinspection UnnecessaryLocalVariable
        final byte[] bestMove = getBestChild(root).getMove();

        return bestMove;
    }


    private MCTSResult applyMCTS(final MCTSNode node)
    {
        final MCTSNode bestChild = getBestChild(node);

        final MCTSResult result;
        if (! bestChild.isExpanded())
        {
            result = playout(bestChild);
        }
        else
        {
            result = applyMCTS(bestChild);
        }

        bestChild.updateResult(result);

        return result;
    }


    public MCTSNode getBestChild(final MCTSNode node)
    {
        final ArrayList<MCTSNode> children = getChildren(node);

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
            final String playerTurn = gameState.getPlayerTurn();
            final byte[] availableMoves = gameState.getAvailableMoves(playerTurn);

            final int numAvailableMoves = availableMoves.length / TinyConst.MOVE_ELEMENT_SIZE;

            children = new ArrayList<>(numAvailableMoves);

            for (int i = 0; i < numAvailableMoves; ++i)
            {
                final byte[] move = TinyGameState.getRow(availableMoves, i, TinyConst.MOVE_ELEMENT_SIZE);
                final TinyGameState nextGameState = gameState.makeMove(playerTurn, move);
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
        if (! node.isExpanded())
        {
            return 0;
        }

        final int parentVisits = node.getParent().getVisits();
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
        final double[] result = getResultArrayFromIndexedScores(scores);

        return new MCTSResult(result);
    }

    private double[] getResultArrayFromIndexedScores(final int[] scores)
    {
        final int numPlayers = scores.length;
        final double[] result = new double[numPlayers];

        for (int i = 0; i < numPlayers; ++i)
        {
            final int playerScore = scores[i];

            boolean win = true;
            int draw = 0;
            for (int j = 0; j < numPlayers; ++j)
            {
                if (scores[j] > playerScore)
                {
                    win = false;
                    break;
                }

                if (scores[j] == playerScore)
                {
                    draw++;
                }
            }

            final double score = win
                    ? draw > 0 ? 1.0 / (double) (draw + 1) : 1.0
                    : 0.0;

            result[i] = score;
        }
        return result;
    }


    private byte[] getPlayoutMove(final String player, final TinyGameState gameState)
    {
        final byte[] availableMoves = gameState.getAvailableMoves(player);

        //noinspection UnnecessaryLocalVariable
        final byte[] move = player.equals(iPlayerName)
                ? iPlayerStrategy.selectMove(player, availableMoves, gameState)
                : iOpponentStrategy.selectMove(player, availableMoves, gameState);

        return move;
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
