package kingdominoplayer.naiverepresentation.datastructures;

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

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }

        final PlacedTile that = (PlacedTile) o;

        return iPosition.equals(that.iPosition) && getTerrain().equals(that.getTerrain()) && getCrowns() == that.getCrowns();
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + iPosition.hashCode();
        return result;
    }
}
