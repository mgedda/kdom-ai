package kingdominoplayer.naivestrategies;

import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.utils.GameUtils;
import kingdominoplayer.naiverepresentation.datastructures.Move;
import kingdominoplayer.naiverepresentation.datastructures.PlacedTile;

import java.util.Collection;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:16<br><br>
 */
public class Expand implements Strategy
{
    @Override
    public Move selectMove(final String playerName, final Move[] availableMoves, final LocalGameState gameState)
    {
        Move expandMove = availableMoves[0];

        // Choose move that picks domino with terrain that we already have most of.
        //
        final Collection<PlacedTile> placedTiles = gameState.getPlacedTiles(playerName);
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
