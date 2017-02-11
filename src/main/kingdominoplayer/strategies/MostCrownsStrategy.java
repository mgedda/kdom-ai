package kingdominoplayer.strategies;

import kingdominoplayer.GameUtils;
import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.datastructures.PlacedTile;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:13<br><br>
 */
public class MostCrownsStrategy implements Strategy
{
    @Override
    public Move selectMove(final Move[] availableMoves, final Domino[] previousDraft, final Domino[] currentDraft, final PlacedTile[] placedTiles)
    {
        final ArrayList<Move> maxCrownsMoves = GameUtils.getMovesWithMostCrownsOnSingleTile(availableMoves);

        return maxCrownsMoves.get(0);
    }
}