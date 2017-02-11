package kingdominoplayer.datastructures;

import java.util.ArrayList;

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

    @Override
    public PlacedTile getTile1()
    {
        return new PlacedTile(super.getTile1(), iTile1Position);
    }

    @Override
    public PlacedTile getTile2()
    {
        return new PlacedTile(super.getTile2(), iTile2Position);
    }

    public ArrayList<PlacedTile> getPlacedTiles()
    {
        final ArrayList<PlacedTile> placedTiles = new ArrayList<>(2);

        placedTiles.add(getTile1());
        placedTiles.add(getTile2());

        return placedTiles;
    }
}
