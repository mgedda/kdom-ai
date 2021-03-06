#=============================================================================
# Experiment 3 - Comparing scoring functions
#-----------------------------------------------------------------------------
# Compares AI players using the evaluators
#
#    MCE-TR/WDL - Monte-Carlo Evaluation with random playout policy (TR)
#                 and Win-Draw-Loss scoring function (WDL)
#    MCE-TR/P   - Monte-Carlo Evaluation with random playout policy (TR)
#                 and Player Greedy scoring function (P)
#    MCE-TR/R   - Monte-Carlo Evaluation with random playout policy (TR)
#                 and Relative scoring function (R)
#
# This m-file processes result files from games between Monte-Carlo
# Evaluation players using different scoring functions.
#
# The processed information is printed to .dat files with space-separated
# values suitable for plotting with gnuplot.
#


function process_experiment_3(target_path, output_dir)

    output_path = [target_path "/" output_dir];
    mkdir(output_path);
    output_file_prefix = "";

    addpath("m_files");   # add project m files to path
    util_functions;       # load functions needed by this script
    io_functions;
    plot_functions;

    opponent_strat_str = "FG";
    num_games = 500;

    #
    # Read experiment result files
    #

    strat1 = dlmread('runs/kdom_exp-20180205-150829-rev-85a55d8-cpu-3.20GHz/kdom_exp_MCE_TR_WDL_vs_FULL_GREEDY_G500_T5_P0.dat', ' ', 19, 0);
    strat2 = dlmread('runs/kdom_exp-20180205-150829-rev-85a55d8-cpu-3.20GHz/kdom_exp_MCE_TR_P_vs_FULL_GREEDY_G500_T5_P0.dat', ' ', 19, 0);
    strat3 = dlmread('runs/kdom_exp-20180205-150829-rev-85a55d8-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_G500_T5_P0.dat', ' ', 19, 0);

    #
    # Set run names
    #

    strat1_str = "MCE-TR/WDL";
    strat2_str = "MCE-TR/P";
    strat3_str = "MCE-TR/R";

    #
    # Create cell arrays
    #

    strats{1} = getStratCellArrayVersion4(strat1, strat1_str, opponent_strat_str);
    strats{2} = getStratCellArrayVersion4(strat2, strat2_str, opponent_strat_str);
    strats{3} = getStratCellArrayVersion4(strat3, strat3_str, opponent_strat_str);


    #
    # Process data
    #


    # Hypothesis test.
    #
    pval = t_test_2(strats{2}{5}, strats{3}{5}, ">")


    # Win/Loss/Draw pie charts.
    #
    #plotWinDrawLossPieCharts(strats);
    #plotScoreHistograms(strats);
    #plotScoreHistogramsOverlap(strats);


    # Player scores
    #
    #plotPlayerScoresOverlap(strats);
    writePlayerScoresToDatFile(strats, output_path, output_file_prefix);


    # Branching factors
    #
    #plotBranchingFactors(strats);


    # Playouts per second
    #
    #plotPlayoutsPerSecondOverlap(strats(1:2));
    writePlayoutsToDatFile(strats, output_path, output_file_prefix);


    return;


    # Number of dominoes to chose from in chosen draft.
    #
    #plotAvailableDraftDominoes(strats);


    # Chosen domino position.
    #
    #plotChosenDominoPositions(strats);

endfunction

