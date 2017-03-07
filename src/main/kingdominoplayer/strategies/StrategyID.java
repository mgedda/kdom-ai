package kingdominoplayer.strategies;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-21<br>
 * Time: 21:36<br><br>
 */
public enum StrategyID
{
    FIRST,
    RANDOM,
    MOST_CROWNS,
    WATER,
    EXPAND,
    GREEDY_RANDOM,
    MONTE_CARLO_GREEDY;

    private Strategy iStrategy;

    static {
        FIRST.iStrategy = new FirstStrategy();
        WATER.iStrategy = new WaterStrategy();
        MOST_CROWNS.iStrategy = new MostCrownsStrategy();
        EXPAND.iStrategy = new ExpandStrategy();
        RANDOM.iStrategy = new RandomStrategy();
        GREEDY_RANDOM.iStrategy = new GreedyRandom();
        MONTE_CARLO_GREEDY.iStrategy = new MonteCarloGreedy();
    }

    public Strategy getStrategy()
    {
        return iStrategy;
    }

}
