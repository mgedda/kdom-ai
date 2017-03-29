package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.tinyrepresentation.movefilters.TinyMoveFilter;
import kingdominoplayer.tinyrepresentation.search.montecarlo.simulation.PlayoutScoringFunction;
import kingdominoplayer.tinyrepresentation.search.montecarlo.simulation.TinyMonteCarloSimulation;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

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
    private final PlayoutScoringFunction iPlayoutScoringFunction;

    private TinyMonteCarloSimulation iSimulation;

    public TinyMonteCarlo(final TinyMoveFilter moveFilter,
                          final TinyStrategy playerStrategy,
                          final TinyStrategy opponentStrategy,
                          final PlayoutScoringFunction playoutScoringFunction)
    {
        iMoveFilter = moveFilter;
        iPlayerStrategy = playerStrategy;
        iOpponentStrategy = opponentStrategy;
        iPlayoutScoringFunction = playoutScoringFunction;
    }

    @Override
    public final byte[] selectMove(final String playerName, final byte[] availableMoves, final TinyGameState gameState)
    {
        final byte[] moves = iMoveFilter.filterMoves(playerName, availableMoves, gameState);

        iSimulation = new TinyMonteCarloSimulation(playerName, iPlayerStrategy, iOpponentStrategy, iPlayoutScoringFunction);

        return iSimulation.evaluate(gameState, moves);
    }

    @Override
    public double getNumPlayoutsPerSecond()
    {
        assert iSimulation != null : "Simulation not initialized.";
        return iSimulation.getNumPlayoutsPerSecond();
    }
}
