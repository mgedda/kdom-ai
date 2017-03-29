package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.tinyrepresentation.movefilters.TinyAllMoves;
import kingdominoplayer.tinyrepresentation.movefilters.TinyMaxScoringMoves;
import kingdominoplayer.tinyrepresentation.search.montecarlo.simulation.PlayerScoreFunction;
import kingdominoplayer.tinyrepresentation.search.montecarlo.simulation.RelativeScoreFunction;
import kingdominoplayer.tinyrepresentation.search.montecarlo.simulation.WinDrawLossFunction;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinyFullGreedySimulationStrategy;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinyTrueRandomSimulationStrategy;

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

    MC_TR_WDL,
    MC_TR_P,
    MC_TR_R,

    MC_FG_R,

    MCTS_TR,
    MCTS_FG;

    private TinyStrategy iStrategy;

    static
    {
        TRUE_RANDOM.iStrategy = new TinyTrueRandom();
        GREEDY_PLACEMENT_RANDOM_DRAFT.iStrategy = new TinyGreedyPlacementRandomDraft();
        FULL_GREEDY.iStrategy = new TinyFullGreedy();

        MC_TR_WDL.iStrategy = new TinyMonteCarlo(new TinyAllMoves(), new TinyTrueRandomSimulationStrategy(), new WinDrawLossFunction());
        MC_TR_P.iStrategy = new TinyMonteCarlo(new TinyAllMoves(), new TinyTrueRandomSimulationStrategy(), new PlayerScoreFunction());
        MC_TR_R.iStrategy = new TinyMonteCarlo(new TinyAllMoves(), new TinyTrueRandomSimulationStrategy(), new RelativeScoreFunction());

        MC_FG_R.iStrategy = new TinyMonteCarlo(new TinyMaxScoringMoves(), new TinyFullGreedySimulationStrategy(), new RelativeScoreFunction());

        MCTS_TR.iStrategy = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy());
        MCTS_FG.iStrategy = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy());
    }

    public TinyStrategy getStrategy()
    {
        return iStrategy;
    }

}
