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
import java.util.Map;

/*package*/ class UCTSNode
{
    //private UCTSReward reward;                 // accumulated iWins/draws/losses for each player [id1 id2 id3 id4]

    private int iWins;
    private int iVisits;

    private final byte[] iMove;                  // Action leading from iParent to this node
    private final TinyGameState iGameState;

    private final UCTSNode iParent;
    private ArrayList<UCTSNode> iChildren;


    UCTSNode(final TinyGameState gameState, @Nullable final UCTSNode parent, final byte[] move)
    {
        iWins = 0;
        iVisits = 1;

        //reward = new UCTSReward(new double[iGameState.getNumPlayers()]);

        iMove = move;
        iGameState = gameState;

        iParent = parent;
        iChildren = new ArrayList<>(0);
    }

    public int getWins()
    {
        return iWins;
    }

    public int getVisits()
    {
        return iVisits;
    }

    public byte[] getMove()
    {
        return iMove;
    }

    public UCTSNode getParent()
    {
        return iParent;
    }

    public ArrayList<UCTSNode> getChildren()
    {
        return iChildren;
    }

    public void addChild(final UCTSNode child)
    {
        iChildren.add(child);
    }

    public TinyGameState getGameState()
    {
        return iGameState;
    }

    public String getPlayerTurn()
    {
        return iGameState.getPlayerTurn();
    }

    public void increaseVisits()
    {
        iVisits++;
    }

    public void increaseWins()
    {
        iWins++;
    }

    public boolean isFullyExpanded()
    {
        final byte[] availableMoves = iGameState.getAvailableMoves(getPlayerTurn());
        final int numAvailableMoves = availableMoves.length / TinyConst.MOVE_ELEMENT_SIZE;
        assert iChildren.size() <= numAvailableMoves : "Too many children";
        return iChildren.size() == numAvailableMoves;
    }

    public boolean isTerminal()
    {
        return iGameState.isGameOver();
    }

    /*
    public double getReward()
    {
        final String playerTurn = iParent == null
                ? iGameState.getPlayerTurn()           // we are at the root node
                : iParent.iGameState.getPlayerTurn();
        final byte playerID = TinyGameState.getPlayerID(playerTurn, iGameState.getPlayers());
        return reward.get(playerID);
    }
    */



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

        nodeString = nodeString.concat(" ").concat(Integer.toString(iWins)).concat("/").concat(Integer.toString(iVisits));
        nodeString = iVisits > 0
                ? nodeString.concat(", Avg: ").concat(String.format("%.3f", iWins /(double) iVisits))
                : nodeString.concat(", Avg: 0");

        if (iParent != null)
        {
            final double upperConfidenceBound = UCTSTreeUtils.getUCB(this, exploreFactor);
            nodeString = nodeString.concat(", UCB: ").concat(String.format("%.5f", upperConfidenceBound));
        }

        //final String parentName = iParent == null ? "-" : iParent.getPlayerTurn();
        //nodeString = nodeString.concat(" [iParent: ").concat(parentName).concat(", player: ").concat(getPlayerTurn()).concat("]");

        final Map<String, Integer> scoresMap = iGameState.getScores();
        nodeString = nodeString.concat(" [");
        for (final String name : scoresMap.keySet())
        {
            nodeString = nodeString.concat(" " + name + ":" + scoresMap.get(name));
        }
        nodeString = nodeString.concat(" ]");

        return nodeString;
    }
}
