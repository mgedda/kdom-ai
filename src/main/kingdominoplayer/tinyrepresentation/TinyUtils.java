package kingdominoplayer.tinyrepresentation;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 12:41<br><br>
 */
public class TinyUtils
{
    public static String to2DArrayString(final byte[] byteArray, final int xSize)
    {
        String result = "{";

        final int ySize = byteArray.length / xSize;
        for (int y = 0; y < ySize; ++y)
        {
            for (int x = 0; x < xSize; ++x)
            {
                result = result.concat(" ".concat(Byte.toString(byteArray[y * xSize + x])));
                if (x < xSize - 1)
                {
                    result = result.concat(",");
                }
            }

            if (y == ySize - 1)
            {
                result = result.concat("}");
            }
            else
            {
                result = result.concat("\n");
            }
        }

        return result;
    }


    /**
     * Get indices adjacent to i in two-dimensional array.
     *
     * @param i
     * @param xSize size of one row in the two-dimensional array
     * @param ySize size of one column in the two dimensional array
     * @return
     */
    public static ArrayList<Byte> getAdjacentIndices(final byte i, final int xSize, final int ySize)
    {
        final ArrayList<Byte> adjacentIndices = new ArrayList<>(4);


        final int x = TinyGameState.indexToArrayXCoordinate(i);
        final int y = TinyGameState.indexToArrayYCoordinate(i);

        if (x > 0)
        {
            adjacentIndices.add((byte)(i - 1));
        }

        if (x < xSize-1)
        {
            adjacentIndices.add((byte)(i + 1));
        }

        if (y > 0)
        {
            adjacentIndices.add((byte)(i - xSize));
        }

        if (y < ySize-1)
        {
            adjacentIndices.add((byte)(i + xSize));
        }

        return adjacentIndices;
    }


    /**
     * Get indices of all placed tiles (excluding the castle).
     *
     * @param playerKingdomTerrains
     * @return
     */
    // TODO [gedda] IMPORTANT! : Change to return LinkedHashSet!
    public static ArrayList<Byte> getPlacedIndices(final byte[] playerKingdomTerrains)
    {
        final ArrayList<Byte> placedIndices = new ArrayList<>(playerKingdomTerrains.length);

        for (byte i = 0; i < playerKingdomTerrains.length; i++)
        {
            if (playerKingdomTerrains[i] != TerrainCode.from("none"))
            {
                placedIndices.add(i);
            }
        }

        return placedIndices;
    }

}
