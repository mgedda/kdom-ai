package kingdominoplayer.utils;

import kingdominoplayer.naiverepresentation.datastructures.Kingdom;
import kingdominoplayer.naiverepresentation.datastructures.PlacedTile;
import kingdominoplayer.naiverepresentation.datastructures.Position;
import kingdominoplayer.naiverepresentation.datastructures.Positions;
import kingdominoplayer.naiverepresentation.datastructures.Tile;
import kingdominoplayer.plot.DebugPlot;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-14<br>
 * Time: 21:34<br><br>
 */
public class GameUtilsTest
{
    @Test
    public void testIsSingleTileHole_allAdjacentOccupied_true() throws Exception
    {
        final ArrayList<PlacedTile> placedTiles = getPlacedTiles(1, 0, 0, 1, 2, 1, 1, 2);
        final Position position = new Position(1, 1);
        final boolean isSingleTileHole = GameUtils.isSingleTileHole(position, placedTiles);

        plotTilesAndPosition(placedTiles, position, "Is Single Tile Hole = " + Boolean.toString(isSingleTileHole));
        Util.noop();

        Assert.assertEquals(isSingleTileHole, true);
    }

    @Test
    public void testIsSingleTileHole_allAdjacentOccupied_false() throws Exception
    {
        final ArrayList<PlacedTile> placedTiles = getPlacedTiles(1, 0, 0, 1, 2, 1);
        final Position position = new Position(1, 1);
        final boolean isSingleTileHole = GameUtils.isSingleTileHole(position, placedTiles);

        plotTilesAndPosition(placedTiles, position, "Is Single Tile Hole = " + Boolean.toString(isSingleTileHole));
        Util.noop();

        Assert.assertEquals(isSingleTileHole, false);
    }

    @Test
    public void testIsSingleTileHole_nextToCastle_true() throws Exception
    {
        final ArrayList<PlacedTile> placedTiles = getPlacedTiles(1, -1, 2, 0, 1, 1);
        final Position position = new Position(1, 0);
        final boolean isSingleTileHole = GameUtils.isSingleTileHole(position, placedTiles);

        plotTilesAndPosition(placedTiles, position, "Is Single Tile Hole = " + Boolean.toString(isSingleTileHole));
        Util.noop();

        Assert.assertEquals(isSingleTileHole, true);
    }

    @Test
    public void testIsSingleTileHole_nextToCastle_false() throws Exception
    {
        final ArrayList<PlacedTile> placedTiles = getPlacedTiles(1, -1, 1, 1);
        final Position position = new Position(1, 0);
        final boolean isSingleTileHole = GameUtils.isSingleTileHole(position, placedTiles);

        plotTilesAndPosition(placedTiles, position, "Is Single Tile Hole = " + Boolean.toString(isSingleTileHole));
        Util.noop();

        Assert.assertEquals(isSingleTileHole, false);
    }

    @Test
    public void testIsSingleTileHole_nextTo5x5Border_true() throws Exception
    {
        final ArrayList<PlacedTile> placedTiles = getPlacedTiles(2, -1, 1, 0, 2, 1);
        final Position position = new Position(2, 0);
        final boolean isSingleTileHole = GameUtils.isSingleTileHole(position, placedTiles);

        plotTilesAndPosition(placedTiles, position, "Is Single Tile Hole = " + Boolean.toString(isSingleTileHole));
        Util.noop();

        Assert.assertEquals(isSingleTileHole, true);
    }

    @Test
    public void testIsSingleTileHole_nextTo5x5Border_false() throws Exception
    {
        final ArrayList<PlacedTile> placedTiles = getPlacedTiles(2, -1, 2, 1);
        final Position position = new Position(2, 0);
        final boolean isSingleTileHole = GameUtils.isSingleTileHole(position, placedTiles);

        plotTilesAndPosition(placedTiles, position, "Is Single Tile Hole = " + Boolean.toString(isSingleTileHole));
        Util.noop();

        Assert.assertEquals(isSingleTileHole, false);
    }

    @Test
    public void testIsSingleTileHole_nextTo5x5Border_false2() throws Exception
    {
        final ArrayList<PlacedTile> placedTiles = getPlacedTiles(1, 0, 2, 0);
        final Position position = new Position(3, 0);
        final boolean isSingleTileHole = GameUtils.isSingleTileHole(position, placedTiles);

        plotTilesAndPosition(placedTiles, position, "Is Single Tile Hole = " + Boolean.toString(isSingleTileHole));
        Util.noop();

        Assert.assertEquals(isSingleTileHole, false);
    }


    private ArrayList<PlacedTile> getPlacedTiles(final int... rowColPairs)
    {
        final Collection<Position> tilePositions = Positions.from(rowColPairs).getPositionArray();
        final ArrayList<PlacedTile> placedTiles = new ArrayList<>(tilePositions.size());

        for (final Position tilePosition : tilePositions)
        {
            placedTiles.add(new PlacedTile(new Tile("water", 0), tilePosition));
        }
        return placedTiles;
    }


    private void plotTilesAndPosition(final ArrayList<PlacedTile> placedTiles, final Position position, final String title)
    {
        final ArrayList<Position> positions = new ArrayList<>(1);
        positions.add(position);
        DebugPlot.plotWithPositionsMarked(new Kingdom(placedTiles), positions, title);
    }

}
