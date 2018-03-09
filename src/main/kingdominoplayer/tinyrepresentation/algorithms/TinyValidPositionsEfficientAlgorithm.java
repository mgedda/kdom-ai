package kingdominoplayer.tinyrepresentation.algorithms;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import kingdominoplayer.tinyrepresentation.TinyUtils;
import kingdominoplayer.tinyrepresentation.datastructures.TerrainCode;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-22<br>
 * Time: 14:43<br><br>
 */
public class TinyValidPositionsEfficientAlgorithm
{
    private final static boolean SANITY_CHECK = false;


    public byte[] applyTo(final byte[] dominoToPlace, final byte[] kingdomTerrains)
    {
        final LinkedHashSet<TinyDominoPosition> validDominoPositions = getValidPositions(dominoToPlace, kingdomTerrains);

        if (SANITY_CHECK)
        {
            final LinkedHashSet<TinyDominoPosition> validDominoPositionsOld = new TinyValidPositionsAlgorithm().getValidPositions(dominoToPlace, kingdomTerrains);
            assert validDominoPositions.size() == validDominoPositionsOld.size() : "Not same size";

            for (final TinyDominoPosition dominoPosition : validDominoPositionsOld)
            {
                assert validDominoPositions.contains(dominoPosition) : "Position discrepancy!";
            }
        }

        //noinspection UnnecessaryLocalVariable
        final byte[] result = getPositionsAsByteArray(validDominoPositions);

        return result;
    }

    private LinkedHashSet<TinyDominoPosition> getValidPositions(final byte[] dominoToPlace, final byte[] kingdomTerrains)
    {
        final byte terrain1 = dominoToPlace[TinyConst.DOMINO_TILE_1_TERRAIN_INDEX];
        final byte terrain2 = dominoToPlace[TinyConst.DOMINO_TILE_2_TERRAIN_INDEX];

        final byte[] terrains;
        if (terrain1 == terrain2)
        {
            terrains = new byte[]{terrain1};
        }
        else
        {
            terrains = new byte[]{terrain1, terrain2};
        }


        LinkedHashSet<TinyDominoPosition> validDominoPositions = new LinkedHashSet<>(2 * 36 * 3);  // 2x approximated upper limit

        final ByteSet placedIndices = TinyUtils.getPlacedIndices(kingdomTerrains);
        final BoundingBox box = getBoundingBox(placedIndices);

        for (final byte terrain : terrains)
        {
            // Get empty tile positions adjacent to already placed tiles.
            //
            final ByteList emptyPositionsAdjacentToPlacedTiles =
                    getEmptyPositionsAdjacentToPlacedTiles(kingdomTerrains, placedIndices, box, terrain);

            // Find valid domino positions from tile positions.
            //
            for (final byte position : emptyPositionsAdjacentToPlacedTiles)
            {
                final ByteList adjacentPositions = TinyUtils.getAdjacentIndices(position, TinyConst.KINGDOM_X_SIZE, TinyConst.KINGDOM_Y_SIZE);

                for (final byte adjacentPosition : adjacentPositions)
                {
                    final boolean isOccupied = placedIndices.contains(adjacentPosition);
                    final boolean isOutOfBounds = isOutOfBounds(adjacentPosition,box);

                    final boolean isValidPosition = !(isOccupied || isOutOfBounds);
                    if (isValidPosition)
                    {
                        final TinyDominoPosition dominoPosition = terrain == terrain1
                                ? new TinyDominoPosition(position, adjacentPosition)
                                : new TinyDominoPosition(adjacentPosition, position);

                        validDominoPositions.add(dominoPosition);
                    }
                }
            }
        }


        // Remove any symmetrical domino positions
        //
        if (terrain1 == terrain2)
        {
            validDominoPositions = removeSymmetricalPositions(validDominoPositions);
        }
        return validDominoPositions;
    }

