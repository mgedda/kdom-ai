package kingdominoplayer.strategies;

import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.datastructures.PlacedTile;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:08<br><br>
 */
public interface Strategy
{
    Move selectMove(Move[] availableMoves, Domino[] previousDraft, Domino[] currentDraft, PlacedTile[] placedTiles);
}