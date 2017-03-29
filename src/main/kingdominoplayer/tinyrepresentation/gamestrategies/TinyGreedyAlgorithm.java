package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.datastructures.TinyKingdomMovePair;
import kingdominoplayer.tinyrepresentation.datastructures.TinyMove;
import kingdominoplayer.tinyrepresentation.TinyUtils;
import kingdominoplayer.utils.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-18<br>
 * Time: 22:12<br><br>
 */
public abstract class TinyGreedyAlgorithm
{

    public byte[] getMaxScoringMoves(final String playerName, final byte[] availableMoves, final TinyGameState gameState)
    {
        final byte[] kingdomTerrains = gameState.getPlayerKingdomTerrains(playerName);
        final byte[] kingdomCrowns = gameState.getPlayerKingdomCrowns(playerName);
        final Set<Byte> placedIndices = TinyUtils.getPlacedIndices(kingdomTerrains);

        // TODO [gedda] IMPORTANT! : Select dominoes that have double match, i.e. which can be placed so both tiles match adjacent terrains.

        final ArrayList<TinyKingdomMovePair> kingdomMovePairs = getTinyKingdomMovePairs(kingdomTerrains, kingdomCrowns, availableMoves);


        final Set<TinyMove> highestScoreMoves = new LinkedHashSet<>();

        if (gameState.isPreviousDraftEmpty())
        {
            // No domino to place. Only select from current draft.

            // See if we have selected one before (possible in 2-player game).
            //
            if (! gameState.isCurrentDraftEmpty())
            {
                // Domino has been selected. Try to select next domino to match.
                //

                // TODO [gedda] IMPORTANT! : CHANGE THIS !!!!!!!!!!!!
                highestScoreMoves.addAll(getMovesWithMostCrownsOnSingleTile(availableMoves));
            }
            else
            {
                // No domino has been selected. So select your first domino. Maximize crowns?
                //
                highestScoreMoves.addAll(getMovesWithMostCrownsOnSingleTile(availableMoves));
            }
        }
        else
        {
            // We have a domino to place.
            //

            // See if we have already placed some dominoes.
            //
            if (! placedIndices.isEmpty())
            {
                // We have dominoes placed in our kingdom.
                //
                final ArrayList<TinyKingdomMovePair> maxScoringKingdomMovePairs = getMaxScoringMovePairs(kingdomMovePairs);

                for (final TinyKingdomMovePair kingdomMovePair : maxScoringKingdomMovePairs)
                {
                    highestScoreMoves.add(kingdomMovePair.getMove());
                }
            }
            else
            {
                // No domino has been placed. Simply place the selected domino.
                // // TODO [gedda] IMPORTANT! : CHANGE THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //
                highestScoreMoves.addAll(getMovesWithMostCrownsOnSingleTile(availableMoves));
            }
        }


        final byte[] movesToEvaluate = new byte[highestScoreMoves.size() * TinyConst.MOVE_ELEMENT_SIZE];
        int counter = 0;
        for (final TinyMove move : highestScoreMoves)
        {
            System.arraycopy(move.getArray(), 0, movesToEvaluate, counter * TinyConst.MOVE_ELEMENT_SIZE, TinyConst.MOVE_ELEMENT_SIZE);
            counter++;
        }

        return movesToEvaluate;
    }


    private ArrayList<TinyKingdomMovePair> getTinyKingdomMovePairs(final byte[] kingdomTerrains,
                                                                   final byte[] kingdomCrowns,
                                                                   final byte[] availableMoves)
    {
        final ArrayList<TinyKingdomMovePair> kingdomMovePairs = new ArrayList<>(availableMoves.length);

        final int numAvailableMoves = availableMoves.length / TinyConst.MOVE_ELEMENT_SIZE;
        for (int i = 0; i < numAvailableMoves; ++i)
        {
            final TinyMove move = new TinyMove(TinyGameState.getRow(availableMoves, i, TinyConst.MOVE_ELEMENT_SIZE));
            kingdomMovePairs.add(new TinyKingdomMovePair(kingdomTerrains, kingdomCrowns, move));
        }
        return kingdomMovePairs;
    }



