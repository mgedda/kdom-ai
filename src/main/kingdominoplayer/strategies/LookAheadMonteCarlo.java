package kingdominoplayer.strategies;

import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.search.MonteCarloSearch;

import java.util.ArrayList;

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
        final ArrayList<Move> maxScoringMoves = selectMaxScoringMoves(playerName, availableMoves, gameState);

        if (maxScoringMoves.size() > 1)
        {
            return new MonteCarloSearch().evaluate(playerName, gameState, maxScoringMoves);
        }
        else
        {
            return maxScoringMoves.get(0);
        }
    }

}
