package kingdominoplayer.naivedatastructures;

import java.util.ArrayList;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-11<br>
 * Time: 15:52<br><br>
 */
public class KingdomMovePair
{
    private final Kingdom iKingdom;
    private final Move iMove;
    private final boolean iIsPlacedDominoPlaced;
    private final DominoPosition iChosenDominoPosition;


    public KingdomMovePair(final Kingdom kingdom, final Move move)
    {
        this(kingdom, move, false, null);
    }

    public KingdomMovePair withChosenDominoPlaced(final DominoPosition chosenDominoPosition)
    {
        assert iMove.getChosenDomino() != null : "There is no chosen domino to place";
        assert ! isChosenPlaced() : "chosen domino is already placed";

        final PlacedDomino chosenDomino = new PlacedDomino(iMove.getChosenDomino(), chosenDominoPosition);
        final Kingdom kingdomWithChosenDominoPlaced = getKingdomWithDominoPlaced(chosenDomino);

        return new KingdomMovePair(kingdomWithChosenDominoPlaced, iMove, iIsPlacedDominoPlaced, chosenDominoPosition);
    }

    public KingdomMovePair withPlacedDominoPlaced()
    {
        assert iMove.getPlacedDomino() != null : "There is no placed domino to place";
        assert ! isPlacedDominoPlaced() : "placed domino is already placed";

        final PlacedDomino placedDomino = iMove.getPlacedDomino();
        final Kingdom kingdomWithPlacedDominoPlaced = getKingdomWithDominoPlaced(placedDomino);

        return new KingdomMovePair(kingdomWithPlacedDominoPlaced, iMove, true, iChosenDominoPosition);
    }

    private Kingdom getKingdomWithDominoPlaced(final PlacedDomino placedDomino)
    {
        final ArrayList<PlacedTile> placedTiles = new ArrayList<>(iKingdom.getPlacedTiles().size() + 2);
        placedTiles.addAll(iKingdom.getPlacedTiles());
        placedTiles.addAll(placedDomino.getPlacedTiles());

        return new Kingdom(placedTiles);
    }

    private KingdomMovePair(final Kingdom kingdom, final Move move, final boolean isPlacedDominoPlaced, final DominoPosition chosenDominoPosition)
    {
        iKingdom = kingdom;
        iMove = move;
        iIsPlacedDominoPlaced = isPlacedDominoPlaced;
        iChosenDominoPosition = chosenDominoPosition;
    }


    public Kingdom getKingdom()
    {
        return iKingdom;
    }

    public Move getMove()
    {
        return iMove;
    }

    public boolean isPlacedDominoPlaced()
    {
        return iIsPlacedDominoPlaced;
    }

    public boolean isChosenPlaced()
    {
        return iChosenDominoPosition != null;
    }

    public DominoPosition getChosenDominoPosition()
    {
        assert isChosenPlaced() : "chosen domino is not placed";
        return iChosenDominoPosition;
    }

    public DominoPosition getPlacedDominoPosition()
    {
        assert isPlacedDominoPlaced() : "chosen domino is not placed";
        return iMove.getPlacedDomino().getDominoPosition();
    }
}
