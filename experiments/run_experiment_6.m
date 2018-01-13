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
time_limits = [0.5 2.0 5.0];        # time limits
c_values = [0.1 0.2 0.3 0.4 0.5 1.0 1.5 2.0];   # UCB constant values

strat1_t0_5 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_1_W0_0_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
strat1_t2_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_1_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat1_t5_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_1_W0_0_vs_FULL_GREEDY_T5_P0.dat', ' ', 19, 0);
strat2_t0_5 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_2_W0_0_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
strat2_t2_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_2_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat2_t5_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_2_W0_0_vs_FULL_GREEDY_T5_P0.dat', ' ', 19, 0);
strat3_t0_5 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_3_W0_0_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
strat3_t2_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_3_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat3_t5_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_3_W0_0_vs_FULL_GREEDY_T5_P0.dat', ' ', 19, 0);
strat4_t0_5 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_4_W0_0_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
strat4_t2_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_4_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat4_t5_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_4_W0_0_vs_FULL_GREEDY_T5_P0.dat', ' ', 19, 0);
strat5_t0_5 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_5_W0_0_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
strat5_t2_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_5_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat5_t5_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C0_5_W0_0_vs_FULL_GREEDY_T5_P0.dat', ' ', 19, 0);
strat6_t0_5 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C1_0_W0_0_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
strat6_t2_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C1_0_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat6_t5_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C1_0_W0_0_vs_FULL_GREEDY_T5_P0.dat', ' ', 19, 0);
strat7_t0_5 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C1_5_W0_0_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
strat7_t2_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C1_5_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat7_t5_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C1_5_W0_0_vs_FULL_GREEDY_T5_P0.dat', ' ', 19, 0);
strat8_t0_5 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C2_0_W0_0_vs_FULL_GREEDY_T0.5_P0.dat', ' ', 19, 0);
strat8_t2_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C2_0_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat8_t5_0 = dlmread('kdom_exp-20180112-210350-rev-1b74839-cpu-2.70GHz/kdom_exp_UCT_TR_C2_0_W0_0_vs_FULL_GREEDY_T5_P0.dat', ' ', 19, 0);

strat1_t0_5_str = "C=0.1 (0.5s)";
strat1_t2_0_str = "C=0.1 (2s)";
strat1_t5_0_str = "C=0.1 (5s)";
strat2_t0_5_str = "C=0.2 (0.5s)";
strat2_t2_0_str = "C=0.2 (2s)";
strat2_t5_0_str = "C=0.2 (5s)";
strat3_t0_5_str = "C=0.3 (0.5s)";
strat3_t2_0_str = "C=0.3 (2s)";
strat3_t5_0_str = "C=0.3 (5s)";
strat4_t0_5_str = "C=0.4 (0.5s)";
strat4_t2_0_str = "C=0.4 (2s)";
strat4_t5_0_str = "C=0.4 (5s)";
strat5_t0_5_str = "C=0.5 (0.5s)";
strat5_t2_0_str = "C=0.5 (2s)";
strat5_t5_0_str = "C=0.5 (5s)";
strat6_t0_5_str = "C=1.0 (0.5s)";
strat6_t2_0_str = "C=1.0 (2s)";
strat6_t5_0_str = "C=1.0 (5s)";
strat7_t0_5_str = "C=1.5 (0.5s)";
strat7_t2_0_str = "C=1.5 (2s)";
strat7_t5_0_str = "C=1.5 (5s)";
strat8_t0_5_str = "C=2.0 (0.5s)";
strat8_t2_0_str = "C=2.0 (2s)";
strat8_t5_0_str = "C=2.0 (5s)";

