package kingdominoplayer.tinyrepresentation.datastructures;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-14<br>
 * Time: 15:22<br><br>
 */
public enum TerrainCode
{
    NONE("NONE"),
    CASTLE("CASTLE"),
    WATER("WATER"),
    FOREST("FOREST"),
    FIELD("FIELD"),
    MINE("MINE"),
    PASTURE("PASTURE"),
    CLAY("CLAY");

    private final String name;

    TerrainCode(final String name)
    {
        this.name = name;
    }

    public static byte from(final String terrain)
    {
        return (byte) TerrainCode.valueOf(terrain).ordinal();
    }

    public static String getName(final int index)
    {
        return TerrainCode.values()[index].name;
    }
}
