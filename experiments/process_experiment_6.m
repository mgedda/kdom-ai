#=============================================================================
# Experiment 6 - Examining the UCB constant in Monte-Carlo Tree Search (UCT)
#-----------------------------------------------------------------------------
# Compares Monte-Carlo Tree Search (UCT) players with different values
# for the UCB constant.
#
#    UCT-TR with C = 0.1
#    UCT-TR with C = 0.2
#    UCT-TR with C = 0.3
#    ...
#
# This m-file processes result files from games between Monte-Carlo
# Tree Search (UCT) players using different values for the UCB constant
# vs FG players for different time limits.
#
# The processed information is printed to .dat files with space-separated
# values suitable for plotting with gnuplot.
#

function process_experiment_6(target_path, output_dir)

    output_path = [target_path "/" output_dir];
    mkdir(output_path);
    output_file_prefix = "";

    addpath("m_files");     # add project m files to path
    util_functions;         # load functions needed by this script
    io_functions;
    plot_functions;

    opponent_strat_str = "FG";
    num_games = 200;
    time_limits = [1.0 2.0 4.0];            # time limits
    c_values = [0.0 0.1 0.2 0.3 0.4 0.5 0.6 1.0 1.5 2.0];   # UCB constant values

    #
    # Read experiment result file
    #

    # UCT-TR (0.1s)
    #
    # NOTE!!! C = 0.0, 1.0, 1.5, and 2.0 are just copies of other runs (because UCT-TR (0.1s) is not used in plot).
    #
    #
    strat1_c0_0 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_1_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat1_c0_1 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_1_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat1_c0_2 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_2_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat1_c0_3 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_3_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat1_c0_4 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_4_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat1_c0_5 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_5_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat1_c0_6 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat1_c1_0 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat1_c1_5 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat1_c2_0 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);

    # UCT-TR (0.2s)
    #
    strat2_c0_0 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_TR_C0_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat2_c0_1 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_1_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat2_c0_2 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_2_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat2_c0_3 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_3_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat2_c0_4 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_4_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat2_c0_5 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_5_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat2_c0_6 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat2_c1_0 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_TR_C1_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat2_c1_5 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_TR_C1_5_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat2_c2_0 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_TR_C2_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);

    # UCT-TR (0.5s)
    #
    strat3_c0_0 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_TR_C0_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_c0_1 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_c0_2 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_2_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_c0_3 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_3_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_c0_4 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_4_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_c0_5 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_5_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_c0_6 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_c1_0 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_TR_C1_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_c1_5 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_TR_C1_5_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_c2_0 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_TR_C2_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);

    # UCT-FG (0.1s)
    #
    # NOTE!!! C = 0.0, 1.0, 1.5, and 2.0 are just copies of other runs (because UCT-FG (0.1s) is not used in plot).
    #
    #
    strat4_c0_0 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_1_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat4_c0_1 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_1_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat4_c0_2 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_2_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat4_c0_3 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_3_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat4_c0_4 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_4_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat4_c0_5 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_5_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat4_c0_6 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat4_c1_0 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat4_c1_5 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);
    strat4_c2_0 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T0.1_P0.dat', ' ', 19, 0);

    # UCT-FG (0.2s)
    #
    strat5_c0_0 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_FG_C0_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat5_c0_1 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_1_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat5_c0_2 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_2_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat5_c0_3 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_3_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat5_c0_4 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_4_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat5_c0_5 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_5_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat5_c0_6 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat5_c1_0 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_FG_C1_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat5_c1_5 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_FG_C1_5_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat5_c2_0 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_FG_C2_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);

    # UCT-FG (0.5s)
    #
    strat6_c0_0 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_FG_C0_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat6_c0_1 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat6_c0_2 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_2_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat6_c0_3 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_3_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat6_c0_4 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_4_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat6_c0_5 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_5_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat6_c0_6 = dlmread('runs/kdom_exp-20180207-092817-rev-cbbd929-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat6_c1_0 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_FG_C1_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat6_c1_5 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_FG_C1_5_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat6_c2_0 = dlmread('runs/kdom_exp-20180209-001517-rev-1c739ce-cpu-3.20GHz/kdom_exp_UCT_FG_C2_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);

    # UCT-FG (2.0s)
    #
    strat7_c0_0 = dlmread('runs/kdom_exp-20180221-230455-rev-054f5b5-cpu-3.20GHz/kdom_exp_UCT_FG_C0_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat7_c0_1 = dlmread('runs/kdom_exp-20180221-230455-rev-054f5b5-cpu-3.20GHz/kdom_exp_UCT_FG_C0_1_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat7_c0_2 = dlmread('runs/kdom_exp-20180221-230455-rev-054f5b5-cpu-3.20GHz/kdom_exp_UCT_FG_C0_2_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat7_c0_3 = dlmread('runs/kdom_exp-20180221-230455-rev-054f5b5-cpu-3.20GHz/kdom_exp_UCT_FG_C0_3_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat7_c0_4 = dlmread('runs/kdom_exp-20180221-230455-rev-054f5b5-cpu-3.20GHz/kdom_exp_UCT_FG_C0_4_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat7_c0_5 = dlmread('runs/kdom_exp-20180221-230455-rev-054f5b5-cpu-3.20GHz/kdom_exp_UCT_FG_C0_5_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat7_c0_6 = dlmread('runs/kdom_exp-20180221-230455-rev-054f5b5-cpu-3.20GHz/kdom_exp_UCT_FG_C0_6_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat7_c1_0 = dlmread('runs/kdom_exp-20180221-230455-rev-054f5b5-cpu-3.20GHz/kdom_exp_UCT_FG_C1_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat7_c1_5 = dlmread('runs/kdom_exp-20180221-230455-rev-054f5b5-cpu-3.20GHz/kdom_exp_UCT_FG_C1_5_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat7_c2_0 = dlmread('runs/kdom_exp-20180221-230455-rev-054f5b5-cpu-3.20GHz/kdom_exp_UCT_FG_C2_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);


    #
    # Set run names
    #

    strat1_c0_0_str = "UCT-TR C=0.0 (0.1s)";
    strat1_c0_1_str = "UCT-TR C=0.1 (0.1s)";
    strat1_c0_2_str = "UCT-TR C=0.2 (0.1s)";
    strat1_c0_3_str = "UCT-TR C=0.3 (0.1s)";
    strat1_c0_4_str = "UCT-TR C=0.4 (0.1s)";
    strat1_c0_5_str = "UCT-TR C=0.5 (0.1s)";
    strat1_c0_6_str = "UCT-TR C=0.6 (0.1s)";
    strat1_c1_0_str = "UCT-TR C=1.0 (0.1s)";
    strat1_c1_5_str = "UCT-TR C=1.5 (0.1s)";
    strat1_c2_0_str = "UCT-TR C=2.0 (0.1s)";

    strat2_c0_0_str = "UCT-TR C=0.0 (0.2s)";
    strat2_c0_1_str = "UCT-TR C=0.1 (0.2s)";
    strat2_c0_2_str = "UCT-TR C=0.2 (0.2s)";
    strat2_c0_3_str = "UCT-TR C=0.3 (0.2s)";
    strat2_c0_4_str = "UCT-TR C=0.4 (0.2s)";
    strat2_c0_5_str = "UCT-TR C=0.5 (0.2s)";
    strat2_c0_6_str = "UCT-TR C=0.6 (0.2s)";
    strat2_c1_0_str = "UCT-TR C=1.0 (0.2s)";
    strat2_c1_5_str = "UCT-TR C=1.5 (0.2s)";
    strat2_c2_0_str = "UCT-TR C=2.0 (0.2s)";

    strat3_c0_0_str = "UCT-TR C=0.0 (0.5s)";
    strat3_c0_1_str = "UCT-TR C=0.1 (0.5s)";
    strat3_c0_2_str = "UCT-TR C=0.2 (0.5s)";
    strat3_c0_3_str = "UCT-TR C=0.3 (0.5s)";
    strat3_c0_4_str = "UCT-TR C=0.4 (0.5s)";
    strat3_c0_5_str = "UCT-TR C=0.5 (0.5s)";
    strat3_c0_6_str = "UCT-TR C=0.6 (0.5s)";
    strat3_c1_0_str = "UCT-TR C=1.0 (0.5s)";
    strat3_c1_5_str = "UCT-TR C=1.5 (0.5s)";
    strat3_c2_0_str = "UCT-TR C=2.0 (0.5s)";

    strat4_c0_0_str = "UCT-FG C=0.0 (0.1s)";
    strat4_c0_1_str = "UCT-FG C=0.1 (0.1s)";
    strat4_c0_2_str = "UCT-FG C=0.2 (0.1s)";
    strat4_c0_3_str = "UCT-FG C=0.3 (0.1s)";
    strat4_c0_4_str = "UCT-FG C=0.4 (0.1s)";
    strat4_c0_5_str = "UCT-FG C=0.5 (0.1s)";
    strat4_c0_6_str = "UCT-FG C=0.6 (0.1s)";
    strat4_c1_0_str = "UCT-FG C=1.0 (0.1s)";
    strat4_c1_5_str = "UCT-FG C=1.5 (0.1s)";
    strat4_c2_0_str = "UCT-FG C=2.0 (0.1s)";

    strat5_c0_0_str = "UCT-FG C=0.0 (0.2s)";
    strat5_c0_1_str = "UCT-FG C=0.1 (0.2s)";
    strat5_c0_2_str = "UCT-FG C=0.2 (0.2s)";
    strat5_c0_3_str = "UCT-FG C=0.3 (0.2s)";
    strat5_c0_4_str = "UCT-FG C=0.4 (0.2s)";
    strat5_c0_5_str = "UCT-FG C=0.5 (0.2s)";
    strat5_c0_6_str = "UCT-FG C=0.6 (0.2s)";
    strat5_c1_0_str = "UCT-FG C=1.0 (0.2s)";
    strat5_c1_5_str = "UCT-FG C=1.5 (0.2s)";
    strat5_c2_0_str = "UCT-FG C=2.0 (0.2s)";

    strat6_c0_0_str = "UCT-FG C=0.0 (0.5s)";
    strat6_c0_1_str = "UCT-FG C=0.1 (0.5s)";
    strat6_c0_2_str = "UCT-FG C=0.2 (0.5s)";
    strat6_c0_3_str = "UCT-FG C=0.3 (0.5s)";
    strat6_c0_4_str = "UCT-FG C=0.4 (0.5s)";
    strat6_c0_5_str = "UCT-FG C=0.5 (0.5s)";
    strat6_c0_6_str = "UCT-FG C=0.6 (0.5s)";
    strat6_c1_0_str = "UCT-FG C=1.0 (0.5s)";
    strat6_c1_5_str = "UCT-FG C=1.5 (0.5s)";
    strat6_c2_0_str = "UCT-FG C=2.0 (0.5s)";

    strat7_c0_0_str = "UCT-FG C=0.0 (2.0s)";
    strat7_c0_1_str = "UCT-FG C=0.1 (2.0s)";
    strat7_c0_2_str = "UCT-FG C=0.2 (2.0s)";
    strat7_c0_3_str = "UCT-FG C=0.3 (2.0s)";
    strat7_c0_4_str = "UCT-FG C=0.4 (2.0s)";
    strat7_c0_5_str = "UCT-FG C=0.5 (2.0s)";
    strat7_c0_6_str = "UCT-FG C=0.6 (2.0s)";
    strat7_c1_0_str = "UCT-FG C=1.0 (2.0s)";
    strat7_c1_5_str = "UCT-FG C=1.5 (2.0s)";
    strat7_c2_0_str = "UCT-FG C=2.0 (2.0s)";


    #
    # Create cell arrays
    #

    strats_1{1} = getStratCellArrayVersion4(strat1_c0_0, strat1_c0_0_str, opponent_strat_str);
    strats_1{2} = getStratCellArrayVersion4(strat1_c0_1, strat1_c0_1_str, opponent_strat_str);
    strats_1{3} = getStratCellArrayVersion4(strat1_c0_2, strat1_c0_2_str, opponent_strat_str);
    strats_1{4} = getStratCellArrayVersion4(strat1_c0_3, strat1_c0_3_str, opponent_strat_str);
    strats_1{5} = getStratCellArrayVersion4(strat1_c0_4, strat1_c0_4_str, opponent_strat_str);
    strats_1{6} = getStratCellArrayVersion4(strat1_c0_5, strat1_c0_5_str, opponent_strat_str);
    strats_1{7} = getStratCellArrayVersion4(strat1_c0_6, strat1_c0_6_str, opponent_strat_str);
    strats_1{8} = getStratCellArrayVersion4(strat1_c1_0, strat1_c1_0_str, opponent_strat_str);
    strats_1{9} = getStratCellArrayVersion4(strat1_c1_5, strat1_c1_5_str, opponent_strat_str);
    strats_1{10} = getStratCellArrayVersion4(strat1_c2_0, strat1_c2_0_str, opponent_strat_str);

    strats_2{1} = getStratCellArrayVersion4(strat2_c0_0, strat2_c0_0_str, opponent_strat_str);
    strats_2{2} = getStratCellArrayVersion4(strat2_c0_1, strat2_c0_1_str, opponent_strat_str);
    strats_2{3} = getStratCellArrayVersion4(strat2_c0_2, strat2_c0_2_str, opponent_strat_str);
    strats_2{4} = getStratCellArrayVersion4(strat2_c0_3, strat2_c0_3_str, opponent_strat_str);
    strats_2{5} = getStratCellArrayVersion4(strat2_c0_4, strat2_c0_4_str, opponent_strat_str);
    strats_2{6} = getStratCellArrayVersion4(strat2_c0_5, strat2_c0_5_str, opponent_strat_str);
    strats_2{7} = getStratCellArrayVersion4(strat2_c0_6, strat2_c0_6_str, opponent_strat_str);
    strats_2{8} = getStratCellArrayVersion4(strat2_c1_0, strat2_c1_0_str, opponent_strat_str);
    strats_2{9} = getStratCellArrayVersion4(strat2_c1_5, strat2_c1_5_str, opponent_strat_str);
    strats_2{10} = getStratCellArrayVersion4(strat2_c2_0, strat2_c2_0_str, opponent_strat_str);

    strats_3{1} = getStratCellArrayVersion4(strat3_c0_0, strat3_c0_0_str, opponent_strat_str);
    strats_3{2} = getStratCellArrayVersion4(strat3_c0_1, strat3_c0_1_str, opponent_strat_str);
    strats_3{3} = getStratCellArrayVersion4(strat3_c0_2, strat3_c0_2_str, opponent_strat_str);
    strats_3{4} = getStratCellArrayVersion4(strat3_c0_3, strat3_c0_3_str, opponent_strat_str);
    strats_3{5} = getStratCellArrayVersion4(strat3_c0_4, strat3_c0_4_str, opponent_strat_str);
    strats_3{6} = getStratCellArrayVersion4(strat3_c0_5, strat3_c0_5_str, opponent_strat_str);
    strats_3{7} = getStratCellArrayVersion4(strat3_c0_6, strat3_c0_6_str, opponent_strat_str);
    strats_3{8} = getStratCellArrayVersion4(strat3_c1_0, strat3_c1_0_str, opponent_strat_str);
    strats_3{9} = getStratCellArrayVersion4(strat3_c1_5, strat3_c1_5_str, opponent_strat_str);
    strats_3{10} = getStratCellArrayVersion4(strat3_c2_0, strat3_c2_0_str, opponent_strat_str);

    strats_4{1} = getStratCellArrayVersion4(strat4_c0_0, strat4_c0_0_str, opponent_strat_str);
    strats_4{2} = getStratCellArrayVersion4(strat4_c0_1, strat4_c0_1_str, opponent_strat_str);
    strats_4{3} = getStratCellArrayVersion4(strat4_c0_2, strat4_c0_2_str, opponent_strat_str);
    strats_4{4} = getStratCellArrayVersion4(strat4_c0_3, strat4_c0_3_str, opponent_strat_str);
    strats_4{5} = getStratCellArrayVersion4(strat4_c0_4, strat4_c0_4_str, opponent_strat_str);
    strats_4{6} = getStratCellArrayVersion4(strat4_c0_5, strat4_c0_5_str, opponent_strat_str);
    strats_4{7} = getStratCellArrayVersion4(strat4_c0_6, strat4_c0_6_str, opponent_strat_str);
    strats_4{8} = getStratCellArrayVersion4(strat4_c1_0, strat4_c1_0_str, opponent_strat_str);
    strats_4{9} = getStratCellArrayVersion4(strat4_c1_5, strat4_c1_5_str, opponent_strat_str);
    strats_4{10} = getStratCellArrayVersion4(strat4_c2_0, strat4_c2_0_str, opponent_strat_str);

    strats_5{1} = getStratCellArrayVersion4(strat5_c0_0, strat5_c0_0_str, opponent_strat_str);
    strats_5{2} = getStratCellArrayVersion4(strat5_c0_1, strat5_c0_1_str, opponent_strat_str);
    strats_5{3} = getStratCellArrayVersion4(strat5_c0_2, strat5_c0_2_str, opponent_strat_str);
    strats_5{4} = getStratCellArrayVersion4(strat5_c0_3, strat5_c0_3_str, opponent_strat_str);
    strats_5{5} = getStratCellArrayVersion4(strat5_c0_4, strat5_c0_4_str, opponent_strat_str);
    strats_5{6} = getStratCellArrayVersion4(strat5_c0_5, strat5_c0_5_str, opponent_strat_str);
    strats_5{7} = getStratCellArrayVersion4(strat5_c0_6, strat5_c0_6_str, opponent_strat_str);
    strats_5{8} = getStratCellArrayVersion4(strat5_c1_0, strat5_c1_0_str, opponent_strat_str);
    strats_5{9} = getStratCellArrayVersion4(strat5_c1_5, strat5_c1_5_str, opponent_strat_str);
    strats_5{10} = getStratCellArrayVersion4(strat5_c2_0, strat5_c2_0_str, opponent_strat_str);

    strats_6{1} = getStratCellArrayVersion4(strat6_c0_0, strat6_c0_0_str, opponent_strat_str);
    strats_6{2} = getStratCellArrayVersion4(strat6_c0_1, strat6_c0_1_str, opponent_strat_str);
    strats_6{3} = getStratCellArrayVersion4(strat6_c0_2, strat6_c0_2_str, opponent_strat_str);
    strats_6{4} = getStratCellArrayVersion4(strat6_c0_3, strat6_c0_3_str, opponent_strat_str);
    strats_6{5} = getStratCellArrayVersion4(strat6_c0_4, strat6_c0_4_str, opponent_strat_str);
    strats_6{6} = getStratCellArrayVersion4(strat6_c0_5, strat6_c0_5_str, opponent_strat_str);
    strats_6{7} = getStratCellArrayVersion4(strat6_c0_6, strat6_c0_6_str, opponent_strat_str);
    strats_6{8} = getStratCellArrayVersion4(strat6_c1_0, strat6_c1_0_str, opponent_strat_str);
    strats_6{9} = getStratCellArrayVersion4(strat6_c1_5, strat6_c1_5_str, opponent_strat_str);
    strats_6{10} = getStratCellArrayVersion4(strat6_c2_0, strat6_c2_0_str, opponent_strat_str);

    strats_7{1} = getStratCellArrayVersion4(strat7_c0_0, strat7_c0_0_str, opponent_strat_str);
    strats_7{2} = getStratCellArrayVersion4(strat7_c0_1, strat7_c0_1_str, opponent_strat_str);
    strats_7{3} = getStratCellArrayVersion4(strat7_c0_2, strat7_c0_2_str, opponent_strat_str);
    strats_7{4} = getStratCellArrayVersion4(strat7_c0_3, strat7_c0_3_str, opponent_strat_str);
    strats_7{5} = getStratCellArrayVersion4(strat7_c0_4, strat7_c0_4_str, opponent_strat_str);
    strats_7{6} = getStratCellArrayVersion4(strat7_c0_5, strat7_c0_5_str, opponent_strat_str);
    strats_7{7} = getStratCellArrayVersion4(strat7_c0_6, strat7_c0_6_str, opponent_strat_str);
    strats_7{8} = getStratCellArrayVersion4(strat7_c1_0, strat7_c1_0_str, opponent_strat_str);
    strats_7{9} = getStratCellArrayVersion4(strat7_c1_5, strat7_c1_5_str, opponent_strat_str);
    strats_7{10} = getStratCellArrayVersion4(strat7_c2_0, strat7_c2_0_str, opponent_strat_str);




    #
    # Process data.
    #

    score_diffs{1} = getScoreDiffsCellArray(strats_1, "UCT-TR_T0_1");
    score_diffs{2} = getScoreDiffsCellArray(strats_2, "UCT-TR_T0_2");
    score_diffs{3} = getScoreDiffsCellArray(strats_3, "UCT-TR_T0_5");
    score_diffs{4} = getScoreDiffsCellArray(strats_4, "UCT-FG_T0_1");
    score_diffs{5} = getScoreDiffsCellArray(strats_5, "UCT-FG_T0_2");
    score_diffs{6} = getScoreDiffsCellArray(strats_6, "UCT-FG_T0_5");
    score_diffs{7} = getScoreDiffsCellArray(strats_7, "UCT-FG_T2_0");

    #plotScoreDiffs(score_diffs, num_games, c_values);
    writeScoreDiffsToDatFile(score_diffs, output_path, c_values);
    #writeScoreDiffsToDatFileInverted(score_diffs, time_limits, c_values, output_path);

endfunction
