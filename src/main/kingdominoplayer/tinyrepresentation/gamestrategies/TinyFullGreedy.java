package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.utils.Random;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-17<br>
 * Time: 16:05<br><br>
 */
public class TinyFullGreedy implements TinyStrategy
{

    @Override
    public byte[] selectMove(final String playerName, final TinyGameState gameState)
    {
        final byte[] availableMoves = gameState.getAvailableMoves(playerName);
        final byte[] maxScoringMoves = new TinyFullGreedyAlgorithm().getMaxScoringMoves(playerName, availableMoves, gameState);

        // Select random move among highest scoring moves.
        //
        final int numMoves = maxScoringMoves.length / TinyConst.MOVE_ELEMENT_SIZE;
        final int randomNum = Random.getInt(numMoves);

        @SuppressWarnings("UnnecessaryLocalVariable")
        final byte[] move = TinyGameState.getRow(maxScoringMoves, randomNum, TinyConst.MOVE_ELEMENT_SIZE);

        return move;
    }
}
