package kingdominoplayer.tinyrepresentation.adapters;

/*
 * Copyright (c) 2018 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2018-01-08<br>
 * Time: 09:49<br><br>
 */

import kingdominoplayer.naiverepresentation.datastructures.Move;
import kingdominoplayer.tinyrepresentation.datastructures.TinyConst;

import java.util.Arrays;

public class MoveMatcher
{
    public static Move getCorrespondingMove(final Move[] availableMoves, final byte[] selectedMove)
    {
        // Find available move corresponding to selected move (selected move can have different move number)
        Move result = null;
        final byte[] selectedMoveNoMoveNumber = new byte[TinyConst.MOVE_ELEMENT_SIZE - 1];
        System.arraycopy(selectedMove, 1, selectedMoveNoMoveNumber, 0, TinyConst.MOVE_ELEMENT_SIZE - 1);
        for (int i = 0; i < availableMoves.length; ++i)
        {
            final byte[] move = MoveAdapter.toTinyRepresentation(availableMoves[i]);
            final byte[] moveNoMoveNumber = new byte[TinyConst.MOVE_ELEMENT_SIZE - 1];
            System.arraycopy(move, 1, moveNoMoveNumber, 0, TinyConst.MOVE_ELEMENT_SIZE - 1);

            if (Arrays.equals(moveNoMoveNumber, selectedMoveNoMoveNumber))
            {
                result = availableMoves[i];
                break;
            }
        }
        assert result != null : "Corresponding move not found";
        return result;
    }

}
