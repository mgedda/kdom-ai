package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.SearchParameters;
import kingdominoplayer.tinyrepresentation.movefilters.TinyAllMoves;
import kingdominoplayer.tinyrepresentation.movefilters.TinyMaxScoringMoves;
import kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation.PlayerScoreFunction;
import kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation.RelativeScoreFunction;
import kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation.WinDrawLossFunction;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinyEpsilonGreedySelectionStrategy;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinyFullGreedySimulationStrategy;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinyTrueRandomSimulationStrategy;

/**
 * Created by gedda on 3/30/17.
 */
public class TinyStrategyFactory
{
    private final SearchParameters iSearchParameters;

    public TinyStrategyFactory(final SearchParameters searchParameters)
    {
        iSearchParameters = searchParameters;
    }

    public TinyStrategy getGameStrategy(final TinyStrategyID tinyStrategyID)
    {
        final TinyStrategy result;

        switch (tinyStrategyID)
        {
            case TRUE_RANDOM:
                result = new TinyTrueRandom();
                break;
            case GREEDY_PLACEMENT_RANDOM_DRAFT:
                result = new TinyGreedyPlacementRandomDraft();
                break;
            case FULL_GREEDY:
                result = new TinyFullGreedy();
                break;
            case MCE_TR_WDL:
                result = new TinyMonteCarloEvaluation(new TinyAllMoves(), new TinyTrueRandomSimulationStrategy(), new WinDrawLossFunction(), iSearchParameters);
                break;
            case MCE_TR_P:
                result = new TinyMonteCarloEvaluation(new TinyAllMoves(), new TinyTrueRandomSimulationStrategy(), new PlayerScoreFunction(), iSearchParameters);
                break;
            case MCE_TR_R:
                result = new TinyMonteCarloEvaluation(new TinyAllMoves(), new TinyTrueRandomSimulationStrategy(), new RelativeScoreFunction(), iSearchParameters);
                break;
            case MCE_FG_R:
                result = new TinyMonteCarloEvaluation(new TinyMaxScoringMoves(), new TinyFullGreedySimulationStrategy(), new RelativeScoreFunction(), iSearchParameters);
                break;
            case MCE_EG_R:
                result = new TinyMonteCarloEvaluation(new TinyMaxScoringMoves(), new TinyEpsilonGreedySelectionStrategy(0.2), new RelativeScoreFunction(), iSearchParameters);
                break;
            case MCTS_TR:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters);
                break;
            case MCTS_FG:
                result = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy(), iSearchParameters);
                break;
            default:
                assert false : "Unknown game strategy!";
                result = null;
        }

        return result;
    }
}