strats_1{1} = getStratCellArrayVersion4(strat1_t0_5, strat1_t0_5_str, opponent_strat_str);
strats_1{2} = getStratCellArrayVersion4(strat1_t2_0, strat1_t2_0_str, opponent_strat_str);
strats_1{3} = getStratCellArrayVersion4(strat1_t5_0, strat1_t5_0_str, opponent_strat_str);
strats_2{1} = getStratCellArrayVersion4(strat2_t0_5, strat2_t0_5_str, opponent_strat_str);
strats_2{2} = getStratCellArrayVersion4(strat2_t2_0, strat2_t2_0_str, opponent_strat_str);
strats_2{3} = getStratCellArrayVersion4(strat2_t5_0, strat2_t5_0_str, opponent_strat_str);
strats_3{1} = getStratCellArrayVersion4(strat3_t0_5, strat3_t0_5_str, opponent_strat_str);
strats_3{2} = getStratCellArrayVersion4(strat3_t2_0, strat3_t2_0_str, opponent_strat_str);
strats_3{3} = getStratCellArrayVersion4(strat3_t5_0, strat3_t5_0_str, opponent_strat_str);
strats_4{1} = getStratCellArrayVersion4(strat4_t0_5, strat4_t0_5_str, opponent_strat_str);
strats_4{2} = getStratCellArrayVersion4(strat4_t2_0, strat4_t2_0_str, opponent_strat_str);
strats_4{3} = getStratCellArrayVersion4(strat4_t5_0, strat4_t5_0_str, opponent_strat_str);
strats_5{1} = getStratCellArrayVersion4(strat5_t0_5, strat5_t0_5_str, opponent_strat_str);
strats_5{2} = getStratCellArrayVersion4(strat5_t2_0, strat5_t2_0_str, opponent_strat_str);
strats_5{3} = getStratCellArrayVersion4(strat5_t5_0, strat5_t5_0_str, opponent_strat_str);
strats_6{1} = getStratCellArrayVersion4(strat6_t0_5, strat6_t0_5_str, opponent_strat_str);
strats_6{2} = getStratCellArrayVersion4(strat6_t2_0, strat6_t2_0_str, opponent_strat_str);
strats_6{3} = getStratCellArrayVersion4(strat6_t5_0, strat6_t5_0_str, opponent_strat_str);
strats_7{1} = getStratCellArrayVersion4(strat7_t0_5, strat7_t0_5_str, opponent_strat_str);
strats_7{2} = getStratCellArrayVersion4(strat7_t2_0, strat7_t2_0_str, opponent_strat_str);
strats_7{3} = getStratCellArrayVersion4(strat7_t5_0, strat7_t5_0_str, opponent_strat_str);
strats_8{1} = getStratCellArrayVersion4(strat8_t0_5, strat8_t0_5_str, opponent_strat_str);
strats_8{2} = getStratCellArrayVersion4(strat8_t2_0, strat8_t2_0_str, opponent_strat_str);
strats_8{3} = getStratCellArrayVersion4(strat8_t5_0, strat8_t5_0_str, opponent_strat_str);


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


function writeScoreDiffsToDatFileInverted(score_diffs, time_values, x_values, output_dir)
  for i = 1:size(time_values,2)  # loop over files
    data = [];
    for j = 1:size(score_diffs,2)
      avg_score_diff = score_diffs{j}{2}(i);
      lower_err = score_diffs{j}{3}(i);
      upper_err = score_diffs{j}{4}(i);
      err = lower_err + upper_err;

      data = [data; x_values(j) avg_score_diff err];
    endfor

    t = time_values(i);
    filename = [output_dir "/SCORE_DIFFS_TIME_" num2str(t) "s.dat"];

    dlmwrite(filename, data, "delimiter", " ")
  endfor
endfunction


#----------------------------------
# Process experiment data.
#

score_diffs{1} = getScoreDiffsCellArray(strats_1, "C0.1");
score_diffs{2} = getScoreDiffsCellArray(strats_2, "C0.2");
score_diffs{3} = getScoreDiffsCellArray(strats_3, "C0.3");
score_diffs{4} = getScoreDiffsCellArray(strats_4, "C0.4");
score_diffs{5} = getScoreDiffsCellArray(strats_5, "C0.5");
score_diffs{6} = getScoreDiffsCellArray(strats_6, "C1.0");
score_diffs{7} = getScoreDiffsCellArray(strats_7, "C1.5");
score_diffs{8} = getScoreDiffsCellArray(strats_8, "C2.0");

#plotScoreDiffs(score_diffs, num_games, time_limits);
writeScoreDiffsToDatFile(score_diffs, output_dir, time_limits);

writeScoreDiffsToDatFileInverted(score_diffs, time_limits, c_values, output_dir);

