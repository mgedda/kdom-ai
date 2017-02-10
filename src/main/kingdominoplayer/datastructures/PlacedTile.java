package kingdominoplayer.datastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 21:34<br><br>
 */
public class PlacedTile extends Tile
{
    private final Position iPosition;

    public PlacedTile(final Tile tile, final Position position)
    {
        super(tile.getTerrain(), tile.getCrowns());
        iPosition = position;
    }

    public Position getPosition()
    {
        return iPosition;
    }
}
