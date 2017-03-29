package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.tinyrepresentation.movefilters.TinyAllMoves;
import kingdominoplayer.tinyrepresentation.movefilters.TinyMaxScoringMoves;
import kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation.PlayerScoreFunction;
import kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation.RelativeScoreFunction;
import kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation.WinDrawLossFunction;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinyEpsilonGreedySelectionStrategy;
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

    MCE_TR_WDL,
    MCE_TR_P,
    MCE_TR_R,

    MCE_FG_R,
    MCE_EG_R,

    MCTS_TR,
    MCTS_FG;

    private TinyStrategy iStrategy;

    static
    {
        TRUE_RANDOM.iStrategy = new TinyTrueRandom();
        GREEDY_PLACEMENT_RANDOM_DRAFT.iStrategy = new TinyGreedyPlacementRandomDraft();
        FULL_GREEDY.iStrategy = new TinyFullGreedy();

        MCE_TR_WDL.iStrategy = new TinyMonteCarloEvaluation(new TinyAllMoves(), new TinyTrueRandomSimulationStrategy(), new WinDrawLossFunction());
        MCE_TR_P.iStrategy = new TinyMonteCarloEvaluation(new TinyAllMoves(), new TinyTrueRandomSimulationStrategy(), new PlayerScoreFunction());
        MCE_TR_R.iStrategy = new TinyMonteCarloEvaluation(new TinyAllMoves(), new TinyTrueRandomSimulationStrategy(), new RelativeScoreFunction());

        MCE_FG_R.iStrategy = new TinyMonteCarloEvaluation(new TinyMaxScoringMoves(), new TinyFullGreedySimulationStrategy(), new RelativeScoreFunction());
        MCE_EG_R.iStrategy = new TinyMonteCarloEvaluation(new TinyMaxScoringMoves(), new TinyEpsilonGreedySelectionStrategy(0.2), new RelativeScoreFunction());

        MCTS_TR.iStrategy = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy());
        MCTS_FG.iStrategy = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy());
    }

    public TinyStrategy getStrategy()
    {
        return iStrategy;
    }

}
