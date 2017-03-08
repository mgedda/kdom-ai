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
    MC_OPPONENT_TRUE_RANDOM,
    MC_OPPONENT_GREEDY_PLACEMENT_RANDOM_DRAFT,
    MC_OPPONENT_FULL_GREEDY;

    private Strategy iStrategy;

    static {
        EXPAND.iStrategy = new Expand();
        TRUE_RANDOM.iStrategy = new TrueRandom();
        //RESTRICTED_RANDOM.iStrategy = new RestrictedRandom();
        GREEDY_PLACEMENT_RANDOM_DRAFT.iStrategy = new GreedyPlacementRandomDraft();
        BASE_PLAYER.iStrategy = new FullGreedy();
        FULL_GREEDY.iStrategy = new FullGreedy();
        MC_OPPONENT_TRUE_RANDOM.iStrategy = new MonteCarloOpponentTrueRandom();
        MC_OPPONENT_GREEDY_PLACEMENT_RANDOM_DRAFT.iStrategy = new MonteCarloOpponentGreedyPlacementRandomDraft();
        MC_OPPONENT_FULL_GREEDY.iStrategy = new MonteCarloOpponentFullGreedy();
    }

    public Strategy getStrategy()
    {
        return iStrategy;
    }

}
