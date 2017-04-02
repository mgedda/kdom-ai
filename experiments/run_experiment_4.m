clear all;
close all;

#
# Setup.
#

addpath("m_files");            # add project m files to path

strat_cell_array_version_4;    # load functions needed by this script
experiment_functions;


output_dir = "run_experiment_4.out";
output_file_prefix = "";

mkdir(output_dir);

opponent_strat_str = "FG";

num_games = 200;

source("kdom_exp-20170401-134843-rev-9365304-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T1_P0.m");
source("kdom_exp-20170401-134843-rev-9365304-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T2_P0.m");
source("kdom_exp-20170401-134843-rev-9365304-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T4_P0.m");
source("kdom_exp-20170401-134843-rev-9365304-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T6_P0.m");
source("kdom_exp-20170401-134843-rev-9365304-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T8_P0.m");
source("kdom_exp-20170401-134843-rev-9365304-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T10_P0.m");

strat1_t1_str = "MCE-FG/R (1s)";
strat1_t2_str = "MCE-FG/R (2s)";
strat1_t4_str = "MCE-FG/R (4s)";
strat1_t6_str = "MCE-FG/R (6s)";
strat1_t8_str = "MCE-FG/R (8s)";
strat1_t10_str = "MCE-FG/R (10s)";

strat1_t1 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T1_P0(1:num_games,:);
strat1_t2 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T2_P0(1:num_games,:);
strat1_t4 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T4_P0(1:num_games,:);
strat1_t6 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T6_P0(1:num_games,:);
strat1_t8 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T8_P0(1:num_games,:);
strat1_t10 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T10_P0(1:num_games,:);

strats_1{1} = getStratCellArrayVersion4(strat1_t1, strat1_t1_str, opponent_strat_str);
strats_1{2} = getStratCellArrayVersion4(strat1_t2, strat1_t2_str, opponent_strat_str);
strats_1{3} = getStratCellArrayVersion4(strat1_t4, strat1_t4_str, opponent_strat_str);
strats_1{4} = getStratCellArrayVersion4(strat1_t6, strat1_t6_str, opponent_strat_str);
strats_1{5} = getStratCellArrayVersion4(strat1_t8, strat1_t8_str, opponent_strat_str);
strats_1{6} = getStratCellArrayVersion4(strat1_t10, strat1_t10_str, opponent_strat_str);


source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T1_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T2_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T4_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T6_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T8_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T10_P0.m");

strat2_t1_str = "FG (1s)";
strat2_t2_str = "FG (2s)";
strat2_t4_str = "FG (4s)";
strat2_t6_str = "FG (6s)";
strat2_t8_str = "FG (8s)";
strat2_t10_str = "FG (10s)";

strat2_t1 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T1_P0(1:num_games,:);
strat2_t2 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T2_P0(1:num_games,:);
strat2_t4 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T4_P0(1:num_games,:);
strat2_t6 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T6_P0(1:num_games,:);
strat2_t8 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T8_P0(1:num_games,:);
strat2_t10 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T10_P0(1:num_games,:);

strats_2{1} = getStratCellArrayVersion4(strat2_t1, strat2_t1_str, opponent_strat_str);
strats_2{2} = getStratCellArrayVersion4(strat2_t2, strat2_t2_str, opponent_strat_str);
strats_2{3} = getStratCellArrayVersion4(strat2_t4, strat2_t4_str, opponent_strat_str);
strats_2{4} = getStratCellArrayVersion4(strat2_t6, strat2_t6_str, opponent_strat_str);
strats_2{5} = getStratCellArrayVersion4(strat2_t8, strat2_t8_str, opponent_strat_str);
strats_2{6} = getStratCellArrayVersion4(strat2_t10, strat2_t10_str, opponent_strat_str);



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

function plotScoreDiffs(score_diffs, num_games)
  figure();

  styles = {'r', 'g', 'b', 'k', 'm', 'c', 'y'};
  num_styles = length(styles);

  annotations = [];
  x = [1 2 4 6 8 10];

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

function writeScoreDiffsToDatFile(score_diffs, output_dir)
  x = [1 2 4 6 8 10];
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

score_diffs{1} = getScoreDiffsCellArray(strats_1, "MCE-FG/R");
score_diffs{2} = getScoreDiffsCellArray(strats_2, "FG");

plotScoreDiffs(score_diffs, num_games);
writeScoreDiffsToDatFile(score_diffs, output_dir);

return;


# Hypothesis test.
#
pval = t_test_2(strats{3}{5}, strats{2}{5}, ">")

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
writePlayerScoresToDatFile(strats, output_dir, output_file_prefix);

return;


# Branching factors
#
plotBranchingFactors(strats);

# Playouts per second
#
plotPlayoutsPerSecondOverlap(strats);
writePlayoutsToDatFile(strats, output_dir, output_file_prefix);

# Number of dominoes to chose from in chosen draft.
#
plotAvailableDraftDominoes(strats);

# Chosen domino position.
#
plotChosenDominoPositions(strats);



