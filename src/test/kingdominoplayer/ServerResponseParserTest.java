package kingdominoplayer;

import kingdominoplayer.naiverepresentation.datastructures.Move;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-09<br>
 * Time: 11:01<br><br>
 */
public class ServerResponseParserTest
{
    @Test
    public void testIsGameOverFalse() throws Exception
    {
        final boolean isGameOver = ServerResponseParser.isGameOver("{\"gameOver\":false}");
        Assert.assertEquals(isGameOver, false);
    }

    @Test
    public void testIsGameOverTrue() throws Exception
    {
        final boolean isGameOver = ServerResponseParser.isGameOver("{\"gameOver\":true}");
        Assert.assertEquals(isGameOver, true);
    }

    @Test
    public void testGetAvailableMoves_simple() throws Exception
    {
        final String availableMoves = "{\"moves\":[" +
                " {\"number\":0, \"chosenDomino\":{\"number\":0, \"tile1\":{\"terrain\":\"water\", \"crowns\":0}, \"tile2\":{\"terrain\":\"forest\", \"crowns\":1}}}" +
                ",{\"number\":1, \"chosenDomino\":{\"number\":1, \"tile1\":{\"terrain\":\"field\", \"crowns\":2}, \"tile2\":{\"terrain\":\"pasture\", \"crowns\":0}}}" +
                ",{\"number\":2, \"chosenDomino\":{\"number\":2, \"tile1\":{\"terrain\":\"field\", \"crowns\":0}, \"tile2\":{\"terrain\":\"clay\", \"crowns\":0}}}" +
                "]}";

        final Move[] moves = ServerResponseParser.getAvailableMoves(availableMoves);

        Assert.assertEquals(moves.length, 3);
        Assert.assertEquals(moves[0].getNumber(), 0);
        Assert.assertEquals(moves[0].getChosenDomino().getNumber(), 0);
        Assert.assertEquals(moves[0].getChosenDomino().getTile1().getTerrain(), "water");
        Assert.assertEquals(moves[0].getChosenDomino().getTile1().getCrowns(), 0);
        Assert.assertEquals(moves[0].getChosenDomino().getTile2().getTerrain(), "forest");
        Assert.assertEquals(moves[0].getChosenDomino().getTile2().getCrowns(), 1);
    }


    @Test
    public void testGetAvailableMoves_onlyChosenDominoes() throws Exception
    {
        final String availableMoves = "{\n" +
                "  \"moves\":[{\n" +
                "    \"number\":0,\n" +
                "    \"chosenDomino\":{\n" +
                "      \"number\":7,\n" +
                "      \"tile1\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      },\n" +
                "      \"tile2\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    }\n" +
                "  },{\n" +
                "    \"number\":1,\n" +
                "    \"chosenDomino\":{\n" +
                "      \"number\":11,\n" +
                "      \"tile1\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":0\n" +
                "      },\n" +
                "      \"tile2\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    }\n" +
                "  },{\n" +
                "    \"number\":2,\n" +
                "    \"chosenDomino\":{\n" +
                "      \"number\":24,\n" +
                "      \"tile1\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":1\n" +
                "      },\n" +
                "      \"tile2\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    }\n" +
                "  },{\n" +
                "    \"number\":3,\n" +
                "    \"chosenDomino\":{\n" +
                "      \"number\":44,\n" +
                "      \"tile1\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":0\n" +
                "      },\n" +
                "      \"tile2\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":2\n" +
                "      }\n" +
                "    }\n" +
                "  }]\n" +
                "}\n";

        final Move[] moves = ServerResponseParser.getAvailableMoves(availableMoves);

        Assert.assertEquals(moves.length, 4);

        Assert.assertEquals(moves[0].getNumber(), 0);
        Assert.assertEquals(moves[1].getNumber(), 1);
        Assert.assertEquals(moves[2].getNumber(), 2);
        Assert.assertEquals(moves[3].getNumber(), 3);

        Assert.assertEquals(moves[3].getChosenDomino().getNumber(), 44);
        Assert.assertEquals(moves[3].getChosenDomino().getTile1().getTerrain(), "pasture");
        Assert.assertEquals(moves[3].getChosenDomino().getTile1().getCrowns(), 0);
        Assert.assertEquals(moves[3].getChosenDomino().getTile2().getTerrain(), "clay");
        Assert.assertEquals(moves[3].getChosenDomino().getTile2().getCrowns(), 2);

        Assert.assertEquals(moves[3].getPlacedDomino(), null);
    }


