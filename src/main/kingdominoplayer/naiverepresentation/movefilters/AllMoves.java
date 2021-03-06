package kingdominoplayer.naiverepresentation.movefilters;

import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.naiverepresentation.datastructures.Move;
import kingdominoplayer.utils.ArrayUtils;

import java.util.ArrayList;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
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
