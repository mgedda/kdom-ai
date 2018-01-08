package kingdominoplayer.utils.plot;

/*
 * Copyright 2018 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2018-01-07<br>
 * Time: 22:18<br><br>
 */

import kingdominoplayer.naiverepresentation.datastructures.Position;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

public class TinyDebugPlot
{
    private static final int cWindowGridX = 43;
    private static final int cWindowGridY = 29;

    private static final int cKingdomsGridX1 = 6;
    private static final int cKingdomsGridX2 = 20;
    private static final int cKingdomsGridY1 = 8;
    private static final int cKingdomsGridY2 = 22;

    private static final int cDraftsX = 32;
    private static final int cCurrentDraftY = 4;
    private static final int cPreviousDraftY = 16;


    private static final Position[] cPlayerCastlePositions = new Position[]{
            new Position(cKingdomsGridY1, cKingdomsGridX1),
            new Position(cKingdomsGridY1, cKingdomsGridX2),
            new Position(cKingdomsGridY2, cKingdomsGridX1),
            new Position(cKingdomsGridY2, cKingdomsGridX2)
    };

    public static void plotGameState(final TinyGameState gameState, final String title)
    {
        final GridImage gridImage = new GridImage(cWindowGridX, cWindowGridY);

        int playerIndex = 0;
        for (final String playerName : gameState.getPlayers())
        {
            final byte[] playerKingdomTerrains = gameState.getPlayerKingdomTerrains(playerName);
            final byte[] playerKingdomCrowns = gameState.getPlayerKingdomCrowns(playerName);

            final Position castlePosition = cPlayerCastlePositions[playerIndex];
            addKingdomToGridImage(playerKingdomTerrains, playerKingdomCrowns, castlePosition, gridImage, true);

            // Print player name.
            //
            final String playerNameString = playerName.length() > 18 ?  playerName.substring(0, 18) + "..." : playerName;
            gridImage.drawHeader(playerNameString, castlePosition.getColumn() - 4, castlePosition.getRow() - 7);

            // Draw player score.
            //
            final int playerScore = gameState.getScore(playerName);
            gridImage.drawLabel("Score: " + Integer.toString(playerScore), castlePosition.getColumn() - 4, castlePosition.getRow() - 6);

            playerIndex++;
        }


        // Draw previous draft.
        //
        final byte draftDominoCount = gameState.getNumPlayers() == 3 ? (byte) 3 : (byte) 4;
        final byte[] previousDraft = gameState.getPreviousDraft();

        int cellPosDeltaY = 0;
        for (int i = 0; i < draftDominoCount; ++i)
        {
            final byte dominoID = previousDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DOMINO_ID_INDEX];

            if (dominoID == TinyConst.INVALID_DOMINO_VALUE)
            {
                continue;
            }

            final byte tile1Terrain = previousDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DOMINO_TILE_1_TERRAIN_INDEX];
            final byte tile1Crowns = previousDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DOMINO_TILE_1_CROWNS_INDEX];
            final byte tile2Terrain = previousDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DOMINO_TILE_2_TERRAIN_INDEX];
            final byte tile2Crowns = previousDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DOMINO_TILE_2_CROWNS_INDEX];

            final int cellPosY = cPreviousDraftY + cellPosDeltaY;
            gridImage.drawTile(cDraftsX, cellPosY, TileType.from(tile1Terrain), tile1Crowns);
            gridImage.drawTile(cDraftsX + 1, cellPosY, TileType.from(tile2Terrain), tile2Crowns);


            final byte playerID = previousDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DRAFT_ELEMENT_PLAYER_ID_INDEX];
            if (playerID != TinyConst.INVALID_PLAYER_ID)
            {
                String playerName = gameState.getPlayers()[playerID];
                playerName = playerName.length() > 25 ? playerName.substring(0, 24) + "..." : playerName;
                gridImage.drawLabel(playerName, cDraftsX + 3, cellPosY);
            }

            cellPosDeltaY += 2;
        }

        gridImage.drawHeader("Previous Draft", cDraftsX, cPreviousDraftY - 2);


        // Draw current draft.
        //
        final byte[] currentDraft = gameState.getCurrentDraft();

        cellPosDeltaY = 0;
        for (int i = 0; i < draftDominoCount; ++i)
        {
            final byte dominoID = currentDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DOMINO_ID_INDEX];

            if (dominoID == TinyConst.INVALID_DOMINO_VALUE)
            {
                continue;
            }

            final byte tile1Terrain = currentDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DOMINO_TILE_1_TERRAIN_INDEX];
            final byte tile1Crowns = currentDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DOMINO_TILE_1_CROWNS_INDEX];
            final byte tile2Terrain = currentDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DOMINO_TILE_2_TERRAIN_INDEX];
            final byte tile2Crowns = currentDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DOMINO_TILE_2_CROWNS_INDEX];

            final int cellPosY = cCurrentDraftY + cellPosDeltaY;
            gridImage.drawTile(cDraftsX, cellPosY, TileType.from(tile1Terrain), tile1Crowns);
            gridImage.drawTile(cDraftsX + 1, cellPosY, TileType.from(tile2Terrain), tile2Crowns);

            final byte playerID = currentDraft[i * TinyConst.DRAFT_ELEMENT_SIZE + TinyConst.DRAFT_ELEMENT_PLAYER_ID_INDEX];
            if (playerID != TinyConst.INVALID_PLAYER_ID)
            {
                String playerName = gameState.getPlayers()[playerID];
                playerName = playerName.length() > 25 ? playerName.substring(0, 24) + "..." : playerName;
                gridImage.drawLabel(playerName, cDraftsX + 3, cellPosY);
            }

            cellPosDeltaY += 2;
        }

        gridImage.drawHeader("Current Draft", cDraftsX, cCurrentDraftY - 2);


        BufferedImageViewer.displayImage(gridImage.toBufferedImage(), title);
    }


    /*package*/ static void addKingdomToGridImage(final byte[] playerKingdomTerrains,
                                                  final byte[] playerKingdomCrowns,
                                                  final Position castlePosition,
                                                  final GridImage gridImage,
                                                  final boolean drawBackgroundTiles)
    {
        // Draw background tiles for each kingdom.
        //
        for (int y = -4; y < 5; ++y)
        {
            for (int x = -4; x < 5; ++x)
            {
                final int i = TinyGameState.tileCoordinateToLinearIndex(x, y);
                final byte terrain = playerKingdomTerrains[i];
                final byte crowns = playerKingdomCrowns[i];

                final int tileX = castlePosition.getColumn() + x;
                final int tileY = castlePosition.getRow() + y;

                final TileType tileType = TileType.from(terrain);

                if (tileType == TileType.UNOCCUPIED)
                {
                    if (drawBackgroundTiles)
                    {
                        gridImage.drawTile(tileX, tileY, tileType);
                    }
                }
                else
                {
                    gridImage.drawTile(tileX, tileY, tileType, crowns);
                }
            }
        }
    }

}
