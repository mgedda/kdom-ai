package kingdominoplayer.search.tinysearch;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:35<br><br>
 */
/*package*/ class TinyMoveScorePair
{
    private final byte[] iMove;
    private final double iScore;

    public TinyMoveScorePair(final byte[] move, final double score)
    {
        iMove = move;
        iScore = score;
    }

    public byte[] getMove()
    {
        return iMove;
    }

    public double getScore()
    {
        return iScore;
    }
}
