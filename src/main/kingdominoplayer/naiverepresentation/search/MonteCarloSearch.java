package kingdominoplayer.naiverepresentation.search;

import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.naiverepresentation.datastructures.Move;
import kingdominoplayer.naiverepresentation.strategies.Strategy;
import kingdominoplayer.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-23<br>
 * Time: 22:24<br><br>
 */
public class MonteCarloSearch
{
    private static final double MAX_SEARCH_TIME_SECONDS = 10d;       // maximum time for one move
    private static final int SEARCH_BREADTH = 1000;                  // max number of moves to evaluate
    private static final long PLAYOUT_FACTOR = 1000000;              // number of desired playouts per move

    private final String CLASS_STRING = "[" + getClass().getSimpleName() + "]";

    private final String iPlayerName;
    private final Strategy iPlayerStrategy;
    private final Strategy iOpponentStrategy;
    private final boolean iRelativeBranchScore;

    public MonteCarloSearch(final String playerName,
                            final Strategy playerStrategy,
                            final Strategy opponentStrategy,
                            final boolean relativeBranchScore)
    {
        iPlayerName = playerName;
        iPlayerStrategy = playerStrategy;
        iOpponentStrategy = opponentStrategy;
        iRelativeBranchScore = relativeBranchScore;
    }

    public Move evaluate(final LocalGameState localGameState, final ArrayList<Move> moves)
    {
        final LocalGameState searchGameState = localGameState.withSearchEnabled();

        final ArrayList<Move> movesToEvaluate = selectMovesRandomly(moves, SEARCH_BREADTH);
        final ArrayList<MoveScorePair> moveScores = getMoveScores(searchGameState, movesToEvaluate);

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

    private ArrayList<MoveScorePair> getMoveScores(final LocalGameState searchGameState, final ArrayList<Move> moves)
    {
        DEBUG.println(CLASS_STRING + " " + iPlayerName + " searching...");

        final LinkedHashMap<Move, ArrayList<Double>> moveScoresMap = new LinkedHashMap<>(moves.size());
        for (final Move move : moves)
        {
            moveScoresMap.put(move, new ArrayList<>());
        }

        assert moveScoresMap.size() == moves.size() : "Size discrepancy!";

        final long searchStartTime = System.nanoTime();

        final long numPlayOuts = PLAYOUT_FACTOR * moves.size();  // X playouts per move
        long playOutCounter = 1;
        while (playOutCounter <= numPlayOuts
                && getSeconds(System.nanoTime() - searchStartTime) < MAX_SEARCH_TIME_SECONDS)
        {
            // Play out a random move.
            //
            final int randomIndex = Random.getInt(moves.size());
            final Move move = moves.get(randomIndex);
            final double score = playOut(move, searchGameState);
            moveScoresMap.get(move).add(score);

            assert moveScoresMap.size() == moves.size() : "Size discrepancy!";

            playOutCounter++;
        }


        final long searchEndTime = System.nanoTime();
        final double searchDurationSeconds = (searchEndTime - searchStartTime) / 1e9d;
        final String searchDurationString = String.format("%.3f", searchDurationSeconds);
        DEBUG.println(CLASS_STRING + " Search finished! (moves: " + Integer.toString(moves.size()) +
                ", playouts: " + Long.toString(playOutCounter - 1) +
                ", time: " + searchDurationString + "s)" +
                ", playouts/s: " + String.format("%.3f", (playOutCounter - 1) / searchDurationSeconds));

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


    private double playOut(final Move move, final LocalGameState gameState)
    {
        // Player carries out move to playout.
        //
        LocalGameState searchState = gameState.makeMove(iPlayerName, move);

        // Play game from new state until end.
        //
        while (! searchState.isGameOver())
        {
            final String playerTurn = searchState.getPlayerTurn();
            final ArrayList<Move> availableMoves = searchState.getAvailableMoves(playerTurn);

            final Move selectedMove = playerTurn.equals(iPlayerName)
                    ? iPlayerStrategy.selectMove(playerTurn, availableMoves.toArray(new Move[availableMoves.size()]), searchState)
                    : iOpponentStrategy.selectMove(playerTurn, availableMoves.toArray(new Move[availableMoves.size()]), searchState);

            searchState = searchState.makeMove(playerTurn, selectedMove);
        }

        final double moveScore = iRelativeBranchScore
                ? computeRelativeBranchScore(searchState)
                : searchState.getScore(iPlayerName);

        return moveScore;
    }


    private double computeRelativeBranchScore(final LocalGameState searchState)
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
            final int randomIndex = Random.getInt(numMoves);
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
