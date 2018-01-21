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
    #num_games = 450;
    num_games = 300;

    #
    # Read experiment result files
    #

    source("runs/kdom_exp-20170326-190517-rev-fd90607/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY.m");
    source("runs/kdom_exp-20170326-005955-rev-fd90607/kdom_exp_MC_TR_TR_WDL_vs_FULL_GREEDY.m");
    source("runs/kdom_exp-20170328-150600-rev-8f1e859/kdom_exp_MC_TR_TR_P_vs_FULL_GREEDY.m");
    source("runs/kdom_exp-20170327-161148-rev-8f1e859/kdom_exp_MC_TR_TR_R_vs_FULL_GREEDY.m");
    source("runs/kdom_exp-20170331-213055-rev-3cc76b2-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T10_P0.m");
    strat1_str = "FG";
    strat2_str = "MCE-TR/WDL";
    strat3_str = "MCE-TR/P";
    strat4_str = "MCE-TR/R";
    strat5_str = "MCE-FG/R";
    strat1 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY(1:num_games,:);
    strat2 = kdom_exp_MC_TR_TR_WDL_vs_FULL_GREEDY(1:num_games,:);
    strat3 = kdom_exp_MC_TR_TR_P_vs_FULL_GREEDY(1:num_games,:);
    strat4 = kdom_exp_MC_TR_TR_R_vs_FULL_GREEDY(1:num_games,:);
    strat5 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T10_P0(1:num_games,:);

    #
    # Process data
    #

    strats{1} = getStratCellArrayVersion3(strat1, strat1_str, opponent_strat_str);
    strats{2} = getStratCellArrayVersion3(strat2, strat2_str, opponent_strat_str);
    strats{3} = getStratCellArrayVersion3(strat3, strat3_str, opponent_strat_str);
    strats{4} = getStratCellArrayVersion3(strat4, strat4_str, opponent_strat_str);
    strats{5} = getStratCellArrayVersion3(strat5, strat5_str, opponent_strat_str);


    # Hypothesis test.
    #
    pval = t_test_2(strats{3}{5}, strats{2}{5}, ">")


    # Win/Loss/Draw pie charts.
    #
    plotWinDrawLossPieCharts(strats);
    #plotScoreHistograms(strats);
    plotScoreHistogramsOverlap(strats);


    # Player scores
    #
    plotPlayerScoresOverlap(strats);
    writePlayerScoresToDatFile(strats, output_path, output_file_prefix);


    # Branching factors
    #
    plotBranchingFactors(strats);


    # Playouts per second
    #
    plotPlayoutsPerSecondOverlap(strats(2:3));
    writePlayoutsToDatFile(strats, output_path, output_file_prefix);


    return;


    # Number of dominoes to chose from in chosen draft.
    #
    plotAvailableDraftDominoes(strats);


    # Chosen domino position.
    #
    plotChosenDominoPositions(strats);

endfunction

