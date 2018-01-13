package kingdominoplayer.tinyrepresentation.gamestrategies;


/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-16<br>
 * Time: 14:51<br><br>
 */
public enum TinyStrategyID
{
    TRUE_RANDOM,
    GREEDY_PLACEMENT_RANDOM_DRAFT,
    FULL_GREEDY,

    MCE_TR_WDL,
    MCE_TR_P,
    MCE_TR_R,

    MCE_FG_R,
    MCE_EG_R,
    MCE_PG_R,

    // for examining UCB constant
    UCT_TR_C0_1_W0_0,
    UCT_TR_C0_2_W0_0,
    UCT_TR_C0_3_W0_0,
    UCT_TR_C0_4_W0_0,
    UCT_TR_C0_5_W0_0,
    UCT_TR_C1_0_W0_0,
    UCT_TR_C1_5_W0_0,
    UCT_TR_C2_0_W0_0,

    // for examining bias weight
    UCT_TR_C0_5_W0_1,
    UCT_TR_C0_5_W0_2,
    UCT_TR_C0_5_W0_3,
    UCT_TR_C0_5_W0_4,
    UCT_TR_C0_5_W0_5,
    UCT_TR_C0_5_W1_0,
    UCT_TR_C0_5_W1_5,
    UCT_TR_C0_5_W2_0,

    UCT_FG_C0_2_W0_0
}
