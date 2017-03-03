package kingdominoplayer.utils;

import kingdominoplayer.CommunicationsHandler;
import kingdominoplayer.Game;
import kingdominoplayer.GameServer;
import kingdominoplayer.Player;
import kingdominoplayer.ServerResponseParser;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-03<br>
 * Time: 19:44<br><br>
 */
public class Output
{
    private static boolean PRINT_GAME_PROGRESS = true;
    private static boolean PRINT_PLAYER_MOVES = true;
    private static boolean PRINT_BRANCHING_FACTOR = false;
    private static boolean PRINT_GAME_RESULT = true;


    public static void printBranchingFactor(final int moveNumber, final int numAvailableMoves)
    {
        if (PRINT_BRANCHING_FACTOR)
        {
            System.out.print(numAvailableMoves + ", ");
        }
    }



    public static void printMakingAMove(final Player player)
    {
        if (PRINT_PLAYER_MOVES)
        {
            System.out.print(player.getName() + ": Making a move...");
        }
    }

    public static void printMoveMade()
    {
        if (PRINT_PLAYER_MOVES)
        {
            System.out.print("move made!\n");
        }
    }

    public static void printWaiting(final Player player)
    {
        if (PRINT_PLAYER_MOVES)
        {
            System.out.print(player.getName() + ": Waiting for my turn...\n");
        }
    }


    public static void printPlayerJoined(final Game game, final String playerName)
    {
        if (PRINT_GAME_PROGRESS)
        {
            System.out.print("Player " + playerName + " joined game " + game.getUUID() + "\n");
        }
    }

    public static void printGameFinished()
    {
        if (PRINT_GAME_PROGRESS)
        {
            System.out.println("Game finished!");
        }
    }


    public static void printResult(final Game game)
    {
        if (PRINT_GAME_RESULT)
        {
            final String gameState = CommunicationsHandler.getGameState(game);
            final String[] playerNames = ServerResponseParser.getPlayerNames(gameState);

            System.out.println("=================================================");
            System.out.println("RESULTS");
            System.out.println("-------------------------------------------------");

            for (final String playerName : playerNames)
            {
                System.out.println(playerName + ": " + GameServer.getPlayerScore(game, playerName));
            }

            System.out.println("-------------------------------------------------");
        }
    }

}
