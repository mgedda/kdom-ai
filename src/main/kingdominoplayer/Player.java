package kingdominoplayer;

import kingdominoplayer.datastructures.*;
import kingdominoplayer.planning.Planner;
import kingdominoplayer.plot.DebugPlot;
import kingdominoplayer.strategies.*;
import kingdominoplayer.utils.Output;
import kingdominoplayer.utils.Util;

import java.util.ArrayList;


/**
 * Copyright 2017 Tomologic AB<br>
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

    private int iMovesMade = 0;

    private final Strategy iStrategy;

    public Player(final String uuid, final String name, final StrategyID strategyID, final boolean enableDebug)
    {
        iUUID = uuid;
        iName = name;
        iStrategy = strategyID.getStrategy();
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

        // TODO [gedda] IMPORTANT! : REMOVE DEBUG
        //sanityCheckAvailableMovesInLocalGameState(localGameState, availableMoves);

        // Show state before move
        //
        final int roundNumber = iMovesMade + 1;
        DEBUG.plotGameState(iDebugEnabled, localGameState, "Before Move (extendedState) " + Integer.toString(roundNumber));
        Util.noop();

        DEBUG.printBranchingFactor(iDebugEnabled, roundNumber, availableMoves.length);

        final Move move = iStrategy.selectMove(iName, availableMoves, localGameState);
        final LocalGameState localGameStateAfterMove = localGameState.makeMove(iName, move);

        // Show state after move
        //
        DEBUG.plotGameState(iDebugEnabled, localGameStateAfterMove, "After Move (extendedState) " + Integer.toString(roundNumber));
        Util.noop();


        GameServer.makeMove(game, this, move);
        iMovesMade++;

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

