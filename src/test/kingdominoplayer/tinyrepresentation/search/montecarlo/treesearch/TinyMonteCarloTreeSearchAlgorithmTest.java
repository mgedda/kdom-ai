package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;


/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-21<br>
 * Time: 15:21<br><br>
 */
public class TinyMonteCarloTreeSearchAlgorithmTest
{
    @Test
    public void testGetResultArrayFromIndexedScores_draw() throws Exception
    {
        final double[] result = TinyMonteCarloTreeSearchAlgorithm.getResultArrayFromIndexedScores(new int[]{10, 10, 1, 1});

        Assert.assertEquals(Arrays.equals(result, new double[]{0.5, 0.5, 0, 0}), true );
    }

    @Test
    public void testGetResultArrayFromIndexedScores_win() throws Exception
    {
        final double[] result = TinyMonteCarloTreeSearchAlgorithm.getResultArrayFromIndexedScores(new int[]{10, 11, 1, 1});

        Assert.assertEquals(Arrays.equals(result, new double[]{0, 1.0, 0, 0}), true );
    }

}
