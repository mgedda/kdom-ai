package kingdominoplayer.strategies.tinystrategies;

import kingdominoplayer.search.TinyMonteCarloSearch;
import kingdominoplayer.strategies.FullGreedyAlgorithm;
import kingdominoplayer.tinyrepresentation.TinyGameState;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:13<br><br>
 */
public class TinyMonteCarlo implements TinyStrategy
{

    private final TinyStrategy iPlayerStrategy;
    private final TinyStrategy iOpponentStrategy;
    private final boolean iUseRelativeBranchScore;

    public TinyMonteCarlo(final TinyStrategy playerStrategy, final TinyStrategy opponentStrategy, final boolean useRelativeBranchScore)
    {
        iPlayerStrategy = playerStrategy;
        iOpponentStrategy = opponentStrategy;
        iUseRelativeBranchScore = useRelativeBranchScore;
    }

    @Override
    public final byte[] selectMove(final String playerName, final byte[] availableMoves, final TinyGameState gameState)
    {
        // TODO [gedda] IMPORTANT! : Write new full greedy algorithm for tiny representation!
        //final byte[] maxScoringMoves = new FullGreedyAlgorithm().getMaxScoringMoves(playerName, availableMoves, gameState);
        final byte[] maxScoringMoves = availableMoves;

        final int numMaxScoringMoves = maxScoringMoves.length / TinyGameState.MOVE_ELEMENT_SIZE;

        if (numMaxScoringMoves > 1)
        {
            final TinyStrategy playerStrategy = getPlayerStrategy();
            final TinyStrategy opponentStrategy = getOpponentStrategy();
            final boolean relativeBranchScore = useRelativeBranchScore();

            return new TinyMonteCarloSearch(playerName, playerStrategy, opponentStrategy, relativeBranchScore).evaluate(gameState, maxScoringMoves);
        }
        else
        {
            return maxScoringMoves;
        }
    }

    private TinyStrategy getPlayerStrategy()
    {
        return iPlayerStrategy;
    }

    private TinyStrategy getOpponentStrategy()
    {
        return iOpponentStrategy;
    }

    private boolean useRelativeBranchScore()
    {
        return iUseRelativeBranchScore;
    }
}
