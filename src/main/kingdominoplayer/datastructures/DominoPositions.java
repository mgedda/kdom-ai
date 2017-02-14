package kingdominoplayer.datastructures;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-14<br>
 * Time: 18:10<br><br>
 */
public class DominoPositions
{
    private final ArrayList<DominoPosition> iDominoPositions;

    public DominoPositions(final ArrayList<DominoPosition> dominoPositions)
    {
        iDominoPositions = dominoPositions;
    }

    public ArrayList<DominoPosition> getDominoPositions()
    {
        return iDominoPositions;
    }
}
