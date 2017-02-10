package datastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-09<br>
 * Time: 10:53<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class Move
{

    private final int iNumber;
    private final Domino iChosenDomino;
    private final PlacedDomino iPlacedDomino;

    public Move(final int number, final Domino chosenDomino, final PlacedDomino placedDomino)
    {
        iNumber = number;
        iChosenDomino = chosenDomino;
        iPlacedDomino = placedDomino;
    }

    public int getNumber()
    {
        return iNumber;
    }

    public Domino getChosenDomino()
    {
        return iChosenDomino;
    }

    public PlacedDomino getPlacedDomino()
    {
        return iPlacedDomino;
    }
}
