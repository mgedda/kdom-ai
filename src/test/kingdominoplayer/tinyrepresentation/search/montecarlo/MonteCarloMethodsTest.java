package kingdominoplayer.tinyrepresentation.search.montecarlo;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;


/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-23<br>
 * Time: 14:00<br><br>
 */
public class MonteCarloMethodsTest
{
    @Test
    public void testGetResultArrayFromIndexedScores_draw() throws Exception
    {
        final double[] result = MonteCarloMethods.getWinDrawLossArrayFromIndexedScores(new int[]{10, 10, 1, 1});

        Assert.assertEquals(Arrays.equals(result, new double[]{0.5, 0.5, 0, 0}), true );
    }

    @Test
    public void testGetResultArrayFromIndexedScores_win() throws Exception
    {
        final double[] result = MonteCarloMethods.getWinDrawLossArrayFromIndexedScores(new int[]{10, 11, 1, 1});

        Assert.assertEquals(Arrays.equals(result, new double[]{0, 1.0, 0, 0}), true );
    }

}
