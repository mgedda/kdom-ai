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
    num_games = 1000;

    #
    # Read experiment result files
    #

    strat1 = dlmread('runs/kdom_exp-20180205-150829-rev-85a55d8-cpu-3.20GHz/kdom_exp_TRUE_RANDOM_vs_TRUE_RANDOM_G1000_T0.1_P0.dat', ' ', 19, 0);
    strat2 = dlmread('runs/kdom_exp-20180205-150829-rev-85a55d8-cpu-3.20GHz/kdom_exp_GREEDY_PLACEMENT_RANDOM_DRAFT_vs_TRUE_RANDOM_G1000_T0.1_P0.dat', ' ', 19, 0);
    strat3 = dlmread('runs/kdom_exp-20180205-150829-rev-85a55d8-cpu-3.20GHz/kdom_exp_FULL_GREEDY_vs_TRUE_RANDOM_G1000_T0.1_P0.dat', ' ', 19, 0);

    #
    # Set run names
    #

    strat1_str = "TR";
    strat2_str = "GPRD";
    strat3_str = "FG";

    #
    # Create cell arrays
    #

    strats{1}  = getStratCellArrayVersion4(strat1, strat1_str, opponent_strat_str);
    strats{2}  = getStratCellArrayVersion4(strat2, strat2_str, opponent_strat_str);
    strats{3}  = getStratCellArrayVersion4(strat3, strat3_str, opponent_strat_str);


    #
    # Process data
    #

    #
    # Write Win/Loss/Draw info to file
    #
    function p = toPercent(fraction)
      p = round(fraction * 1000) / 10;
    endfunction

    function writeWinDrawLossToFile(strats, output_path, output_file_prefix)

      # Construct the filename
      filename = [output_path "/" output_file_prefix "WIN_DRAW_LOSS.dat"]

      fid = fopen (filename, "w");
      fdisp (fid, "# Wins  Win(%)  Draws  Draw(%)  Losses Loss(%)");
      fclose (fid);

      for i = 1:size(strats,2)

        # Win/Loss/Draw vector (1x3 matrix)
        wdl = strats{i}{6};

        strat_str = strats{i}{1};
        opponent_str = strats{i}{2};
        num_games = size(strats{i}{4}, 1);

        # Compose data in file.
        # The data will be on the form
        #
        #    # Wins  Win(%)  Draws  Draw(%)  Losses Loss(%)
        #    # TR
        #    254     25.4    82     8.2      734    73.4
        #    # GPRD
        #    465     46.5    21     2.1      512    51.2
        #    # FG
        #    923     92.3    2      0.2      75     7.5
        #
        fid = fopen (filename, "a");
        fdisp (fid, ["# " strat_str]);
        fclose (fid);

        data = [wdl(1) toPercent(wdl(1)/num_games) wdl(2) toPercent(wdl(2)/num_games) wdl(3) toPercent(wdl(3)/num_games)];

        # Write data to file
        dlmwrite(filename, data, "-append", "delimiter", " ");

      endfor
    endfunction

    writeWinDrawLossToFile(strats, output_path, output_file_prefix);


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
