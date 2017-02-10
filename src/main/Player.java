import datastructures.Domino;
import datastructures.Move;
import datastructures.Tile;

import java.util.LinkedHashMap;
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

    enum Strategy
    {
        SELECT_FIRST,
        SELECT_RANDOM,
        SELECT_MOST_CROWNS,
        SELECT_WATER,
        SELECT_EXPAND
    }

    private final Strategy iStrategy;

    public Player(String uuid, final String name)
    {
        iUUID = uuid;
        iName = name;
        iStrategy = Strategy.SELECT_RANDOM;
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
        return "Player{" +
                "iName='" + iName + '\'' +
                ", iUUID='" + iUUID + '\'' +
                '}';
    }

    public boolean makeAMove(final Game game)
    {
        if (! game.getCurrentPlayer().equals(getName()))
        {
            DEBUG.printWaiting(this);
            return false;
        }

        DEBUG.printMakingAMove(this);

        final Move[] availableMoves = game.getAvailableMoves();

        assert availableMoves.length > 0 : "no moves to choose from";

        final Move move = pickAMove(game, availableMoves);

        game.makeMove(this, move);

        DEBUG.printMoveMade();

        return true;
    }


    private Move pickAMove(final Game game, final Move[] availableMoves)
    {
        Move move = availableMoves[0];

        switch (iStrategy)
        {
            case SELECT_FIRST:
                move = availableMoves[0];
                break;

            case SELECT_RANDOM:
                final int numMoves = availableMoves.length;
                int randomNum = ThreadLocalRandom.current().nextInt(0, numMoves);
                move = availableMoves[randomNum];
                break;

            case SELECT_MOST_CROWNS:
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

            case SELECT_WATER:
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

            case SELECT_EXPAND:

                // Get number of placed tiles for each terrain.
                //
                final LinkedHashMap<String, Integer> terrainToNumTilesMap = getNumberOfPlacedTilesForEachTerrain(game);

                // Choose move that picks the the domino with such terrain (if domino such exist).
                //
                final int totalNumTiles = ArrayUtils.sum(ArrayUtils.toArray(terrainToNumTilesMap.values()));

                if (totalNumTiles > 0)
                {
                    final String terrainWithMaxNumberOfTiles = getTerrainWithMaxNumberOfTiles(terrainToNumTilesMap);
                    final Move selectedMove = selectMoveWhereChosenDominoHasTerrain(terrainWithMaxNumberOfTiles, availableMoves);
                    move = selectedMove == null ? availableMoves[0] : selectedMove;
                }

                break;

            default:
                System.err.print("Error: unknown strategy.");
                System.exit(0);
        }

        return move;
    }


    private String getTerrainWithMaxNumberOfTiles(final LinkedHashMap<String, Integer> terrainToNumTilesMap)
    {
        String terrainWithMaxNumTiles = "water";
        int maxNumTiles = -1;

        for (final String terrain : terrainToNumTilesMap.keySet())
        {
            final Integer terrainNumTiles = terrainToNumTilesMap.get(terrain);
            if (terrainNumTiles > maxNumTiles)
            {
                terrainWithMaxNumTiles = terrain;
                maxNumTiles = terrainNumTiles;

            }
        }

        return terrainWithMaxNumTiles;
    }

    private LinkedHashMap<String, Integer> getNumberOfPlacedTilesForEachTerrain(final Game game)
    {
        final String[] terrains = Game.getTerrainNames();

        final LinkedHashMap<String, Integer> terrainToNumPlacedTilesMap = new LinkedHashMap<>(terrains.length);

        for (final String terrain : terrains)
        {
            terrainToNumPlacedTilesMap.put(terrain, 0);
        }


        final Tile[] placedTiles = game.getPlacedTiles(this);
        for (final Tile placedTile : placedTiles)
        {
            final String terrain = placedTile.getTerrain();
            terrainToNumPlacedTilesMap.put(terrain, terrainToNumPlacedTilesMap.get(terrain) + 1);
        }

        return terrainToNumPlacedTilesMap;
    }


    private Move selectMoveWhereChosenDominoHasTerrain(final String terrain, final Move[] availableMoves)
    {
        int maxTerrains = 0;
        Move move = null;

        for (final Move availableMove : availableMoves)
        {
            final Domino chosenDomino = availableMove.getChosenDomino();
            if (chosenDomino != null)
            {
                final int tile1Terrain = chosenDomino.getTile1().getTerrain().equals(terrain)? 1 : 0;
                final int tile2Terrain = chosenDomino.getTile2().getTerrain().equals(terrain)? 1 : 0;

                if (tile1Terrain + tile2Terrain > maxTerrains)
                {
                    maxTerrains = tile1Terrain + tile2Terrain;
                    move = availableMove;
                }
            }
        }

        return move;
    }


    private static class DEBUG
    {
        private static boolean DEBUG = true;

        private static void print(final String msg)
        {
            if (DEBUG)
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

