package kingdominoplayer.naiverepresentation.planning;

import kingdominoplayer.utils.GameUtils;
import kingdominoplayer.naiverepresentation.datastructures.PlacedTile;
import kingdominoplayer.naiverepresentation.datastructures.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 12:46<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class Scorer
{
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static int computeScore(final Collection<PlacedTile> placedTiles)
    {
        // Compute connected components score.
        //
        int connectedComponentsScore = getConnectedComponentsScore(placedTiles);

        int middleKingdomScore = getMiddleKingdomScore(placedTiles);

        int harmonyScore = getHarmonyScore(placedTiles);

        final int totalScore = connectedComponentsScore + middleKingdomScore + harmonyScore;

        return totalScore;
    }


    private static int getHarmonyScore(final Collection<PlacedTile> placedTiles)
    {
        return placedTiles.size() == 25 ? 5 : 0;
    }


    private static int getMiddleKingdomScore(final Collection<PlacedTile> placedTiles)
    {
        for (final PlacedTile placedTile : placedTiles)
        {
            final Position position = placedTile.getPosition();

            final boolean positionTooFarAwayFromCastle =
                    Math.abs(position.getColumn()) > 2 || Math.abs(position.getRow()) > 2;

            if (positionTooFarAwayFromCastle)
            {
                return 0;
            }
        }

        return 10;
    }


    private static int getConnectedComponentsScore(final Collection <PlacedTile> placedTiles)
    {
        final LinkedHashSet<PlacedTile> tilesScored = new LinkedHashSet<>(placedTiles.size());

        int connectedComponentsScore = 0;

        for (final PlacedTile placedTile : placedTiles)
        {
            if (tilesScored.contains(placedTile) || placedTile.getTerrain().equals("castle"))
            {
                continue;
            }

            final String terrain = placedTile.getTerrain();
            final Position position = placedTile.getPosition();

            final ArrayList<Position> adjacentPositions = GameUtils.getAdjacentPositions(position);

            final ArrayList<PlacedTile> visitedTiles = new ArrayList<>();
            final ArrayList<PlacedTile> connectedTerrainTiles = new ArrayList<>();

            visitedTiles.add(placedTile);
            connectedTerrainTiles.add(placedTile);

            for (final Position adjacentPosition : adjacentPositions)
            {
                getConnectedTerrainTilesRecursively(
                        terrain, adjacentPosition, placedTiles, /* output */ connectedTerrainTiles, /* output */ visitedTiles);
            }

            final LinkedHashSet<PlacedTile> connectedTerrainTilesUnique = getUniqueTiles(connectedTerrainTiles);

            final int connectedTerrainScore = computeConnectedTerrainScore(connectedTerrainTilesUnique);

            connectedComponentsScore += connectedTerrainScore;

            tilesScored.addAll(connectedTerrainTilesUnique);
        }

        return connectedComponentsScore;
    }

    private static int computeConnectedTerrainScore(final Set<PlacedTile> placedTiles)
    {
        if (placedTiles.isEmpty())
        {
            return 0;
        }

        String terrain = "";   // for debug purposes
        int numCrowns = 0;

        for (final PlacedTile placedTile : placedTiles)
        {
            if (terrain.equals(""))
            {
                terrain = placedTile.getTerrain();
            }

            assert placedTile.getTerrain().equals(terrain) : "found placed tile with different terrain";

            numCrowns += placedTile.getCrowns();
        }

        return numCrowns * placedTiles.size();
    }


    private static LinkedHashSet<PlacedTile> getUniqueTiles(final ArrayList<PlacedTile> placedTiles)
    {
        final LinkedHashSet<PlacedTile> unique = new LinkedHashSet<>(placedTiles.size());
        unique.addAll(placedTiles);

        return unique;
    }


    /*package*/ static void getConnectedTerrainTilesRecursively(final String terrain,
                                                                final Position position,
                                                                final Collection<PlacedTile> placedTiles,
                                                                /* UPDATED */ final ArrayList<PlacedTile> connectedTerrainTiles,
                                                                /* UPDATED */ final ArrayList<PlacedTile> visitedTiles)
    {
        final PlacedTile adjacentTile = GameUtils.getTileAtPosition(position, placedTiles);

        if (adjacentTile != null && ! contains(adjacentTile, visitedTiles))
        {
            visitedTiles.add(adjacentTile);

            if (adjacentTile.getTerrain().equals(terrain))
            {
                connectedTerrainTiles.add(adjacentTile);
                final ArrayList<Position> adjacentPositions = GameUtils.getAdjacentPositions(adjacentTile.getPosition());

                for (final Position adjacentPosition : adjacentPositions)
                {
                    getConnectedTerrainTilesRecursively(terrain, adjacentPosition, placedTiles, connectedTerrainTiles, visitedTiles);
                }
            }
        }
    }



    private static boolean contains(final PlacedTile placedTile, final ArrayList<PlacedTile> placedTiles)
    {
        for (final PlacedTile tile : placedTiles)
        {
            if (tile.getPosition().equals(placedTile.getPosition()))
            {
                return true;
            }
        }

        return false;
    }

}
