package kingdominoplayer.gamecontents;

import kingdominoplayer.naivedatastructures.Tile;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Copyright 2017 Tomologic AB<br>
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

        Assert.assertEquals(Collections.frequency(tiles, new Tile("field", 0)), 21);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("field", 1)), 5);

        Assert.assertEquals(Collections.frequency(tiles, new Tile("water", 0)), 12);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("water", 1)), 6);

        Assert.assertEquals(Collections.frequency(tiles, new Tile("forest", 0)), 16);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("forest", 1)), 6);

        Assert.assertEquals(Collections.frequency(tiles, new Tile("pasture", 0)), 10);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("pasture", 1)), 2);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("pasture", 2)), 2);

        Assert.assertEquals(Collections.frequency(tiles, new Tile("clay", 0)), 6);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("clay", 1)), 2);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("clay", 2)), 2);

        Assert.assertEquals(Collections.frequency(tiles, new Tile("mine", 0)), 1);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("mine", 1)), 1);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("mine", 2)), 3);
        Assert.assertEquals(Collections.frequency(tiles, new Tile("mine", 3)), 1);
    }



}
