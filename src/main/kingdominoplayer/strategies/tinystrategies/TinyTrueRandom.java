package kingdominoplayer.strategies.tinystrategies;

import kingdominoplayer.tinyrepresentation.TinyGameState;
import kingdominoplayer.utils.Random;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:56<br><br>
 */
public class TinyTrueRandom implements TinyStrategy
{

    @Override
    public byte[] selectMove(final String playerName, final byte[] availableMoves, final TinyGameState gameState)
    {
        final int numMoves = availableMoves.length / TinyGameState.MOVE_ELEMENT_SIZE;
        final int randomIndex = Random.getInt(numMoves);
        final byte[] move = new byte[TinyGameState.MOVE_ELEMENT_SIZE];
        System.arraycopy(availableMoves, randomIndex * TinyGameState.MOVE_ELEMENT_SIZE, move, 0, TinyGameState.MOVE_ELEMENT_SIZE);

        return move;
    }
}
