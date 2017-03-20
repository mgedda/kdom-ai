package kingdominoplayer.gamecontents;

import kingdominoplayer.naivedatastructures.Domino;
import kingdominoplayer.naivedatastructures.Tile;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-02-17<br>
 * Time: 22:55<br><br>
 */
public final class GameContents
{
    public static Set<Domino> getDominoes()
    {
        final LinkedHashSet<Domino> dominoes = new LinkedHashSet<>(48);

        dominoes.add(new Domino(1, new Tile("field", 0), new Tile("field", 0)));
        dominoes.add(new Domino(2, new Tile("field", 0), new Tile("field", 0)));
        dominoes.add(new Domino(3, new Tile("forest", 0), new Tile("forest", 0)));
        dominoes.add(new Domino(4, new Tile("forest", 0), new Tile("forest", 0)));
        dominoes.add(new Domino(5, new Tile("forest", 0), new Tile("forest", 0)));
        dominoes.add(new Domino(6, new Tile("forest", 0), new Tile("forest", 0)));
        dominoes.add(new Domino(7, new Tile("water", 0), new Tile("water", 0)));
        dominoes.add(new Domino(8, new Tile("water", 0), new Tile("water", 0)));
        dominoes.add(new Domino(9, new Tile("water", 0), new Tile("water", 0)));

        dominoes.add(new Domino(10, new Tile("pasture", 0), new Tile("pasture", 0)));
        dominoes.add(new Domino(11, new Tile("pasture", 0), new Tile("pasture", 0)));
        dominoes.add(new Domino(12, new Tile("clay", 0), new Tile("clay", 0)));
        dominoes.add(new Domino(13, new Tile("field", 0), new Tile("forest", 0)));
        dominoes.add(new Domino(14, new Tile("field", 0), new Tile("water", 0)));
        dominoes.add(new Domino(15, new Tile("field", 0), new Tile("pasture", 0)));
        dominoes.add(new Domino(16, new Tile("field", 0), new Tile("clay", 0)));
        dominoes.add(new Domino(17, new Tile("forest", 0), new Tile("water", 0)));
        dominoes.add(new Domino(18, new Tile("forest", 0), new Tile("pasture", 0)));
        dominoes.add(new Domino(19, new Tile("field", 1), new Tile("forest", 0)));

        dominoes.add(new Domino(20, new Tile("field", 1), new Tile("water", 0)));
        dominoes.add(new Domino(21, new Tile("field", 1), new Tile("pasture", 0)));
        dominoes.add(new Domino(22, new Tile("field", 1), new Tile("clay", 0)));
        dominoes.add(new Domino(23, new Tile("field", 1), new Tile("mine", 0)));
        dominoes.add(new Domino(24, new Tile("forest", 1), new Tile("field", 0)));
        dominoes.add(new Domino(25, new Tile("forest", 1), new Tile("field", 0)));
        dominoes.add(new Domino(26, new Tile("forest", 1), new Tile("field", 0)));
        dominoes.add(new Domino(27, new Tile("forest", 1), new Tile("field", 0)));
        dominoes.add(new Domino(28, new Tile("forest", 1), new Tile("water", 0)));
        dominoes.add(new Domino(29, new Tile("forest", 1), new Tile("pasture", 0)));

        dominoes.add(new Domino(30, new Tile("water", 1), new Tile("field", 0)));
        dominoes.add(new Domino(31, new Tile("water", 1), new Tile("field", 0)));
        dominoes.add(new Domino(32, new Tile("water", 1), new Tile("forest", 0)));
        dominoes.add(new Domino(33, new Tile("water", 1), new Tile("forest", 0)));
        dominoes.add(new Domino(34, new Tile("water", 1), new Tile("forest", 0)));
        dominoes.add(new Domino(35, new Tile("water", 1), new Tile("forest", 0)));
        dominoes.add(new Domino(36, new Tile("field", 0), new Tile("pasture", 1)));
        dominoes.add(new Domino(37, new Tile("water", 0), new Tile("pasture", 1)));
        dominoes.add(new Domino(38, new Tile("field", 0), new Tile("clay", 1)));
        dominoes.add(new Domino(39, new Tile("pasture", 0), new Tile("clay", 1)));

        dominoes.add(new Domino(40, new Tile("mine", 1), new Tile("field", 0)));
        dominoes.add(new Domino(41, new Tile("field", 0), new Tile("pasture", 2)));
        dominoes.add(new Domino(42, new Tile("water", 0), new Tile("pasture", 2)));
        dominoes.add(new Domino(43, new Tile("field", 0), new Tile("clay", 2)));
        dominoes.add(new Domino(44, new Tile("pasture", 0), new Tile("clay", 2)));
        dominoes.add(new Domino(45, new Tile("mine", 2), new Tile("field", 0)));
        dominoes.add(new Domino(46, new Tile("clay", 0), new Tile("mine", 2)));
        dominoes.add(new Domino(47, new Tile("clay", 0), new Tile("mine", 2)));
        dominoes.add(new Domino(48, new Tile("field", 0), new Tile("mine", 3)));

        return dominoes;
    }

    public static ArrayList<Tile> getTiles()
    {
        final Set<Domino> dominoes = getDominoes();
        final ArrayList<Tile> tiles = new ArrayList<>(dominoes.size() * 2);

        for (final Domino domino : dominoes)
        {
            tiles.add(domino.getTile1());
            tiles.add(domino.getTile2());
        }

        return tiles;
    }

}
