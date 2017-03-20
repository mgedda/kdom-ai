package kingdominoplayer.tinyrepresentation.movefilters;

import kingdominoplayer.tinyrepresentation.TinyGameState;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 21:37<br><br>
 */
public interface TinyMoveFilter
{
    byte[] filterMoves(String playerName, byte[] moves, TinyGameState gameState);
}
