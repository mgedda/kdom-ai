package kingdominoplayer;

import kingdominoplayer.naiverepresentation.strategies.StrategyID;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategyFactory;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategyID;

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
        final SearchParameters searchParameters = new SearchParameters(0, 10);

        final String player1Name = "SirPranceALot1";
        final String player2Name = "SirPranceALot2";
        final String player3Name = "SirPranceALot3";
        final String player4Name = "TyrionPlannister";

        final String player1UUID = game.addPlayer(player1Name);
        final String player2UUID = game.addPlayer(player2Name);
        final String player3UUID = game.addPlayer(player3Name);
        final String player4UUID = game.addPlayer(player4Name);

        final ArrayList<Player> players = new ArrayList<>(4);
        players.add(new Player(player1UUID, player1Name, StrategyID.FULL_GREEDY, false));
        players.add(new Player(player2UUID, player2Name, StrategyID.FULL_GREEDY, false));
        players.add(new Player(player3UUID, player3Name, StrategyID.FULL_GREEDY, false));
        players.add(new TinyPlayer(player4UUID, player4Name, new TinyStrategyFactory(searchParameters).getGameStrategy(TinyStrategyID.FULL_GREEDY), false));

        game.play(players);

        game.printResult();
    }

}
