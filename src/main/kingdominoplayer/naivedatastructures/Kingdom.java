package kingdominoplayer.naivedatastructures;


import kingdominoplayer.planning.Scorer;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:41<br><br>
 */
public class Kingdom
{
    private final Collection<PlacedTile> iPlacedTiles;  // including castle (?)
    private Integer iScore = null;   // cached score

    public Kingdom(final Collection<PlacedTile> placedTiles)
    {
        iPlacedTiles = placedTiles;
    }

    public Collection<PlacedTile> getPlacedTiles()
    {
        return iPlacedTiles;
    }

    public LinkedHashSet<Position> getPlacedTilePositions()
    {
        final LinkedHashSet<Position> positions = new LinkedHashSet<>(iPlacedTiles.size());

        for (final PlacedTile placedTile : iPlacedTiles)
        {
            positions.add(placedTile.getPosition());
        }

        return positions;
    }

    public int getScore()
    {
        if (iScore == null)
        {
            iScore = Scorer.computeScore(getPlacedTiles());
        }
        return iScore;
    }
}
