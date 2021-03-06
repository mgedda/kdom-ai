package kingdominoplayer.naiverepresentation.strategies;

import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.naiverepresentation.datastructures.Move;
import kingdominoplayer.utils.Random;


/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:11<br><br>
 */
public class TrueRandom implements Strategy
{
    @Override
    public Move selectMove(final String playerName, final Move[] availableMoves, final LocalGameState gameState)
    {
        final int numMoves = availableMoves.length;
        final int randomNum = Random.getInt(numMoves);

        return availableMoves[randomNum];
    }
}
