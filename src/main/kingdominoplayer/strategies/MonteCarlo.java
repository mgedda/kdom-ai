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
public class MonteCarlo implements Strategy
{

    private final Strategy iPlayerStrategy;
    private final Strategy iOpponentStrategy;
    private final boolean iUseRelativeBranchScore;

    public MonteCarlo(final Strategy playerStrategy, final Strategy opponentStrategy, final boolean useRelativeBranchScore)
    {
        iPlayerStrategy = playerStrategy;
        iOpponentStrategy = opponentStrategy;
        iUseRelativeBranchScore = useRelativeBranchScore;
    }

    @Override
    public final Move selectMove(final String playerName, final Move[] availableMoves, final LocalGameState gameState)
    {
        // TODO [gedda] IMPORTANT! : Change back to full greedy algorithm!
        //final ArrayList<Move> maxScoringMoves = new FullGreedyAlgorithm().getMaxScoringMoves(playerName, availableMoves, gameState);
        final ArrayList<Move> maxScoringMoves = new ArrayList<>(availableMoves.length);
        for (final Move move : availableMoves)
        {
            maxScoringMoves.add(move);
        }


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

    private Strategy getPlayerStrategy()
    {
        return iPlayerStrategy;
    }

    private Strategy getOpponentStrategy()
    {
        return iOpponentStrategy;
    }

    private boolean useRelativeBranchScore()
    {
        return iUseRelativeBranchScore;
    }
}
