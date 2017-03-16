package kingdominoplayer.tinyrepresentation;

import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.DominoPosition;
import kingdominoplayer.datastructures.Kingdom;
import kingdominoplayer.datastructures.PlacedTile;
import kingdominoplayer.datastructures.Position;
import kingdominoplayer.datastructures.Tile;
import kingdominoplayer.planning.Planner;

import java.util.ArrayList;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-15<br>
 * Time: 13:16<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class PlannerAdapter
{
    public byte[] getValidPositionsUnique(final byte[] dominoToPlace, final byte[] kingdomTerrains)
    {
        assert kingdomTerrains.length == TinyGameState.SINGLE_PLAYER_KINGDOM_SIZE : "Kingdom has wrong size. Passed entire game state instead of player kingdom?";

        final Domino domino = toDominoObject(dominoToPlace);
        final Kingdom kingdom = toKingdom(kingdomTerrains);

        final Set<DominoPosition> validPositions = Planner.getValidPositions(domino, kingdom);

        final byte[] result = new byte[validPositions.size() * TinyGameState.DOMINOPOSITION_ELEMENT_SIZE];

        int counter = 0;
        for (final DominoPosition dominoPosition : validPositions)
        {
            final int positionIndex = counter * TinyGameState.DOMINOPOSITION_ELEMENT_SIZE;

            result[positionIndex + TinyGameState.DOMINOPOSITION_TILE_1_X_INDEX] = (byte )dominoPosition.getTile1Position().getColumn();
            result[positionIndex + TinyGameState.DOMINOPOSITION_TILE_1_Y_INDEX] = (byte )dominoPosition.getTile1Position().getRow();
            result[positionIndex + TinyGameState.DOMINOPOSITION_TILE_2_X_INDEX] = (byte )dominoPosition.getTile2Position().getColumn();
            result[positionIndex + TinyGameState.DOMINOPOSITION_TILE_2_Y_INDEX] = (byte )dominoPosition.getTile2Position().getRow();

            counter++;
        }

        return result;
    }

    private Kingdom toKingdom(final byte[] kingdomTerrains)
    {
        final ArrayList<PlacedTile> placedTiles = getPlacedTiles(kingdomTerrains);

        return new Kingdom(placedTiles);
    }

    private ArrayList<PlacedTile> getPlacedTiles(final byte[] kingdomTerrains)
    {
        final ArrayList<PlacedTile> placedTiles = new ArrayList<>(kingdomTerrains.length);

        for (int i = 0; i < kingdomTerrains.length; ++i)
        {
            if (kingdomTerrains[i] != TerrainCode.from("none"))
            {
                final String terrain = TerrainCode.getName(kingdomTerrains[i]);

                final int x = TinyGameState.indexToTileXCoordinate(i);
                final int y = TinyGameState.indexToTileYCoordinate(i);

                placedTiles.add(new PlacedTile(new Tile(terrain, 0), new Position(y, x)));
            }
        }

        return placedTiles;
    }


    private Domino toDominoObject(final byte[] domino)
    {
        final byte tile1TerrainIndex = domino[TinyGameState.DOMINO_TILE_1_TERRAIN_INDEX];
        final byte tile2TerrainIndex = domino[TinyGameState.DOMINO_TILE_2_TERRAIN_INDEX];

        final String tile1Terrain = TerrainCode.getName(tile1TerrainIndex);
        final String tile2Terrain = TerrainCode.getName(tile2TerrainIndex);

        return new Domino(0, new Tile(tile1Terrain, 0), new Tile(tile2Terrain, 0));
    }

}
