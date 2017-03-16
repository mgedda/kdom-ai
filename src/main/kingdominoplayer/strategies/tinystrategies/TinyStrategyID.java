package kingdominoplayer.strategies.tinystrategies;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:51<br><br>
 */
public enum TinyStrategyID
{
    MC_TR_TR_R;

    private TinyStrategy iStrategy;

    static {
        MC_TR_TR_R.iStrategy = new TinyMonteCarlo();
    }

    public TinyStrategy getStrategy()
    {
        return iStrategy;
    }

}
