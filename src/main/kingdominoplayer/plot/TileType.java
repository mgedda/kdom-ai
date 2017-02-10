package kingdominoplayer.plot;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 19:29<br><br>
 */
public enum TileType
{
    CASTLE,
    WATER,
    FOREST,
    FIELD,
    MINE,
    PASTURE,
    CLAY,
    UNOCCUPIED;

    private int color;
    private int textColor;

    static {
        CASTLE.color = 0xFF000000;      // black
        WATER.color = 0xFF70BCFF;       // blue
        FOREST.color = 0xFF009102;      // dark green
        FIELD.color = 0xFF87ED34;       // light green
        MINE.color = 0xFF828282;        // dark gray
        PASTURE.color = 0xFFE6CF3C;     // orange
        CLAY.color = 0xFFA38612;        // brown
        UNOCCUPIED.color = 0xFFF0F0F0;  // light gray

        CASTLE.textColor = 0xFFFFFFFF;
        WATER.textColor = 0xFF000000;
        FOREST.textColor = 0xFFFFFFFF;
        FIELD.textColor = 0xFF000000;
        MINE.textColor = 0xFFFFFFFF;
        PASTURE.textColor = 0xFF000000;
        CLAY.textColor = 0xFFFFFFFF;
        UNOCCUPIED.textColor = 0xFF000000;
    }

    public int getColor()
    {
        return color;
    }

    public int getTextColor()
    {
        return textColor;
    }
}
