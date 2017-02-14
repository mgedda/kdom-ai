package kingdominoplayer.datastructures;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-14<br>
 * Time: 20:47<br><br>
 */
public class Positions
{
    private final ArrayList<Position> iPositionArray;

    public Positions(final ArrayList<Position> positionArray)
    {
        iPositionArray = positionArray;

    }

    public ArrayList<Position> getPositionArray()
    {
        return iPositionArray;
    }
}
