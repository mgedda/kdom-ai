package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts;

/*
 * Copyright 2018 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2018-01-05<br>
 * Time: 09:03<br><br>
 */

import java.util.ArrayDeque;
import java.util.ArrayList;

/*package*/ class UCTSTreeUtils
{
    public static double getUCB(final UCTSNode node, final double exploreFactor, double biasWeight)
    {
        final UCTSNode parent = node.getParent();

        final double exploit = (double) node.getWins() / (double) node.getVisits();
        final double explore = Math.sqrt(Math.log(parent.getVisits()) / (double) node.getVisits());

        // Win-persistent progressive bias
        double bias = 0.0;
        if (biasWeight > 0)
        {
            final String player = parent.getPlayerTurn();
            final int scoreBeforeMove = parent.getGameState().getScore(player);
            final int scoreAfterMove = node.getGameState().getScore(player);
            final double heuristic = scoreAfterMove - scoreBeforeMove;
            bias = biasWeight * heuristic / (node.getVisits() * (1.0 - exploit) + 1);
        }

        return exploit + exploreFactor * explore + bias;
    }


    public static void printTree(final UCTSNode root, final double exploreFactor, final double biasWeight)
    {
        System.out.println("Monte-Carlo Search Tree:");
        printTree(root, new ArrayList<>(), exploreFactor, biasWeight);
    }


    public static void printChildren(final UCTSNode node, final double exploreFactor, final double biasWeight)
    {
        int counter = 0;
        for (final UCTSNode child : node.getChildren())
        {
            final ArrayList<Integer> depthIndices = new ArrayList<>();
            depthIndices.add(counter++);
            System.out.println(child.toStringNestedBrackets(depthIndices, exploreFactor, biasWeight));
        }
    }

    private static void printTree(final UCTSNode node, final ArrayList<Integer> depthIndices, final double exploreFactor, final double biasWeight)
    {
        String nodeString = node.toStringNestedBrackets(depthIndices, exploreFactor, biasWeight);

        System.out.println(nodeString);


        final ArrayList<UCTSNode> children = node.getChildren();
        for (int i = 0; i < children.size(); ++i)
        {
            final ArrayList<Integer> indices = new ArrayList<>(depthIndices.size() + 1);
            indices.addAll(depthIndices);
            indices.add(i);

            printTree(children.get(i), indices, exploreFactor, biasWeight);
        }
    }

    public static void printTreeBFS(final UCTSNode root, final double exploreFactor, final double biasWeight)
    {
        printTreeBFS(root, -1, exploreFactor, biasWeight);
    }

    public static void printTreeBFS(final UCTSNode root, final long numNodes, final double exploreFactor, final double biasWeight)
    {
        final ArrayDeque<NodeDepthIndicesPair> nodeQueue = new ArrayDeque<>(1000);

        nodeQueue.add(new NodeDepthIndicesPair(root, new ArrayList<>()));

        long counter = 0;
        final long maxCount = numNodes == -1 ? Long.MAX_VALUE : numNodes;

        while (! nodeQueue.isEmpty() && counter < maxCount)
        {
            final NodeDepthIndicesPair current = nodeQueue.pop();
            final UCTSNode node = current.iNode;

            final String nodeString = node.toStringNestedBrackets(current.iDepthIndices, exploreFactor, biasWeight);
            System.out.println(nodeString);

            final ArrayList<UCTSNode> children = node.getChildren();
            for (int i = 0; i < children.size(); ++i)
            {
                final ArrayList<Integer> depthIndices = new ArrayList<>();
                depthIndices.addAll(current.iDepthIndices);
                depthIndices.add(i);

                nodeQueue.add(new NodeDepthIndicesPair(children.get(i), depthIndices));
            }

            counter++;
        }
    }


    private static class NodeDepthIndicesPair
    {
        UCTSNode iNode;
        ArrayList<Integer> iDepthIndices;

        public NodeDepthIndicesPair(final UCTSNode node, final ArrayList<Integer> depthIndices)
        {
            iNode = node;
            iDepthIndices = depthIndices;
        }
    }

}
