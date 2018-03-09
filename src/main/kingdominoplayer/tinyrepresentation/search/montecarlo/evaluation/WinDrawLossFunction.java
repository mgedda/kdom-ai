package kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.search.montecarlo.MonteCarloMethods;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-23<br>
 * Time: 13:51<br><br>
 */
public class WinDrawLossFunction implements PlayoutScoringFunction
{
    @Override
    public double applyTo(final TinyGameState gameState, final String playerName)
    {
        final int[] scores = gameState.getScoresIndexed();
        final double[] result = MonteCarloMethods.getWinDrawLossArrayFromIndexedScores(scores);

        return result[TinyGameState.getPlayerID(playerName, gameState.getPlayers())];
    }
}
