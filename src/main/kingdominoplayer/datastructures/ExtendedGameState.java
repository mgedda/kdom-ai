package kingdominoplayer.datastructures;

import kingdominoplayer.plot.KingdomInfo;
import kingdominoplayer.utils.ArrayUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-19<br>
 * Time: 00:30<br><br>
 */
public class ExtendedGameState extends GameState
{
    /**
     * Dominoes left to draft.
     */
    private final Set<Domino> iDrawPile;

    /**
     * Dominoes added to kingdoms.
     */
    private final Set<PlacedDomino> iPlacedPile;

    /**
     * True if this game state is part of a search.
     *
     * When part of a search, the current draft is being generated from the draw pile.
     * If not part of a search, the game server will generate the current draft so it
     * will be set by a method call specifying what dominoes were placed in the
     * draft.
     */
    private boolean iIsSearching;

    public ExtendedGameState(final ArrayList<KingdomInfo> kingdomInfos,
                             final ArrayList<DraftElement> previousDraft,
                             final ArrayList<DraftElement> currentDraft,
                             final boolean isGameOver,
                             final Set<Domino> drawPile,
                             final Set<PlacedDomino> placedPile,
                             final boolean isSearching)
    {
        super(kingdomInfos, previousDraft, currentDraft, isGameOver);

        iDrawPile = drawPile;
        iPlacedPile = placedPile;
        iIsSearching = isSearching;
    }



    public ExtendedGameState makeMove(final String playerName, final Move move)
    {
        boolean isGameOver = false;

        /////////////////////////////////////////////////////////
        // Process placed domino.
        /////////////////////////////////////////////////////////

        final ArrayList<KingdomInfo> kingdomInfos = new ArrayList<>(iKingdomInfos.size());
        final ArrayList<DraftElement> previousDraft = new ArrayList<>(iPreviousDraft.size());
        final LinkedHashSet<PlacedDomino> placedPile = new LinkedHashSet<>(iPlacedPile.size() + 1);

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
                    final ArrayList<PlacedTile> placedTiles = ArrayUtils.toArrayList(kingdomInfo.getKingdom().getPlacedTiles());
                    placedTiles.addAll(placedDomino.getPlacedTiles());

                    final Kingdom kingdomWithDominoPlaced = new Kingdom(placedTiles.toArray(new PlacedTile[placedTiles.size()]));
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
                if (! draftElement.getDomino().equals(placedDomino))
                {
                    previousDraft.add(draftElement);
                }
            }

            // Put domino in placed pile.
            //
            placedPile.addAll(iPlacedPile);
            placedPile.add(placedDomino);


            // Check if game is over.
            //
            isGameOver = iCurrentDraft.isEmpty() && previousDraft.isEmpty();
        }
        else
        {
            // Kingdoms and previous draft remain unchanged.
            //
            kingdomInfos.addAll(iKingdomInfos);
            previousDraft.addAll(iPreviousDraft);
            placedPile.addAll(iPlacedPile);
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


        /////////////////////////////////////////////////////////
        // Handle finished selection
        /////////////////////////////////////////////////////////

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

            if (! iDrawPile.isEmpty())
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

            return new ExtendedGameState(kingdomInfos, previousDraft, currentDraft, isGameOver, drawPile, placedPile, iIsSearching);
        }
        else
        {
            return new ExtendedGameState(kingdomInfos, previousDraft, currentDraft, isGameOver, iDrawPile, placedPile, iIsSearching);
        }
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

        return new ExtendedGameState(iKingdomInfos, previousDraft, currentDraft, false, drawPile, iPlacedPile, iIsSearching);
    }


    private ArrayList<Integer> getRandomDominoNumbers(final int draftSize)
    {
        final ArrayList<Integer> draftedDominoNumbers = new ArrayList<>(draftSize);
        final ArrayList<Integer> dominoNumbersInDrawPile = getDominoNumbersInDrawPile();

        for (int i = 0; i < draftSize; ++i)
        {
            final int index = ThreadLocalRandom.current().nextInt(0, dominoNumbersInDrawPile.size());
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
}
