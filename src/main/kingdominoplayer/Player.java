package kingdominoplayer;

import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.datastructures.PlacedTile;
import kingdominoplayer.plot.SceneRenderer;
import kingdominoplayer.strategies.*;
import kingdominoplayer.utils.GameUtils;

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

    public Player(String uuid, final String name)
    {
        iUUID = uuid;
        iName = name;
        iStrategy = Strategy.RANDOM;
    }

    public Player(String uuid, final String name, final String strategy)
    {
        iUUID = uuid;
        iName = name;
        iStrategy = Strategy.valueOf(strategy);
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

    public boolean makeAMove(final Game game)
    {
        if (! game.getCurrentPlayer().equals(getName()))
        {
            OUTPUT.printWaiting(this);
            return false;
        }

        OUTPUT.printMakingAMove(this);

        final String gameState = game.getGameState();

        final Move[] availableMoves = game.getAvailableMoves();
        assert availableMoves.length > 0 : "no moves to choose from";

        // Show state before move
        //
        //DEBUG.plotGameState(gameState, "Before Move " + Integer.toString(iMovesMade + 1));
        //System.out.println(gameState);

        final Move move = pickAMove(gameState, availableMoves);

        // Show state after move

        game.makeMove(this, move);
        iMovesMade++;

        OUTPUT.printMoveMade();

        return true;
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

        private static void plotGameState(final String gameState, final String title)
        {
            if (DEBUG)
            {
                SceneRenderer.render(gameState, title);
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

        public static void printWaiting(final Player player)
        {
            print(player.getName() + ": Waiting for my turn...\n");
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

