package kingdominoplayer.datastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:41<br><br>
 */
public class Kingdom
{
    private final PlacedTile[] iPlacedTiles;

    public Kingdom(final PlacedTile[] placedTiles)
    {
        iPlacedTiles = placedTiles;
    }

    public PlacedTile[] getPlacedTiles()
    {
        return iPlacedTiles;
    }
}
