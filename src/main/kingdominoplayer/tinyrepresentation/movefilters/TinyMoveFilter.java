package kingdominoplayer.tinyrepresentation.movefilters;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 21:37<br><br>
 */
public interface TinyMoveFilter
{
    byte[] filterMoves(String playerName, byte[] moves, TinyGameState gameState);
}
