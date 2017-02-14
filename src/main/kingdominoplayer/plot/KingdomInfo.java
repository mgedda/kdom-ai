package kingdominoplayer.plot;

import kingdominoplayer.datastructures.Kingdom;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-12<br>
 * Time: 21:08<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class KingdomInfo
{
    private final Kingdom iKingdom;
    private final String iPlayerName;


    public KingdomInfo(final Kingdom kingdom, final String playerName)
    {
        iKingdom = kingdom;
        iPlayerName = playerName;
    }

    public Kingdom getKingdom()
    {
        return iKingdom;
    }

    public String getPlayerName()
    {
        return iPlayerName;
    }

    public int getScore()
    {
        return iKingdom.getScore();
    }
}
