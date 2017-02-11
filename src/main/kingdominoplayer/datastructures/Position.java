package kingdominoplayer.datastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-09<br>
 * Time: 12:04<br><br>
 */
public class Position
{
    private final int iRow;
    private final int iColumn;

    public Position(int row, int column)
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

    public Position plus(final Position other)
    {
        return new Position(getRow() + other.getRow(), getColumn() + other.getColumn());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final Position position = (Position) o;

        if (iRow != position.iRow)
        {
            return false;
        }
        return iColumn == position.iColumn;
    }

    @Override
    public int hashCode()
    {
        int result = iRow;
        result = 31 * result + iColumn;
        return result;
    }
}
