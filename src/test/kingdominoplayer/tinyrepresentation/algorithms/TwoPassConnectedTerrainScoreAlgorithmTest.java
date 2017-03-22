package kingdominoplayer.tinyrepresentation.algorithms;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-22<br>
 * Time: 10:33<br><br>
 */
public class TwoPassConnectedTerrainScoreAlgorithmTest
{
    @Test
    public void testGetConnectedComponents_1() throws Exception
    {
        final byte[] kingdomTerrains = {
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 3, 0, 0, 0, 0,
                0, 0, 0, 3, 3, 0, 0, 0, 0,
                0, 0, 2, 2, 0, 3, 3, 0, 0,
                0, 0, 0, 2, 1, 3, 3, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] connectedComponents = new TwoPassConnectedTerrainScoreAlgorithm().getConnectedComponents((byte) 3, kingdomTerrains);


        byte[] labels = {
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 1, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 3, 3, 0, 0,
                0, 0, 0, 0, 0, 3, 3, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0};

        Assert.assertEquals(Arrays.equals(connectedComponents, labels), true);
    }

    @Test
    public void testGetConnectedComponents_2() throws Exception
    {
        final byte[] kingdomTerrains = {
                0, 2, 0, 2, 0, 2, 0, 2, 0,
                2, 2, 2, 2, 2, 2, 2, 2, 2,
                0, 0, 0, 0, 0, 0, 0, 0, 2,
                0, 2, 0, 2, 0, 2, 0, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2,
                0, 0, 0, 0, 0, 0, 0, 0, 2,
                0, 2, 0, 2, 0, 2, 0, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] connectedComponents = new TwoPassConnectedTerrainScoreAlgorithm().getConnectedComponents((byte) 2, kingdomTerrains);

        byte[] labels = {
                0, 1, 0, 1, 0, 1, 0, 1, 0,
                1, 1, 1, 1, 1, 1, 1, 1, 1,
                0, 0, 0, 0, 0, 0, 0, 0, 1,
                0, 1, 0, 1, 0, 1, 0, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1,
                0, 0, 0, 0, 0, 0, 0, 0, 1,
                0, 1, 0, 1, 0, 1, 0, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1,
                0, 0, 0, 0, 0, 0, 0, 0, 0};

        Assert.assertEquals(Arrays.equals(connectedComponents, labels), true);
    }

}
