package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:12<br><br>
 */
public interface TinyStrategy
{
    byte[] selectMove(String playerName, TinyGameState gameState);

    default double getNumPlayoutsPerSecond()
    {
        return 0.0;
    }
}
