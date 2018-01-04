package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.SearchParameters;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.TinyMonteCarloTreeSearchAlgorithm;
import kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.UCTSearch;
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

    //private TinyMonteCarloTreeSearchAlgorithm iSearchAlgorithm;
    private UCTSearch iSearchAlgorithm;

    public TinyMonteCarloTreeSearch(final TinySimulationStrategy simulationStrategy, final SearchParameters searchParameters)
    {
        iSimulationStrategy = simulationStrategy;
        iSearchParameters = searchParameters;
    }

    @Override
    public final byte[] selectMove(final String playerName, final byte[] availableMoves, final TinyGameState gameState)
    {
        //iSearchAlgorithm = new TinyMonteCarloTreeSearchAlgorithm(playerName, iSimulationStrategy, iSearchParameters);
        iSearchAlgorithm = new UCTSearch(playerName, iSimulationStrategy, iSearchParameters);
        return iSearchAlgorithm.evaluate(gameState, availableMoves);
    }

    @Override
    public double getNumPlayoutsPerSecond()
    {
        assert iSearchAlgorithm != null : "Search algorithm not initialized yet!";
        return iSearchAlgorithm.getNumPlayoutsPerSecond();
    }
}
