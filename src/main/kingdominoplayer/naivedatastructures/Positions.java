package kingdominoplayer.naivedatastructures;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-14<br>
 * Time: 20:47<br><br>
 */
public class Positions
{
    private final Collection<Position> iPositionArray;

    public Positions(final Collection<Position> positionArray)
    {
        iPositionArray = positionArray;

    }

    public Collection<Position> getPositionArray()
    {
        return iPositionArray;
    }


    public static Positions from(final int... rowColPairs)
    {
        assert rowColPairs.length % 2 == 0 : "rowColPairs must be multiple of 2";

        final ArrayList<Position> positions = new ArrayList<>(rowColPairs.length / 2);

        for (int i = 0; i < rowColPairs.length; i += 2 )
        {
            positions.add(new Position(rowColPairs[i], rowColPairs[i+1]));
        }

        return new Positions(positions);
    }
}
