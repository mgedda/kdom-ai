package kingdominoplayer.naivedatastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-09<br>
 * Time: 10:47<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class Tile
{

    private final String iTerrain;
    private final int iCrowns;

    public Tile(final String terrain, final int crowns)
    {
        iTerrain = terrain;
        iCrowns = crowns;
    }

    public String getTerrain()
    {
        return iTerrain;
    }

    public int getCrowns()
    {
        return iCrowns;
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

        final Tile tile = (Tile) o;

        if (iCrowns != tile.iCrowns)
        {
            return false;
        }
        return iTerrain != null ? iTerrain.equals(tile.iTerrain) : tile.iTerrain == null;
    }

    @Override
    public int hashCode()
    {
        int result = iTerrain != null ? iTerrain.hashCode() : 0;
        result = 31 * result + iCrowns;
        return result;
    }

    @Override
    public String toString()
    {
        return "Tile{" +
                "iTerrain='" + iTerrain + '\'' +
                ", iCrowns=" + iCrowns +
                '}';
    }
}
