package kingdominoplayer.strategies;

import kingdominoplayer.utils.GameUtils;
import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.datastructures.PlacedTile;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:16<br><br>
 */
public class ExpandStrategy implements Strategy
{
    @Override
    public Move selectMove(final Move[] availableMoves, final Domino[] previousDraft, final Domino[] currentDraft, final PlacedTile[] placedTiles)
    {
        Move expandMove = availableMoves[0];

        // Choose move that picks domino with terrain that we already have most of.
        //
        final String[] terrainsSorted = GameUtils.getTerrainsSortedBasedOnNumberOfTilesUseCrownsAsDealBreaker(placedTiles);

        for (final String terrain : terrainsSorted)
        {
            final Move selectedMove = GameUtils.getMoveWithChosenDominoTerrainUseCrownsAsDealBreaker(terrain, availableMoves);

            if (selectedMove != null)
            {
                expandMove = selectedMove;
                break;
            }
        }

        return expandMove;
    }
}
