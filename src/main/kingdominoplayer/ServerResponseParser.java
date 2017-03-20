package kingdominoplayer;

import kingdominoplayer.naivedatastructures.*;
import kingdominoplayer.naivedatastructures.GameState;
import kingdominoplayer.naivedatastructures.KingdomInfo;
import kingdominoplayer.utils.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-08<br>
 * Time: 23:11<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class ServerResponseParser
{
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String getUUID(final String response)
    {
        final JSONObject obj = new JSONObject(response);
        final String uuid = obj.getString("uuid");

        return uuid;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static boolean isGameOver(final String gameState)
    {
        final JSONObject obj = new JSONObject(gameState);
        final boolean isGameOver = obj.getBoolean("gameOver");

        return isGameOver;
    }

    public static Move[] getAvailableMoves(final String moves)
    {
        final JSONArray movesArray = new JSONObject(moves).getJSONArray("moves");

        final Move[] availableMoves = new Move[movesArray.length()];

        for (int i = 0; i < movesArray.length(); ++i)
        {
            final JSONObject moveJSON = movesArray.getJSONObject(i);
            final int number = moveJSON.getInt("number");

            final Domino chosenDomino = getDomino(moveJSON, "chosenDomino");
            final PlacedDomino placedDomino = getPlacedDomino(moveJSON);

            availableMoves[i] = new Move(number, chosenDomino, placedDomino);
        }

        return availableMoves;
    }

    private static Domino getDomino(final JSONObject object, final String name)
    {
        if (! object.has(name))
        {
            return null;
        }

        final JSONObject chosenDominoJSON = object.getJSONObject(name);

        final int number = chosenDominoJSON.getInt("number");
        final Tile tile1 = getTile(chosenDominoJSON, "tile1");
        final Tile tile2 = getTile(chosenDominoJSON, "tile2");

        return new Domino(number, tile1, tile2);
    }

    private static PlacedDomino getPlacedDomino(final JSONObject moveJSON)
    {
        if (! moveJSON.has("placedDomino"))
        {
            return null;
        }

        final JSONObject placedDominoJSON = moveJSON.getJSONObject("placedDomino");

        final Domino domino = getDomino(placedDominoJSON, "domino");

        final Position tile1Position = getTilePosition(placedDominoJSON, "tile1Position");
        final Position tile2Position = getTilePosition(placedDominoJSON, "tile2Position");

        return new PlacedDomino(domino, new DominoPosition(tile1Position, tile2Position));
    }

    private static Position getTilePosition(final JSONObject object, final String tilePositionName)
    {
        final JSONObject tilePositionJSON = object.getJSONObject(tilePositionName);
        final int row = tilePositionJSON.getInt("row");
        final int column = tilePositionJSON.getInt("col");

        return new Position(row, column);
    }

    private static Tile getTile(final JSONObject chosenDominoJSON, final String tileName)
    {
        final JSONObject obj = chosenDominoJSON.getJSONObject(tileName);
        final String terrain = obj.getString("terrain");
        final int crowns = obj.getInt("crowns");

        return new Tile(terrain, crowns);
    }

    public static String[] getPlayerNames(final String gameState)
    {
        final JSONArray kingdomsArray = new JSONObject(gameState).getJSONArray("kingdoms");

        final String[] playerNames = new String[kingdomsArray.length()];

        for (int i = 0; i < kingdomsArray.length(); ++i)
        {
            final JSONObject kingdomJSON = kingdomsArray.getJSONObject(i);
            final JSONObject playerJSON = kingdomJSON.getJSONObject("player");
            final String name = playerJSON.getString("name");

            playerNames[i] = name;
        }

        return playerNames;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static int getPlayerScore(final String gameState, final String playerName)
    {
        final JSONArray kingdomsArray = new JSONObject(gameState).getJSONArray("kingdoms");

        for (int i = 0; i < kingdomsArray.length(); ++i)
        {
            final JSONObject kingdomJSON = kingdomsArray.getJSONObject(i);
            final JSONObject playerJSON = kingdomJSON.getJSONObject("player");
            final String name = playerJSON.getString("name");

            if (name.equals(playerName))
            {
                final JSONObject score = kingdomJSON.getJSONObject("score");
                final int total = score.getInt("total");

                return total;
            }
        }

        assert false : "player does not exist";
        return -1;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String getCurrentPlayer(final String gameState)
    {
        final JSONObject currentPlayerJSON = new JSONObject(gameState).getJSONObject("currentPlayer");
        final String name = currentPlayerJSON.getString("name");

        return name;
    }

    public static PlacedTile[] getPlayerPlacedTiles(final String gameState, final String playerName)
    {
        final JSONArray kingdomsArray = new JSONObject(gameState).getJSONArray("kingdoms");

        for (int i = 0; i < kingdomsArray.length(); ++i)
        {
            final JSONObject kingdomJSON = kingdomsArray.getJSONObject(i);
            final JSONObject playerJSON = kingdomJSON.getJSONObject("player");
            final String name = playerJSON.getString("name");

            if (name.equals(playerName))
            {
                final JSONArray placedTiles = kingdomJSON.getJSONArray("placedTiles");

                final PlacedTile[] tilePositionPairs = new PlacedTile[placedTiles.length()];

                for (int j = 0; j < placedTiles.length(); ++j)
                {
                    final JSONObject tileJSON = placedTiles.getJSONObject(j).getJSONObject("tile");
                    final String terrain = tileJSON.getString("terrain");
                    final int crowns = tileJSON.getInt("crowns");

                    final Tile tile = new Tile(terrain, crowns);

                    final JSONObject positionJSON = placedTiles.getJSONObject(j).getJSONObject("position");
                    final int row = positionJSON.getInt("row");
                    final int col = positionJSON.getInt("col");

                    final Position position = new Position(row, col);

                    tilePositionPairs[j] = new PlacedTile(tile, position);
                }

                return tilePositionPairs;
            }
        }

        assert false : "player does not exist";
        return new PlacedTile[0];
    }


    public static DraftElement[] getPreviousDraft(final String gameState)
    {
        final JSONObject gameStateJSON = new JSONObject(gameState);

        final JSONObject previousDraftJSON = gameStateJSON.getJSONObject("previousDraft");

        return getDraftElements(previousDraftJSON);
    }

    public static DraftElement[] getCurrentDraft(final String gameState)
    {
        final JSONObject gameStateJSON = new JSONObject(gameState);

        final JSONObject previousDraftJSON = gameStateJSON.getJSONObject("currentDraft");

        return getDraftElements(previousDraftJSON);
    }

    private static DraftElement[] getDraftElements(final JSONObject draftJSON)
    {
        final JSONArray dominoesJSONArray = draftJSON.getJSONArray("dominoes");

        final ArrayList<DraftElement> previousDraft = new ArrayList<>(4);

        for (int i = 0; i < dominoesJSONArray.length(); ++i)
        {
            final JSONObject dominoJSONArrayObject = dominoesJSONArray.getJSONObject(i);

            final Domino domino = getDomino(dominoJSONArrayObject, "domino");

            String playerName = null;
            if (dominoJSONArrayObject.has("player"))
            {
                final JSONObject playerJSON = dominoJSONArrayObject.getJSONObject("player");
                playerName = playerJSON.getString("name");
            }

            previousDraft.add(new DraftElement(domino, playerName));
        }

        return previousDraft.toArray(new DraftElement[previousDraft.size()]);
    }


    public static Domino[] getCurrentDraftForPlayer(final String gameState, final String playerName)
    {
        final DraftElement[] currentDraft = getCurrentDraft(gameState);

        return getDraftForPlayer(currentDraft, playerName);
    }


    public static Domino[] getPreviousDraftForPlayer(final String gameState, final String playerName)
    {
        final DraftElement[] previousDraft = getPreviousDraft(gameState);

        return getDraftForPlayer(previousDraft, playerName);
    }


    private static Domino[] getDraftForPlayer(final DraftElement[] Draft, final String playerName)
    {
        final ArrayList<Domino> draftForPlayer = new ArrayList<>(2);

        for (final DraftElement draftElement : Draft)
        {
            if (draftElement.getPlayerName() != null && draftElement.getPlayerName().equals(playerName))
            {
                draftForPlayer.add(draftElement.getDomino());
            }
        }

        return draftForPlayer.toArray(new Domino[draftForPlayer.size()]);
    }


    public static GameState getGameStateObject(final String gameState)
    {
        final String[] playerNames = ServerResponseParser.getPlayerNames(gameState);

        final ArrayList<KingdomInfo> kingdomInfos = new ArrayList<>(playerNames.length);
        for (final String playerName : playerNames)
        {
            final PlacedTile[] placedTiles = ServerResponseParser.getPlayerPlacedTiles(gameState, playerName);
            kingdomInfos.add(new KingdomInfo(new Kingdom(ArrayUtils.toArrayList(placedTiles)), playerName));
        }

        final ArrayList<DraftElement> previousDraft = ArrayUtils.toArrayList(ServerResponseParser.getPreviousDraft(gameState));
        final ArrayList<DraftElement> currentDraft = ArrayUtils.toArrayList(ServerResponseParser.getCurrentDraft(gameState));

        return new GameState(kingdomInfos, previousDraft, currentDraft);
    }
}
