package datastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-09<br>
 * Time: 12:03<br><br>
 */
public class PlacedDomino extends Domino
{
    private final TilePosition iTile1Position;
    private final TilePosition iTile2Position;

    public PlacedDomino(final Domino domino, final TilePosition tile1Position, final TilePosition tile2Position)
    {
        super(domino.getNumber(), domino.getTile1(), domino.getTile2());
        iTile1Position = tile1Position;
        iTile2Position = tile2Position;
    }

    public TilePosition getTile1Position()
    {
        return iTile1Position;
    }

    public TilePosition getTile2Position()
    {
        return iTile2Position;
    }
}
