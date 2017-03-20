package kingdominoplayer.naivemovefilters;

import kingdominoplayer.naivedatastructures.LocalGameState;
import kingdominoplayer.naivedatastructures.Move;
import kingdominoplayer.utils.ArrayUtils;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 21:33<br><br>
 */
public class AllMoves implements MoveFilter
{

    @Override
    public ArrayList<Move> filterMoves(final String playerName, final Move[] moves, final LocalGameState gameState)
    {
        return ArrayUtils.toArrayList(moves);
    }
}
