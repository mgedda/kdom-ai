package kingdominoplayer.tinyrepresentation.gamestrategies;

/*
 * Copyright 2018 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2018-01-23<br>
 * Time: 20:41<br><br>
 */

import kingdominoplayer.SearchParameters;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts.UCB;
import kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts.UCBProgressiveWinBias;
import kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts.UCTSearch;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinySimulationStrategy;

public class TinyMonteCarloTreeSearchPWB implements TinyStrategy
{

    private final TinySimulationStrategy iSimulationStrategy;
    private final SearchParameters iSearchParameters;

    private final UCB iUCB;
    private UCTSearch iSearchAlgorithm;

    public TinyMonteCarloTreeSearchPWB(final TinySimulationStrategy simulationStrategy,
                                       final SearchParameters searchParameters,
                                       final double exploreFactor,
                                       final double biasWeight)
    {
        iSimulationStrategy = simulationStrategy;
        iSearchParameters = searchParameters;
        iUCB = new UCBProgressiveWinBias(exploreFactor, biasWeight);
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
