package kingdominoplayer.tinyrepresentation;

import kingdominoplayer.tinyrepresentation.datastructures.TerrainCode;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 12:41<br><br>
 */
public class TinyUtils
{
    public static String to2DArrayString(final byte[] byteArray, final int xSize)
    {
        String result = "{";

        final int ySize = byteArray.length / xSize;
        for (int y = 0; y < ySize; ++y)
        {
            for (int x = 0; x < xSize; ++x)
            {
                result = result.concat(" ".concat(Byte.toString(byteArray[y * xSize + x])));
                if (x < xSize - 1)
                {
                    result = result.concat(",");
                }
            }

            if (y == ySize - 1)
            {
                result = result.concat("}");
            }
            else
            {
                result = result.concat("\n");
            }
        }

        return result;
    }


    /**
     * Get indices adjacent to i in two-dimensional array.
     *
     * @param i
     * @param xSize size of one row in the two-dimensional array
     * @param ySize size of one column in the two dimensional array
     * @return
     */
    public static ArrayList<Byte> getAdjacentIndices(final byte i, final int xSize, final int ySize)
    {
        final ArrayList<Byte> adjacentIndices = new ArrayList<>(4);


        final int x = TinyGameState.indexToArrayXCoordinate(i);
        final int y = TinyGameState.indexToArrayYCoordinate(i);

        if (x > 0)
        {
            adjacentIndices.add((byte)(i - 1));
        }

        if (x < xSize-1)
        {
            adjacentIndices.add((byte)(i + 1));
        }

        if (y > 0)
        {
            adjacentIndices.add((byte)(i - xSize));
        }

        if (y < ySize-1)
        {
            adjacentIndices.add((byte)(i + xSize));
        }

        return adjacentIndices;
    }


    /**
     * Get indices of all placed tiles (excluding the castle).
     *
     * @param playerKingdomTerrains
     * @return
     */
    public static LinkedHashSet<Byte> getPlacedIndices(final byte[] playerKingdomTerrains)
    {
        final LinkedHashSet<Byte> placedIndices = new LinkedHashSet<>(2 * 5 * 5);  // 2x upper bound

        for (byte i = 0; i < playerKingdomTerrains.length; i++)
        {
            if (playerKingdomTerrains[i] != TerrainCode.from("none"))
            {
                placedIndices.add(i);
            }
        }

        return placedIndices;
    }

    public static byte[] place(final byte tile1Value,
                               final byte tile2Value,
                               final byte tile1X,
                               final byte tile1Y,
                               final byte tile2X,
                               final byte tile2Y,
                               final byte[] kingdom)
    {
        final byte[] updatedKingdom = Arrays.copyOf(kingdom, kingdom.length);

        final int tile1Index = TinyGameState.tileCoordinateToLinearIndex(tile1X, tile1Y);
        final int tile2Index = TinyGameState.tileCoordinateToLinearIndex(tile2X, tile2Y);

        updatedKingdom[tile1Index] = tile1Value;
        updatedKingdom[tile2Index] = tile2Value;

        return updatedKingdom;
    }


    public static byte[] placeTerrains(final byte[] move, final byte[] kingdomTerrains)
    {
        assert move[TinyConst.MOVE_PLACED_DOMINO_INDEX] != TinyConst.INVALID_DOMINO_VALUE : "Move has no placed domino!";

        final byte[] updatedKingdomTerrains = Arrays.copyOf(kingdomTerrains, kingdomTerrains.length);

        final byte tile1Terrain = move[TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_TILE_1_TERRAIN_INDEX];
        final byte tile2Terrain = move[TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_TILE_2_TERRAIN_INDEX];

        final byte tile1X = move[TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_TILE_1_X_INDEX];
        final byte tile1Y = move[TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_TILE_1_Y_INDEX];

        final byte tile2X = move[TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_TILE_2_X_INDEX];
        final byte tile2Y = move[TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_TILE_2_Y_INDEX];

        final int tile1Index = TinyGameState.tileCoordinateToLinearIndex(tile1X, tile1Y);
        final int tile2Index = TinyGameState.tileCoordinateToLinearIndex(tile2X, tile2Y);

        updatedKingdomTerrains[tile1Index] = tile1Terrain;
        updatedKingdomTerrains[tile2Index] = tile2Terrain;

        return updatedKingdomTerrains;
    }

    public static byte[] placeCrowns(final byte[] move, final byte[] kingdomCrowns)
    {
        assert move[TinyConst.MOVE_PLACED_DOMINO_INDEX] != TinyConst.INVALID_DOMINO_VALUE : "Move has no placed domino!";

        final byte[] updatedKingdomCrowns = Arrays.copyOf(kingdomCrowns, kingdomCrowns.length);

        final byte tile1Crowns = move[TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_TILE_1_CROWNS_INDEX];
        final byte tile2Crowns = move[TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_TILE_2_CROWNS_INDEX];

        final byte tile1X = move[TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_TILE_1_X_INDEX];
        final byte tile1Y = move[TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_TILE_1_Y_INDEX];

        final byte tile2X = move[TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_TILE_2_X_INDEX];
        final byte tile2Y = move[TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_TILE_2_Y_INDEX];

        final int tile1Index = TinyGameState.tileCoordinateToLinearIndex(tile1X, tile1Y);
        final int tile2Index = TinyGameState.tileCoordinateToLinearIndex(tile2X, tile2Y);

        updatedKingdomCrowns[tile1Index] = tile1Crowns;
        updatedKingdomCrowns[tile2Index] = tile2Crowns;

        return updatedKingdomCrowns;
    }
}
