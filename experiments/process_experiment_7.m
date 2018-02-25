#=============================================================================
# Experiment 7 - Examining the factor W in progressive (win) bias
#-----------------------------------------------------------------------------
# Compares Monte-Carlo Tree Search (UCT) players using the progressive
# (win) bias enhancement with different values for W.
#
#    UCT_W-TR with W = 0.1
#    UCT_W-TR with W = 0.2
#    UCT_W-TR with W = 0.3
#    ...
#
# This m-file processes result files from games between Monte-Carlo
# Tree Search (UCT) players with the progressive (win) bias enhancement
# for different values of W vs FG players for different time limits.
#
# The processed information is printed to .dat files with space-separated
# values suitable for plotting with gnuplot.
#

function process_experiment_7(target_path, output_dir)

    output_path = [target_path "/" output_dir];
    mkdir(output_path);
    output_file_prefix = "";

    addpath("m_files");     # add project m files to path
    util_functions;         # load functions needed by this script
    io_functions;
    plot_functions;

    opponent_strat_str = "FG";
    num_games = 200;
    time_limits = [0.5 2.0];            # time limits
    w_values = [0.0 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1.0];

    #
    # Read experiment result file
    #

    # UCT_W-TR (0.5s)
    #
    strat1_w0_0 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat1_w0_1 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat1_w0_2 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_2_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat1_w0_3 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_3_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat1_w0_4 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_4_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat1_w0_5 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_5_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat1_w0_6 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_6_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat1_w0_7 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_7_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat1_w0_8 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_8_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat1_w0_9 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_9_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat1_w1_0 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W1_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);

    # UCT_W-TR (2.0s)
    #
    strat2_w0_0 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat2_w0_1 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_1_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat2_w0_2 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_2_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat2_w0_3 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_3_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat2_w0_4 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_4_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat2_w0_5 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_5_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat2_w0_6 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_6_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat2_w0_7 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_7_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat2_w0_8 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_8_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat2_w0_9 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W0_9_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat2_w1_0 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_6_W1_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);

    # UCT_W-FG (0.5s)
    #
    strat3_w0_0 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_w0_1 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_w0_2 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_2_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_w0_3 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_3_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_w0_4 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_4_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_w0_5 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_5_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_w0_6 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_6_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_w0_7 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_7_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_w0_8 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_8_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_w0_9 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_9_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat3_w1_0 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W1_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);

    # UCT_W-FG (2.0s)
    #
    strat4_w0_0 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat4_w0_1 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_1_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat4_w0_2 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_2_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat4_w0_3 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_3_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat4_w0_4 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_4_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat4_w0_5 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_5_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat4_w0_6 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_5_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat4_w0_7 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_5_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat4_w0_8 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_5_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat4_w0_9 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_5_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat4_w1_0 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_5_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
