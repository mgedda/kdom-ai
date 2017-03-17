package kingdominoplayer.tinyrepresentation;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-14<br>
 * Time: 15:16<br><br>
 */
public class TinyScorerAlgorithm
{
    public static int applyTo(final byte[] kingdomTerrains, final byte[] kingdomCrowns)
    {
        final ArrayList<Byte> placedIndices = TinyUtils.getPlacedIndices(kingdomTerrains);

        int connectedComponentsScore = getConnectedTerrainsScore(placedIndices, kingdomTerrains, kingdomCrowns);
        int middleKingdomScore = getMiddleKingdomScore(placedIndices);
        int harmonyScore = getHarmonyScore(placedIndices);

        return connectedComponentsScore + middleKingdomScore + harmonyScore;
    }


    /**
     * Get bonus score if all dominoes are placed.
     *
     * @param placedIndices
     * @return
     */
    private static int getHarmonyScore(final ArrayList<Byte> placedIndices)
    {
        return placedIndices.size() == 24 ? 5 : 0;
    }


    /**
     * Get bonus score if castle is in the center of the 5x5 grid.
     *
     * @return
     */
    private static int getMiddleKingdomScore(final ArrayList<Byte> placedIndices)
    {
        for (final int placedIndex : placedIndices)
        {
            final int x = TinyGameState.indexToTileXCoordinate(placedIndex);
            final int y = TinyGameState.indexToTileYCoordinate(placedIndex);

            final boolean positionTooFarAwayFromCastle = x < -2 || x > 2 || y < -2 || y > 2;
            if (positionTooFarAwayFromCastle)
            {
                return 0;
            }
        }

        return 10;
    }


    /**
     * Get score for all connected terrain areas.
     *
     *
     * @param placedIndices
     * @param kingdomTerrains
     * @param kingdomCrowns
     * @return
     */
    private static int getConnectedTerrainsScore(final ArrayList<Byte> placedIndices, final byte[] kingdomTerrains, final byte[] kingdomCrowns)
    {
        final LinkedHashSet<Byte> scoredIndices = new LinkedHashSet<>(2 * kingdomTerrains.length);

        int connectedComponentsScore = 0;

        for (final byte index : placedIndices)
        {
            if (scoredIndices.contains(index))
            {
                continue;
            }

            final Set<Byte> visitedIndices = new LinkedHashSet<>(2 * kingdomTerrains.length);
            final Set<Byte> connectedTerrainIndices = new LinkedHashSet<>(2 * kingdomTerrains.length);

            visitedIndices.addAll(scoredIndices);
            visitedIndices.add(index);
            connectedTerrainIndices.add(index);

            final byte terrain = kingdomTerrains[index];
            for (final byte adjacentIndex : TinyUtils.getAdjacentIndices(index, TinyConst.KINGDOM_X_SIZE, TinyConst.KINGDOM_Y_SIZE))
            {
                if (! visitedIndices.contains(adjacentIndex))
                {
                    getConnectedTerrainTilesRecursively(terrain, adjacentIndex, /* output */ connectedTerrainIndices, /* output */ visitedIndices, kingdomTerrains);
                }
            }

            final int connectedTerrainScore = computeConnectedTerrainScore(connectedTerrainIndices, kingdomTerrains, kingdomCrowns);

            connectedComponentsScore += connectedTerrainScore;
            scoredIndices.addAll(connectedTerrainIndices);
        }

        return connectedComponentsScore;
    }




    /**
     * Find adjacent tiles that has terrain=currentTerrain recursively.
     *
     * @param currentTerrain
     * @param index
     * @param connectedTerrainIndices
     * @param visitedIndices
     * @param kingdomTerrains
     */
    private static void getConnectedTerrainTilesRecursively(final byte currentTerrain,
                                                            final byte index,
                                                            /* UPDATED */ final Set<Byte> connectedTerrainIndices,
                                                            /* UPDATED */ final Set<Byte> visitedIndices,
                                                            final byte[] kingdomTerrains)
    {
        final byte terrain = kingdomTerrains[index];
        if (terrain == TerrainCode.from("none") || terrain == TerrainCode.from("castle"))
        {
            return;
        }

        visitedIndices.add(index);

        if (terrain == currentTerrain)
        {
            connectedTerrainIndices.add(index);

            final ArrayList<Byte> adjacentIndices = TinyUtils.getAdjacentIndices(index, TinyConst.KINGDOM_X_SIZE, TinyConst.KINGDOM_Y_SIZE);

            for (final byte adjacentIndex : adjacentIndices)
            {
                if (! visitedIndices.contains(adjacentIndex))
                {
                    getConnectedTerrainTilesRecursively(currentTerrain, adjacentIndex, connectedTerrainIndices, visitedIndices, kingdomTerrains);
                }
            }
        }
    }



    /**
     * Compute score for connected terrains at indices.
     *
     * @param indices
     * @param kingdomTerrains
     * @param kingdomCrowns
     * @return
     */
    private static int computeConnectedTerrainScore(final Set<Byte> indices, final byte[] kingdomTerrains, final byte[] kingdomCrowns)
    {
        if (indices.isEmpty())
        {
            return 0;
        }

        byte terrain = TerrainCode.from("none");   // for debug purposes
        int numCrowns = 0;

        for (final int i : indices)
        {
            if (terrain == TerrainCode.from("none"))
            {
                terrain = kingdomTerrains[i];
                assert terrain != TerrainCode.from("none") : "Computing connected terrain score on unoccupied terrain!";
            }

            assert kingdomTerrains[i] == terrain : "Found placed tile with different terrain!";
            numCrowns += kingdomCrowns[i];
        }

        return numCrowns * indices.size();
    }




}
