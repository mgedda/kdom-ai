package kingdominoplayer.tinyrepresentation.movefilters;

import kingdominoplayer.tinyrepresentation.gamestrategies.TinyFullGreedyAlgorithm;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-19<br>
 * Time: 22:38<br><br>
 */
public class TinyMaxScoringMoves implements TinyMoveFilter
{
    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public byte[] filterMoves(final String playerName, final byte[] moves, final TinyGameState gameState)
    {
        final byte[] maxScoringMoves = new TinyFullGreedyAlgorithm().getMaxScoringMoves(playerName, moves, gameState);

        return maxScoringMoves;
    }
}
