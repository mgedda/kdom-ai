package kingdominoplayer;

/*
 * Copyright 2018 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2018-01-04<br>
 * Time: 22:28<br><br>
 */

import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategy;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategyFactory;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategyID;

public class Parameters
{
    private static final String USAGE = "Usage: java -jar kdom-exp <playerStrategy> <opponentStrategy> <outputFile> <max_search_time> <max_playouts>";

    public static Parameters from(final String[] args)
    {
        if (args.length != 5)
        {
            System.err.println(USAGE);
            System.exit(0);
        }

        final double maxSearchTime = Double.valueOf(args[3]);
        final long maxNumPlayouts = Long.valueOf(args[4]);
        final SearchParameters searchParameters = new SearchParameters(maxNumPlayouts, maxSearchTime);

        final TinyStrategy playerStrategy = new TinyStrategyFactory(searchParameters).getGameStrategy(TinyStrategyID.valueOf(args[0]));
        final TinyStrategy opponentStrategy = new TinyStrategyFactory(searchParameters).getGameStrategy(TinyStrategyID.valueOf(args[1]));

        final String outputFile = args[2];

        return new Parameters(playerStrategy, opponentStrategy, outputFile);
    }

    private final TinyStrategy iPlayerStrategy;
    private final TinyStrategy iOpponentStrategy;
    private final String iOutputFile;

    private Parameters(final TinyStrategy playerStrategy, final TinyStrategy opponentStrategy, final String outputFile)
    {
        iPlayerStrategy = playerStrategy;
        iOpponentStrategy = opponentStrategy;
        iOutputFile = outputFile;
    }

    public TinyStrategy getPlayerStrategy()
    {
        return iPlayerStrategy;
    }

    public TinyStrategy getOpponentStrategy()
    {
        return iOpponentStrategy;
    }

    public String getOutputFile()
    {
        return iOutputFile;
    }
}

