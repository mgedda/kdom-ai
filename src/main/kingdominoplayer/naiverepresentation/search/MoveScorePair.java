package kingdominoplayer.naiverepresentation.search;

import kingdominoplayer.naiverepresentation.datastructures.Move;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
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
