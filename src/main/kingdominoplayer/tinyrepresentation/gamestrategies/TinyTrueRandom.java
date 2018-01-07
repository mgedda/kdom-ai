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
    public byte[] selectMove(final String playerName, final byte[] availableMoves, final TinyGameState gameState)
    {
        final int numMoves = availableMoves.length / TinyConst.MOVE_ELEMENT_SIZE;
        final int randomIndex = Random.getInt(numMoves);

        @SuppressWarnings("UnnecessaryLocalVariable")
        final byte[] move = TinyGameState.getRow(availableMoves, randomIndex, TinyConst.MOVE_ELEMENT_SIZE);

        return move;
    }
}
