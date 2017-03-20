package kingdominoplayer.strategies;

import kingdominoplayer.naivedatastructures.LocalGameState;
import kingdominoplayer.naivedatastructures.Move;
import kingdominoplayer.utils.Random;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-02<br>
 * Time: 17:04<br><br>
 */
public class FullGreedy implements Strategy
{
    @Override
    public Move selectMove(final String playerName, final Move[] availableMoves, final LocalGameState gameState)
    {
        final ArrayList<Move> maxScoringMoves = new FullGreedyAlgorithm().getMaxScoringMoves(playerName, availableMoves, gameState);

        // Select random move among highest scoring moves.
        //
        final int numMoves = maxScoringMoves.size();
        final int randomNum = Random.getInt(numMoves);

        return maxScoringMoves.get(randomNum);
    }

}
