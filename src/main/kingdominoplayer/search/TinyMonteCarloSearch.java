package kingdominoplayer.search;

import kingdominoplayer.strategies.tinystrategies.TinyStrategy;
import kingdominoplayer.tinyrepresentation.TinyConst;
import kingdominoplayer.tinyrepresentation.TinyGameState;
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
public class TinyMonteCarloSearch
{
    private static final double MAX_SEARCH_TIME_SECONDS = 10d;       // maximum time for one move
    private static final int SEARCH_BREADTH = 1000;                  // max number of moves to evaluate
    private static final long PLAYOUT_FACTOR = 1000000;              // number of desired playouts per move

    private final String CLASS_STRING = "[" + getClass().getSimpleName() + "]";

    private final String iPlayerName;
    private final TinyStrategy iPlayerStrategy;
    private final TinyStrategy iOpponentStrategy;
    private final boolean iRelativeBranchScore;

    public TinyMonteCarloSearch(final String playerName,
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
        final byte[] movesToEvaluate = selectMovesRandomly(moves, SEARCH_BREADTH);
        final ArrayList<TinyMoveScorePair> moveScores = getMoveScores(gameState, movesToEvaluate);

        // Select move with best score.
        //
        return getMoveWithHighestScore(moveScores);
    }

    private byte[] getMoveWithHighestScore(final ArrayList<TinyMoveScorePair> moveScores)
    {
        moveScores.sort((TinyMoveScorePair moveScore1, TinyMoveScorePair moveScore2) ->
        {
            final double s1 = moveScore1.getScore();
            final double s2 = moveScore2.getScore();

            return s1 > s2 ? -1 : s2 > s1 ? 1 : 0;
        });

        return moveScores.get(0).getMove();
    }

    private ArrayList<TinyMoveScorePair> getMoveScores(final TinyGameState gameState, final byte[] moves)
    {
        DEBUG.println("\n" + CLASS_STRING + " " + iPlayerName + " searching...");

        final int numMoves = moves.length / TinyConst.MOVE_ELEMENT_SIZE;

        final LinkedHashMap<Byte, ArrayList<Double>> moveScoresMap = new LinkedHashMap<>(numMoves);
        for (int i = 0; i < numMoves; ++i)
        {
            final byte[] move = TinyGameState.getRow(moves, i, TinyConst.MOVE_ELEMENT_SIZE);
            moveScoresMap.put(move[TinyConst.MOVE_NUMBER_INDEX], new ArrayList<>());
        }

        final long searchStartTime = System.nanoTime();

        final long numPlayOuts = PLAYOUT_FACTOR * numMoves;  // X playouts per move
        long playOutCounter = 1;
        while (playOutCounter <= numPlayOuts
                && getSeconds(System.nanoTime() - searchStartTime) < MAX_SEARCH_TIME_SECONDS)
        {
            // Play out a random move.
            //
            final int randomIndex = Random.getInt(numMoves);
            final byte[] move = TinyGameState.getRow(moves, randomIndex, TinyConst.MOVE_ELEMENT_SIZE);
            final double score = playOut(move, gameState);
            moveScoresMap.get(move[TinyConst.MOVE_NUMBER_INDEX]).add(score);

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

        final ArrayList<TinyMoveScorePair> moveScores = assembleScores(moveScoresMap, moves);
        printMoveScores(moveScores);

        assert moveScoresMap.size() == numMoves : "Size discrepancy!";

        return moveScores;
    }


    private double getSeconds(final long nanoTime)
    {
        return nanoTime / 1e9d;
    }


    private ArrayList<TinyMoveScorePair> assembleScores(final Map<Byte, ArrayList<Double>> moveScoresMap, byte[] moves)
    {
        final ArrayList<TinyMoveScorePair> moveScorePairs = new ArrayList<>(moveScoresMap.size());

        for (final byte moveNumber : moveScoresMap.keySet())
        {
            final byte[] move = getMove(moveNumber, moves);
            final ArrayList<Double> scores = moveScoresMap.get(moveNumber);

            if (scores.isEmpty())
            {
                moveScorePairs.add(new TinyMoveScorePair(move, 0.0));
            }
            else
            {
                double accScore = 0.0;
                for (final Double score : scores)
                {
                    accScore += score;
                }
                moveScorePairs.add(new TinyMoveScorePair(move, accScore / scores.size()));
            }
        }

        return moveScorePairs;
    }

    private byte[] getMove(final byte moveNumber, final byte[] moves)
    {
        final int numMoves = moves.length / TinyConst.MOVE_ELEMENT_SIZE;

        byte[] move = new byte[0];

        for (int i = 0; i < numMoves; ++i)
        {
            final int moveIndex = i * TinyConst.MOVE_ELEMENT_SIZE;

            if (moves[moveIndex + TinyConst.MOVE_NUMBER_INDEX] == moveNumber)
            {
                move = TinyGameState.getRow(moves, i, TinyConst.MOVE_ELEMENT_SIZE);
                break;
            }
        }

        assert move.length > 0 : "Move not found!";

        return move;
    }


    private void printMoveScores(final ArrayList<TinyMoveScorePair> moveScores)
    {
        DEBUG.println(CLASS_STRING + "------------------------------------------------------------");
        for (final TinyMoveScorePair moveScorePair : moveScores)
        {
            final String scoreString = String.format("%.3f", moveScorePair.getScore());
            DEBUG.println(CLASS_STRING + " move: " + Integer.toString(moveScorePair.getMove()[TinyConst.MOVE_NUMBER_INDEX]) +  ", score: " + scoreString);
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




    /**
     * Select numMovesToPick randomly from moves. If the number of moves in moves are fewer than
     * numMovesToPick, then return all moves.
     *
     * @param moves
     * @param numMovesToPick
     * @return
     */
    private byte[] selectMovesRandomly(final byte[] moves, final int numMovesToPick)
    {
        final int numMoves = moves.length / TinyConst.MOVE_ELEMENT_SIZE;
        if (numMoves <= numMovesToPick)
        {
            return moves;
        }

        final byte[] movesToEvaluate = new byte[numMovesToPick * TinyConst.MOVE_ELEMENT_SIZE];

        final Set<Integer> pickedIndices = new LinkedHashSet<>(2 * numMovesToPick);

        while (pickedIndices.size() < numMovesToPick)
        {
            final int randomIndex = Random.getInt(numMoves);
            if (! pickedIndices.contains(randomIndex))
            {
                System.arraycopy(moves, randomIndex * TinyConst.MOVE_ELEMENT_SIZE, movesToEvaluate, pickedIndices.size() * TinyConst.MOVE_ELEMENT_SIZE, TinyConst.MOVE_ELEMENT_SIZE);
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
