package kingdominoplayer.naiverepresentation.datastructures;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-13<br>
 * Time: 22:01<br><br>
 */
public class KingdomDominoPositionPair
{
    private final Kingdom iKingdom;
    private final DominoPosition iDominoPosition;

    public KingdomDominoPositionPair(Kingdom kingdom, DominoPosition dominoPosition)
    {
        iKingdom = kingdom;
        iDominoPosition = dominoPosition;
    }

    public Kingdom getKingdom()
    {
        return iKingdom;
    }

    public DominoPosition getDominoPosition()
    {
        return iDominoPosition;
    }
}
