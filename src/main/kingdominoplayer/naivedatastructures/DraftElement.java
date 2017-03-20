package kingdominoplayer.naivedatastructures;

import com.sun.istack.internal.Nullable;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 23:14<br><br>
 */
public class DraftElement
{
    private final Domino iDomino;
    private final String iPlayerName;

    public DraftElement(final Domino domino, @Nullable final String playerName)
    {
        iDomino = domino;
        iPlayerName = playerName;
    }

    public Domino getDomino()
    {
        return iDomino;
    }

    public String getPlayerName()
    {
        return iPlayerName;
    }
}
