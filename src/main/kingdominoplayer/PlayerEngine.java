package kingdominoplayer;

import kingdominoplayer.datastructures.*;
import kingdominoplayer.utils.Timing;

import java.io.*;
import java.util.Set;


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
        final String usage = "Usage: java -jar spawnplayer [-d] <playerName> <strategy> <gameUUID>";

        String playerName = "";
        String gameUUID = "";
        String strategy = "";
        boolean enableDebug = false;

        if (args.length == 3)
        {
            playerName = args[0];
            strategy = args[1];
            gameUUID = args[2];
        }
        else if (args.length == 4)
        {
            if (! args[0].equals("-d"))
            {
                System.err.println(usage);
                System.exit(0);
            }

            enableDebug = true;
            playerName = args[1];
            strategy = args[2];
            gameUUID = args[3];
        }
        else
        {
            System.err.println(usage);
            System.exit(0);
        }

        final Game game = new Game(gameUUID);
        final Player player = game.addPlayer(playerName, strategy, enableDebug);

        game.waitForPlayersToJoin(TIMEOUT_MINUTES);

        makeMoves(game, player);

        System.out.println("Player " + player.getName() + " leaving (game finished).");
    }




    private static void makeMoves(final Game game, final Player player)
    {
        final int sleepMilliSeconds = 1000;
        final int timeoutMilliSeconds = TIMEOUT_MINUTES * 60 * 1000;   // min * s/min * ms/s
        final int timeoutMaxCount = (int)((double)timeoutMilliSeconds / (double)sleepMilliSeconds);

        Set<Domino> drawnDominoes = GameStateHandler.getDraftDominoes(game);

        int timeoutCounter = 0;
        while (! game.isGameOver() && timeoutCounter++ < timeoutMaxCount)
        {
            if (game.getCurrentPlayer().equals(player.getName()))
            {
                // Update dominoes drawn.
                //
                final Set<Domino> draftDominoes = GameStateHandler.getDraftDominoes(game);
                drawnDominoes.addAll(draftDominoes);

                // Create local game state.
                //
                final LocalGameState localGameState = GameStateHandler.createLocalGameState(game, drawnDominoes);

                // Make move for player.
                //
                player.makeAMove(game, localGameState);

                // Reset time out counter.
                //
                timeoutCounter = 0;
            }
            else
            {
                // Declare that we are waiting for our turn.
                //
                OUTPUT.printWaiting(player);
            }

            // Wait a while between polls.
            //
            Timing.sleep(sleepMilliSeconds);
        }

        if (timeoutCounter >= timeoutMaxCount)
        {
            System.err.println("Error: Timed out!");
            System.exit(0);
        }
    }

    private static class OUTPUT
    {
        private static boolean OUTPUT = true;

        private static void print(final String msg)
        {
            if (OUTPUT)
            {
                System.out.print(msg);
            }
        }

        public static void printWaiting(final Player player)
        {
            print(player.getName() + ": Waiting for my turn...\n");
        }
    }
}
