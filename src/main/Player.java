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

    public Player(String uuid, final String name)
    {
        iUUID = uuid;
        iName = name;
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

        game.makeMove(this, availableMoves[0]);

        DEBUG.printMoveMade();

        return true;
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

