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
    public static double getUCB(final UCTSNode node, final double exploreFactor)
    {
        final int parentVisits = node.parent.visits;

        final double exploit = (double) node.wins / (double) node.visits;
        final double explore = Math.sqrt(Math.log(2 * parentVisits) / (double) node.visits);

        return exploit + exploreFactor * explore;
    }


    private void printTree(final UCTSNode root, final double exploreFactor)
    {
        System.out.println("Monte-Carlo Search Tree:");
        printTree(root, new ArrayList<>(), exploreFactor);
    }


    private void printChildren(final UCTSNode node, final double exploreFactor)
    {
        int counter = 0;
        for (final UCTSNode child : node.children)
        {
            final ArrayList<Integer> depthIndices = new ArrayList<>();
            depthIndices.add(counter++);
            System.out.println(getNodeString(child, depthIndices, exploreFactor));
        }
    }

    private void printTree(final UCTSNode node, final ArrayList<Integer> depthIndices, final double exploreFactor)
    {
        String nodeString = getNodeString(node, depthIndices, exploreFactor);

        System.out.println(nodeString);


        final ArrayList<UCTSNode> children = node.children;
        for (int i = 0; i < children.size(); ++i)
        {
            final ArrayList<Integer> indices = new ArrayList<>(depthIndices.size() + 1);
            indices.addAll(depthIndices);
            indices.add(i);

            printTree(children.get(i), indices, exploreFactor);
        }
    }

    private void printTreeBFS(final UCTSNode root, final double exploreFactor)
    {
        printTreeBFS(root, -1, exploreFactor);
    }

    private void printTreeBFS(final UCTSNode root, final long numNodes, final double exploreFactor)
    {
        final ArrayDeque<NodeDepthIndicesPair> nodeQueue = new ArrayDeque<>(1000);

        nodeQueue.add(new NodeDepthIndicesPair(root, new ArrayList<>()));

        long counter = 0;
        final long maxCount = numNodes == -1 ? Long.MAX_VALUE : numNodes;

        while (! nodeQueue.isEmpty() && counter < maxCount)
        {
            final NodeDepthIndicesPair current = nodeQueue.pop();
            final UCTSNode node = current.iNode;

            final String nodeString = getNodeString(node, current.iDepthIndices, exploreFactor);
            System.out.println(nodeString);

            final ArrayList<UCTSNode> children = node.children;
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


    private class NodeDepthIndicesPair
    {
        UCTSNode iNode;
        ArrayList<Integer> iDepthIndices;

        public NodeDepthIndicesPair(final UCTSNode node, final ArrayList<Integer> depthIndices)
        {
            iNode = node;
            iDepthIndices = depthIndices;
        }
    }


    public static String getNodeString(final UCTSNode node, final ArrayList<Integer> depthIndices, final double exploreFactor)
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

        final int wins = node.wins;
        final int visits = node.visits;
        nodeString = nodeString.concat(" ").concat(Integer.toString(wins)).concat("/").concat(Integer.toString(visits));
        nodeString = visits > 0
                ? nodeString.concat(", Avg: ").concat(String.format("%.5f", wins/(double)visits))
                : nodeString.concat(", Avg: 0");

        if (node.parent != null)
        {
            final double upperConfidenceBound = getUCB(node, exploreFactor);
            nodeString = nodeString.concat(", UCB: ").concat(String.format("%.5f", upperConfidenceBound));
        }

        final String playerName = node.playerName;
        final String parentName = node.parent == null ? "-" : node.parent.playerName;
        nodeString = nodeString.concat(" [parent: ").concat(parentName).concat(", player: ").concat(playerName).concat("]");
        return nodeString;
    }

}
