package kingdominoplayer.search;

import kingdominoplayer.datastructures.Move;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-23<br>
 * Time: 22:26<br><br>
 */
/*package*/ class MoveScorePair
{
    private final Move iMove;
    private final double iScore;

    public MoveScorePair(final Move move, final double score)
    {
        iMove = move;
        iScore = score;
    }

    public Move getMove()
    {
        return iMove;
    }

    public double getScore()
    {
        return iScore;
    }
}
