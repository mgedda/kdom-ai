package kingdominoplayer.strategies;

import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.search.MonteCarloSearch;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-08<br>
 * Time: 22:08<br><br>
 */
public abstract class MonteCarlo implements Strategy
{
    @Override
    public final Move selectMove(final String playerName, final Move[] availableMoves, final LocalGameState gameState)
    {
        final ArrayList<Move> maxScoringMoves = new FullGreedyAlgorithm().getMaxScoringMoves(playerName, availableMoves, gameState);

        if (maxScoringMoves.size() > 1)
        {
            final Strategy playerStrategy = getPlayerStrategy();
            final Strategy opponentStrategy = getOpponentStrategy();
            final boolean relativeBranchScore = useRelativeBranchScore();

            return new MonteCarloSearch(playerName, playerStrategy, opponentStrategy, relativeBranchScore).evaluate(gameState, maxScoringMoves);
        }
        else
        {
            return maxScoringMoves.get(0);
        }
    }

    protected abstract Strategy getPlayerStrategy();

    protected abstract Strategy getOpponentStrategy();

    protected abstract boolean useRelativeBranchScore();
}
