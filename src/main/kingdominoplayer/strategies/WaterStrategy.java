package kingdominoplayer.strategies;

import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.datastructures.PlacedTile;

import java.util.Collection;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:15<br><br>
 */
public class WaterStrategy implements Strategy
{
    @Override
    public Move selectMove(final Move[] availableMoves,
                           final Collection<Domino> previousDraft,
                           final Collection<Domino> currentDraft,
                           final Collection<PlacedTile> placedTiles)
    {
        Move maxWatersMove = availableMoves[0];

        int maxWaters = 0;
        for (final Move availableMove : availableMoves)
        {
            final Domino chosenDomino = availableMove.getChosenDomino();

            if (chosenDomino != null)
            {
                final int tile1Waters = chosenDomino.getTile1().getTerrain().equals("water")? 1 : 0;
                final int tile2Waters = chosenDomino.getTile2().getTerrain().equals("water")? 1 : 0;

                if (tile1Waters + tile2Waters > maxWaters)
                {
                    maxWatersMove = availableMove;
                    maxWaters = tile1Waters + tile2Waters;
                }
            }
        }

        return maxWatersMove;
    }
}
