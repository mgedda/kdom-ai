package kingdominoplayer.tinyrepresentation.algorithms;

import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import kingdominoplayer.tinyrepresentation.TinyUtils;
import kingdominoplayer.tinyrepresentation.datastructures.TerrainCode;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.utils.collections.ByteCompactLinkedSet;

import java.util.LinkedHashSet;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-17<br>
 * Time: 13:46<br><br>
 */
@Deprecated // use TinyValidPositionsEfficientAlgorithm instead.
public class TinyValidPositionsAlgorithm
{

    public byte[] applyTo(final byte[] dominoToPlace, final byte[] kingdomTerrains)
    {
        final LinkedHashSet<TinyDominoPosition> validDominoPositions = getValidPositions(dominoToPlace, kingdomTerrains);


        // Build result.
        //
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

    /*package*/ LinkedHashSet<TinyDominoPosition> getValidPositions(final byte[] dominoToPlace, final byte[] kingdomTerrains)
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

        for (final byte terrain : terrains)
        {
            // Find all unique positions adjacent to tiles with current terrain (or the castle).
            //
            ByteSet possibleTilePositions = getAllAdjacentTilePositions(terrain, placedIndices, kingdomTerrains);


            // Remove all occupied positions.
            //
            possibleTilePositions = removePositions(possibleTilePositions, placedIndices);



            // Remove any positions outside 5x5 grid.
            //
            int minRow = 100;
            int maxRow = -100;
            int minCol = 100;
            int maxCol = -100;

            for (final byte placedIndex : placedIndices)
            {
                final int col = TinyGameState.indexToArrayXCoordinate(placedIndex);
                final int row = TinyGameState.indexToArrayYCoordinate(placedIndex);

                minRow = row < minRow ? row : minRow;
                maxRow = row > maxRow ? row : maxRow;

                minCol = col < minCol ? col : minCol;
                maxCol = col > maxCol ? col : maxCol;
            }

            possibleTilePositions = removePositionsOutside5x5Grid(possibleTilePositions, minCol, maxCol, minRow, maxRow);


            // Find valid domino positions from tile positions.
            //
            for (final byte position : possibleTilePositions)
            {
                final ByteList adjacentPositions = TinyUtils.getAdjacentIndices(position, TinyConst.KINGDOM_X_SIZE, TinyConst.KINGDOM_Y_SIZE);

                for (final byte adjacentPosition : adjacentPositions)
                {
                    final boolean isOccupied = placedIndices.contains(adjacentPosition);
                    final boolean isOutOfBounds = isOutOfBounds(adjacentPosition, minRow, maxRow, minCol, maxCol);

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

    private ByteSet removePositionsOutside5x5Grid(final ByteSet possibleTilePositions, final int minCol, final int maxCol, final int minRow, final int maxRow)
    {
        final ByteCompactLinkedSet result = new ByteCompactLinkedSet();

        for (final byte position : possibleTilePositions)
        {
            final boolean isPositionOutOfBounds = isOutOfBounds(position, minRow, maxRow, minCol, maxCol);
            if (! isPositionOutOfBounds)
            {
                result.add(position);
            }
        }

        return result;
    }


    private static boolean isOutOfBounds(final byte position, final int minRow, final int maxRow, final int minCol, final int maxCol)
    {
        final int col = TinyGameState.indexToArrayXCoordinate(position);
        final int row = TinyGameState.indexToArrayYCoordinate(position);

        return row < maxRow - 4 || row > minRow + 4 || col < maxCol - 4 || col > minCol + 4;
    }


    private ByteSet removePositions(final ByteSet positions, final ByteSet positionsToRemove)
    {
        final ByteCompactLinkedSet result = new ByteCompactLinkedSet(); // 2x upper limit

        for (final byte position : positions)
        {
            if (! positionsToRemove.contains(position))
            {
                result.add(position);
            }
        }

        return result;
    }


    private ByteSet getAllAdjacentTilePositions(final byte terrain, final ByteSet placedIndices, final byte[] kingdomTerrains)
    {
        final ByteCompactLinkedSet allAdjacentTilePositions = new ByteCompactLinkedSet(); // 2x upper limit

        for (final byte placedIndex : placedIndices)
        {
            if (kingdomTerrains[placedIndex] == terrain || kingdomTerrains[placedIndex] == TerrainCode.from("CASTLE"))
            {
                final ByteList adjacentIndices = TinyUtils.getAdjacentIndices(placedIndex, TinyConst.KINGDOM_X_SIZE, TinyConst.KINGDOM_Y_SIZE);
                allAdjacentTilePositions.addAll(adjacentIndices);
            }
        }

        return allAdjacentTilePositions;
    }
}
