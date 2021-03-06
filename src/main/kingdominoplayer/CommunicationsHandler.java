package kingdominoplayer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-08<br>
 * Time: 22:45<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class CommunicationsHandler
{
    public static String startNewGame(final int numPlayers, final String server)
    {
        assert numPlayers > 1 && numPlayers < 5 : "wrong number of players (numPlayers=)" + numPlayers + ")";

        final String url = server + "/new-games/?playerCount=" + numPlayers;
        return sendPostRequest(url);
    }


    @SuppressWarnings("UnnecessaryLocalVariable")
    public static String joinGame(final Game game, final String playerName)
    {
        final String url = game.getServer() + "/new-games/" + game.getUUID() + "/join/" + playerName;
        final String response = sendPostRequest(url);

        return response;
    }


    public static String getGameState(final Game game)
    {
        final String url = game.getServer() + "/games/" + game.getUUID();

        return sendGetRequest(url);
    }

    public static String getAvailableMoves(final Game game)
    {
        final String url = game.getServer() + "/games/" + game.getUUID() + "/available-moves";

        return sendGetRequest(url);
    }

    public static boolean allPlayersJoined(final Game game)
    {
        final String url = game.getServer() + "/games/" + game.getUUID();

        try
        {
            final URL urlObj = new URL(url);
            final HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

            conn.setRequestMethod("GET");
            final InputStream inputStream = conn.getInputStream();

            @SuppressWarnings("UnnecessaryLocalVariable")
            final String response = convertStreamToString(inputStream);

            return true;
        }
        catch (Exception e)
        {
            return false;
            //e.printStackTrace();
        }
    }

    private static String sendGetRequest(final String url)
    {
        try
        {
            final URL urlObj = new URL(url);
            final HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

            conn.setRequestMethod("GET");
            final InputStream inputStream = conn.getInputStream();

            @SuppressWarnings("UnnecessaryLocalVariable")
            final String response = convertStreamToString(inputStream);

            return response;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return "";
    }

    private static String sendPostRequest(final String url)
    {
        try
        {
            final URL urlObj = new URL(url);
            final HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

            conn.setRequestMethod("POST");
            final InputStream inputStream = conn.getInputStream();

            @SuppressWarnings("UnnecessaryLocalVariable")
            final String response = convertStreamToString(inputStream);

            return response;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return "";
    }

    private static String convertStreamToString(final InputStream is)
    {
        try(java.util.Scanner s = new java.util.Scanner(is))
        {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        }
    }

    public static void makeMove(final Game game, final String playerUUID, final int moveNumber)
    {
        final String url = game.getServer() + "/games/" + game.getUUID() + "/players/" + playerUUID + "/moves/" + moveNumber;
        sendPostRequest(url);
    }
}
