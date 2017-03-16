package kingdominoplayer.strategies.tinystrategies;

import kingdominoplayer.movefilters.tinymovefilters.AllTinyMoves;
import kingdominoplayer.movefilters.tinymovefilters.TinyMoveFilter;
import kingdominoplayer.search.TinyMonteCarloSearch;
import kingdominoplayer.tinyrepresentation.TinyGameState;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:13<br><br>
 */
public class TinyMonteCarlo implements TinyStrategy
{

    private final TinyMoveFilter iMoveFilter;
    private final TinyStrategy iPlayerStrategy;
    private final TinyStrategy iOpponentStrategy;
    private final boolean iUseRelativeBranchScore;

    public TinyMonteCarlo(final TinyStrategy playerStrategy, final TinyStrategy opponentStrategy, final boolean useRelativeBranchScore)
    {
        iMoveFilter = new AllTinyMoves();
        iPlayerStrategy = playerStrategy;
        iOpponentStrategy = opponentStrategy;
        iUseRelativeBranchScore = useRelativeBranchScore;
    }

    @Override
    public final byte[] selectMove(final String playerName, final byte[] availableMoves, final TinyGameState gameState)
    {
        final byte[] moves = iMoveFilter.filterMoves(playerName, availableMoves, gameState);

        final int numMaxScoringMoves = moves.length / TinyGameState.MOVE_ELEMENT_SIZE;

        if (numMaxScoringMoves > 1)
        {
            return new TinyMonteCarloSearch(playerName, iPlayerStrategy, iOpponentStrategy, iUseRelativeBranchScore).evaluate(gameState, moves);
        }
        else
        {
            return moves;
        }
    }
}
