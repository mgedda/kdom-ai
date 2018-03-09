package kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation;

import kingdominoplayer.SearchParameters;
import kingdominoplayer.tinyrepresentation.TinyUtils;
import kingdominoplayer.tinyrepresentation.search.montecarlo.MonteCarloMethods;
import kingdominoplayer.tinyrepresentation.search.montecarlo.TinyMoveAverageScorePair;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinySimulationStrategy;
import kingdominoplayer.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 13:46<br><br>
 */
public class TinyMonteCarloEvaluationAlgorithm
{
    private final String CLASS_STRING = "[" + getClass().getSimpleName() + "]";

    private final String iPlayerName;
    private final TinySimulationStrategy iSimulationStrategy;
    private final PlayoutScoringFunction iPlayoutScoringFunction;
    private final SearchParameters iSearchParameters;

    private double iNumPlayoutsPerSecond = -1;      // output

    public TinyMonteCarloEvaluationAlgorithm(final String playerName,
                                             final TinySimulationStrategy simulationStrategy,
                                             final PlayoutScoringFunction playoutScoringFunction,
                                             final SearchParameters searchParameters)
    {
        iPlayerName = playerName;
        iSimulationStrategy = simulationStrategy;
        iPlayoutScoringFunction = playoutScoringFunction;
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
        final ArrayList<TinyMoveAverageScorePair> moveScores = getMoveScores(gameState, moves);

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

        long playOutCounter = 1;
        while (playOutCounter <= iSearchParameters.getMaxNumPlayouts()
                && getSeconds(System.nanoTime() - searchStartTime) < iSearchParameters.getMaxSearchTime())
        {
            // Play out a random move.
            //
            final int randomIndex = Random.getInt(numMoves);
            final byte[] move = TinyGameState.getRow(moves, randomIndex, TinyConst.MOVE_ELEMENT_SIZE);
            final double score = MonteCarloMethods.playOut(move, gameState, iPlayerName, iSimulationStrategy, iPlayoutScoringFunction);
            moveScoresMap.get(move[TinyConst.MOVE_NUMBER_INDEX]).addScore(score);

            assert moveScoresMap.size() == numMoves : "Size discrepancy!";

            playOutCounter++;
        }


        final long searchEndTime = System.nanoTime();
        final double searchDurationSeconds = (searchEndTime - searchStartTime) / 1e9d;
        final String searchDurationString = String.format("%.3f", searchDurationSeconds);

        MonteCarloMethods.printMoveScores(moveScoresMap.values(), CLASS_STRING, numMoves, playOutCounter,
                searchDurationString, searchDurationSeconds);

        iNumPlayoutsPerSecond = (playOutCounter - 1) / searchDurationSeconds;

        assert moveScoresMap.size() == numMoves : "Size discrepancy!";

        final ArrayList<TinyMoveAverageScorePair> moveScores = new ArrayList<>(moveScoresMap.values().size());
        moveScores.addAll(moveScoresMap.values());

        return moveScores;
    }


    private double getSeconds(final long nanoTime)
    {
        return nanoTime / 1e9d;
    }

    public double getNumPlayoutsPerSecond()
    {
        assert iNumPlayoutsPerSecond >= 0 : "Value not initialized yet.";
        return iNumPlayoutsPerSecond;
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
