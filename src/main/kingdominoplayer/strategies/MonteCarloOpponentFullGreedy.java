package kingdominoplayer.strategies;


/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-02<br>
 * Time: 17:04<br><br>
 */
public class MonteCarloOpponentFullGreedy extends MonteCarlo
{
    @Override
    protected Strategy getPlayerStrategy()
    {
        return new FullGreedy();
    }

    @Override
    protected Strategy getOpponentStrategy()
    {
        return new FullGreedy();
    }

    @Override
    protected boolean useRelativeBranchScore()
    {
        return true;
    }
}
