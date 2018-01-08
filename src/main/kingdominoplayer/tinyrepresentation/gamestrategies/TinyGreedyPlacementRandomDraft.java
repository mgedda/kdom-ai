package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.utils.Random;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-20<br>
 * Time: 11:23<br><br>
 */
public class TinyGreedyPlacementRandomDraft implements TinyStrategy
{
    @Override
    public byte[] selectMove(final String playerName, final TinyGameState gameState)
    {
        final byte[] availableMoves = gameState.getAvailableMoves(playerName);
        final byte[] maxScoringMoves = new TinyGreedyPlacementRandomDraftAlgorithm().getMaxScoringMoves(playerName, availableMoves, gameState);

        // Select random move among highest scoring moves.
        //
        final int numMoves = maxScoringMoves.length / TinyConst.MOVE_ELEMENT_SIZE;
        final int randomNum = Random.getInt(numMoves);

        @SuppressWarnings("UnnecessaryLocalVariable")
        final byte[] move = TinyGameState.getRow(maxScoringMoves, randomNum, TinyConst.MOVE_ELEMENT_SIZE);

        return move;
    }
}
