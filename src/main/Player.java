import java.util.concurrent.ThreadLocalRandom;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-08<br>
 * Time: 22:57<br><br>
 */
@SuppressWarnings("WeakerAccess")
public class Player
{
    private final String iUUID;
    private final String iName;

    enum Strategy
    {
        SELECT_FIRST,
        SELECT_RANDOM,
        SELECT_MOST_CROWNS
    }

    private final Strategy iStrategy;

    public Player(String uuid, final String name)
    {
        iUUID = uuid;
        iName = name;
        iStrategy = Strategy.SELECT_RANDOM;
    }

    public Player(String uuid, final String name, final String strategy)
    {
        iUUID = uuid;
        iName = name;
        iStrategy = Strategy.valueOf(strategy);
    }

    public String getName()
    {
        return iName;
    }

    public String getUUID()
    {
        return iUUID;
    }

    @Override
    public String toString()
    {
        return "Player{" +
                "iName='" + iName + '\'' +
                ", iUUID='" + iUUID + '\'' +
                '}';
    }

    public boolean makeAMove(final Game game)
    {
        if (! game.getCurrentPlayer().equals(getName()))
        {
            DEBUG.printWaiting(this);
            return false;
        }

        DEBUG.printMakingAMove(this);

        final Move[] availableMoves = game.getAvailableMoves();

        assert availableMoves.length > 0 : "no moves to choose from";

        final Move move = pickAMove(availableMoves);

        game.makeMove(this, move);

        DEBUG.printMoveMade();

        return true;
    }


    private Move pickAMove(final Move[] availableMoves)
    {
        Move move = availableMoves[0];

        switch (iStrategy)
        {
            case SELECT_FIRST:
                move = availableMoves[0];
                break;

            case SELECT_RANDOM:
                final int numMoves = availableMoves.length;
                int randomNum = ThreadLocalRandom.current().nextInt(0, numMoves);
                move = availableMoves[randomNum];
                break;

            case SELECT_MOST_CROWNS:
                int maxCrowns = 0;
                for (final Move availableMove : availableMoves)
                {
                    final Domino chosenDomino = availableMove.getChosenDomino();

                    if (chosenDomino != null)
                    {
                        final int tile1Crowns = chosenDomino.getTile1().getCrowns();
                        final int tile2Crowns = chosenDomino.getTile2().getCrowns();
                        if (tile1Crowns > maxCrowns || tile2Crowns > maxCrowns)
                        {
                            move = availableMove;
                            maxCrowns = Math.max(tile1Crowns, tile2Crowns);
                        }
                    }
                }
                break;

            default:
                System.err.print("Error: unknown strategy.");
                System.exit(0);
        }

        return move;
    }


    private static class DEBUG
    {
        private static boolean DEBUG = true;

        private static void print(final String msg)
        {
            if (DEBUG)
            {
                System.out.print(msg);
            }
        }

        public static void printWaiting(final Player player)
        {
            print(player.getName() + ": Waiting for my turn...\n");
        }

        public static void printMakingAMove(final Player player)
        {
            print(player.getName() + ": Making a move...");
        }

        public static void printMoveMade()
        {
            print("move made!\n");
        }
    }
}

