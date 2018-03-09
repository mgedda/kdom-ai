package kingdominoplayer.naiverepresentation.strategies;

import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.naiverepresentation.datastructures.Move;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:08<br><br>
 */
public interface Strategy
{
    Move selectMove(String playerName, Move[] availableMoves, LocalGameState gameState);
}
