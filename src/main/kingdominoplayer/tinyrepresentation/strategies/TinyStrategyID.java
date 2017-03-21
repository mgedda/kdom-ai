package kingdominoplayer.tinyrepresentation.strategies;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:51<br><br>
 */
public enum TinyStrategyID
{
    TRUE_RANDOM,
    GREEDY_PLACEMENT_RANDOM_DRAFT,
    FULL_GREEDY,
    MC_TR_TR_R,
    MC_FG_TR_R,
    MC_FG_FG_R,
    MCTS_TR_TR,
    MCTS_FG_FG;

    private TinyStrategy iStrategy;

    static
    {
        TRUE_RANDOM.iStrategy = new TinyTrueRandom();
        GREEDY_PLACEMENT_RANDOM_DRAFT.iStrategy = new TinyGreedyPlacementRandomDraft();
        FULL_GREEDY.iStrategy = new TinyFullGreedy();
        MC_TR_TR_R.iStrategy = new TinyMonteCarlo(new TinyTrueRandom(), new TinyTrueRandom(), true);
        MC_FG_TR_R.iStrategy = new TinyMonteCarlo(new TinyFullGreedy(), new TinyTrueRandom(), true);
        MC_FG_FG_R.iStrategy = new TinyMonteCarlo(new TinyFullGreedy(), new TinyFullGreedy(), true);
        MCTS_TR_TR.iStrategy = new TinyMonteCarloTreeSearch(new TinyTrueRandom(), new TinyTrueRandom());
        MCTS_FG_FG.iStrategy = new TinyMonteCarloTreeSearch(new TinyFullGreedy(), new TinyFullGreedy());
    }

    public TinyStrategy getStrategy()
    {
        return iStrategy;
    }

}
