package kingdominoplayer.tinyrepresentation;

import java.util.Arrays;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-18<br>
 * Time: 22:33<br><br>
 */
public class TinyMove
{
    final byte[] iMove;

    public TinyMove(final byte[] move)
    {
        iMove = move;
    }

    public byte[] getArray()
    {
        return iMove;
    }

    public boolean hasChosenDomino()
    {
        return iMove[TinyConst.MOVE_CHOSEN_DOMINO_INDEX] != TinyConst.INVALID_DOMINO_VALUE;
    }

    public boolean hasPlacedDomino()
    {
        return iMove[TinyConst.MOVE_PLACED_DOMINO_INDEX] != TinyConst.INVALID_DOMINO_VALUE;
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

        final TinyMove tinyMove = (TinyMove) o;

        return Arrays.equals(iMove, tinyMove.iMove);
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(iMove);
    }

    public byte[] getChosenDomino()
    {
        final byte[] chosenDomino = new byte[TinyConst.DOMINO_SIZE];

        System.arraycopy(iMove, TinyConst.MOVE_CHOSEN_DOMINO_INDEX, chosenDomino, 0, TinyConst.DOMINO_SIZE);

        return chosenDomino;
    }
}
