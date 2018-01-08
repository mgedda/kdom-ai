package kingdominoplayer;

import kingdominoplayer.naiverepresentation.datastructures.LocalGameState;
import kingdominoplayer.naiverepresentation.datastructures.Move;
import kingdominoplayer.tinyrepresentation.adapters.MoveMatcher;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategy;
import kingdominoplayer.tinyrepresentation.adapters.LocalGameStateToTinyGameStateAlgorithm;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 15:02<br><br>
 */
public class TinyPlayer extends Player
{

    private final TinyStrategy iGameStrategy;

    public TinyPlayer(final String uuid, final String name, final TinyStrategy gameStrategy, final boolean enableDebug)
    {
        super(uuid, name, null, enableDebug);

        iGameStrategy = gameStrategy;
    }

    @Override
    protected Move selectMove(final Move[] availableMoves, final LocalGameState localGameState, final int roundNumber)
    {
        final TinyGameState tinyGameState = new LocalGameStateToTinyGameStateAlgorithm().applyTo(localGameState);
        final byte[] selectedMove = iGameStrategy.selectMove(getName(), tinyGameState);

        getNumPlayoutsPerSecond()[roundNumber - 1] = iGameStrategy.getNumPlayoutsPerSecond();
        Move result = MoveMatcher.getCorrespondingMove(availableMoves, selectedMove);

        return result;
    }

}
