package kingdominoplayer;

import kingdominoplayer.datastructures.*;
import kingdominoplayer.gamecontents.GameContents;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-19<br>
 * Time: 20:17<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class GameStateHandler
{
    public static GameState getServerGameState(final Game game)
    {
        final String gameStateString = CommunicationsHandler.getGameState(game);
        return ServerResponseParser.getGameStateObject(gameStateString);
    }

    public static LocalGameState createLocalGameState(final Game game, final Set<Domino> drawnDominoes)
    {
        final GameState serverGameState = getServerGameState(game);

        final Set<Domino> drawPile = GameContents.getDominoes();
        drawPile.removeAll(drawnDominoes);

        final LocalGameState localGameState = new LocalGameState(
                serverGameState.getKingdomInfos(),
                serverGameState.getPreviousDraft(),
                serverGameState.getCurrentDraft(),
                serverGameState.isGameOver(),
                drawPile,
                false);

        return localGameState;
    }

    public static Set<Domino> getDraftDominoes(final Game game)
    {
        final GameState serverGameState = GameStateHandler.getServerGameState(game);

        final LinkedHashSet<Domino> dominoes = new LinkedHashSet<>(8);

        for (final DraftElement draftElement : serverGameState.getPreviousDraft())
        {
            dominoes.add(draftElement.getDomino());
        }

        for (final DraftElement draftElement : serverGameState.getCurrentDraft())
        {
            dominoes.add(draftElement.getDomino());
        }

        return dominoes;
    }

}
