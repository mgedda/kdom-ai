package kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-23<br>
 * Time: 13:49<br><br>
 */
public interface PlayoutScoringFunction
{
    double applyTo(TinyGameState gameState, String playerName);
}
