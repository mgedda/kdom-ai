package kingdominoplayer;

import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.datastructures.PlacedTile;
import kingdominoplayer.plot.SceneRenderer;

import java.util.concurrent.ThreadLocalRandom;

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
        EXPAND
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

        final String gameState = CommunicationsHandler.getGameState(game);
        final Move[] availableMoves = game.getAvailableMoves();
        assert availableMoves.length > 0 : "no moves to choose from";

        // Show state before move
        //
        DEBUG.plotGameState(gameState, "Before Move " + Integer.toString(iMovesMade + 1));

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
        final Domino[] previousDrafts = GameUtils.getPreviousDrafts(this, gameState);

        Move move = availableMoves[0];

        switch (iStrategy)
        {
            case FIRST:
                move = availableMoves[0];
                break;

            case RANDOM:
                final int numMoves = availableMoves.length;
                int randomNum = ThreadLocalRandom.current().nextInt(0, numMoves);
                move = availableMoves[randomNum];
                break;

            case MOST_CROWNS:
                int maxCrowns = 0;
                for (final Move availableMove : availableMoves)
                {
                    final Domino chosenDomino = availableMove.getChosenDomino();

                    if (chosenDomino != null)
                    {
                        final int tile1Crowns = chosenDomino.getTile1().getCrowns();
                        final int tile2Crowns = chosenDomino.getTile2().getCrowns();
                        if (tile1Crowns > maxCrowns || tile2Crowns > maxCrowns)
                        {
                            move = availableMove;
                            maxCrowns = Math.max(tile1Crowns, tile2Crowns);
                        }
                    }
                }
                break;

            case WATER:
                int maxWaters = 0;
                for (final Move availableMove : availableMoves)
                {
                    final Domino chosenDomino = availableMove.getChosenDomino();

                    if (chosenDomino != null)
                    {
                        final int tile1Waters = chosenDomino.getTile1().getTerrain().equals("water")? 1 : 0;
                        final int tile2Waters = chosenDomino.getTile2().getTerrain().equals("water")? 1 : 0;

                        if (tile1Waters + tile2Waters > maxWaters)
                        {
                            move = availableMove;
                            maxWaters = tile1Waters + tile2Waters;
                        }
                    }
                }
                break;

            case EXPAND:

                // Choose move that picks domino with terrain that we already have most of.
                //
                final String[] terrainsSorted = GameUtils.getTerrainsSortedBasedOnNumberOfTilesUseCrownsAsDealBreaker(placedTiles);

                for (final String terrain : terrainsSorted)
                {
                    final Move selectedMove = GameUtils.getMoveWithChosenDominoTerrainUseCrownsAsDealBreaker(terrain, availableMoves);

                    if (selectedMove != null)
                    {
                        move = selectedMove;
                        break;
                    }
                }

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

