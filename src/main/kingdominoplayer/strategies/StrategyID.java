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
    LOOK_AHEAD_RANDOM,
    LOOK_AHEAD_MONTE_CARLO;

    private Strategy iStrategy;

    static {
        FIRST.iStrategy = new FirstStrategy();
        RANDOM.iStrategy = new RandomStrategy();
        MOST_CROWNS.iStrategy = new MostCrownsStrategy();
        WATER.iStrategy = new WaterStrategy();
        EXPAND.iStrategy = new ExpandStrategy();
        LOOK_AHEAD_RANDOM.iStrategy = new LookAheadRandom();
        LOOK_AHEAD_MONTE_CARLO.iStrategy = new LookAheadMonteCarlo();
    }

    public Strategy getStrategy()
    {
        return iStrategy;
    }

}
