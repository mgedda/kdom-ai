package kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-23<br>
 * Time: 13:50<br><br>
 */
public class PlayerScoreFunction implements PlayoutScoringFunction
{
    @Override
    public double applyTo(final TinyGameState gameState, final String playerName)
    {
        return gameState.getScore(playerName);
    }
}
