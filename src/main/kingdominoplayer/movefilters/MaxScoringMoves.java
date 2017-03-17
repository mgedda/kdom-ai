package kingdominoplayer.movefilters;

import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.strategies.FullGreedyAlgorithm;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 21:34<br><br>
 */
public class MaxScoringMoves implements MoveFilter
{
    @Override
    public ArrayList<Move> filterMoves(final String playerName, final Move[] moves, final LocalGameState gameState)
    {
        final ArrayList<Move> maxScoringMoves = new FullGreedyAlgorithm().getMaxScoringMoves(playerName, moves, gameState);

        return maxScoringMoves;
    }
}