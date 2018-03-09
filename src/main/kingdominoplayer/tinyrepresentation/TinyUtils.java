package kingdominoplayer.tinyrepresentation;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import kingdominoplayer.tinyrepresentation.datastructures.TerrainCode;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.utils.Random;
import kingdominoplayer.utils.collections.ByteCompactLinkedSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
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
            if (y > 0)
            {
                result = result.concat(" ");
            }

            for (int x = 0; x < xSize; ++x)
            {
                result = result.concat(" ".concat(Byte.toString(byteArray[y * xSize + x])));
                if (! (y == ySize - 1 && x == xSize - 1))
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
    public static ByteList getAdjacentIndices(final byte i, final int xSize, final int ySize)
    {
        final ByteArrayList adjacentIndices = new ByteArrayList(4);


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
     * Get indices of all placed tiles (excluding the castle). // TODO [gedda] IMPORTANT! : INCLUDING THE CASTLE???
     *
     * @param playerKingdomTerrains
     * @return
     */
    public static ByteSet getPlacedIndices(final byte[] playerKingdomTerrains)
    {
        final ByteCompactLinkedSet placedIndices = new ByteCompactLinkedSet();  // 2x upper bound

        final byte noTerrain = TerrainCode.from("NONE");

        for (byte i = 0; i < playerKingdomTerrains.length; i++)
        {
            if (playerKingdomTerrains[i] != noTerrain)
            {
                placedIndices.add(i);
            }
        }

        return placedIndices;
    }

    public static boolean hasPlacedTile(final byte[] playerKingdomTerrains)
    {
        final byte noTerrain = TerrainCode.from("NONE");
        final byte castleTerrain = TerrainCode.from("CASTLE");

        for (byte i = 0; i < playerKingdomTerrains.length; i++)
        {
            if (playerKingdomTerrains[i] != noTerrain && playerKingdomTerrains[i] != castleTerrain)
            {
                return true;
            }
        }

        return false;
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


    /**
     * Select numMovesToPick randomly from moves. If the number of moves in moves are fewer than
     * numMovesToPick, then return all moves.
     *
     * @param moves
     * @param numMovesToPick
     * @return
     */
    public static byte[] selectMovesRandomly(final byte[] moves, final int numMovesToPick)
    {
        final int numMoves = moves.length / TinyConst.MOVE_ELEMENT_SIZE;
        if (numMoves <= numMovesToPick)
        {
            return moves;
        }

        final byte[] movesToEvaluate = new byte[numMovesToPick * TinyConst.MOVE_ELEMENT_SIZE];

        final Set<Integer> pickedIndices = new LinkedHashSet<>(2 * numMovesToPick);

        while (pickedIndices.size() < numMovesToPick)
        {
            final int randomIndex = Random.getInt(numMoves);
            if (! pickedIndices.contains(randomIndex))
            {
                System.arraycopy(moves, randomIndex * TinyConst.MOVE_ELEMENT_SIZE, movesToEvaluate, pickedIndices.size() * TinyConst.MOVE_ELEMENT_SIZE, TinyConst.MOVE_ELEMENT_SIZE);
                pickedIndices.add(randomIndex);
            }
        }

        return movesToEvaluate;
    }

}
