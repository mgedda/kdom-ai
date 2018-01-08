package kingdominoplayer.utils.plot;

/*
 * Copyright 2018 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2018-01-07<br>
 * Time: 23:04<br><br>
 */

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameStateTest;

import kingdominoplayer.utils.Util;
import org.testng.annotations.Test;

public class TinyDebugPlotTest
{
    @Test
    public void testPlotGameState()
    {
        TinyDebugPlot.plotGameState(getSampleGameState(), "Game State 1");
        TinyDebugPlot.plotGameState(TinyGameStateTest.getTinyGameState(DebugPlotTest.getSampleGameState()), "Game State 2");
        TinyDebugPlot.plotGameState(TinyGameStateTest.getTinyGameState(TinyGameStateTest.getSampleJSONGameState()), "Game State 3");

        Util.noop();
    }


    public static TinyGameState getSampleGameState()
    {
        final byte[] kingdomTerrains = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 5, 0, 0, 0, 0,
                0, 0, 0, 0, 4, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,

                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 2, 3, 0, 0,
                0, 0, 0, 0, 5, 6, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] kingdomCrowns = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 2, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,

                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 2, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] currentDraft = new byte[]{
                14, 3, 0, 4, 0, -1, -1, -1, -1, 0,
                18, 2, 0, 2, 0, -1, -1, -1, -1, 1,
                22, 5, 1, 6, 2, -1, -1, -1, -1, -1,
                39, 4, 0, 2, 0, -1, -1, -1, -1, 1,
        };

        final byte[] previousDraft = new byte[]{
                26, 3, 1, 4, 0, -1, -1, -1, -1, 0,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        };

        final String[] players = new String[]{
                "PlayerA",
                "PlayerB",
        };


        final byte[] drawPile = new byte[]{
                19, 3, 0, 7, 1, -1, -1, -1, -1,
                11, 5, 2, 3, 0, -1, -1, -1, -1,
                35, 6, 0, 3, 0, -1, -1, -1, -1,
                29, 4, 0, 5, 0, -1, -1, -1, -1,
        };

        return new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, drawPile, null);
    }
}