#    strat4_w0_6 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_6_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
#    strat4_w0_7 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_7_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
#    strat4_w0_8 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_8_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
#    strat4_w0_9 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W0_9_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
#    strat4_w1_0 = dlmread('runs/kdom_exp-20180222-214839-rev-1989901-cpu-3.20GHz/kdom_exp_UCTW_FG_C0_6_W1_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);


    #
    # Set run names
    #

    strat1_w0_0_str = "UCT_W-TR W=0.0 (0.5s)";
    strat1_w0_1_str = "UCT_W-TR W=0.1 (0.5s)";
    strat1_w0_2_str = "UCT_W-TR W=0.2 (0.5s)";
    strat1_w0_3_str = "UCT_W-TR W=0.3 (0.5s)";
    strat1_w0_4_str = "UCT_W-TR W=0.4 (0.5s)";
    strat1_w0_5_str = "UCT_W-TR W=0.5 (0.5s)";
    strat1_w0_6_str = "UCT_W-TR W=0.6 (0.5s)";
    strat1_w0_7_str = "UCT_W-TR W=0.7 (0.5s)";
    strat1_w0_8_str = "UCT_W-TR W=0.8 (0.5s)";
    strat1_w0_9_str = "UCT_W-TR W=0.9 (0.5s)";
    strat1_w1_0_str = "UCT_W-TR W=1.0 (0.5s)";

    strat2_w0_0_str = "UCT_W-TR W=0.0 (2.0s)";
    strat2_w0_1_str = "UCT_W-TR W=0.1 (2.0s)";
    strat2_w0_2_str = "UCT_W-TR W=0.2 (2.0s)";
    strat2_w0_3_str = "UCT_W-TR W=0.3 (2.0s)";
    strat2_w0_4_str = "UCT_W-TR W=0.4 (2.0s)";
    strat2_w0_5_str = "UCT_W-TR W=0.5 (2.0s)";
    strat2_w0_6_str = "UCT_W-TR W=0.6 (2.0s)";
    strat2_w0_7_str = "UCT_W-TR W=0.7 (2.0s)";
    strat2_w0_8_str = "UCT_W-TR W=0.8 (2.0s)";
    strat2_w0_9_str = "UCT_W-TR W=0.9 (2.0s)";
    strat2_w1_0_str = "UCT_W-TR W=1.0 (2.0s)";

    strat3_w0_0_str = "UCT_W-FG W=0.0 (0.5s)";
    strat3_w0_1_str = "UCT_W-FG W=0.1 (0.5s)";
    strat3_w0_2_str = "UCT_W-FG W=0.2 (0.5s)";
    strat3_w0_3_str = "UCT_W-FG W=0.3 (0.5s)";
    strat3_w0_4_str = "UCT_W-FG W=0.4 (0.5s)";
    strat3_w0_5_str = "UCT_W-FG W=0.5 (0.5s)";
    strat3_w0_6_str = "UCT_W-FG W=0.6 (0.5s)";
    strat3_w0_7_str = "UCT_W-FG W=0.7 (0.5s)";
    strat3_w0_8_str = "UCT_W-FG W=0.8 (0.5s)";
    strat3_w0_9_str = "UCT_W-FG W=0.9 (0.5s)";
    strat3_w1_0_str = "UCT_W-FG W=1.0 (0.5s)";

    strat4_w0_0_str = "UCT_W-FG W=0.0 (2.0s)";
    strat4_w0_1_str = "UCT_W-FG W=0.1 (2.0s)";
    strat4_w0_2_str = "UCT_W-FG W=0.2 (2.0s)";
    strat4_w0_3_str = "UCT_W-FG W=0.3 (2.0s)";
    strat4_w0_4_str = "UCT_W-FG W=0.4 (2.0s)";
    strat4_w0_5_str = "UCT_W-FG W=0.5 (2.0s)";
    strat4_w0_6_str = "UCT_W-FG W=0.6 (2.0s)";
    strat4_w0_7_str = "UCT_W-FG W=0.7 (2.0s)";
    strat4_w0_8_str = "UCT_W-FG W=0.8 (2.0s)";
    strat4_w0_9_str = "UCT_W-FG W=0.9 (2.0s)";
    strat4_w1_0_str = "UCT_W-FG W=1.0 (2.0s)";


    #
    # Create cell arrays
    #

    strats_1{1} = getStratCellArrayVersion4(strat1_w0_0, strat1_w0_0_str, opponent_strat_str);
    strats_1{2} = getStratCellArrayVersion4(strat1_w0_1, strat1_w0_1_str, opponent_strat_str);
    strats_1{3} = getStratCellArrayVersion4(strat1_w0_2, strat1_w0_2_str, opponent_strat_str);
    strats_1{4} = getStratCellArrayVersion4(strat1_w0_3, strat1_w0_3_str, opponent_strat_str);
    strats_1{5} = getStratCellArrayVersion4(strat1_w0_4, strat1_w0_4_str, opponent_strat_str);
    strats_1{6} = getStratCellArrayVersion4(strat1_w0_5, strat1_w0_5_str, opponent_strat_str);
    strats_1{7} = getStratCellArrayVersion4(strat1_w0_6, strat1_w0_6_str, opponent_strat_str);
    strats_1{8} = getStratCellArrayVersion4(strat1_w0_7, strat1_w0_7_str, opponent_strat_str);
    strats_1{9} = getStratCellArrayVersion4(strat1_w0_8, strat1_w0_8_str, opponent_strat_str);
    strats_1{10} = getStratCellArrayVersion4(strat1_w0_9, strat1_w0_9_str, opponent_strat_str);
    strats_1{11} = getStratCellArrayVersion4(strat1_w1_0, strat1_w1_0_str, opponent_strat_str);

    strats_2{1} = getStratCellArrayVersion4(strat2_w0_0, strat2_w0_0_str, opponent_strat_str);
    strats_2{2} = getStratCellArrayVersion4(strat2_w0_1, strat2_w0_1_str, opponent_strat_str);
    strats_2{3} = getStratCellArrayVersion4(strat2_w0_2, strat2_w0_2_str, opponent_strat_str);
    strats_2{4} = getStratCellArrayVersion4(strat2_w0_3, strat2_w0_3_str, opponent_strat_str);
    strats_2{5} = getStratCellArrayVersion4(strat2_w0_4, strat2_w0_4_str, opponent_strat_str);
    strats_2{6} = getStratCellArrayVersion4(strat2_w0_5, strat2_w0_5_str, opponent_strat_str);
    strats_2{7} = getStratCellArrayVersion4(strat2_w0_6, strat2_w0_6_str, opponent_strat_str);
    strats_2{8} = getStratCellArrayVersion4(strat2_w0_7, strat2_w0_7_str, opponent_strat_str);
    strats_2{9} = getStratCellArrayVersion4(strat2_w0_8, strat2_w0_8_str, opponent_strat_str);
    strats_2{10} = getStratCellArrayVersion4(strat2_w0_9, strat2_w0_9_str, opponent_strat_str);
    strats_2{11} = getStratCellArrayVersion4(strat2_w1_0, strat2_w1_0_str, opponent_strat_str);

    strats_3{1} = getStratCellArrayVersion4(strat3_w0_0, strat3_w0_0_str, opponent_strat_str);
    strats_3{2} = getStratCellArrayVersion4(strat3_w0_1, strat3_w0_1_str, opponent_strat_str);
    strats_3{3} = getStratCellArrayVersion4(strat3_w0_2, strat3_w0_2_str, opponent_strat_str);
    strats_3{4} = getStratCellArrayVersion4(strat3_w0_3, strat3_w0_3_str, opponent_strat_str);
    strats_3{5} = getStratCellArrayVersion4(strat3_w0_4, strat3_w0_4_str, opponent_strat_str);
    strats_3{6} = getStratCellArrayVersion4(strat3_w0_5, strat3_w0_5_str, opponent_strat_str);
    strats_3{7} = getStratCellArrayVersion4(strat3_w0_6, strat3_w0_6_str, opponent_strat_str);
    strats_3{8} = getStratCellArrayVersion4(strat3_w0_7, strat3_w0_7_str, opponent_strat_str);
    strats_3{9} = getStratCellArrayVersion4(strat3_w0_8, strat3_w0_8_str, opponent_strat_str);
    strats_3{10} = getStratCellArrayVersion4(strat3_w0_9, strat3_w0_9_str, opponent_strat_str);
    strats_3{11} = getStratCellArrayVersion4(strat3_w1_0, strat3_w1_0_str, opponent_strat_str);

    strats_4{1} = getStratCellArrayVersion4(strat4_w0_0, strat4_w0_0_str, opponent_strat_str);
    strats_4{2} = getStratCellArrayVersion4(strat4_w0_1, strat4_w0_1_str, opponent_strat_str);
    strats_4{3} = getStratCellArrayVersion4(strat4_w0_2, strat4_w0_2_str, opponent_strat_str);
    strats_4{4} = getStratCellArrayVersion4(strat4_w0_3, strat4_w0_3_str, opponent_strat_str);
    strats_4{5} = getStratCellArrayVersion4(strat4_w0_4, strat4_w0_4_str, opponent_strat_str);
    strats_4{6} = getStratCellArrayVersion4(strat4_w0_5, strat4_w0_5_str, opponent_strat_str);
    strats_4{7} = getStratCellArrayVersion4(strat4_w0_6, strat4_w0_6_str, opponent_strat_str);
    strats_4{8} = getStratCellArrayVersion4(strat4_w0_7, strat4_w0_7_str, opponent_strat_str);
    strats_4{9} = getStratCellArrayVersion4(strat4_w0_8, strat4_w0_8_str, opponent_strat_str);
    strats_4{10} = getStratCellArrayVersion4(strat4_w0_9, strat4_w0_9_str, opponent_strat_str);
    strats_4{11} = getStratCellArrayVersion4(strat4_w1_0, strat4_w1_0_str, opponent_strat_str);


    #
    # Process data.
    #

    score_diffs{1} = getScoreDiffsCellArray(strats_1, "UCT_W-TR_T0_5");
    score_diffs{2} = getScoreDiffsCellArray(strats_2, "UCT_W-TR_T2_0");
    score_diffs{3} = getScoreDiffsCellArray(strats_3, "UCT_W-FG_T0_5");
    score_diffs{4} = getScoreDiffsCellArray(strats_4, "UCT_W-FG_T2_0");

    #plotScoreDiffs(score_diffs, num_games, w_values);
    writeScoreDiffsToDatFile(score_diffs, output_path, w_values);
    #writeScoreDiffsToDatFileInverted(score_diffs, time_limits, w_values, output_path);

endfunction
