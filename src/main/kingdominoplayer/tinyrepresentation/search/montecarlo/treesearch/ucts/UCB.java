package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts;

/*
 * Copyright 2018 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2018-01-23<br>
 * Time: 20:46<br><br>
 */

public class UCB
{

    protected final double iExplorationFactor;

    public UCB(final double explorationFactor)
    {
        iExplorationFactor = explorationFactor;
    }

    public double getUCB(final UCTSNode node)
    {
        final UCTSNode parent = node.getParent();

        final double exploit = (double) node.getWins() / (double) node.getVisits();
        final double explore = Math.sqrt(Math.log(parent.getVisits()) / (double) node.getVisits());

        return exploit + iExplorationFactor * explore;
    }

}
