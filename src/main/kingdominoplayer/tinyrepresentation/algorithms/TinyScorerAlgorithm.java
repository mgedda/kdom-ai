package kingdominoplayer.tinyrepresentation.algorithms;

import it.unimi.dsi.fastutil.bytes.ByteSet;
import kingdominoplayer.tinyrepresentation.TinyUtils;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;


public class TinyScorerAlgorithm
{
    public static int applyTo(final byte[] kingdomTerrains, final byte[] kingdomCrowns)
    {
        int connectedComponentsScore = getConnectedTerrainsScore(kingdomTerrains, kingdomCrowns);

        final ByteSet placedIndices = TinyUtils.getPlacedIndices(kingdomTerrains);
        int middleKingdomScore = getMiddleKingdomScore(placedIndices);
        int harmonyScore = getHarmonyScore(placedIndices);

        return connectedComponentsScore + middleKingdomScore + harmonyScore;
    }

    private static int getConnectedTerrainsScore(final byte[] kingdomTerrains, final byte[] kingdomCrowns)
    {
        // The "two-pass" algorithm makes each round ~10-100% faster than the recursive
        // algorithm according to tests 2017-03-22.
        //
        //return new RecursiveConnectedTerrainScoreAlgorithm().applyTo(kingdomTerrains, kingdomCrowns);
        return new TwoPassConnectedTerrainScoreAlgorithm().applyTo(kingdomTerrains, kingdomCrowns);
    }


    /**
     * Get bonus score if all dominoes are placed.
     *
     * @param placedIndices
     * @return
     */
    private static int getHarmonyScore(final ByteSet placedIndices)
    {
        return placedIndices.size() == 25 ? 5 : 0;
    }


    /**
     * Get bonus score if castle is in the center of the 5x5 grid.
     *
     * @return
     */
    private static int getMiddleKingdomScore(final ByteSet placedIndices)
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
