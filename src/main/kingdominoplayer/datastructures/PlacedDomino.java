package kingdominoplayer.datastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-09<br>
 * Time: 12:03<br><br>
 */
public class PlacedDomino extends Domino
{
    private final Position iTile1Position;
    private final Position iTile2Position;

    public PlacedDomino(final Domino domino, final Position tile1Position, final Position tile2Position)
    {
        super(domino.getNumber(), domino.getTile1(), domino.getTile2());
        iTile1Position = tile1Position;
        iTile2Position = tile2Position;
    }

    public Position getTile1Position()
    {
        return iTile1Position;
    }

    public Position getTile2Position()
    {
        return iTile2Position;
    }
}
