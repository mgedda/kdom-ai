package kingdominoplayer.strategies;

import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.search.MonteCarloSearch;

import java.util.ArrayList;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-02<br>
 * Time: 17:04<br><br>
 */
public class LookAheadMonteCarlo extends LookAheadStrategy implements Strategy
{
    @Override
    public Move selectMove(final String playerName, final Move[] availableMoves, final LocalGameState gameState)
    {
        final Set<Move> maxScoringMoves = selectMaxScoringMoves(playerName, availableMoves, gameState);

        final ArrayList<Move> movesToEvaluate = new ArrayList<>(maxScoringMoves.size());
        movesToEvaluate.addAll(maxScoringMoves);

        if (maxScoringMoves.size() > 1)
        {
            return new MonteCarloSearch().evaluate(playerName, gameState, movesToEvaluate);
        }
        else
        {
            return movesToEvaluate.get(0);
        }
    }

}
