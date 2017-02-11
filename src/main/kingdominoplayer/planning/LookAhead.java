package kingdominoplayer.planning;

import kingdominoplayer.datastructures.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 16:31<br><br>
 */
public class LookAhead
{
    public static ArrayList<KingdomMovePair> getPossibleNewKingdoms(final Kingdom kingdom, final Move[] moves)
    {
        final PlacedTile[] placedTiles = kingdom.getPlacedTiles();

        final ArrayList<KingdomMovePair> possibleNewKingdoms = new ArrayList<>();

        for (final Move move : moves)
        {
            final PlacedDomino placedDomino = move.getPlacedDomino();

            if (placedDomino != null) // maybe we could not place the domino
            {
                final PlacedTile placedTile1 = placedDomino.getTile1();
                final PlacedTile placedTile2 = placedDomino.getTile2();

                final ArrayList<PlacedTile> totalPlacedTiles = new ArrayList<>(placedTiles.length + 2);

                Collections.addAll(totalPlacedTiles, placedTiles);
                totalPlacedTiles.add(placedTile1);
                totalPlacedTiles.add(placedTile2);

                final Kingdom newKingdom = new Kingdom(totalPlacedTiles.toArray(new PlacedTile[totalPlacedTiles.size()]));

                possibleNewKingdoms.add(new KingdomMovePair(newKingdom, move));
            }
            else
            {
                possibleNewKingdoms.add(new KingdomMovePair(kingdom, move));
            }
        }

        return possibleNewKingdoms;
    }

}
