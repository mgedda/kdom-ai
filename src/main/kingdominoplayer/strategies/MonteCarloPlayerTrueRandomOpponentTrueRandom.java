package kingdominoplayer.strategies;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 17:07<br><br>
 */
public class MonteCarloPlayerTrueRandomOpponentTrueRandom extends MonteCarlo
{
    @Override
    protected Strategy getPlayerStrategy()
    {
        return new TrueRandom();
    }

    @Override
    protected Strategy getOpponentStrategy()
    {
        return new TrueRandom();
    }

    @Override
    protected boolean useRelativeBranchScore()
    {
        return true;
    }
}
