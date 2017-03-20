package kingdominoplayer.naiverepresentation.strategies;

import kingdominoplayer.naiverepresentation.datastructures.Domino;
import kingdominoplayer.naiverepresentation.datastructures.Kingdom;
import kingdominoplayer.naiverepresentation.datastructures.KingdomMovePair;
import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.naiverepresentation.datastructures.Move;
import kingdominoplayer.naiverepresentation.datastructures.PlacedTile;
import kingdominoplayer.utils.GameUtils;
import kingdominoplayer.naiveplanning.Planner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:18<br><br>
 */
public abstract class GreedyAlgorithm
{

    public ArrayList<Move> getMaxScoringMoves(final String playerName, final Move[] availableMoves, final LocalGameState gameState)
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


        final Set<Move> highestScoreMoves = new LinkedHashSet<>();

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
                highestScoreMoves.addAll(GameUtils.getMovesWithMostCrownsOnSingleTile(availableMoves));
            }
            else
            {
                // No domino has been selected. So select your first domino. Maximize crowns?
                //
                highestScoreMoves.addAll(GameUtils.getMovesWithMostCrownsOnSingleTile(availableMoves));
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
                final ArrayList<KingdomMovePair> maxScoringKingdomMovePairs = getMaxScoringMovePairs(kingdomMovePairs);

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
                highestScoreMoves.addAll(GameUtils.getMovesWithMostCrownsOnSingleTile(availableMoves));
            }
        }

        final ArrayList<Move> movesToEvaluate = new ArrayList<>(highestScoreMoves.size());
        movesToEvaluate.addAll(highestScoreMoves);

        return movesToEvaluate;
    }


    private ArrayList<KingdomMovePair> getMaxScoringMovePairs(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        // Look ahead one move and see what kingdoms we can produce.
        //
        final ArrayList<KingdomMovePair> kingdomMovePairsWithPlacedDominoPlaced = Planner.getKingdomMovePairsWithPlacedDominoPlaced(kingdomMovePairs);

        final ArrayList<KingdomMovePair> kingdomMovePairsForChosenPlacement =
                kingdomMovePairsWithPlacedDominoPlaced.isEmpty()
                        ? kingdomMovePairs
                        : kingdomMovePairsWithPlacedDominoPlaced;

        final ArrayList<KingdomMovePair> kingdomMovePairsWithPlacedAndChosenDominoPlaced = getKingdomMovePairsWithChosenDominoPlaced(kingdomMovePairsForChosenPlacement);

        final ArrayList<KingdomMovePair> kingdomMovePairsToEvaluate =
                kingdomMovePairsWithPlacedAndChosenDominoPlaced.isEmpty()
                        ? kingdomMovePairs
                        : kingdomMovePairsWithPlacedAndChosenDominoPlaced;

        final ArrayList<KingdomMovePair> kingdomMovePairsRefined = excludeDestructiveMoves(kingdomMovePairsToEvaluate);


        // See which moves have the maximum scores.
        //
        final ArrayList<KingdomMovePair> maxScoringKingdomMovePairs = Planner.getMaxScoringKingdomMovePairs(kingdomMovePairsRefined);


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


    protected abstract ArrayList<KingdomMovePair> getKingdomMovePairsWithChosenDominoPlaced(final ArrayList<KingdomMovePair> kingdomMovePairsForChosenPlacement);


    private ArrayList<KingdomMovePair> excludeDestructiveMoves(final ArrayList<KingdomMovePair> kingdomMovePairs)
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

        return kingdomMovePairsWithoutSingleTileHoles;
    }

}
