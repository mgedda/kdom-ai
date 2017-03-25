package kingdominoplayer.tinyrepresentation.strategies;

import kingdominoplayer.tinyrepresentation.movefilters.TinyAllMoves;
import kingdominoplayer.tinyrepresentation.movefilters.TinyMaxScoringMoves;
import kingdominoplayer.tinyrepresentation.search.montecarlo.simulation.PlayerScoreFunction;
import kingdominoplayer.tinyrepresentation.search.montecarlo.simulation.RelativeScoreFunction;
import kingdominoplayer.tinyrepresentation.search.montecarlo.simulation.WinDrawLossFunction;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:51<br><br>
 */
public enum TinyStrategyID
{
    TRUE_RANDOM,
    GREEDY_PLACEMENT_RANDOM_DRAFT,
    FULL_GREEDY,

    MC_TR_TR_WDL,
    MC_TR_TR_P,
    MC_TR_TR_R,

    MC_FG_TR_R,
    MC_FG_FG_R,

    MCTS_TR_TR,
    MCTS_FG_FG;

    private TinyStrategy iStrategy;

    static
    {
        TRUE_RANDOM.iStrategy = new TinyTrueRandom();
        GREEDY_PLACEMENT_RANDOM_DRAFT.iStrategy = new TinyGreedyPlacementRandomDraft();
        FULL_GREEDY.iStrategy = new TinyFullGreedy();

        MC_TR_TR_WDL.iStrategy = new TinyMonteCarlo(new TinyAllMoves(), new TinyTrueRandom(), new TinyTrueRandom(), new WinDrawLossFunction());

        MC_TR_TR_P.iStrategy = new TinyMonteCarlo(new TinyAllMoves(), new TinyTrueRandom(), new TinyTrueRandom(), new PlayerScoreFunction());

        MC_TR_TR_R.iStrategy = new TinyMonteCarlo(new TinyAllMoves(), new TinyTrueRandom(), new TinyTrueRandom(), new RelativeScoreFunction());
        MC_FG_TR_R.iStrategy = new TinyMonteCarlo(new TinyMaxScoringMoves(), new TinyFullGreedy(), new TinyTrueRandom(), new RelativeScoreFunction());
        MC_FG_FG_R.iStrategy = new TinyMonteCarlo(new TinyMaxScoringMoves(), new TinyFullGreedy(), new TinyFullGreedy(), new RelativeScoreFunction());

        MCTS_TR_TR.iStrategy = new TinyMonteCarloTreeSearch(new TinyTrueRandom(), new TinyTrueRandom());
        MCTS_FG_FG.iStrategy = new TinyMonteCarloTreeSearch(new TinyFullGreedy(), new TinyFullGreedy());
    }

    public TinyStrategy getStrategy()
    {
        return iStrategy;
    }

}
