package kingdominoplayer;

import kingdominoplayer.strategies.StrategyID;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-21<br>
 * Time: 20:28<br><br>
 */
public class MultiPlayerEngine
{
    public static void main(final String[] args) throws IOException
    {
        final Game game = GameServer.startGame(4);

        final ArrayList<Player> players = new ArrayList<>(4);
        //players.add(game.addPlayer("RandomCalrissian1", StrategyID.RANDOM, false));
        //players.add(game.addPlayer("RandomCalrissian2", StrategyID.RANDOM, false));
        //players.add(game.addPlayer("RandomCalrissian3", StrategyID.RANDOM, false));
        //players.add(game.addPlayer("RandomCalrissian4", StrategyID.RANDOM, true));
        players.add(game.addPlayer("SirPranceALot1", StrategyID.LOOK_AHEAD_RANDOM, false));
        players.add(game.addPlayer("SirPranceALot2", StrategyID.LOOK_AHEAD_RANDOM, false));
        players.add(game.addPlayer("SirPranceALot3", StrategyID.LOOK_AHEAD_RANDOM, false));
        players.add(game.addPlayer("TyrionPlannister", StrategyID.LOOK_AHEAD_MONTE_CARLO, true));

        game.play(players);

        game.printResult();
    }

}