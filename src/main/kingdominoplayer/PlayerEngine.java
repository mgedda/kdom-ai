package kingdominoplayer;

import java.io.*;
import java.util.ArrayList;


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

        final ArrayList<Player> players = new ArrayList<>(1);
        players.add(game.addPlayer(playerName, strategy, enableDebug));

        GameServer.waitForPlayersToJoin(game, TIMEOUT_MINUTES);

        game.makeMoves(players);

        System.out.println("Player " + players.get(0).getName() + " leaving (game finished).");
    }
}
