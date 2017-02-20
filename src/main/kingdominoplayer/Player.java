package kingdominoplayer;

import kingdominoplayer.datastructures.*;
import kingdominoplayer.planning.Planner;
import kingdominoplayer.plot.DebugPlot;
import kingdominoplayer.strategies.*;
import kingdominoplayer.utils.GameUtils;
import kingdominoplayer.utils.Util;

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

    enum Strategy
    {
        FIRST,
        RANDOM,
        MOST_CROWNS,
        WATER,
        EXPAND,
        LOOK_AHEAD
    }

    private final Strategy iStrategy;

    public Player(final String uuid, final String name, final String strategy, final boolean enableDebug)
    {
        iUUID = uuid;
        iName = name;
        iStrategy = Strategy.valueOf(strategy);
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
        return "kingdominoplayer.Player{" +
                "iName='" + iName + '\'' +
                ", iUUID='" + iUUID + '\'' +
                '}';
    }

    public void makeAMove(final Game game, final LocalGameState localGameState)
    {
        OUTPUT.printMakingAMove(this);

        final String serverGameState = game.getGameState();

        final Move[] availableMoves = game.getAvailableMoves();
        assert availableMoves.length > 0 : "no moves to choose from";

        // Show state before move
        //
        //DEBUG.plotGameState(iDebugEnabled, serverGameState, "Before Move " + Integer.toString(iMovesMade + 1));
        DEBUG.plotGameState(iDebugEnabled, localGameState, "Before Move (extendedState) " + Integer.toString(iMovesMade + 1));
        //System.out.println(serverGameState);
        Util.noop();

        final Move move = pickAMove(serverGameState, availableMoves);

        final LocalGameState localGameStateAfterMove = localGameState.makeMove(iName, move);

        // Show state after move
        //
        // // TODO [gedda] IMPORTANT! : THIS HAS BUGS, NOT SHOWING CORRECTLY
        //DEBUG.plotGameStateAfterMove(iDebugEnabled, serverGameState, move, iName, "After Move " + Integer.toString(iMovesMade + 1));
        DEBUG.plotGameState(iDebugEnabled, localGameStateAfterMove, "After Move (extendedState) " + Integer.toString(iMovesMade + 1));
        Util.noop();


        game.makeMove(this, move);
        iMovesMade++;

        OUTPUT.printMoveMade();
    }


    private Move pickAMove(final String gameState, final Move[] availableMoves)
    {
        final PlacedTile[] placedTiles = GameUtils.getPlacedTiles(this, gameState);
        final Domino[] previousDraft = GameUtils.getPreviousDraft(this, gameState);
        final Domino[] currentDraft = GameUtils.getCurrentDraft(this, gameState);

        Move move = availableMoves[0];

        switch (iStrategy)
        {
            case FIRST:
                move = new FirstStrategy().selectMove(availableMoves, previousDraft, currentDraft, placedTiles);
                break;

            case RANDOM:
                move = new RandomStrategy().selectMove(availableMoves, previousDraft, currentDraft, placedTiles);
                break;

            case MOST_CROWNS:
                move = new MostCrownsStrategy().selectMove(availableMoves, previousDraft, currentDraft, placedTiles);
                break;

            case WATER:
                move = new WaterStrategy().selectMove(availableMoves, previousDraft, currentDraft, placedTiles);
                break;

            case EXPAND:
                move = new ExpandStrategy().selectMove(availableMoves, previousDraft, currentDraft, placedTiles);
                break;

            case LOOK_AHEAD:
                move = new LookAheadStrategy().selectMove(availableMoves, previousDraft, currentDraft, placedTiles);
                break;

            default:
                System.err.print("Error: unknown strategy.");
                System.exit(0);
        }

        return move;
    }



    private static class DEBUG
    {
        private static boolean DEBUG = true;

        private static void plotGameState(final boolean isDebugEnabled, final String gameState, final String title)
        {
            if (DEBUG && isDebugEnabled)
            {
                DebugPlot.plotGameState(gameState, title);
            }
        }

        private static void plotGameState(final boolean isDebugEnabled, final GameState gameState, final String title)
        {
            if (DEBUG && isDebugEnabled)
            {
                DebugPlot.plotGameState(gameState, title);
            }
        }

        public static void plotGameStateAfterMove(final boolean isDebugEnabled, final String gameState, final Move move, final String playerName, final String title)
        {
            if (DEBUG && isDebugEnabled)
            {
                final GameState gameStateObject = ServerResponseParser.getGameStateObject(gameState);
                final GameState newGameState = Planner.makeMove(move, gameStateObject, playerName);
                DebugPlot.plotGameState(newGameState, title);
            }
        }
    }


    private static class OUTPUT
    {
        private static boolean OUTPUT = true;

        private static void print(final String msg)
        {
            if (OUTPUT)
            {
                System.out.print(msg);
            }
        }

        public static void printMakingAMove(final Player player)
        {
            print(player.getName() + ": Making a move...");
        }

        public static void printMoveMade()
        {
            print("move made!\n");
        }
    }
}

