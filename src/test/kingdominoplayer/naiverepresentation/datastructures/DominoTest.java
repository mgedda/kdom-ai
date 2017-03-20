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
        Assert.assertEquals(new Domino(1, new Tile("field", 0), new Tile("forest", 1)).equals(
                new Domino(1, new Tile("field", 0), new Tile("forest", 1))),
                true);

        Assert.assertEquals(new Domino(1, new Tile("field", 0), new Tile("forest", 1)).equals(
                new Domino(2, new Tile("field", 0), new Tile("forest", 1))),
                false);
        Assert.assertEquals(new Domino(1, new Tile("field", 0), new Tile("forest", 1)).equals(
                new Domino(1, new Tile("forest", 0), new Tile("forest", 1))),
                false);
        Assert.assertEquals(new Domino(1, new Tile("field", 0), new Tile("forest", 1)).equals(
                new Domino(1, new Tile("field", 1), new Tile("forest", 1))),
                false);
    }

}
