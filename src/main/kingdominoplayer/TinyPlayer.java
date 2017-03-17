package kingdominoplayer;

import kingdominoplayer.datastructures.LocalGameState;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.strategies.tinystrategies.TinyStrategy;
import kingdominoplayer.strategies.tinystrategies.TinyStrategyID;
import kingdominoplayer.tinyrepresentation.LocalGameStateToTinyGameStateAlgorithm;
import kingdominoplayer.tinyrepresentation.MoveAdapter;
import kingdominoplayer.tinyrepresentation.TinyConst;
import kingdominoplayer.tinyrepresentation.TinyGameState;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 15:02<br><br>
 */
public class TinyPlayer extends Player
{

    private final TinyStrategy iStrategy;

    public TinyPlayer(final String uuid, final String name, final TinyStrategyID strategyID, final boolean enableDebug)
    {
        super(uuid, name, null, enableDebug);

        iStrategy = strategyID.getStrategy();
    }

    @Override
    protected Move selectMove(final Move[] availableMoves, final LocalGameState localGameState)
    {
        final byte[] moves = new byte[availableMoves.length * TinyConst.MOVE_ELEMENT_SIZE];

        for (int i = 0; i < availableMoves.length; ++i)
        {
            final byte[] move = MoveAdapter.toTinyRepresentation(availableMoves[i]);
            System.arraycopy(move, 0, moves, i * TinyConst.MOVE_ELEMENT_SIZE, TinyConst.MOVE_ELEMENT_SIZE);
        }

        final TinyGameState tinyGameState = new LocalGameStateToTinyGameStateAlgorithm().applyTo(localGameState);
        final byte[] selectedMove = iStrategy.selectMove(getName(), moves, tinyGameState);

        return MoveAdapter.toMove(selectedMove);
    }
}
