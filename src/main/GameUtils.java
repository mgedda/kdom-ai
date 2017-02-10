import datastructures.Domino;
import datastructures.Move;
import datastructures.Tile;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 13:40<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class GameUtils
{
    public static String[] getTerrains()
    {
        return new String[]{"water", "forest", "field", "mine", "pasture", "clay"};
    }



    public static Tile[] getPlacedTiles(final Player player, final String gameState)
    {
        return GameResponseParser.getPlayerPlacedTiles(gameState, player.getName());
    }


    public static Domino[] getPreviousDrafts(final Player player, final String gameState)
    {
        return GameResponseParser.getPreviousDraftsForPlayer(gameState, player.getName());
    }


    public static LinkedHashMap<String, Tile[]> getTerrainToTilesMap(final Tile[] tiles)
    {
        final LinkedHashMap<String, Tile[]> terrainToNumPlacedTilesMap = new LinkedHashMap<>(GameUtils.getTerrains().length);
        for (final Tile placedTile : tiles)
        {
            final String terrain = placedTile.getTerrain();
            if (terrainToNumPlacedTilesMap.containsKey(terrain))
            {
                terrainToNumPlacedTilesMap.put(terrain, ArrayUtils.append(terrainToNumPlacedTilesMap.get(terrain), placedTile));
            }
            else
            {
                terrainToNumPlacedTilesMap.put(terrain, new Tile[]{placedTile});
            }
        }
        return terrainToNumPlacedTilesMap;
    }




    public static String[] getTerrainsSortedBasedOnNumberOfTilesUseCrownsAsDealBreaker(final Tile[] tiles)
    {
        if (tiles.length == 0)
        {
            return new String[0];
        }

        final LinkedHashMap<String, Tile[]> terrainToTilesMap = GameUtils.getTerrainToTilesMap(tiles);
        final ArrayList<TerrainTilesPair> terrainTilesPairs = new ArrayList<>(terrainToTilesMap.size());

        for (final String terrain : terrainToTilesMap.keySet())
        {
            terrainTilesPairs.add(new TerrainTilesPair(terrain, terrainToTilesMap.get(terrain)));
        }

        terrainTilesPairs.sort((TerrainTilesPair t1, TerrainTilesPair t2) ->
        {
            // Use number of crowns as deal breaker.
            //
            if (t1.iTiles.length == t2.iTiles.length)
            {
                return getNumCrowns(t1.iTiles) > getNumCrowns(t2.iTiles)? -1 : 1;
            }

            // Place terrain with most number of tiles first.
            //
            return t1.iTiles.length > t2.iTiles.length? -1 : 1;
        });


        final ArrayList<String> terrainsSorted = new ArrayList<>(terrainTilesPairs.size());

        for (final TerrainTilesPair terrainTilesPair : terrainTilesPairs)
        {
            terrainsSorted.add(terrainTilesPair.iTerrain);
        }

        return terrainsSorted.toArray(new String[terrainsSorted.size()]);
    }


    public static int getNumCrowns(final Tile[] tiles)
    {
        int numCrowns = 0;
        for (final Tile tile : tiles)
        {
            numCrowns += tile.getCrowns();
        }

        return numCrowns;
    }

    public static Move getMoveWithChosenDominoTerrainUseCrownsAsDealBreaker(final String terrain, final Move[] availableMoves)
    {
        final ArrayList<Move> terrainMoves = new ArrayList<>();

        for (final Move availableMove : availableMoves)
        {
            final Domino chosenDomino = availableMove.getChosenDomino();
            final boolean chosenDominoHasTerrainMatch = chosenDomino != null
                    && (chosenDomino.getTile1().getTerrain().equals(terrain) || chosenDomino.getTile2().getTerrain().equals(terrain));

            if (chosenDominoHasTerrainMatch)
            {
                terrainMoves.add(availableMove);
            }
        }

        if (terrainMoves.isEmpty())
        {
            return null;
        }
        else
        {
            terrainMoves.sort((Move m1, Move m2) ->
            {
                final Domino chosenDomino1 = m1.getChosenDomino();
                final Tile domino1tile1 = chosenDomino1.getTile1();
                final Tile domino1tile2 = chosenDomino1.getTile2();

                final int domino1NumTerrainMatches = (domino1tile1.getTerrain().equals(terrain) ? 1 : 0) + (domino1tile2.getTerrain().equals(terrain) ? 1 : 0);

                final Domino chosenDomino2 = m2.getChosenDomino();
                final Tile domino2tile1 = chosenDomino2.getTile1();
                final Tile domino2tile2 = chosenDomino2.getTile2();

                final int domino2NumTerrainMatches = (domino2tile1.getTerrain().equals(terrain) ? 1 : 0) + (domino2tile2.getTerrain().equals(terrain) ? 1 : 0);

                if (domino1NumTerrainMatches == domino2NumTerrainMatches)
                {
                    // Use number of crowns for terrain as deal breaker.
                    //
                    final int domino1Crowns = getCrownsForTerrain(terrain, chosenDomino1);
                    final int domino2Crowns = getCrownsForTerrain(terrain, chosenDomino2);

                    return domino1Crowns > domino2Crowns? -1 : 1;
                }

                return domino1NumTerrainMatches > domino2NumTerrainMatches? -1 : 1;
            });

            return terrainMoves.get(0);
        }
    }

    private static int getCrownsForTerrain(final String terrain, final Domino domino)
    {
        final Tile tile1 = domino.getTile1();
        final Tile tile2 = domino.getTile1();

        final int numCrownsTile1 = tile1.getTerrain().equals(terrain)? tile1.getCrowns() : 0;
        final int numCrownsTile2 = tile2.getTerrain().equals(terrain)? tile2.getCrowns() : 0;

        return numCrownsTile1 + numCrownsTile2;
    }


    private static class TerrainTilesPair
    {
        final String iTerrain;
        final Tile[] iTiles;

        private TerrainTilesPair(final String terrain, final Tile[] tiles)
        {
            iTerrain = terrain;
            iTiles = tiles;
        }
    }

}
