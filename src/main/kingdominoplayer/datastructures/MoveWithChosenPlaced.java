package kingdominoplayer.datastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-14<br>
 * Time: 23:04<br><br>
 */
public class MoveWithChosenPlaced extends Move
{
    private final PlacedDomino iChosenDomino;

    public MoveWithChosenPlaced(final int number, final PlacedDomino chosenDomino, final PlacedDomino placedDomino)
    {
        super(number, chosenDomino, placedDomino);

        iChosenDomino = chosenDomino;
    }

    public DominoPosition getDominoPositionForChosen()
    {
        return iChosenDomino.getDominoPosition();
    }
}
