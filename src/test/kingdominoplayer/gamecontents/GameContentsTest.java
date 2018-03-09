package kingdominoplayer.gamecontents;

import kingdominoplayer.naiverepresentation.datastructures.Tile;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-18<br>
 * Time: 00:07<br><br>
 */
public class GameContentsTest
{

    @Test
    public void testNumDominoes() throws Exception
    {
        Assert.assertEquals(GameContents.getDominoes().size(), 48);
    }

    @Test
    public void testTileTypes() throws Exception
    {
        final ArrayList<Tile> tiles = GameContents.getTiles();

        Assert.assertEquals(Collections.frequency(tiles, new Tile("FIELD", 0)), 21);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("FIELD", 1)), 5);

        Assert.assertEquals(Collections.frequency(tiles, new Tile("WATER", 0)), 12);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("WATER", 1)), 6);

        Assert.assertEquals(Collections.frequency(tiles, new Tile("FOREST", 0)), 16);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("FOREST", 1)), 6);

        Assert.assertEquals(Collections.frequency(tiles, new Tile("PASTURE", 0)), 10);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("PASTURE", 1)), 2);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("PASTURE", 2)), 2);

        Assert.assertEquals(Collections.frequency(tiles, new Tile("CLAY", 0)), 6);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("CLAY", 1)), 2);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("CLAY", 2)), 2);

        Assert.assertEquals(Collections.frequency(tiles, new Tile("MINE", 0)), 1);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("MINE", 1)), 1);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("MINE", 2)), 3);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("MINE", 3)), 1);
    }



}
