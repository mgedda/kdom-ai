package kingdominoplayer.tinyrepresentation.simulationstrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyFullGreedy;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategy;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyTrueRandom;

/**
 * Created by gedda on 4/8/17.
 */
public class TinyPlayerGreedySimulationStrategy implements TinySimulationStrategy
{

    private final static TinyStrategy cGreedyStrategy = new TinyFullGreedy();
    private final static TinyStrategy cRandomStrategy = new TinyTrueRandom();

    @Override
    public byte[] selectMove(final String playerName, final byte[] availableMoves, final TinyGameState gameState)
    {
        final byte[] move;

        if (gameState.getPlayerTurn().equals(playerName))
        {
            move = cGreedyStrategy.selectMove(playerName, availableMoves, gameState);
        }
        else
        {
            move = cRandomStrategy.selectMove(playerName, availableMoves, gameState);
        }

        return move;
    }
}
