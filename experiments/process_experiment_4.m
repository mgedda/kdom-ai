#=============================================================================
# Experiment 4 - Comparing different playout policies (scores and playouts/s)
#-----------------------------------------------------------------------------
# Compares Monte-Carlo Evaluation players using the playout policies
#
#    FG - Full Greedy playout policy
#    TR - True Random playout policy
#    eG - epsilon-Greedy playout policy
#    PG - Player Greedy playout policy
#
# This m-file processes result files from games between Monte-Carlo
# Evaluation players using different playout policies for different
# time limits. It also stores the average playouts/s for each round
# for each player.
#
# The processed information is printed to .dat files with space-separated
# values suitable for plotting with gnuplot.
#

function process_experiment_4(target_path, output_dir)

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


    #
    # Set run names
    #

    strat1_t0_1_str = "MCE-TR/R (0.1s)";
    strat1_t0_2_str = "MCE-TR/R (0.2s)";
    strat1_t0_3_str = "MCE-TR/R (0.3s)";
    strat1_t0_5_str = "MCE-TR/R (0.5s)";
    strat1_t1_str   = "MCE-TR/R (1s)";
    strat1_t2_str   = "MCE-TR/R (2s)";
    strat1_t4_str   = "MCE-TR/R (4s)";
    strat1_t6_str   = "MCE-TR/R (6s)";
    strat1_t8_str   = "MCE-TR/R (8s)";
    strat1_t10_str  = "MCE-TR/R (10s)";

    strat2_t0_1_str = "MCE-FG/R (0.1s)";
    strat2_t0_2_str = "MCE-FG/R (0.2s)";
    strat2_t0_3_str = "MCE-FG/R (0.3s)";
    strat2_t0_5_str = "MCE-FG/R (0.5s)";
    strat2_t1_str   = "MCE-FG/R (1s)";
    strat2_t2_str   = "MCE-FG/R (2s)";
    strat2_t4_str   = "MCE-FG/R (4s)";
    strat2_t6_str   = "MCE-FG/R (6s)";
    strat2_t8_str   = "MCE-FG/R (8s)";
    strat2_t10_str  = "MCE-FG/R (10s)";

    strat3_t0_1_str = "MCE-EG/R (0.1s)";
    strat3_t0_2_str = "MCE-EG/R (0.2s)";
    strat3_t0_3_str = "MCE-EG/R (0.3s)";
    strat3_t0_5_str = "MCE-EG/R (0.5s)";
    strat3_t1_str   = "MCE-EG/R (1s)";
    strat3_t2_str   = "MCE-EG/R (2s)";
    strat3_t4_str   = "MCE-EG/R (4s)";
    strat3_t6_str   = "MCE-EG/R (6s)";
    strat3_t8_str   = "MCE-EG/R (8s)";
    strat3_t10_str  = "MCE-EG/R (10s)";

    strat4_t0_1_str = "MCE-PG/R (0.1s)";
    strat4_t0_2_str = "MCE-PG/R (0.2s)";
    strat4_t0_3_str = "MCE-PG/R (0.3s)";
    strat4_t0_5_str = "MCE-PG/R (0.5s)";
    strat4_t1_str   = "MCE-PG/R (1s)";
    strat4_t2_str   = "MCE-PG/R (2s)";
    strat4_t4_str   = "MCE-PG/R (4s)";
    strat4_t6_str   = "MCE-PG/R (6s)";
    strat4_t8_str   = "MCE-PG/R (8s)";
    strat4_t10_str  = "MCE-PG/R (10s)";


    #
    # Create cell arrays
    #

    strats_1{1}  = getStratCellArrayVersion4(strat1_t0_1, strat1_t0_1_str, opponent_strat_str);
    strats_1{2}  = getStratCellArrayVersion4(strat1_t0_2, strat1_t0_2_str, opponent_strat_str);
    strats_1{3}  = getStratCellArrayVersion4(strat1_t0_3, strat1_t0_3_str, opponent_strat_str);
    strats_1{4}  = getStratCellArrayVersion4(strat1_t0_5, strat1_t0_5_str, opponent_strat_str);
    strats_1{5}  = getStratCellArrayVersion4(strat1_t1,   strat1_t1_str,   opponent_strat_str);
    strats_1{6}  = getStratCellArrayVersion4(strat1_t2,   strat1_t2_str,   opponent_strat_str);
    strats_1{7}  = getStratCellArrayVersion4(strat1_t4,   strat1_t4_str,   opponent_strat_str);
    strats_1{8}  = getStratCellArrayVersion4(strat1_t6,   strat1_t6_str,   opponent_strat_str);
    strats_1{9}  = getStratCellArrayVersion4(strat1_t8,   strat1_t8_str,   opponent_strat_str);
    strats_1{10} = getStratCellArrayVersion4(strat1_t10,  strat1_t10_str,  opponent_strat_str);

    strats_2{1}  = getStratCellArrayVersion4(strat2_t0_1, strat2_t0_1_str, opponent_strat_str);
    strats_2{2}  = getStratCellArrayVersion4(strat2_t0_2, strat2_t0_2_str, opponent_strat_str);
    strats_2{3}  = getStratCellArrayVersion4(strat2_t0_3, strat2_t0_3_str, opponent_strat_str);
    strats_2{4}  = getStratCellArrayVersion4(strat2_t0_5, strat2_t0_5_str, opponent_strat_str);
    strats_2{5}  = getStratCellArrayVersion4(strat2_t1,   strat2_t1_str,   opponent_strat_str);
    strats_2{6}  = getStratCellArrayVersion4(strat2_t2,   strat2_t2_str,   opponent_strat_str);
    strats_2{7}  = getStratCellArrayVersion4(strat2_t4,   strat2_t4_str,   opponent_strat_str);
    strats_2{8}  = getStratCellArrayVersion4(strat2_t6,   strat2_t6_str,   opponent_strat_str);
    strats_2{9}  = getStratCellArrayVersion4(strat2_t8,   strat2_t8_str,   opponent_strat_str);
    strats_2{10} = getStratCellArrayVersion4(strat2_t10,  strat2_t10_str,  opponent_strat_str);

    strats_3{1}  = getStratCellArrayVersion4(strat3_t0_1, strat3_t0_1_str, opponent_strat_str);
    strats_3{2}  = getStratCellArrayVersion4(strat3_t0_2, strat3_t0_2_str, opponent_strat_str);
    strats_3{3}  = getStratCellArrayVersion4(strat3_t0_3, strat3_t0_3_str, opponent_strat_str);
    strats_3{4}  = getStratCellArrayVersion4(strat3_t0_5, strat3_t0_5_str, opponent_strat_str);
    strats_3{5}  = getStratCellArrayVersion4(strat3_t1,   strat3_t1_str,   opponent_strat_str);
    strats_3{6}  = getStratCellArrayVersion4(strat3_t2,   strat3_t2_str,   opponent_strat_str);
    strats_3{7}  = getStratCellArrayVersion4(strat3_t4,   strat3_t4_str,   opponent_strat_str);
    strats_3{8}  = getStratCellArrayVersion4(strat3_t6,   strat3_t6_str,   opponent_strat_str);
    strats_3{9}  = getStratCellArrayVersion4(strat3_t8,   strat3_t8_str,   opponent_strat_str);
    strats_3{10} = getStratCellArrayVersion4(strat3_t10,  strat3_t10_str,  opponent_strat_str);

    strats_4{1}  = getStratCellArrayVersion4(strat4_t0_1, strat4_t0_1_str, opponent_strat_str);
    strats_4{2}  = getStratCellArrayVersion4(strat4_t0_2, strat4_t0_2_str, opponent_strat_str);
    strats_4{3}  = getStratCellArrayVersion4(strat4_t0_3, strat4_t0_3_str, opponent_strat_str);
    strats_4{4}  = getStratCellArrayVersion4(strat4_t0_5, strat4_t0_5_str, opponent_strat_str);
    strats_4{5}  = getStratCellArrayVersion4(strat4_t1,   strat4_t1_str,   opponent_strat_str);
    strats_4{6}  = getStratCellArrayVersion4(strat4_t2,   strat4_t2_str,   opponent_strat_str);
    strats_4{7}  = getStratCellArrayVersion4(strat4_t4,   strat4_t4_str,   opponent_strat_str);
    strats_4{8}  = getStratCellArrayVersion4(strat4_t6,   strat4_t6_str,   opponent_strat_str);
    strats_4{9}  = getStratCellArrayVersion4(strat4_t8,   strat4_t8_str,   opponent_strat_str);
    strats_4{10} = getStratCellArrayVersion4(strat4_t10,  strat4_t10_str,  opponent_strat_str);


    #
    # Process data
    #

    score_diffs{1} = getScoreDiffsCellArray(strats_1, "MCE-TR/R");
    score_diffs{2} = getScoreDiffsCellArray(strats_2, "MCE-FG/R");
    score_diffs{3} = getScoreDiffsCellArray(strats_3, "MCE-EG/R");
    score_diffs{4} = getScoreDiffsCellArray(strats_4, "MCE-PG/R");

    #plotScoreDiffs(score_diffs, num_games, time_limits);
    writeScoreDiffsToDatFile(score_diffs, output_path, time_limits);


    # Playouts per second
    #
    playout_strats{1} = strats_1{10};
    playout_strats{2} = strats_2{10};
    playout_strats{3} = strats_3{10};
    playout_strats{4} = strats_4{10};

    #plotPlayoutsPerSecondOverlap(playout_strats);
    writePlayoutsToDatFile(playout_strats, output_path, output_file_prefix);


    return;


    # Hypothesis test.
    #
    pval = t_test_2(strats{3}{5}, strats{2}{5}, ">")


    # Win/Loss/Draw pie charts.
    #
    #plotWinDrawLossPieCharts(strats);


    # Score histograms.
    #
    #plotScoreHistograms(strats);
    #plotScoreHistogramsOverlap(strats);


    # Player scores
    #
    #plotPlayerScoresOverlap(strats);
    writePlayerScoresToDatFile(strats, output_path, output_file_prefix);


    return;


    # Branching factors
    #
    #plotBranchingFactors(strats);


    # Number of dominoes to chose from in chosen draft.
    #
    #plotAvailableDraftDominoes(strats);


    # Chosen domino position.
    #
    #plotChosenDominoPositions(strats);

endfunction
