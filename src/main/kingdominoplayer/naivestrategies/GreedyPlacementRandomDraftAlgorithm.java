package kingdominoplayer.naivestrategies;

import kingdominoplayer.naiverepresentation.datastructures.KingdomMovePair;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-07<br>
 * Time: 21:46<br><br>
 */
public class GreedyPlacementRandomDraftAlgorithm extends GreedyAlgorithm
{
    protected ArrayList<KingdomMovePair> getKingdomMovePairsWithChosenDominoPlaced(final ArrayList<KingdomMovePair> kingdomMovePairsForChosenPlacement)
    {
        // Do not place any dominoes from the current draft.
        //
        return kingdomMovePairsForChosenPlacement;
    }

}