    private ArrayList<TinyKingdomMovePair> getMaxScoringMovePairs(final ArrayList<TinyKingdomMovePair> kingdomMovePairs)
    {
        // Look ahead one move and see what kingdoms we can produce.
        //
        final ArrayList<TinyKingdomMovePair> kingdomMovePairsWithPlacedDominoPlaced = getKingdomMovePairsWithPlacedDominoPlaced(kingdomMovePairs);

        final ArrayList<TinyKingdomMovePair> kingdomMovePairsForChosenPlacement =
                kingdomMovePairsWithPlacedDominoPlaced.isEmpty()
                        ? kingdomMovePairs
                        : kingdomMovePairsWithPlacedDominoPlaced;

        final ArrayList<TinyKingdomMovePair> kingdomMovePairsWithPlacedAndChosenDominoPlaced = getKingdomMovePairsWithChosenDominoPlaced(kingdomMovePairsForChosenPlacement);

        final ArrayList<TinyKingdomMovePair> kingdomMovePairsToEvaluate =
                kingdomMovePairsWithPlacedAndChosenDominoPlaced.isEmpty()
                        ? kingdomMovePairs
                        : kingdomMovePairsWithPlacedAndChosenDominoPlaced;

        final ArrayList<TinyKingdomMovePair> kingdomMovePairsRefined = excludeDestructiveMoves(kingdomMovePairsToEvaluate);


        // See which moves have the maximum scores.
        //
        final ArrayList<TinyKingdomMovePair> maxScoringKingdomMovePairs = getMaxScoringKingdomMovePairs(kingdomMovePairsRefined);


        if (maxScoringKingdomMovePairs.isEmpty())
        {
            // TODO [gedda] IMPORTANT! : FIX THIS!!!!!
            return kingdomMovePairsToEvaluate;
        }
        else if (maxScoringKingdomMovePairs.size() > 1)
        {
            // TODO [gedda] IMPORTANT! : Select move where the chosen domino placement has best neighbour match or is completely surrounded!!!
            return maxScoringKingdomMovePairs;
        }

        return maxScoringKingdomMovePairs;
    }



    private ArrayList<TinyKingdomMovePair> excludeDestructiveMoves(final ArrayList<TinyKingdomMovePair> kingdomMovePairs)
    {
        // Remove all kingdoms that break the Middle Kingdom rule.
        //
        ArrayList<TinyKingdomMovePair> validKingdomMovePairs = removeKingdomMovePairsBreakingMiddleKingdomRule(kingdomMovePairs);

        if (validKingdomMovePairs.isEmpty())
        {
            // TODO [gedda] IMPORTANT! : FIX THIS!!!
            validKingdomMovePairs = kingdomMovePairs;
        }


        // Remove all kingdoms that leave single-tile holes.
        //
        ArrayList<TinyKingdomMovePair> kingdomMovePairsWithoutSingleTileHoles = removeKingdomMovePairsWithSingleTileHoles(validKingdomMovePairs);

        if (kingdomMovePairsWithoutSingleTileHoles.isEmpty())
        {
            // TODO [gedda] IMPORTANT! : FIX THIS!!!
            kingdomMovePairsWithoutSingleTileHoles = validKingdomMovePairs;
        }

        return kingdomMovePairsWithoutSingleTileHoles;
    }



