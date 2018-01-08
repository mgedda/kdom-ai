package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
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
    public byte[] selectMove(final String playerName, final TinyGameState gameState)
    {
        final byte[] availableMoves = gameState.getAvailableMoves(playerName);
        final int numMoves = availableMoves.length / TinyConst.MOVE_ELEMENT_SIZE;
        final int randomIndex = Random.getInt(numMoves);
        final byte[] move = new byte[TinyConst.MOVE_ELEMENT_SIZE];
        System.arraycopy(availableMoves, randomIndex * TinyConst.MOVE_ELEMENT_SIZE, move, 0, TinyConst.MOVE_ELEMENT_SIZE);

        return move;
    }
}
