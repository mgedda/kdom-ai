package kingdominoplayer.strategies;

import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.utils.Random;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-07<br>
 * Time: 21:28<br><br>
 */
public class GreedyPlacementRandomDraft implements Strategy
{
    @Override
    public Move selectMove(final String playerName, final Move[] availableMoves, final LocalGameState gameState)
    {
        final ArrayList<Move> maxScoringMoves = new GreedyPlacementRandomDraftAlgorithm().getMaxScoringMoves(playerName, availableMoves, gameState);

        final int numMoves = maxScoringMoves.size();
        final int randomNum = Random.getInt(numMoves);

        return maxScoringMoves.get(randomNum);
    }
}
