package kingdominoplayer;

import kingdominoplayer.naiverepresentation.datastructures.GameState;
import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.naiverepresentation.datastructures.Move;
import kingdominoplayer.naiverepresentation.strategies.Strategy;
import kingdominoplayer.naiverepresentation.strategies.StrategyID;
import kingdominoplayer.utils.Output;
import kingdominoplayer.utils.Util;


/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-08<br>
 * Time: 22:57<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class Player
{
    private final String iUUID;
    private final String iName;
    private final boolean iDebugEnabled;

    // Statistics
    private final int[] iNumAvailableMoves = new int[13];     // Number of available moves each round.
    private final int[] iNumAvailableDraft = new int[13];     // Number of available dominoes in the current draft each round.
    private final int[] iChosenDraftPositions = new int[13];  // The priority position of the chosen domino in current draft.
    private final double[] iNumPlayoutsPerSecond = new double[13];
    private final int[] iScores = new int[13];                // The score after each round

    private int iMovesMade = 0;

    private final StrategyID iStrategyID;

    public Player(final String uuid, final String name, final StrategyID strategyID, final boolean enableDebug)
    {
        iUUID = uuid;
        iName = name;
        iStrategyID = strategyID;
        iDebugEnabled = enableDebug;
    }

    public String getName()
    {
        return iName;
    }

    public String getUUID()
    {
        return iUUID;
    }

    @Override
    public String toString()
    {
        return "Player{" +
                "iName='" + iName + '\'' +
                ", iUUID='" + iUUID + '\'' +
                '}';
    }

    public void makeAMove(final Game game, final LocalGameState localGameState)
    {
        final Move[] availableMoves = GameServer.getAvailableMoves(game);
        assert availableMoves.length > 0 : "no moves to choose from";

        // Show state before move
        //
        final int roundNumber = iMovesMade + 1;
        System.out.println("\nRound: " + Integer.toString(roundNumber));
        DEBUG.plotGameState(iDebugEnabled, localGameState, "Before Move (extendedState) " + Integer.toString(roundNumber));
        Util.noop();

        // Store some stats.
        //
        iNumAvailableMoves[roundNumber-1] = availableMoves.length;
        iNumAvailableDraft[roundNumber-1] = localGameState.getNumAvailableDominoesInCurrentDraft();
        DEBUG.printBranchingFactor(iDebugEnabled, roundNumber, availableMoves.length);

        // Select move.
        //
        final Move move = selectMove(availableMoves, localGameState, roundNumber);
        final LocalGameState localGameStateAfterMove = localGameState.makeMove(iName, move);

        iChosenDraftPositions[roundNumber-1] = localGameState.getPositionInCurrentDraft(move.getChosenDomino());

        // Show state after move
        //
        DEBUG.plotGameState(iDebugEnabled, localGameStateAfterMove, "After Move (extendedState) " + Integer.toString(roundNumber));
        Util.noop();

        GameServer.makeMove(game, this, move);
        iMovesMade++;

        iScores[roundNumber-1] = localGameStateAfterMove.getScore(iName);
    }

    protected Move selectMove(final Move[] availableMoves, final LocalGameState localGameState, final int roundNumber)
    {
        final Strategy strategy = iStrategyID.getStrategy();
        final Move move = strategy.selectMove(iName, availableMoves, localGameState);

        return move;
    }


    public int[] getNumAvailableMoves()
    {
        return iNumAvailableMoves;
    }

    public int[] getNumAvailableDraft()
    {
        return iNumAvailableDraft;
    }

    public int[] getChosenDraftPositions()
    {
        return iChosenDraftPositions;
    }

    public double[] getNumPlayoutsPerSecond()
    {
        return iNumPlayoutsPerSecond;
    }

    public int[] getScores()
    {
        return iScores;
    }

    private static class DEBUG
    {
        private static boolean DEBUG = true;

        private static void plotGameState(final boolean isDebugEnabled, final GameState gameState, final String title)
        {
            if (DEBUG && isDebugEnabled)
            {
                //DebugPlot.plotGameState(gameState, title);
            }
        }

        public static void printBranchingFactor(final boolean isDebugEnabled, final int roundNumber, final int numAvailableMoves)
        {
            if (DEBUG && isDebugEnabled)
            {
                Output.printBranchingFactor(roundNumber, numAvailableMoves);
            }
        }
    }
}

