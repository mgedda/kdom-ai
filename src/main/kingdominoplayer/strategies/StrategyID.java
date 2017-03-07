package kingdominoplayer.strategies;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-21<br>
 * Time: 21:36<br><br>
 */
public enum StrategyID
{
    TRUE_RANDOM,
    EXPAND,
    FULL_GREEDY,
    MONTE_CARLO;

    private Strategy iStrategy;

    static {
        EXPAND.iStrategy = new Expand();
        TRUE_RANDOM.iStrategy = new TrueRandom();
        FULL_GREEDY.iStrategy = new FullGreedy();
        MONTE_CARLO.iStrategy = new MonteCarlo();
    }

    public Strategy getStrategy()
    {
        return iStrategy;
    }

}
