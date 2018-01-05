package kingdominoplayer.tinyrepresentation.search.montecarlo.treesearch.ucts;

/*
 * Copyright 2018 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2018-01-05<br>
 * Time: 08:51<br><br>
 */

/**
 * Accumulated wins/draws/losses for each player [id1 id2 id3 id4]
 */
/*package*/ class UCTSReward
{
    final double[] values;

    UCTSReward(final double[] values)
    {
        this.values = values;
    }

    UCTSReward add(final UCTSReward other)
    {
        final int numValues = this.values.length;
        assert other.values.length == numValues : "Values mismatch";

        final double[] result = new double[numValues];
        for (int i = 0; i < numValues; ++i)
        {
            result[i] = this.values[i] + other.values[i];
        }

        return new UCTSReward(result);
    }

    double get(final int playerID)
    {
        return values[playerID];
    }
}
