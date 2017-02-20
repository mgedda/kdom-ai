package kingdominoplayer.datastructures;

import kingdominoplayer.datastructures.*;
import kingdominoplayer.plot.KingdomInfo;

import java.util.*;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-12<br>
 * Time: 21:06<br><br>
 */
public class GameState
{
    /**
     * The kingdom for each player.
     */
    protected final ArrayList<KingdomInfo> iKingdomInfos;

    /**
     * The dominoes selected in the previous draft.
     */
    protected final ArrayList<DraftElement> iPreviousDraft;

    /**
     * The dominoes to choose from in the current draft.
     */
    protected final ArrayList<DraftElement> iCurrentDraft;

    protected final boolean iIsGameOver;


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


    public int getNumPlayers()
    {
        return iKingdomInfos.size();
    }

    protected int getDraftSize()
    {
        return getNumPlayers() == 3 ? 3 : 4;
    }

    public ArrayList<Domino> getDominoesInCurrentDraft()
    {
        final ArrayList<Domino> currentDraftDominoes = new ArrayList<>(getDraftSize());

        for (final DraftElement draftElement : iCurrentDraft)
        {
            currentDraftDominoes.add(draftElement.getDomino());
        }

        return currentDraftDominoes;
    }


    public Map<String, Integer> getScores()
    {
        final LinkedHashMap<String, Integer> playerNameToScoreMap = new LinkedHashMap<>(getNumPlayers());

        for (final KingdomInfo kingdomInfo : iKingdomInfos)
        {
            playerNameToScoreMap.put(kingdomInfo.getPlayerName(), kingdomInfo.getScore());
        }

        return playerNameToScoreMap;
    }

    public Collection<PlacedTile> getPlacedTiles(final String name)
    {
        for (final KingdomInfo kingdomInfo : iKingdomInfos)
        {
            if (kingdomInfo.getPlayerName().equals(name))
            {
                return kingdomInfo.getKingdom().getPlacedTiles();
            }
        }

        assert false : "Player '" + name + "' not found!";
        return null;
    }

    public Collection<Domino> getPreviousDraft(final String name)
    {
        final ArrayList<Domino> previousDraft = new ArrayList<>(2);

        for (final DraftElement draftElement : iPreviousDraft)
        {
            if (draftElement.getPlayerName() != null && draftElement.getPlayerName().equals(name))
            {
                previousDraft.add(draftElement.getDomino());
            }
        }

        return previousDraft;
    }

    public Collection<Domino> getCurrentDraft(final String name)
    {
        final ArrayList<Domino> currentDraft = new ArrayList<>(2);

        for (final DraftElement draftElement : iCurrentDraft)
        {
            if (draftElement.getPlayerName() != null && draftElement.getPlayerName().equals(name))
            {
                currentDraft.add(draftElement.getDomino());
            }
        }

        return currentDraft;
    }
}
