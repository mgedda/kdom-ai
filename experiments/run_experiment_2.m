clear all;
close all;

#
# Setup.
#

output_dir = "run_experiment_2.out";
output_file_prefix = "";

source("kdom_exp-20170326-005955-rev-fd90607/kdom_exp_MC_TR_TR_WDL_vs_FULL_GREEDY.m");
source("kdom_exp-20170326-190517-rev-fd90607/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY.m");

strat1_str = "MCE-TR/WDL";
strat2_str = "FG";

opponent_strat_str = "FG";

num_games = 450;

strat1 = kdom_exp_MC_TR_TR_WDL_vs_FULL_GREEDY(1:num_games,:);
strat2 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY(1:num_games,:);


mkdir(output_dir);


function scores = getScores(strat_result)
  scores = strat_result(:,2);
endfunction

function win_draw_loss = getWinDrawLossVectorUnprocessed(strat_result)
  win_draw_loss = strat_result(:,1);
endfunction

function wdl = getWinDrawLossVector(strat_result)
  win_draw_loss = getWinDrawLossVectorUnprocessed(strat_result);
  wins = size( win_draw_loss(win_draw_loss == 1), 1);
  draw = size( win_draw_loss(win_draw_loss == 0), 1);
  loss = size( win_draw_loss(win_draw_loss == -1), 1);
  wdl = [wins, draw, loss];
endfunction

function branching_factors = getBranchingFactors(strat_result)
  branching_factors = strat_result(:, 4:16);
endfunction

function available_current_draft_dominoes = getAvailableCurrentDraftDominoes(strat_result)
  available_current_draft_dominoes = strat_result(:, 17:29);
endfunction

function chosen_domino_positions = getChosenDominoPositions(strat_result)
  chosen_domino_positions = strat_result(:, 30:42);
endfunction

function playouts_per_seconds = getPlayoutsPerSeconds(strat_result)
  playouts_per_seconds = strat_result(:, 43:55);
endfunction

function player_scores = getPlayerScores(strat_result)
  player_scores = strat_result(:, 56:68);
endfunction

function strat_cell = getStratAsCellArray(strat_result, strat_str, opponent_str)
  strat_cell = {strat_str, ...                                        # "TRUE RANDOM"
		opponent_str, ...                                     # "BASE PLAYER"
		max(getScores(strat_result)), ...                     # max score
		getScores(strat_result), ...                          # all end scores
		getWinDrawLossVectorUnprocessed(strat_result), ...    # [1, 1, 0, -1, 0, -1, ...]
		getWinDrawLossVector(strat_result), ...               # [893, 1, 6]
		getBranchingFactors(strat_result), ...                # number of available moves each round
		getAvailableCurrentDraftDominoes(strat_result), ...   # number of available dominoes in current draft
		getChosenDominoPositions(strat_result), ...           # chosen domino position in current draft
		getPlayoutsPerSeconds(strat_result), ...              # number of playouts per second (Monte-Carlo searches only)
		getPlayerScores(strat_result)};                       # player score for each round
endfunction

strats{1} = getStratAsCellArray(strat1, strat1_str, opponent_strat_str);
strats{2} = getStratAsCellArray(strat2, strat2_str, opponent_strat_str);

#
# Hypothesis test.
#

pval = t_test_2(strats{1}{5}, strats{2}{5}, ">")




function max_score = getMaxScoreAllStrats(strats)

  max_scores = [];
  for i = 1:size(strats,2)
    max_scores = [max_scores strats{i}{3}];
  endfor
  max_score = max(max_scores);
endfunction


#
# Win/Loss/Draw pie charts.
#

function plotWinDrawLossPieCharts(strats)

  for i = 1:size(strats,2)
    figure();
    wdl = strats{i}{6};
    pie(wdl, {'win', 'draw', 'loss'});
    strat_str = strats{i}{1};
    opponent_str = strats{i}{2};
    num_games = size(strats{i}{4}, 1);
    title_str = sprintf("%s vs %s (%d games)\n\nWin (%d) / Draw (%d) / Loss (%d)", strat_str, opponent_str, num_games, wdl);
    title(title_str);
  endfor
endfunction


plotWinDrawLossPieCharts(strats);



#
# Score histograms.
#

function plotScoreHistograms(strats)

  for i = 1:size(strats,2)
    scores = strats{i}{4};

    max_score = getMaxScoreAllStrats(strats);
    x_max = max_score + 5;
    num_bins = 30;

    figure();
    #hist(scores, num_bins)
    [y, x] = hist(scores, num_bins);
    plot(x, y, '-b');
    strat_str = strats{i}{1};
    opponent_str = strats{i}{2};
    num_games = size(strats{i}{4}, 1);
    title_str = sprintf("Scores for %s vs %s (%d games)", strat_str, opponent_str, num_games);
    title(title_str);
    axis([0 x_max]);
  endfor
