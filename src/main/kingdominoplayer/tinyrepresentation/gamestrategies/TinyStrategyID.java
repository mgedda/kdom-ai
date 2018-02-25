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

    // Regular Monte-Carlo Tree Search (UTC)
    UCT_TR_C0_0,
    UCT_TR_C0_1,
    UCT_TR_C0_2,
    UCT_TR_C0_3,
    UCT_TR_C0_4,
    UCT_TR_C0_5,
    UCT_TR_C0_6,
    UCT_TR_C1_0,
    UCT_TR_C1_5,
    UCT_TR_C2_0,

    UCT_FG_C0_0,
    UCT_FG_C0_1,
    UCT_FG_C0_2,
    UCT_FG_C0_3,
    UCT_FG_C0_4,
    UCT_FG_C0_5,
    UCT_FG_C0_6,
    UCT_FG_C1_0,
    UCT_FG_C1_5,
    UCT_FG_C2_0,

    UCT_EG_C0_6,

    UCT_PG_C0_6,

    // Monte-Carlo Tree Search with Progressive Win Bias
    UCTW_TR_C0_6_W0_0,
    UCTW_TR_C0_6_W0_1,
    UCTW_TR_C0_6_W0_2,
    UCTW_TR_C0_6_W0_3,
    UCTW_TR_C0_6_W0_4,
    UCTW_TR_C0_6_W0_5,
    UCTW_TR_C0_6_W0_6,
    UCTW_TR_C0_6_W0_7,
    UCTW_TR_C0_6_W0_8,
    UCTW_TR_C0_6_W0_9,
    UCTW_TR_C0_6_W1_0,

    UCTW_FG_C0_6_W0_0,
    UCTW_FG_C0_6_W0_1,
    UCTW_FG_C0_6_W0_2,
    UCTW_FG_C0_6_W0_3,
    UCTW_FG_C0_6_W0_4,
    UCTW_FG_C0_6_W0_5,
    UCTW_FG_C0_6_W0_6,
    UCTW_FG_C0_6_W0_7,
    UCTW_FG_C0_6_W0_8,
    UCTW_FG_C0_6_W0_9,
    UCTW_FG_C0_6_W1_0,

    UCTW_EG_C0_6_W0_1,

    UCTW_PG_C0_6_W0_1,

    // Monte-Carlo Tree Search with Progressive Bias
    UCTB_TR_C0_6_W0_1,

    UCTB_FG_C0_6_W0_1,

    UCTB_EG_C0_6_W0_1,

    UCTB_PG_C0_6_W0_1
}
