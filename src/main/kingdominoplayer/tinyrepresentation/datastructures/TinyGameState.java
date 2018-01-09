package kingdominoplayer.tinyrepresentation.datastructures;

import com.sun.istack.internal.Nullable;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import kingdominoplayer.tinyrepresentation.algorithms.TinyScorerAlgorithm;
import kingdominoplayer.tinyrepresentation.algorithms.TinyValidPositionsEfficientAlgorithm;
import kingdominoplayer.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-13<br>
 * Time: 15:29<br><br>
 *
 * Game state with efficient data representation.

 * <pre>
 *     byte[] iKingdomTerrains    (Type: KINGDOM_TERRAINS)
 *     byte[] iKingdomCrowns      (Type: KINGDOM_CROWNS)
 *     byte[] iCurrentDraft       (Type: DRAFT)
 *     byte[] iPreviousDraft      (Type: DRAFT)
 *     byte[] iDrawPile           (Type: DRAW_PILE)
 * </pre>
 *
 */
@SuppressWarnings("WeakerAccess")
public class TinyGameState
{
    private final byte[] iKingdomTerrains;
    private final byte[] iKingdomCrowns;
    private final byte[] iCurrentDraft;
    private final byte[] iPreviousDraft;
    private final byte[] iDrawPile;

    private final String[] iPlayers;
    private final byte iNumPlayers;
    private final byte iDraftDominoCount;  // 3 or 4 depending on the number of players.

    private final HashMap<String, Integer> iPlayerScoresMap = new LinkedHashMap<>(2 * 4);

    private String iPlayerTurn;
    private byte[] iAvailableMoves;

    public TinyGameState(final byte[] kingdomTerrains,
                         final byte[] kingdomCrowns,
                         final byte[] currentDraft,
                         final byte[] previousDraft,
                         final String[] players,
                         final byte[] drawPile,
                         @Nullable final String playerTurn)
    {
        iNumPlayers = (byte) players.length;
        iDraftDominoCount = iNumPlayers == 3 ? (byte) 3 : (byte) 4;

        assert kingdomTerrains.length == iNumPlayers * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE;
        assert kingdomCrowns.length == iNumPlayers * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE;
        assert currentDraft.length == iDraftDominoCount * TinyConst.DRAFT_ELEMENT_SIZE;
        assert previousDraft.length == iDraftDominoCount * TinyConst.DRAFT_ELEMENT_SIZE;
        assert drawPile.length % TinyConst.DOMINO_SIZE == 0 : "Inconsistent draw pile size!";
        assert drawPile.length % iDraftDominoCount == 0 : "Inconsistent draw pile size!";

        iKingdomTerrains = kingdomTerrains;
        iKingdomCrowns = kingdomCrowns;
        iCurrentDraft = currentDraft;
        iPreviousDraft = previousDraft;
        iPlayers = players;
        iDrawPile = drawPile;
        iPlayerTurn = playerTurn;

        iAvailableMoves = null;
    }


    /*package*/ byte[] getKingdomTerrains()
    {
        return iKingdomTerrains;
    }

    /*package*/ byte[] getKingdomCrowns()
    {
        return iKingdomCrowns;
    }

    public byte[] getCurrentDraft()
    {
        return iCurrentDraft;
    }

    public byte[] getPreviousDraft()
    {
        return iPreviousDraft;
    }

    /*package*/ byte[] getDrawPile()
    {
        return iDrawPile;
    }

    /**
     * Get ID of player.
     *
     * @param playerName name of player
     * @return id of player
     */
    public static byte getPlayerID(final String playerName, final String[] playerNames)
    {
        for (byte i = 0; i < playerNames.length; ++i)
        {
            if (playerNames[i].equals(playerName))
            {
                return i;
            }
        }

        return TinyConst.INVALID_PLAYER_ID;
    }


