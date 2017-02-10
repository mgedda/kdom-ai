
/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-08<br>
 * Time: 23:12<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class Game
{
    private final String iUUID;

    public Game(final String uuid)
    {
        iUUID = uuid;
    }

    public Player addPlayer(final String playerName)
    {
        final String response = CommunicationsHandler.joinGame(this, playerName);
        final String uuid = GameResponseParser.getUUID(response);

        final Player player = new Player(uuid, playerName);

        DEBUG.printPlayerJoined(this, playerName);

        return player;
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

    final String getCurrentPlayer()
    {
        final String gameState = CommunicationsHandler.getGameState(this);

        return GameResponseParser.getCurrentPlayer(gameState);
    }

    public boolean isGameOver()
    {
        final String gameState = CommunicationsHandler.getGameState(this);

        return GameResponseParser.isGameOver(gameState);
    }

    public Move[] getAvailableMoves()
    {
        final String moves = CommunicationsHandler.getAvailableMoves(this);

        return GameResponseParser.getAvailableMoves(moves);
    }

    final int getPlayerScore(final String playerName)
    {
        final String gameState = CommunicationsHandler.getGameState(this);

        return GameResponseParser.getPlayerScore(gameState, playerName);
    }

    public void showResult()
    {
        final String gameState = CommunicationsHandler.getGameState(this);
        final String[] playerNames = GameResponseParser.getPlayerNames(gameState);

        System.out.println("=================================================");
        System.out.println("RESULTS");
        System.out.println("-------------------------------------------------");

        for (final String playerName : playerNames)
        {
            System.out.println(playerName + ": " + getPlayerScore(playerName));
        }

        System.out.println("-------------------------------------------------");
    }

    public boolean allPlayersJoined()
    {
        return CommunicationsHandler.allPlayersJoined(this);
    }

    public void makeMove(final Player player, final Move move)
    {
        final String playerUUID = player.getUUID();
        final int moveNumber = move.getNumber();

        CommunicationsHandler.makeMove(this, playerUUID, moveNumber);
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
