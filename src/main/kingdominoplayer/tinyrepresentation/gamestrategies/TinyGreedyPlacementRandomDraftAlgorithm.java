package kingdominoplayer.tinyrepresentation.gamestrategies;

import kingdominoplayer.tinyrepresentation.datastructures.TinyKingdomMovePair;

import java.util.ArrayList;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-03-20<br>
 * Time: 11:22<br><br>
 */
public class TinyGreedyPlacementRandomDraftAlgorithm extends TinyGreedyAlgorithm
{
    @Override
    protected ArrayList<TinyKingdomMovePair> getKingdomMovePairsWithChosenDominoPlaced(final ArrayList<TinyKingdomMovePair> kingdomMovePairs)
    {
        return kingdomMovePairs;
    }
}
