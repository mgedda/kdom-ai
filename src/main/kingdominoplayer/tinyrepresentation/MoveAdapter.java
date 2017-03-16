package kingdominoplayer.tinyrepresentation;

import kingdominoplayer.datastructures.Domino;
import kingdominoplayer.datastructures.DominoPosition;
import kingdominoplayer.datastructures.Move;
import kingdominoplayer.datastructures.PlacedDomino;
import kingdominoplayer.datastructures.Position;
import kingdominoplayer.datastructures.Tile;

import java.util.Arrays;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 13:52<br><br>
 */
public class MoveAdapter
{
    public static byte[] toTinyRepresentation(final Move move)
    {
        final byte[] result = new byte[TinyGameState.MOVE_ELEMENT_SIZE];
        Arrays.fill(result, TinyGameState.INVALID_DOMINO_VALUE);

        assert move.getNumber() < 256 : "Move number too large for byte representation!";

        result[TinyGameState.MOVE_NUMBER_INDEX] = (byte) move.getNumber();

        final Domino chosenDomino = move.getChosenDomino();
        final PlacedDomino placedDomino = move.getPlacedDomino();

        if (chosenDomino != null)
        {
            result[TinyGameState.MOVE_CHOSEN_DOMINO_INDEX + TinyGameState.DOMINO_ID_INDEX] = (byte) chosenDomino.getNumber();
            result[TinyGameState.MOVE_CHOSEN_DOMINO_INDEX + TinyGameState.DOMINO_TILE_1_TERRAIN_INDEX] = TerrainCode.from(chosenDomino.getTile1().getTerrain());
            result[TinyGameState.MOVE_CHOSEN_DOMINO_INDEX + TinyGameState.DOMINO_TILE_1_CROWNS_INDEX] = (byte) chosenDomino.getTile1().getCrowns();
            result[TinyGameState.MOVE_CHOSEN_DOMINO_INDEX + TinyGameState.DOMINO_TILE_2_TERRAIN_INDEX] = TerrainCode.from(chosenDomino.getTile2().getTerrain());
            result[TinyGameState.MOVE_CHOSEN_DOMINO_INDEX + TinyGameState.DOMINO_TILE_2_CROWNS_INDEX] = (byte) chosenDomino.getTile2().getCrowns();
        }

        if (placedDomino != null)
        {
            result[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_ID_INDEX] = (byte) placedDomino.getNumber();
            result[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_1_TERRAIN_INDEX] = TerrainCode.from(placedDomino.getTile1().getTerrain());
            result[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_1_CROWNS_INDEX] = (byte) placedDomino.getTile1().getCrowns();
            result[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_2_TERRAIN_INDEX] = TerrainCode.from(placedDomino.getTile2().getTerrain());
            result[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_2_CROWNS_INDEX] = (byte) placedDomino.getTile2().getCrowns();
            result[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_1_X_INDEX] = (byte) placedDomino.getTile1().getPosition().getColumn();
            result[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_1_Y_INDEX] = (byte) placedDomino.getTile1().getPosition().getRow();
            result[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_2_X_INDEX] = (byte) placedDomino.getTile2().getPosition().getColumn();
            result[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_2_Y_INDEX] = (byte) placedDomino.getTile2().getPosition().getRow();
        }

        return result;
    }


    public static Move toMove(final byte[] tinyMove)
    {
        final int moveNumber = tinyMove[TinyGameState.MOVE_NUMBER_INDEX] & 0xFF;  // signed byte to unsigned value

        final Domino chosenDomino;
        if (tinyMove[TinyGameState.MOVE_CHOSEN_DOMINO_INDEX] == TinyGameState.INVALID_DOMINO_VALUE)
        {
            chosenDomino = null;
        }
        else
        {
            final byte dominoNumber = tinyMove[TinyGameState.MOVE_CHOSEN_DOMINO_INDEX + TinyGameState.DOMINO_ID_INDEX];

            final String tile1Terrain = TerrainCode.getName(tinyMove[TinyGameState.MOVE_CHOSEN_DOMINO_INDEX + TinyGameState.DOMINO_TILE_1_TERRAIN_INDEX]);
            final byte tile1Crowns = tinyMove[TinyGameState.MOVE_CHOSEN_DOMINO_INDEX + TinyGameState.DOMINO_TILE_1_CROWNS_INDEX];
            final String tile2Terrain = TerrainCode.getName(tinyMove[TinyGameState.MOVE_CHOSEN_DOMINO_INDEX + TinyGameState.DOMINO_TILE_2_TERRAIN_INDEX]);
            final byte tile2Crowns = tinyMove[TinyGameState.MOVE_CHOSEN_DOMINO_INDEX + TinyGameState.DOMINO_TILE_2_CROWNS_INDEX];

            chosenDomino = new Domino(dominoNumber, new Tile(tile1Terrain, tile1Crowns), new Tile(tile2Terrain, tile2Crowns));
        }

        final PlacedDomino placedDomino;
        if (tinyMove[TinyGameState.MOVE_PLACED_DOMINO_INDEX] == TinyGameState.INVALID_DOMINO_VALUE)
        {
            placedDomino = null;
        }
        else
        {
            final byte dominoNumber = tinyMove[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_ID_INDEX];

            final String tile1Terrain = TerrainCode.getName(tinyMove[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_1_TERRAIN_INDEX]);
            final byte tile1Crowns = tinyMove[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_1_CROWNS_INDEX];
            final String tile2Terrain = TerrainCode.getName(tinyMove[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_2_TERRAIN_INDEX]);
            final byte tile2Crowns = tinyMove[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_2_CROWNS_INDEX];

            final byte tile1X = tinyMove[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_1_X_INDEX];
            final byte tile1Y = tinyMove[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_1_Y_INDEX];
            final byte tile2X = tinyMove[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_2_X_INDEX];
            final byte tile2Y = tinyMove[TinyGameState.MOVE_PLACED_DOMINO_INDEX + TinyGameState.DOMINO_TILE_2_Y_INDEX];

            placedDomino = new PlacedDomino(new Domino(dominoNumber, new Tile(tile1Terrain, tile1Crowns), new Tile(tile2Terrain, tile2Crowns)),
                    new DominoPosition(new Position(tile1Y, tile1X), new Position(tile2Y, tile2X)));
        }

        return new Move(moveNumber, chosenDomino, placedDomino);
    }
}
