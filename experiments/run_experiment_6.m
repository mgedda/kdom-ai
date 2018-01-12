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

clear all;
close all;

#----------------------------------
# Setup.
#

addpath("m_files");            # add project m files to path

strat_cell_array_version_4;    # load functions needed by this script
experiment_functions;


output_dir = "run_experiment_6.out";
output_file_prefix = "";

mkdir(output_dir);

opponent_strat_str = "FG";

num_games = 20;
time_limits = [0.5 2.0];    # time limits used

strat1_t0_5 = dlmread('kdom_exp-20180112-115628-rev-9542014-cpu-2.70GHz/kdom_exp_UCT_TR_C01_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
strat1_t2_0 = dlmread('kdom_exp-20180112-115628-rev-9542014-cpu-2.70GHz/kdom_exp_UCT_TR_C01_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat1_t0_5_str = "C=0.1 (0.5s)";
strat1_t2_0_str = "C=0.1 (2s)";
strats_1{1} = getStratCellArrayVersion4(strat1_t0_5, strat1_t0_5_str, opponent_strat_str);
strats_1{2} = getStratCellArrayVersion4(strat1_t2_0, strat1_t2_0_str, opponent_strat_str);

strat2_t0_5 = dlmread('kdom_exp-20180112-115628-rev-9542014-cpu-2.70GHz/kdom_exp_UCT_TR_C02_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
strat2_t2_0 = dlmread('kdom_exp-20180112-115628-rev-9542014-cpu-2.70GHz/kdom_exp_UCT_TR_C02_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat2_t0_5_str = "C=0.2 (0.5s)";
strat2_t2_0_str = "C=0.2 (2s)";
strats_2{1} = getStratCellArrayVersion4(strat2_t0_5, strat2_t0_5_str, opponent_strat_str);
strats_2{2} = getStratCellArrayVersion4(strat2_t2_0, strat2_t2_0_str, opponent_strat_str);


#----------------------------------
# Functions.
#

function score_diff_cell = getScoreDiffsCellArray(strats, strat_str)
  avg_score_diff = [];
  lower_err = [];
  upper_err = [];

  for i = 1:size(strats,2)
    score_diffs = strats{i}{12};
    avg_score_diff = [avg_score_diff  mean(score_diffs)];
    num_games = size(score_diffs, 1);
    # standard error over mean
    sem = std(score_diffs) / sqrt(num_games);
    # 95% confidence intervals
    lower_err = [lower_err 1.96 * sem];
    upper_err = [upper_err 1.96 * sem];
  endfor

  score_diff_cell = {strat_str, avg_score_diff, lower_err, upper_err};
endfunction

function plotScoreDiffs(score_diffs, num_games, x)
  figure();

  styles = {'r', 'g', 'b', 'k', 'm', 'c', 'y'};
  num_styles = length(styles);

  annotations = [];

  for i = 1:size(score_diffs,2)
    strat_str = score_diffs{i}{1};
    avg_score_diff = score_diffs{i}{2};
    lower_err = score_diffs{i}{3};
    upper_err = score_diffs{i}{4};

    errorbar(x, avg_score_diff, lower_err, upper_err, styles{mod(i, num_styles+1)});
    hold on;

    annotations = [annotations; strat_str];
  endfor

  title_str = sprintf("Score diff to best opponent (%d games)", num_games);
  title(title_str);

  legend(annotations);

  xlabel("Seconds per turn");
  ylabel("Score difference");
endfunction

function writeScoreDiffsToDatFile(score_diffs, output_dir, x)
  for i = 1:size(score_diffs,2)
    strat_str = strrep(score_diffs{i}{1}, "/", "_");
    avg_score_diffs = score_diffs{i}{2};
    lower_err = score_diffs{i}{3};
    upper_err = score_diffs{i}{4};

    filename = [output_dir "/SCORE_DIFFS_" strat_str ".dat"];

    err = lower_err + upper_err;

    data = [x; avg_score_diffs; err]';
    dlmwrite(filename, data, "delimiter", " ");
  endfor
endfunction


#----------------------------------
# Process experiment data.
#

score_diffs{1} = getScoreDiffsCellArray(strats_1, "C0.1");
score_diffs{2} = getScoreDiffsCellArray(strats_2, "C0.2");

plotScoreDiffs(score_diffs, num_games, time_limits);
writeScoreDiffsToDatFile(score_diffs, output_dir, time_limits);

