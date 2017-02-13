package kingdominoplayer.plot;

import kingdominoplayer.GameResponseParser;
import kingdominoplayer.datastructures.*;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 20:04<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class SceneRenderer
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

    public static void render(final String gameState, final String title)
    {
        final GameState gameStateObject = GameResponseParser.getGameStateObject(gameState);

        render(gameStateObject, title);
    }

    public static void render(final GameState gameState, final String title)
    {
        final GridImage gridImage = new GridImage(cWindowGridX, cWindowGridY);

        int playerIndex = 0;
        for (final KingdomInfo kingdomInfo : gameState.getKingdomInfos())
        {
            final String playerName = kingdomInfo.getPlayerName();
            final Kingdom kingdom = kingdomInfo.getKingdom();

            final Position castlePosition = cPlayerCastlePositions[playerIndex];
            drawKingdom(kingdom, castlePosition, gridImage, true);

            // Draw player name.
            //
            final String playerNameString = playerName.length() > 18 ?  playerName.substring(0, 18) + "..." : playerName;
            gridImage.drawHeader(playerNameString, castlePosition.getColumn() - 4, castlePosition.getRow() - 7);

            // Draw player score.
            //
            final int playerScore = kingdomInfo.getScore();
            gridImage.drawLabel("Score: " + Integer.toString(playerScore), castlePosition.getColumn() - 4, castlePosition.getRow() - 6);

            playerIndex++;
        }


        // Draw previous draft.
        //
        final ArrayList<DraftElement> previousDraft = gameState.getPreviousDraft();
        int draftElementCounter = 0;
        for (final DraftElement draftElement : previousDraft)
        {
            final Domino domino = draftElement.getDomino();
            final Tile tile1 = domino.getTile1();
            final Tile tile2 = domino.getTile2();

            final int cellPosY = cPreviousDraftY + draftElementCounter;
            gridImage.drawTile(cDraftsX, cellPosY, tile1);
            gridImage.drawTile(cDraftsX + 1, cellPosY, tile2);

            String playerName = draftElement.getPlayerName();
            if (playerName != null)
            {
                playerName = playerName.length() > 25? playerName.substring(0, 24) + "..." : playerName;
                gridImage.drawLabel(playerName, cDraftsX + 3, cellPosY);
            }

            draftElementCounter += 2;
        }

        gridImage.drawHeader("Previous Draft", cDraftsX, cPreviousDraftY - 2);


        // Draw current draft.
        //
        final ArrayList<DraftElement> currentDraft = gameState.getCurrentDraft();
        draftElementCounter = 0;
        for (final DraftElement draftElement : currentDraft)
        {
            final Domino domino = draftElement.getDomino();
            final Tile tile1 = domino.getTile1();
            final Tile tile2 = domino.getTile2();

            final int cellPosY = cCurrentDraftY + draftElementCounter;
            gridImage.drawTile(cDraftsX, cellPosY, tile1);
            gridImage.drawTile(cDraftsX + 1, cellPosY, tile2);

            String playerName = draftElement.getPlayerName();
            if (playerName != null)
            {
                playerName = playerName.length() > 25? playerName.substring(0, 24) + "..." : playerName;
                gridImage.drawLabel(playerName, cDraftsX + 3, cellPosY);
            }

            draftElementCounter += 2;
        }

        gridImage.drawHeader("Current Draft", cDraftsX, cCurrentDraftY - 2);



        BufferedImageViewer.displayImage(gridImage.toBufferedImage(), title);
    }


    public static void drawKingdom(final Kingdom kingdom, final Position castlePosition, final GridImage gridImage, final boolean drawBackgroundTiles)
    {
        // Draw background tiles for each kingdom.
        //
        if (drawBackgroundTiles)
        {
            for (int y = -4; y < 5; ++y)
            {
                for (int x = -4; x < 5; ++x)
                {
                    gridImage.drawTile(castlePosition.plus(new Position(y, x)), TileType.UNOCCUPIED);
                }
            }
        }

        // Draw kingdom tiles for player.
        //
        final PlacedTile[] placedTiles = kingdom.getPlacedTiles();
        gridImage.drawTile(castlePosition, TileType.CASTLE);
        for (final PlacedTile placedTile : placedTiles)
        {
            final Position tilePosition = placedTile.getPosition();
            final Position position = castlePosition.plus(tilePosition);

            gridImage.drawTile(position, placedTile);
        }
    }


}
