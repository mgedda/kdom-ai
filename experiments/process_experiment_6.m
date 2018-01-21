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
    time_limits = [1.0 2.0];                # time limits
    c_values = [0.1 0.2 0.3 0.4 0.5 0.6];   # UCB constant values

    #
    # Read experiment result file
    #

    strat1_t1_0 = dlmread('runs/kdom_exp-20180118-072314-rev-9b54a81-cpu-3.20GHz/kdom_exp_UCT_TR_C0_1_W0_0_vs_FULL_GREEDY_T1_P0.dat', ' ', 19, 0);
    strat2_t1_0 = dlmread('runs/kdom_exp-20180118-072314-rev-9b54a81-cpu-3.20GHz/kdom_exp_UCT_TR_C0_2_W0_0_vs_FULL_GREEDY_T1_P0.dat', ' ', 19, 0);
    strat3_t1_0 = dlmread('runs/kdom_exp-20180118-072314-rev-9b54a81-cpu-3.20GHz/kdom_exp_UCT_TR_C0_3_W0_0_vs_FULL_GREEDY_T1_P0.dat', ' ', 19, 0);
    strat4_t1_0 = dlmread('runs/kdom_exp-20180118-072314-rev-9b54a81-cpu-3.20GHz/kdom_exp_UCT_TR_C0_4_W0_0_vs_FULL_GREEDY_T1_P0.dat', ' ', 19, 0);
    strat5_t1_0 = dlmread('runs/kdom_exp-20180118-072314-rev-9b54a81-cpu-3.20GHz/kdom_exp_UCT_TR_C0_5_W0_0_vs_FULL_GREEDY_T1_P0.dat', ' ', 19, 0);
    strat6_t1_0 = dlmread('runs/kdom_exp-20180118-072314-rev-9b54a81-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_W0_0_vs_FULL_GREEDY_T1_P0.dat', ' ', 19, 0);

    strat1_t2_0 = dlmread('runs/kdom_exp-20180113-151620-rev-08e3392-cpu-3.20GHz/kdom_exp_UCT_TR_C0_1_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
    strat2_t2_0 = dlmread('runs/kdom_exp-20180113-151620-rev-08e3392-cpu-3.20GHz/kdom_exp_UCT_TR_C0_2_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
    strat3_t2_0 = dlmread('runs/kdom_exp-20180113-151620-rev-08e3392-cpu-3.20GHz/kdom_exp_UCT_TR_C0_3_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
    strat4_t2_0 = dlmread('runs/kdom_exp-20180113-151620-rev-08e3392-cpu-3.20GHz/kdom_exp_UCT_TR_C0_4_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
    strat5_t2_0 = dlmread('runs/kdom_exp-20180113-151620-rev-08e3392-cpu-3.20GHz/kdom_exp_UCT_TR_C0_5_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
    strat6_t2_0 = dlmread('runs/kdom_exp-20180118-072314-rev-9b54a81-cpu-3.20GHz/kdom_exp_UCT_TR_C0_6_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);

    strat1_t1_0_str = "C=0.1 (1s)";
    strat2_t1_0_str = "C=0.2 (1s)";
    strat3_t1_0_str = "C=0.3 (1s)";
    strat4_t1_0_str = "C=0.4 (1s)";
    strat5_t1_0_str = "C=0.5 (1s)";
    strat6_t1_0_str = "C=0.6 (1s)";

    strat1_t2_0_str = "C=0.1 (2s)";
    strat2_t2_0_str = "C=0.2 (2s)";
    strat3_t2_0_str = "C=0.3 (2s)";
    strat4_t2_0_str = "C=0.4 (2s)";
    strat5_t2_0_str = "C=0.5 (2s)";
    strat6_t2_0_str = "C=0.6 (2s)";

    strats_1{1} = getStratCellArrayVersion4(strat1_t1_0, strat1_t1_0_str, opponent_strat_str);
    strats_2{1} = getStratCellArrayVersion4(strat2_t1_0, strat2_t1_0_str, opponent_strat_str);
    strats_3{1} = getStratCellArrayVersion4(strat3_t1_0, strat3_t1_0_str, opponent_strat_str);
    strats_4{1} = getStratCellArrayVersion4(strat4_t1_0, strat4_t1_0_str, opponent_strat_str);
    strats_5{1} = getStratCellArrayVersion4(strat5_t1_0, strat5_t1_0_str, opponent_strat_str);
    strats_6{1} = getStratCellArrayVersion4(strat6_t1_0, strat6_t1_0_str, opponent_strat_str);

    strats_1{2} = getStratCellArrayVersion4(strat1_t2_0, strat1_t2_0_str, opponent_strat_str);
    strats_2{2} = getStratCellArrayVersion4(strat2_t2_0, strat2_t2_0_str, opponent_strat_str);
    strats_3{2} = getStratCellArrayVersion4(strat3_t2_0, strat3_t2_0_str, opponent_strat_str);
    strats_4{2} = getStratCellArrayVersion4(strat4_t2_0, strat4_t2_0_str, opponent_strat_str);
    strats_5{2} = getStratCellArrayVersion4(strat5_t2_0, strat5_t2_0_str, opponent_strat_str);
    strats_6{2} = getStratCellArrayVersion4(strat6_t2_0, strat6_t2_0_str, opponent_strat_str);


    #
    # Process data.
    #

    score_diffs{1} = getScoreDiffsCellArray(strats_1, "C0.1");
    score_diffs{2} = getScoreDiffsCellArray(strats_2, "C0.2");
    score_diffs{3} = getScoreDiffsCellArray(strats_3, "C0.3");
    score_diffs{4} = getScoreDiffsCellArray(strats_4, "C0.4");
    score_diffs{5} = getScoreDiffsCellArray(strats_5, "C0.5");
    score_diffs{6} = getScoreDiffsCellArray(strats_6, "C0.6");

    plotScoreDiffs(score_diffs, num_games, time_limits);
    writeScoreDiffsToDatFile(score_diffs, output_path, time_limits);
    writeScoreDiffsToDatFileInverted(score_diffs, time_limits, c_values, output_path);

    win_percentages{1} = getWinPercentagesCellArray(strats_1, "C0.1");
    win_percentages{2} = getWinPercentagesCellArray(strats_2, "C0.2");
    win_percentages{3} = getWinPercentagesCellArray(strats_3, "C0.3");
    win_percentages{4} = getWinPercentagesCellArray(strats_4, "C0.4");
    win_percentages{5} = getWinPercentagesCellArray(strats_5, "C0.5");
    win_percentages{6} = getWinPercentagesCellArray(strats_6, "C0.6");

    writeWinPercentagesToDatFileInverted(win_percentages, time_limits, c_values, output_path);

endfunction
