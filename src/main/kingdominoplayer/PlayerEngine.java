package kingdominoplayer;

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
        String playerName = "";
        String gameUUID = "";
        String strategy = "RANDOM";

        if (args.length == 2)
        {
            playerName = args[0];
            gameUUID = args[1];
        }
        else if (args.length == 3)
        {
            playerName = args[0];
            strategy = args[1];
            gameUUID = args[2];
        }
        else
        {
            System.err.println("Usage: java -jar spawnplayer <playerName> [<strategy>] <gameUUID>");
            System.exit(0);
        }

        final Game game = new Game(gameUUID);
        final Player player = game.addPlayer(playerName, strategy);

        game.waitForPlayersToJoin(TIMEOUT_MINUTES);

        makeMoves(game, player);

        System.out.println("kingdominoplayer.Player " + player.getName() + " leaving (game finished).");
    }


    private static void makeMoves(final Game game, final Player player)
    {
        final int sleepMilliSeconds = 1000;
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
