package kingdominoplayer.naiverepresentation.datastructures;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-04<br>
 * Time: 20:03<br><br>
 */
public class DominoTest
{
    @Test
    public void testEquals() throws Exception
    {
        Assert.assertEquals(new Domino(1, new Tile("FIELD", 0), new Tile("FOREST", 1)).equals(
                new Domino(1, new Tile("FIELD", 0), new Tile("FOREST", 1))),
                true);

        Assert.assertEquals(new Domino(1, new Tile("FIELD", 0), new Tile("FOREST", 1)).equals(
                new Domino(2, new Tile("FIELD", 0), new Tile("FOREST", 1))),
                false);
        Assert.assertEquals(new Domino(1, new Tile("FIELD", 0), new Tile("FOREST", 1)).equals(
                new Domino(1, new Tile("FOREST", 0), new Tile("FOREST", 1))),
                false);
        Assert.assertEquals(new Domino(1, new Tile("FIELD", 0), new Tile("FOREST", 1)).equals(
                new Domino(1, new Tile("FIELD", 1), new Tile("FOREST", 1))),
                false);
    }

}
