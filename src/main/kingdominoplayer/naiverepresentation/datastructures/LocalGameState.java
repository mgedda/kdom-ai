package kingdominoplayer.naiverepresentation.datastructures;

import kingdominoplayer.naiverepresentation.planning.Planner;
import kingdominoplayer.utils.plot.DebugPlot;
import kingdominoplayer.utils.Random;

import java.util.*;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-19<br>
 * Time: 00:30<br><br>
 */
public class LocalGameState extends GameState
{
    /**
     * Dominoes left to draft.
     */
    private final Set<Domino> iDrawPile;

    /**
     * True if this game state is part of a search.
     *
     * When part of a search, the current draft is being generated from the draw pile.
     * If not part of a search, the game server will generate the current draft so it
     * will be set by a method call specifying what dominoes were placed in the
     * draft.
     */
    private final boolean iIsSearching;


    public LocalGameState(final GameState gameState,
                          final Set<Domino> drawPile,
                          final boolean isSearching)
    {
        super(gameState.getKingdomInfos(), gameState.getPreviousDraft(), gameState.getCurrentDraft(), gameState.iCurrentPlayer);

        iDrawPile = drawPile;
        iIsSearching = isSearching;
    }

    public LocalGameState(final ArrayList<KingdomInfo> kingdomInfos,
                          final ArrayList<DraftElement> previousDraft,
                          final ArrayList<DraftElement> currentDraft,
                          final String currentPlayer,
                          final Set<Domino> drawPile,
                          final boolean isSearching)
    {
        super(kingdomInfos, previousDraft, currentDraft, currentPlayer);

        iDrawPile = drawPile;
        iIsSearching = isSearching;
    }


    public LocalGameState withSearchEnabled()
    {
        return new LocalGameState(iKingdomInfos, iPreviousDraft, iCurrentDraft, iCurrentPlayer, iDrawPile, true);
    }


    public String getPlayerTurn()
    {
        if (isGameOver())
        {
            assert false : "The game is over, so it is no ones turn!";
        }

        if (iCurrentPlayer == null)
        {
            if (getPreviousDraft().isEmpty())
            {
                final LinkedHashSet<String> playerNames = getPlayerNames();
                final ArrayList<DraftElement> currentDraft = getCurrentDraft();

                assert ! currentDraft.isEmpty() : "Both previous and current draft are empty!";

                for (final DraftElement draftElement : currentDraft)
                {
                    if (draftElement.getPlayerName() != null)
                    {
                        playerNames.remove(draftElement.getPlayerName());
                    }
                }

                assert ! playerNames.isEmpty() : "All players have chosen current draft but previous draft is empty!";

                if (playerNames.size() > 1)
                {
                    final int numNames = playerNames.size();
                    final ArrayList<String> playersLeft = new ArrayList<>(numNames);
                    playersLeft.addAll(playerNames);

                    final int randomNum = Random.getInt(numNames);
                    iCurrentPlayer = playersLeft.get(randomNum);
                }
                else
                {
                    iCurrentPlayer = playerNames.iterator().next();
                }
            }

            iCurrentPlayer = getPreviousDraft().get(0).getPlayerName();
        }

        return iCurrentPlayer;
    }


