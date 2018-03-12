#=============================================================================
# Experiment 8 - Comparing different strategies
#-----------------------------------------------------------------------------
# Comparison of most strategies by simply writing their score
# diffs (vs FG) to file.
#
# This m-file processes result files from games between most strategies
# and FG for different time limits.
#
# The processed information is printed to .dat files with space-separated
# values suitable for plotting with gnuplot or including in table.
#

function process_experiment_8(target_path, output_dir)

    output_path = [target_path "/" output_dir];
    mkdir(output_path);
    output_file_prefix = "";

    addpath("m_files");   # add project m files to path
    util_functions;       # load functions needed by this script
    io_functions;
    plot_functions;

    opponent_strat_str = "FG";
    num_games = 200;
    time_limits = [0.1 0.2 0.3 0.5 1 2 4 6 8 10];

    #
    # Read experiment result files
    #

    # MCE-TR/R
    #
    strat1_t0_1 = dlmread('runs/kdom_exp-20180115-173259-rev-8a5e2af-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T0.1_P0.dat', ' ', 19, 0);
    strat1_t0_2 = dlmread('runs/kdom_exp-20180115-173259-rev-8a5e2af-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T0.2_P0.dat', ' ', 19, 0);
    strat1_t0_3 = dlmread('runs/kdom_exp-20180115-173259-rev-8a5e2af-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T0.3_P0.dat', ' ', 19, 0);
    strat1_t0_5 = dlmread('runs/kdom_exp-20180115-173259-rev-8a5e2af-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
    strat1_t1   = dlmread('runs/kdom_exp-20180115-173259-rev-8a5e2af-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T1_P0.dat', ' ', 19, 0);
    strat1_t2   = dlmread('runs/kdom_exp-20180115-173259-rev-8a5e2af-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
    strat1_t4   = dlmread('runs/kdom_exp-20180115-173259-rev-8a5e2af-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T4_P0.dat', ' ', 19, 0);
    strat1_t6   = dlmread('runs/kdom_exp-20180115-173259-rev-8a5e2af-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T6_P0.dat', ' ', 19, 0);
    strat1_t8   = dlmread('runs/kdom_exp-20180115-173259-rev-8a5e2af-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T8_P0.dat', ' ', 19, 0);
    strat1_t10  = dlmread('runs/kdom_exp-20180115-173259-rev-8a5e2af-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T10_P0.dat', ' ', 19, 0);

    # MCE-FG/R
    #
    strat2_t0_1 = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T0.1_P0.dat', ' ', 19, 0);
    strat2_t0_2 = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T0.2_P0.dat', ' ', 19, 0);
    strat2_t0_3 = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T0.3_P0.dat', ' ', 19, 0);
    strat2_t0_5 = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
    strat2_t1   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T1_P0.dat', ' ', 19, 0);
    strat2_t2   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
    strat2_t4   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T4_P0.dat', ' ', 19, 0);
    strat2_t6   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T6_P0.dat', ' ', 19, 0);
    strat2_t8   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T8_P0.dat', ' ', 19, 0);
    strat2_t10  = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T10_P0.dat', ' ', 19, 0);

    # MCE-EG/R
    #
    strat3_t0_1 = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T0.1_P0.dat', ' ', 19, 0);
    strat3_t0_2 = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T0.2_P0.dat', ' ', 19, 0);
    strat3_t0_3 = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T0.3_P0.dat', ' ', 19, 0);
    strat3_t0_5 = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
    strat3_t1   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T1_P0.dat', ' ', 19, 0);
    strat3_t2   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
    strat3_t4   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T4_P0.dat', ' ', 19, 0);
    strat3_t6   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T6_P0.dat', ' ', 19, 0);
    strat3_t8   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T8_P0.dat', ' ', 19, 0);
    strat3_t10  = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T10_P0.dat', ' ', 19, 0);

    # MCE-PG/R
    #
    strat4_t0_1 = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T0.1_P0.dat', ' ', 19, 0);
    strat4_t0_2 = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T0.2_P0.dat', ' ', 19, 0);
    strat4_t0_3 = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T0.3_P0.dat', ' ', 19, 0);
    strat4_t0_5 = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
    strat4_t1   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T1_P0.dat', ' ', 19, 0);
    strat4_t2   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
    strat4_t4   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T4_P0.dat', ' ', 19, 0);
    strat4_t6   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T6_P0.dat', ' ', 19, 0);
    strat4_t8   = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T8_P0.dat', ' ', 19, 0);
    strat4_t10  = dlmread('runs/kdom_exp-20180123-201654-rev-be7b6d7-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T10_P0.dat', ' ', 19, 0);

    # UCT-TR
    #
    strat5_t0_1 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat5_t0_2 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat5_t0_3 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T0.3_P0.dat', ' ', 19, 0);
    strat5_t0_5 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat5_t1   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T1_P0.dat', ' ', 19, 0);
    strat5_t2   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T2_P0.dat', ' ', 19, 0);
    strat5_t4   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T4_P0.dat', ' ', 19, 0);
    strat5_t6   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T6_P0.dat', ' ', 19, 0);
    strat5_t8   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T8_P0.dat', ' ', 19, 0);
    strat5_t10  = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T10_P0.dat', ' ', 19, 0);

    # UCT-FG
    #
    strat6_t0_1 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat6_t0_2 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat6_t0_3 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T0.3_P0.dat', ' ', 19, 0);
    strat6_t0_5 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat6_t1   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T1_P0.dat', ' ', 19, 0);
    strat6_t2   = dlmread('runs/kdom_exp-20180221-230455-rev-054f5b5-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat6_t4   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T4_P0.dat', ' ', 19, 0);
    strat6_t6   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T6_P0.dat', ' ', 19, 0);
    strat6_t8   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T8_P0.dat', ' ', 19, 0);
    strat6_t10  = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T10_P0.dat', ' ', 19, 0);

    # UCT-EG
    #
    strat7_t0_1 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_EG_C0_6_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat7_t0_2 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_EG_C0_6_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat7_t0_3 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_EG_C0_6_vs_FULL_GREEDY_G200_T0.3_P0.dat', ' ', 19, 0);
    strat7_t0_5 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_EG_C0_6_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat7_t1   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_EG_C0_6_vs_FULL_GREEDY_G200_T1_P0.dat', ' ', 19, 0);
    strat7_t2   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_EG_C0_6_vs_FULL_GREEDY_G200_T2_P0.dat', ' ', 19, 0);
    strat7_t4   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_EG_C0_6_vs_FULL_GREEDY_G200_T4_P0.dat', ' ', 19, 0);
    strat7_t6   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_EG_C0_6_vs_FULL_GREEDY_G200_T6_P0.dat', ' ', 19, 0);
    strat7_t8   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_EG_C0_6_vs_FULL_GREEDY_G200_T8_P0.dat', ' ', 19, 0);
    strat7_t10  = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_EG_C0_6_vs_FULL_GREEDY_G200_T10_P0.dat', ' ', 19, 0);

    # UCT-PG
    #
    strat8_t0_1 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_PG_C0_6_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat8_t0_2 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_PG_C0_6_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat8_t0_3 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_PG_C0_6_vs_FULL_GREEDY_G200_T0.3_P0.dat', ' ', 19, 0);
    strat8_t0_5 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_PG_C0_6_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat8_t1   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_PG_C0_6_vs_FULL_GREEDY_G200_T1_P0.dat', ' ', 19, 0);
    strat8_t2   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_PG_C0_6_vs_FULL_GREEDY_G200_T2_P0.dat', ' ', 19, 0);
    strat8_t4   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_PG_C0_6_vs_FULL_GREEDY_G200_T4_P0.dat', ' ', 19, 0);
    strat8_t6   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_PG_C0_6_vs_FULL_GREEDY_G200_T6_P0.dat', ' ', 19, 0);
    strat8_t8   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_PG_C0_6_vs_FULL_GREEDY_G200_T8_P0.dat', ' ', 19, 0);
    strat8_t10  = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCT_PG_C0_6_vs_FULL_GREEDY_G200_T10_P0.dat', ' ', 19, 0);

    # UCTW-TR
    #
    strat9_t0_1 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat9_t0_2 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat9_t0_3 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.3_P0.dat', ' ', 19, 0);
    strat9_t0_5 = dlmread('runs/kdom_exp-20180225-001530-rev-f2aa1c3-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat9_t1   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T1_P0.dat', ' ', 19, 0);
    strat9_t2   = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat9_t4   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T4_P0.dat', ' ', 19, 0);
    strat9_t6   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T6_P0.dat', ' ', 19, 0);
    strat9_t8   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T8_P0.dat', ' ', 19, 0);
    strat9_t10  = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T10_P0.dat', ' ', 19, 0);

    # UCTW-FG
    #
    strat10_t0_1 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat10_t0_2 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat10_t0_3 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.3_P0.dat', ' ', 19, 0);
    strat10_t0_5 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat10_t1   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T1_P0.dat', ' ', 19, 0);
    strat10_t2   = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat10_t4   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T4_P0.dat', ' ', 19, 0);
    strat10_t6   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T6_P0.dat', ' ', 19, 0);
    strat10_t8   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T8_P0.dat', ' ', 19, 0);
    strat10_t10  = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T10_P0.dat', ' ', 19, 0);

    # UCTW-EG
    #
    strat11_t0_1 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat11_t0_2 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat11_t0_3 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.3_P0.dat', ' ', 19, 0);
    strat11_t0_5 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat11_t1   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T1_P0.dat', ' ', 19, 0);
    strat11_t2   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T2_P0.dat', ' ', 19, 0);
    strat11_t4   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T4_P0.dat', ' ', 19, 0);
    strat11_t6   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T6_P0.dat', ' ', 19, 0);
    strat11_t8   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T8_P0.dat', ' ', 19, 0);
    strat11_t10  = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T10_P0.dat', ' ', 19, 0);

    # UCTW-PG
    #
    strat12_t0_1 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat12_t0_2 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat12_t0_3 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.3_P0.dat', ' ', 19, 0);
    strat12_t0_5 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat12_t1   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T1_P0.dat', ' ', 19, 0);
    strat12_t2   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T2_P0.dat', ' ', 19, 0);
    strat12_t4   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T4_P0.dat', ' ', 19, 0);
    strat12_t6   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T6_P0.dat', ' ', 19, 0);
    strat12_t8   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T8_P0.dat', ' ', 19, 0);
    strat12_t10  = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T10_P0.dat', ' ', 19, 0);

    # UCTB-TR
    #
    strat13_t0_1 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat13_t0_2 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat13_t0_3 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.3_P0.dat', ' ', 19, 0);
    strat13_t0_5 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat13_t1   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T1_P0.dat', ' ', 19, 0);
    strat13_t2   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T2_P0.dat', ' ', 19, 0);
    strat13_t4   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T4_P0.dat', ' ', 19, 0);
    strat13_t6   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T6_P0.dat', ' ', 19, 0);
    strat13_t8   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T8_P0.dat', ' ', 19, 0);
    strat13_t10  = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T10_P0.dat', ' ', 19, 0);

    # UCTB-FG
    #
    strat14_t0_1 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat14_t0_2 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat14_t0_3 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.3_P0.dat', ' ', 19, 0);
    strat14_t0_5 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat14_t1   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T1_P0.dat', ' ', 19, 0);
    strat14_t2   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T2_P0.dat', ' ', 19, 0);
    strat14_t4   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T4_P0.dat', ' ', 19, 0);
    strat14_t6   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T6_P0.dat', ' ', 19, 0);
    strat14_t8   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T8_P0.dat', ' ', 19, 0);
    strat14_t10  = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T10_P0.dat', ' ', 19, 0);

    # UCTB-EG
    #
    strat15_t0_1 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat15_t0_2 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat15_t0_3 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.3_P0.dat', ' ', 19, 0);
    strat15_t0_5 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat15_t1   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T1_P0.dat', ' ', 19, 0);
    strat15_t2   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T2_P0.dat', ' ', 19, 0);
    strat15_t4   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T4_P0.dat', ' ', 19, 0);
    strat15_t6   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T6_P0.dat', ' ', 19, 0);
    strat15_t8   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T8_P0.dat', ' ', 19, 0);
    strat15_t10  = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_EG_C0_6_W0_1_vs_FULL_GREEDY_G200_T10_P0.dat', ' ', 19, 0);

    # UCTB-PG
    #
    strat16_t0_1 = dlmread('runs/kdom_exp-20180226-090937-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat16_t0_2 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat16_t0_3 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.3_P0.dat', ' ', 19, 0);
    strat16_t0_5 = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat16_t1   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T1_P0.dat', ' ', 19, 0);
    strat16_t2   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T2_P0.dat', ' ', 19, 0);
    strat16_t4   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T4_P0.dat', ' ', 19, 0);
    strat16_t6   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T6_P0.dat', ' ', 19, 0);
    strat16_t8   = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTB_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T8_P0.dat', ' ', 19, 0);
    strat16_t10  = dlmread('runs/kdom_exp-20180226-134616-rev-16285e2-cpu-3.20GHz/kdom_exp_UCTW_PG_C0_6_W0_1_vs_FULL_GREEDY_G200_T10_P0.dat', ' ', 19, 0);


    #
    # Create cell arrays
    #

    strats_1{1}  = getStratCellArrayVersion4(strat1_t0_1, "", opponent_strat_str);
    strats_1{2}  = getStratCellArrayVersion4(strat1_t0_2, "", opponent_strat_str);
    strats_1{3}  = getStratCellArrayVersion4(strat1_t0_3, "", opponent_strat_str);
    strats_1{4}  = getStratCellArrayVersion4(strat1_t0_5, "", opponent_strat_str);
    strats_1{5}  = getStratCellArrayVersion4(strat1_t1,   "", opponent_strat_str);
    strats_1{6}  = getStratCellArrayVersion4(strat1_t2,   "", opponent_strat_str);
    strats_1{7}  = getStratCellArrayVersion4(strat1_t4,   "", opponent_strat_str);
    strats_1{8}  = getStratCellArrayVersion4(strat1_t6,   "", opponent_strat_str);
    strats_1{9}  = getStratCellArrayVersion4(strat1_t8,   "", opponent_strat_str);
    strats_1{10} = getStratCellArrayVersion4(strat1_t10,  "", opponent_strat_str);

    strats_2{1}  = getStratCellArrayVersion4(strat2_t0_1, "", opponent_strat_str);
    strats_2{2}  = getStratCellArrayVersion4(strat2_t0_2, "", opponent_strat_str);
    strats_2{3}  = getStratCellArrayVersion4(strat2_t0_3, "", opponent_strat_str);
    strats_2{4}  = getStratCellArrayVersion4(strat2_t0_5, "", opponent_strat_str);
    strats_2{5}  = getStratCellArrayVersion4(strat2_t1,   "", opponent_strat_str);
    strats_2{6}  = getStratCellArrayVersion4(strat2_t2,   "", opponent_strat_str);
    strats_2{7}  = getStratCellArrayVersion4(strat2_t4,   "", opponent_strat_str);
    strats_2{8}  = getStratCellArrayVersion4(strat2_t6,   "", opponent_strat_str);
    strats_2{9}  = getStratCellArrayVersion4(strat2_t8,   "", opponent_strat_str);
    strats_2{10} = getStratCellArrayVersion4(strat2_t10,  "", opponent_strat_str);

    strats_3{1}  = getStratCellArrayVersion4(strat3_t0_1, "", opponent_strat_str);
    strats_3{2}  = getStratCellArrayVersion4(strat3_t0_2, "", opponent_strat_str);
    strats_3{3}  = getStratCellArrayVersion4(strat3_t0_3, "", opponent_strat_str);
    strats_3{4}  = getStratCellArrayVersion4(strat3_t0_5, "", opponent_strat_str);
    strats_3{5}  = getStratCellArrayVersion4(strat3_t1,   "", opponent_strat_str);
    strats_3{6}  = getStratCellArrayVersion4(strat3_t2,   "", opponent_strat_str);
    strats_3{7}  = getStratCellArrayVersion4(strat3_t4,   "", opponent_strat_str);
    strats_3{8}  = getStratCellArrayVersion4(strat3_t6,   "", opponent_strat_str);
    strats_3{9}  = getStratCellArrayVersion4(strat3_t8,   "", opponent_strat_str);
    strats_3{10} = getStratCellArrayVersion4(strat3_t10,  "", opponent_strat_str);

    strats_4{1}  = getStratCellArrayVersion4(strat4_t0_1, "", opponent_strat_str);
    strats_4{2}  = getStratCellArrayVersion4(strat4_t0_2, "", opponent_strat_str);
    strats_4{3}  = getStratCellArrayVersion4(strat4_t0_3, "", opponent_strat_str);
    strats_4{4}  = getStratCellArrayVersion4(strat4_t0_5, "", opponent_strat_str);
    strats_4{5}  = getStratCellArrayVersion4(strat4_t1,   "", opponent_strat_str);
    strats_4{6}  = getStratCellArrayVersion4(strat4_t2,   "", opponent_strat_str);
    strats_4{7}  = getStratCellArrayVersion4(strat4_t4,   "", opponent_strat_str);
    strats_4{8}  = getStratCellArrayVersion4(strat4_t6,   "", opponent_strat_str);
    strats_4{9}  = getStratCellArrayVersion4(strat4_t8,   "", opponent_strat_str);
    strats_4{10} = getStratCellArrayVersion4(strat4_t10,  "", opponent_strat_str);

    strats_5{1}  = getStratCellArrayVersion4(strat5_t0_1, "", opponent_strat_str);
    strats_5{2}  = getStratCellArrayVersion4(strat5_t0_2, "", opponent_strat_str);
    strats_5{3}  = getStratCellArrayVersion4(strat5_t0_3, "", opponent_strat_str);
    strats_5{4}  = getStratCellArrayVersion4(strat5_t0_5, "", opponent_strat_str);
    strats_5{5}  = getStratCellArrayVersion4(strat5_t1,   "", opponent_strat_str);
    strats_5{6}  = getStratCellArrayVersion4(strat5_t2,   "", opponent_strat_str);
    strats_5{7}  = getStratCellArrayVersion4(strat5_t4,   "", opponent_strat_str);
    strats_5{8}  = getStratCellArrayVersion4(strat5_t6,   "", opponent_strat_str);
    strats_5{9}  = getStratCellArrayVersion4(strat5_t8,   "", opponent_strat_str);
    strats_5{10} = getStratCellArrayVersion4(strat5_t10,  "", opponent_strat_str);

    strats_6{1}  = getStratCellArrayVersion4(strat6_t0_1, "", opponent_strat_str);
    strats_6{2}  = getStratCellArrayVersion4(strat6_t0_2, "", opponent_strat_str);
    strats_6{3}  = getStratCellArrayVersion4(strat6_t0_3, "", opponent_strat_str);
    strats_6{4}  = getStratCellArrayVersion4(strat6_t0_5, "", opponent_strat_str);
    strats_6{5}  = getStratCellArrayVersion4(strat6_t1,   "", opponent_strat_str);
    strats_6{6}  = getStratCellArrayVersion4(strat6_t2,   "", opponent_strat_str);
    strats_6{7}  = getStratCellArrayVersion4(strat6_t4,   "", opponent_strat_str);
    strats_6{8}  = getStratCellArrayVersion4(strat6_t6,   "", opponent_strat_str);
    strats_6{9}  = getStratCellArrayVersion4(strat6_t8,   "", opponent_strat_str);
    strats_6{10} = getStratCellArrayVersion4(strat6_t10,  "", opponent_strat_str);

    strats_7{1}  = getStratCellArrayVersion4(strat7_t0_1, "", opponent_strat_str);
    strats_7{2}  = getStratCellArrayVersion4(strat7_t0_2, "", opponent_strat_str);
    strats_7{3}  = getStratCellArrayVersion4(strat7_t0_3, "", opponent_strat_str);
    strats_7{4}  = getStratCellArrayVersion4(strat7_t0_5, "", opponent_strat_str);
    strats_7{5}  = getStratCellArrayVersion4(strat7_t1,   "", opponent_strat_str);
    strats_7{6}  = getStratCellArrayVersion4(strat7_t2,   "", opponent_strat_str);
    strats_7{7}  = getStratCellArrayVersion4(strat7_t4,   "", opponent_strat_str);
    strats_7{8}  = getStratCellArrayVersion4(strat7_t6,   "", opponent_strat_str);
    strats_7{9}  = getStratCellArrayVersion4(strat7_t8,   "", opponent_strat_str);
    strats_7{10} = getStratCellArrayVersion4(strat7_t10,  "", opponent_strat_str);

    strats_8{1}  = getStratCellArrayVersion4(strat8_t0_1, "", opponent_strat_str);
    strats_8{2}  = getStratCellArrayVersion4(strat8_t0_2, "", opponent_strat_str);
    strats_8{3}  = getStratCellArrayVersion4(strat8_t0_3, "", opponent_strat_str);
    strats_8{4}  = getStratCellArrayVersion4(strat8_t0_5, "", opponent_strat_str);
    strats_8{5}  = getStratCellArrayVersion4(strat8_t1,   "", opponent_strat_str);
    strats_8{6}  = getStratCellArrayVersion4(strat8_t2,   "", opponent_strat_str);
    strats_8{7}  = getStratCellArrayVersion4(strat8_t4,   "", opponent_strat_str);
    strats_8{8}  = getStratCellArrayVersion4(strat8_t6,   "", opponent_strat_str);
    strats_8{9}  = getStratCellArrayVersion4(strat8_t8,   "", opponent_strat_str);
    strats_8{10} = getStratCellArrayVersion4(strat8_t10,  "", opponent_strat_str);

    strats_9{1}  = getStratCellArrayVersion4(strat9_t0_1, "", opponent_strat_str);
    strats_9{2}  = getStratCellArrayVersion4(strat9_t0_2, "", opponent_strat_str);
    strats_9{3}  = getStratCellArrayVersion4(strat9_t0_3, "", opponent_strat_str);
    strats_9{4}  = getStratCellArrayVersion4(strat9_t0_5, "", opponent_strat_str);
    strats_9{5}  = getStratCellArrayVersion4(strat9_t1,   "", opponent_strat_str);
    strats_9{6}  = getStratCellArrayVersion4(strat9_t2,   "", opponent_strat_str);
    strats_9{7}  = getStratCellArrayVersion4(strat9_t4,   "", opponent_strat_str);
    strats_9{8}  = getStratCellArrayVersion4(strat9_t6,   "", opponent_strat_str);
    strats_9{9}  = getStratCellArrayVersion4(strat9_t8,   "", opponent_strat_str);
    strats_9{10} = getStratCellArrayVersion4(strat9_t10,  "", opponent_strat_str);

    strats_10{1}  = getStratCellArrayVersion4(strat10_t0_1, "", opponent_strat_str);
    strats_10{2}  = getStratCellArrayVersion4(strat10_t0_2, "", opponent_strat_str);
    strats_10{3}  = getStratCellArrayVersion4(strat10_t0_3, "", opponent_strat_str);
    strats_10{4}  = getStratCellArrayVersion4(strat10_t0_5, "", opponent_strat_str);
    strats_10{5}  = getStratCellArrayVersion4(strat10_t1,   "", opponent_strat_str);
    strats_10{6}  = getStratCellArrayVersion4(strat10_t2,   "", opponent_strat_str);
    strats_10{7}  = getStratCellArrayVersion4(strat10_t4,   "", opponent_strat_str);
    strats_10{8}  = getStratCellArrayVersion4(strat10_t6,   "", opponent_strat_str);
    strats_10{9}  = getStratCellArrayVersion4(strat10_t8,   "", opponent_strat_str);
    strats_10{10} = getStratCellArrayVersion4(strat10_t10,  "", opponent_strat_str);

    strats_11{1}  = getStratCellArrayVersion4(strat11_t0_1, "", opponent_strat_str);
    strats_11{2}  = getStratCellArrayVersion4(strat11_t0_2, "", opponent_strat_str);
    strats_11{3}  = getStratCellArrayVersion4(strat11_t0_3, "", opponent_strat_str);
    strats_11{4}  = getStratCellArrayVersion4(strat11_t0_5, "", opponent_strat_str);
    strats_11{5}  = getStratCellArrayVersion4(strat11_t1,   "", opponent_strat_str);
    strats_11{6}  = getStratCellArrayVersion4(strat11_t2,   "", opponent_strat_str);
    strats_11{7}  = getStratCellArrayVersion4(strat11_t4,   "", opponent_strat_str);
    strats_11{8}  = getStratCellArrayVersion4(strat11_t6,   "", opponent_strat_str);
    strats_11{9}  = getStratCellArrayVersion4(strat11_t8,   "", opponent_strat_str);
    strats_11{10} = getStratCellArrayVersion4(strat11_t10,  "", opponent_strat_str);

    strats_12{1}  = getStratCellArrayVersion4(strat12_t0_1, "", opponent_strat_str);
    strats_12{2}  = getStratCellArrayVersion4(strat12_t0_2, "", opponent_strat_str);
    strats_12{3}  = getStratCellArrayVersion4(strat12_t0_3, "", opponent_strat_str);
    strats_12{4}  = getStratCellArrayVersion4(strat12_t0_5, "", opponent_strat_str);
    strats_12{5}  = getStratCellArrayVersion4(strat12_t1,   "", opponent_strat_str);
    strats_12{6}  = getStratCellArrayVersion4(strat12_t2,   "", opponent_strat_str);
    strats_12{7}  = getStratCellArrayVersion4(strat12_t4,   "", opponent_strat_str);
    strats_12{8}  = getStratCellArrayVersion4(strat12_t6,   "", opponent_strat_str);
    strats_12{9}  = getStratCellArrayVersion4(strat12_t8,   "", opponent_strat_str);
    strats_12{10} = getStratCellArrayVersion4(strat12_t10,  "", opponent_strat_str);

    strats_13{1}  = getStratCellArrayVersion4(strat13_t0_1, "", opponent_strat_str);
    strats_13{2}  = getStratCellArrayVersion4(strat13_t0_2, "", opponent_strat_str);
    strats_13{3}  = getStratCellArrayVersion4(strat13_t0_3, "", opponent_strat_str);
    strats_13{4}  = getStratCellArrayVersion4(strat13_t0_5, "", opponent_strat_str);
    strats_13{5}  = getStratCellArrayVersion4(strat13_t1,   "", opponent_strat_str);
    strats_13{6}  = getStratCellArrayVersion4(strat13_t2,   "", opponent_strat_str);
    strats_13{7}  = getStratCellArrayVersion4(strat13_t4,   "", opponent_strat_str);
    strats_13{8}  = getStratCellArrayVersion4(strat13_t6,   "", opponent_strat_str);
    strats_13{9}  = getStratCellArrayVersion4(strat13_t8,   "", opponent_strat_str);
    strats_13{10} = getStratCellArrayVersion4(strat13_t10,  "", opponent_strat_str);

    strats_14{1}  = getStratCellArrayVersion4(strat14_t0_1, "", opponent_strat_str);
    strats_14{2}  = getStratCellArrayVersion4(strat14_t0_2, "", opponent_strat_str);
    strats_14{3}  = getStratCellArrayVersion4(strat14_t0_3, "", opponent_strat_str);
    strats_14{4}  = getStratCellArrayVersion4(strat14_t0_5, "", opponent_strat_str);
    strats_14{5}  = getStratCellArrayVersion4(strat14_t1,   "", opponent_strat_str);
    strats_14{6}  = getStratCellArrayVersion4(strat14_t2,   "", opponent_strat_str);
    strats_14{7}  = getStratCellArrayVersion4(strat14_t4,   "", opponent_strat_str);
    strats_14{8}  = getStratCellArrayVersion4(strat14_t6,   "", opponent_strat_str);
    strats_14{9}  = getStratCellArrayVersion4(strat14_t8,   "", opponent_strat_str);
    strats_14{10} = getStratCellArrayVersion4(strat14_t10,  "", opponent_strat_str);

    strats_15{1}  = getStratCellArrayVersion4(strat15_t0_1, "", opponent_strat_str);
    strats_15{2}  = getStratCellArrayVersion4(strat15_t0_2, "", opponent_strat_str);
    strats_15{3}  = getStratCellArrayVersion4(strat15_t0_3, "", opponent_strat_str);
    strats_15{4}  = getStratCellArrayVersion4(strat15_t0_5, "", opponent_strat_str);
    strats_15{5}  = getStratCellArrayVersion4(strat15_t1,   "", opponent_strat_str);
    strats_15{6}  = getStratCellArrayVersion4(strat15_t2,   "", opponent_strat_str);
    strats_15{7}  = getStratCellArrayVersion4(strat15_t4,   "", opponent_strat_str);
    strats_15{8}  = getStratCellArrayVersion4(strat15_t6,   "", opponent_strat_str);
    strats_15{9}  = getStratCellArrayVersion4(strat15_t8,   "", opponent_strat_str);
    strats_15{10} = getStratCellArrayVersion4(strat15_t10,  "", opponent_strat_str);

    strats_16{1}  = getStratCellArrayVersion4(strat16_t0_1, "", opponent_strat_str);
    strats_16{2}  = getStratCellArrayVersion4(strat16_t0_2, "", opponent_strat_str);
    strats_16{3}  = getStratCellArrayVersion4(strat16_t0_3, "", opponent_strat_str);
    strats_16{4}  = getStratCellArrayVersion4(strat16_t0_5, "", opponent_strat_str);
    strats_16{5}  = getStratCellArrayVersion4(strat16_t1,   "", opponent_strat_str);
    strats_16{6}  = getStratCellArrayVersion4(strat16_t2,   "", opponent_strat_str);
    strats_16{7}  = getStratCellArrayVersion4(strat16_t4,   "", opponent_strat_str);
    strats_16{8}  = getStratCellArrayVersion4(strat16_t6,   "", opponent_strat_str);
    strats_16{9}  = getStratCellArrayVersion4(strat16_t8,   "", opponent_strat_str);
    strats_16{10} = getStratCellArrayVersion4(strat16_t10,  "", opponent_strat_str);


    #
    # Process data
    #

    score_diffs{1} = getScoreDiffsCellArray(strats_1, "MCE-TR/R");
    score_diffs{2} = getScoreDiffsCellArray(strats_2, "MCE-FG/R");
    score_diffs{3} = getScoreDiffsCellArray(strats_3, "MCE-EG/R");
    score_diffs{4} = getScoreDiffsCellArray(strats_4, "MCE-PG/R");

    score_diffs{5} = getScoreDiffsCellArray(strats_5, "UCT-TR");
    score_diffs{6} = getScoreDiffsCellArray(strats_6, "UCT-FG");
    score_diffs{7} = getScoreDiffsCellArray(strats_7, "UCT-EG");
    score_diffs{8} = getScoreDiffsCellArray(strats_8, "UCT-PG");

    score_diffs{9}  = getScoreDiffsCellArray(strats_9,  "UCTW-TR");
    score_diffs{10} = getScoreDiffsCellArray(strats_10, "UCTW-FG");
    score_diffs{11} = getScoreDiffsCellArray(strats_11, "UCTW-EG");
    score_diffs{12} = getScoreDiffsCellArray(strats_12, "UCTW-PG");

    score_diffs{13} = getScoreDiffsCellArray(strats_13, "UCTB-TR");
    score_diffs{14} = getScoreDiffsCellArray(strats_14, "UCTB-FG");
    score_diffs{15} = getScoreDiffsCellArray(strats_15, "UCTB-EG");
    score_diffs{16} = getScoreDiffsCellArray(strats_16, "UCTB-PG");

    writeScoreDiffsToDatFile(score_diffs, output_path, time_limits);


endfunction
