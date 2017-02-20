package kingdominoplayer.strategies;

import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.datastructures.PlacedTile;

import java.util.Collection;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:10<br><br>
 */
public class FirstStrategy implements Strategy
{
    @Override
    public Move selectMove(final Move[] availableMoves,
                           final Collection<Domino> previousDraft,
                           final Collection<Domino> currentDraft,
                           final Collection<PlacedTile> placedTiles)
    {
        return availableMoves[0];
    }
}
