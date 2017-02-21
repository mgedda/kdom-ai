package kingdominoplayer;

import kingdominoplayer.datastructures.Move;
import kingdominoplayer.utils.Timing;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-21<br>
 * Time: 21:08<br><br>
 */
public class GameServer
{
    public static Game startGame(final int numPlayers)
    {
        if (numPlayers < 2 && numPlayers > 4)
        {
            System.err.println("Wrong number of players (numPlayers=" + numPlayers + ")");
            System.exit(0);
        }

        final String response = CommunicationsHandler.startNewGame(numPlayers);
        final String uuid = ServerResponseParser.getUUID(response);

        final Game game = new Game(uuid);

        System.out.println("Game started! UUID: " + game.getUUID());

        return game;
    }

    public static String getCurrentPlayer(final Game game)
    {
        final String gameState = CommunicationsHandler.getGameState(game);
        return ServerResponseParser.getCurrentPlayer(gameState);
    }

    public static boolean isGameOver(final Game game)
    {
        final String gameState = CommunicationsHandler.getGameState(game);
        return ServerResponseParser.isGameOver(gameState);
    }

    public static Move[] getAvailableMoves(final Game game)
    {
        final String moves = CommunicationsHandler.getAvailableMoves(game);
        return ServerResponseParser.getAvailableMoves(moves);
    }

    public static int getPlayerScore(final Game game, final String playerName)
    {
        final String gameState = CommunicationsHandler.getGameState(game);
        return ServerResponseParser.getPlayerScore(gameState, playerName);
    }

    public static boolean allPlayersJoined(final Game game)
    {
        return CommunicationsHandler.allPlayersJoined(game);
    }

    public static void makeMove(final Game game, final Player player, final Move move)
    {
        final String playerUUID = player.getUUID();
        final int moveNumber = move.getNumber();

        CommunicationsHandler.makeMove(game, playerUUID, moveNumber);
    }

    public static void waitForPlayersToJoin(final Game game, final int timeoutMinutes)
    {
        final int sleepMilliSeconds = 1000;

        final int timeoutMilliSeconds = timeoutMinutes * 60 * 1000;   // min * s/min * ms/s
        final int timeoutMaxCount = (int)((double)timeoutMilliSeconds / (double)sleepMilliSeconds);

        int timeoutCounter = 0;

        System.out.println("Waiting for all players to join game.");

        while(! allPlayersJoined(game) && timeoutCounter++ < timeoutMaxCount)
        {
            Timing.sleep(sleepMilliSeconds);
        }

        if (timeoutCounter >= timeoutMaxCount)
        {
            System.err.println("Error: Timed out!");
            System.exit(0);
        }

        System.out.println("All players joined!");

        final String gameState = CommunicationsHandler.getGameState(game);
        final String[] playerNames = ServerResponseParser.getPlayerNames(gameState);

        int counter = 0;
        for (final String playerName : playerNames)
        {
            System.out.println(Integer.toString(++counter) + ": " + playerName);
        }
    }

    public static void waitForPlayersToFinish(final Game game, final int timeoutMinutes)
    {
        final int sleepMilliSeconds = 1000;

        final int timeoutMilliSeconds = timeoutMinutes * 60 * 1000;   // min * s/min * ms/s
        final int timeoutMaxCount = (int)((double)timeoutMilliSeconds / (double)sleepMilliSeconds);

        int timeoutCounter = 0;

        System.out.println("Waiting for players to finish.");

        while (! isGameOver(game) && timeoutCounter++ < timeoutMaxCount)
        {
            Timing.sleep(sleepMilliSeconds);
        }

        if (timeoutCounter >= timeoutMaxCount)
        {
            System.err.println("Error: Timed out!");
            System.exit(0);
        }

        System.out.println("Game finished!");
    }

}
