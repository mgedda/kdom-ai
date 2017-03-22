package kingdominoplayer.tinyrepresentation.algorithms;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-22<br>
 * Time: 14:53<br><br>
 */
/*package*/ class TinyDominoPosition
{
    byte iTile1Index;
    byte iTile2Index;

    public TinyDominoPosition(final byte tile1Index, final byte tile2Index)
    {
        iTile1Index = tile1Index;
        iTile2Index = tile2Index;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final TinyDominoPosition that = (TinyDominoPosition) o;

        if (iTile1Index != that.iTile1Index)
        {
            return false;
        }
        return iTile2Index == that.iTile2Index;
    }

    @Override
    public int hashCode()
    {
        int result = (int) iTile1Index;
        result = 31 * result + (int) iTile2Index;
        return result;
    }

    public TinyDominoPosition getInverted()
    {
        return new TinyDominoPosition(iTile2Index, iTile1Index);
    }
}
