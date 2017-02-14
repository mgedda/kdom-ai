package kingdominoplayer.strategies;

import kingdominoplayer.plot.DebugPlot;
import kingdominoplayer.utils.GameUtils;
import kingdominoplayer.datastructures.*;
import kingdominoplayer.planning.Planner;
import kingdominoplayer.utils.Util;

import java.util.ArrayList;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:18<br><br>
 */
public class LookAheadStrategy implements Strategy
{
    @Override
    public Move selectMove(final Move[] availableMoves, final Domino[] previousDraft, final Domino[] currentDraft, final PlacedTile[] placedTiles)
    {
        // TODO [gedda] IMPORTANT! : Select dominoes that have double match, i.e. which can be placed so both tiles match adjacent terrains.
        // TODO [gedda] IMPORTANT! : Make good choices for chosen domino! Currently it does not take chosen dominoes into account!


        final Kingdom kingdom = new Kingdom(placedTiles);

        if (previousDraft.length == 0)
        {
            // No domino to place. Only select from current draft.

            // See if we have selected one before (possible in 2-player game).
            //
            if (currentDraft.length > 0)
            {
                // Domino has been selected. Try to select next domino to match.
                //

                // TODO [gedda] IMPORTANT! : CHANGE THIS !!!!!!!!!!!!
                final ArrayList<Move> movesWithMostCrowns = GameUtils.getMovesWithMostCrownsOnSingleTile(availableMoves);

                return movesWithMostCrowns.get(0);
            }
            else
            {
                // No domino has been selected. So select your first domino. Maximize crowns?
                //
                final ArrayList<Move> movesWithMostCrowns = GameUtils.getMovesWithMostCrownsOnSingleTile(availableMoves);

                return movesWithMostCrowns.get(0);
            }
        }
        else
        {
            // We have a domino to place.
            //

            // See if we have already placed some dominoes.
            //
            if (placedTiles.length > 0)
            {
                // We have dominoes placed in our kingdom.
                //
                final ArrayList<KingdomMovePair> maxScoringMoves = selectMovesWithBestPlacedDomino(availableMoves, kingdom);

                Util.noop();


                // Now we have chosen which domino(es) to place. The next step is to chose a good domino from the
                // current draft.
                //
                final ArrayList<KingdomMovePair> maxScoringMovesWithChosenDominoPlaced = selectMoveWithBestChosenDomino(maxScoringMoves);

                if (maxScoringMovesWithChosenDominoPlaced.isEmpty())
                {
                    // TODO [gedda] IMPORTANT! : FIX THIS!!!!!
                    return maxScoringMoves.get(0).getMove();
                }

                if (maxScoringMovesWithChosenDominoPlaced.size() > 1)
                {
                    // TODO [gedda] IMPORTANT! : Select move where the chosen domino placement has best neighbour match!!!

                }

                return maxScoringMovesWithChosenDominoPlaced.get(0).getMove();
            }
            else
            {
                // No domino has been placed. Simply place the selected domino.
                // // TODO [gedda] IMPORTANT! : CHANGE THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //
                final ArrayList<Move> movesWithMostCrowns = GameUtils.getMovesWithMostCrownsOnSingleTile(availableMoves);

                return movesWithMostCrowns.get(0);
            }
        }
    }


    private ArrayList<KingdomMovePair> selectMovesWithBestPlacedDomino(final Move[] availableMoves, final Kingdom kingdom)
    {
        // Look ahead one move and see what kingdoms we can produce.
        //
        final ArrayList<KingdomMovePair> possibleNewKingdoms = Planner.getPossibleNewKingdoms(kingdom, availableMoves);

        //DebugPlot.plotKingdomsWithPlacedDominoMarked(possibleNewKingdoms, "Kingdoms With Placed Domino");
        Util.noop();

        // Remove all kingdoms that break the Middle Kingdom rule.
        //
        ArrayList<KingdomMovePair> validKingdomMovePairs = GameUtils.removeMovesBreakingMiddleKingdomRule(possibleNewKingdoms);

        if (validKingdomMovePairs.isEmpty())
        {
            // TODO [gedda] IMPORTANT! : FIX THIS!!!
            validKingdomMovePairs = possibleNewKingdoms;
        }


        // Remove all kingdoms that leave single-tile holes.
        //
        ArrayList<KingdomMovePair> nonHoleMakingKingdomMovePairs = GameUtils.removeMovesCreatingSingleTileHoles(validKingdomMovePairs);

        if (nonHoleMakingKingdomMovePairs.isEmpty())
        {
            // TODO [gedda] IMPORTANT! : FIX THIS!!!
            nonHoleMakingKingdomMovePairs = validKingdomMovePairs;
        }


        // See which moves have the maximum scores.
        //
        final ArrayList<KingdomMovePair> maxScoringKingdomMovePairs = getMaxScoringKingdomMovePairs(nonHoleMakingKingdomMovePairs);

        return maxScoringKingdomMovePairs;
    }

    private ArrayList<KingdomMovePair> getMaxScoringKingdomMovePairs(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
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


    private ArrayList<KingdomMovePair> selectMoveWithBestChosenDomino(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<KingdomMovePair> kingdomWithChosenDominoPlacedMovePairs = placeChosenDominoes(kingdomMovePairs);

        final ArrayList<KingdomMovePair> maxScoringMovesWithChosenDominoPlaced = getMaxScoringKingdomMovePairs(kingdomWithChosenDominoPlacedMovePairs);

        return maxScoringMovesWithChosenDominoPlaced;
    }


    private ArrayList<KingdomMovePair> placeChosenDominoes(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<KingdomMovePair> kingdomsWithChosenDominoPlacedMovePair = new ArrayList<>(1000);

        for (final KingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            final Move move = kingdomMovePair.getMove();
            final Domino chosenDomino = move.getChosenDomino();

            if (chosenDomino != null)
            {
                final Kingdom kingdom = kingdomMovePair.getKingdom();

                final Set<DominoPosition> dominoPositions = Planner.getValidPositions(chosenDomino, kingdom);

                final ArrayList<KingdomDominoPositionPair> DEBUG_kingdomWithPlacedChosenDominoPositons = new ArrayList<>(1000);

                for (final DominoPosition dominoPosition : dominoPositions)
                {
                    final PlacedDomino chosenDominoPlaced = new PlacedDomino(chosenDomino, dominoPosition);
                    final Kingdom kingdomWithChosenDominoPlaced = GameUtils.getKingdomWithDominoPlaced(kingdom, chosenDominoPlaced);
                    final KingdomMovePair kingdomWithChosenDominoPlacedMovePair = new KingdomMovePair(kingdomWithChosenDominoPlaced, move);

                    kingdomsWithChosenDominoPlacedMovePair.add(kingdomWithChosenDominoPlacedMovePair);

                    DEBUG_kingdomWithPlacedChosenDominoPositons.add(new KingdomDominoPositionPair(kingdomWithChosenDominoPlaced, dominoPosition));
                }

                //DebugPlot.plotKingdomsWithDominoPositionMarked(DEBUG_kingdomWithPlacedChosenDominoPositons, "Kingdoms with Chosen Domino Placed");
                Util.noop();
            }
        }
        return kingdomsWithChosenDominoPlacedMovePair;
    }


}
