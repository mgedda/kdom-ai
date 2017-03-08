package kingdominoplayer.strategies;


/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-08<br>
 * Time: 22:01<br><br>
 */
public class MonteCarloOpponentTrueRandom extends MonteCarlo
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
}
