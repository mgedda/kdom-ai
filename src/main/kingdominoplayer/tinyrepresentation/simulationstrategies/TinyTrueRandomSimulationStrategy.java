package kingdominoplayer.tinyrepresentation.simulationstrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategy;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyTrueRandom;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-29<br>
 * Time: 13:50<br><br>
 */
public class TinyTrueRandomSimulationStrategy implements TinySimulationStrategy
{
    private final static TinyStrategy cPlayoutStrategy = new TinyTrueRandom();

    @Override
    public byte[] selectMove(final String playerName, final String playerTurn, final byte[] availableMoves, final TinyGameState gameState)
    {
        return cPlayoutStrategy.selectMove(playerTurn, gameState);
    }

}
