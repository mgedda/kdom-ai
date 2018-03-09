package kingdominoplayer.naiverepresentation.strategies;

import kingdominoplayer.naiverepresentation.datastructures.KingdomMovePair;
import kingdominoplayer.naiverepresentation.planning.Planner;

import java.util.ArrayList;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
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
