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

num_games = 200;
time_limits = [2.0];        # time limits
c_values = [0.1 0.2 0.3 0.4 0.5];   # UCB constant values

strat1_t2_0 = dlmread('kdom_exp-20180113-151620-rev-08e3392-cpu-3.20GHz/kdom_exp_UCT_TR_C0_1_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat2_t2_0 = dlmread('kdom_exp-20180113-151620-rev-08e3392-cpu-3.20GHz/kdom_exp_UCT_TR_C0_2_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat3_t2_0 = dlmread('kdom_exp-20180113-151620-rev-08e3392-cpu-3.20GHz/kdom_exp_UCT_TR_C0_3_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat4_t2_0 = dlmread('kdom_exp-20180113-151620-rev-08e3392-cpu-3.20GHz/kdom_exp_UCT_TR_C0_4_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);
strat5_t2_0 = dlmread('kdom_exp-20180113-151620-rev-08e3392-cpu-3.20GHz/kdom_exp_UCT_TR_C0_5_W0_0_vs_FULL_GREEDY_T2_P0.dat', ' ', 19, 0);

strat1_t2_0_str = "C=0.1 (2s)";
strat2_t2_0_str = "C=0.2 (2s)";
strat3_t2_0_str = "C=0.3 (2s)";
strat4_t2_0_str = "C=0.4 (2s)";
strat5_t2_0_str = "C=0.5 (2s)";

strats_1{1} = getStratCellArrayVersion4(strat1_t2_0, strat1_t2_0_str, opponent_strat_str);
strats_2{1} = getStratCellArrayVersion4(strat2_t2_0, strat2_t2_0_str, opponent_strat_str);
strats_3{1} = getStratCellArrayVersion4(strat3_t2_0, strat3_t2_0_str, opponent_strat_str);
strats_4{1} = getStratCellArrayVersion4(strat4_t2_0, strat4_t2_0_str, opponent_strat_str);
strats_5{1} = getStratCellArrayVersion4(strat5_t2_0, strat5_t2_0_str, opponent_strat_str);


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

function win_percentages_cell = getWinPercentagesCellArray(strats, strat_str)
  win_percentages = [];

  for i = 1:size(strats,2)
    wins = strats{i}{6}(1);
    draws = strats{i}{6}(2);
    losses = strats{i}{6}(3);

    win_percentage = (wins/(wins + draws + losses))*100;
    win_percentages = [win_percentages  win_percentage];
  endfor

  win_percentages_cell = {strat_str, win_percentages};
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

function writeWinPercentagesToDatFileInverted(win_percentages, time_values, x_values, output_dir)
  for i = 1:size(time_values,2)  # loop over files
    data = [];
    for j = 1:size(win_percentages,2)
      win_percentage = win_percentages{j}{2}(i);
      data = [data; x_values(j) win_percentage];
    endfor

    t = time_values(i);
    filename = [output_dir "/WIN_PERCENTAGES_TIME_" num2str(t) "s.dat"];

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

#plotScoreDiffs(score_diffs, num_games, time_limits);
writeScoreDiffsToDatFile(score_diffs, output_dir, time_limits);
writeScoreDiffsToDatFileInverted(score_diffs, time_limits, c_values, output_dir);

win_percentages{1} = getWinPercentagesCellArray(strats_1, "C0.1");
win_percentages{2} = getWinPercentagesCellArray(strats_2, "C0.2");
win_percentages{3} = getWinPercentagesCellArray(strats_3, "C0.3");
win_percentages{4} = getWinPercentagesCellArray(strats_4, "C0.4");
win_percentages{5} = getWinPercentagesCellArray(strats_5, "C0.5");

writeWinPercentagesToDatFileInverted(win_percentages, time_limits, c_values, output_dir);
