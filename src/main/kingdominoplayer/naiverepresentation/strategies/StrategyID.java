package kingdominoplayer.naiverepresentation.strategies;

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
    MC_TR_TR_R,
    MC_FG_TR_R,
    MC_FG_GPRD_R,
    MC_FG_FG_R,
    MC_FG_TR_P;

    private Strategy iStrategy;

    static {
        EXPAND.iStrategy = new Expand();
        TRUE_RANDOM.iStrategy = new TrueRandom();
        //RESTRICTED_RANDOM.iStrategy = new RestrictedRandom();
        GREEDY_PLACEMENT_RANDOM_DRAFT.iStrategy = new GreedyPlacementRandomDraft();
        BASE_PLAYER.iStrategy = new FullGreedy();
        FULL_GREEDY.iStrategy = new FullGreedy();
        MC_TR_TR_R.iStrategy = new MonteCarlo(new TrueRandom(), new TrueRandom(), true);
        MC_FG_TR_R.iStrategy = new MonteCarlo(new FullGreedy(), new TrueRandom(), true);
        MC_FG_GPRD_R.iStrategy = new MonteCarlo(new FullGreedy(), new GreedyPlacementRandomDraft(), true);
        MC_FG_FG_R.iStrategy = new MonteCarlo(new FullGreedy(), new FullGreedy(), true);
        MC_FG_TR_P.iStrategy = new MonteCarlo(new FullGreedy(), new TrueRandom(), false);
    }

    public Strategy getStrategy()
    {
        return iStrategy;
    }

}
