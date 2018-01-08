package kingdominoplayer.utils.plot;

import kingdominoplayer.naiverepresentation.datastructures.Tile;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 19:29<br><br>
 */
/*package*/ enum TileType
{
    UNOCCUPIED,
    CASTLE,
    WATER,
    FOREST,
    FIELD,
    MINE,
    PASTURE,
    CLAY;

    private int color;
    private int textColor;

    static {
        UNOCCUPIED.color = 0xFFeeeeee;  // light gray
        CASTLE.color = 0xFF000000;      // black
        WATER.color = 0xFF579ac5;       // blue
        FOREST.color = 0xFF30ad63;      // dark green
        FIELD.color = 0xFFf7dc6f;       // yellow
        MINE.color = 0xFF515a5a;        // dark gray
        PASTURE.color = 0xFFa9dfbf;     // light green
        CLAY.color = 0xFFf0b27a;        // brown

        UNOCCUPIED.textColor = 0xFF000000;
        CASTLE.textColor = 0xFFFFFFFF;
        WATER.textColor = 0xFF000000;
        FOREST.textColor = 0xFF000000;
        FIELD.textColor = 0xFF000000;
        MINE.textColor = 0xFFFFFFFF;
        PASTURE.textColor = 0xFF000000;
        CLAY.textColor = 0xFF000000;
    }

    public int getColor()
    {
        return color;
    }

    public int getTextColor()
    {
        return textColor;
    }

    public static TileType from(final Tile tile)
    {
        return valueOf(tile.getTerrain().toUpperCase());
    }

    public static TileType from(final int i)
    {
        return TileType.values()[i];
    }
}
