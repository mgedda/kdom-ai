package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts;

/*
 * Copyright (c) 2018 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2018-01-05<br>
 * Time: 09:03<br><br>
 */

import java.util.ArrayDeque;
import java.util.ArrayList;

/*package*/ class UCTSTreeUtils
{

    public static void printTree(final UCTSNode root, final UCB ucb)
    {
        System.out.println("Monte-Carlo Search Tree:");
        printTree(root, new ArrayList<>(), ucb);
    }


    public static void printChildren(final UCTSNode node, final UCB ucb)
    {
        int counter = 0;
        for (final UCTSNode child : node.getChildren())
        {
            final ArrayList<Integer> depthIndices = new ArrayList<>();
            depthIndices.add(counter++);
            System.out.println(child.toStringNestedBrackets(depthIndices, ucb));
        }
    }

    private static void printTree(final UCTSNode node, final ArrayList<Integer> depthIndices, final UCB ucb)
    {
        String nodeString = node.toStringNestedBrackets(depthIndices, ucb);

        System.out.println(nodeString);


        final ArrayList<UCTSNode> children = node.getChildren();
        for (int i = 0; i < children.size(); ++i)
        {
            final ArrayList<Integer> indices = new ArrayList<>(depthIndices.size() + 1);
            indices.addAll(depthIndices);
            indices.add(i);

            printTree(children.get(i), indices, ucb);
        }
    }

    public static void printTreeBFS(final UCTSNode root, final UCB ucb)
    {
        printTreeBFS(root, -1, ucb);
    }

    public static void printTreeBFS(final UCTSNode root, final long numNodes, final UCB ucb)
    {
        final ArrayDeque<NodeDepthIndicesPair> nodeQueue = new ArrayDeque<>(1000);

        nodeQueue.add(new NodeDepthIndicesPair(root, new ArrayList<>()));

        long counter = 0;
        final long maxCount = numNodes == -1 ? Long.MAX_VALUE : numNodes;

        while (! nodeQueue.isEmpty() && counter < maxCount)
        {
            final NodeDepthIndicesPair current = nodeQueue.pop();
            final UCTSNode node = current.iNode;

            final String nodeString = node.toStringNestedBrackets(current.iDepthIndices, ucb);
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