    private byte[] getPositionsAsByteArray(final LinkedHashSet<TinyDominoPosition> validDominoPositions)
    {
        final byte[] result = new byte[validDominoPositions.size() * TinyConst.DOMINOPOSITION_ELEMENT_SIZE];

        int counter = 0;
        for (final TinyDominoPosition position : validDominoPositions)
        {
            final byte tile1X = (byte) TinyGameState.indexToTileXCoordinate(position.iTile1Index);
            final byte tile1Y = (byte) TinyGameState.indexToTileYCoordinate(position.iTile1Index);
            final byte tile2X = (byte) TinyGameState.indexToTileXCoordinate(position.iTile2Index);
            final byte tile2Y = (byte) TinyGameState.indexToTileYCoordinate(position.iTile2Index);

            final int dominoPositionIndex = counter * TinyConst.DOMINOPOSITION_ELEMENT_SIZE;
            result[dominoPositionIndex + TinyConst.DOMINOPOSITION_TILE_1_X_INDEX] = tile1X;
            result[dominoPositionIndex + TinyConst.DOMINOPOSITION_TILE_1_Y_INDEX] = tile1Y;
            result[dominoPositionIndex + TinyConst.DOMINOPOSITION_TILE_2_X_INDEX] = tile2X;
            result[dominoPositionIndex + TinyConst.DOMINOPOSITION_TILE_2_Y_INDEX] = tile2Y;

            counter++;
        }
        return result;
    }

    private ByteList getEmptyPositionsAdjacentToPlacedTiles(final byte[] kingdomTerrains,
                                                            final ByteSet placedIndices,
                                                            final BoundingBox box,
                                                            final byte terrain)
    {
        final ByteArrayList result = new ByteArrayList(7 * 7);

        for (final byte placedIndex : placedIndices)
        {
            if (kingdomTerrains[placedIndex] == terrain || kingdomTerrains[placedIndex] == TerrainCode.from("CASTLE"))
            {
                final ByteList adjacentIndices = TinyUtils.getAdjacentIndices(placedIndex, TinyConst.KINGDOM_X_SIZE, TinyConst.KINGDOM_Y_SIZE);

                for (final byte adjacentIndex : adjacentIndices)
                {
                    final boolean isOccupied = placedIndices.contains(adjacentIndex);
                    final boolean isOutOfBounds = isOutOfBounds(adjacentIndex, box);
                    final boolean isValid = !isOccupied && !isOutOfBounds;

                    if (isValid)
                    {
                        result.add(adjacentIndex);
                    }
                }
            }
        }

        return result;
    }

    private BoundingBox getBoundingBox(final ByteSet placedIndices)
    {
        final BoundingBox box = new BoundingBox((byte) 100, (byte) -100, (byte) 100, (byte) -100);
        for (final byte placedIndex : placedIndices)
        {
            final byte col = (byte) TinyGameState.indexToArrayXCoordinate(placedIndex);
            final byte row = (byte) TinyGameState.indexToArrayYCoordinate(placedIndex);

            box.iMinRow = row < box.iMinRow ? row : box.iMinRow;
            box.iMaxRow = row > box.iMaxRow ? row : box.iMaxRow;

            box.iMinCol = col < box.iMinCol ? col : box.iMinCol;
            box.iMaxCol = col > box.iMaxCol ? col : box.iMaxCol;
        }
        return box;
    }

    private LinkedHashSet<TinyDominoPosition> removeSymmetricalPositions(final LinkedHashSet<TinyDominoPosition> validDominoPositions)
    {
        final LinkedHashSet<TinyDominoPosition> result = new LinkedHashSet<>(2 * validDominoPositions.size());

        for (final TinyDominoPosition dominoPosition : validDominoPositions)
        {
            if (! result.contains(dominoPosition.getInverted()))
            {
                result.add(dominoPosition);
            }
        }

        return result;
    }


    private static boolean isOutOfBounds(final byte position, final BoundingBox box)
    {
        final int col = TinyGameState.indexToArrayXCoordinate(position);
        final int row = TinyGameState.indexToArrayYCoordinate(position);

        return row < box.iMaxRow - 4 || row > box.iMinRow + 4 || col < box.iMaxCol - 4 || col > box.iMinCol + 4;
    }


    private static class BoundingBox
    {
        byte iMinCol;
        byte iMaxCol;
        byte iMinRow;
        byte iMaxRow;

        private BoundingBox(final byte minCol, final byte maxCol, final byte minRow, final byte maxRow)
        {
            iMinCol = minCol;
            iMaxCol = maxCol;
            iMinRow = minRow;
            iMaxRow = maxRow;
        }
    }
}
