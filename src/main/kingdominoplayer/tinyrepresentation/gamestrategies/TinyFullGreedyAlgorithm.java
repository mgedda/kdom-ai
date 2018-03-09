package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;
import kingdominoplayer.tinyrepresentation.datastructures.TinyKingdomMovePair;
import kingdominoplayer.tinyrepresentation.algorithms.TinyValidPositionsAlgorithm;

import java.util.ArrayList;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-17<br>
 * Time: 16:10<br><br>
 */
public class TinyFullGreedyAlgorithm extends TinyGreedyAlgorithm
{

    @Override
    protected ArrayList<TinyKingdomMovePair> getKingdomMovePairsWithChosenDominoPlaced(final ArrayList<TinyKingdomMovePair> kingdomMovePairs)
    {
        final ArrayList<TinyKingdomMovePair> kingdomMovePairsWithChosenDominoPlaced = new ArrayList<>(kingdomMovePairs.size());

        for (final TinyKingdomMovePair kingdomMovePair : kingdomMovePairs)
        {
            if (kingdomMovePair.getMove().hasChosenDomino()) // chosen domino is not always available
            {
                final byte[] validDominoPositions = new TinyValidPositionsAlgorithm().applyTo(kingdomMovePair.getChosenDomino(), kingdomMovePair.getKingdomTerrains());

                final int numPositions = validDominoPositions.length / TinyConst.DOMINOPOSITION_ELEMENT_SIZE;

                for (int i = 0; i < numPositions; ++i)
                {
                    final int dominoPositionIndex = i * TinyConst.DOMINOPOSITION_ELEMENT_SIZE;
                    final byte tile1X = validDominoPositions[dominoPositionIndex + TinyConst.DOMINOPOSITION_TILE_1_X_INDEX];
                    final byte tile1Y = validDominoPositions[dominoPositionIndex + TinyConst.DOMINOPOSITION_TILE_1_Y_INDEX];
                    final byte tile2X = validDominoPositions[dominoPositionIndex + TinyConst.DOMINOPOSITION_TILE_2_X_INDEX];
                    final byte tile2Y = validDominoPositions[dominoPositionIndex + TinyConst.DOMINOPOSITION_TILE_2_Y_INDEX];

                    final TinyKingdomMovePair kingdomMovePairWithChosenDominoPlaced = kingdomMovePair.withChosenDominoPlaced(tile1X, tile1Y, tile2X, tile2Y);
                    kingdomMovePairsWithChosenDominoPlaced.add(kingdomMovePairWithChosenDominoPlaced);
                }
            }
        }

        return kingdomMovePairsWithChosenDominoPlaced;
    }

}
