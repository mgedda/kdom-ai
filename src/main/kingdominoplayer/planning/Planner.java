package kingdominoplayer.planning;

import kingdominoplayer.datastructures.*;
import kingdominoplayer.utils.GameUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 16:31<br><br>
 */
public class Planner
{
    public static ArrayList<KingdomMovePair> getPossibleNewKingdoms(final Kingdom kingdom, final Move[] moves)
    {
        final ArrayList<KingdomMovePair> possibleNewKingdoms = new ArrayList<>();

        for (final Move move : moves)
        {
            final PlacedDomino placedDomino = move.getPlacedDomino();

            if (placedDomino != null) // maybe we could not place the domino
            {
                final Kingdom newKingdom = GameUtils.getKingdomWithDominoPlaced(kingdom, placedDomino);

                possibleNewKingdoms.add(new KingdomMovePair(newKingdom, move));
            }
            else
            {
                possibleNewKingdoms.add(new KingdomMovePair(kingdom, move));
            }
        }

        return possibleNewKingdoms;
    }

    /**
     * Find all valid positions for domino in kingdom.
     * @param domino
     * @param kingdom
     * @return
     */
    public static Set<DominoPosition> getValidPositions(final Domino domino, final Kingdom kingdom)
    {
        final String terrain1 = domino.getTile1().getTerrain();
        final String terrain2 = domino.getTile2().getTerrain();

        final ArrayList<String> terrains = new ArrayList<>(2);

        terrains.add(terrain1);
        if (! terrain2.equals(terrain1))
        {
            terrains.add(terrain2);
        }


        final LinkedHashSet<DominoPosition> validDominoPositions = new LinkedHashSet<>(100);

        for (final String terrain : terrains)
        {
            // Find all unique positions adjacent to tiles with current terrain (or the castle).
            //
            final LinkedHashSet<Position> possibleTilePositions = new LinkedHashSet<>(25);

            for (final PlacedTile placedTile : kingdom.getPlacedTiles())
            {
                if (placedTile.getTerrain().equals(terrain))
                {
                    final ArrayList<Position> adjacentPositions = GameUtils.getAdjacentPositions(placedTile.getPosition());
                    possibleTilePositions.addAll(adjacentPositions);
                }
            }

            final Position castlePosition = new Position(0, 0);

            final ArrayList<Position> positionsAdjacentToCastle = GameUtils.getAdjacentPositions(castlePosition);
            possibleTilePositions.addAll(positionsAdjacentToCastle);


            // Remove all occupied positions.
            //
            final LinkedHashSet<Position> occupiedPositions = kingdom.getPlacedTilePositions();
            occupiedPositions.add(castlePosition);

            final ArrayList<Position> occupiedList = new ArrayList<>(possibleTilePositions.size());

            for (final Position position : possibleTilePositions)
            {
                if (occupiedPositions.contains(position))
                {
                    occupiedList.add(position);
                }
            }

            possibleTilePositions.removeAll(occupiedList);


            // Remove any positions outside 5x5 grid.
            //
            int minRow = 0;
            int maxRow = 0;
            int minCol = 0;
            int maxCol = 0;

            for (final PlacedTile placedTile : kingdom.getPlacedTiles())
            {
                final int row = placedTile.getPosition().getRow();
                final int col = placedTile.getPosition().getColumn();

                minRow = row < minRow ? row : minRow;
                maxRow = row > maxRow ? row : maxRow;

                minCol = col < minCol ? col : minCol;
                maxCol = col > maxCol ? col : maxCol;
            }

            final ArrayList<Position> outOfBoundsList = new ArrayList<>(possibleTilePositions.size());

            for (final Position position : possibleTilePositions)
            {
                final boolean isPositionOutOfBounds = isOutOfBounds(position, minRow, maxRow, minCol, maxCol);
                if (isPositionOutOfBounds)
                {
                    outOfBoundsList.add(position);
                }
            }

            possibleTilePositions.removeAll(outOfBoundsList);



            // Find all valid domino positions from tile positions.
            //
            for (final Position terrainTilePosition : possibleTilePositions)
            {
                final ArrayList<Position> adjacentPositions = GameUtils.getAdjacentPositions(terrainTilePosition);

                for (final Position adjacentPosition : adjacentPositions)
                {
                    final boolean isOccupied = occupiedPositions.contains(adjacentPosition);
                    final boolean isOutOfBounds = isOutOfBounds(adjacentPosition, minRow, maxRow, minCol, maxCol);

                    final boolean isValidPosition = !(isOccupied || isOutOfBounds);
                    if (isValidPosition)
                    {
                        final DominoPosition dominoPosition = terrain.equals(terrain1)
                                ? new DominoPosition(terrainTilePosition, adjacentPosition)
                                : new DominoPosition(adjacentPosition, terrainTilePosition);

                        validDominoPositions.add(dominoPosition);
                    }
                }
            }
        }

        return validDominoPositions;
    }


    private static boolean isOutOfBounds(final Position position, final int minRow, final int maxRow, final int minCol, final int maxCol)
    {
        final int row = position.getRow();
        final int col = position.getColumn();

        return row < maxRow - 4 || row > minRow + 4 || col < maxCol - 4 || col > minCol + 4;
    }
}
