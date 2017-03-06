package kingdominoplayer.strategies;

import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;

import java.util.ArrayList;
import java.util.Set;
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
        final Set<Move> maxScoringMoves = selectMaxScoringMoves(playerName, availableMoves, gameState);

        final ArrayList<Move> movesToEvaluate = new ArrayList<>(maxScoringMoves.size());
        movesToEvaluate.addAll(maxScoringMoves);

        // Select random move among highest scoring moves.
        //
        final int numMoves = movesToEvaluate.size();
        final int randomNum = ThreadLocalRandom.current().nextInt(0, numMoves);

        return movesToEvaluate.get(randomNum);
    }

}
