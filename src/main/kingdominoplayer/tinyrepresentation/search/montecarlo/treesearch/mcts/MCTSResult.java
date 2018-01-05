package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.mcts;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-21<br>
 * Time: 09:27<br><br>
 */
@SuppressWarnings("WeakerAccess")
/*package*/ class MCTSResult
{
    private final double[] iArray;

    public MCTSResult(final double[] array)
    {
        iArray = array;
    }

    public double getResult(final int playerIndex)
    {
        return iArray[playerIndex];
    }
}
