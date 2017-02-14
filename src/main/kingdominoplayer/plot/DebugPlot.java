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
public class DebugPlot
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

    public static void plotGameState(final String gameState, final String title)
    {
        final GameState gameStateObject = GameResponseParser.getGameStateObject(gameState);

        plotGameState(gameStateObject, title);
    }

    public static void plotGameState(final GameState gameState, final String title)
    {
        final GridImage gridImage = new GridImage(cWindowGridX, cWindowGridY);

        int playerIndex = 0;
        for (final KingdomInfo kingdomInfo : gameState.getKingdomInfos())
        {
            final String playerName = kingdomInfo.getPlayerName();
            final Kingdom kingdom = kingdomInfo.getKingdom();

            final Position castlePosition = cPlayerCastlePositions[playerIndex];
            addKingdomToGridImage(kingdom, castlePosition, gridImage, true);

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


    public static void plotKingdomsWithPlacedDominoMarked(final ArrayList<KingdomMovePair> kingdomMovePairs, final String title)
    {
        final ArrayList<KingdomDominoPositionPair> kingdomDominoPositionPairs = new ArrayList<>();

        for (final KingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            final Kingdom kingdom = kingdomMovePair.getKingdom();
            final PlacedDomino placedDomino = kingdomMovePair.getMove().getPlacedDomino();
            final DominoPosition dominoPosition = placedDomino.getDominoPosition();

            kingdomDominoPositionPairs.add(new KingdomDominoPositionPair(kingdom, dominoPosition));
        }
        final ArrayList<GridImage> gridImages = DebugPlot.getGridImagesShowingKingdomsWithMarkedDominoes(kingdomDominoPositionPairs);

        int counter = 0;
        for (GridImage gridImage : gridImages)
        {
            BufferedImageViewer.displayImage(gridImage.toBufferedImage(), title + " #" + Integer.toString(counter));
            counter++;
        }
    }

    /*package*/ static ArrayList<GridImage> getGridImagesShowingKingdomsWithMarkedDominoes(final ArrayList<KingdomDominoPositionPair> kingdomDominoPositionPairs)
    {
        int kingdomColumns = 5;
        int kingdomRows = 3;

        int kingdomColumnWidth = 1 /* space */ + 9 /* tiles */;
        int kingdomRowHeight = 1 /* space */ + 1 /* score label */ + 9 /* tiles */;

        int cellSize = 25;

        final int kingdomsPerPage = kingdomColumns * kingdomRows;

        final ArrayList<GridImage> gridImages = new ArrayList<>(kingdomDominoPositionPairs.size());

        int counter = 0;
        GridImage gridImage = null;
        for (final KingdomDominoPositionPair kingdomDominoPositionPair : kingdomDominoPositionPairs)
        {
            if (counter % kingdomsPerPage == 0)
            {
                counter = 0;

                final int numCellsX = kingdomColumns * kingdomColumnWidth + 1;
                final int numCellsY = kingdomRows * kingdomRowHeight + 1;

                gridImage = new GridImage(numCellsX, numCellsY, cellSize, cellSize);

                gridImages.add(gridImage);
            }

            final int kingdomColumn = counter % kingdomColumns;
            final int kingdomRow = counter / kingdomColumns;

            final int scoreLabelColumn = kingdomColumn * kingdomColumnWidth + 1;
            final int scoreLabelRow = kingdomRow * kingdomRowHeight + 1;

            final Kingdom kingdom = kingdomDominoPositionPair.getKingdom();
            gridImage.drawLabel("Score: " + Integer.toString(kingdom.getScore()), scoreLabelColumn, scoreLabelRow);

            final int castleColumn = kingdomColumn * kingdomColumnWidth + 1 + 4;
            final int castleRow = scoreLabelRow + 5;
            final Position castlePosition = new Position(castleRow, castleColumn);

            addKingdomToGridImage(kingdom, castlePosition, gridImage, true);

            final DominoPosition dominoPosition = kingdomDominoPositionPair.getDominoPosition();

            final Position tile1Position = castlePosition.plus(dominoPosition.getTile1Position());
            final Position tile2Position = castlePosition.plus(dominoPosition.getTile2Position());

            gridImage.markArea(tile1Position, tile2Position, 0xFFFF0000);

            counter++;
        }

        return gridImages;
    }

    /*package*/ static void addKingdomToGridImage(final Kingdom kingdom, final Position castlePosition, final GridImage gridImage, final boolean drawBackgroundTiles)
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
