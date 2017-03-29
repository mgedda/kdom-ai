package kingdominoplayer.tinyrepresentation.simulationstrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyFullGreedy;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategy;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-29<br>
 * Time: 13:48<br><br>
 */
public class TinyFullGreedySimulationStrategy implements TinySimulationStrategy
{
    private final static TinyStrategy cPlayoutStrategy = new TinyFullGreedy();

    @Override
    public byte[] selectMove(final String playerName, final byte[] availableMoves, final TinyGameState gameState)
    {
        return cPlayoutStrategy.selectMove(playerName, availableMoves, gameState);
    }
}
