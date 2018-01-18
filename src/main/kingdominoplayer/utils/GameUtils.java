package kingdominoplayer.utils;

import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import kingdominoplayer.ServerResponseParser;
import kingdominoplayer.Player;
import kingdominoplayer.naiverepresentation.datastructures.Domino;
import kingdominoplayer.naiverepresentation.datastructures.Kingdom;
import kingdominoplayer.naiverepresentation.datastructures.KingdomMovePair;
import kingdominoplayer.naiverepresentation.datastructures.Move;
import kingdominoplayer.naiverepresentation.datastructures.PlacedTile;
import kingdominoplayer.naiverepresentation.datastructures.Position;
import kingdominoplayer.naiverepresentation.datastructures.Positions;
import kingdominoplayer.naiverepresentation.datastructures.Tile;

import java.util.*;

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
        return ServerResponseParser.getPlayerPlacedTiles(gameState, player.getName());
    }


    public static Domino[] getPreviousDraft(final Player player, final String gameState)
    {
        return ServerResponseParser.getPreviousDraftForPlayer(gameState, player.getName());
    }

    public static Domino[] getCurrentDraft(final Player player, final String gameState)
    {
        return ServerResponseParser.getCurrentDraftForPlayer(gameState, player.getName());
    }


    public static PlacedTile getTileAtPosition(final Position position, final Collection<PlacedTile> placedTiles)
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


    public static LinkedHashMap<String, Tile[]> getTerrainToTilesMap(final Collection<? extends Tile> tiles)
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




    public static String[] getTerrainsSortedBasedOnNumberOfTilesUseCrownsAsDealBreaker(final Collection<? extends Tile> tiles)
    {
        if (tiles.isEmpty())
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


    public static ArrayList<KingdomMovePair> removeKingdomMovePairsBreakingMiddleKingdomRule(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<KingdomMovePair> kingdomMovePairsFulfillingRule = new ArrayList<>(kingdomMovePairs.size());

        for (final KingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            boolean kingdomFulfilsRule = true;
            for (final PlacedTile placedTile : kingdomMovePair.getKingdom().getPlacedTiles())
            {
                final Position position = placedTile.getPosition();

                final boolean isWithinBounds = Math.abs(position.getColumn()) < 3 && Math.abs(position.getRow()) < 3;

                if (! isWithinBounds)
                {
                    kingdomFulfilsRule = false;
                    break;
                }
            }

            if (kingdomFulfilsRule)
            {
                kingdomMovePairsFulfillingRule.add(kingdomMovePair);
            }

        }

        return kingdomMovePairsFulfillingRule;
    }


    public static ArrayList<KingdomMovePair> removeKingdomMovePairsWithSingleTileHoles(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<KingdomMovePair> validPairs = new ArrayList<>(kingdomMovePairs.size());

        final ArrayList<Kingdom> DEBUG_kingdoms = new ArrayList<>(kingdomMovePairs.size());
        final ArrayList<Positions> DEBUG_positionsArray = new ArrayList<>(kingdomMovePairs.size());

        for (final KingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            final Kingdom kingdom = kingdomMovePair.getKingdom();
            final Collection<PlacedTile> placedTiles = kingdom.getPlacedTiles();

            final LinkedHashSet<Position> adjacentPositions = new LinkedHashSet<>(100);
            for (final PlacedTile placedTile : placedTiles)
            {
                final ArrayList<Position> positions = getAdjacentPositions(placedTile.getPosition());
                adjacentPositions.addAll(positions);
            }
            adjacentPositions.removeAll(kingdom.getPlacedTilePositions());

            final ArrayList<Position> singleTileHolePositions = getSingleTileHoles(adjacentPositions, placedTiles);

            if (singleTileHolePositions.isEmpty())
            {
                validPairs.add(kingdomMovePair);
            }

            DEBUG_kingdoms.add(kingdom);
            DEBUG_positionsArray.add(new Positions(singleTileHolePositions));

            Util.noop();
        }


        //DebugPlot.plotWithPositionsMarked(DEBUG_kingdoms, DEBUG_positionsArray, "Single Tile Hole Positions Adjacent To Placed Domino");
        Util.noop();

        return validPairs;
    }



    private static ArrayList<Position> getSingleTileHoles(final Collection<Position> positions,
                                                          final Collection<PlacedTile> placedTiles)
    {
        final ArrayList<Position> singleTileHolePositions = new ArrayList<>(4);

        for (final Position position : positions)
        {
            if (isSingleTileHole(position, placedTiles))
            {
                singleTileHolePositions.add(position);
            }
        }

        return singleTileHolePositions;
    }


    public static boolean isSingleTileHole(final Position position, final Collection<PlacedTile> placedTiles)
    {
        final boolean isPositionOutsideCastleCenteredKingdom = Math.abs(position.getColumn()) > 2 || Math.abs(position.getRow()) > 2;
        if (isPositionOutsideCastleCenteredKingdom)
        {
            // Classifying a position outside the castle-centered kingdom will potentially
            // prevent good placements adjacent to this position.
            //
            //  For example,
            //
            //        C
            //        D
            //        D
            //        P
            //
            //  where C=castle, D=domino, P=position, will prevent the domino from being placed since
            //  P would be classed as isSingleTileHole would return true (all positions adjacent to P
            //  are outside the castle-centered kingdom except for one which is occupied by the lower
            //  D).
            //
            return false;
        }

        final ArrayList<Position> adjacentPositions = getAdjacentPositions(position);

        final BooleanArrayList  adjacentOccupiedStatuses = new BooleanArrayList(adjacentPositions.size());
        for (final Position adjacentPosition : adjacentPositions)
        {
            boolean isAdjacentOccupied = false;

            final boolean isCastleTile = adjacentPosition.equals(new Position(0, 0));
            // TODO [gedda] IMPORTANT! : change to outside 5x5 kingdom area!!!
            final boolean isOutsideCastleCenteredKingdom = Math.abs(adjacentPosition.getColumn()) > 2 || Math.abs(adjacentPosition.getRow()) > 2;
            final boolean isTilePosition = isTilePosition(adjacentPosition, placedTiles);

            if (isCastleTile || isOutsideCastleCenteredKingdom || isTilePosition)
            {
                isAdjacentOccupied = true;
            }

            adjacentOccupiedStatuses.add(isAdjacentOccupied);
        }

        boolean isSingleTileHole = true;
        for (final boolean isAdjacentOccupied : adjacentOccupiedStatuses)
        {
            isSingleTileHole &= isAdjacentOccupied;
        }

        return isSingleTileHole;
    }


    private static boolean isTilePosition(final Position position, final Collection<PlacedTile> placedTiles)
    {
        final ArrayList<Position> tilePositions = new ArrayList<>(placedTiles.size());
        for (final PlacedTile placedTile : placedTiles)
        {
            tilePositions.add(placedTile.getPosition());
        }
        return tilePositions.contains(position);
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
