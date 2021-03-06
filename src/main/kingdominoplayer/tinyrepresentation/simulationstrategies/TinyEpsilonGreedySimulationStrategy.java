package kingdominoplayer.tinyrepresentation.simulationstrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyFullGreedy;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategy;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyTrueRandom;
import kingdominoplayer.utils.Random;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-29<br>
 * Time: 13:51<br><br>
 */
public class TinyEpsilonGreedySimulationStrategy implements TinySimulationStrategy
{
    private final static TinyStrategy cGreedyStrategy = new TinyFullGreedy();
    private final static TinyStrategy cRandomStrategy = new TinyTrueRandom();

    private final double iEpsilon;

    public TinyEpsilonGreedySimulationStrategy(final double epsilon)
    {
        assert epsilon >= 0.0 && epsilon <= 1.0 : "pre: illegal argument!";
        iEpsilon = epsilon;
    }

    @Override
    public byte[] selectMove(final String playerName, final String playerTurn, final byte[] availableMoves, final TinyGameState gameState)
    {
        final double randomValue = Random.getInt(101) / 100.0;

        return randomValue > iEpsilon
                ? cGreedyStrategy.selectMove(playerTurn, gameState)
                : cRandomStrategy.selectMove(playerTurn, gameState);
    }
}
