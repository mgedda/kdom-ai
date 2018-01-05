package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.mcts;

import com.sun.istack.internal.Nullable;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-20<br>
 * Time: 14:58<br><br>
 */
/*package*/ class MCTSNode
{
    private final TinyGameState iGameState;
    private final String iPlayerName;

    private final MCTSNode iParent;             // Parent node
    private final byte[] iMove;                 // Action leading from parent to this node

    private int iVisits;
    private int iWins;

    private ArrayList<MCTSNode> iChildren;

    public MCTSNode(final TinyGameState gameState, @Nullable final MCTSNode parent, final byte[] move)
    {
        this(gameState, parent, move, gameState.getPlayerTurn());
    }

    public MCTSNode(final TinyGameState gameState, @Nullable final MCTSNode parent, final byte[] move, final String playerName)
    {
        iGameState = gameState;
        iPlayerName = playerName;

        iParent = parent;
        iMove = move;

        iVisits = 0;
        iWins = 0;

        iChildren = new ArrayList<>(0);
    }

    public boolean isExpanded()
    {
        return iVisits > 0;
    }

    public void expand()
    {
        assert !isExpanded() : "Node already expanded!";
        iVisits++;
    }

    public TinyGameState getGameState()
    {
        return iGameState;
    }

    public String getPlayerName()
    {
        return iPlayerName;
    }

    public void setChildren(final ArrayList<MCTSNode> children)
    {
        iChildren = children;
    }

    public ArrayList<MCTSNode> getChildren()
    {
        return iChildren;
    }

    public MCTSNode getParent()
    {
        return iParent;
    }

    public byte[] getMove()
    {
        return iMove;
    }


    public int getVisits()
    {
        return iVisits;
    }

    public int getWins()
    {
        return iWins;
    }

    public void updateResult(final MCTSResult result)
    {
        final String playerTurn = iParent == null
                ? getGameState().getPlayerTurn()           // we are at the root node
                : iParent.getGameState().getPlayerTurn();
        final byte playerID = TinyGameState.getPlayerID(playerTurn, iGameState.getPlayers());
        final double playerResult = result.getResult(playerID);

        iWins += playerResult == 1 ? 1 : 0;
        iVisits++;
    }
}
