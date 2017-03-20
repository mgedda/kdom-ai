package kingdominoplayer.naiverepresentation.strategies;

import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.naiverepresentation.datastructures.Move;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:08<br><br>
 */
public interface Strategy
{
    Move selectMove(String playerName, Move[] availableMoves, LocalGameState gameState);
}
