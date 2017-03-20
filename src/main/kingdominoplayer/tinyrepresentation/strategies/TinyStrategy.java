package kingdominoplayer.tinyrepresentation.strategies;

import kingdominoplayer.tinyrepresentation.TinyGameState;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:12<br><br>
 */
public interface TinyStrategy
{
    byte[] selectMove(String playerName, byte[] availableMoves, TinyGameState gameState);
}
