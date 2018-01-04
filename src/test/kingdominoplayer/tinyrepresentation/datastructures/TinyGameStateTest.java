package kingdominoplayer.tinyrepresentation.datastructures;

import kingdominoplayer.ServerResponseParser;
import kingdominoplayer.naiverepresentation.datastructures.GameState;
import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.utils.plot.DebugPlotTest;
import kingdominoplayer.tinyrepresentation.TinyUtils;
import kingdominoplayer.tinyrepresentation.adapters.LocalGameStateToTinyGameStateAlgorithm;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-13<br>
 * Time: 23:24<br><br>
 */
public class TinyGameStateTest
{

    @Test
    public void testPrintKingdom() throws Exception
    {
        final String jsonGameState = DebugPlotTest.getSampleGameState();
        final TinyGameState tinyGameState = getTinyGameState(jsonGameState);

        System.out.println(tinyGameState.toString());
    }


    @Test
    public void testGetScore() throws Exception
    {
        final TinyGameState tinyGameState = getTinyGameState(getSampleJSONGameState());
        //System.out.println(tinyGameState.toString());

        Assert.assertEquals(tinyGameState.getScore("DarthCrusader"), 13);
        Assert.assertEquals(tinyGameState.getScore("RandomCalrissian"), 32);
    }

    @Test
    public void testPlayerTurn() throws Exception
    {
        final TinyGameState tinyGameState = getTinyGameState(getSampleJSONGameState());
        //System.out.println(tinyGameState.toString());

        Assert.assertEquals(tinyGameState.getPlayerTurn(), "DarthCrusader");
    }



