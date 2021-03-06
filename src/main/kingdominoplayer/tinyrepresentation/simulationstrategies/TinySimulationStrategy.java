package kingdominoplayer.tinyrepresentation.simulationstrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-29<br>
 * Time: 13:43<br><br>
 */
public interface TinySimulationStrategy
{
    byte[] selectMove(String playerName, String playerTurn, byte[] availableMoves, TinyGameState gameState);
}
