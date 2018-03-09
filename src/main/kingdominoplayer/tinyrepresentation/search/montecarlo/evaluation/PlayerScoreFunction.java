package kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
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
