package kingdominoplayer.naiverepresentation.movefilters;

import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.naiverepresentation.datastructures.Move;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 21:30<br><br>
 */
public interface MoveFilter
{
    ArrayList<Move> filterMoves(String playerName, Move[] moves, LocalGameState gameState);
}
