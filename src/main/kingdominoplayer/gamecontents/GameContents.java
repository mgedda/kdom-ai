package kingdominoplayer.gamecontents;

import kingdominoplayer.naiverepresentation.datastructures.Domino;
import kingdominoplayer.naiverepresentation.datastructures.Tile;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/*
 * Copyright (c) 2017 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2017-02-17<br>
 * Time: 22:55<br><br>
 */
public final class GameContents
{
    public static Set<Domino> getDominoes()
    {
        final LinkedHashSet<Domino> dominoes = new LinkedHashSet<>(48);

        dominoes.add(new Domino(1, new Tile("FIELD", 0), new Tile("FIELD", 0)));
        dominoes.add(new Domino(2, new Tile("FIELD", 0), new Tile("FIELD", 0)));
        dominoes.add(new Domino(3, new Tile("FOREST", 0), new Tile("FOREST", 0)));
        dominoes.add(new Domino(4, new Tile("FOREST", 0), new Tile("FOREST", 0)));
        dominoes.add(new Domino(5, new Tile("FOREST", 0), new Tile("FOREST", 0)));
        dominoes.add(new Domino(6, new Tile("FOREST", 0), new Tile("FOREST", 0)));
        dominoes.add(new Domino(7, new Tile("WATER", 0), new Tile("WATER", 0)));
        dominoes.add(new Domino(8, new Tile("WATER", 0), new Tile("WATER", 0)));
        dominoes.add(new Domino(9, new Tile("WATER", 0), new Tile("WATER", 0)));

        dominoes.add(new Domino(10, new Tile("PASTURE", 0), new Tile("PASTURE", 0)));
        dominoes.add(new Domino(11, new Tile("PASTURE", 0), new Tile("PASTURE", 0)));
        dominoes.add(new Domino(12, new Tile("CLAY", 0), new Tile("CLAY", 0)));
        dominoes.add(new Domino(13, new Tile("FIELD", 0), new Tile("FOREST", 0)));
        dominoes.add(new Domino(14, new Tile("FIELD", 0), new Tile("WATER", 0)));
        dominoes.add(new Domino(15, new Tile("FIELD", 0), new Tile("PASTURE", 0)));
        dominoes.add(new Domino(16, new Tile("FIELD", 0), new Tile("CLAY", 0)));
        dominoes.add(new Domino(17, new Tile("FOREST", 0), new Tile("WATER", 0)));
        dominoes.add(new Domino(18, new Tile("FOREST", 0), new Tile("PASTURE", 0)));
        dominoes.add(new Domino(19, new Tile("FIELD", 1), new Tile("FOREST", 0)));

        dominoes.add(new Domino(20, new Tile("FIELD", 1), new Tile("WATER", 0)));
        dominoes.add(new Domino(21, new Tile("FIELD", 1), new Tile("PASTURE", 0)));
        dominoes.add(new Domino(22, new Tile("FIELD", 1), new Tile("CLAY", 0)));
        dominoes.add(new Domino(23, new Tile("FIELD", 1), new Tile("MINE", 0)));
        dominoes.add(new Domino(24, new Tile("FOREST", 1), new Tile("FIELD", 0)));
        dominoes.add(new Domino(25, new Tile("FOREST", 1), new Tile("FIELD", 0)));
        dominoes.add(new Domino(26, new Tile("FOREST", 1), new Tile("FIELD", 0)));
        dominoes.add(new Domino(27, new Tile("FOREST", 1), new Tile("FIELD", 0)));
        dominoes.add(new Domino(28, new Tile("FOREST", 1), new Tile("WATER", 0)));
        dominoes.add(new Domino(29, new Tile("FOREST", 1), new Tile("PASTURE", 0)));

        dominoes.add(new Domino(30, new Tile("WATER", 1), new Tile("FIELD", 0)));
        dominoes.add(new Domino(31, new Tile("WATER", 1), new Tile("FIELD", 0)));
        dominoes.add(new Domino(32, new Tile("WATER", 1), new Tile("FOREST", 0)));
        dominoes.add(new Domino(33, new Tile("WATER", 1), new Tile("FOREST", 0)));
        dominoes.add(new Domino(34, new Tile("WATER", 1), new Tile("FOREST", 0)));
        dominoes.add(new Domino(35, new Tile("WATER", 1), new Tile("FOREST", 0)));
        dominoes.add(new Domino(36, new Tile("FIELD", 0), new Tile("PASTURE", 1)));
        dominoes.add(new Domino(37, new Tile("WATER", 0), new Tile("PASTURE", 1)));
        dominoes.add(new Domino(38, new Tile("FIELD", 0), new Tile("CLAY", 1)));
        dominoes.add(new Domino(39, new Tile("PASTURE", 0), new Tile("CLAY", 1)));

        dominoes.add(new Domino(40, new Tile("MINE", 1), new Tile("FIELD", 0)));
        dominoes.add(new Domino(41, new Tile("FIELD", 0), new Tile("PASTURE", 2)));
        dominoes.add(new Domino(42, new Tile("WATER", 0), new Tile("PASTURE", 2)));
        dominoes.add(new Domino(43, new Tile("FIELD", 0), new Tile("CLAY", 2)));
        dominoes.add(new Domino(44, new Tile("PASTURE", 0), new Tile("CLAY", 2)));
        dominoes.add(new Domino(45, new Tile("MINE", 2), new Tile("FIELD", 0)));
        dominoes.add(new Domino(46, new Tile("CLAY", 0), new Tile("MINE", 2)));
        dominoes.add(new Domino(47, new Tile("CLAY", 0), new Tile("MINE", 2)));
        dominoes.add(new Domino(48, new Tile("FIELD", 0), new Tile("MINE", 3)));

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
