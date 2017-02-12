package kingdominoplayer.plot;

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



    private String getSampleGameState()
    {
        return "{\n" +
                "  \"uuid\":\"f65389d3-34b1-41ab-b225-78ad949c15b2\",\n" +
                "  \"created\":\"2017-02-10T16:08:31.992Z\",\n" +
                "  \"updated\":\"2017-02-10T16:19:36.212Z\",\n" +
                "  \"kingdoms\":[{\n" +
                "    \"player\":{\n" +
                "      \"name\":\"MrAwesomeGreedy-0\"\n" +
                "    },\n" +
                "    \"placedTiles\":[{\n" +
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
                "        \"row\":0,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"castle\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":1\n" +
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
                "      \"total\":11,\n" +
                "      \"areaScores\":[0,1,0,0],\n" +
                "      \"centerBonus\":10,\n" +
                "      \"completeBonus\":0\n" +
                "    }\n" +
                "  },{\n" +
                "    \"player\":{\n" +
                "      \"name\":\"MrAwesomeGreedy-0-Prime\"\n" +
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
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-2,\n" +
                "        \"col\":0\n" +
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
                "        \"terrain\":\"forest\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    }],\n" +
                "    \"score\":{\n" +
                "      \"total\":11,\n" +
                "      \"areaScores\":[0,1,0],\n" +
                "      \"centerBonus\":10,\n" +
                "      \"completeBonus\":0\n" +
                "    }\n" +
                "  },{\n" +
                "    \"player\":{\n" +
                "      \"name\":\"kdom-rusty-35936586-396e-4ed5-96ff-8941649eb36e\"\n" +
                "    },\n" +
                "    \"placedTiles\":[{\n" +
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
                "        \"row\":0,\n" +
                "        \"col\":-1\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"water\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    },{\n" +
                "      \"position\":{\n" +
                "        \"row\":-1,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"forest\",\n" +
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
                "        \"row\":-2,\n" +
                "        \"col\":0\n" +
                "      },\n" +
                "      \"tile\":{\n" +
                "        \"terrain\":\"field\",\n" +
                "        \"crowns\":0\n" +
                "      }\n" +
                "    }],\n" +
                "    \"score\":{\n" +
                "      \"total\":11,\n" +
                "      \"areaScores\":[0,0,0,1],\n" +
                "      \"centerBonus\":10,\n" +
                "      \"completeBonus\":0\n" +
                "    }\n" +
                "  }],\n" +
                "  \"currentDraft\":{\n" +
                "    \"dominoes\":[{\n" +
                "      \"player\":{\n" +
                "        \"name\":\"kdom-rusty-35936586-396e-4ed5-96ff-8941649eb36e\"\n" +
                "      },\n" +
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
                "      \"player\":{\n" +
                "        \"name\":\"MrAwesomeGreedy-0\"\n" +
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
                "      \"domino\":{\n" +
                "        \"number\":38,\n" +
                "        \"tile1\":{\n" +
                "          \"terrain\":\"field\",\n" +
                "          \"crowns\":0\n" +
                "        },\n" +
                "        \"tile2\":{\n" +
                "          \"terrain\":\"clay\",\n" +
                "          \"crowns\":1\n" +
                "        }\n" +
                "      }\n" +
                "    }]\n" +
                "  },\n" +
                "  \"previousDraft\":{\n" +
                "    \"dominoes\":[{\n" +
                "      \"player\":{\n" +
                "        \"name\":\"MrAwesomeGreedy-0-Prime\"\n" +
                "      },\n" +
                "      \"domino\":{\n" +
                "        \"number\":39,\n" +
                "        \"tile1\":{\n" +
                "          \"terrain\":\"pasture\",\n" +
                "          \"crowns\":0\n" +
                "        },\n" +
                "        \"tile2\":{\n" +
                "          \"terrain\":\"clay\",\n" +
                "          \"crowns\":1\n" +
                "        }\n" +
                "      }\n" +
                "    }]\n" +
                "  },\n" +
                "  \"currentPlayer\":{\n" +
                "    \"name\":\"MrAwesomeGreedy-0-Prime\"\n" +
                "  },\n" +
                "  \"gameOver\":false,\n" +
                "  \"turn\":9\n" +
                "}";
    }


}
