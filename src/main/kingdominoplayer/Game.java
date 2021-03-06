package kingdominoplayer;

import kingdominoplayer.naiverepresentation.datastructures.Domino;
import kingdominoplayer.naiverepresentation.datastructures.GameState;
import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.utils.Output;
import kingdominoplayer.utils.Timing;

import java.util.Collection;
import java.util.Set;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-08<br>
 * Time: 23:12<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class Game
{
    private static final double POLL_EVERY_X_SECONDS = 0.1;
    private static final int TIMEOUT_MINUTES = 30;

    private final String iUUID;
    private final String iServer;

    public Game(final String uuid, final String server)
    {
        iUUID = uuid;
        iServer = server;
    }

    public String getUUID()
    {
        return iUUID;
    }

    public String getServer()
    {
        return iServer;
    }

    @Override
    public String toString()
    {
        return "Game{" +
                "iUUID='" + iUUID + '\'' +
                '}';
    }

    public GameState play(final Collection<Player> players)
    {
        final int sleepMilliSeconds = (int)(1000 * POLL_EVERY_X_SECONDS);
        final int timeoutMilliSeconds = TIMEOUT_MINUTES * 60 * 1000;   // min * s/min * ms/s
        final int timeoutMaxCount = (int)((double)timeoutMilliSeconds / (double)sleepMilliSeconds);

        final Set<Domino> drawnDominoes = GameServer.getDraftDominoes(this);

        int timeoutCounter = 0;
        while (! GameServer.isGameOver(this) && timeoutCounter++ < timeoutMaxCount)
        {
            for (final Player player : players)
            {
                if (GameServer.isGameOver(this))
                {
                    break;
                }

                final String currentPlayer = GameServer.getCurrentPlayer(this);
                if (currentPlayer.equals(player.getName()))
                {
                    // Update dominoes drawn.
                    //
                    final Set<Domino> draftDominoes = GameServer.getDraftDominoes(this);
                    drawnDominoes.addAll(draftDominoes);

                    // Create local game state.
                    //
                    final LocalGameState localGameState = GameStateHandler.createLocalGameState(this, drawnDominoes);

                    // Make move for player.
                    //
                    Output.printMakingAMove(player);
                    player.makeAMove(this, localGameState);
                    Output.printMoveMade();

                    // Reset time out counter.
                    //
                    timeoutCounter = 0;
                }
                else
                {
                    // Declare that we are waiting for our turn.
                    //
                    Output.printWaiting(player);
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

        Output.printGameFinished();

        return GameStateHandler.getServerGameState(this);
    }


    public String addPlayer(final String playerName)
    {
        final String response = CommunicationsHandler.joinGame(this, playerName);
        final String uuid = ServerResponseParser.getUUID(response);

        Output.printPlayerJoined(this, playerName);

        return uuid;
    }

    public void printResult()
    {
        Output.printResult(this);
    }
}
