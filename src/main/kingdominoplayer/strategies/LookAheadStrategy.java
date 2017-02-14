package kingdominoplayer.strategies;

import kingdominoplayer.utils.GameUtils;
import kingdominoplayer.datastructures.*;
import kingdominoplayer.planning.Planner;
import kingdominoplayer.planning.Scorer;
import kingdominoplayer.utils.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
                final ArrayList<KingdomMovePair> movesWithBestChosenDomino = selectMoveWithBestChosenDomino(maxScoringMoves);

                if (movesWithBestChosenDomino.size() > 1)
                {
                    // TODO [gedda] IMPORTANT! : Select move where the chosen domino placement has best neighbour match!!!

                }

                return movesWithBestChosenDomino.get(0).getMove();
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
        //Look ahead one move and see what kingdoms we can produce.
        //
        final ArrayList<KingdomMovePair> possibleNewKingdoms = Planner.getPossibleNewKingdoms(kingdom, availableMoves);

        // Compute scores for all possible kingdoms.
        //
        final LinkedHashMap<KingdomMovePair, Integer> kingdomMovePairToScoreMap = GameUtils.getKingdomScores(possibleNewKingdoms);

        // Remove all kingdoms that break the Middle Kingdom rule.
        //
        LinkedHashMap<KingdomMovePair, Integer> validKingdomMovePairToScoreMap = getOnlyValidKingdomMovePairs(kingdomMovePairToScoreMap);

        if (validKingdomMovePairToScoreMap.isEmpty())
        {
            // TODO [gedda] IMPORTANT! : FIX THIS!!!
            validKingdomMovePairToScoreMap = kingdomMovePairToScoreMap;
        }


        // Remove all kingdoms that leave single-tile holes.
        //
        LinkedHashMap<KingdomMovePair, Integer> nonHoleMakingKingdomMovePairToScoreMap = getOnlyKingdomMovePairsNotMakingHoles(validKingdomMovePairToScoreMap);

        if (nonHoleMakingKingdomMovePairToScoreMap.isEmpty())
        {
            // TODO [gedda] IMPORTANT! : FIX THIS!!!
            nonHoleMakingKingdomMovePairToScoreMap = kingdomMovePairToScoreMap;
        }


        // See which moves have the maximum scores.
        //
        int maxScore = 0;
        for (final Integer score : nonHoleMakingKingdomMovePairToScoreMap.values())
        {
            maxScore = score > maxScore ? score : maxScore;
        }

        final ArrayList<KingdomMovePair> maxScoringMoves = new ArrayList<>(availableMoves.length);
        for (final KingdomMovePair kingdomMovePair : possibleNewKingdoms)
        {
            if (kingdomMovePairToScoreMap.get(kingdomMovePair) == maxScore)
            {
                maxScoringMoves.add(kingdomMovePair);
            }
        }
        return maxScoringMoves;
    }


    final private ArrayList<KingdomMovePair> getMaxScoringMoves(final LinkedHashMap<KingdomMovePair, Integer> kingdomMovePairScoreMap)
    {
        // See which moves have the maximum scores.
        //
        int maxScore = 0;
        for (final Integer score : kingdomMovePairScoreMap.values())
        {
            maxScore = score > maxScore ? score : maxScore;
        }

        final ArrayList<KingdomMovePair> maxScoringMoves = new ArrayList<>(kingdomMovePairScoreMap.size());
        for (final KingdomMovePair kingdomMovePair : kingdomMovePairScoreMap.keySet())
        {
            if (kingdomMovePairScoreMap.get(kingdomMovePair) == maxScore)
            {
                maxScoringMoves.add(kingdomMovePair);
            }
        }

        return maxScoringMoves;
    }


    private ArrayList<KingdomMovePair> selectMoveWithBestChosenDomino(final ArrayList<KingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<KingdomMovePair> kingdomWithChosenDominoPlacedMovePairs = placeChosenDominoes(kingdomMovePairs);

        final LinkedHashMap<KingdomMovePair, Integer> kingdomWithChosenDominoPlacedMovePairToScoreMap = getKingdomScores(kingdomWithChosenDominoPlacedMovePairs);

        final ArrayList<KingdomMovePair> maxScoringMovesWithChosenDominoPlaced = getMaxScoringMoves(kingdomWithChosenDominoPlacedMovePairToScoreMap);

        if (maxScoringMovesWithChosenDominoPlaced.isEmpty())
        {
            return kingdomMovePairs;
        }

        return maxScoringMovesWithChosenDominoPlaced;
    }


    private ArrayList<KingdomMovePair> placeChosenDominoes(ArrayList<KingdomMovePair> kingdomMovePairs)
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

                for (final DominoPosition dominoPosition : dominoPositions)
                {
                    final PlacedDomino chosenDominoPlaced = new PlacedDomino(chosenDomino, dominoPosition);
                    final Kingdom kingdomWithChosenDominoPlaced = GameUtils.getKingdomWithDominoPlaced(kingdom, chosenDominoPlaced);
                    final KingdomMovePair kingdomWithChosenDominoPlacedMovePair = new KingdomMovePair(kingdomWithChosenDominoPlaced, move);
                    kingdomsWithChosenDominoPlacedMovePair.add(kingdomWithChosenDominoPlacedMovePair);
                }
            }
        }
        return kingdomsWithChosenDominoPlacedMovePair;
    }


    private LinkedHashMap<KingdomMovePair, Integer> getKingdomScores(ArrayList<KingdomMovePair> kingdomsWithChosenDominoPlacedMovePair)
    {
        final LinkedHashMap<KingdomMovePair, Integer> kingdomMovePairToScoreMap = new LinkedHashMap<>(1000);

        for (final KingdomMovePair kingdomMovePair : kingdomsWithChosenDominoPlacedMovePair)
        {
            final int score = Scorer.computeScore(kingdomMovePair.getKingdom().getPlacedTiles());
            kingdomMovePairToScoreMap.put(kingdomMovePair, score);
        }
        return kingdomMovePairToScoreMap;
    }


    @SuppressWarnings("UnnecessaryLocalVariable")
    private LinkedHashMap<KingdomMovePair, Integer> getOnlyKingdomMovePairsNotMakingHoles(final LinkedHashMap<KingdomMovePair, Integer> kingdomMovePairToScoreMap)
    {
        final ArrayList<KingdomMovePair> kingdomMovePairs = new ArrayList<>(kingdomMovePairToScoreMap.size());
        kingdomMovePairs.addAll(kingdomMovePairToScoreMap.keySet());
        final ArrayList<KingdomMovePair> validKingdomMovePairs = GameUtils.removeMovesCreatingSingleTileHoles(kingdomMovePairs);

        final LinkedHashMap<KingdomMovePair, Integer> validKingdomMovePairToScoreMap = keep(validKingdomMovePairs, kingdomMovePairToScoreMap);

        return validKingdomMovePairToScoreMap;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private LinkedHashMap<KingdomMovePair, Integer> getOnlyValidKingdomMovePairs(final LinkedHashMap<KingdomMovePair, Integer> kingdomMovePairToScoreMap)
    {
        final ArrayList<KingdomMovePair> kingdomMovePairs = new ArrayList<>(kingdomMovePairToScoreMap.size());
        kingdomMovePairs.addAll(kingdomMovePairToScoreMap.keySet());
        final ArrayList<KingdomMovePair> validKingdomMovePairs = GameUtils.removeMovesBreakingMiddleKingdomRule(kingdomMovePairs);

        final LinkedHashMap<KingdomMovePair, Integer> validKingdomMovePairToScoreMap = keep(validKingdomMovePairs, kingdomMovePairToScoreMap);

        return validKingdomMovePairToScoreMap;
    }


    private LinkedHashMap<KingdomMovePair, Integer> keep(final ArrayList<KingdomMovePair> keysToKeep,
                                                         final LinkedHashMap<KingdomMovePair, Integer> kingdomMovePairToScoreMap)
    {
        final LinkedHashMap<KingdomMovePair, Integer> validKingdomMovePairToScoreMap = new LinkedHashMap<>(keysToKeep.size());
        for (final KingdomMovePair kingdomMovePair : keysToKeep)
        {
            validKingdomMovePairToScoreMap.put(kingdomMovePair, kingdomMovePairToScoreMap.get(kingdomMovePair));
        }

        return validKingdomMovePairToScoreMap;
    }

}
