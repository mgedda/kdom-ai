package kingdominoplayer.strategies;

import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.datastructures.PlacedTile;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:11<br><br>
 */
public class RandomStrategy implements Strategy
{
    @Override
    public Move selectMove(final Move[] availableMoves,
                           final Collection<Domino> previousDraft,
                           final Collection<Domino> currentDraft,
                           final Collection<PlacedTile> placedTiles)
    {
        final int numMoves = availableMoves.length;
        final int randomNum = ThreadLocalRandom.current().nextInt(0, numMoves);

        return availableMoves[randomNum];
    }
}
