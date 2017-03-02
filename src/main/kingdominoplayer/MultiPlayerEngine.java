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
        final Game game = GameServer.startGame(3);

        final ArrayList<Player> players = new ArrayList<>(4);
        players.add(game.addPlayer("RandomCalrissian1", StrategyID.RANDOM, false));
        players.add(game.addPlayer("RandomCalrissian2", StrategyID.RANDOM, false));
        players.add(game.addPlayer("TyrionPlannister", StrategyID.LOOK_AHEAD_MONTE_CARLO, false));

        game.play(players);

        System.out.println("Game finished!");

        game.printResult();
    }

}
