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

        game.waitForPlayersToJoin(TIMEOUT_MINUTES);
        game.waitForPlayersToFinish(TIMEOUT_MINUTES);

        showGameResult(game);
    }


    private static void showGameResult(final Game game)
    {
        game.showResult();
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

}
