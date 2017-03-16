package kingdominoplayer.strategies;

import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.movefilters.AllMoves;
import kingdominoplayer.search.MonteCarloSearch;
import kingdominoplayer.utils.ArrayUtils;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-08<br>
 * Time: 22:08<br><br>
 */
public class MonteCarlo implements Strategy
{

    private final AllMoves iMoveFilter;
    private final Strategy iPlayerStrategy;
    private final Strategy iOpponentStrategy;
    private final boolean iUseRelativeBranchScore;

    public MonteCarlo(final Strategy playerStrategy, final Strategy opponentStrategy, final boolean useRelativeBranchScore)
    {
        iMoveFilter = new AllMoves();
        iPlayerStrategy = playerStrategy;
        iOpponentStrategy = opponentStrategy;
        iUseRelativeBranchScore = useRelativeBranchScore;
    }

    @Override
    public final Move selectMove(final String playerName, final Move[] availableMoves, final LocalGameState gameState)
    {
        final ArrayList<Move> moves = iMoveFilter.filterMoves(playerName, availableMoves, gameState);

        if (moves.size() > 1)
        {
            return new MonteCarloSearch(playerName, iPlayerStrategy, iOpponentStrategy, iUseRelativeBranchScore).evaluate(gameState, moves);
        }
        else
        {
            return moves.get(0);
        }
    }
}
