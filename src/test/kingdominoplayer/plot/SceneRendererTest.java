package kingdominoplayer.plot;

import kingdominoplayer.GameResponseParser;
import kingdominoplayer.datastructures.Kingdom;
import kingdominoplayer.datastructures.Position;
import kingdominoplayer.utils.Util;
import org.testng.annotations.Test;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 21:54<br><br>
 */
public class SceneRendererTest
{
    @Test
    public void testRenderer() throws Exception
    {
        SceneRenderer.render(getSampleGameState(), "TestRendering");

        Util.noop();
    }


    @Test
    public void testDrawKingdom() throws Exception
    {
        final GameState gameState = GameResponseParser.getGameStateObject(getSampleGameState());
        final Kingdom kingdom = gameState.getKingdomInfos().get(0).getKingdom();

        final GridImage gridImage = new GridImage(9, 9);
        SceneRenderer.drawKingdom(kingdom, new Position(4, 4), gridImage, true);

        BufferedImageViewer.displayImage(gridImage.toBufferedImage(), "Kingdom");

        Util.noop();
    }



    private String getSampleGameState()
    {
        return "{\n" +
                "  \"uuid\":\"50c6fcbb-feee-4b10-bf5e-d71948a52d12\",\n" +
                "  \"created\":\"2017-02-12T19:46:50.787Z\",\n" +
                "  \"updated\":\"2017-02-12T19:48:00.756Z\",\n" +
                "  \"kingdoms\":[{\n" +
                "    \"player\":{\n" +
                "      \"name\":\"ParvusPlus\"\n" +
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
                "        \"row\":-2,\n" +
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":0\n" +
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
                "        \"row\":-1,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
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
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
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
                "        \"row\":0,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":1\n" +
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
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    }],\n" +
                "    \"score\":{\n" +
                "      \"total\":43,\n" +
                "      \"areaScores\":[0,14,15,0,0,0,4,0],\n" +
                "      \"centerBonus\":10,\n" +
                "      \"completeBonus\":0\n" +
                "    }\n" +
                "  },{\n" +
                "    \"player\":{\n" +
                "      \"name\":\"TyrionPlannister\"\n" +
                "    },\n" +
                "    \"placedTiles\":[{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":2\n" +
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
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"mine\",\n" +
                "        \"crowns\":2\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"mine\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
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
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":2\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":1\n" +
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
                "        \"row\":-1,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"mine\",\n" +
                "        \"crowns\":2\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"mine\",\n" +
                "        \"crowns\":2\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"mine\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
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
                "    }],\n" +
                "    \"score\":{\n" +
                "      \"total\":64,\n" +
                "      \"areaScores\":[5,0,0,0,2,2,35,0,0,5,0,0],\n" +
                "      \"centerBonus\":10,\n" +
                "      \"completeBonus\":5\n" +
                "    }\n" +
                "  },{\n" +
                "    \"player\":{\n" +
                "      \"name\":\"TyrionPlannister2\"\n" +
                "    },\n" +
                "    \"placedTiles\":[{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":2\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":-1\n" +
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
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":1\n" +
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
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":1,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":2\n" +
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
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"clay\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"pasture\",\n" +
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
                "        \"row\":0,\n" +
                "        \"col\":2\n" +
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
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":1\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":1\n" +
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
                "        \"row\":-2,\n" +
                "        \"col\":-2\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":2,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":0,\n" +
                "        \"col\":1\n" +
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
                "      \"total\":63,\n" +
                "      \"areaScores\":[8,0,0,21,0,16,3,0,0,0],\n" +
                "      \"centerBonus\":10,\n" +
                "      \"completeBonus\":5\n" +
                "    }\n" +
                "  }],\n" +
                "  \"currentDraft\":{\n" +
                "    \"dominoes\":[]\n" +
                "  },\n" +
                "  \"previousDraft\":{\n" +
                "    \"dominoes\":[]\n" +
                "  },\n" +
                "  \"gameOver\":true,\n" +
                "  \"turn\":40\n" +
                "}";
    }


}
