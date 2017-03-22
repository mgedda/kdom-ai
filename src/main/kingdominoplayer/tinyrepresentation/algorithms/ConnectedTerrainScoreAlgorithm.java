package kingdominoplayer.tinyrepresentation.algorithms;

import java.util.Set;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-22<br>
 * Time: 08:26<br><br>
 */
/*package*/ interface ConnectedTerrainScoreAlgorithm
{
    int applyTo(final Set<Byte> placedIndices, final byte[] kingdomTerrains, final byte[] kingdomCrowns);
}
