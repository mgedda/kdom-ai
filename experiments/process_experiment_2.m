#=============================================================================
# Experiment 2 - Static vs statistical evaluator
#-----------------------------------------------------------------------------
# Compares AI players using the evaluators
#
#    FG         - Full greedy
#    MCE-TR/WDL - Monte-Carlo Evaluation with random playout policy (TR)
#                 and Win-Draw-Loss scoring function (WDL)
#
# This m-file processes result files from games between players using
# Monte-Carlo Evaluation (statistical evaluator) and Full Greedy (static
# evaluator).
#
# The processed information is printed to .dat files with space-separated
# values suitable for plotting with gnuplot.
#

function process_experiment_2(target_path, output_dir)

    output_path = [target_path "/" output_dir];
    mkdir(output_path);
    output_file_prefix = "";

    addpath("m_files");     # add project m files to path
    util_functions;         # load functions needed by this script
    io_functions;
    plot_functions;

    opponent_strat_str = "FG";
    num_games = 450;

    #
    # Read experiment result files
    #

    source("runs/kdom_exp-20170326-005955-rev-fd90607/kdom_exp_MC_TR_TR_WDL_vs_FULL_GREEDY.m");
    source("runs/kdom_exp-20170326-190517-rev-fd90607/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY.m");
    strat1_str = "MCE-TR/WDL";
    strat2_str = "FG";
    strat1 = kdom_exp_MC_TR_TR_WDL_vs_FULL_GREEDY(1:num_games,:);
    strat2 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY(1:num_games,:);


    #
    # Process data
    #

    strats{1} = getStratCellArrayVersion3(strat1, strat1_str, opponent_strat_str);
    strats{2} = getStratCellArrayVersion3(strat2, strat2_str, opponent_strat_str);


    # Hypothesis test.
    #
    pval = t_test_2(strats{1}{5}, strats{2}{5}, ">")


    # Win/Loss/Draw pie charts.
    #
    plotWinDrawLossPieCharts(strats);


    # Score histograms.
    #
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
    plotPlayoutsPerSecondOverlap(strats);
    writePlayoutsToDatFile(strats(1), output_path, output_file_prefix);


    return;


    # Number of dominoes to chose from in chosen draft.
    #
    plotAvailableDraftDominoes(strats);


    # Chosen domino position.
    #
    plotChosenDominoPositions(strats);

endfunction
