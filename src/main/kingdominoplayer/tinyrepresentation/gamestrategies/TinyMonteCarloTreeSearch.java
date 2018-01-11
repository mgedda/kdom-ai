package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.SearchParameters;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts.UCTSearch;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinySimulationStrategy;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-21<br>
 * Time: 14:31<br><br>
 */
public class TinyMonteCarloTreeSearch implements TinyStrategy
{

    private final TinySimulationStrategy iSimulationStrategy;
    private final SearchParameters iSearchParameters;
    private final double iExploreFactor;

    private UCTSearch iSearchAlgorithm;

    public TinyMonteCarloTreeSearch(final TinySimulationStrategy simulationStrategy,
                                    final SearchParameters searchParameters,
                                    final double exploreFactor)
    {
        iSimulationStrategy = simulationStrategy;
        iSearchParameters = searchParameters;
        iExploreFactor = exploreFactor;
    }

    @Override
    public final byte[] selectMove(final String playerName, final TinyGameState gameState)
    {
        iSearchAlgorithm = new UCTSearch(playerName, iSimulationStrategy, iSearchParameters, iExploreFactor);
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
