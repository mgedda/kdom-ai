package kingdominoplayer.datastructures;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-09<br>
 * Time: 12:03<br><br>
 */
public class PlacedDomino extends Domino
{
    private final DominoPosition iDominoPosition;

    public PlacedDomino(final Domino domino, final DominoPosition dominoPosition)
    {
        super(domino.getNumber(), domino.getTile1(), domino.getTile2());
        iDominoPosition = dominoPosition;
    }

    @Override
    public PlacedTile getTile1()
    {
        return new PlacedTile(super.getTile1(), iDominoPosition.getTile1Position());
    }

    @Override
    public PlacedTile getTile2()
    {
        return new PlacedTile(super.getTile2(), iDominoPosition.getTile2Position());
    }

    public ArrayList<PlacedTile> getPlacedTiles()
    {
        final ArrayList<PlacedTile> placedTiles = new ArrayList<>(2);

        placedTiles.add(getTile1());
        placedTiles.add(getTile2());

        return placedTiles;
    }
}