    /**
     * Produce child state for move.
     *
     * @param playerName name of player making the move
     * @param move the move to make
     * @return updated game state after move
     */
    public TinyGameState makeMove(final String playerName, final byte[] move)
    {
        //final int moveNumber = move[MOVE_NUMBER_INDEX] & 0xFF;  // signed byte to unsigned value

        final byte[] chosenDomino = new byte[TinyConst.DOMINO_SIZE];
        System.arraycopy(move, 1, chosenDomino, 0, TinyConst.DOMINO_SIZE);

        final byte[] placedDomino = new byte[TinyConst.DOMINO_SIZE];
        System.arraycopy(move, 1 + TinyConst.DOMINO_SIZE, placedDomino, 0, TinyConst.DOMINO_SIZE);

        final byte playerID = getPlayerID(playerName, iPlayers);


        /////////////////////////////////////////////////////////
        // Process placed domino.
        /////////////////////////////////////////////////////////

        final byte[] kingdomTerrains = Arrays.copyOf(iKingdomTerrains, iKingdomTerrains.length);
        final byte[] kingdomCrowns = Arrays.copyOf(iKingdomCrowns, iKingdomCrowns.length);
        final byte[] previousDraft = Arrays.copyOf(iPreviousDraft, iPreviousDraft.length);

        if (placedDomino[0] != TinyConst.INVALID_DOMINO_VALUE)
        {
            // Player placed a domino.

            // Update kingdom.
            //
            final byte[] playerKingdomTerrains = getPlayerKingdomTerrains(playerName);
            final byte[] playerKingdomCrowns = getPlayerKingdomCrowns(playerName);

            final byte tile1Terrain = placedDomino[TinyConst.DOMINO_TILE_1_TERRAIN_INDEX];
            final byte tile1Crowns = placedDomino[TinyConst.DOMINO_TILE_1_CROWNS_INDEX];
            final byte tile2Terrain = placedDomino[TinyConst.DOMINO_TILE_2_TERRAIN_INDEX];
            final byte tile2Crowns = placedDomino[TinyConst.DOMINO_TILE_2_CROWNS_INDEX];

            final byte tile1X = placedDomino[TinyConst.DOMINO_TILE_1_X_INDEX];
            final byte tile1Y = placedDomino[TinyConst.DOMINO_TILE_1_Y_INDEX];
            final byte tile2X = placedDomino[TinyConst.DOMINO_TILE_2_X_INDEX];
            final byte tile2Y = placedDomino[TinyConst.DOMINO_TILE_2_Y_INDEX];

            final int tile1Index = tileCoordinateToLinearIndex(tile1X, tile1Y);
            final int tile2Index = tileCoordinateToLinearIndex(tile2X, tile2Y);

            playerKingdomTerrains[tile1Index] = tile1Terrain;
            playerKingdomCrowns[tile1Index] = tile1Crowns;
            playerKingdomTerrains[tile2Index] = tile2Terrain;
            playerKingdomCrowns[tile2Index] = tile2Crowns;

            System.arraycopy(playerKingdomTerrains, 0, kingdomTerrains, playerID * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE, playerKingdomTerrains.length);
            System.arraycopy(playerKingdomCrowns, 0, kingdomCrowns, playerID * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE, playerKingdomCrowns.length);


            // Remove from previous draft
            //
            final byte dominoID = placedDomino[0];

            for (int i = 0; i < iDraftDominoCount; ++i)
            {
                final int elementStartIndex = i * TinyConst.DRAFT_ELEMENT_SIZE;

                if (previousDraft[elementStartIndex] == dominoID)
                {
                    clearElementInDraft(previousDraft, i);
                    break;
                }
            }

            reorderDraft(previousDraft);
        }
        else
        {
            // Check if player had domino with no valid placement.
            //
            if (! isDraftEmpty(previousDraft))
            {
                if (previousDraft[TinyConst.DRAFT_ELEMENT_PLAYER_ID_INDEX] == playerID)
                {
                    // Player was unable to place the domino.
                    // Remove non-placeable domino from draft.
                    //
                    clearElementInDraft(previousDraft, 0);
                    reorderDraft(previousDraft);
                }
            }
        }


        /////////////////////////////////////////////////////////
        // Process chosen domino.
        /////////////////////////////////////////////////////////

        final byte[] currentDraft = Arrays.copyOf(iCurrentDraft, iCurrentDraft.length);

        if (chosenDomino[0] != TinyConst.INVALID_DOMINO_VALUE)
        {
            final byte dominoId = chosenDomino[0];

            // Update current draft with chosen domino.
            //
            for (int i = 0; i < iDraftDominoCount; ++i)
            {
                final int elementStartIndex = i * TinyConst.DRAFT_ELEMENT_SIZE;
                if (currentDraft[elementStartIndex] == dominoId)
                {
                    currentDraft[elementStartIndex + TinyConst.DRAFT_ELEMENT_PLAYER_ID_INDEX] = playerID;
                    break;
                }
            }
        }


        /////////////////////////////////////////////////////////
        // Handle finished selection
        /////////////////////////////////////////////////////////

        final byte[] drawPile;

        if (isCurrentDraftSelectionComplete(currentDraft))
        {
            // Move current draft to previous draft.
            //
            System.arraycopy(currentDraft, 0, previousDraft, 0, currentDraft.length);
            Arrays.fill(currentDraft, TinyConst.INVALID_DOMINO_VALUE);


            if (! isDrawPileEmpty())
            {
                // Draw new current draft from draw pile.
                //
                assert iDrawPile.length >= iDraftDominoCount * TinyConst.DOMINO_SIZE : "Draw pile not big enough!";

                final Set<Integer> drawnIndices = new LinkedHashSet<>(8);  // max 4 elements
                for (int i = 0; i < iDraftDominoCount; ++i)
                {
                    final int numDrawPileElements = iDrawPile.length / TinyConst.DOMINO_SIZE;
                    int drawPileIndex = Random.getInt(numDrawPileElements);

                    while (drawnIndices.contains(drawPileIndex))
                    {
                        // index already drawn, draw new index
                        drawPileIndex = Random.getInt(numDrawPileElements);
                    }

                    // draw domino
                    System.arraycopy(iDrawPile, drawPileIndex * TinyConst.DOMINO_SIZE, currentDraft, i * TinyConst.DRAFT_ELEMENT_SIZE, TinyConst.DOMINO_SIZE);

                    drawnIndices.add(drawPileIndex);
                }

                sortDraft(currentDraft);


                // build new reduced draw pile
                //
                drawPile = new byte[iDrawPile.length - iDraftDominoCount * TinyConst.DOMINO_SIZE];

                int newDrawPileElementCounter = 0;
                for (int i = 0; i < iDrawPile.length / TinyConst.DOMINO_SIZE; ++i)
                {
                    if (! drawnIndices.contains(i))
                    {
                        System.arraycopy(iDrawPile, i * TinyConst.DOMINO_SIZE, drawPile, newDrawPileElementCounter * TinyConst.DOMINO_SIZE, TinyConst.DOMINO_SIZE);
                        newDrawPileElementCounter++;
                    }
                }
            }
            else
            {
                // No changes to draw pile.
                drawPile = iDrawPile;
            }
        }
        else
        {
            // No changes to draw pile.
            drawPile = iDrawPile;
        }

        @SuppressWarnings("UnnecessaryLocalVariable")
        final TinyGameState result = new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, iPlayers, drawPile, null);