    private ArrayList<TinyMove> getMovesWithMostCrownsOnSingleTile(final byte[] moves)
    {
        // Pick moves with max crowns.
        //
        final ArrayList<TinyMove> maxCrownsMoves = new ArrayList<>(moves.length);

        int maxCrowns = 0;
        final int numMoves = moves.length / TinyConst.MOVE_ELEMENT_SIZE;
        for (int i = 0; i < numMoves; ++i)
        {
            final int moveElementIndex = i * TinyConst.MOVE_ELEMENT_SIZE;
            final boolean chosenDominoValid = moves[moveElementIndex + TinyConst.MOVE_CHOSEN_DOMINO_INDEX] != TinyConst.INVALID_DOMINO_VALUE;

            if (chosenDominoValid)
            {
                final byte tile1Crowns = moves[moveElementIndex + TinyConst.MOVE_CHOSEN_DOMINO_INDEX + TinyConst.DOMINO_TILE_1_CROWNS_INDEX];
                final byte tile2Crowns = moves[moveElementIndex + TinyConst.MOVE_CHOSEN_DOMINO_INDEX + TinyConst.DOMINO_TILE_2_CROWNS_INDEX];

                final int max = Math.max(tile1Crowns, tile2Crowns);

                if (max == maxCrowns)
                {
                    final byte[] move = TinyGameState.getRow(moves, i, TinyConst.MOVE_ELEMENT_SIZE);
                    maxCrownsMoves.add(new TinyMove(move));
                }
                else if (max > maxCrowns)
                {
                    final byte[] move = TinyGameState.getRow(moves, i, TinyConst.MOVE_ELEMENT_SIZE);
                    maxCrownsMoves.clear();
                    maxCrownsMoves.add(new TinyMove(move));

                    maxCrowns = max;
                }
            }
        }

        return maxCrownsMoves;
    }


    private ArrayList<TinyKingdomMovePair> getKingdomMovePairsWithPlacedDominoPlaced(final ArrayList<TinyKingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<TinyKingdomMovePair> kingdomsWithPlacedDominoPlaced = new ArrayList<>(100);

        for (final TinyKingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            if (kingdomMovePair.getMove().hasPlacedDomino()) // placed domino is not always available
            {
                kingdomsWithPlacedDominoPlaced.add(kingdomMovePair.withPlacedDominoPlaced());
            }
        }

        return kingdomsWithPlacedDominoPlaced;
    }


    protected abstract ArrayList<TinyKingdomMovePair> getKingdomMovePairsWithChosenDominoPlaced(final ArrayList<TinyKingdomMovePair> kingdomMovePairs);


    private ArrayList<TinyKingdomMovePair> getMaxScoringKingdomMovePairs(final ArrayList<TinyKingdomMovePair> kingdomMovePairs)
    {
        if (kingdomMovePairs.isEmpty())
        {
            return new ArrayList<>();
        }

        kingdomMovePairs.sort((TinyKingdomMovePair kingdomMovePair1, TinyKingdomMovePair kingdomMovePair2) ->
        {
            final int score1 = kingdomMovePair1.getScore();
            final int score2 = kingdomMovePair2.getScore();
            return score1 > score2 ? -1 : score1 == score2 ? 0 : 1;
        });


        int maxScore = kingdomMovePairs.get(0).getScore();

        final ArrayList<TinyKingdomMovePair> maxScoringKingdomMovePairs = new ArrayList<>(kingdomMovePairs.size());
        for (final TinyKingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            if (kingdomMovePair.getScore() != maxScore)
            {
                break;
            }
            maxScoringKingdomMovePairs.add(kingdomMovePair);
        }

        return maxScoringKingdomMovePairs;
    }


    private ArrayList<TinyKingdomMovePair> removeKingdomMovePairsBreakingMiddleKingdomRule(final ArrayList<TinyKingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<TinyKingdomMovePair> kingdomMovePairsFulfillingRule = new ArrayList<>(kingdomMovePairs.size());

        for (final TinyKingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            boolean kingdomFulfilsRule = true;
            for (final byte placedIndex : kingdomMovePair.getPlacedIndices())
            {
                final int tileX = TinyGameState.indexToTileXCoordinate(placedIndex);
                final int tileY = TinyGameState.indexToTileYCoordinate(placedIndex);

                final boolean isWithinBounds = Math.abs(tileX) < 3 && Math.abs(tileY) < 3;

                if (! isWithinBounds)
                {
                    kingdomFulfilsRule = false;
                    break;
                }
            }

            if (kingdomFulfilsRule)
            {
                kingdomMovePairsFulfillingRule.add(kingdomMovePair);
            }

        }

        return kingdomMovePairsFulfillingRule;
    }


