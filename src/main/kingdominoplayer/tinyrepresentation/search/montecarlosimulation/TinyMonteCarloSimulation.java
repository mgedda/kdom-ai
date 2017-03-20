package kingdominoplayer.tinyrepresentation.search.montecarlosimulation;

import kingdominoplayer.tinyrepresentation.TinyUtils;
import kingdominoplayer.tinyrepresentation.strategies.TinyStrategy;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 13:46<br><br>
 */
public class TinyMonteCarloSimulation
{
    private static final double MAX_SIMULATION_TIME_SECONDS = 10d;   // maximum time for one move
    private static final int SIMULATION_BREADTH = 1000;              // max number of moves to evaluate
    private static final long PLAYOUT_FACTOR = 1000000;              // number of desired playouts per move

    private final String CLASS_STRING = "[" + getClass().getSimpleName() + "]";

    private final String iPlayerName;
    private final TinyStrategy iPlayerStrategy;
    private final TinyStrategy iOpponentStrategy;
    private final boolean iRelativeBranchScore;

    public TinyMonteCarloSimulation(final String playerName,
                                    final TinyStrategy playerStrategy,
                                    final TinyStrategy opponentStrategy,
                                    final boolean relativeBranchScore)
    {
        iPlayerName = playerName;
        iPlayerStrategy = playerStrategy;
        iOpponentStrategy = opponentStrategy;
        iRelativeBranchScore = relativeBranchScore;
    }

    /**
     *
     * @param gameState
     * @param moves
     * @return the max scoring move
     */
    public byte[] evaluate(final TinyGameState gameState, final byte[] moves)
    {
        final byte[] movesToEvaluate = TinyUtils.selectMovesRandomly(moves, SIMULATION_BREADTH);
        final ArrayList<TinyMoveAverageScorePair> moveScores = getMoveScores(gameState, movesToEvaluate);

        // Select move with best score.
        //
        return getMoveWithHighestScore(moveScores);
    }

    private byte[] getMoveWithHighestScore(final ArrayList<TinyMoveAverageScorePair> moveScores)
    {
        moveScores.sort((TinyMoveAverageScorePair moveScore1, TinyMoveAverageScorePair moveScore2) ->
        {
            final double s1 = moveScore1.getAverageScore();
            final double s2 = moveScore2.getAverageScore();

            return s1 > s2 ? -1 : s2 > s1 ? 1 : 0;
        });

        return moveScores.get(0).getMove();
    }

    private ArrayList<TinyMoveAverageScorePair> getMoveScores(final TinyGameState gameState, final byte[] moves)
    {
        DEBUG.println("\n" + CLASS_STRING + " " + iPlayerName + " searching...");

        final int numMoves = moves.length / TinyConst.MOVE_ELEMENT_SIZE;

        final LinkedHashMap<Byte, TinyMoveAverageScorePair> moveScoresMap = new LinkedHashMap<>(numMoves);
        for (int i = 0; i < numMoves; ++i)
        {
            final byte[] move = TinyGameState.getRow(moves, i, TinyConst.MOVE_ELEMENT_SIZE);
            moveScoresMap.put(move[TinyConst.MOVE_NUMBER_INDEX], new TinyMoveAverageScorePair(move));
        }

        final long searchStartTime = System.nanoTime();

        final long numPlayOuts = PLAYOUT_FACTOR * numMoves;  // max X playouts per move
        long playOutCounter = 1;
        while (playOutCounter <= numPlayOuts
                && getSeconds(System.nanoTime() - searchStartTime) < MAX_SIMULATION_TIME_SECONDS)
        {
            // Play out a random move.
            //
            final int randomIndex = Random.getInt(numMoves);
            final byte[] move = TinyGameState.getRow(moves, randomIndex, TinyConst.MOVE_ELEMENT_SIZE);
            final double score = playOut(move, gameState);
            moveScoresMap.get(move[TinyConst.MOVE_NUMBER_INDEX]).addScore(score);

            assert moveScoresMap.size() == numMoves : "Size discrepancy!";

            playOutCounter++;
        }


        final long searchEndTime = System.nanoTime();
        final double searchDurationSeconds = (searchEndTime - searchStartTime) / 1e9d;
        final String searchDurationString = String.format("%.3f", searchDurationSeconds);
        DEBUG.println(CLASS_STRING + " Search finished! (moves: " + Integer.toString(numMoves) +
                ", playouts: " + Long.toString(playOutCounter - 1) +
                ", time: " + searchDurationString + "s)" +
                ", playouts/s: " + String.format("%.3f", (playOutCounter - 1) / searchDurationSeconds));

        printMoveScores(moveScoresMap.values());

        assert moveScoresMap.size() == numMoves : "Size discrepancy!";

        final ArrayList<TinyMoveAverageScorePair> moveScores = new ArrayList<>(moveScoresMap.values().size());
        moveScores.addAll(moveScoresMap.values());

        return moveScores;
    }


    private double getSeconds(final long nanoTime)
    {
        return nanoTime / 1e9d;
    }


    private void printMoveScores(final Collection<TinyMoveAverageScorePair> moveScores)
    {
        DEBUG.println(CLASS_STRING + "------------------------------------------------------------");
        for (final TinyMoveAverageScorePair moveScorePair : moveScores)
        {
            final String scoreString = String.format("%.3f", moveScorePair.getAverageScore());
            DEBUG.println(CLASS_STRING + " move: " + Integer.toString(moveScorePair.getMove()[TinyConst.MOVE_NUMBER_INDEX])
                    + ", score: " + scoreString
                    + ", playouts: " + Integer.toString(moveScorePair.getPlayouts()));
        }
        DEBUG.println(CLASS_STRING + "------------------------------------------------------------");
    }


    private double playOut(final byte[] move, final TinyGameState gameState)
    {
        // Player carries out move to playout.
        //
        TinyGameState searchState = gameState.makeMove(iPlayerName, move);

        // Play game from new state until end.
        //
        while (! searchState.isGameOver())
        {
            final String playerTurn = searchState.getPlayerTurn();
            final byte[] availableMoves = searchState.getAvailableMoves(playerTurn);

            final byte[] selectedMove = playerTurn.equals(iPlayerName)
                    ? iPlayerStrategy.selectMove(playerTurn, availableMoves, searchState)
                    : iOpponentStrategy.selectMove(playerTurn, availableMoves, searchState);

            searchState = searchState.makeMove(playerTurn, selectedMove);
        }

        final double moveScore = iRelativeBranchScore
                ? computeRelativeBranchScore(searchState)
                : searchState.getScore(iPlayerName);

        return moveScore;
    }


    private double computeRelativeBranchScore(final TinyGameState searchState)
    {
        final Map<String, Integer> scores = searchState.getScores();

        final int playerScore = scores.get(iPlayerName);
        scores.remove(iPlayerName);

        final Collection<Integer> opponentScores = scores.values();

        final ArrayList<Integer> opponentScoresSorted = new ArrayList<>(opponentScores.size());
        opponentScoresSorted.addAll(opponentScores);
        opponentScoresSorted.sort((Integer s1, Integer s2) ->
        {
            return s1 > s2 ? -1 : s2 > s1 ? 1 : 0;
        });

        final Integer topOpponentScore = opponentScoresSorted.get(0);

        final double score = playerScore / (double)(topOpponentScore + playerScore);

        return score;
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
