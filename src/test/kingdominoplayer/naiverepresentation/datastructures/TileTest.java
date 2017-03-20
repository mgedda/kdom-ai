package kingdominoplayer.naiverepresentation.datastructures;

import org.junit.Assert;
import org.testng.annotations.Test;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-04<br>
 * Time: 19:59<br><br>
 */
public class TileTest
{
    @Test
    public void testEquals() throws Exception
    {
        Assert.assertEquals(new Tile("field", 1).equals(new Tile("field", 1)), true);
        Assert.assertEquals(new Tile("water", 1).equals(new Tile("field", 1)), false);
        Assert.assertEquals(new Tile("field", 1).equals(new Tile("field", 0)), false);
    }
}
