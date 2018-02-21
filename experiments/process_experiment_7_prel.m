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

function process_experiment_7_prel(target_path, output_dir)

    output_path = [target_path "/" output_dir];
    mkdir(output_path);
    output_file_prefix = "";

    addpath("m_files");     # add project m files to path
    util_functions;         # load functions needed by this script
    io_functions;
    plot_functions;

    opponent_strat_str = "FG";
    num_games = 200;
    time_limits = [0.2 0.5 2.0];            # time limits
    w_values = [0.0 0.1 0.5 1.0 5.0 10.0 50.0];

    #
    # Read experiment result file
    #

    # UCT_W-TR (0.2s)
    #
    strat1_w0_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W0_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat1_w0_1 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W0_1_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat1_w0_5 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W0_5_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat1_w1_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W1_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat1_w5_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W5_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat1_w10_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W10_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat1_w50_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W50_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);

    # UCT_W-TR (0.5s)
    #
    strat2_w0_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W0_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat2_w0_1 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat2_w0_5 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W0_5_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat2_w1_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W1_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat2_w5_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W5_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat2_w10_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W10_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat2_w50_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W50_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);

    # UCT_W-TR (2.0s)
    #
    strat3_w0_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W0_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat3_w0_1 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W0_1_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat3_w0_5 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W0_5_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat3_w1_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W1_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat3_w5_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W5_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat3_w10_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W10_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat3_w50_0 = dlmread('runs/kdom_exp-20180217-103756-rev-96c8a72-cpu-3.20GHz/kdom_exp_UCTW_TR_C0_5_W50_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);

    # UCT_W-FG (0.2s)
    #
    strat4_w0_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W0_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat4_w0_1 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W0_1_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat4_w0_5 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W0_5_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat4_w1_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W1_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat4_w5_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W5_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat4_w10_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W10_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);
    strat4_w50_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W50_0_vs_FULL_GREEDY_G200_T0.2_P0.dat', ' ', 19, 0);

    # UCT_W-FG (0.5s)
    #
    strat5_w0_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W0_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat5_w0_1 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W0_1_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat5_w0_5 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W0_5_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat5_w1_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W1_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat5_w5_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W5_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat5_w10_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W10_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);
    strat5_w50_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W50_0_vs_FULL_GREEDY_G200_T0.5_P0.dat', ' ', 19, 0);

    # UCT_W-FG (2.0s)
    #
    strat6_w0_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W0_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat6_w0_1 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W0_1_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat6_w0_5 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W0_5_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat6_w1_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W1_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat6_w5_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W5_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat6_w10_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W10_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);
    strat6_w50_0 = dlmread('runs/kdom_exp-20180218-225300-rev-cd84729-cpu-3.20GHz/kdom_exp_UCTW_FG_C1_0_W50_0_vs_FULL_GREEDY_G200_T2.0_P0.dat', ' ', 19, 0);


    #
    # Set run names
    #

    strat1_w0_0_str = "UCT_W-TR W=0.0 (0.2s)";
    strat1_w0_1_str = "UCT_W-TR W=0.1 (0.2s)";
    strat1_w0_5_str = "UCT_W-TR W=0.5 (0.2s)";
    strat1_w1_0_str = "UCT_W-TR W=1.0 (0.2s)";
    strat1_w5_0_str = "UCT_W-TR W=5.0 (0.2s)";
    strat1_w10_0_str = "UCT_W-TR W=10.0 (0.2s)";
    strat1_w50_0_str = "UCT_W-TR W=50.0 (0.2s)";

    strat2_w0_0_str = "UCT_W-TR W=0.0 (0.5s)";
    strat2_w0_1_str = "UCT_W-TR W=0.1 (0.5s)";
    strat2_w0_5_str = "UCT_W-TR W=0.5 (0.5s)";
    strat2_w1_0_str = "UCT_W-TR W=1.0 (0.5s)";
    strat2_w5_0_str = "UCT_W-TR W=5.0 (0.5s)";
    strat2_w10_0_str = "UCT_W-TR W=10.0 (0.5s)";
    strat2_w50_0_str = "UCT_W-TR W=50.0 (0.5s)";

    strat3_w0_0_str = "UCT_W-TR W=0.0 (2.0s)";
    strat3_w0_1_str = "UCT_W-TR W=0.1 (2.0s)";
    strat3_w0_5_str = "UCT_W-TR W=0.5 (2.0s)";
    strat3_w1_0_str = "UCT_W-TR W=1.0 (2.0s)";
    strat3_w5_0_str = "UCT_W-TR W=5.0 (2.0s)";
    strat3_w10_0_str = "UCT_W-TR W=10.0 (2.0s)";
    strat3_w50_0_str = "UCT_W-TR W=50.0 (2.0s)";

    strat4_w0_0_str = "UCT_W-FG W=0.0 (0.2s)";
    strat4_w0_1_str = "UCT_W-FG W=0.1 (0.2s)";
    strat4_w0_5_str = "UCT_W-FG W=0.5 (0.2s)";
    strat4_w1_0_str = "UCT_W-FG W=1.0 (0.2s)";
    strat4_w5_0_str = "UCT_W-FG W=5.0 (0.2s)";
    strat4_w10_0_str = "UCT_W-FG W=10.0 (0.2s)";
    strat4_w50_0_str = "UCT_W-FG W=50.0 (0.2s)";

    strat5_w0_0_str = "UCT_W-FG W=0.0 (0.5s)";
    strat5_w0_1_str = "UCT_W-FG W=0.1 (0.5s)";
    strat5_w0_5_str = "UCT_W-FG W=0.5 (0.5s)";
    strat5_w1_0_str = "UCT_W-FG W=1.0 (0.5s)";
    strat5_w5_0_str = "UCT_W-FG W=5.0 (0.5s)";
    strat5_w10_0_str = "UCT_W-FG W=10.0 (0.5s)";
    strat5_w50_0_str = "UCT_W-FG W=50.0 (0.5s)";

    strat6_w0_0_str = "UCT_W-FG W=0.0 (2.0s)";
    strat6_w0_1_str = "UCT_W-FG W=0.1 (2.0s)";
    strat6_w0_5_str = "UCT_W-FG W=0.5 (2.0s)";
    strat6_w1_0_str = "UCT_W-FG W=1.0 (2.0s)";
    strat6_w5_0_str = "UCT_W-FG W=5.0 (2.0s)";
    strat6_w10_0_str = "UCT_W-FG W=10.0 (2.0s)";
    strat6_w50_0_str = "UCT_W-FG W=50.0 (2.0s)";


    #
    # Create cell arrays
    #

    strats_1{1} = getStratCellArrayVersion4(strat1_w0_0, strat1_w0_0_str, opponent_strat_str);
    strats_1{2} = getStratCellArrayVersion4(strat1_w0_1, strat1_w0_1_str, opponent_strat_str);
    strats_1{3} = getStratCellArrayVersion4(strat1_w0_5, strat1_w0_5_str, opponent_strat_str);
    strats_1{4} = getStratCellArrayVersion4(strat1_w1_0, strat1_w1_0_str, opponent_strat_str);
    strats_1{5} = getStratCellArrayVersion4(strat1_w5_0, strat1_w5_0_str, opponent_strat_str);
    strats_1{6} = getStratCellArrayVersion4(strat1_w10_0, strat1_w10_0_str, opponent_strat_str);
    strats_1{7} = getStratCellArrayVersion4(strat1_w50_0, strat1_w50_0_str, opponent_strat_str);

    strats_2{1} = getStratCellArrayVersion4(strat2_w0_0, strat2_w0_0_str, opponent_strat_str);
    strats_2{2} = getStratCellArrayVersion4(strat2_w0_1, strat2_w0_1_str, opponent_strat_str);
    strats_2{3} = getStratCellArrayVersion4(strat2_w0_5, strat2_w0_5_str, opponent_strat_str);
    strats_2{4} = getStratCellArrayVersion4(strat2_w1_0, strat2_w1_0_str, opponent_strat_str);
    strats_2{5} = getStratCellArrayVersion4(strat2_w5_0, strat2_w5_0_str, opponent_strat_str);
    strats_2{6} = getStratCellArrayVersion4(strat2_w10_0, strat2_w10_0_str, opponent_strat_str);
    strats_2{7} = getStratCellArrayVersion4(strat2_w50_0, strat2_w50_0_str, opponent_strat_str);

    strats_3{1} = getStratCellArrayVersion4(strat3_w0_0, strat3_w0_0_str, opponent_strat_str);
    strats_3{2} = getStratCellArrayVersion4(strat3_w0_1, strat3_w0_1_str, opponent_strat_str);
    strats_3{3} = getStratCellArrayVersion4(strat3_w0_5, strat3_w0_5_str, opponent_strat_str);
    strats_3{4} = getStratCellArrayVersion4(strat3_w1_0, strat3_w1_0_str, opponent_strat_str);
    strats_3{5} = getStratCellArrayVersion4(strat3_w5_0, strat3_w5_0_str, opponent_strat_str);
    strats_3{6} = getStratCellArrayVersion4(strat3_w10_0, strat3_w10_0_str, opponent_strat_str);
    strats_3{7} = getStratCellArrayVersion4(strat3_w50_0, strat3_w50_0_str, opponent_strat_str);

    strats_4{1} = getStratCellArrayVersion4(strat4_w0_0, strat4_w0_0_str, opponent_strat_str);
    strats_4{2} = getStratCellArrayVersion4(strat4_w0_1, strat4_w0_1_str, opponent_strat_str);
    strats_4{3} = getStratCellArrayVersion4(strat4_w0_5, strat4_w0_5_str, opponent_strat_str);
    strats_4{4} = getStratCellArrayVersion4(strat4_w1_0, strat4_w1_0_str, opponent_strat_str);
    strats_4{5} = getStratCellArrayVersion4(strat4_w5_0, strat4_w5_0_str, opponent_strat_str);
    strats_4{6} = getStratCellArrayVersion4(strat4_w10_0, strat4_w10_0_str, opponent_strat_str);
    strats_4{7} = getStratCellArrayVersion4(strat4_w50_0, strat4_w50_0_str, opponent_strat_str);

    strats_5{1} = getStratCellArrayVersion4(strat5_w0_0, strat5_w0_0_str, opponent_strat_str);
    strats_5{2} = getStratCellArrayVersion4(strat5_w0_1, strat5_w0_1_str, opponent_strat_str);
    strats_5{3} = getStratCellArrayVersion4(strat5_w0_5, strat5_w0_5_str, opponent_strat_str);
    strats_5{4} = getStratCellArrayVersion4(strat5_w1_0, strat5_w1_0_str, opponent_strat_str);
    strats_5{5} = getStratCellArrayVersion4(strat5_w5_0, strat5_w5_0_str, opponent_strat_str);
    strats_5{6} = getStratCellArrayVersion4(strat5_w10_0, strat5_w10_0_str, opponent_strat_str);
    strats_5{7} = getStratCellArrayVersion4(strat5_w50_0, strat5_w50_0_str, opponent_strat_str);

    strats_6{1} = getStratCellArrayVersion4(strat6_w0_0, strat6_w0_0_str, opponent_strat_str);
    strats_6{2} = getStratCellArrayVersion4(strat6_w0_1, strat6_w0_1_str, opponent_strat_str);
    strats_6{3} = getStratCellArrayVersion4(strat6_w0_5, strat6_w0_5_str, opponent_strat_str);
    strats_6{4} = getStratCellArrayVersion4(strat6_w1_0, strat6_w1_0_str, opponent_strat_str);
    strats_6{5} = getStratCellArrayVersion4(strat6_w5_0, strat6_w5_0_str, opponent_strat_str);
    strats_6{6} = getStratCellArrayVersion4(strat6_w10_0, strat6_w10_0_str, opponent_strat_str);
    strats_6{7} = getStratCellArrayVersion4(strat6_w50_0, strat6_w50_0_str, opponent_strat_str);



    #
    # Process data.
    #

    score_diffs{1} = getScoreDiffsCellArray(strats_1, "UCT_W-TR_T0_2");
    score_diffs{2} = getScoreDiffsCellArray(strats_2, "UCT_W-TR_T0_5");
    score_diffs{3} = getScoreDiffsCellArray(strats_3, "UCT_W-TR_T2_0");
    score_diffs{4} = getScoreDiffsCellArray(strats_4, "UCT_W-FG_T0_2");
    score_diffs{5} = getScoreDiffsCellArray(strats_5, "UCT_W-FG_T0_5");
    score_diffs{6} = getScoreDiffsCellArray(strats_6, "UCT_W-FG_T2_0");

    #plotScoreDiffs(score_diffs, num_games, w_values);
    writeScoreDiffsToDatFile(score_diffs, output_path, w_values);
    #writeScoreDiffsToDatFileInverted(score_diffs, time_limits, w_values, output_path);

endfunction
