import java.io.IOException;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-09<br>
 * Time: 17:59<br><br>
 */
public class GameStarter
{
    private static final int TIMEOUT_MINUTES = 30;


    public static void main(final String[] args) throws IOException
    {
        if (args.length != 1)
        {
            System.err.println("Usage: java -jar startgame <numPlayers>");
            System.exit(0);
        }

        final int numPlayers = new Integer(args[0]);

        final Game game = startGame(numPlayers);

        waitForPlayersToJoin(game);

        waitForPlayersToFinish(game);

        showGameResult(game);
    }


    private static void showGameResult(final Game game)
    {
        game.showResult();
    }


    private static void waitForPlayersToFinish(final Game game)
    {
        final int sleepMilliSeconds = 1000;

        final int timeoutMilliSeconds = TIMEOUT_MINUTES * 60 * 1000;   // min * s/min * ms/s
        final int timeoutMaxCount = (int)((double)timeoutMilliSeconds / (double)sleepMilliSeconds);

        int timeoutCounter = 0;

        System.out.println("Waiting for players to finish.");

        while (! game.isGameOver() && timeoutCounter++ < timeoutMaxCount)
        {
            sleep(sleepMilliSeconds);
        }

        if (timeoutCounter >= timeoutMaxCount)
        {
            System.err.println("Error: Timed out!");
            System.exit(0);
        }

        System.out.println("Game finished!");
    }


    private static void waitForPlayersToJoin(final Game game)
    {
        final int sleepMilliSeconds = 1000;

        final int timeoutMilliSeconds = TIMEOUT_MINUTES * 60 * 1000;   // min * s/min * ms/s
        final int timeoutMaxCount = (int)((double)timeoutMilliSeconds / (double)sleepMilliSeconds);

        int timeoutCounter = 0;

        System.out.println("Waiting for all players to join game.");

        while(! game.allPlayersJoined() && timeoutCounter++ < timeoutMaxCount)
        {
            sleep(sleepMilliSeconds);
        }

        if (timeoutCounter >= timeoutMaxCount)
        {
            System.err.println("Error: Timed out!");
            System.exit(0);
        }

        System.out.println("All players joined!");
    }


    private static Game startGame(final int numPlayers)
    {
        if (numPlayers < 2 && numPlayers > 4)
        {
            System.err.println("Wrong number of players (numPlayers=" + numPlayers + ")");
            System.exit(0);
        }

        final String response = CommunicationsHandler.startNewGame(numPlayers);
        final String uuid = GameResponseParser.getUUID(response);

        final Game game = new Game(uuid);

        System.out.println("Game started! UUID: " + game.getUUID());

        return game;
    }


    @SuppressWarnings("SameParameterValue")
    private static void sleep(final int milliSeconds)
    {
        try {
            Thread.sleep(milliSeconds);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

}
