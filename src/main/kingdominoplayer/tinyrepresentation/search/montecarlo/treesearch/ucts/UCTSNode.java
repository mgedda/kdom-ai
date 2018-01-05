package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts;

/*
 * Copyright 2018 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2018-01-05<br>
 * Time: 08:49<br><br>
 */

import com.sun.istack.internal.Nullable;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

import java.util.ArrayList;

/*package*/ class UCTSNode
{
    int visits;
    UCTSReward reward;                 // accumulated wins/draws/losses for each player [id1 id2 id3 id4]
    int wins;
    final TinyGameState gameState;
    final byte[] move;             // Action leading from parent to this node
    final String playerName;       // Player who's turn it is
    final UCTSNode parent;
    ArrayList<UCTSNode> children;

    UCTSNode(final TinyGameState gameState,
         @Nullable final UCTSNode parent,
         final byte[] move)
    {
        this(gameState, parent, move, gameState.getPlayerTurn());
    }

    UCTSNode(final TinyGameState gameState,
         @Nullable final UCTSNode parent,
         final byte[] move,
         final String playerName)
    {
        final String playerTurn = gameState.getPlayerTurn();
        assert playerTurn.equals(playerName) : "Player turn mismatch";
        visits = 1;
        wins = 0;
        reward = new UCTSReward(new double[gameState.getNumPlayers()]);
        this.gameState = gameState;
        this.move = move;
        this.playerName = playerName;
        this.parent = parent;
        children = new ArrayList<>(0);
    }

    boolean isFullyExpanded()
    {
        final byte[] availableMoves = gameState.getAvailableMoves(playerName);
        final int numAvailableMoves = availableMoves.length / TinyConst.MOVE_ELEMENT_SIZE;
        assert children.size() <= numAvailableMoves : "Too many children";
        return children.size() == numAvailableMoves;
    }

    boolean isTerminal()
    {
        return gameState.isGameOver();
    }

    double getReward()
    {
        final String playerTurn = parent == null
                ? gameState.getPlayerTurn()           // we are at the root node
                : parent.gameState.getPlayerTurn();
        final byte playerID = TinyGameState.getPlayerID(playerTurn, gameState.getPlayers());
        return reward.get(playerID);
    }



    public String toStringNestedBrackets(final ArrayList<Integer> depthIndices, final double exploreFactor)
    {
        String nodeString = "";

        if (! depthIndices.isEmpty())
        {
            nodeString = nodeString.concat("{");

            for (int i = 0; i < depthIndices.size(); ++i)
            {
                nodeString = nodeString.concat(Integer.toString(depthIndices.get(i)));

                if (i < depthIndices.size() - 1)
                {
                    nodeString = nodeString.concat(", ");
                }
            }
            nodeString = nodeString.concat("}");
        }

        nodeString = nodeString.concat(" ").concat(Integer.toString(wins)).concat("/").concat(Integer.toString(visits));
        nodeString = visits > 0
                ? nodeString.concat(", Avg: ").concat(String.format("%.5f", wins/(double)visits))
                : nodeString.concat(", Avg: 0");

        if (parent != null)
        {
            final double upperConfidenceBound = UCTSTreeUtils.getUCB(this, exploreFactor);
            nodeString = nodeString.concat(", UCB: ").concat(String.format("%.5f", upperConfidenceBound));
        }

        final String parentName = parent == null ? "-" : parent.playerName;
        nodeString = nodeString.concat(" [parent: ").concat(parentName).concat(", player: ").concat(playerName).concat("]");
        return nodeString;
    }

}
