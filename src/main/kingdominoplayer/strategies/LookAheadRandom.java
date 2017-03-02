package kingdominoplayer.strategies;

import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-02<br>
 * Time: 17:04<br><br>
 */
public class LookAheadRandom extends LookAheadStrategy implements Strategy
{
    @Override
    public Move selectMove(final String playerName, final Move[] availableMoves, final LocalGameState gameState)
    {
        final ArrayList<Move> maxScoringMoves = selectMaxScoringMoves(playerName, availableMoves, gameState);

        // Select random move among highest scoring moves.
        //
        final int numMoves = maxScoringMoves.size();
        final int randomNum = ThreadLocalRandom.current().nextInt(0, numMoves);

        return maxScoringMoves.get(randomNum);
    }

}
