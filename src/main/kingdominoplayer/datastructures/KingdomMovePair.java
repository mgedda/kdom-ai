package kingdominoplayer.datastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:52<br><br>
 */
public class KingdomMovePair
{
    private final Kingdom iKingdom;
    private final Move iMove;

    public KingdomMovePair(Kingdom kingdom, Move move)
    {
        iKingdom = kingdom;

        iMove = move;
    }

    public Kingdom getKingdom()
    {
        return iKingdom;
    }

    public Move getMove()
    {
        return iMove;
    }
}