    @Test
    public void testGetPlayerScore() throws Exception
    {
        final String gameState = "{\n" +
                "  \"uuid\":\"4b8eafa2-1ff2-44da-99d0-d24b71c0cbfc\",\n" +
                "  \"created\":\"2017-02-08T08:01:34.917Z\",\n" +
                "  \"updated\":\"2017-02-08T08:03:26.044Z\",\n" +
                "  \"kingdoms\":[{\n" +
                "    \"player\":{\n" +
                "      \"name\":\"BB8\"\n" +
                "    },\n" +
                "    \"placedTiles\":[{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"castle\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    }],\n" +
                "    \"score\":{\n" +
                "      \"total\":12,\n" +
                "      \"areaScores\":[0],\n" +
                "      \"centerBonus\":10,\n" +
                "      \"completeBonus\":0\n" +
                "    }\n" +
                "  },{\n" +
                "    \"player\":{\n" +
                "      \"name\":\"test\"\n" +
                "    },\n" +
                "    \"placedTiles\":[{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"castle\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    }],\n" +
                "    \"score\":{\n" +
                "      \"total\":10,\n" +
                "      \"areaScores\":[0],\n" +
                "      \"centerBonus\":10,\n" +
                "      \"completeBonus\":0\n" +
                "    }\n" +
                "  }],\n" +
                "  \"currentDraft\":{\n" +
                "    \"dominoes\":[{\n" +
                "      \"domino\":{\n" +
                "        \"number\":7,\n" +
                "        \"tile1\":{\n" +
                "          \"terrain\":\"water\",\n" +
                "          \"crowns\":0\n" +
                "        },\n" +
                "        \"tile2\":{\n" +
                "          \"terrain\":\"water\",\n" +
                "          \"crowns\":0\n" +
                "        }\n" +
                "      }\n" +
                "    },{\n" +
                "      \"domino\":{\n" +
                "        \"number\":11,\n" +
                "        \"tile1\":{\n" +
                "          \"terrain\":\"pasture\",\n" +
                "          \"crowns\":0\n" +
                "        },\n" +
                "        \"tile2\":{\n" +
                "          \"terrain\":\"pasture\",\n" +
                "          \"crowns\":0\n" +
                "        }\n" +
                "      }\n" +
                "    },{\n" +
                "      \"domino\":{\n" +
                "        \"number\":24,\n" +
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
                "      \"domino\":{\n" +
                "        \"number\":44,\n" +
                "        \"tile1\":{\n" +
                "          \"terrain\":\"pasture\",\n" +
                "          \"crowns\":0\n" +
                "        },\n" +
                "        \"tile2\":{\n" +
                "          \"terrain\":\"clay\",\n" +
                "          \"crowns\":2\n" +
                "        }\n" +
                "      }\n" +
                "    }]\n" +
                "  },\n" +
                "  \"previousDraft\":{\n" +
                "    \"dominoes\":[]\n" +
                "  },\n" +
                "  \"currentPlayer\":{\n" +
                "    \"name\":\"BB8\"\n" +
                "  },\n" +
                "  \"gameOver\":false\n" +
                "}";

        final int playerScore = ServerResponseParser.getPlayerScore(gameState, "test");
        Assert.assertEquals(playerScore, 10);
    }


    @Test
    public void testGetCurrentPlayer() throws Exception
    {
        final String gameState = "{\n" +
                "  \"uuid\":\"4b8eafa2-1ff2-44da-99d0-d24b71c0cbfc\",\n" +
                "  \"currentPlayer\":{\n" +
                "    \"name\":\"BB8\"\n" +
                "  }" +
                "}";

        final String currentPlayer = ServerResponseParser.getCurrentPlayer(gameState);
        Assert.assertEquals(currentPlayer, "BB8");
    }

}
