package kingdominoplayer.strategies;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-11<br>
 * Time: 20:19<br><br>
 */
public class MonteCarloLimitedOpponentTrueRandom extends MonteCarlo
{
    @Override
    protected Strategy getPlayerStrategy()
    {
        return new FullGreedy();
    }

    @Override
    protected Strategy getOpponentStrategy()
    {
        return new TrueRandom();
    }

    @Override
    protected boolean useRelativeBranchScore()
    {
        return false;
    }
}
