package kingdominoplayer.tinyrepresentation.search.montecarlo;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:35<br><br>
 */
public class TinyMoveAverageScorePair
{
    private byte[] iMove;
    private double iAverageScore;
    private int iPlayouts;

    public TinyMoveAverageScorePair(final byte[] move)
    {
        iMove = move;
        iAverageScore = 0;
        iPlayouts = 0;
    }

    public byte[] getMove()
    {
        return iMove;
    }

    public double getAverageScore()
    {
        return iAverageScore;
    }

    public int getPlayouts()
    {
        return iPlayouts;
    }

    public void addScore(final double score)
    {
        iAverageScore = iAverageScore + (score - iAverageScore) / (iPlayouts + 1);
        iPlayouts++;
    }
}
