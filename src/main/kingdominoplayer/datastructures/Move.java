package kingdominoplayer.datastructures;

import com.sun.istack.internal.Nullable;

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

    public Move(final int number, @Nullable final Domino chosenDomino, @Nullable final PlacedDomino placedDomino)
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


    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final Move move = (Move) o;

        if (iNumber != move.iNumber)
        {
            return false;
        }

        if (iChosenDomino != null ? !iChosenDomino.equals(move.iChosenDomino) : move.iChosenDomino != null)
        {
            return false;
        }
        return iPlacedDomino != null ? iPlacedDomino.equals(move.iPlacedDomino) : move.iPlacedDomino == null;
    }

    @Override
    public int hashCode()
    {
        int result = iNumber;
        result = 31 * result + (iChosenDomino != null ? iChosenDomino.hashCode() : 0);
        result = 31 * result + (iPlacedDomino != null ? iPlacedDomino.hashCode() : 0);
        return result;
    }
}
