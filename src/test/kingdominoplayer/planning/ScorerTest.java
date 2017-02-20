package kingdominoplayer.planning;

import com.sun.deploy.util.ArrayUtil;
import kingdominoplayer.ServerResponseParser;
import kingdominoplayer.utils.ArrayUtils;
import kingdominoplayer.utils.Util;
import kingdominoplayer.datastructures.PlacedTile;
import kingdominoplayer.plot.DebugPlot;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 14:09<br><br>
 */
public class ScorerTest
{

    @Test
    public void testScorer() throws Exception
    {
        final String gameState = getSampleGameState();

        DebugPlot.plotGameState(gameState, "TestRendering");

        final ArrayList<PlacedTile> placedTilesRandomCalrissian = ArrayUtils.toArrayList(ServerResponseParser.getPlayerPlacedTiles(gameState, "RandomCalrissian"));
        final ArrayList<PlacedTile> placedTilesDarthCrusader = ArrayUtils.toArrayList(ServerResponseParser.getPlayerPlacedTiles(gameState, "DarthCrusader"));

        final int scoreRandomCalrissian = Scorer.computeScore(placedTilesRandomCalrissian);
        final int scoreDarthCrusader = Scorer.computeScore(placedTilesDarthCrusader);

        Assert.assertEquals(scoreRandomCalrissian, 32);
        Assert.assertEquals(scoreDarthCrusader, 13);

        Util.noop();
    }



    private String getSampleGameState()
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
