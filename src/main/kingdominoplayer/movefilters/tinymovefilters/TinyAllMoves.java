package kingdominoplayer.movefilters.tinymovefilters;

import kingdominoplayer.tinyrepresentation.TinyGameState;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 21:39<br><br>
 */
public class TinyAllMoves implements TinyMoveFilter
{
    @Override
    public byte[] filterMoves(final String playerName, final byte[] moves, final TinyGameState gameState)
    {
        return moves;
    }
}
