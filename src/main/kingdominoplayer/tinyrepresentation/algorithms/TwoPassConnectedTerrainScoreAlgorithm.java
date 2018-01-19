package kingdominoplayer.tinyrepresentation.algorithms;


import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyGameState;
import kingdominoplayer.utils.Util;
import kingdominoplayer.utils.collections.ByteCompactLinkedSet;
import kingdominoplayer.utils.collections.ByteFullArrayLinkedMap;
import kingdominoplayer.utils.collections.PositiveByteArrayLinkedMap;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-22<br>
 * Time: 09:07<br><br>
 */
public class TwoPassConnectedTerrainScoreAlgorithm implements ConnectedTerrainScoreAlgorithm
{
    @Override
    public int applyTo(final byte[] kingdomTerrains, final byte[] kingdomCrowns)
    {
        final ByteSet terrainValues = getValues(kingdomTerrains);

        final ArrayList<TilesCrownsPair> allPairs = new ArrayList<>(24);
        for (final byte terrainValue : terrainValues)
        {
            final Collection<TilesCrownsPair> pairs = getTilesCrownsPairs(terrainValue, kingdomTerrains, kingdomCrowns);
            allPairs.addAll(pairs);
        }

        int score = 0;
        for (final TilesCrownsPair pair : allPairs)
        {
            score += pair.iTiles * pair.iCrowns;
        }

        return score;
    }

    private Collection<TilesCrownsPair> getTilesCrownsPairs(final byte terrainValue, final byte[] kingdomTerrains, final byte[] kingdomCrowns)
    {
        final byte[] connectedComponents = getConnectedComponents(terrainValue, kingdomTerrains);

        byte maxValue = 0;
        for (byte connectedComponent : connectedComponents)
        {
            maxValue = (byte) Math.max(maxValue, connectedComponent);
        }

        final Byte2ObjectMap<TilesCrownsPair> valueToTilesCrownsPair = new PositiveByteArrayLinkedMap<>(maxValue, maxValue);

        for (int i = 0; i < connectedComponents.length; ++i)
        {
            final byte value = connectedComponents[i];
            if (value > 0)
            {
                if (! valueToTilesCrownsPair.containsKey(value))
                {
                    valueToTilesCrownsPair.put(value, new TilesCrownsPair(0, 0));
                }

                final TilesCrownsPair pair = valueToTilesCrownsPair.get(value);
                pair.iTiles++;
                pair.iCrowns += kingdomCrowns[i];
            }
        }

        return valueToTilesCrownsPair.values();
    }

    /*package*/ byte[] getConnectedComponents(final byte terrainValue, final byte[] kingdomTerrains)
    {
        final byte[] equalityTable = new byte[24];
        final byte[] labels = new byte[kingdomTerrains.length];

        // Forward pass
        //
        byte nextLabel = 1;
        for (int i = 0; i < kingdomTerrains.length; ++i)
        {
            if (kingdomTerrains[i] == terrainValue)
            {
                final int n1 = getWestIndex(i);
                final int n2 = getNorthIndex(i);

                final byte l1 = n1 == -1 || kingdomTerrains[n1] != terrainValue ? -1 : labels[n1];
                final byte l2 = n2 == -1 || kingdomTerrains[n2] != terrainValue ? -1 : labels[n2];

                if (l1 != -1 && l2 != -1)
                {
                    if (l1 == l2)
                    {
                        labels[i] = l1;
                    }
                    else
                    {
                        labels[i] = (byte) Math.min(l1, l2);
                        equalityTable[Math.max(l1, l2)] = labels[i];
                    }
                }
                else if (l1 != -1)
                {
                    labels[i] = l1;
                }
                else if (l2 != -1)
                {
                    labels[i] = l2;
                }
                else
                {
                    labels[i] = nextLabel;
                    nextLabel++;
                }
            }
        }

        /*
        System.out.println("Labels=");
        System.out.println(TinyUtils.to2DArrayString(labels, TinyConst.KINGDOM_X_SIZE));
        System.out.println("EqualityTable=");
        for (int i = 0; i < equalityTable.length; ++i)
        {
            if (equalityTable[i] != 0)
            {
                System.out.println(Integer.toString(i) + " => " + Integer.toString(equalityTable[i]));
            }
        }
        */

        Util.noop();

        for (int i = labels.length - 1; i >= 0; --i)
        {
            if (labels[i] > 0)
            {
                labels[i] = getLabelRecursively(equalityTable, labels[i]);
            }
        }

        /*
        System.out.println("KingdomTerrains=");
        System.out.println(TinyUtils.to2DArrayString(kingdomTerrains, TinyConst.KINGDOM_X_SIZE));
        System.out.println("Labels=");
        System.out.println(TinyUtils.to2DArrayString(labels, TinyConst.KINGDOM_X_SIZE));
        */

        return labels;
    }

    private byte getLabelRecursively(final byte[] equalityTable, final byte label)
    {
        if (equalityTable[label] == 0)
        {
            return label;
        }

        return getLabelRecursively(equalityTable, equalityTable[label]);
    }


    private ByteSet getValues(final byte[] array)
    {
        final ByteCompactLinkedSet values = new ByteCompactLinkedSet();

        for (int i = 0; i < array.length; ++i)
        {
            if (array[i] > 1)
            {
                values.add(array[i]);
            }
        }

        return values;
    }


    private int getWestIndex(final int i)
    {
        if (TinyGameState.indexToArrayXCoordinate(i) > 0)
        {
            return i - 1;
        }

        return -1;
    }


    private int getNorthIndex(final int i)
    {
        if (TinyGameState.indexToArrayYCoordinate(i) > 0)
        {
            return i - TinyConst.KINGDOM_X_SIZE;
        }

        return -1;
    }


    private static class TilesCrownsPair
    {
        int iTiles;
        int iCrowns;

        private TilesCrownsPair(final int tiles, final int crowns)
        {
            iTiles = tiles;
            iCrowns = crowns;
        }
    }
}
