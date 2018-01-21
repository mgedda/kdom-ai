#=============================================================================
# Experiment 1 - Branching factor and impact of basic domain knowledge
#-----------------------------------------------------------------------------
# Compares AI players using the static evaluators
#
#    TR   - True random moves
#    GPRD - Greedy placement random draft
#    FG   - Full greedy
#
# This m-file processes result files from games where each AI player played
# against three TR opponents.
#
# The processed information is printed to .dat files with space-separated
# values suitable for plotting with gnuplot.
#

function process_experiment_1(target_path, output_dir)

    output_path = [target_path "/" output_dir];
    mkdir(output_path);
    output_file_prefix = "";

    addpath("m_files");     # add project m files to path
    util_functions;         # load functions needed by this script
    io_functions;
    plot_functions;

    opponent_strat_str = "TR";
    num_games = 1500;

    #
    # Read experiment result files
    #

    source("runs/kdom_exp-20170322-235332-rev-7a53876/kdom_exp_TRUE_RANDOM_vs_TRUE_RANDOM.m");
    source("runs/kdom_exp-20170322-235357-rev-7a53876/kdom_exp_GREEDY_PLACEMENT_RANDOM_DRAFT_vs_TRUE_RANDOM.m");
    source("runs/kdom_exp-20170322-235414-rev-7a53876/kdom_exp_FULL_GREEDY_vs_TRUE_RANDOM.m");
    strat1_str = "TR";
    strat2_str = "GPRD";
    strat3_str = "FG";
    strat1 = kdom_exp_TRUE_RANDOM_vs_TRUE_RANDOM(1:num_games,:);
    strat2 = kdom_exp_GREEDY_PLACEMENT_RANDOM_DRAFT_vs_TRUE_RANDOM(1:num_games,:);
    strat3 = kdom_exp_FULL_GREEDY_vs_TRUE_RANDOM(1:num_games,:);

    #
    # Process data
    #

    strats{1} = getStratCellArrayVersion3(strat1, strat1_str, opponent_strat_str);
    strats{2} = getStratCellArrayVersion3(strat2, strat2_str, opponent_strat_str);
    strats{3} = getStratCellArrayVersion3(strat3, strat3_str, opponent_strat_str);


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
    writeBranchingFactorsToDatFile(strats, output_path, output_file_prefix);


    return;


    # Number of dominoes to chose from in chosen draft.
    #
    plotAvailableDraftDominoes(strats);


    # Chosen domino position.
    #
    plotChosenDominoPositions(strats);


    # Hypothesis test.
    #
    pval = t_test_2(strats{7}{5}, strats{5}{5}, ">")

endfunction
