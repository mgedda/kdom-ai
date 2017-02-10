package kingdominoplayer.plot;

import kingdominoplayer.GameResponseParser;
import kingdominoplayer.datastructures.PlacedTile;
import kingdominoplayer.datastructures.Position;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-10<br>
 * Time: 20:04<br><br>
 */
public class SceneRenderer
{
    private static final int cKingdomsGridX1 = 10;
    private static final int cKingdomsGridX2 = 25;
    private static final int cKingdomsGridY1 = 10;
    private static final int cKingdomsGridY2 = 25;

    private static final Position[] cPlayerCastlePositions = new Position[]{
            new Position(cKingdomsGridY1, cKingdomsGridX1),
            new Position(cKingdomsGridY1, cKingdomsGridX2),
            new Position(cKingdomsGridY2, cKingdomsGridX1),
            new Position(cKingdomsGridY2, cKingdomsGridX2)
    };


    public static void render(final String gameState, final String title)
    {
        final GridImage gridImage = new GridImage(60, 40);

        final String[] playerNames = GameResponseParser.getPlayerNames(gameState);

        int playerIndex = 0;
        for (final String playerName : playerNames)
        {
            final Position castlePosition = cPlayerCastlePositions[playerIndex];
            final PlacedTile[] placedTiles = GameResponseParser.getPlayerPlacedTiles(gameState, playerName);
            final int playerScore = GameResponseParser.getPlayerScore(gameState, playerName);

            for (int y = -4; y < 5; ++y)
            {
                for (int x = -4; x < 5; ++x)
                {
                    gridImage.drawTile(castlePosition.plus(new Position(y, x)), TileType.UNOCCUPIED);
                }
            }

            gridImage.drawTile(castlePosition, TileType.CASTLE);
            for (final PlacedTile placedTile : placedTiles)
            {
                final TileType tileType = TileType.valueOf(placedTile.getTerrain().toUpperCase());
                final Position tilePosition = placedTile.getPosition();

                final int numCrowns = placedTile.getCrowns();

                final Position position = castlePosition.plus(tilePosition);
                gridImage.drawTile(position, tileType, numCrowns);
            }

            final String playerNameString = playerName.length() > 18 ?  playerName.substring(0, 18) + "..." : playerName;
            gridImage.drawHeader(playerNameString, castlePosition.getColumn() - 4, castlePosition.getRow() - 7);
            gridImage.drawLabel("Score: " + Integer.toString(playerScore), castlePosition.getColumn() - 4, castlePosition.getRow() - 6);

            playerIndex++;
        }

        BufferedImageViewer.displayImage(gridImage.toBufferedImage(), title);

    }
}
