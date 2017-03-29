package kingdominoplayer.tinyrepresentation.simulationstrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-29<br>
 * Time: 13:43<br><br>
 */
public interface TinySimulationStrategy
{
    byte[] selectMove(String playerName, byte[] availableMoves, TinyGameState gameState);
}
