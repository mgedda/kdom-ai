package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.SearchParameters;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts.UCB;
import kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts.UCTSearch;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinySimulationStrategy;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-21<br>
 * Time: 14:31<br><br>
 */
public class TinyMonteCarloTreeSearch implements TinyStrategy
{

    private final TinySimulationStrategy iSimulationStrategy;
    private final SearchParameters iSearchParameters;

    private final UCB iUCB;
    private UCTSearch iSearchAlgorithm;

    public TinyMonteCarloTreeSearch(final TinySimulationStrategy simulationStrategy,
                                    final SearchParameters searchParameters,
                                    final double exploreFactor)
    {
        iSimulationStrategy = simulationStrategy;
        iSearchParameters = searchParameters;
        iUCB = new UCB(exploreFactor);
    }

    @Override
    public final byte[] selectMove(final String playerName, final TinyGameState gameState)
    {
        iSearchAlgorithm = new UCTSearch(playerName, iSimulationStrategy, iSearchParameters, iUCB);
        final byte[] availableMoves = gameState.getAvailableMoves(playerName);
        return iSearchAlgorithm.evaluate(gameState, availableMoves);
    }

    @Override
    public double getNumPlayoutsPerSecond()
    {
        assert iSearchAlgorithm != null : "Search algorithm not initialized yet!";
        return iSearchAlgorithm.getNumPlayoutsPerSecond();
    }
}
