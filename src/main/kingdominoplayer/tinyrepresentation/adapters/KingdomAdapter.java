package kingdominoplayer.tinyrepresentation.adapters;

import kingdominoplayer.naiverepresentation.datastructures.Kingdom;
import kingdominoplayer.naiverepresentation.datastructures.PlacedTile;
import kingdominoplayer.naiverepresentation.datastructures.Position;
import kingdominoplayer.tinyrepresentation.datastructures.TerrainCode;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-22<br>
 * Time: 08:46<br><br>
 */
public class KingdomAdapter
{
    /**
     * Get kingdom terrains as byte array representation.
     *
     * @param kingdom
     * @return
     */
    public static byte[] toTinyKingdomTerrains(final Kingdom kingdom)
    {
        final byte[] result = new byte[TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];

        for (final PlacedTile placedTile : kingdom.getPlacedTiles())
        {
            final Position position = placedTile.getPosition();
            final int x = position.getColumn();
            final int y = position.getRow();

            final String terrain = placedTile.getTerrain();
            final int terrainIndex = TinyGameState.tileCoordinateToLinearIndex(x, y);
            result[terrainIndex] = TerrainCode.from(terrain);
        }

        return result;
    }

    /**
     * Get kingdom crowns as byte array representation.
     *
     * @param kingdom
     * @return
     */
    public static byte[] toTinyKingdomCrowns(final Kingdom kingdom)
    {
        final byte[] result = new byte[TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];

        for (final PlacedTile placedTile : kingdom.getPlacedTiles())
        {
            final Position position = placedTile.getPosition();
            final int x = position.getColumn();
            final int y = position.getRow();

            final int crowns = placedTile.getCrowns();
            final int crownsIndex = TinyGameState.tileCoordinateToLinearIndex(x, y);
            result[crownsIndex] = (byte) crowns;
        }

        return result;
    }

}
