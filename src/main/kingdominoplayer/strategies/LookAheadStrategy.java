package kingdominoplayer.strategies;

import kingdominoplayer.GameUtils;
import kingdominoplayer.datastructures.*;
import kingdominoplayer.planning.Scorer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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

            // See if we have already placed some dominos.
            //
            if (placedTiles.length > 0)
            {
                // We have dominos placed in our kingdom. See what options we have for placing
                // our selected domino.
                //
                final ArrayList<KingdomMovePair> possibleNewKingdoms = getPossibleNewKingdoms(kingdom, availableMoves);

                // Compute scores for all possible kingdom moves.
                //
                final LinkedHashMap<KingdomMovePair, Integer> kingdomMovePairToScoreMap = new LinkedHashMap<>(100);  // magic number

                for (final KingdomMovePair kingdomMovePair : possibleNewKingdoms)
                {
                    final int score = Scorer.computeScore(kingdomMovePair.getKingdom().getPlacedTiles());
                    kingdomMovePairToScoreMap.put(kingdomMovePair, score);
                }

                // See which moves have the maximum scores.
                //
                int maxScore = 0;
                for (final Integer score : kingdomMovePairToScoreMap.values())
                {
                    maxScore = score > maxScore ? score : maxScore;
                }

                final ArrayList<Move> maxScoringMoves = new ArrayList<>(availableMoves.length);
                for (final KingdomMovePair kingdomMovePair : possibleNewKingdoms)
                {
                    if (kingdomMovePairToScoreMap.get(kingdomMovePair) == maxScore)
                    {
                        maxScoringMoves.add(kingdomMovePair.getMove());
                    }
                }



                // Differentiate between max scoring moves!
                // // TODO [gedda] IMPORTANT! : CHANGE THIS TO TAKE CHOSEN DOMINO INTO ACCOUNT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //
                final ArrayList<Move> movesWithMostCrowns = GameUtils.getMovesWithMostCrownsOnSingleTile(maxScoringMoves.toArray(new Move[maxScoringMoves.size()]));

                return movesWithMostCrowns.get(0);

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


    private ArrayList<KingdomMovePair> getPossibleNewKingdoms(final Kingdom kingdom, final Move[] moves)
    {
        final PlacedTile[] placedTiles = kingdom.getPlacedTiles();

        final ArrayList<KingdomMovePair> possibleNewKingdoms = new ArrayList<>();

        for (final Move move : moves)
        {
            final PlacedDomino placedDomino = move.getPlacedDomino();

            if (placedDomino != null) // maybe we could not place the domino
            {
                final PlacedTile placedTile1 = new PlacedTile(placedDomino.getTile1(), placedDomino.getTile1Position());
                final PlacedTile placedTile2 = new PlacedTile(placedDomino.getTile2(), placedDomino.getTile2Position());

                final ArrayList<PlacedTile> newPlacedTiles = new ArrayList<>(placedTiles.length + 2);

                for (final PlacedTile placedTile : placedTiles)
                {
                    newPlacedTiles.add(placedTile);
                }
                newPlacedTiles.add(placedTile1);
                newPlacedTiles.add(placedTile2);

                final Kingdom newKingdom = new Kingdom(newPlacedTiles.toArray(new PlacedTile[newPlacedTiles.size()]));

                possibleNewKingdoms.add(new KingdomMovePair(newKingdom, move));
            }
            else
            {
                possibleNewKingdoms.add(new KingdomMovePair(kingdom, move));
            }
        }

        return possibleNewKingdoms;
    }
}
