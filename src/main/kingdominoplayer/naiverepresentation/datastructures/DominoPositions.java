package kingdominoplayer.naiverepresentation.datastructures;

import java.util.Collection;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-14<br>
 * Time: 18:10<br><br>
 */
public class DominoPositions
{
    private final Collection<DominoPosition> iDominoPositions;

    public DominoPositions(final Collection<DominoPosition> dominoPositions)
    {
        iDominoPositions = dominoPositions;
    }

    public Collection<DominoPosition> getDominoPositions()
    {
        return iDominoPositions;
    }
}