    public ArrayList<TinyKingdomMovePair> removeKingdomMovePairsWithSingleTileHoles(final ArrayList<TinyKingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<TinyKingdomMovePair> validPairs = new ArrayList<>(kingdomMovePairs.size());

        for (final TinyKingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            final byte[] kingdomTerrains = kingdomMovePair.getKingdomTerrains();
            final Set<Byte> placedIndices = TinyUtils.getPlacedIndices(kingdomTerrains);

            final LinkedHashSet<Byte> adjacentPositions = new LinkedHashSet<>(100);

            for (final byte placedIndex : placedIndices)
            {
                final ArrayList<Byte> adjacentIndices = TinyUtils.getAdjacentIndices(placedIndex, TinyConst.KINGDOM_X_SIZE, TinyConst.KINGDOM_Y_SIZE);
                adjacentPositions.addAll(adjacentIndices);
            }
            adjacentPositions.removeAll(placedIndices);


            final ArrayList<Byte> singleTileHolePositions = getSingleTileHoles(adjacentPositions, placedIndices);

            if (singleTileHolePositions.isEmpty())
            {
                validPairs.add(kingdomMovePair);
            }
        }

        //DebugPlot.plotWithPositionsMarked(DEBUG_kingdoms, DEBUG_positionsArray, "Single Tile Hole Positions Adjacent To Placed Domino");
        Util.noop();

        return validPairs;
    }





    private ArrayList<Byte> getSingleTileHoles(final Collection<Byte> positions,
                                               final Collection<Byte> placedIndices)
    {
        final ArrayList<Byte> singleTileHolePositions = new ArrayList<>(positions.size());

        for (final byte position : positions)
        {
            if (isSingleTileHole(position, placedIndices))
            {
                singleTileHolePositions.add(position);
            }
        }

        return singleTileHolePositions;
    }


    private boolean isSingleTileHole(final byte position, final Collection<Byte> placedIndices)
    {

        final int tileX = TinyGameState.indexToTileXCoordinate(position);
        final int tileY = TinyGameState.indexToTileYCoordinate(position);

        final boolean isPositionOutsideCastleCenteredKingdom = Math.abs(tileX) > 2 || Math.abs(tileY) > 2;
        if (isPositionOutsideCastleCenteredKingdom)
        {
            // Classifying a position outside the castle-centered kingdom will potentially
            // prevent good placements adjacent to this position.
            //
            //  For example,
            //
            //        C
            //        D
            //        D
            //        P
            //
            //  where C=castle, D=domino, P=position, will prevent the domino from being placed since
            //  P would be classed as isSingleTileHole would return true (all positions adjacent to P
            //  are outside the castle-centered kingdom except for one which is occupied by the lower
            //  D).
            //
            return false;
        }

        final ArrayList<Byte> adjacentPositions = TinyUtils.getAdjacentIndices(position, TinyConst.KINGDOM_X_SIZE, TinyConst.KINGDOM_Y_SIZE);

        final ArrayList<Boolean> adjacentOccupiedStatuses = new ArrayList<>(adjacentPositions.size());
        for (final byte adjacentPosition : adjacentPositions)
        {
            boolean isAdjacentOccupied = false;

            final int castleIndex = TinyGameState.tileCoordinateToLinearIndex(0, 0);
            final boolean isCastleTile = adjacentPosition == castleIndex;
            // TODO [gedda] IMPORTANT! : change to outside 5x5 kingdom area!!!
            final int adjacentTileX = TinyGameState.indexToTileXCoordinate(adjacentPosition);
            final int adjacentTileY = TinyGameState.indexToTileYCoordinate(adjacentPosition);
            final boolean isOutsideCastleCenteredKingdom = Math.abs(adjacentTileX) > 2 || Math.abs(adjacentTileY) > 2;
            final boolean isTilePosition = placedIndices.contains(adjacentPosition);

            if (isCastleTile || isOutsideCastleCenteredKingdom || isTilePosition)
            {
                isAdjacentOccupied = true;
            }

            adjacentOccupiedStatuses.add(isAdjacentOccupied);
        }

        boolean isSingleTileHole = true;
        for (final boolean isAdjacentOccupied : adjacentOccupiedStatuses)
        {
            isSingleTileHole &= isAdjacentOccupied;
        }

        return isSingleTileHole;
    }

}
