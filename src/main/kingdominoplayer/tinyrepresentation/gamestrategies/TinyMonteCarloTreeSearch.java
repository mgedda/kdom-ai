package kingdominoplayer.tinyrepresentation.gamestrategies;

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

    private TinyMonteCarloTreeSearchAlgorithm iSearchAlgorithm;

    public TinyMonteCarloTreeSearch(final TinyStrategy playerStrategy, final TinyStrategy opponentStrategy)
    {
        iPlayerStrategy = playerStrategy;
        iOpponentStrategy = opponentStrategy;
    }

    @Override
    public final byte[] selectMove(final String playerName, final byte[] availableMoves, final TinyGameState gameState)
    {
        iSearchAlgorithm = new TinyMonteCarloTreeSearchAlgorithm(playerName, iPlayerStrategy, iOpponentStrategy);
        return iSearchAlgorithm.evaluate(gameState, availableMoves);
    }

    @Override
    public double getNumPlayoutsPerSecond()
    {
        assert iSearchAlgorithm != null : "Search algorithm not initialized yet!";
        return iSearchAlgorithm.getNumPlayoutsPerSecond();
    }
}
