package kingdominoplayer;

import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.strategies.StrategyID;
import kingdominoplayer.utils.Timing;

import java.util.Collection;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-08<br>
 * Time: 23:12<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class Game
{
    private static final int TIMEOUT_MINUTES = 30;

    private final String iUUID;

    public Game(final String uuid)
    {
        iUUID = uuid;
    }

    public String getUUID()
    {
        return iUUID;
    }

    @Override
    public String toString()
    {
        return "Game{" +
                "iUUID='" + iUUID + '\'' +
                '}';
    }

    public void play(final Collection<Player> players)
    {
        final int sleepMilliSeconds = 1000;
        final int timeoutMilliSeconds = TIMEOUT_MINUTES * 60 * 1000;   // min * s/min * ms/s
        final int timeoutMaxCount = (int)((double)timeoutMilliSeconds / (double)sleepMilliSeconds);

        Set<Domino> drawnDominoes = GameStateHandler.getDraftDominoes(this);

        int timeoutCounter = 0;
        while (! GameServer.isGameOver(this) && timeoutCounter++ < timeoutMaxCount)
        {
            for (final Player player : players)
            {
                if (GameServer.isGameOver(this))
                {
                    break;
                }

                if (GameServer.getCurrentPlayer(this).equals(player.getName()))
                {
                    // Update dominoes drawn.
                    //
                    final Set<Domino> draftDominoes = GameStateHandler.getDraftDominoes(this);
                    drawnDominoes.addAll(draftDominoes);

                    // Create local game state.
                    //
                    final LocalGameState localGameState = GameStateHandler.createLocalGameState(this, drawnDominoes);

                    // Make move for player.
                    //
                    player.makeAMove(this, localGameState);

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


    public Player addPlayer(final String playerName, final StrategyID strategyID, final boolean enableDebug)
    {
        final String response = CommunicationsHandler.joinGame(this, playerName);
        final String uuid = ServerResponseParser.getUUID(response);

        final Player player = new Player(uuid, playerName, strategyID, enableDebug);

        DEBUG.printPlayerJoined(this, playerName);

        return player;
    }

    public void printResult()
    {
        final String gameState = CommunicationsHandler.getGameState(this);
        final String[] playerNames = ServerResponseParser.getPlayerNames(gameState);

        System.out.println("=================================================");
        System.out.println("RESULTS");
        System.out.println("-------------------------------------------------");

        for (final String playerName : playerNames)
        {
            System.out.println(playerName + ": " + GameServer.getPlayerScore(this, playerName));
        }

        System.out.println("-------------------------------------------------");
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

    private static class DEBUG
    {
        private static boolean DEBUG = true;

        private static void print(final String msg)
        {
            if (DEBUG)
            {
                System.out.print(msg);
            }
        }

        public static void printGameStarted(final Game game)
        {
            print("Game started! UUID: " + game.getUUID() + "\n");
        }

        public static void printPlayerJoined(final Game game, final String playerName)
        {
            print("Player " + playerName + " joined game " + game.getUUID() + "\n");
        }
    }
}
