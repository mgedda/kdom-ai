package kingdominoplayer.tinyrepresentation.simulationstrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyFullGreedy;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategy;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyTrueRandom;
import kingdominoplayer.utils.Random;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-29<br>
 * Time: 13:51<br><br>
 */
public class TinyEpsilonGreedySelectionStrategy implements TinySimulationStrategy
{
    private final static TinyStrategy cGreedyStrategy = new TinyFullGreedy();
    private final static TinyStrategy cRandomStrategy = new TinyTrueRandom();

    private final double iEpsilon;

    public TinyEpsilonGreedySelectionStrategy(final double epsilon)
    {
        assert epsilon >= 0.0 && epsilon <= 1.0 : "pre: illegal argument!";
        iEpsilon = epsilon;
    }

    @Override
    public byte[] selectMove(final String playerName, final byte[] availableMoves, final TinyGameState gameState)
    {
        final double randomValue = Random.getInt(101) / 100.0;

        return randomValue > iEpsilon
                ? cGreedyStrategy.selectMove(playerName, availableMoves, gameState)
                : cRandomStrategy.selectMove(playerName, availableMoves, gameState);
    }
}
