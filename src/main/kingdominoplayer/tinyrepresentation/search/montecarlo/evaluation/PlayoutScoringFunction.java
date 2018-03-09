package kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-23<br>
 * Time: 13:49<br><br>
 */
public interface PlayoutScoringFunction
{
    double applyTo(TinyGameState gameState, String playerName);
}