    public LocalGameState makeMove(final String playerName, final Move move)
    {
        //DEBUG_checkDominoAlreadyChosen(iCurrentDraft, playerName);

        /////////////////////////////////////////////////////////
        // Process placed domino.
        /////////////////////////////////////////////////////////

        final ArrayList<KingdomInfo> kingdomInfos = new ArrayList<>(iKingdomInfos.size());
        final ArrayList<DraftElement> previousDraft = new ArrayList<>(iPreviousDraft.size());

        final PlacedDomino placedDomino = move.getPlacedDomino();
        if (placedDomino != null)
        {
            // Player placed a domino.

            // Update kingdom.
            //
            for (final KingdomInfo kingdomInfo : iKingdomInfos)
            {
                if (kingdomInfo.getPlayerName().equals(playerName))
                {
                    final ArrayList<PlacedTile> placedTiles = new ArrayList<>(kingdomInfo.getKingdom().getPlacedTiles().size() + 2);
                    placedTiles.addAll(kingdomInfo.getKingdom().getPlacedTiles());
                    placedTiles.addAll(placedDomino.getPlacedTiles());

                    final Kingdom kingdomWithDominoPlaced = new Kingdom(placedTiles);
                    final KingdomInfo updatedKingdomInfo = new KingdomInfo(kingdomWithDominoPlaced, playerName);

                    kingdomInfos.add(updatedKingdomInfo);
                }
                else
                {
                    kingdomInfos.add(kingdomInfo);
                }
            }

            // Remove from previous draft
            //
            for (final DraftElement draftElement : iPreviousDraft)
            {
                if (draftElement.getDomino().getNumber() != placedDomino.getNumber())
                {
                    previousDraft.add(draftElement);
                }
            }
        }
        else
        {
            // Placed domino == null

            // Check if player had domino with no valid placement in draft.
            //
            if (! iPreviousDraft.isEmpty())
            {
                if (iPreviousDraft.get(0).getPlayerName().equals(playerName))
                {
                    // Player was unable to place the domino.
                    // Remove non-placeable domino from draft.
                    //
                    for (int i = 1; i < iPreviousDraft.size(); ++i)
                    {
                        final DraftElement draftElement = iPreviousDraft.get(i);
                        previousDraft.add(draftElement);
                    }
                }
                else
                {
                    // Previous draft remains unchanged.
                    //
                    previousDraft.addAll(iPreviousDraft);
                }

                // Kingdoms remain unchanged.
                //
                kingdomInfos.addAll(iKingdomInfos);
            }
            else
            {
                // Previous draft and Kingdoms remain unchanged.
                //
                previousDraft.addAll(iPreviousDraft);
                kingdomInfos.addAll(iKingdomInfos);
            }
        }



        /////////////////////////////////////////////////////////
        // Process chosen domino.
        /////////////////////////////////////////////////////////

        final Domino chosenDomino = move.getChosenDomino();

        final ArrayList<DraftElement> currentDraft = new ArrayList<>(iCurrentDraft.size());

        if (chosenDomino != null)
        {
            // Update current draft with chosen domino.
            //
            for (final DraftElement draftElement : iCurrentDraft)
            {
                if (draftElement.getDomino().equals(chosenDomino))
                {
                    currentDraft.add(new DraftElement(chosenDomino, playerName));
                }
                else
                {
                    currentDraft.add(draftElement);
                }
            }
        }
        else
        {
            currentDraft.addAll(iCurrentDraft);
        }



        //DEBUG_countPlayerAllocations(currentDraft);


        /////////////////////////////////////////////////////////
        // Handle finished selection
        /////////////////////////////////////////////////////////

        final LocalGameState result;

        if (iIsSearching && isCurrentDraftSelectionComplete(currentDraft))
        {
            // Move current draft to previous draft.
            //
            previousDraft.clear();
            previousDraft.addAll(currentDraft);
            currentDraft.clear();


            // Draw new current draft from draw pile.
            //
            final LinkedHashSet<Domino> drawPile = new LinkedHashSet<>(iDrawPile.size());
            drawPile.addAll(iDrawPile);

            if (!isDrawPileEmpty())
            {
                final int numDominoesToDraw = iDrawPile.size();

                final int draftSize = getDraftSize();
                assert numDominoesToDraw >= draftSize : "Not enough dominoes in draw pile for draft!";

                final ArrayList<Integer> draftedDominoNumbers = getRandomDominoNumbers(draftSize);
                final ArrayList<Domino> dominoesFromDrawPile = getDominoesFromDrawPileSorted(draftedDominoNumbers);

                drawPile.removeAll(dominoesFromDrawPile);

                for (final Domino domino : dominoesFromDrawPile)
                {
                    currentDraft.add(new DraftElement(domino, null));
                }
            }

            result = new LocalGameState(kingdomInfos, previousDraft, currentDraft, null, drawPile, iIsSearching);
        }
        else
        {
            result = new LocalGameState(kingdomInfos, previousDraft, currentDraft, null, iDrawPile, iIsSearching);
        }


        //sanityCheck(result);

        return result;
    }

    private void DEBUG_checkDominoAlreadyChosen(final ArrayList<DraftElement> currentDraft, final String player)
    {
        for (final DraftElement draftElement : currentDraft)
        {
            if (draftElement.getPlayerName() != null && draftElement.getPlayerName().equals(player))
            {
                assert false : "Player already has a chosen domino!";
            }
        }
    }


    private void DEBUG_countPlayerAllocations(final ArrayList<DraftElement> currentDraft)
    {
        final LinkedHashMap<String, Integer> playerCountMap = new LinkedHashMap<>(2 * getNumPlayers());

        for (final String player : getPlayerNames())
        {
            playerCountMap.put(player, 0);
        }

        for (final DraftElement draftElement : currentDraft)
        {
            final String player = draftElement.getPlayerName();
            if (player != null)
            {
                playerCountMap.put(player, playerCountMap.get(player) + 1);
            }
        }

        for (final int numAllocations : playerCountMap.values())
        {
            final int maxAllocations = getNumPlayers() == 2 ? 2 : 1;
            assert numAllocations <= maxAllocations : "Player has too many dominoes allocated!";
        }
    }


