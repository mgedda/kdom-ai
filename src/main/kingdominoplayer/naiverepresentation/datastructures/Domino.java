package kingdominoplayer.naiverepresentation.datastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-09<br>
 * Time: 10:50<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class Domino
{

    private final int iNumber;
    private final Tile iTile1;
    private final Tile iTile2;

    public Domino(int number, final Tile tile1, final Tile tile2)
    {
        iNumber = number;
        iTile1 = tile1;
        iTile2 = tile2;
    }

    public int getNumber()
    {
        return iNumber;
    }

    public Tile getTile1()
    {
        return iTile1;
    }

    public Tile getTile2()
    {
        return iTile2;
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

        final Domino domino = (Domino) o;

        return iNumber == domino.iNumber && iTile1.equals(domino.iTile1) && iTile2.equals(domino.iTile2);
    }

    @Override
    public int hashCode()
    {
        return iNumber;
    }
}
