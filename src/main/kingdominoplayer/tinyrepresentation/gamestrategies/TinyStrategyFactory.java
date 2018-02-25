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

            case UCT_TR_C0_0:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.0);
                break;
            case UCT_TR_C0_1:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.1);
                break;
            case UCT_TR_C0_2:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.2);
                break;
            case UCT_TR_C0_3:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.3);
                break;
            case UCT_TR_C0_4:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.4);
                break;
            case UCT_TR_C0_5:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.5);
                break;
            case UCT_TR_C0_6:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6);
                break;
            case UCT_TR_C1_0:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 1.0);
                break;
            case UCT_TR_C1_5:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 1.5);
                break;
            case UCT_TR_C2_0:
                result = new TinyMonteCarloTreeSearch(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 2.0);
                break;

            case UCT_FG_C0_0:
                result = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.0);
                break;
            case UCT_FG_C0_1:
                result = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.1);
                break;
            case UCT_FG_C0_2:
                result = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.2);
                break;
            case UCT_FG_C0_3:
                result = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.3);
                break;
            case UCT_FG_C0_4:
                result = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.4);
                break;
            case UCT_FG_C0_5:
                result = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.5);
                break;
            case UCT_FG_C0_6:
                result = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6);
                break;
            case UCT_FG_C1_0:
                result = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy(), iSearchParameters, 1.0);
                break;
            case UCT_FG_C1_5:
                result = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy(), iSearchParameters, 1.5);
                break;
            case UCT_FG_C2_0:
                result = new TinyMonteCarloTreeSearch(new TinyFullGreedySimulationStrategy(), iSearchParameters, 2.0);
                break;

            case UCT_EG_C0_6:
                result = new TinyMonteCarloTreeSearch(new TinyEpsilonGreedySimulationStrategy(0.75), iSearchParameters, 0.6);
                break;

            case UCT_PG_C0_6:
                result = new TinyMonteCarloTreeSearch(new TinyPlayerGreedySimulationStrategy(), iSearchParameters, 0.6);
                break;


            case UCTW_TR_C0_6_W0_0:
                result = new TinyMonteCarloTreeSearchPWB(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6, 0.0);
                break;
            case UCTW_TR_C0_6_W0_1:
                result = new TinyMonteCarloTreeSearchPWB(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6, 0.1);
                break;
            case UCTW_TR_C0_6_W0_2:
                result = new TinyMonteCarloTreeSearchPWB(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6, 0.2);
                break;
            case UCTW_TR_C0_6_W0_3:
                result = new TinyMonteCarloTreeSearchPWB(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6, 0.3);
                break;
            case UCTW_TR_C0_6_W0_4:
                result = new TinyMonteCarloTreeSearchPWB(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6, 0.4);
                break;
            case UCTW_TR_C0_6_W0_5:
                result = new TinyMonteCarloTreeSearchPWB(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6, 0.5);
                break;
            case UCTW_TR_C0_6_W0_6:
                result = new TinyMonteCarloTreeSearchPWB(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6, 0.6);
                break;
            case UCTW_TR_C0_6_W0_7:
                result = new TinyMonteCarloTreeSearchPWB(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6, 0.7);
                break;
            case UCTW_TR_C0_6_W0_8:
                result = new TinyMonteCarloTreeSearchPWB(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6, 0.8);
                break;
            case UCTW_TR_C0_6_W0_9:
                result = new TinyMonteCarloTreeSearchPWB(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6, 0.9);
                break;
            case UCTW_TR_C0_6_W1_0:
                result = new TinyMonteCarloTreeSearchPWB(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6, 1.0);
                break;

            case UCTW_FG_C0_6_W0_0:
                result = new TinyMonteCarloTreeSearchPWB(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6, 0.0);
                break;
            case UCTW_FG_C0_6_W0_1:
                result = new TinyMonteCarloTreeSearchPWB(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6, 0.1);
                break;
            case UCTW_FG_C0_6_W0_2:
                result = new TinyMonteCarloTreeSearchPWB(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6, 0.2);
                break;
            case UCTW_FG_C0_6_W0_3:
                result = new TinyMonteCarloTreeSearchPWB(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6, 0.3);
                break;
            case UCTW_FG_C0_6_W0_4:
                result = new TinyMonteCarloTreeSearchPWB(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6, 0.4);
                break;
            case UCTW_FG_C0_6_W0_5:
                result = new TinyMonteCarloTreeSearchPWB(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6, 0.5);
                break;
            case UCTW_FG_C0_6_W0_6:
                result = new TinyMonteCarloTreeSearchPWB(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6, 0.6);
                break;
            case UCTW_FG_C0_6_W0_7:
                result = new TinyMonteCarloTreeSearchPWB(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6, 0.7);
                break;
            case UCTW_FG_C0_6_W0_8:
                result = new TinyMonteCarloTreeSearchPWB(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6, 0.8);
                break;
            case UCTW_FG_C0_6_W0_9:
                result = new TinyMonteCarloTreeSearchPWB(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6, 0.9);
                break;
            case UCTW_FG_C0_6_W1_0:
                result = new TinyMonteCarloTreeSearchPWB(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6, 1.0);
                break;

            case UCTW_EG_C0_6_W0_1:
                result = new TinyMonteCarloTreeSearchPWB(new TinyEpsilonGreedySimulationStrategy(0.75), iSearchParameters, 0.6, 0.1);
                break;

            case UCTW_PG_C0_6_W0_1:
                result = new TinyMonteCarloTreeSearchPWB(new TinyPlayerGreedySimulationStrategy(), iSearchParameters, 0.6, 0.1);
                break;


            case UCTB_TR_C0_6_W0_1:
                result = new TinyMonteCarloTreeSearchPB(new TinyTrueRandomSimulationStrategy(), iSearchParameters, 0.6, 0.1);
                break;

            case UCTB_FG_C0_6_W0_1:
                result = new TinyMonteCarloTreeSearchPB(new TinyFullGreedySimulationStrategy(), iSearchParameters, 0.6, 0.1);
                break;

            case UCTB_EG_C0_6_W0_1:
                result = new TinyMonteCarloTreeSearchPB(new TinyEpsilonGreedySimulationStrategy(0.75), iSearchParameters, 0.6, 0.1);
                break;

            case UCTB_PG_C0_6_W0_1:
                result = new TinyMonteCarloTreeSearchPB(new TinyPlayerGreedySimulationStrategy(), iSearchParameters, 0.6, 0.1);
                break;
            default:
                assert false : "Unknown game strategy!";
                result = null;
        }

        return result;
    }
}