endfunction

function plotScoreHistogramsOverlap(strats)

  figure();

  styles = {'r', 'g', 'b', 'k', 'm', 'c', 'y'};
  num_styles = length(styles);

  annotations = [];

  for i = 1:size(strats,2)
    scores = strats{i}{4};

    num_games = size(strats{i}{4}, 1);
    num_bins = round(num_games / 40);

    #hist(scores, num_bins)
    [y, x] = hist(scores, num_bins);
    plot(x, y, styles{mod(i, num_styles+1)});
    hold on;
    strat_str = strats{i}{1};
    opponent_str = strats{i}{2};

    #annotations = [annotations; strcat(strat_str, " vs ", opponent_str)];
    annotations = [annotations; strat_str];
  endfor

  title_str = sprintf("Score Histograms (%d games)", num_games);
  title(title_str);

  legend(annotations);

  xlabel("Score");
  ylabel("Number of games");

  max_score = getMaxScoreAllStrats(strats);
  x_max = max_score + 5;
  axis([0 x_max]);
endfunction


#plotScoreHistograms(strats);
plotScoreHistogramsOverlap(strats);




#
# Player scores
#

function plotPlayerScoresOverlap(strats)

  figure();

  styles = {'r', 'g', 'b', 'k', 'm', 'c', 'y'};
  num_styles = length(styles);

  annotations = [];
  x = 1:1:13;

  max_mean_score = 0;

  for i = 1:size(strats,2)
    player_scores = strats{i}{11};
    player_mean_scores = mean(player_scores);
    max_mean_score = max(max_mean_score, max(player_mean_scores));

    num_games = size(strats{i}{11}, 1);

    # standard error over mean
    sem = std(player_scores) / sqrt(num_games);
    #lower_err = 0.5 * sem;
    #upper_err = 0.5 * sem;

    # 95% confidence intervals
    lower_err = 1.96 * sem;
    upper_err = 1.96 * sem;

    #plot(x, player_mean_scores, styles{mod(i, num_styles+1)});
    errorbar(x, player_mean_scores, lower_err, upper_err, styles{mod(i, num_styles+1)});
    hold on;
    strat_str = strats{i}{1};
    opponent_str = strats{i}{2};

    annotations = [annotations; strat_str];
  endfor

  title_str = sprintf("Scores per round (%d games)", num_games);
  title(title_str);

  legend(annotations);

  xlabel("Round");
  ylabel("Score");

  y_max = max_mean_score + 15;
  axis([0 14 0 y_max]);
endfunction

function writePlayerScoresToDatFile(strats, output_dir, output_file_prefix)

  x = 1:1:13;

  for i = 1:size(strats,2)
    player_scores = strats{i}{11};
    player_mean_scores = mean(player_scores);

    # standard error over mean                                                                                                                              
    num_games = size(strats{i}{11}, 1);
    sem = std(player_scores) / sqrt(num_games);

    # 95% confidence intervals                                                                                                                              
    lower_err = 1.96 * sem;
    upper_err = 1.96 * sem;

    #errorbar(x, player_mean_scores, lower_err, upper_err, styles{mod(i, num_styles+1)});                                                                   

    strat_str = strrep(strats{i}{1}, "/", "_");
    filename = [output_dir "/" output_file_prefix "SCORES_" strat_str ".dat"];

    err = lower_err + upper_err;

    data = [x; player_mean_scores; err]';
    dlmwrite(filename, data, "delimiter", " ");
  endfor
endfunction


plotPlayerScoresOverlap(strats);
writePlayerScoresToDatFile(strats, output_dir, output_file_prefix);



#
# Branching factors
#

function plotPerRoundValues(values, title_str)
  num_games = size(values, 1);
  num_rounds = size(values, 2);
  x = 1:num_rounds;
  y = mean(values);

  sem = std(values) / sqrt(num_games);
  #lower_err = 0.5 * sem;
  #upper_err = 0.5 * sem;
  lower_err = 1.96 * sem;    # 95% confidence interval
  upper_err = 1.96 * sem;    # 95% confidence interval

  figure();
  errorbar (x, y, lower_err, upper_err);
  #axis ([0, 14, 0, 75]);
  xlabel("Round");
  ylabel("");
  title(title_str);
endfunction

function title_str = getBranchingFactorPlotTitle(player_strat_str, opponent_strat_str, num_games)
  title_str = sprintf("Branching factors for %s vs %s (%d games)", player_strat_str, opponent_strat_str, num_games);
