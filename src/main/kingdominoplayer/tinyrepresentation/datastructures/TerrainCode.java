package kingdominoplayer.tinyrepresentation.datastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-14<br>
 * Time: 15:22<br><br>
 */
public enum TerrainCode
{
    NONE,
    CASTLE,
    WATER,
    FOREST,
    FIELD,
    MINE,
    PASTURE,
    CLAY;

    private String name;

    static {
        NONE.name = "none";
        CASTLE.name = "castle";
        WATER.name = "water";
        FOREST.name = "forest";
        FIELD.name = "field";
        MINE.name = "mine";
        PASTURE.name = "pasture";
        CLAY.name = "clay";
    }

    public static byte from(final String terrain)
    {
        return (byte) TerrainCode.valueOf(terrain.toUpperCase()).ordinal();
    }

    public static String getName(final int index)
    {
        return TerrainCode.values()[index].name;
    }
}
