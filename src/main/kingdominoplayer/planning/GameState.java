package kingdominoplayer.planning;

import kingdominoplayer.datastructures.DraftElement;
import kingdominoplayer.plot.KingdomInfo;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-12<br>
 * Time: 21:06<br><br>
 */
public class GameState
{
    private final ArrayList<KingdomInfo> iKingdomInfos;

    private final ArrayList<DraftElement> iPreviousDraft;
    private final ArrayList<DraftElement> iCurrentDraft;

    private final boolean iIsGameOver;

    public GameState(final ArrayList<KingdomInfo> kingdomInfos,
                     final ArrayList<DraftElement> previousDraft,
                     final ArrayList<DraftElement> currentDraft,
                     final boolean isGameOver)
    {
        iKingdomInfos = kingdomInfos;
        iPreviousDraft = previousDraft;
        iCurrentDraft = currentDraft;
        iIsGameOver = isGameOver;
    }

    public ArrayList<KingdomInfo> getKingdomInfos()
    {
        return iKingdomInfos;
    }

    public ArrayList<DraftElement> getPreviousDraft()
    {
        return iPreviousDraft;
    }

    public ArrayList<DraftElement> getCurrentDraft()
    {
        return iCurrentDraft;
    }

    public boolean isGameOver()
    {
        return iIsGameOver;
    }
}
