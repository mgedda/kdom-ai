package kingdominoplayer.tinyrepresentation.algorithms;

import kingdominoplayer.tinyrepresentation.TinyUtils;
import kingdominoplayer.tinyrepresentation.datastructures.TerrainCode;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-22<br>
 * Time: 08:28<br><br>
 */
@Deprecated // use TwoPassConnectedTerrainScoreAlgorithm instead.
/*package*/ class RecursiveConnectedTerrainScoreAlgorithm implements ConnectedTerrainScoreAlgorithm
{

    /**
     * Get score for all connected terrain areas.
     *
     * @param kingdomTerrains
     * @param kingdomCrowns
     * @return
     */
    public int applyTo(final byte[] kingdomTerrains, final byte[] kingdomCrowns)
    {
        final LinkedHashSet<Byte> scoredIndices = new LinkedHashSet<>(2 * kingdomTerrains.length);

        int connectedComponentsScore = 0;

        final Set<Byte> placedIndices = TinyUtils.getPlacedIndices(kingdomTerrains);

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
    private void getConnectedTerrainTilesRecursively(final byte currentTerrain,
                                                     final byte index,
                                                     /* UPDATED */ final Set<Byte> connectedTerrainIndices,
                                                     /* UPDATED */ final Set<Byte> visitedIndices,
                                                     final byte[] kingdomTerrains)
    {
        final byte terrain = kingdomTerrains[index];
        if (terrain == TerrainCode.from("NONE") || terrain == TerrainCode.from("CASTLE"))
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
    private int computeConnectedTerrainScore(final Set<Byte> indices, final byte[] kingdomTerrains, final byte[] kingdomCrowns)
    {
        if (indices.isEmpty())
        {
            return 0;
        }

        byte terrain = TerrainCode.from("NONE");   // for debug purposes
        int numCrowns = 0;

        for (final int i : indices)
        {
            if (terrain == TerrainCode.from("NONE"))
            {
                terrain = kingdomTerrains[i];
                assert terrain != TerrainCode.from("NONE") : "Computing connected terrain score on unoccupied terrain!";
            }

            assert kingdomTerrains[i] == terrain : "Found placed tile with different terrain!";
            numCrowns += kingdomCrowns[i];
        }

        return numCrowns * indices.size();
    }

}
