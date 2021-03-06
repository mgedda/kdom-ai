package kingdominoplayer.naiverepresentation.strategies;

import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.naiverepresentation.datastructures.Move;
import kingdominoplayer.naiverepresentation.movefilters.MaxScoringMoves;
import kingdominoplayer.naiverepresentation.movefilters.MoveFilter;
import kingdominoplayer.naiverepresentation.search.MonteCarloSearch;

import java.util.ArrayList;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-08<br>
 * Time: 22:08<br><br>
 */
public class MonteCarlo implements Strategy
{

    private final MoveFilter iMoveFilter;
    private final Strategy iPlayerStrategy;
    private final Strategy iOpponentStrategy;
    private final boolean iUseRelativeBranchScore;

    public MonteCarlo(final Strategy playerStrategy, final Strategy opponentStrategy, final boolean useRelativeBranchScore)
    {
        iMoveFilter = new MaxScoringMoves();
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