        // TODO [gedda] IMPORTANT! : Sanity check result like in LocalGameState
        //sanityCheck(result);

        return result;
    }

    private boolean isDrawPileEmpty()
    {
        return iDrawPile.length == 0;
    }


    /*package*/ void sortDraft(final byte[] draft)
    {
        final ByteArrayList ids = new ByteArrayList(4);

        for (int i = 0; i < iDraftDominoCount; ++i)
        {
            ids.add(draft[i * TinyConst.DRAFT_ELEMENT_SIZE]);
        }

        ids.sort((Byte b1, Byte b2) ->
        {
            //noinspection CodeBlock2Expr
            return b1 < b2 ? -1 : b2 < b1 ? 1 : 0;
        });


        final byte[] temp = new byte[draft.length];
        int tempCounter = 0;

        for (final byte id : ids)
        {
            for (int i = 0; i < iDraftDominoCount; ++i)
            {
                if (draft[i * TinyConst.DRAFT_ELEMENT_SIZE] == id)
                {
                    System.arraycopy(draft, i * TinyConst.DRAFT_ELEMENT_SIZE, temp, tempCounter * TinyConst.DRAFT_ELEMENT_SIZE, TinyConst.DRAFT_ELEMENT_SIZE);
                    break;
                }
            }

            tempCounter++;
        }

        System.arraycopy(temp, 0, draft, 0, draft.length);
    }

    /**
     * Ensure that all empty draft slots are at the bottom.
     *
     * @param draft the draft
     */
    /*package*/ void reorderDraft(final byte[] draft)
    {
        final byte[] temp = new byte[draft.length];
        Arrays.fill(temp, TinyConst.INVALID_DOMINO_VALUE);

        int counter = 0;
        for (int i = 0; i < iDraftDominoCount; ++i)
        {
            final int elementStartIndex = i * TinyConst.DRAFT_ELEMENT_SIZE;
            if (draft[elementStartIndex] != TinyConst.INVALID_DOMINO_VALUE)
            {
                final int destPos = counter * TinyConst.DRAFT_ELEMENT_SIZE;
                System.arraycopy(draft, elementStartIndex, temp, destPos, TinyConst.DRAFT_ELEMENT_SIZE);
                counter++;
            }
        }

        System.arraycopy(temp, 0, draft, 0, draft.length);
    }


    /*package*/ void clearElementInDraft(final byte[] draft, final int elementIndex)
    {
        final int elementStartIndex = elementIndex * TinyConst.DRAFT_ELEMENT_SIZE;
        final int toIndex = elementStartIndex + TinyConst.DRAFT_ELEMENT_SIZE;
        Arrays.fill(draft, elementStartIndex, toIndex, TinyConst.INVALID_DOMINO_VALUE);
    }


    /*package*/ byte[] getDominoFromDraft(final byte[] draft, final int elementIndex)
    {
        final int dominoStartIndex = elementIndex * TinyConst.DRAFT_ELEMENT_SIZE;
        final byte[] domino = new byte[TinyConst.DOMINO_SIZE];
        System.arraycopy(draft, dominoStartIndex, domino, 0, TinyConst.DOMINO_SIZE);

        return domino;
    }

    /*package*/ byte getPlayerIdFromDraft(final byte[] draft, final int elementIndex)
    {
        final int elementStartIndex = elementIndex * TinyConst.DRAFT_ELEMENT_SIZE;
        final int playerIndex = elementStartIndex + TinyConst.DRAFT_ELEMENT_PLAYER_ID_INDEX;

        return draft[playerIndex];
    }


    /*package*/ boolean isCurrentDraftSelectionComplete(final byte[] currentDraft)
    {
        for (int i = 0; i < iDraftDominoCount; ++i)
        {
            final int draftElementIndex = i * TinyConst.DRAFT_ELEMENT_SIZE;

            if (currentDraft[draftElementIndex + TinyConst.DRAFT_ELEMENT_PLAYER_ID_INDEX] == TinyConst.INVALID_DOMINO_VALUE)
            {
                return false;
            }
        }

        return true;
    }


    /**
     * Get row from two-dimensional array.
     *
     * @param array the two-dimensional array
     * @param rowIndex the row index
     * @param rowSize the row size
     * @return one row from a two-dimensional array
     */
    public static byte[] getRow(final byte[] array, final int rowIndex, final int rowSize)
    {
        final int rowStartIndex = rowIndex * rowSize;
        final byte[] element = new byte[rowSize];
        System.arraycopy(array, rowStartIndex, element, 0, rowSize);

        return element;
    }


    /**
     * Check if game is over.
     *
     * @return True if game is over, False otherwise
     */
    public boolean isGameOver()
    {
        return isDraftEmpty(iCurrentDraft) && isDraftEmpty(iPreviousDraft);
    }


    /**
     * Get player whose turn it is.
     *
     * @return name of player whose turn it is.
     */
    public String getPlayerTurn()
    {
        if (isGameOver())
        {
            //assert false : "The game is over, so it is no ones turn!";
            return "";
        }

        if (iPlayerTurn == null)
        {
            if (isDraftEmpty(iPreviousDraft))
            {
                assert ! isDraftEmpty(iCurrentDraft) : "Both previous and current draft are empty!";
                final ByteList playerIDs = getPlayerIDs();

                if (playerIDs.size() == 2)
                {
                    playerIDs.add((byte) 0);
                    playerIDs.add((byte) 1);
                }

                for (int i = 0; i < iDraftDominoCount; ++i)
                {
                    final byte playerId = iCurrentDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DRAFT_ELEMENT_PLAYER_ID_INDEX];

                    if (playerId != TinyConst.INVALID_PLAYER_ID)
                    {
                        playerIDs.remove(new Byte(playerId));
                    }
                }

                assert ! playerIDs.isEmpty() : "All players have chosen current draft but previous draft is empty!";

                if (playerIDs.size() > 1)
                {
                    final int numIDsToChoseFrom = playerIDs.size();

                    final int randomNum = Random.getInt(numIDsToChoseFrom);
                    final Byte playerIDTurn = playerIDs.get(randomNum);

                    iPlayerTurn = iPlayers[playerIDTurn];
                }
                else
                {
                    iPlayerTurn = iPlayers[playerIDs.iterator().next()];
                }
            }
            else
            {
                final byte playerID = iPreviousDraft[TinyConst.DRAFT_ELEMENT_PLAYER_ID_INDEX];   // player id of first draft element.
                iPlayerTurn = iPlayers[playerID];
            }
        }

        return iPlayerTurn;
    }


    private ByteList getPlayerIDs()
    {
        final ByteArrayList ids = new ByteArrayList();
        for (int i = 0; i < iNumPlayers; ++i)
        {
            ids.add((byte)i);
        }
        return ids;
    }

    public String[] getPlayers()
    {
        return iPlayers;
    }

    public byte getNumPlayers()
    {
        return iNumPlayers;
    }

    public boolean isPreviousDraftEmpty()
    {
        return isDraftEmpty(iPreviousDraft);
    }

    public boolean isCurrentDraftEmpty()
    {
        return isDraftEmpty(iCurrentDraft);
    }

    /*package*/ boolean isDraftEmpty(final byte[] draft)
    {
        return draft[0] == TinyConst.INVALID_DOMINO_VALUE;
    }


    /**
     * Get all possible moves.
     *
     * @return all available moves
     */
    public byte[] getAvailableMoves(final String playerName)
    {
        if (iAvailableMoves != null)
        {
            return iAvailableMoves;
        }

        final byte[] possiblePlacedDominoes;
        if (! isDraftEmpty(iPreviousDraft))
        {
            final byte playerId = iPreviousDraft[TinyConst.DRAFT_ELEMENT_PLAYER_ID_INDEX];

            if (! iPlayers[playerId].equals(playerName))
            {
                return new byte[0];
            }

            final byte[] dominoToPlace = getDominoFromDraft(iPreviousDraft, 0);
            final byte[] playerKingdomTerrains = getPlayerKingdomTerrains(playerName);

            final byte[] dominoPositions = getValidPositionsUnique(dominoToPlace, playerKingdomTerrains);
            final int numDominoPositions = dominoPositions.length / TinyConst.DOMINOPOSITION_ELEMENT_SIZE;

            possiblePlacedDominoes = new byte[Math.max(numDominoPositions, 1) * TinyConst.DOMINO_SIZE];
            Arrays.fill(possiblePlacedDominoes, TinyConst.INVALID_DOMINO_VALUE);

            for (int i = 0; i < numDominoPositions; ++i)
            {
                final int dominoPositionElementIndex = i * TinyConst.DOMINOPOSITION_ELEMENT_SIZE;

                final byte tile1X = dominoPositions[dominoPositionElementIndex + TinyConst.DOMINOPOSITION_TILE_1_X_INDEX];
                final byte tile1Y = dominoPositions[dominoPositionElementIndex + TinyConst.DOMINOPOSITION_TILE_1_Y_INDEX];
                final byte tile2X = dominoPositions[dominoPositionElementIndex + TinyConst.DOMINOPOSITION_TILE_2_X_INDEX];
                final byte tile2Y = dominoPositions[dominoPositionElementIndex + TinyConst.DOMINOPOSITION_TILE_2_Y_INDEX];

                final int placedDominoIndex = i * TinyConst.DOMINO_SIZE;

                System.arraycopy(dominoToPlace, 0, possiblePlacedDominoes, placedDominoIndex, TinyConst.DOMINO_SIZE);
                possiblePlacedDominoes[placedDominoIndex + TinyConst.DOMINO_TILE_1_X_INDEX] = tile1X;
                possiblePlacedDominoes[placedDominoIndex + TinyConst.DOMINO_TILE_1_Y_INDEX] = tile1Y;
                possiblePlacedDominoes[placedDominoIndex + TinyConst.DOMINO_TILE_2_X_INDEX] = tile2X;
                possiblePlacedDominoes[placedDominoIndex + TinyConst.DOMINO_TILE_2_Y_INDEX] = tile2Y;
            }
        }
        else
        {
            possiblePlacedDominoes = new byte[0];
        }


        final byte[] possibleChosenDominoes;
        if (! isDraftEmpty(iCurrentDraft))
        {
            final byte[] temp = new byte[4 * TinyConst.DOMINO_SIZE];
            int tempCounter = 0;

            int playerDominoesInDraft = 0;                                           // for sanity check
            final int maxAllowedPlayerDominoesInDraft = iNumPlayers == 2 ? 1 : 0;    // for sanity check

            for (int i = 0; i < iDraftDominoCount; ++i)
            {
                final byte playerId = iCurrentDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DRAFT_ELEMENT_PLAYER_ID_INDEX];
                if (playerId == TinyConst.INVALID_PLAYER_ID)
                {
                    System.arraycopy(iCurrentDraft, i * TinyConst.DRAFT_ELEMENT_SIZE, temp, tempCounter * TinyConst.DOMINO_SIZE, TinyConst.DOMINO_SIZE);
                    tempCounter++;
                }
                else
                {
                    // Sanity check
                    if (playerId == getPlayerID(playerName, iPlayers))
                    {
                        playerDominoesInDraft++;
                        assert playerDominoesInDraft <= maxAllowedPlayerDominoesInDraft : "Player has too many chosen dominoes in current draft!";
                    }
                }
            }

            possibleChosenDominoes = new byte[tempCounter * TinyConst.DOMINO_SIZE];
            System.arraycopy(temp, 0, possibleChosenDominoes, 0, possibleChosenDominoes.length);
        }
        else
        {
            possibleChosenDominoes = new byte[0];
        }


        final byte[] availableMoves;
        int moveCounter = 0;
        if (possibleChosenDominoes.length == 0)
        {
            // Last round. Only placed domino to process.
            //
            final int numPossiblePlacedDominoes = possiblePlacedDominoes.length / TinyConst.DOMINO_SIZE;

            availableMoves = new byte[numPossiblePlacedDominoes * TinyConst.MOVE_ELEMENT_SIZE];

            for (int i = 0; i < numPossiblePlacedDominoes; ++i)
            {
                final int moveElementIndex = i * TinyConst.MOVE_ELEMENT_SIZE;

                availableMoves[moveElementIndex] = (byte) moveCounter;
                Arrays.fill(availableMoves, moveElementIndex + TinyConst.MOVE_CHOSEN_DOMINO_INDEX, moveElementIndex + TinyConst.MOVE_CHOSEN_DOMINO_INDEX + TinyConst.DOMINO_SIZE, TinyConst.INVALID_DOMINO_VALUE);
                System.arraycopy(possiblePlacedDominoes, i * TinyConst.DOMINO_SIZE, availableMoves, moveElementIndex + TinyConst.MOVE_PLACED_DOMINO_INDEX, TinyConst.DOMINO_SIZE);

                moveCounter++;
            }
        }
        else if (possiblePlacedDominoes.length == 0)
        {
            // First round. Only chosen domino to process.
            //
            final int numPossibleChosenDominoes = possibleChosenDominoes.length / TinyConst.DOMINO_SIZE;

            availableMoves = new byte[numPossibleChosenDominoes * TinyConst.MOVE_ELEMENT_SIZE];

            for (int i = 0; i < numPossibleChosenDominoes; ++i)
            {
                final int moveElementIndex = i * TinyConst.MOVE_ELEMENT_SIZE;

                availableMoves[moveElementIndex] = (byte) moveCounter;
                System.arraycopy(possibleChosenDominoes, i * TinyConst.DOMINO_SIZE, availableMoves, moveElementIndex + TinyConst.MOVE_CHOSEN_DOMINO_INDEX, TinyConst.DOMINO_SIZE);
                Arrays.fill(availableMoves, moveElementIndex + TinyConst.MOVE_PLACED_DOMINO_INDEX, moveElementIndex + TinyConst.MOVE_PLACED_DOMINO_INDEX + TinyConst.DOMINO_SIZE, TinyConst.INVALID_DOMINO_VALUE);

                moveCounter++;
            }
        }
        else
        {
            final int numPossibleChosenDominoes = possibleChosenDominoes.length / TinyConst.DOMINO_SIZE;
            final int numPossiblePlacedDominoes = possiblePlacedDominoes.length / TinyConst.DOMINO_SIZE;

            availableMoves = new byte[numPossibleChosenDominoes * numPossiblePlacedDominoes * TinyConst.MOVE_ELEMENT_SIZE];

            for (int i = 0; i < numPossibleChosenDominoes; ++i)
            {
                for (int j = 0; j < numPossiblePlacedDominoes; ++j)
                {
                    final int moveElementIndex = moveCounter * TinyConst.MOVE_ELEMENT_SIZE;

                    availableMoves[moveElementIndex] = (byte) moveCounter;
                    System.arraycopy(possibleChosenDominoes, i * TinyConst.DOMINO_SIZE, availableMoves, moveElementIndex + TinyConst.MOVE_CHOSEN_DOMINO_INDEX, TinyConst.DOMINO_SIZE);
                    System.arraycopy(possiblePlacedDominoes, j * TinyConst.DOMINO_SIZE, availableMoves, moveElementIndex + TinyConst.MOVE_PLACED_DOMINO_INDEX, TinyConst.DOMINO_SIZE);

                    moveCounter++;
                }
            }
        }

        assert moveCounter < 256 : "More moves than byte representation allows!";

        iAvailableMoves = availableMoves;

        return iAvailableMoves;
    }


    private boolean hasValidPosition(final byte[] domino, final byte[] kingdomTerrains)
    {
        return getValidPositionsUnique(domino, kingdomTerrains).length > 0;
    }

    /*package*/ byte[] getValidPositionsUnique(final byte[] dominoToPlace, final byte[] kingdomTerrains)
    {
        //return new TinyValidPositionsAlgorithm().applyTo(dominoToPlace, kingdomTerrains);
        return new TinyValidPositionsEfficientAlgorithm().applyTo(dominoToPlace, kingdomTerrains);
    }


    /**
     * Compute score for player.
     *
     * @param playerName name of player
     * @return score of player
     */
    public int getScore(final String playerName)
    {
        if (! iPlayerScoresMap.containsKey(playerName))
        {
            final byte[] kingdomTerrains = getPlayerKingdomTerrains(playerName);
            final byte[] kingdomCrowns = getPlayerKingdomCrowns(playerName);
            final int score = TinyScorerAlgorithm.applyTo(kingdomTerrains, kingdomCrowns);

            iPlayerScoresMap.put(playerName, score);
        }

        return iPlayerScoresMap.get(playerName);
    }

    public Map<String, Integer> getScores()
    {
        final LinkedHashMap<String, Integer> playerNameToScoreMap = new LinkedHashMap<>(iNumPlayers);

        for (int i = 0; i < iNumPlayers; ++i)
        {
            playerNameToScoreMap.put(iPlayers[i], getScore(iPlayers[i]));
        }

        return playerNameToScoreMap;
    }

    public int[] getScoresIndexed()
    {
        final int[] scores = new int[iNumPlayers];

        for (int i = 0; i < iNumPlayers; ++i)
        {
            scores[i] = getScore(iPlayers[i]);
        }

        return scores;
    }


    public byte[] getPlayerKingdomTerrains(final String playerName)
    {
        return getPlayerKingdomTerrains(playerName, iKingdomTerrains);
    }

    public byte[] getPlayerKingdomTerrains(final String playerName, final byte[] allKingdomTerrains)
    {
        final byte id = getPlayerID(playerName, iPlayers);
        final byte[] playerKingdomTerrains = new byte[TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        System.arraycopy(allKingdomTerrains, id * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE, playerKingdomTerrains, 0, TinyConst.SINGLE_PLAYER_KINGDOM_SIZE);

        return playerKingdomTerrains;
    }



    public byte[] getPlayerKingdomCrowns(final String playerName)
    {
        final byte id = getPlayerID(playerName, iPlayers);
        final byte[] kingdomCrowns = new byte[TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        System.arraycopy(iKingdomCrowns, id * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE, kingdomCrowns, 0, TinyConst.SINGLE_PLAYER_KINGDOM_SIZE);

        return kingdomCrowns;
    }

    /**
     * Get kingdom array x-coordinate for linear index i.
     *
     * @param i linear index
     * @return x-coordinate for two-dimensional array
     */
    public static int indexToArrayXCoordinate(final int i)
    {
        return i % TinyConst.KINGDOM_X_SIZE;
    }

    /**
     * Get kingdom array y-coordinate for linear index i.
     *
     * @param i linear index
     * @return y-coordinate for two-dimensional array
     */
    public static int indexToArrayYCoordinate(final int i)
    {
        return i / TinyConst.KINGDOM_X_SIZE;
    }

    /**
     * Get linear index in kingdom array from tile coordinate (castle at x=0,y=0).
     *
     * @param x kingdom tile x-coordinate
     * @param y kingdom tile y-coordinate
     * @return linear index
     */
    public static int tileCoordinateToLinearIndex(final int x, final int y)
    {
        final int absoluteX = x + 4;
        final int absoluteY = y + 4;

        return absoluteX + absoluteY * 9;
    }

    /**
     * Get kingdom tile x-coordinate for linear index i.
     *
     * @param i linear index
     * @return tile x-coordinate
     */
    public static int indexToTileXCoordinate(final int i)
    {
        return indexToArrayXCoordinate(i) - 4;
    }


    /**
     * Get kingdom tile y-coordinate for linear index i.
     *
     * @param i linear index
     * @return tile y-coordinate
     */
    public static int indexToTileYCoordinate(final int i)
    {
        return indexToArrayYCoordinate(i) - 4;
    }


    @Override
    public String toString()
    {
        String result = "";

        for (int p = 0; p < iNumPlayers; ++p)
        {
            result = result.concat("\n");
            result = result.concat("Terrains(" + iPlayers[p] + ")=\n");
            final byte[] kingdomTerrains = getPlayerKingdomTerrains(iPlayers[p]);

            for (int i = 0; i < TinyConst.SINGLE_PLAYER_KINGDOM_SIZE; ++i)
            {
                if (i % 9 == 0)
                {
                    result = result.concat("\n");
                }

                result = result.concat(Byte.toString(kingdomTerrains[i])).concat(" ");
            }
            result = result.concat("\n");
            result = result.concat("\n");
            result = result.concat("Crowns(" + iPlayers[p] + ")=\n");

            final byte[] kingdomCrowns = getPlayerKingdomCrowns(iPlayers[p]);

            for (int i = 0; i < TinyConst.SINGLE_PLAYER_KINGDOM_SIZE; ++i)
            {
                if (i % 9 == 0)
                {
                    result = result.concat("\n");
                }

                result = result.concat(Byte.toString(kingdomCrowns[i])).concat(" ");
            }

            result = result.concat("\n");
        }

        result = result.concat("\n");
        result = result.concat("iCurrentDraft=\n");

        for (int i = 0; i < iDraftDominoCount; ++i)
        {
            for (int j = 0; j < TinyConst.DRAFT_ELEMENT_SIZE; ++j)
            {
                result = result.concat(Byte.toString(iCurrentDraft[j + i * TinyConst.DRAFT_ELEMENT_SIZE]).concat(", "));
            }

            result = result.concat("\n");
        }

        result = result.concat("\n");
        result = result.concat("iPreviousDraft=\n");

        for (int i = 0; i < iDraftDominoCount; ++i)
        {
            for (int j = 0; j < TinyConst.DRAFT_ELEMENT_SIZE; ++j)
            {
                result = result.concat(Byte.toString(iPreviousDraft[j + i * TinyConst.DRAFT_ELEMENT_SIZE]).concat(", "));
            }

            result = result.concat("\n");
        }

        result = result.concat("\n");
        result = result.concat("iPlayers=\n");

        for (int i = 0; i < iNumPlayers; ++i)
        {
            result = result.concat(Integer.toString(i) + ": " + iPlayers[i] + "\n");

        }

        return result;
    }



    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final TinyGameState that = (TinyGameState) o;

        if (iDraftDominoCount != that.iDraftDominoCount)
        {
            return false;
        }
        if (!Arrays.equals(iKingdomTerrains, that.iKingdomTerrains))
        {
            return false;
        }
        if (!Arrays.equals(iKingdomCrowns, that.iKingdomCrowns))
        {
            return false;
        }
        if (!Arrays.equals(iCurrentDraft, that.iCurrentDraft))
        {
            return false;
        }
        if (!Arrays.equals(iPreviousDraft, that.iPreviousDraft))
        {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(iPlayers, that.iPlayers);
    }

    @Override
    public int hashCode()
    {
        int result = Arrays.hashCode(iKingdomTerrains);
        result = 31 * result + Arrays.hashCode(iKingdomCrowns);
        result = 31 * result + Arrays.hashCode(iCurrentDraft);
        result = 31 * result + Arrays.hashCode(iPreviousDraft);
        result = 31 * result + Arrays.hashCode(iPlayers);
        result = 31 * result + (int) iDraftDominoCount;
        return result;
    }


    public String getPlayerWithHighestScore()
    {
        final Map<String, Integer> scores = getScores();

        ArrayList<String> winners = new ArrayList<>(4);
        int highestScore = 0;

        for (String player : scores.keySet())
        {
            final Integer score = scores.get(player);
            if (score == highestScore)
            {
                winners.add(player);
            }

            if (score > highestScore)
            {
                winners.clear();
                winners.add(player);
                highestScore = score;
            }
        }

        return winners.size() > 1 ? "" : winners.get(0);
    }
}
