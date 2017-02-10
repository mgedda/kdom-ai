package datastructures;

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
}
