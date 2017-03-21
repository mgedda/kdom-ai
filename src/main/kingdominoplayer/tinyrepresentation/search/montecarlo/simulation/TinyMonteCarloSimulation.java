package kingdominoplayer.tinyrepresentation.search.montecarlo.simulation;

import kingdominoplayer.tinyrepresentation.TinyUtils;
import kingdominoplayer.tinyrepresentation.search.montecarlo.MonteCarloMethods;
import kingdominoplayer.tinyrepresentation.search.montecarlo.TinyMoveAverageScorePair;
import kingdominoplayer.tinyrepresentation.strategies.TinyStrategy;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
        return MonteCarloMethods.getMoveWithHighestScore(moveScores);
    }


    private ArrayList<TinyMoveAverageScorePair> getMoveScores(final TinyGameState gameState, final byte[] moves)
    {
        DEBUG.println(CLASS_STRING + " " + iPlayerName + " searching...");

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
            final double score = MonteCarloMethods.playOut(move, gameState, iPlayerName, iPlayerStrategy, iOpponentStrategy, iRelativeBranchScore);
            moveScoresMap.get(move[TinyConst.MOVE_NUMBER_INDEX]).addScore(score);

            assert moveScoresMap.size() == numMoves : "Size discrepancy!";

            playOutCounter++;
        }


        final long searchEndTime = System.nanoTime();
        final double searchDurationSeconds = (searchEndTime - searchStartTime) / 1e9d;
        final String searchDurationString = String.format("%.3f", searchDurationSeconds);

        MonteCarloMethods.printMoveScores(moveScoresMap.values(), CLASS_STRING, numMoves, playOutCounter,
                searchDurationString, searchDurationSeconds);

        assert moveScoresMap.size() == numMoves : "Size discrepancy!";

        final ArrayList<TinyMoveAverageScorePair> moveScores = new ArrayList<>(moveScoresMap.values().size());
        moveScores.addAll(moveScoresMap.values());

        return moveScores;
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
