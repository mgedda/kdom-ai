package kingdominoplayer.tinyrepresentation.adapters;

import kingdominoplayer.naiverepresentation.datastructures.Domino;
import kingdominoplayer.naiverepresentation.datastructures.DominoPosition;
import kingdominoplayer.naiverepresentation.datastructures.DraftElement;
import kingdominoplayer.naiverepresentation.datastructures.Kingdom;
import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.naiverepresentation.datastructures.PlacedDomino;
import kingdominoplayer.naiverepresentation.datastructures.PlacedTile;
import kingdominoplayer.naiverepresentation.datastructures.Position;
import kingdominoplayer.naiverepresentation.datastructures.Tile;
import kingdominoplayer.naiverepresentation.datastructures.KingdomInfo;
import kingdominoplayer.tinyrepresentation.datastructures.TerrainCode;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-14<br>
 * Time: 21:13<br><br>
 */
public class LocalGameStateToTinyGameStateAlgorithm
{
    public TinyGameState applyTo(final LocalGameState localGameState)
    {
        final byte numPlayers = (byte)localGameState.getNumPlayers();
        final byte draftDominoCount = numPlayers == 3 ? (byte) 3 : (byte) 4;

        // Init array with default values.
        //
        final byte[] kingdomTerrains = new byte[numPlayers * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] kingdomCrowns = new byte[numPlayers * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] currentDraft = new byte[draftDominoCount * TinyConst.DRAFT_ELEMENT_SIZE];
        final byte[] previousDraft = new byte[draftDominoCount * TinyConst.DRAFT_ELEMENT_SIZE];
        final byte[] drawPile = new byte[localGameState.getDrawPile().size() * TinyConst.DOMINO_SIZE];

        Arrays.fill(currentDraft, TinyConst.INVALID_DOMINO_VALUE);
        Arrays.fill(previousDraft, TinyConst.INVALID_DOMINO_VALUE);


        // Get players.
        //
        final String[] players = new String[numPlayers];
        final LinkedHashSet<String> playerNames = localGameState.getPlayerNames();
        int counter = 0;
        for (final String playerName : playerNames)
        {
            players[counter++] = playerName;
        }


        // Get kingdoms.
        //
        final ArrayList<KingdomInfo> kingdomInfos = localGameState.getKingdomInfos();
        for (int i = 0; i < kingdomInfos.size(); ++i)
        {
            final Kingdom kingdom = kingdomInfos.get(i).getKingdom();

            final byte[] playerKingdomTerrains = KingdomAdapter.toTinyKingdomTerrains(kingdom);
            System.arraycopy(playerKingdomTerrains, 0, kingdomTerrains, i * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE, playerKingdomTerrains.length);

            final byte[] playerKingdomCrowns = KingdomAdapter.toTinyKingdomCrowns(kingdom);
            System.arraycopy(playerKingdomCrowns, 0, kingdomCrowns, i * TinyConst.SINGLE_PLAYER_KINGDOM_SIZE, playerKingdomCrowns.length);
        }


        // Get current draft.
        //
        final ArrayList<DraftElement> localGameStateCurrentDraft = localGameState.getCurrentDraft();
        for (int i = 0; i < localGameStateCurrentDraft.size(); ++i)
        {
            final byte[] draftElement = getDraftElementAsByteArray(localGameStateCurrentDraft.get(i), players);
            final int destPos = i * TinyConst.DRAFT_ELEMENT_SIZE;
            System.arraycopy(draftElement, 0, currentDraft, destPos, draftElement.length);
        }


        // Get previous draft.
        //
        final ArrayList<DraftElement> localGameStatePreviousDraft = localGameState.getPreviousDraft();
        for (int i = 0; i < localGameStatePreviousDraft.size(); ++i)
        {
            final byte[] draftElement = getDraftElementAsByteArray(localGameStatePreviousDraft.get(i), players);
            final int destPos = i * TinyConst.DRAFT_ELEMENT_SIZE;
            System.arraycopy(draftElement, 0, previousDraft, destPos, draftElement.length);
        }

        // Get draw pile
        //
        final Set<Domino> localGameStateDrawPile = localGameState.getDrawPile();
        int dominoCounter = 0;
        for (final Domino drawPileElement : localGameStateDrawPile)
        {
            final byte[] domino = getDrawPileDominoAsByteArray(drawPileElement);
            final int destPos = dominoCounter * TinyConst.DOMINO_SIZE;
            System.arraycopy(domino, 0, drawPile, destPos, domino.length);
            dominoCounter++;
        }


        return new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, drawPile, localGameState.getPlayerTurnOrNull());
    }



    /**
     * Convert draft element to byte array representation.
     *
     * @param draftElement
     * @param players
     * @return
     */
    private byte[] getDraftElementAsByteArray(final DraftElement draftElement, final String[] players)
    {
        final Domino domino = draftElement.getDomino();
        final Tile tile1 = domino.getTile1();
        final Tile tile2 = domino.getTile2();

        final byte[] result = new byte[TinyConst.DRAFT_ELEMENT_SIZE];

        result[TinyConst.DOMINO_ID_INDEX] = (byte) domino.getNumber();;
        result[TinyConst.DOMINO_TILE_1_TERRAIN_INDEX] = TerrainCode.from(tile1.getTerrain());
        result[TinyConst.DOMINO_TILE_1_CROWNS_INDEX] = (byte) tile1.getCrowns();
        result[TinyConst.DOMINO_TILE_2_TERRAIN_INDEX] = TerrainCode.from(tile2.getTerrain());
        result[TinyConst.DOMINO_TILE_2_CROWNS_INDEX] = (byte) tile2.getCrowns();

        final boolean isPlaced = draftElement.getDomino() instanceof PlacedDomino;
        if (isPlaced)
        {
            final DominoPosition dominoPosition = ((PlacedDomino) domino).getDominoPosition();

            result[TinyConst.DOMINO_TILE_1_X_INDEX] = (byte) dominoPosition.getTile1Position().getColumn();
            result[TinyConst.DOMINO_TILE_1_Y_INDEX] = (byte) dominoPosition.getTile1Position().getRow();
            result[TinyConst.DOMINO_TILE_2_X_INDEX] = (byte) dominoPosition.getTile2Position().getColumn();
            result[TinyConst.DOMINO_TILE_2_Y_INDEX] = (byte) dominoPosition.getTile2Position().getRow();
        }
        else
        {
            result[TinyConst.DOMINO_TILE_1_X_INDEX] = TinyConst.INVALID_PLACEMENT_VALUE;
            result[TinyConst.DOMINO_TILE_1_Y_INDEX] = TinyConst.INVALID_PLACEMENT_VALUE;
            result[TinyConst.DOMINO_TILE_2_X_INDEX] = TinyConst.INVALID_PLACEMENT_VALUE;
            result[TinyConst.DOMINO_TILE_2_Y_INDEX] = TinyConst.INVALID_PLACEMENT_VALUE;
        }

        result[TinyConst.DRAFT_ELEMENT_PLAYER_ID_INDEX] = TinyGameState.getPlayerID(draftElement.getPlayerName(), players);

        return result;
    }

    /**
     * Convert domino to byte array representation.
     *
     * @param domino
     * @return
     */
    private byte[] getDrawPileDominoAsByteArray(final Domino domino)
    {
        final Tile tile1 = domino.getTile1();
        final Tile tile2 = domino.getTile2();

        final byte[] result = new byte[TinyConst.DOMINO_SIZE];

        result[TinyConst.DOMINO_ID_INDEX] = (byte) domino.getNumber();;
        result[TinyConst.DOMINO_TILE_1_TERRAIN_INDEX] = TerrainCode.from(tile1.getTerrain());
        result[TinyConst.DOMINO_TILE_1_CROWNS_INDEX] = (byte) tile1.getCrowns();
        result[TinyConst.DOMINO_TILE_2_TERRAIN_INDEX] = TerrainCode.from(tile2.getTerrain());
        result[TinyConst.DOMINO_TILE_2_CROWNS_INDEX] = (byte) tile2.getCrowns();

        result[TinyConst.DOMINO_TILE_1_X_INDEX] = TinyConst.INVALID_PLACEMENT_VALUE;
        result[TinyConst.DOMINO_TILE_1_Y_INDEX] = TinyConst.INVALID_PLACEMENT_VALUE;
        result[TinyConst.DOMINO_TILE_2_X_INDEX] = TinyConst.INVALID_PLACEMENT_VALUE;
        result[TinyConst.DOMINO_TILE_2_Y_INDEX] = TinyConst.INVALID_PLACEMENT_VALUE;

        return result;
    }

}
