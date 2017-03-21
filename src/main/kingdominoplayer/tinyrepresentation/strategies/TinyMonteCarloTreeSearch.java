package kingdominoplayer.tinyrepresentation.strategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.TinyMonteCarloTreeSearchAlgorithm;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-21<br>
 * Time: 14:31<br><br>
 */
public class TinyMonteCarloTreeSearch implements TinyStrategy
{

    private final TinyStrategy iPlayerStrategy;
    private final TinyStrategy iOpponentStrategy;

    public TinyMonteCarloTreeSearch(final TinyStrategy playerStrategy, final TinyStrategy opponentStrategy)
    {
        iPlayerStrategy = playerStrategy;
        iOpponentStrategy = opponentStrategy;
    }

    @Override
    public final byte[] selectMove(final String playerName, final byte[] availableMoves, final TinyGameState gameState)
    {
        final int numMoves = availableMoves.length / TinyConst.MOVE_ELEMENT_SIZE;

        if (numMoves > 1)
        {
            return new TinyMonteCarloTreeSearchAlgorithm(playerName, iPlayerStrategy, iOpponentStrategy).evaluate(gameState, availableMoves);
        }
        else
        {
            return availableMoves;
        }
    }

}
