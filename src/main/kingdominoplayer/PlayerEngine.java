package kingdominoplayer;

import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.gamecontents.GameContents;
import kingdominoplayer.datastructures.ExtendedGameState;
import kingdominoplayer.datastructures.GameState;
import kingdominoplayer.utils.Timing;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
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

        final ExtendedGameState gameState = initGameState(game);

        makeMoves(game, player);

        System.out.println("kingdominoplayer.Player " + player.getName() + " leaving (game finished).");
    }


    private static ExtendedGameState initGameState(final Game game)
    {
        final String gameStateString = CommunicationsHandler.getGameState(game);
        final GameState gameState = GameResponseParser.getGameStateObject(gameStateString);

        final Set<Domino> drawPile = GameContents.getDominoes();
        final ArrayList<Domino> dominoesInCurrentDraft = gameState.getDominoesInCurrentDraft();
        drawPile.removeAll(dominoesInCurrentDraft);

        return new ExtendedGameState(
                gameState.getKingdomInfos(),
                gameState.getPreviousDraft(),
                gameState.getCurrentDraft(),
                gameState.isGameOver(),
                drawPile,
                new LinkedHashSet<>(drawPile.size()),
                false);
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
            Timing.sleep(sleepMilliSeconds);

            timeoutCounter = moveWasMade? 0 : timeoutCounter;
        }

        if (timeoutCounter >= timeoutMaxCount)
        {
            System.err.println("Error: Timed out!");
            System.exit(0);
        }
    }
}
