package kingdominoplayer.search;

import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.strategies.FullGreedy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-23<br>
 * Time: 22:24<br><br>
 */
public class MonteCarloSearch
{
    private static final double MAX_SEARCH_TIME_SECONDS = 10d;       // maximum time for one move
    private static final int SEARCH_BREADTH = 40;                    // max number of moves to evaluate
    private static final long PLAYOUT_FACTOR = 30;                   // number of desired playouts per move

    private final String CLASS_STRING = "[" + getClass().getSimpleName() + "]";


    public Move evaluate(final String playerName, final LocalGameState localGameState, final ArrayList<Move> moves)
    {
        final LocalGameState searchGameState = localGameState.withSearchEnabled();

        final ArrayList<Move> movesToEvaluate = selectMovesRandomly(moves, SEARCH_BREADTH);
        final ArrayList<MoveScorePair> moveScores = getMoveScores(playerName, searchGameState, movesToEvaluate);

        // Select move with best score.
        //
        return getMoveWithHighestScore(moveScores);
    }

    private Move getMoveWithHighestScore(final ArrayList<MoveScorePair> moveScores)
    {
        moveScores.sort((MoveScorePair moveScore1, MoveScorePair moveScore2) ->
        {
            final double s1 = moveScore1.getScore();
            final double s2 = moveScore2.getScore();

            return s1 > s2 ? -1 : s2 > s1 ? 1 : 0;
        });

        return moveScores.get(0).getMove();
    }

    private ArrayList<MoveScorePair> getMoveScores(final String playerName, final LocalGameState searchGameState, final ArrayList<Move> moves)
    {
        DEBUG.println("\n" + CLASS_STRING + " " + playerName + " searching...");

        final LinkedHashMap<Move, ArrayList<Double>> moveScoresMap = new LinkedHashMap<>(moves.size());
        for (final Move move : moves)
        {
            moveScoresMap.put(move, new ArrayList<>());
        }

        assert moveScoresMap.size() == moves.size() : "Size discrepancy!";

        final long searchStartTime = System.nanoTime();

        long previousPrintTime = searchStartTime;

        final long numPlayOuts = PLAYOUT_FACTOR * moves.size();  // X playouts per move
        long playOutCounter = 1;
        while (playOutCounter <= numPlayOuts
                && getSeconds(System.nanoTime() - searchStartTime) < MAX_SEARCH_TIME_SECONDS)
        {
            // Play out a random move.
            //
            final int randomIndex = ThreadLocalRandom.current().nextInt(0, moves.size());
            final Move move = moves.get(randomIndex);
            final double score = playOut(move, playerName, searchGameState);
            moveScoresMap.get(move).add(score);

            assert moveScoresMap.size() == moves.size() : "Size discrepancy!";

            /*
            // Print move scores every X seconds.
            //
            final long currentTime = System.nanoTime();
            final double printDurationSeconds = (currentTime - previousPrintTime) / 1e9d;
            if (printDurationSeconds > 5.0)
            {
                printMoveScores(assembleScores(moveScoresMap));
                previousPrintTime = currentTime;
            }
            */

            playOutCounter++;
        }


        final long searchEndTime = System.nanoTime();
        final double searchDurationSeconds = (searchEndTime - searchStartTime) / 1e9d;
        final String searchDurationString = String.format("%.3f", searchDurationSeconds);
        DEBUG.println(CLASS_STRING + " Search finished! (moves: " + Integer.toString(moves.size()) + ", playouts: " + Long.toString(playOutCounter - 1) + ", time: " + searchDurationString + "s)");

        final ArrayList<MoveScorePair> moveScores = assembleScores(moveScoresMap);
        printMoveScores(moveScores);

        assert moveScoresMap.size() == moves.size() : "Size discrepancy!";

        return moveScores;
    }


    private double getSeconds(final long nanoTime)
    {
        return nanoTime / 1e9d;
    }


    private ArrayList<MoveScorePair> assembleScores(final Map<Move, ArrayList<Double>> moveScoresMap)
    {
        final ArrayList<MoveScorePair> moveScorePairs = new ArrayList<>(moveScoresMap.size());

        for (final Move move : moveScoresMap.keySet())
        {
            final ArrayList<Double> scores = moveScoresMap.get(move);

            if (scores.isEmpty())
            {
                moveScorePairs.add(new MoveScorePair(move, 0.0));
            }
            else
            {
                double accScore = 0.0;
                for (final Double score : scores)
                {
                    accScore += score;
                }
                moveScorePairs.add(new MoveScorePair(move, accScore / scores.size()));
            }
        }

        return moveScorePairs;
    }


    private void printMoveScores(final ArrayList<MoveScorePair> moveScores)
    {
        DEBUG.println(CLASS_STRING + "------------------------------------------------------------");
        for (final MoveScorePair moveScorePair : moveScores)
        {
            final String scoreString = String.format("%.3f", moveScorePair.getScore());
            DEBUG.println(CLASS_STRING + " move: " + Integer.toString(moveScorePair.getMove().getNumber()) +  ", score: " + scoreString);
        }
        DEBUG.println(CLASS_STRING + "------------------------------------------------------------");
    }


    /**
     *
     *
     * @param move
     * @param playerName
     * @param gameState
     * @return
     */
    private double playOut(final Move move, final String playerName, final LocalGameState gameState)
    {
        // Player executes move.
        //
        LocalGameState searchState = gameState.makeMove(playerName, move);

        // Opponents execute their moves.
        //
        while (! searchState.isGameOver())
        {
            final String playerTurn = searchState.getPlayerTurn();
            final ArrayList<Move> availableMoves = searchState.getAvailableMoves(playerTurn);
            final Move opponentMove = selectMove(playerTurn, availableMoves, searchState);
            searchState = searchState.makeMove(playerTurn, opponentMove);
        }

        final double moveScore = computeMoveScore(playerName, searchState);

        return moveScore;
    }


    private double computeMoveScore(final String playerName, final LocalGameState searchState)
    {
        final Map<String, Integer> scores = searchState.getScores();

        final int playerScore = scores.get(playerName);
        scores.remove(playerName);

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


    private Move selectMove(final String opponentName, final ArrayList<Move> availableMoves, final LocalGameState searchState)
    {
        return new FullGreedy().selectMove(opponentName, availableMoves.toArray(new Move[availableMoves.size()]), searchState);
    }


    /**
     * Select numberOfMoves randomly from moves. If the number of moves in moves are fewer than
     * numberOfMoves, then return all moves.
     *
     * @param moves
     * @param numberOfMoves
     * @return
     */
    private ArrayList<Move> selectMovesRandomly(final ArrayList<Move> moves, final int numberOfMoves)
    {
        if (moves.size() <= numberOfMoves)
        {
            return moves;
        }

        final ArrayList<Move> movesToEvaluate = new ArrayList<>(numberOfMoves);
        final Set<Integer> pickedIndices = new LinkedHashSet<>(numberOfMoves);

        final int numMoves = moves.size();
        while (movesToEvaluate.size() < numberOfMoves)
        {
            final int randomIndex = ThreadLocalRandom.current().nextInt(0, numMoves);
            if (! pickedIndices.contains(randomIndex))
            {
                movesToEvaluate.add(moves.get(randomIndex));
                pickedIndices.add(randomIndex);
            }
        }
        return movesToEvaluate;
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
