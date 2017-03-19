package kingdominoplayer.tinyrepresentation;

import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-18<br>
 * Time: 22:25<br><br>
 */
public class TinyKingdomMovePair
{
    private final byte[] iKingdomTerrains;
    private final byte[] iKingdomCrowns;
    private final TinyMove iMove;

    private int iScore = -1;

    public TinyKingdomMovePair(final byte[] kingdomTerrains, final byte[] kingdomCrowns, final TinyMove move)
    {
        iKingdomTerrains = kingdomTerrains;
        iKingdomCrowns = kingdomCrowns;
        iMove = move;
    }

    public byte[] getKingdomTerrains()
    {
        return iKingdomTerrains;
    }

    public byte[] getKingdomCrowns()
    {
        return iKingdomCrowns;
    }

    public TinyMove getMove()
    {
        return iMove;
    }


    public int getScore()
    {
        if (iScore < 0)
        {
            iScore = TinyScorerAlgorithm.applyTo(iKingdomTerrains, iKingdomCrowns);
        }

        return iScore;
    }

    public Set<Byte> getPlacedIndices()
    {
        return TinyUtils.getPlacedIndices(iKingdomTerrains);
    }

    public TinyKingdomMovePair withChosenDominoPlaced(final byte tile1X, final byte tile1Y, final byte tile2X, final byte tile2Y)
    {
        final byte[] chosenDomino = iMove.getChosenDomino();
        final byte tile1Terrain = chosenDomino[TinyConst.DOMINO_TILE_1_TERRAIN_INDEX];
        final byte tile1Crowns = chosenDomino[TinyConst.DOMINO_TILE_1_CROWNS_INDEX];
        final byte tile2Terrain = chosenDomino[TinyConst.DOMINO_TILE_2_TERRAIN_INDEX];
        final byte tile2Crowns = chosenDomino[TinyConst.DOMINO_TILE_2_CROWNS_INDEX];

        final byte[] kingdomTerrains = TinyUtils.place(tile1Terrain, tile2Terrain, tile1X, tile1Y, tile2X, tile2Y, iKingdomTerrains);
        final byte[] kingdomCrowns = TinyUtils.place(tile1Crowns, tile2Crowns, tile1X, tile1Y, tile2X, tile2Y, iKingdomCrowns);

        return new TinyKingdomMovePair(kingdomTerrains, kingdomCrowns, iMove);
    }

    public TinyKingdomMovePair withPlacedDominoPlaced()
    {
        final byte[] kingdomTerrains = TinyUtils.placeTerrains(getMove().getArray(), iKingdomTerrains);
        final byte[] kingdomCrowns = TinyUtils.placeCrowns(getMove().getArray(), iKingdomCrowns);

        return new TinyKingdomMovePair(kingdomTerrains, kingdomCrowns, iMove);
    }

    public byte[] getChosenDomino()
    {
        return iMove.getChosenDomino();
    }

}
