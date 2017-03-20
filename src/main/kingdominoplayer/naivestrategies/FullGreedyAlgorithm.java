package kingdominoplayer.naivestrategies;

import kingdominoplayer.naivedatastructures.KingdomMovePair;
import kingdominoplayer.naiveplanning.Planner;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-07<br>
 * Time: 21:46<br><br>
 */
public class FullGreedyAlgorithm extends GreedyAlgorithm
{
    protected ArrayList<KingdomMovePair> getKingdomMovePairsWithChosenDominoPlaced(final ArrayList<KingdomMovePair> kingdomMovePairsForChosenPlacement)
    {
        return Planner.getKingdomMovePairsWithChosenDominoPlaced(kingdomMovePairsForChosenPlacement);
    }
}
