package kingdominoplayer.naiverepresentation.movefilters;

import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.naiverepresentation.datastructures.Move;
import kingdominoplayer.naiverepresentation.strategies.FullGreedyAlgorithm;

import java.util.ArrayList;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 21:34<br><br>
 */
public class MaxScoringMoves implements MoveFilter
{
    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public ArrayList<Move> filterMoves(final String playerName, final Move[] moves, final LocalGameState gameState)
    {
        final ArrayList<Move> maxScoringMoves = new FullGreedyAlgorithm().getMaxScoringMoves(playerName, moves, gameState);

        return maxScoringMoves;
    }
}
