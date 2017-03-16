package kingdominoplayer.tinyrepresentation;

import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.DominoPosition;
import kingdominoplayer.datastructures.DraftElement;
import kingdominoplayer.datastructures.Kingdom;
import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.PlacedDomino;
import kingdominoplayer.datastructures.PlacedTile;
import kingdominoplayer.datastructures.Position;
import kingdominoplayer.datastructures.Tile;
import kingdominoplayer.plot.KingdomInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Copyright 2017 Tomologic AB<br>
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
        final byte[] kingdomTerrains = new byte[numPlayers * TinyGameState.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] kingdomCrowns = new byte[numPlayers * TinyGameState.SINGLE_PLAYER_KINGDOM_SIZE];
        final byte[] currentDraft = new byte[draftDominoCount * TinyGameState.DRAFT_ELEMENT_SIZE];
        final byte[] previousDraft = new byte[draftDominoCount * TinyGameState.DRAFT_ELEMENT_SIZE];

        Arrays.fill(currentDraft, TinyGameState.INVALID_DOMINO_VALUE);
        Arrays.fill(previousDraft, TinyGameState.INVALID_DOMINO_VALUE);


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

            final byte[] playerKingdomTerrains = getKingdomTerrains(kingdom);
            System.arraycopy(playerKingdomTerrains, 0, kingdomTerrains, i * TinyGameState.SINGLE_PLAYER_KINGDOM_SIZE, playerKingdomTerrains.length);

            final byte[] playerKingdomCrowns = getKingdomCrowns(kingdom);
            System.arraycopy(playerKingdomCrowns, 0, kingdomCrowns, i * TinyGameState.SINGLE_PLAYER_KINGDOM_SIZE, playerKingdomCrowns.length);
        }


        // Get current draft.
        //
        final ArrayList<DraftElement> localGameStateCurrentDraft = localGameState.getCurrentDraft();
        for (int i = 0; i < localGameStateCurrentDraft.size(); ++i)
        {
            final byte[] draftElement = getDraftElementAsByteArray(localGameStateCurrentDraft.get(i), players);
            final int destPos = i * TinyGameState.DRAFT_ELEMENT_SIZE;
            System.arraycopy(draftElement, 0, currentDraft, destPos, draftElement.length);
        }


        // Get previous draft.
        //
        final ArrayList<DraftElement> localGameStatePreviousDraft = localGameState.getPreviousDraft();
        for (int i = 0; i < localGameStatePreviousDraft.size(); ++i)
        {
            final byte[] draftElement = getDraftElementAsByteArray(localGameStatePreviousDraft.get(i), players);
            final int destPos = i * TinyGameState.DRAFT_ELEMENT_SIZE;
            System.arraycopy(draftElement, 0, previousDraft, destPos, draftElement.length);
        }

        // TODO [gedda] IMPORTANT! : ADD DRAW PILE HERE!
        return new TinyGameState(kingdomTerrains, kingdomCrowns, currentDraft, previousDraft, players, new byte[0], true);
    }


    /**
     * Get kingdom terrains as byte array representation.
     *
     * @param kingdom
     * @return
     */
    private byte[] getKingdomTerrains(final Kingdom kingdom)
    {
        final byte[] result = new byte[TinyGameState.SINGLE_PLAYER_KINGDOM_SIZE];

        for (final PlacedTile placedTile : kingdom.getPlacedTiles())
        {
            final Position position = placedTile.getPosition();
            final int x = position.getColumn();
            final int y = position.getRow();

            final String terrain = placedTile.getTerrain();
            final int terrainIndex = TinyGameState.tileCoordinateToLinearIndex(x, y);
            result[terrainIndex] = TerrainCode.from(terrain);
        }

        return result;
    }

    /**
     * Get kingdom crowns as byte array representation.
     *
     * @param kingdom
     * @return
     */
    private byte[] getKingdomCrowns(final Kingdom kingdom)
    {
        final byte[] result = new byte[TinyGameState.SINGLE_PLAYER_KINGDOM_SIZE];

        for (final PlacedTile placedTile : kingdom.getPlacedTiles())
        {
            final Position position = placedTile.getPosition();
            final int x = position.getColumn();
            final int y = position.getRow();

            final int crowns = placedTile.getCrowns();
            final int crownsIndex = TinyGameState.tileCoordinateToLinearIndex(x, y);
            result[crownsIndex] = (byte) crowns;
        }

        return result;
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

        final byte[] result = new byte[TinyGameState.DRAFT_ELEMENT_SIZE];

        result[TinyGameState.DOMINO_ID_INDEX] = (byte) domino.getNumber();;
        result[TinyGameState.DOMINO_TILE_1_TERRAIN_INDEX] = (byte) TerrainCode.from(tile1.getTerrain());
        result[TinyGameState.DOMINO_TILE_1_CROWNS_INDEX] = (byte) tile1.getCrowns();
        result[TinyGameState.DOMINO_TILE_2_TERRAIN_INDEX] = (byte) TerrainCode.from(tile2.getTerrain());
        result[TinyGameState.DOMINO_TILE_2_CROWNS_INDEX] = (byte) tile2.getCrowns();

        final boolean isPlaced = draftElement.getDomino() instanceof PlacedDomino;
        if (isPlaced)
        {
            final DominoPosition dominoPosition = ((PlacedDomino) domino).getDominoPosition();

            result[TinyGameState.DOMINO_TILE_1_X_INDEX] = (byte) dominoPosition.getTile1Position().getColumn();
            result[TinyGameState.DOMINO_TILE_1_Y_INDEX] = (byte) dominoPosition.getTile1Position().getRow();
            result[TinyGameState.DOMINO_TILE_2_X_INDEX] = (byte) dominoPosition.getTile2Position().getColumn();
            result[TinyGameState.DOMINO_TILE_2_Y_INDEX] = (byte) dominoPosition.getTile2Position().getRow();
        }
        else
        {
            result[TinyGameState.DOMINO_TILE_1_X_INDEX] = TinyGameState.INVALID_PLACEMENT_VALUE;
            result[TinyGameState.DOMINO_TILE_1_Y_INDEX] = TinyGameState.INVALID_PLACEMENT_VALUE;
            result[TinyGameState.DOMINO_TILE_2_X_INDEX] = TinyGameState.INVALID_PLACEMENT_VALUE;
            result[TinyGameState.DOMINO_TILE_2_Y_INDEX] = TinyGameState.INVALID_PLACEMENT_VALUE;
        }

        result[TinyGameState.DRAFT_ELEMENT_PLAYER_ID_INDEX] = TinyGameState.getPlayerID(draftElement.getPlayerName(), players);

        return result;
    }

}
