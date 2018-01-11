package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.SearchParameters;
import kingdominoplayer.tinyrepresentation.movefilters.TinyAllMoves;
import kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation.PlayerScoreFunction;
import kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation.RelativeScoreFunction;
import kingdominoplayer.tinyrepresentation.search.montecarlo.evaluation.WinDrawLossFunction;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinyEpsilonGreedySimulationStrategy;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinyFullGreedySimulationStrategy;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinyPlayerGreedySimulationStrategy;
import kingdominoplayer.tinyrepresentation.simulationstrategies.TinyTrueRandomSimulationStrategy;

/*
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
                result = new TinyMonteCarloEvaluation(new TinyAllMoves(), new TinyFullGreedySimulationStrategy(), new RelativeScoreFunction(), iSearchParameters);
                break;
            case MCE_EG_R:
                result = new TinyMonteCarloEvaluation(new TinyAllMoves(), new TinyEpsilonGreedySimulationStrategy(0.75), new RelativeScoreFunction(), iSearchParameters);
                break;
            case MCE_PG_R:
                result = new TinyMonteCarloEvaluation(new TinyAllMoves(), new TinyPlayerGreedySimulationStrategy(), new RelativeScoreFunction(), iSearchParameters);
                break;
            case UCT_TR_C01:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.1);
                break;
            case UCT_TR_C02:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.2);
                break;
            case UCT_TR_C03:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.3);
                break;
            case UCT_TR_C04:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.4);
                break;
            case UCT_TR_C05:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.5);
                break;
            case UCT_TR_C10:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 1.0);
                break;
            case UCT_TR_C15:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 1.5);
                break;
            case UCT_TR_C20:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 2.0);
                break;
            case UCT_FG_C02:
                result = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.2);
                break;
            default:
                assert false : "Unknown game strategy!";
                result = null;
        }

        return result;
    }
}
