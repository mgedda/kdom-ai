package kingdominoplayer.strategies;

import kingdominoplayer.utils.GameUtils;
import kingdominoplayer.datastructures.*;
import kingdominoplayer.planning.Planner;
import kingdominoplayer.utils.Util;

import java.util.ArrayList;

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
        final ArrayList<KingdomMovePair> kingdomMovePairs = new ArrayList<>(availableMoves.length);

        for (final Move availableMove : availableMoves)
        {
            kingdomMovePairs.add(new KingdomMovePair(kingdom, availableMove));
        }


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
                final Move maxScoringKingdomMovePair = evaluateBestMoveFirstPlacedThenChosen(kingdomMovePairs);
                //final Move maxScoringKingdomMovePair = evaluateBestMovePlacedAndChosenSimultaneously(availableMoves, kingdom);

                return maxScoringKingdomMovePair;

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


    private Move evaluateBestMoveFirstPlacedThenChosen(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        // We have dominoes placed in our kingdom. // TODO [gedda] IMPORTANT! : Evaluate placed + chosen together, not first placed then chosen!
        //
        final ArrayList<KingdomMovePair> maxScoringKingdomMovePairsWithPlacedDominoPlaced = getMaxScoringKingdomMovePairsWithPlacedDominoPlaced(kingdomMovePairs);

        //DebugPlot.plotKingdomsWithPlacedDominoMarked(maxScoringKingdomMovePairsWithPlacedDominoPlaced, "Max Scoring Moves With Placed Marked");
        Util.noop();


        // Now we have chosen which domino(es) to place. The next step is to chose a good domino from the
        // current draft.
        //
        final ArrayList<KingdomMovePair> maxScoringMovesWithBothPlacedAndChosenDominoPlaced = selectMoveWithBestChosenDomino(maxScoringKingdomMovePairsWithPlacedDominoPlaced);

        //DebugPlot.plotKingdomsWithChosenDominoMarked(maxScoringMovesWithBothPlacedAndChosenDominoPlaced, "Max Scoring Moves With Chosen Marked");
        Util.noop();

        if (maxScoringMovesWithBothPlacedAndChosenDominoPlaced.isEmpty())
        {
            // TODO [gedda] IMPORTANT! : FIX THIS!!!!!
            return maxScoringKingdomMovePairsWithPlacedDominoPlaced.get(0).getMove();
        }

        if (maxScoringMovesWithBothPlacedAndChosenDominoPlaced.size() > 1)
        {
            // TODO [gedda] IMPORTANT! : Select move where the chosen domino placement has best neighbour match or is completely surrounded!!!

        }

        return maxScoringMovesWithBothPlacedAndChosenDominoPlaced.get(0).getMove();
    }


    private ArrayList<KingdomMovePair> getMaxScoringKingdomMovePairsWithPlacedDominoPlaced(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        // Look ahead one move and see what kingdoms we can produce.
        //
        final ArrayList<KingdomMovePair> kingdomMovePairsWithPlacedDominoPlaced = Planner.getKingdomMovePairsWithPlacedDominoPlaced(kingdomMovePairs);

        //DebugPlot.plotKingdomsWithPlacedDominoMarked(kingdomMovePairsWithPlacedDominoPlaced, "Kingdoms With Placed Domino");
        if (kingdomMovePairsWithPlacedDominoPlaced.isEmpty())
        {
            // TODO [gedda] IMPORTANT! : is this really what we want to do?
            return kingdomMovePairs;
        }
        else
        {
            // Remove all kingdoms that break the Middle Kingdom rule.
            //
            ArrayList<KingdomMovePair> validKingdomMovePairs = GameUtils.removeMovesBreakingMiddleKingdomRule(kingdomMovePairsWithPlacedDominoPlaced);

            if (validKingdomMovePairs.isEmpty())
            {
                // TODO [gedda] IMPORTANT! : FIX THIS!!!
                validKingdomMovePairs = kingdomMovePairsWithPlacedDominoPlaced;
            }


            // Remove all kingdoms that leave single-tile holes.
            //
            ArrayList<KingdomMovePair> kingdomMovePairsWithoutSingleTileHoles = GameUtils.removeKingdomMovePairsWithSingleTileHoles(validKingdomMovePairs);

            if (kingdomMovePairsWithoutSingleTileHoles.isEmpty())
            {
                // TODO [gedda] IMPORTANT! : FIX THIS!!!
                kingdomMovePairsWithoutSingleTileHoles = validKingdomMovePairs;
            }


            // See which moves have the maximum scores.
            //
            final ArrayList<KingdomMovePair> maxScoringKingdomMovePairs = Planner.getMaxScoringKingdomMovePairs(kingdomMovePairsWithoutSingleTileHoles);

            return maxScoringKingdomMovePairs;
        }
    }


    private ArrayList<KingdomMovePair> selectMoveWithBestChosenDomino(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<KingdomMovePair> kingdomMovePairsWithChosenDominoPlaced = Planner.getKingdomMovePairsWithChosenDominoPlaced(kingdomMovePairs);
        final ArrayList<KingdomMovePair> maxScoringMovesWithChosenDominoPlaced = Planner.getMaxScoringKingdomMovePairs(kingdomMovePairsWithChosenDominoPlaced);

        return maxScoringMovesWithChosenDominoPlaced;
    }

}
