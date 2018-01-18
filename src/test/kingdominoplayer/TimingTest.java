package kingdominoplayer;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import kingdominoplayer.gamecontents.GameContents;
import kingdominoplayer.naiverepresentation.datastructures.*;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategy;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategyFactory;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategyID;
import org.testng.annotations.Test;

import java.util.*;

import static kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategyID.*;

/**
 * User: zayenz<br/>
 * Date: 2018-01-08<br/>
 * Time: 16:10<br/>
 *
 *
 * This test is used as a simple timing test when making small changes. Given a new blank slate,
 * how much time does it take to run the different strategies.
 */
public class TimingTest
{
    @Test( enabled = false )
    public void timeAllStrategies()
    {
        final TinyStrategyID[] strategyIDS = TinyStrategyID.values();

        timeStrategies(strategyIDS);

    }

    @Test( enabled = false )
    public void timeSomeStrategies()
    {
        // Easily editable list of strategies to test
        final TinyStrategyID[] strategyIDS = {
                TRUE_RANDOM,
                GREEDY_PLACEMENT_RANDOM_DRAFT,
                FULL_GREEDY,
//                MCE_TR_WDL,
//                MCE_TR_P,
//                MCE_TR_R,
//                MCE_FG_R,
//                MCE_EG_R,
                MCE_PG_R,
//                MCTS_TR,
//                MCTS_FG
        };

        timeStrategies(strategyIDS);

    }

    @Test
    public void timeFullGreedyStrategy()
    {
        final TinyStrategyID[] strategyIDS = {
                FULL_GREEDY,
        };

        timeStrategies(strategyIDS);

    }

    @Test
    public void timeMonteCarloEvaluationPlayerGreedyStrategy()
    {
        final TinyStrategyID[] strategyIDS = {
                MCE_PG_R,
        };

        timeStrategies(strategyIDS);

    }

    /**
     * Runs a set of strategies several times, and reports the time it takes for each strategy. The settings used are
     * to limit the number of playouts to 100. Strategies are run on an empty initial state, with the player as the
     * first player, and tested 5 times taking the minimum as the best possible time.
     * 
     * @param strategyIDS Strategies to run
     */
    private void timeStrategies(final TinyStrategyID[] strategyIDS)
    {
        runTimingTests(strategyIDS, 1, 100);
    }

    /**
     * Runs a set of strategies several times, and reports the time it takes for each strategy. Strategies are run on an
     * empty initial state, with the player as the first player, and tested 5 times taking the minimum as the best
     * possible time.
     *
     * @param strategyIDS Strategies to run
     * @param defaultIterationsCount Number of iterations a normal strategy should run for
     * @param playouts The number of playouts to use as limit
     */
    private void runTimingTests(final TinyStrategyID[] strategyIDS, final int defaultIterationsCount, final int playouts)
    {
        final Object2IntMap<TinyStrategyID> speicalIterations = new Object2IntOpenHashMap<>(TinyStrategyID.values().length);
        speicalIterations.putIfAbsent(TRUE_RANDOM, 1000);
        speicalIterations.putIfAbsent(GREEDY_PLACEMENT_RANDOM_DRAFT, 1000);
        speicalIterations.putIfAbsent(FULL_GREEDY, 1000);

        final Object2DoubleMap<TinyStrategyID> timings = new Object2DoubleOpenHashMap<>(strategyIDS.length);

        final String playerName = "bar";
        final String playerUUID = "foo";
        final LocalGameState gameState = createGameState(playerName);
        final Move[] availableMoves = gameState.getAvailableMoves(playerName).toArray(new Move[0]);

        final SearchParameters searchParameters = new SearchParameters(playouts, 0);
        final TinyStrategyFactory tinyStrategyFactory = new TinyStrategyFactory(searchParameters);
        int dummy = 0;
        for (int round = 0; round < 5; ++round)
        {
            for (TinyStrategyID id : strategyIDS)
            {
                final int iterations = speicalIterations.getOrDefault(id, defaultIterationsCount);
                final TinyStrategy strategy = tinyStrategyFactory.getGameStrategy(id);
                final TinyPlayer player = new TinyPlayer(playerUUID, playerName, strategy, false);
                final long timeBefore = System.nanoTime();
                for (int iteration = 0; iteration < iterations; iteration++)
                {
                    final Move move = player.selectMove(availableMoves, gameState, 1);
                    dummy += move.getNumber();
                }
                final double elapsedTime = (System.nanoTime() - timeBefore) / iterations;
                final double previousBestTime = timings.getOrDefault(id, Double.MAX_VALUE);
                timings.put(id, Math.min(previousBestTime, elapsedTime));
            }
        }

        System.out.println("Dummy value for optimization defeating: " + dummy);
        System.out.printf("Using playouts limit %d%n", searchParameters.getMaxNumPlayouts());
        int width = 0;
        for (TinyStrategyID strategyID : strategyIDS)
        {
            width = Math.max(width, strategyID.name().length());
        }
        double totalStrategyTime = 0;
        for (TinyStrategyID strategyID : strategyIDS)
        {
            final double strategyRunTime = timings.getOrDefault(strategyID, Double.NaN) / 1E9;
            System.out.printf("Best time for %" + width + "s is %.8f%n", strategyID.name(), strategyRunTime);
            totalStrategyTime += strategyRunTime;
        }
        
        System.out.printf("\nTotal run time for all strategies is %.8f%n", totalStrategyTime);
    }

    /**
     *
     * @param playerName
     * @return Initial game state with playerName as one of the players
     */
    private LocalGameState createGameState(final String playerName)
    {
        final ArrayList<KingdomInfo> kingdomInfos = createKingdomInfos(playerName);
        final ArrayList<DraftElement> previousDraft = new ArrayList<>(0);
        final ArrayList<Domino> allDominoes = new ArrayList<>(GameContents.getDominoes());
        final ArrayList<DraftElement> currentDraft = new ArrayList<>(4);
        for (int i = 0; i < 4; i++)
        {
            currentDraft.add(new DraftElement(allDominoes.get(i), null));
        }
        final Set<Domino> drawPile = new HashSet<>(allDominoes.subList(4, allDominoes.size()));
        return new LocalGameState(kingdomInfos, previousDraft, currentDraft, playerName, drawPile, true);
    }

    /**
     *
     * @param playerName
     * @return Initial kingdoms, including one for a given playerName
     */
    private ArrayList<KingdomInfo> createKingdomInfos(final String playerName)
    {
        final String[] allPlayerNames = {playerName, "Opponent 1", "Opponent 2", "Opponent 3"};
        final ArrayList<KingdomInfo> kingdomInfos = new ArrayList<>(4);
        for (final String somePlayerName : allPlayerNames)
        {
            kingdomInfos.add(createEmptyKingdom(somePlayerName));
        }
        return kingdomInfos;
    }

    /**
     *
     * @param playerName
     * @return Empty kindgom for a playerName
     */
    private KingdomInfo createEmptyKingdom(final String playerName)
    {
        final PlacedTile castle = new PlacedTile(new Tile("CASTLE", 0), new Position(0, 0));
        final Kingdom kingdom = new Kingdom(Collections.singletonList(castle));
        return new KingdomInfo(kingdom, playerName);
    }
}
