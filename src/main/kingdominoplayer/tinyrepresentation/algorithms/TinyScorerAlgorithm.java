package kingdominoplayer.tinyrepresentation.algorithms;

import kingdominoplayer.tinyrepresentation.TinyUtils;
import kingdominoplayer.tinyrepresentation.datastructures.TerrainCode;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

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
        int connectedComponentsScore = getConnectedTerrainsScore(kingdomTerrains, kingdomCrowns);

        final Set<Byte> placedIndices = TinyUtils.getPlacedIndices(kingdomTerrains);
        int middleKingdomScore = getMiddleKingdomScore(placedIndices);
        int harmonyScore = getHarmonyScore(placedIndices);

        return connectedComponentsScore + middleKingdomScore + harmonyScore;
    }

    private static int getConnectedTerrainsScore(final byte[] kingdomTerrains, final byte[] kingdomCrowns)
    {
        return new RecursiveConnectedTerrainScoreAlgorithm().applyTo(kingdomTerrains, kingdomCrowns);
    }


    /**
     * Get bonus score if all dominoes are placed.
     *
     * @param placedIndices
     * @return
     */
    private static int getHarmonyScore(final Set<Byte> placedIndices)
    {
        return placedIndices.size() == 24 ? 5 : 0;
    }


    /**
     * Get bonus score if castle is in the center of the 5x5 grid.
     *
     * @return
     */
    private static int getMiddleKingdomScore(final Set<Byte> placedIndices)
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





}