    @Test
    public void testReorderDraft() throws Exception
    {
        final byte[] kingdomTerrains = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] kingdomCrowns = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] currentDraft = new byte[4 * TinyConst.DRAFT_ELEMENT_SIZE];
        final String[] players = new String[]{"PlayerA", "PlayerB"};

        final byte[] previousDraft = new byte[]{
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                20, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        final byte[] expected = new byte[]{
                20, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
        };

        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);

        tinyGameState.reorderDraft(previousDraft);
        //System.out.println(tinyGameState.toString());

        Assert.assertEquals(Arrays.equals(previousDraft, expected), true);
    }

    @Test
    public void testSortDraft() throws Exception
    {
        final byte[] kingdomTerrains = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] kingdomCrowns = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] currentDraft = new byte[4 * TinyConst.DRAFT_ELEMENT_SIZE];
        final String[] players = new String[]{"PlayerA", "PlayerB"};

        final byte[] previousDraft = new byte[]{
                3, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                4, 0, 0, 0, 0, 0, 0, 0, 0, 4,
                2, 0, 0, 0, 0, 0, 0, 0, 0, 2,
        };

        final byte[] expected = new byte[]{
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
                2, 0, 0, 0, 0, 0, 0, 0, 0, 2,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 3,
                4, 0, 0, 0, 0, 0, 0, 0, 0, 4,
        };

        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);

        tinyGameState.sortDraft(previousDraft);
        //System.out.println(tinyGameState.toString());

        Assert.assertEquals(Arrays.equals(previousDraft, expected), true);
    }

    @Test
    public void testGetPlayerIdFromDraft() throws Exception
    {
        final byte[] kingdomTerrains = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] kingdomCrowns = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] currentDraft = new byte[4 * TinyConst.DRAFT_ELEMENT_SIZE];
        final String[] players = new String[]{"PlayerA", "PlayerB"};

        final byte[] previousDraft = new byte[]{
                1, 0, 0, 0, 0, 0, 0, 0, 0, 11,
                2, 0, 0, 0, 0, 0, 0, 0, 0, 12,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 13,
                4, 0, 0, 0, 0, 0, 0, 0, 0, 14,
        };


        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);

        Assert.assertEquals(tinyGameState.getPlayerIdFromDraft(previousDraft, 0), 11);
        Assert.assertEquals(tinyGameState.getPlayerIdFromDraft(previousDraft, 1), 12);
        Assert.assertEquals(tinyGameState.getPlayerIdFromDraft(previousDraft, 2), 13);
        Assert.assertEquals(tinyGameState.getPlayerIdFromDraft(previousDraft, 3), 14);
    }

    @Test
    public void testClearElementInDraft() throws Exception
    {
        final byte[] kingdomTerrains = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] kingdomCrowns = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] currentDraft = new byte[4 * TinyConst.DRAFT_ELEMENT_SIZE];
        final String[] players = new String[]{"PlayerA", "PlayerB"};

        final byte[] previousDraft = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        final byte[] expected = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);

        tinyGameState.clearElementInDraft(previousDraft, 1);
        System.out.println(tinyGameState.toString());

        Assert.assertEquals(Arrays.equals(previousDraft, expected), true);
    }


    @Test
    public void testGetDominoFromDraft() throws Exception
    {
        final byte[] kingdomTerrains = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] kingdomCrowns = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] currentDraft = new byte[4 * TinyConst.DRAFT_ELEMENT_SIZE];
        final String[] players = new String[]{"PlayerA", "PlayerB"};

        final byte[] previousDraft = new byte[]{
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
                30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
        };

        final byte[] expected = new byte[]{20, 21, 22, 23, 24, 25, 26, 27, 28};

        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);

        final byte[] domino = tinyGameState.getDominoFromDraft(previousDraft, 2);

        Assert.assertEquals(Arrays.equals(domino, expected), true);
    }


    @Test
    public void testIsCurrentDraftSelectionComplete_false() throws Exception
    {
        final byte[] kingdomTerrains = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] kingdomCrowns = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] previousDraft = new byte[4 * TinyConst.DRAFT_ELEMENT_SIZE];
        final String[] players = new String[]{"PlayerA", "PlayerB"};

        final byte[] currentDraft = new byte[]{
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                20, 21, 22, 23, 24, 25, 26, 27, 28, -1,
                30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
        };

        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);

        final boolean currentDraftSelectionComplete = tinyGameState.isCurrentDraftSelectionComplete(currentDraft);

        Assert.assertEquals(currentDraftSelectionComplete, false);
    }

    @Test
    public void testIsCurrentDraftSelectionComplete_true() throws Exception
    {
        final byte[] kingdomTerrains = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] kingdomCrowns = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] previousDraft = new byte[4 * TinyConst.DRAFT_ELEMENT_SIZE];
        final String[] players = new String[]{"PlayerA", "PlayerB"};

        final byte[] currentDraft = new byte[]{
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
                30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
        };

        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);

        final boolean currentDraftSelectionComplete = tinyGameState.isCurrentDraftSelectionComplete(currentDraft);

        Assert.assertEquals(currentDraftSelectionComplete, true);
    }


    @Test
    public void testIsDraftEmpty_false() throws Exception
    {
        final byte[] kingdomTerrains = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] kingdomCrowns = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] previousDraft = new byte[4 * TinyConst.DRAFT_ELEMENT_SIZE];
        final String[] players = new String[]{"PlayerA", "PlayerB"};

        final byte[] currentDraft = new byte[]{
                0, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        };

        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);

        final boolean draftEmpty = tinyGameState.isDraftEmpty(currentDraft);

        Assert.assertEquals(draftEmpty, false);
    }

    @Test
    public void testIsDraftEmpty_true() throws Exception
    {
        final byte[] kingdomTerrains = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] kingdomCrowns = new byte[2 * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] previousDraft = new byte[4 * TinyConst.DRAFT_ELEMENT_SIZE];
        final String[] players = new String[]{"PlayerA", "PlayerB"};

        final byte[] currentDraft = new byte[]{
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        };

        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);

        final boolean draftEmpty = tinyGameState.isDraftEmpty(currentDraft);

        Assert.assertEquals(draftEmpty, true);
    }



    @Test
    public void testMakeMove() throws Exception
    {
        final byte[] kingdomTerrains = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,

                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] kingdomCrowns = new byte[]{
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
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] currentDraft = new byte[]{
                14, 3, 0, 4, 0, -1, -1, -1, -1, -1,
                18, 2, 0, 2, 0, -1, -1, -1, -1, -1,
                22, 5, 1, 6, 2, -1, -1, -1, -1, -1,
                39, 4, 0, 2, 0, -1, -1, -1, -1, -1,
        };

        final byte[] previousDraft = new byte[]{
                 2, 4, 1, 5, 2, -1, -1, -1, -1, 0,
                 8, 2, 0, 3, 0, -1, -1, -1, -1, 1,
                21, 5, 2, 6, 0, -1, -1, -1, -1, 1,
                26, 3, 0, 3, 0, -1, -1, -1, -1, 0,
        };

        final String[] players = new String[]{
                "PlayerA",
                "PlayerB",
        };


        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);


        final byte[] move = new byte[]{
                1,                                // move number
                22, 5, 1, 6, 2, -1, -1, -1, -1,   // chosen domino
                2, 4, 1, 5, 2, 0, -1, 0, -2,      // placed domino
        };

        final TinyGameState newTinyGameState = tinyGameState.makeMove("PlayerA", move);


        final byte[] expectedKingdomTerrains = new byte[]{
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
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] expectedKingdomCrowns = new byte[]{
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
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] expectedCurrentDraft = new byte[]{
                14, 3, 0, 4, 0, -1, -1, -1, -1, -1,
                18, 2, 0, 2, 0, -1, -1, -1, -1, -1,
                22, 5, 1, 6, 2, -1, -1, -1, -1, 0,
                39, 4, 0, 2, 0, -1, -1, -1, -1, -1,
        };

        final byte[] expectedPreviousDraft = new byte[]{
                8, 2, 0, 3, 0, -1, -1, -1, -1, 1,
                21, 5, 2, 6, 0, -1, -1, -1, -1, 1,
                26, 3, 0, 3, 0, -1, -1, -1, -1, 0,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        };


        Assert.assertEquals(Arrays.equals(newTinyGameState.getKingdomTerrains(), expectedKingdomTerrains), true);
        Assert.assertEquals(Arrays.equals(newTinyGameState.getKingdomCrowns(), expectedKingdomCrowns), true);
        Assert.assertEquals(Arrays.equals(newTinyGameState.getCurrentDraft(), expectedCurrentDraft), true);
        Assert.assertEquals(Arrays.equals(newTinyGameState.getPreviousDraft(), expectedPreviousDraft), true);
    }


    @Test
    public void testMakeMove_newDraft() throws Exception
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

        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, drawPile, null);

        final byte[] move = new byte[]{
                1,                                // move number
                22, 5, 1, 6, 2, -1, -1, -1, -1,   // chosen domino
                26, 3, 1, 4, 0, 1, 0, 1, 1,       // placed domino
        };

        final TinyGameState newTinyGameState = tinyGameState.makeMove("PlayerA", move);


        final byte[] expectedKingdomTerrains = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 5, 0, 0, 0, 0,
                0, 0, 0, 0, 4, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 3, 0, 0, 0,
                0, 0, 0, 0, 0, 4, 0, 0, 0,
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

        final byte[] expectedKingdomCrowns = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 2, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 1, 0, 0, 0,
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

        final byte[] expectedCurrentDraft = new byte[]{
                11, 5, 2, 3, 0, -1, -1, -1, -1, -1,
                19, 3, 0, 7, 1, -1, -1, -1, -1, -1,
                29, 4, 0, 5, 0, -1, -1, -1, -1, -1,
                35, 6, 0, 3, 0, -1, -1, -1, -1, -1,
        };

        final byte[] expectedPreviousDraft = new byte[]{
                14, 3, 0, 4, 0, -1, -1, -1, -1, 0,
                18, 2, 0, 2, 0, -1, -1, -1, -1, 1,
                22, 5, 1, 6, 2, -1, -1, -1, -1, 0,
                39, 4, 0, 2, 0, -1, -1, -1, -1, 1,
        };

        System.out.print(newTinyGameState.toString());

        Assert.assertEquals(Arrays.equals(newTinyGameState.getKingdomTerrains(), expectedKingdomTerrains), true);
        Assert.assertEquals(Arrays.equals(newTinyGameState.getKingdomCrowns(), expectedKingdomCrowns), true);
        Assert.assertEquals(Arrays.equals(newTinyGameState.getCurrentDraft(), expectedCurrentDraft), true);
        Assert.assertEquals(Arrays.equals(newTinyGameState.getPreviousDraft(), expectedPreviousDraft), true);
        Assert.assertEquals(newTinyGameState.getDrawPile().length == 0, true);
    }


    @Test
    public void testMakeMove_invalidMovesInCurrentDraft() throws Exception
    {
        final byte[] kingdomTerrains = {
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 2, 3, 5, 5, 5, 0, 0,
                0, 0, 4, 5, 4, 5, 5, 0, 0,
                0, 0, 4, 6, 1, 4, 2, 0, 0,
                0, 0, 6, 2, 7, 3, 4, 0, 0,
                0, 0, 7, 7, 7, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,

                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 5, 5, 5, 0, 0,
                0, 0, 4, 5, 4, 5, 5, 0, 0,
                0, 0, 4, 6, 1, 4, 2, 0, 0,
                0, 0, 6, 2, 7, 3, 4, 0, 0,
                0, 0, 7, 7, 7, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] kingdomCrowns = {
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

        final byte[] currentDraft = {
                14, 5, 0, 2, 0, -1, -1, -1, -1, 0,
                18, 2, 0, 5, 0, -1, -1, -1, -1, 1,
                22, 6, 1, 6, 2, -1, -1, -1, -1, -1,
                39, 6, 0, 2, 0, -1, -1, -1, -1, 1,
        };

        final byte[] previousDraft = {
                26, 5, 1, 4, 0, -1, -1, -1, -1, 1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        };

        final String[] players = new String[]{
                "PlayerA",
                "PlayerB",
        };


        final byte[] drawPile = new byte[]{
        };

        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, drawPile, null);

        final byte[] move = new byte[]{
                1,                                // move number
                22, 6, 1, 6, 2, -1, -1, -1, -1,   // chosen domino
                26, 5, 1, 4, 0, -1, -2, -2, -2,       // placed domino
        };

        final TinyGameState newTinyGameState = tinyGameState.makeMove("PlayerB", move);


        final byte[] expectedPreviousDraft = new byte[]{
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        };

        System.out.print(newTinyGameState.toString());

        Assert.assertEquals(Arrays.equals(newTinyGameState.getPreviousDraft(), expectedPreviousDraft), true);
        Assert.assertEquals(newTinyGameState.isGameOver(), true);
    }



    @Test
    public void testGetValidPositionsUnique() throws Exception
    {
        final TinyGameState tinyGameState = getEmptyTinyGameState("PlayerA", "PlayerB");


        final byte[] playerKingdomTerrains = {
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] dominoToPlace = {0, 3, 0, 2, 0, -99, -99, -99, -99};

        final byte[] validPositions = tinyGameState.getValidPositionsUnique(dominoToPlace, playerKingdomTerrains);

        System.out.println(TinyUtils.to2DArrayString(validPositions, TinyConst.DOMINOPOSITION_ELEMENT_SIZE));

        final int numValidPositions = validPositions.length / TinyConst.DOMINOPOSITION_ELEMENT_SIZE;
        Assert.assertEquals(numValidPositions, 24);
    }


    @Test
    public void testGetValidPositionsUnique_noSymmetry() throws Exception
    {
        final TinyGameState tinyGameState = getEmptyTinyGameState("PlayerA", "PlayerB");


        final byte[] playerKingdomTerrains = {
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] dominoToPlace = {0, 2, 0, 2, 0, -99, -99, -99, -99};

        final byte[] validPositions = tinyGameState.getValidPositionsUnique(dominoToPlace, playerKingdomTerrains);

        System.out.println(TinyUtils.to2DArrayString(validPositions, TinyConst.DOMINOPOSITION_ELEMENT_SIZE));

        final int numValidPositions = validPositions.length / TinyConst.DOMINOPOSITION_ELEMENT_SIZE;
        Assert.assertEquals(numValidPositions, 12);
    }


    @Test
    public void testGetValidPositionsUnique_noSymmetrySingleDomino() throws Exception
    {
        final TinyGameState tinyGameState = getEmptyTinyGameState("PlayerA", "PlayerB");


        final byte[] playerKingdomTerrains = {
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 2, 2, 2, 2, 2, 0, 0,
                0, 0, 2, 2, 2, 2, 2, 0, 0,
                0, 0, 2, 2, 1, 2, 2, 0, 0,
                0, 0, 2, 2, 2, 2, 0, 0, 0,
                0, 0, 2, 2, 2, 2, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] dominoToPlace = {0, 2, 0, 2, 0, -99, -99, -99, -99};

        final byte[] validPositions = tinyGameState.getValidPositionsUnique(dominoToPlace, playerKingdomTerrains);

        System.out.println(TinyUtils.to2DArrayString(validPositions, TinyConst.DOMINOPOSITION_ELEMENT_SIZE));

        final int numValidPositions = validPositions.length / TinyConst.DOMINOPOSITION_ELEMENT_SIZE;
        Assert.assertEquals(numValidPositions, 1);
    }



    @Test
    public void testGetAvailableMoves() throws Exception
    {
        final byte[] kingdomTerrains = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,

                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] kingdomCrowns = new byte[]{
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
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] currentDraft = new byte[]{
                14, 3, 0, 4, 0, -1, -1, -1, -1, -1,
                18, 2, 0, 2, 0, -1, -1, -1, -1, -1,
                22, 5, 1, 6, 2, -1, -1, -1, -1, -1,
                39, 4, 0, 2, 0, -1, -1, -1, -1, -1,
        };

        final byte[] previousDraft = new byte[]{
                2, 4, 1, 5, 2, -1, -1, -1, -1, 0,
                8, 2, 0, 3, 0, -1, -1, -1, -1, 1,
                21, 5, 2, 6, 0, -1, -1, -1, -1, 1,
                26, 3, 0, 3, 0, -1, -1, -1, -1, 0,
        };

        final String[] players = new String[]{
                "PlayerA",
                "PlayerB",
        };


        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);

        final byte[] availableMoves = tinyGameState.getAvailableMoves("PlayerA");

        System.out.println(TinyUtils.to2DArrayString(availableMoves, TinyConst.MOVE_ELEMENT_SIZE));

        final int numAvailableMoves = availableMoves.length / TinyConst.MOVE_ELEMENT_SIZE;
        Assert.assertEquals(numAvailableMoves, 4 * 24);    // 24 possible placements of previous draft domino, times 4 dominoes to choose from

        final byte[] move = new byte[TinyConst.MOVE_ELEMENT_SIZE];
        System.arraycopy(availableMoves, 0, move, 0, TinyConst.MOVE_ELEMENT_SIZE);
        final TinyGameState newTinyGameState = tinyGameState.makeMove("PlayerA", move);
        System.out.println(newTinyGameState);
    }


    @Test
    public void testGetAvailableMoves_lastRound_noValidPositions() throws Exception
    {
        final byte[] kingdomTerrains = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 2, 2, 2, 2, 2, 0, 0,
                0, 0, 2, 2, 2, 2, 2, 0, 0,
                0, 0, 2, 2, 1, 2, 2, 0, 0,
                0, 0, 2, 2, 2, 2, 2, 0, 0,
                0, 0, 2, 2, 2, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,

                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] kingdomCrowns = new byte[]{
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
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
        };

        final byte[] currentDraft = new byte[]{
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        };

        final byte[] previousDraft = new byte[]{
                2, 5, 0, 5, 0, -1, -1, -1, -1, 0,
                3, 5, 0, 5, 0, -1, -1, -1, -1, 1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        };

        final String[] players = new String[]{
                "PlayerA",
                "PlayerB",
        };


        final TinyGameState tinyGameState = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);

        final byte[] availableMoves = tinyGameState.getAvailableMoves("PlayerA");

        System.out.println(TinyUtils.to2DArrayString(availableMoves, TinyConst.MOVE_ELEMENT_SIZE));


        final byte[] expected = {0,
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
        };

        Assert.assertEquals(Arrays.equals(availableMoves, expected), true);



        final byte[] move = new byte[TinyConst.MOVE_ELEMENT_SIZE];
        System.arraycopy(availableMoves, 0, move, 0, TinyConst.MOVE_ELEMENT_SIZE);
        final TinyGameState newTinyGameState = tinyGameState.makeMove("PlayerA", move);
        System.out.println(newTinyGameState);
    }


    private TinyGameState getEmptyTinyGameState(final String... players)
    {
        assert players.length > 1 && players.length < 5 : "Invalid number of players!";

        final int numPlayers = players.length;
        final int draftSize = numPlayers == 3 ? 3 : 4;

        final byte[] kingdomTerrains = new byte[numPlayers * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] kingdomCrowns = new byte[numPlayers * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] previousDraft = new byte[draftSize * TinyConst.DRAFT_ELEMENT_SIZE];
        final byte[] currentDraft = new byte[draftSize * TinyConst.DRAFT_ELEMENT_SIZE];

        return new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], null);
    }


    private TinyGameState getTinyGameState(final String jsonGameState)
    {
        final GameState gameState = ServerResponseParser.getGameStateObject(jsonGameState);
        final LocalGameState localGameState = toLocalGameState(gameState);
        return new LocalGameStateToTinyGameStateAlgorithm().applyTo(localGameState);
    }

    private LocalGameState toLocalGameState(final GameState gameState)
    {
        return new LocalGameState(
                gameState,
                new LinkedHashSet<>(0),
                true);
    }


    private String getSampleJSONGameState()
    {
        return "{\n" +
                "  \"uuid\":\"f4b86cce-d21d-472d-878a-f762cdc4dee7\",\n" +
                "  \"created\":\"2017-02-11T13:02:48.114Z\",\n" +
                "  \"updated\":\"2017-02-11T13:06:54.108Z\",\n" +
                "  \"kingdoms\":[{\n" +
                "    \"player\":{\n" +
                "      \"name\":\"DarthCrusader\"\n" +
                "    },\n" +
                "    \"placedTiles\":[{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":-3\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":-4\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-3,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-4,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-4,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-3,\n" +
                "        \"col\":-4\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":-3\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-4,\n" +
                "        \"col\":-4\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-4,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":-3\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-4,\n" +
                "        \"col\":-3\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":-4\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-3,\n" +
                "        \"col\":-3\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":-4\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"castle\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-3,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-3,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    }],\n" +
                "    \"score\":{\n" +
                "      \"total\":13,\n" +
                "      \"areaScores\":[0,1,0,0,0,1,2,0,0,9],\n" +
                "      \"centerBonus\":0,\n" +
                "      \"completeBonus\":0\n" +
                "    }\n" +
                "  },{\n" +
                "    \"player\":{\n" +
                "      \"name\":\"RandomCalrissian\"\n" +
                "    },\n" +
                "    \"placedTiles\":[{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"mine\",\n" +
                "        \"crowns\":2\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"mine\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":2\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"mine\",\n" +
                "        \"crowns\":2\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"castle\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    }],\n" +
                "    \"score\":{\n" +
                "      \"total\":32,\n" +
                "      \"areaScores\":[2,0,2,0,3,0,1,2,2,6,0,0,4,0],\n" +
                "      \"centerBonus\":10,\n" +
                "      \"completeBonus\":0\n" +
                "    }\n" +
                "  }],\n" +
                "  \"currentDraft\":{\n" +
                "    \"dominoes\":[]\n" +
                "  },\n" +
                "  \"previousDraft\":{\n" +
                "    \"dominoes\":[{\n" +
                "      \"player\":{\n" +
                "        \"name\":\"DarthCrusader\"\n" +
                "      },\n" +
                "      \"domino\":{\n" +
                "        \"number\":26,\n" +
                "        \"tile1\":{\n" +
                "          \"terrain\":\"forest\",\n" +
                "          \"crowns\":1\n" +
                "        },\n" +
                "        \"tile2\":{\n" +
                "          \"terrain\":\"field\",\n" +
                "          \"crowns\":0\n" +
                "        }\n" +
                "      }\n" +
                "    },{\n" +
                "      \"player\":{\n" +
                "        \"name\":\"RandomCalrissian\"\n" +
                "      },\n" +
                "      \"domino\":{\n" +
                "        \"number\":40,\n" +
                "        \"tile1\":{\n" +
                "          \"terrain\":\"mine\",\n" +
                "          \"crowns\":1\n" +
                "        },\n" +
                "        \"tile2\":{\n" +
                "          \"terrain\":\"field\",\n" +
                "          \"crowns\":0\n" +
                "        }\n" +
                "      }\n" +
                "    }]\n" +
                "  },\n" +
                "  \"currentPlayer\":{\n" +
                "    \"name\":\"DarthCrusader\"\n" +
                "  },\n" +
                "  \"gameOver\":false,\n" +
                "  \"turn\":27\n" +
                "}";
    }
}
