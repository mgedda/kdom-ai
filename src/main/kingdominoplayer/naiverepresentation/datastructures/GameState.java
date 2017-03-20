package kingdominoplayer.naiverepresentation.datastructures;

import kingdominoplayer.naiverepresentation.planning.Planner;

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


    public GameState(final ArrayList<KingdomInfo> kingdomInfos,
                     final ArrayList<DraftElement> previousDraft,
                     final ArrayList<DraftElement> currentDraft)
    {
        iKingdomInfos = kingdomInfos;
        iPreviousDraft = previousDraft;
        iCurrentDraft = currentDraft;
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
        return iPreviousDraft.isEmpty() && iCurrentDraft.isEmpty();
    }


    public int getNumPlayers()
    {
        return iKingdomInfos.size();
    }

    public LinkedHashSet<String> getPlayerNames()
    {
        final LinkedHashSet<String> playerNames = new LinkedHashSet<>(getNumPlayers());

        for (final KingdomInfo kingdomInfo : iKingdomInfos)
        {
            playerNames.add(kingdomInfo.getPlayerName());
        }

        return playerNames;
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

    public int getScore(final String playerName)
    {
        final Map<String, Integer> scores = getScores();
        return scores.get(playerName);
    }

    public Collection<PlacedTile> getPlacedTiles(final String name)
    {
        //noinspection ConstantConditions
        return getKingdom(name).getPlacedTiles();
    }

    public ArrayList<Domino> getPreviousDraft(final String name)
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


    public int getNumAvailableDominoesInCurrentDraft()
    {
        int counter = 0;

        for (final DraftElement draftElement : getCurrentDraft())
        {
            if (draftElement.getPlayerName() == null)
            {
                counter++;
            }
        }

        return counter;
    }

    public ArrayList<Move> getAvailableMoves(final String playerName)
    {
        final ArrayList<PlacedDomino> possiblePlacedDominoes = new ArrayList<>();
        final ArrayList<DraftElement> previousDraft = getPreviousDraft();

        if (! previousDraft.isEmpty())
        {
            final DraftElement draftElement = previousDraft.get(0);
            final boolean isPlayersTurn = draftElement.getPlayerName().equals(playerName);

            if (! isPlayersTurn)
            {
                return new ArrayList<>(0);
            }

            final Domino dominoToPlace = draftElement.getDomino();
            final Kingdom kingdom = getKingdom(playerName);
            final Set<DominoPosition> dominoPositions = Planner.getValidPositions(dominoToPlace, kingdom);

            for (final DominoPosition dominoPosition : dominoPositions)
            {
                possiblePlacedDominoes.add(new PlacedDomino(dominoToPlace, dominoPosition));
            }
        }


        final ArrayList<Domino> possibleChosenDominoes = new ArrayList<>(4);
        final ArrayList<DraftElement> currentDraft = getCurrentDraft();

        if (! currentDraft.isEmpty())
        {
            for (final DraftElement draftElement : currentDraft)
            {
                if (draftElement.getPlayerName() == null)
                {
                    possibleChosenDominoes.add(draftElement.getDomino());
                }
            }
        }


        final ArrayList<Move> availableMoves = new ArrayList<>();
        int moveCounter = 0;
        if (possibleChosenDominoes.isEmpty())
        {
            for (final PlacedDomino placedDomino : possiblePlacedDominoes)
            {
                availableMoves.add(new Move(moveCounter++, null, placedDomino));
            }

        }
        else if (possiblePlacedDominoes.isEmpty())
        {
            for (final Domino chosenDomino : possibleChosenDominoes)
            {
                availableMoves.add(new Move(moveCounter++, chosenDomino, null));
            }
        }
        else
        {
            for (final Domino chosenDomino : possibleChosenDominoes)
            {
                for (final PlacedDomino placedDomino : possiblePlacedDominoes)
                {
                    availableMoves.add(new Move(moveCounter++, chosenDomino, placedDomino));
                }
            }
        }

        return availableMoves;
    }



    private Kingdom getKingdom(final String playerName)
    {
        for (final KingdomInfo kingdomInfo : iKingdomInfos)
        {
            if (kingdomInfo.getPlayerName().equals(playerName))
            {
                return kingdomInfo.getKingdom();
            }
        }

        assert false : "Player '" + playerName + "' not found!";
        return null;
    }


    public int getPositionInCurrentDraft(final Domino domino)
    {
        if (domino == null)
        {
            return 0;
        }

        final ArrayList<DraftElement> currentDraft = getCurrentDraft();
        assert !currentDraft.isEmpty() : "No dominoes in current draft!";

        for (int i = 0; i < currentDraft.size(); ++i)
        {
            if (currentDraft.get(i).getDomino().equals(domino))
            {
                return i + 1;
            }
        }

        assert false : "Current draft does not contain domino";
        return -1;
    }
}
