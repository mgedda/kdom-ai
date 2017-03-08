package kingdominoplayer.strategies;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-21<br>
 * Time: 21:36<br><br>
 */
public enum StrategyID
{
    EXPAND,
    TRUE_RANDOM,
    //RESTRICTED_RANDOM,
    GREEDY_PLACEMENT_RANDOM_DRAFT,
    BASE_PLAYER,                     // alias for FULL_GREEDY
    FULL_GREEDY,
    //MONTE_CARLO_OPPONENT_TRUE_RANDOM,
    //MONTE_CARLO_OPPONENT_GREEDY_PLACEMENT_RANDOM_DRAFT,
    MONTE_CARLO_OPPONENT_FULL_GREEDY;

    private Strategy iStrategy;

    static {
        EXPAND.iStrategy = new Expand();
        TRUE_RANDOM.iStrategy = new TrueRandom();
        //RESTRICTED_RANDOM.iStrategy = new RestrictedRandom();
        GREEDY_PLACEMENT_RANDOM_DRAFT.iStrategy = new GreedyPlacementRandomDraft();
        BASE_PLAYER.iStrategy = new FullGreedy();
        FULL_GREEDY.iStrategy = new FullGreedy();
        //MONTE_CARLO_OPPONENT_TRUE_RANDOM.iStrategy = new MonteCarloOpponentTrueRandom();
        //MONTE_CARLO_OPPONENT_GREEDY_PLACEMENT_RANDOM_DRAFT.iStrategy = new MonteCarloOpponentGreedyPlacementRandomDraft();
        MONTE_CARLO_OPPONENT_FULL_GREEDY.iStrategy = new MonteCarloOpponentFullGreedy();
    }

    public Strategy getStrategy()
    {
        return iStrategy;
    }

}
