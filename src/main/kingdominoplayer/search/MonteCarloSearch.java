package kingdominoplayer.search;

import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.strategies.LookAheadRandom;

import java.util.ArrayList;
import java.util.Collection;
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
    private static final int SURFACE_BREADTH = 10;   // max number of moves to evaluate
    private static final int SEARCH_BREADTH = 3;     // max number of branches to evaluate for each move

    private final String CLASS_STRING = "[" + getClass().getSimpleName() + "]";


    public Move evaluate(final String playerName, final LocalGameState localGameState, final ArrayList<Move> moves)
    {
        final LocalGameState searchGameState = localGameState.withSearchEnabled();

        final ArrayList<Move> movesToEvaluate = selectMovesRandomly(moves, SURFACE_BREADTH);
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
        final ArrayList<MoveScorePair> moveScores = new ArrayList<>(moves.size());

        DEBUG.println("\n" + CLASS_STRING + " " + playerName + " searching...");

        final long searchStartTime = System.nanoTime();

        int moveCounter = 1;
        for (final Move move : moves)
        {
            DEBUG.print(CLASS_STRING + " Making move " + moveCounter++ + "/" + moves.size() + "...");
            final long moveStartTime = System.nanoTime();

            final double score = getMoveScoreRecursively(move, playerName, searchGameState);
            moveScores.add(new MoveScorePair(move, score));

            final long moveEndTime = System.nanoTime();
            final long moveDuration = (moveEndTime - moveStartTime);  //divide by 1000000 to get milliseconds.
            final double moveDurationSeconds = moveDuration / 1e9d;

            //DEBUG.print("done! (move score: " + Double.toString(score) + ", moveDuration: " + Double.toString(moveDurationSeconds) + "s)\n");
            final String scoreString = String.format("%.3f", score);
            final String moveDurationString = String.format("%.3f", moveDurationSeconds);
            DEBUG.println("done! (score: " + scoreString + ", time: " + moveDurationString + "s)");
        }


        final long searchEndTime = System.nanoTime();
        final long searchDuration = (searchEndTime - searchStartTime);  //divide by 1000000 to get milliseconds.
        final double searchDurationSeconds = searchDuration / 1e9d;
        final String searchDurationString = String.format("%.3f", searchDurationSeconds);
        DEBUG.println(CLASS_STRING + " Search time: " + searchDurationString + "s");

        return moveScores;
    }


    /**
     *
     *
     * @param move
     * @param playerName
     * @param gameState
     * @return
     */
    private double getMoveScoreRecursively(final Move move, final String playerName, final LocalGameState gameState)
    {
        // Player executes move.
        //
        LocalGameState searchState = gameState.makeMove(playerName, move);

        // Opponents execute their moves.
        //
        while (! searchState.isGameOver() && ! searchState.getPlayerTurn().equals(playerName))
        {
            final String opponentName = searchState.getPlayerTurn();
            final ArrayList<Move> availableMoves = searchState.getAvailableMoves(opponentName);
            final Move opponentMove = selectOpponentMove(opponentName, availableMoves, searchState);
            searchState = searchState.makeMove(opponentName, opponentMove);
        }


        // Check if game ended here.
        //
        if (searchState.isGameOver())
        {
            final double moveScore = computeMoveScore(playerName, searchState);

            return moveScore;
        }


        // Game is not over, evaluate new moves.
        //
        final ArrayList<Move> availableMoves = searchState.getAvailableMoves(playerName);
        final ArrayList<Move> maxScoringMoves = new LookAheadRandom().selectMaxScoringMoves(playerName, availableMoves.toArray(new Move[availableMoves.size()]), searchState);
        final ArrayList<Move> movesToEvaluate = selectMovesRandomly(maxScoringMoves, SEARCH_BREADTH);

        double totalScore = 0;
        for (final Move moveToEvaluate : movesToEvaluate)
        {
            totalScore += getMoveScoreRecursively(moveToEvaluate, playerName, searchState);
        }
        final int numMovesEvaluated = movesToEvaluate.size();

        return totalScore / numMovesEvaluated;
    }

    private double computeMoveScore_old(final String playerName, final LocalGameState searchState)
    {
        final boolean playerWon = hasPlayerHighestScore(playerName, searchState);
        return playerWon ? 1.0 : 0.0;
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


    private Move selectOpponentMove(final String opponentName, final ArrayList<Move> availableMoves, final LocalGameState searchState)
    {
        return new LookAheadRandom().selectMove(opponentName, availableMoves.toArray(new Move[availableMoves.size()]), searchState);
    }



    /**
     * Check if playerName has the highest score.
     *
     * @param playerName
     * @param searchState
     * @return
     */
    private boolean hasPlayerHighestScore(final String playerName, final LocalGameState searchState)
    {
        final Map<String, Integer> scores = searchState.getScores();

        final int playerScore = scores.get(playerName);
        scores.remove(playerName);

        final Collection<Integer> opponentScores = scores.values();

        boolean playerHasHighestScore = true;
        for (final int opponentScore : opponentScores)
        {
            if (opponentScore >= playerScore)
            {
                playerHasHighestScore = false;
                break;
            }
        }

        return playerHasHighestScore;
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
