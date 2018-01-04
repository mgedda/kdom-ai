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

import java.util.ArrayList;
import java.util.Collection;

public class ExperimentParameters
{
    private static final String USAGE = "Usage: java -jar kdom-exp <playerStrategy> <opponentStrategy> <max_search_time> <max_playouts> [outputFile]";

    public static ExperimentParameters from(final String[] args)
    {
        if (args.length < 4 || args.length > 5)
        {
            System.err.println(USAGE);
            System.exit(0);
        }

        final double maxSearchTime = Double.valueOf(args[2]);
        final long maxNumPlayouts = Long.valueOf(args[3]);
        final SearchParameters searchParameters = new SearchParameters(maxNumPlayouts, maxSearchTime);

        final TinyStrategy playerStrategy = new TinyStrategyFactory(searchParameters).getGameStrategy(TinyStrategyID.valueOf(args[0]));
        final TinyStrategy opponentStrategy = new TinyStrategyFactory(searchParameters).getGameStrategy(TinyStrategyID.valueOf(args[1]));

        final ArrayList<String> outputFileOption = new ArrayList<>();
        if (args.length == 5)
        {
            final String outputFile = args[4];
            outputFileOption.add(outputFile);
        }

        return new ExperimentParameters(playerStrategy, opponentStrategy, outputFileOption);
    }

    private final TinyStrategy iPlayerStrategy;
    private final TinyStrategy iOpponentStrategy;
    private final Collection<String> iOutputFileOption;

    private ExperimentParameters(final TinyStrategy playerStrategy, final TinyStrategy opponentStrategy, final Collection<String> outputFileOption)
    {
        iPlayerStrategy = playerStrategy;
        iOpponentStrategy = opponentStrategy;
        iOutputFileOption = outputFileOption;
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
        assert hasOutputFile();
        return iOutputFileOption.iterator().next();
    }

    public boolean hasOutputFile()
    {
        return iOutputFileOption.iterator().hasNext();
    }
}

