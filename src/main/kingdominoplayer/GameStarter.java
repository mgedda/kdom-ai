package kingdominoplayer;

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

        final Game game = GameServer.startGame(numPlayers, "http://localhost/");

        GameServer.waitForPlayersToJoin(game, TIMEOUT_MINUTES);
        GameServer.waitForPlayersToFinish(game, TIMEOUT_MINUTES);

        showGameResult(game);
    }


    private static void showGameResult(final Game game)
    {
        game.printResult();
    }

}
