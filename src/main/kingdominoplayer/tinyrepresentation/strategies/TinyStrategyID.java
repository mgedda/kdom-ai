package kingdominoplayer.tinyrepresentation.strategies;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:51<br><br>
 */
public enum TinyStrategyID
{
    FULL_GREEDY,
    MC_TR_TR_R,
    MC_FG_TR_R,
    MC_FG_FG_R;

    private TinyStrategy iStrategy;

    static
    {
        FULL_GREEDY.iStrategy = new TinyFullGreedy();
        MC_TR_TR_R.iStrategy = new TinyMonteCarlo(new TinyTrueRandom(), new TinyTrueRandom(), true);
        MC_FG_TR_R.iStrategy = new TinyMonteCarlo(new TinyFullGreedy(), new TinyTrueRandom(), true);
        MC_FG_FG_R.iStrategy = new TinyMonteCarlo(new TinyFullGreedy(), new TinyFullGreedy(), true);
    }

    public TinyStrategy getStrategy()
    {
        return iStrategy;
    }

}
