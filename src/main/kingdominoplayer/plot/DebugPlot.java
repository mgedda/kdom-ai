package kingdominoplayer.plot;

import kingdominoplayer.GameResponseParser;
import kingdominoplayer.datastructures.*;

import java.util.ArrayList;
import java.util.Collection;

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


    public static void plotKingdomsWithPlacedDominoMarked(final Collection<KingdomMovePair> kingdomMovePairs, final String title)
    {
        final ArrayList<KingdomDominoPositionPair> kingdomDominoPositionPairs = new ArrayList<>(kingdomMovePairs.size());

        for (final KingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            final Kingdom kingdom = kingdomMovePair.getKingdom();
            final PlacedDomino placedDomino = kingdomMovePair.getMove().getPlacedDomino();
            final DominoPosition dominoPosition = placedDomino.getDominoPosition();

            kingdomDominoPositionPairs.add(new KingdomDominoPositionPair(kingdom, dominoPosition));
        }

        plotKingdomsWithDominoPositionMarked(kingdomDominoPositionPairs, title);
    }

    public static void plotKingdomsWithChosenDominoMarked(final Collection<KingdomMovePair> kingdomMovePairs, final String title)
    {
        final ArrayList<KingdomDominoPositionPair> kingdomDominoPositionPairs = new ArrayList<>(kingdomMovePairs.size());

        for (final KingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            assert kingdomMovePair.isChosenPlaced() : "chosen domino has not been placed";

            final Kingdom kingdom = kingdomMovePair.getKingdom();
            final DominoPosition dominoPosition = kingdomMovePair.getChosenDominoPosition();

            kingdomDominoPositionPairs.add(new KingdomDominoPositionPair(kingdom, dominoPosition));
        }

        plotKingdomsWithDominoPositionMarked(kingdomDominoPositionPairs, title);
    }

    public static void plotKingdomsWithDominoPositionMarked(final Collection<KingdomDominoPositionPair> kingdomDominoPositionPairs, final String title)
    {
        final ArrayList<Kingdom> kingdoms = new ArrayList<>(kingdomDominoPositionPairs.size());
        final ArrayList<DominoPositions> dominoPositionsArray = new ArrayList<>(kingdomDominoPositionPairs.size());

        for (final KingdomDominoPositionPair kingdomDominoPositionPair : kingdomDominoPositionPairs)
        {
            kingdoms.add(kingdomDominoPositionPair.getKingdom());

            final ArrayList<DominoPosition> dominoPositions = new ArrayList<>();
            dominoPositions.add(kingdomDominoPositionPair.getDominoPosition());

            dominoPositionsArray.add(new DominoPositions(dominoPositions));
        }

        final ArrayList<GridImage> gridImages = DebugPlot.getGridImagesShowingKingdomsWithDominoesMarked(kingdoms, dominoPositionsArray);

        int counter = 0;
        for (GridImage gridImage : gridImages)
        {
            BufferedImageViewer.displayImage(gridImage.toBufferedImage(), title + " #" + Integer.toString(counter));
            counter++;
        }
    }

    public static void plotWithPositionsMarked(final Kingdom kingdom, final Collection<Position> positions, final String title)
    {
        final ArrayList<Kingdom> kingdoms = new ArrayList<>(1);
        kingdoms.add(kingdom);
        final ArrayList<Positions> positionsArray = new ArrayList<>(1);
        positionsArray.add(new Positions(positions));

        plotWithPositionsMarked(kingdoms, positionsArray, title);
    }

    public static void plotWithPositionsMarked(final ArrayList<Kingdom> kingdoms, final Collection<Positions> positionsArray, final String title)
    {
        final ArrayList<DominoPositions> dominoPositionsArray = new ArrayList<>(positionsArray.size());

        for (final Positions positions : positionsArray)
        {
            final ArrayList<DominoPosition> dominoPositions = new ArrayList<>(positions.getPositionArray().size());

            for (final Position position : positions.getPositionArray())
            {
                dominoPositions.add(new DominoPosition(position, position));
            }

            dominoPositionsArray.add(new DominoPositions(dominoPositions));
        }

        plotWithDominoPositionsMarked(kingdoms, dominoPositionsArray, title);
    }


    public static void plotWithDominoPositionsMarked(final ArrayList<Kingdom> kingdoms, final ArrayList<DominoPositions> dominoPositionsArray, final String title)
    {
        assert kingdoms.size() == dominoPositionsArray.size() : "pre: array size inconsistency";

        final ArrayList<GridImage> gridImages = DebugPlot.getGridImagesShowingKingdomsWithDominoesMarked(kingdoms, dominoPositionsArray);

        int counter = 0;
        for (GridImage gridImage : gridImages)
        {
            BufferedImageViewer.displayImage(gridImage.toBufferedImage(), title + " #" + Integer.toString(counter));
            counter++;
        }
    }

    /*package*/ static ArrayList<GridImage> getGridImagesShowingKingdomsWithDominoesMarked(final ArrayList<Kingdom> kingdoms, final ArrayList<DominoPositions> dominoPositionsArray)
    {
        assert kingdoms.size() == dominoPositionsArray.size() : "pre: array size inconsistency";

        int kingdomColumns = 5;
        int kingdomRows = 3;

        int kingdomColumnWidth = 1 /* space */ + 9 /* tiles */;
        int kingdomRowHeight = 1 /* space */ + 1 /* score label */ + 9 /* tiles */;

        int cellSize = 25;

        final int kingdomsPerPage = kingdomColumns * kingdomRows;

        final ArrayList<GridImage> gridImages = new ArrayList<>(kingdoms.size());

        int counter = 0;
        GridImage gridImage = null;
        for (int i = 0; i < kingdoms.size(); ++i)
        {
            final Kingdom kingdom = kingdoms.get(i);
            final DominoPositions dominoPositions = dominoPositionsArray.get(i);

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

            gridImage.drawLabel("Score: " + Integer.toString(kingdom.getScore()), scoreLabelColumn, scoreLabelRow);

            final int castleColumn = kingdomColumn * kingdomColumnWidth + 1 + 4;
            final int castleRow = scoreLabelRow + 5;
            final Position castlePosition = new Position(castleRow, castleColumn);

            addKingdomToGridImage(kingdom, castlePosition, gridImage, true);

            for (final DominoPosition dominoPosition : dominoPositions.getDominoPositions())
            {
                final Position tile1Position = castlePosition.plus(dominoPosition.getTile1Position());
                final Position tile2Position = castlePosition.plus(dominoPosition.getTile2Position());

                gridImage.markArea(tile1Position, tile2Position, 0xFFFF0000);
            }

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
