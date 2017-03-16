package kingdominoplayer;

import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.GameState;
import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.strategies.StrategyID;
import kingdominoplayer.utils.Output;
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

    public GameState play(final Collection<Player> players)
    {
        final int sleepMilliSeconds = 1000;
        final int timeoutMilliSeconds = TIMEOUT_MINUTES * 60 * 1000;   // min * s/min * ms/s
        final int timeoutMaxCount = (int)((double)timeoutMilliSeconds / (double)sleepMilliSeconds);

        Set<Domino> drawnDominoes = GameServer.getDraftDominoes(this);

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