endfunction


function plotBranchingFactors(strats)

  for i = 1:size(strats,2)
    branching_factors = strats{i}{7};

    strat_str = strats{i}{1};
    opponent_str = strats{i}{2};
    num_games = size(strats{i}{4}, 1);
    title = getBranchingFactorPlotTitle(strat_str, opponent_str, num_games);

    plotPerRoundValues(branching_factors, title);
  endfor
endfunction

plotBranchingFactors(strats);

#
# Playouts per second
#

function plotPlayoutsPerSecondOverlap(strats)

  figure();

  styles = {'r', 'g', 'b', 'k', 'm', 'c', 'y'};
  num_styles = length(styles);

  annotations = [];
  x = 1:1:13;

  max_mean_playouts = 0;

  for i = 1:size(strats,2)
    playouts_per_second = strats{i}{10};
    mean_playouts_per_second = mean(playouts_per_second);
    max_mean_playouts = max(max_mean_playouts, max(mean_playouts_per_second));

    num_games = size(strats{i}{10}, 1);

    # standard error over mean
    sem = std(playouts_per_second) / sqrt(num_games);
    #lower_err = 0.5 * sem;
    #upper_err = 0.5 * sem;

    # 95% confidence intervals
    lower_err = 1.96 * sem;
    upper_err = 1.96 * sem;

    #plot(x, player_mean_scores, styles{mod(i, num_styles+1)});
    errorbar(x, mean_playouts_per_second, lower_err, upper_err, styles{mod(i, num_styles+1)});
    hold on;
    strat_str = strats{i}{1};
    opponent_str = strats{i}{2};

    annotations = [annotations; strat_str];
  endfor

  title_str = sprintf("Average playouts per seconds (%d games)", num_games);
  title(title_str);

  legend(annotations);

  xlabel("Round");
  ylabel("Playouts/s");

  y_max = round(max_mean_playouts * 1.1);
  axis([0 14 0 y_max]);
endfunction

function writePlayoutsToDatFile(strats, output_dir, output_file_prefix)

  x = 1:1:13;

  #for i = 1:size(strats,2)
  for i = 1:1
    playouts = strats{i}{10};
    mean_playouts = mean(playouts);

    # standard error over mean
    num_games = size(strats{i}{10}, 1);
    sem = std(playouts) / sqrt(num_games);

    # 95% confidence intervals
    lower_err = 1.96 * sem;
    upper_err = 1.96 * sem;

    #errorbar(x, player_mean_scores, lower_err, upper_err, styles{mod(i, num_styles+1)});


    strat_str = strrep(strats{i}{1}, "/", "_");
    filename = [output_dir "/" output_file_prefix "PLAYOUTS_" strat_str ".dat"];

    err = lower_err + upper_err;

    data = [x; mean_playouts; err]';
    dlmwrite(filename, data, "delimiter", " ");
  endfor
endfunction

plotPlayoutsPerSecondOverlap(strats);
writePlayoutsToDatFile(strats, output_dir, output_file_prefix);

return;

#
# Number of dominoes to chose from in chosen draft.
#

function title_str = getAvailableDraftPlotTitle(player_strat_str, opponent_strat_str, num_games)
  title_str = sprintf("Available dominoes for %s vs %s (%d games)", player_strat_str, opponent_strat_str, num_games);
endfunction

function plotAvailableDraftDominoes(strats)

  for i = 1:size(strats,2)
    available_dominoes = strats{i}{8};

    strat_str = strats{i}{1};
    opponent_str = strats{i}{2};
    num_games = size(strats{i}{4}, 1);
    title = getAvailableDraftPlotTitle(strat_str, opponent_str, num_games);

    plotPerRoundValues(available_dominoes, title);
  endfor
endfunction

plotAvailableDraftDominoes(strats);



#
# Chosen domino position.
#

function title_str = getChosenDominoPositionsPlotTitle(player_strat_str, opponent_strat_str, num_games)
  title_str = sprintf("Chosen domino position for %s vs %s (%d games)", player_strat_str, opponent_strat_str, num_games);
endfunction


function plotChosenDominoPositions(strats)

  for i = 1:size(strats,2)
    domino_positions = strats{i}{9};

    strat_str = strats{i}{1};
    opponent_str = strats{i}{2};
    num_games = size(strats{i}{4}, 1);
    title = getChosenDominoPositionsPlotTitle(strat_str, opponent_str, num_games);

    plotPerRoundValues(domino_positions, title);
  endfor
endfunction

plotChosenDominoPositions(strats);