    public Set<Domino> getDrawPile()
    {
        return iDrawPile;
    }

    private boolean isDrawPileEmpty()
    {
        return iDrawPile.isEmpty();
    }


    private static void sanityCheck(final LocalGameState gameState)
    {
        if (! gameState.getPreviousDraft().isEmpty())
        {
            final int maxPreviousDraftCount = gameState.getNumPlayers() == 2 ? 2 : 1;

            for (final String playerName : gameState.getPlayerNames())
            {
                int previousDraftCount = 0;

                for (final DraftElement draftElement : gameState.getPreviousDraft())
                {
                    previousDraftCount = draftElement.getPlayerName().equals(playerName) ? previousDraftCount + 1 : previousDraftCount;
                }

                assert previousDraftCount <= maxPreviousDraftCount : "Player '" + playerName + "' has too many dominoes in previous draft!";
            }
        }
    }


    private KingdomInfo getKingdomInfo(final String playerName, final ArrayList<KingdomInfo> kingdomInfos)
    {
        for (final KingdomInfo kingdomInfo : kingdomInfos)
        {
            if (kingdomInfo.getPlayerName().equals(playerName))
            {
                return kingdomInfo;
            }
        }

        assert  false : "Player '" + playerName + "' has no kingdom!";

        return null;
    }


    public GameState makeNewCurrentDraft(final Collection<Integer> dominoNumbers)
    {
        final ArrayList<Domino> dominoesFromDrawPile = getDominoesFromDrawPileSorted(dominoNumbers);
        final LinkedHashSet<Domino> drawPile = new LinkedHashSet<>(iDrawPile.size());
        drawPile.addAll(iDrawPile);
        drawPile.removeAll(dominoesFromDrawPile);

        final ArrayList<DraftElement> currentDraft = new ArrayList<>(4);
        for (final Domino domino : dominoesFromDrawPile)
        {
            currentDraft.add(new DraftElement(domino, null));
        }

        final ArrayList<DraftElement> previousDraft = iCurrentDraft;

        return new LocalGameState(iKingdomInfos, previousDraft, currentDraft, null, drawPile, iIsSearching);
    }


    private ArrayList<Integer> getRandomDominoNumbers(final int draftSize)
    {
        final ArrayList<Integer> draftedDominoNumbers = new ArrayList<>(draftSize);
        final ArrayList<Integer> dominoNumbersInDrawPile = getDominoNumbersInDrawPile();

        for (int i = 0; i < draftSize; ++i)
        {
            final int index = Random.getInt(dominoNumbersInDrawPile.size());
            draftedDominoNumbers.add(dominoNumbersInDrawPile.get(index));
            dominoNumbersInDrawPile.remove(index);
        }
        return draftedDominoNumbers;
    }


    private ArrayList<Integer> getDominoNumbersInDrawPile()
    {
        final ArrayList<Integer> numbers = new ArrayList<>(iDrawPile.size());
        for (final Domino domino : iDrawPile)
        {
            numbers.add(domino.getNumber());
        }

        return numbers;
    }

    private boolean isCurrentDraftSelectionComplete(final ArrayList<DraftElement> currentDraft)
    {
        boolean isCurrentDraftSelectionComplete = true;
        for (final DraftElement draftElement : currentDraft)
        {
            if (draftElement.getPlayerName() == null)
            {
                isCurrentDraftSelectionComplete = false;
                break;
            }
        }

        return isCurrentDraftSelectionComplete;
    }

    private ArrayList<Domino> getDominoesFromDrawPileSorted(final Collection<Integer> numbers)
    {
        assert iDrawPile.size() >= numbers.size() : "Not enough dominoes in draw pile!";

        final ArrayList<Domino> dominoes = new ArrayList<>(numbers.size());

        for (final Domino domino : iDrawPile)
        {
            if (numbers.contains(domino.getNumber()))
            {
                dominoes.add(domino);
            }
        }

        assert dominoes.size() == numbers.size() : "Not all numbers were present in draw pile!";

        Collections.sort(dominoes, (Domino d1, Domino d2) ->
        {
            final int number1 = d1.getNumber();
            final int number2 = d2.getNumber();

            return number1 < number2 ? -1 : number2 < number1 ? 1 : 0;
        });

        return dominoes;
    }


    public void DEBUG_plot(final String title)
    {
        DebugPlot.plotGameState(this, title);
    }

    public String getPlayerTurnOrNull()
    {
        return iCurrentPlayer;
    }
}
