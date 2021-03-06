package kingdominoplayer.tinyrepresentation.movefilters;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
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
