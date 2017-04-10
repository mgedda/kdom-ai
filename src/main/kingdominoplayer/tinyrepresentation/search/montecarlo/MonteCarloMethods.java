package kingdominoplayer.tinyrepresentation.search.montecarlo;

import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation.PlayoutScoringFunction;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinySimulationStrategy;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-20<br>
 * Time: 16:27<br><br>
 */
public class MonteCarloMethods
{
    public static byte[] getMoveWithHighestScore(final ArrayList<TinyMoveAverageScorePair> moveScores)
    {
        moveScores.sort((TinyMoveAverageScorePair moveScore1, TinyMoveAverageScorePair moveScore2) ->
        {
            final double s1 = moveScore1.getAverageScore();
            final double s2 = moveScore2.getAverageScore();

            return s1 > s2 ? -1 : s2 > s1 ? 1 : 0;
        });

        return moveScores.get(0).getMove();
    }

    public static void printMoveScores(final Collection<TinyMoveAverageScorePair> moveScores,
                                       final String classString,
                                       final int numMoves,
                                       final long playOutCounter,
                                       final String searchDurationString,
                                       final double searchDurationSeconds)
    {
        DEBUG.println(classString + " Search finished! (moves: " + Integer.toString(numMoves) +
                ", playouts: " + Long.toString(playOutCounter - 1) +
                ", time: " + searchDurationString + "s)" +
                ", playouts/s: " + String.format("%.3f", (playOutCounter - 1) / searchDurationSeconds));

        DEBUG.println(classString + "------------------------------------------------------------");
        for (final TinyMoveAverageScorePair moveScorePair : moveScores)
        {
            final String scoreString = String.format("%.3f", moveScorePair.getAverageScore());
            DEBUG.println(classString + " move: " + Integer.toString(moveScorePair.getMove()[TinyConst.MOVE_NUMBER_INDEX])
                    + ", score: " + scoreString
                    + ", playouts: " + Integer.toString(moveScorePair.getPlayouts()));
        }
        DEBUG.println(classString + "------------------------------------------------------------");
    }


    public static double playOut(final byte[] move,
                                 final TinyGameState gameState,
                                 final String playerName,
                                 final TinySimulationStrategy simulationStrategy,
                                 final PlayoutScoringFunction playoutScoringFunction)
    {
        // Player carries out move to playout.
        //
        TinyGameState searchState = gameState.makeMove(playerName, move);

        // Play game from new state until end.
        //
        while (! searchState.isGameOver())
        {
            final String playerTurn = searchState.getPlayerTurn();
            final byte[] availableMoves = searchState.getAvailableMoves(playerTurn);
            final byte[] selectedMove = simulationStrategy.selectMove(playerName, playerTurn, availableMoves, searchState);

            searchState = searchState.makeMove(playerTurn, selectedMove);
        }

        //noinspection UnnecessaryLocalVariable
        final double moveScore = playoutScoringFunction.applyTo(searchState, playerName);

        return moveScore;
    }


    public static double[] getWinDrawLossArrayFromIndexedScores(final int[] scores)
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
                if (j == i)
                {
                    continue;
                }

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
