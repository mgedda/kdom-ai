package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.SearchParameters;
import kingdominoplayer.tinyrepresentation.movefilters.TinyMoveFilter;
import kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation.PlayoutScoringFunction;
import kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation.TinyMonteCarloEvaluationAlgorithm;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinySimulationStrategy;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:13<br><br>
 */
public class TinyMonteCarloEvaluation implements TinyStrategy
{

    private final TinyMoveFilter iMoveFilter;
    private final TinySimulationStrategy iSimulationStrategy;
    private final PlayoutScoringFunction iPlayoutScoringFunction;
    private final SearchParameters iSearchParameters;

    private TinyMonteCarloEvaluationAlgorithm iSimulation;

    public TinyMonteCarloEvaluation(final TinyMoveFilter moveFilter,
                                    final TinySimulationStrategy simulationStrategy,
                                    final PlayoutScoringFunction playoutScoringFunction,
                                    final SearchParameters searchParameters)
    {
        iMoveFilter = moveFilter;
        iSimulationStrategy = simulationStrategy;
        iPlayoutScoringFunction = playoutScoringFunction;
        iSearchParameters = searchParameters;
    }

    @Override
    public final byte[] selectMove(final String playerName, final TinyGameState gameState)
    {
        final byte[] availableMoves = gameState.getAvailableMoves(playerName);
        final byte[] moves = iMoveFilter.filterMoves(playerName, availableMoves, gameState);

        iSimulation = new TinyMonteCarloEvaluationAlgorithm(playerName, iSimulationStrategy, iPlayoutScoringFunction, iSearchParameters);

        return iSimulation.evaluate(gameState, moves);
    }

    @Override
    public double getNumPlayoutsPerSecond()
    {
        assert iSimulation != null : "Simulation not initialized.";
        return iSimulation.getNumPlayoutsPerSecond();
    }
}
