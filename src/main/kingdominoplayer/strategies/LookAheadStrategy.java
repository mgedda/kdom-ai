package kingdominoplayer.strategies;

import kingdominoplayer.utils.GameUtils;
import kingdominoplayer.datastructures.*;
import kingdominoplayer.planning.Planner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:18<br><br>
 */
public abstract class LookAheadStrategy
{

    public ArrayList<Move> selectMaxScoringMoves(final String playerName, final Move[] availableMoves, final LocalGameState gameState)
    {
        final Collection<PlacedTile> placedTiles = gameState.getPlacedTiles(playerName);
        final Collection<Domino> previousDraft = gameState.getPreviousDraft(playerName);
        final Collection<Domino> currentDraft = gameState.getCurrentDraft(playerName);


        // TODO [gedda] IMPORTANT! : Select dominoes that have double match, i.e. which can be placed so both tiles match adjacent terrains.

        final Kingdom kingdom = new Kingdom(placedTiles);
        final ArrayList<KingdomMovePair> kingdomMovePairs = new ArrayList<>(availableMoves.length);

        for (final Move availableMove : availableMoves)
        {
            kingdomMovePairs.add(new KingdomMovePair(kingdom, availableMove));
        }


        final ArrayList<Move> highestScoreMoves;

        if (previousDraft.isEmpty())
        {
            // No domino to place. Only select from current draft.

            // See if we have selected one before (possible in 2-player game).
            //
            if (! currentDraft.isEmpty())
            {
                // Domino has been selected. Try to select next domino to match.
                //

                // TODO [gedda] IMPORTANT! : CHANGE THIS !!!!!!!!!!!!
                highestScoreMoves = GameUtils.getMovesWithMostCrownsOnSingleTile(availableMoves);
            }
            else
            {
                // No domino has been selected. So select your first domino. Maximize crowns?
                //
                highestScoreMoves = GameUtils.getMovesWithMostCrownsOnSingleTile(availableMoves);
            }
        }
        else
        {
            // We have a domino to place.
            //

            // See if we have already placed some dominoes.
            //
            if (! placedTiles.isEmpty())
            {
                // We have dominoes placed in our kingdom.
                //
                final ArrayList<KingdomMovePair> maxScoringKingdomMovePairs = evaluateBestMovesByLookAhead(kingdomMovePairs);

                highestScoreMoves = new ArrayList<>(maxScoringKingdomMovePairs.size());
                for (final KingdomMovePair kingdomMovePair : maxScoringKingdomMovePairs)
                {
                    highestScoreMoves.add(kingdomMovePair.getMove());
                }
            }
            else
            {
                // No domino has been placed. Simply place the selected domino.
                // // TODO [gedda] IMPORTANT! : CHANGE THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //
                highestScoreMoves = GameUtils.getMovesWithMostCrownsOnSingleTile(availableMoves);
            }
        }

        return highestScoreMoves;
    }


    private ArrayList<KingdomMovePair> evaluateBestMovesByLookAhead(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        // Look ahead one move and see what kingdoms we can produce.
        //
        final ArrayList<KingdomMovePair> kingdomMovePairsWithPlacedDominoPlaced = Planner.getKingdomMovePairsWithPlacedDominoPlaced(kingdomMovePairs);

        final ArrayList<KingdomMovePair> kingdomMovePairsForChosenPlacement =
                kingdomMovePairsWithPlacedDominoPlaced.isEmpty()
                        ? kingdomMovePairs
                        : kingdomMovePairsWithPlacedDominoPlaced;

        final ArrayList<KingdomMovePair> kingdomMovePairsWithPlacedAndChosenDominoPlaced = Planner.getKingdomMovePairsWithChosenDominoPlaced(kingdomMovePairsForChosenPlacement);

        final ArrayList<KingdomMovePair> kingdomMovePairsToEvaluate =
                kingdomMovePairsWithPlacedAndChosenDominoPlaced.isEmpty()
                        ? kingdomMovePairs
                        : kingdomMovePairsWithPlacedAndChosenDominoPlaced;

        final ArrayList<KingdomMovePair> maxScoringKingdomMovePairs = getMaxScoringKingdomMovePairs(kingdomMovePairsToEvaluate);

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


    private ArrayList<KingdomMovePair> getMaxScoringKingdomMovePairs(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        // Remove all kingdoms that break the Middle Kingdom rule.
        //
        ArrayList<KingdomMovePair> validKingdomMovePairs = GameUtils.removeKingdomMovePairsBreakingMiddleKingdomRule(kingdomMovePairs);

        if (validKingdomMovePairs.isEmpty())
        {
            // TODO [gedda] IMPORTANT! : FIX THIS!!!
            validKingdomMovePairs = kingdomMovePairs;
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
        return Planner.getMaxScoringKingdomMovePairs(kingdomMovePairsWithoutSingleTileHoles);
    }

}
