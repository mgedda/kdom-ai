package datastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-09<br>
 * Time: 12:04<br><br>
 */
public class TilePosition
{
    private final int iRow;
    private final int iColumn;

    public TilePosition(int row, int column)
    {
        iRow = row;
        iColumn = column;
    }

    public int getRow()
    {
        return iRow;
    }

    public int getColumn()
    {
        return iColumn;
    }
}
