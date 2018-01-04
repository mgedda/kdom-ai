package kingdominoplayer;

/**
 * Created by gedda on 3/30/17.
 */
public class SearchParameters
{
    private final long iMaxNumPlayouts;
    private final double iMaxSearchTime;  // (s)

    public SearchParameters(long maxNumPlayouts, double maxSearchTime)
    {
        assert maxNumPlayouts > 0 || maxSearchTime > 0 : "Either search time or num playouts must be capped!";

        iMaxNumPlayouts = maxNumPlayouts;
        iMaxSearchTime = maxSearchTime;
    }

    public long getMaxNumPlayouts()
    {
        return iMaxNumPlayouts > 0 ? iMaxNumPlayouts : Long.MAX_VALUE;
    }

    public double getMaxSearchTime()
    {
        return iMaxSearchTime > 0 ? iMaxSearchTime : Double.MAX_VALUE;
    }
}
