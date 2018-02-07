package kingdominoplayer;

import kingdominoplayer.naiverepresentation.datastructures.GameState;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-06<br>
 * Time: 19:47<br><br>
 */
public class ExperimentEngine
{
    public static void main(final String[] args) throws IOException
    {
        final ExperimentParameters parameters = ExperimentParameters.from(args);

        final String server = "http://localhost";

        final Game game = GameServer.startGame(4, server);

        final String opponent1Name = "Opponent1";
        final String opponent2Name = "Opponent2";
        final String opponent3Name = "Opponent3";
        final String playerName = "Player";

        final String opponent1UUID = game.addPlayer(opponent1Name);
        final String opponent2UUID = game.addPlayer(opponent2Name);
        final String opponent3UUID = game.addPlayer(opponent3Name);

        final String playerUUID = game.addPlayer(playerName);
        final TinyPlayer player = new TinyPlayer(playerUUID, playerName, parameters.getPlayerStrategy(), false);

        final ArrayList<Player> players = new ArrayList<>(4);
        players.add(new TinyPlayer(opponent1UUID, opponent1Name, parameters.getOpponentStrategy(), false));
        players.add(new TinyPlayer(opponent2UUID, opponent2Name, parameters.getOpponentStrategy(), false));
        players.add(new TinyPlayer(opponent3UUID, opponent3Name, parameters.getOpponentStrategy(), false));
        players.add(player);

        final GameState gameState = game.play(players);

        game.printResult();

        if (parameters.hasOutputFile())
        {
            final ExperimentResult experimentResult = new ExperimentResult(gameState, player);
            experimentResult.appendToFile(parameters.getOutputFile());
        }
    }
}
