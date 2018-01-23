package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts;

/*
 * Copyright 2018 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2018-01-23<br>
 * Time: 20:48<br><br>
 */

public class UCBWinPersistentProgressiveBias extends UCB
{

    private final double iBiasWeight;

    public UCBWinPersistentProgressiveBias(final double exploringFactor, final double biasWeight)
    {
        super(exploringFactor);
        iBiasWeight = biasWeight;
    }

    @Override
    public double getUCB(final UCTSNode node)
    {
        final UCTSNode parent = node.getParent();

        final double exploit = (double) node.getWins() / (double) node.getVisits();

        final String player = parent.getPlayerTurn();
        final int scoreBeforeMove = parent.getGameState().getScore(player);
        final int scoreAfterMove = node.getGameState().getScore(player);
        final double heuristic = scoreAfterMove - scoreBeforeMove;
        final double bias = iBiasWeight * heuristic / (node.getVisits() * (1.0 - exploit) + 1);

        return super.getUCB(node) + bias;
    }

}
