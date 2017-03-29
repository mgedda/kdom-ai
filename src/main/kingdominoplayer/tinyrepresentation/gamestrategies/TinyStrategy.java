package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:12<br><br>
 */
public interface TinyStrategy
{
    byte[] selectMove(String playerName, byte[] availableMoves, TinyGameState gameState);

    default double getNumPlayoutsPerSecond()
    {
        return 0.0;
    }
}
