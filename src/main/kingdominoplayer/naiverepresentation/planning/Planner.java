package kingdominoplayer.naiverepresentation.planning;

import kingdominoplayer.naiverepresentation.datastructures.Domino;
import kingdominoplayer.naiverepresentation.datastructures.DominoPosition;
import kingdominoplayer.naiverepresentation.datastructures.DraftElement;
import kingdominoplayer.naiverepresentation.datastructures.GameState;
import kingdominoplayer.naiverepresentation.datastructures.Kingdom;
import kingdominoplayer.naiverepresentation.datastructures.KingdomInfo;
import kingdominoplayer.naiverepresentation.datastructures.KingdomMovePair;
import kingdominoplayer.naiverepresentation.datastructures.Move;
import kingdominoplayer.naiverepresentation.datastructures.PlacedDomino;
import kingdominoplayer.naiverepresentation.datastructures.PlacedTile;
import kingdominoplayer.naiverepresentation.datastructures.Position;
import kingdominoplayer.utils.GameUtils;
import kingdominoplayer.utils.Util;

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
    public static ArrayList<KingdomMovePair> getKingdomMovePairsWithPlacedDominoPlaced(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<KingdomMovePair> kingdomsWithPlacedDominoPlaced = new ArrayList<>(100);

        for (final KingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            final PlacedDomino placedDomino = kingdomMovePair.getMove().getPlacedDomino();

            if (placedDomino != null) // placed domino is not always available
            {
                kingdomsWithPlacedDominoPlaced.add(kingdomMovePair.withPlacedDominoPlaced());
            }
        }

        return kingdomsWithPlacedDominoPlaced;
    }


    public static ArrayList<KingdomMovePair> getKingdomMovePairsWithChosenDominoPlaced(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<KingdomMovePair> kingdomMovePairsWithChosenDominoPlaced = new ArrayList<>(1000);

        for (final KingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            final Domino chosenDomino = kingdomMovePair.getMove().getChosenDomino();

            if (chosenDomino != null)
            {
                final Kingdom kingdom = kingdomMovePair.getKingdom();
                final Set<DominoPosition> dominoPositions = Planner.getValidPositions(chosenDomino, kingdom);

                for (final DominoPosition dominoPosition : dominoPositions)
                {
                    final KingdomMovePair kingdomMovePairWithChosenDominoPlaced = kingdomMovePair.withChosenDominoPlaced(dominoPosition);
                    kingdomMovePairsWithChosenDominoPlaced.add(kingdomMovePairWithChosenDominoPlaced);
                }

                //DebugPlot.plotKingdomsWithChosenDominoMarked(kingdomMovePairsWithChosenDominoPlaced, "Kingdoms with Chosen Domino Placed");
                Util.noop();
            }
        }
        return kingdomMovePairsWithChosenDominoPlaced;
    }


    public static ArrayList<KingdomMovePair> getMaxScoringKingdomMovePairs(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        if (kingdomMovePairs.isEmpty())
        {
            return new ArrayList<>();
        }

        kingdomMovePairs.sort((KingdomMovePair kingdomMovePair1, KingdomMovePair kingdomMovePair2) ->
        {
            final int score1 = kingdomMovePair1.getKingdom().getScore();
            final int score2 = kingdomMovePair2.getKingdom().getScore();
            return score1 > score2 ? -1 : score1 == score2 ? 0 : 1;
        });


        int maxScore = kingdomMovePairs.get(0).getKingdom().getScore();

        final ArrayList<KingdomMovePair> maxScoringKingdomMovePairs = new ArrayList<>(kingdomMovePairs.size());
        for (final KingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            if (kingdomMovePair.getKingdom().getScore() != maxScore)
            {
                break;
            }
            maxScoringKingdomMovePairs.add(kingdomMovePair);
        }

        return maxScoringKingdomMovePairs;
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

        // Remove any symmetrical domino positions
        //
        if (terrain1.equals(terrain2))
        {
            final LinkedHashSet<DominoPosition> result = new LinkedHashSet<>(2 * validDominoPositions.size());

            for (final DominoPosition dominoPosition : validDominoPositions)
            {
                if (! result.contains(dominoPosition.getInverted()))
                {
                    result.add(dominoPosition);
                }
            }

            validDominoPositions.clear();
            validDominoPositions.addAll(result);
        }

        return validDominoPositions;
    }


    private static boolean isOutOfBounds(final Position position, final int minRow, final int maxRow, final int minCol, final int maxCol)
    {
        final int row = position.getRow();
        final int col = position.getColumn();

        return row < maxRow - 4 || row > minRow + 4 || col < maxCol - 4 || col > minCol + 4;
    }


    public static GameState makeMove(final Move move, final GameState gameState, final String playerName)
    {
        final ArrayList<KingdomInfo> kingdomInfos = gameState.getKingdomInfos();
        final ArrayList<DraftElement> previousDraft = gameState.getPreviousDraft();

        final ArrayList<DraftElement> updatedPreviousDraft = new ArrayList<>(previousDraft.size());
        final ArrayList<KingdomInfo> updatedKingdomInfos = new ArrayList<>(kingdomInfos.size());

        final PlacedDomino placedDomino = move.getPlacedDomino();
        if (placedDomino != null)
        {
            // remove from previous draft and add to player's placed tiles.
            //
            for (final DraftElement draftElement : previousDraft)
            {
                if (! draftElement.getDomino().equals(placedDomino))
                {
                    updatedPreviousDraft.add(draftElement);
                }
            }

            for (final KingdomInfo kingdomInfo : kingdomInfos)
            {
                if (kingdomInfo.getPlayerName().equals(playerName))
                {
                    final ArrayList<PlacedTile> placedTiles = new ArrayList<>(kingdomInfo.getKingdom().getPlacedTiles().size() + 1);
                    placedTiles.addAll(kingdomInfo.getKingdom().getPlacedTiles());
                    placedTiles.addAll(placedDomino.getPlacedTiles());

                    final Kingdom updatedKingdom = new Kingdom(placedTiles);
                    updatedKingdomInfos.add(new KingdomInfo(updatedKingdom, playerName));
                }
                else
                {
                    updatedKingdomInfos.add(kingdomInfo);
                }
            }
        }
        else
        {
            updatedKingdomInfos.addAll(kingdomInfos);
        }


        final ArrayList<DraftElement> currentDraft = gameState.getCurrentDraft();

        final ArrayList<DraftElement> updatedCurrentDraft = new ArrayList<>(currentDraft.size());

        final Domino chosenDomino = move.getChosenDomino();
        if (chosenDomino != null)
        {
            for (final DraftElement draftElement : currentDraft)
            {
                if (draftElement.getDomino().equals(chosenDomino))
                {
                    updatedCurrentDraft.add(new DraftElement(chosenDomino, playerName));
                }
                else
                {
                    updatedCurrentDraft.add(draftElement);
                }
            }
        }


        return new GameState(updatedKingdomInfos, updatedPreviousDraft, updatedCurrentDraft, null);
    }
}
