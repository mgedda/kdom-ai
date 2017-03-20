package kingdominoplayer.naivedatastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-12<br>
 * Time: 10:01<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class DominoPosition
{
    private final Position iTile1Position;
    private final Position iTile2Position;

    public DominoPosition(final Position tile1Position, final Position tile2Position)
    {
        iTile1Position = tile1Position;
        iTile2Position = tile2Position;
    }

    public DominoPosition getInverted()
    {
        return new DominoPosition(iTile2Position, iTile1Position);
    }

    public Position getTile1Position()
    {
        return iTile1Position;
    }

    public Position getTile2Position()
    {
        return iTile2Position;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final DominoPosition that = (DominoPosition) o;

        if (!iTile1Position.equals(that.iTile1Position))
        {
            return false;
        }
        return iTile2Position.equals(that.iTile2Position);
    }

    @Override
    public int hashCode()
    {
        int result = iTile1Position.hashCode();
        result = 31 * result + iTile2Position.hashCode();
        return result;
    }
}
