package kingdominoplayer;

import kingdominoplayer.datastructures.*;
import kingdominoplayer.planning.Scorer;

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


    public static PlacedTile[] getPlacedTiles(final Player player, final String gameState)
    {
        return GameResponseParser.getPlayerPlacedTiles(gameState, player.getName());
    }


    public static Domino[] getPreviousDraft(final Player player, final String gameState)
    {
        return GameResponseParser.getPreviousDraftForPlayer(gameState, player.getName());
    }

    public static Domino[] getCurrentDraft(final Player player, final String gameState)
    {
        return GameResponseParser.getCurrentDraftForPlayer(gameState, player.getName());
    }


    public static PlacedTile getTileAtPosition(final Position position, final PlacedTile[] placedTiles)
    {
        for (final PlacedTile placedTile : placedTiles)
        {
            if (placedTile.getPosition().equals(position))
            {
                return placedTile;
            }
        }

        return null;
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

    public static LinkedHashMap<KingdomMovePair, Integer> getKingdomScores(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        final LinkedHashMap<KingdomMovePair, Integer> kingdomMovePairToScoreMap = new LinkedHashMap<>(100);  // magic number

        for (final KingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            final int score = Scorer.computeScore(kingdomMovePair.getKingdom().getPlacedTiles());
            kingdomMovePairToScoreMap.put(kingdomMovePair, score);
        }
        return kingdomMovePairToScoreMap;
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


    public static ArrayList<Move> getMovesWithMostCrownsOnSingleTile(final Move[] moves)
    {
        // Find how many crowns is the max.
        //
        int maxCrowns = 0;
        for (final Move move : moves)
        {
            final Domino chosenDomino = move.getChosenDomino();

            if (chosenDomino != null)
            {
                final int tile1Crowns = chosenDomino.getTile1().getCrowns();
                final int tile2Crowns = chosenDomino.getTile2().getCrowns();

                if (tile1Crowns > maxCrowns || tile2Crowns > maxCrowns)
                {
                    maxCrowns = Math.max(tile1Crowns, tile2Crowns);
                }
            }
        }


        // Pick moves with max crowns.
        //
        final ArrayList<Move> maxCrownsMoves = new ArrayList<>(moves.length);

        for (final Move move : moves)
        {
            final Domino chosenDomino = move.getChosenDomino();

            final boolean dominoHasMaxCrowns = chosenDomino != null
                    && (chosenDomino.getTile1().getCrowns() == maxCrowns || chosenDomino.getTile2().getCrowns() == maxCrowns);
            if (dominoHasMaxCrowns)
            {
                maxCrownsMoves.add(move);
            }
        }

        return maxCrownsMoves;
    }


    public static ArrayList<KingdomMovePair> removeMovesBreakingMiddleKingdomRule(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<KingdomMovePair> validPairs = new ArrayList<>(kingdomMovePairs.size());

        for (final KingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            final PlacedDomino placedDomino = kingdomMovePair.getMove().getPlacedDomino();

            if (placedDomino != null)
            {
                final Position tile1Position = placedDomino.getTile1().getPosition();
                final Position tile2Position = placedDomino.getTile2().getPosition();

                final boolean isWithinBounds = Math.abs(tile1Position.getColumn()) < 3 && Math.abs(tile1Position.getRow()) < 3
                        && Math.abs(tile2Position.getColumn()) < 3 && Math.abs(tile2Position.getRow()) < 3;

                if (isWithinBounds)
                {
                    validPairs.add(kingdomMovePair);
                }
            }
        }

        return validPairs;
    }


    public static ArrayList<KingdomMovePair> removeMovesCreatingSingleTileHoles(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<KingdomMovePair> validPairs = new ArrayList<>(kingdomMovePairs.size());

        for (final KingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            final PlacedDomino placedDomino = kingdomMovePair.getMove().getPlacedDomino();

            if (placedDomino != null)
            {
                final Kingdom kingdom = kingdomMovePair.getKingdom();
                final ArrayList<PlacedTile> allPlacedTiles = ArrayUtils.toArrayList(kingdom.getPlacedTiles());
                allPlacedTiles.addAll(placedDomino.getPlacedTiles());

                final ArrayList<Position> adjacentPositions = getAdjacentPositions(placedDomino);
                final boolean containsSingleTileHole = checkForSingleTileHole(adjacentPositions, allPlacedTiles);

                if (! containsSingleTileHole)
                {
                    validPairs.add(kingdomMovePair);
                }
            }
        }

        return validPairs;
    }




    private static boolean checkForSingleTileHole(final ArrayList<Position> positions,
                                                  final ArrayList<PlacedTile> placedTiles)
    {
        for (final Position position : positions)
        {
            if (isSingleTileHole(position, placedTiles))
            {
                return true;
            }
        }

        return false;
    }


    public static boolean isSingleTileHole(final Position position, final ArrayList<PlacedTile> placedTiles)
    {
        final ArrayList<Position> adjacentPositions = getAdjacentPositions(position);

        boolean isSingleTileHole = true;
        for (final Position adjacentPosition : adjacentPositions)
        {
            if (adjacentPosition.equals(new Position(0, 0)))
            {
                continue;
            }

            boolean hasTile = false;

            for (final PlacedTile placedTile : placedTiles)
            {
                if (placedTile.getPosition().equals(adjacentPosition))
                {
                    hasTile = true;
                    break;
                }
            }

            if (! hasTile)
            {
                isSingleTileHole = false;
                break;
            }
        }

        return isSingleTileHole;
    }


    public static ArrayList<Position> getAdjacentPositions(final PlacedDomino placedDomino)
    {
        final Position tile1Position = placedDomino.getTile1().getPosition();
        final Position tile2Position = placedDomino.getTile2().getPosition();

        final ArrayList<Position> tile1Adjacent = getAdjacentPositions(tile1Position);
        final ArrayList<Position> tile2Adjacent = getAdjacentPositions(tile2Position);

        final ArrayList<Position> allAdjacent = new ArrayList<>(8);
        allAdjacent.addAll(tile1Adjacent);
        allAdjacent.addAll(tile2Adjacent);
        allAdjacent.remove(tile1Position);
        allAdjacent.remove(tile2Position);

        return allAdjacent;
    }

    public static ArrayList<Position> getAdjacentPositions(final Position position)
    {
        final ArrayList<Position> adjacentPositions = new ArrayList<>(4);

        adjacentPositions.add(new Position(position.getRow() - 1, position.getColumn()));       // N
        adjacentPositions.add(new Position(position.getRow() + 1, position.getColumn()));       // S
        adjacentPositions.add(new Position(position.getRow(), position.getColumn() + 1));    // E
        adjacentPositions.add(new Position(position.getRow(), position.getColumn() - 1));    // W

        return adjacentPositions;
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
