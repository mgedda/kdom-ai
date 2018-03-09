package kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-23<br>
 * Time: 13:50<br><br>
 */
public class RelativeScoreFunction implements PlayoutScoringFunction
{
    @Override
    public double applyTo(final TinyGameState gameState, final String playerName)
    {
        final Map<String, Integer> scores = gameState.getScores();

        final int playerScore = scores.get(playerName);
        scores.remove(playerName);

        final Collection<Integer> opponentScores = scores.values();

        final ArrayList<Integer> opponentScoresSorted = new ArrayList<>(opponentScores.size());
        opponentScoresSorted.addAll(opponentScores);
        opponentScoresSorted.sort((Integer s1, Integer s2) ->
        {
            //noinspection CodeBlock2Expr
            return s1 > s2 ? -1 : s2 > s1 ? 1 : 0;
        });

        final Integer topOpponentScore = opponentScoresSorted.get(0);

        //noinspection UnnecessaryLocalVariable
        final double score = playerScore / (double)(topOpponentScore + playerScore);

        return score;
    }
}
