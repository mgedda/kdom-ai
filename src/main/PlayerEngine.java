import java.io.*;


/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-08<br>
 * Time: 21:54<br><br>
 */
public class PlayerEngine
{
    private static final int TIMEOUT_MINUTES = 30;


    public static void main(final String[] args) throws IOException
    {
        if (args.length != 2)
        {
            System.err.println("Usage: java -jar spawnplayer <playerName> <gameUUID>");
            System.exit(0);
        }

        final String playerName = args[0];
        final String gameUUID = args[1];

        final Game game = new Game(gameUUID);
        final Player player = game.addPlayer(playerName);

        game.waitForPlayersToJoin(TIMEOUT_MINUTES);

        makeMoves(game, player);

        System.out.println("Player " + player.getName() + " leaving (game finished).");
    }


    private static void makeMoves(final Game game, final Player player)
    {
        final int sleepMilliSeconds = 2000;
        final int timeoutMilliSeconds = TIMEOUT_MINUTES * 60 * 1000;   // min * s/min * ms/s
        final int timeoutMaxCount = (int)((double)timeoutMilliSeconds / (double)sleepMilliSeconds);

        int timeoutCounter = 0;

        while (! game.isGameOver() && timeoutCounter++ < timeoutMaxCount)
        {
            final boolean moveWasMade = player.makeAMove(game);
            sleep(sleepMilliSeconds);

            timeoutCounter = moveWasMade? 0 : timeoutCounter;
        }

        if (timeoutCounter >= timeoutMaxCount)
        {
            System.err.println("Error: Timed out!");
            System.exit(0);
        }
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
