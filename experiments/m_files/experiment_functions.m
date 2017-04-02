# Prevent Octave from thinking that this
# is a function file:

1;


#
# Maximum score
#
function max_score = getMaxScoreAllStrats(strats)
  max_scores = [];
  for i = 1:size(strats,2)
    max_scores = [max_scores strats{i}{3}];
  endfor
  max_score = max(max_scores);
endfunction

#
# Win/draw/loss pie charts
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

    [y, x] = hist(scores, num_bins);
    plot(x, y, styles{mod(i, num_styles+1)});
    hold on;
    strat_str = strats{i}{1};
    opponent_str = strats{i}{2};

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
    # 95% confidence intervals
    lower_err = 1.96 * sem;
    upper_err = 1.96 * sem;

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

    strat_str = strrep(strats{i}{1}, "/", "_");
    filename = [output_dir "/" output_file_prefix "SCORES_" strat_str ".dat"];

    err = lower_err + upper_err;

    data = [x; player_mean_scores; err]';
    dlmwrite(filename, data, "delimiter", " ");
  endfor
endfunction

#
# Branching factors
#
function plotPerRoundValues(values, title_str)
  num_games = size(values, 1);
  num_rounds = size(values, 2);
  x = 1:num_rounds;
  y = mean(values);

  sem = std(values) / sqrt(num_games);
  lower_err = 1.96 * sem;    # 95% confidence interval
  upper_err = 1.96 * sem;    # 95% confidence interval

  figure();
  errorbar (x, y, lower_err, upper_err);
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
    # 95% confidence intervals
    lower_err = 1.96 * sem;
    upper_err = 1.96 * sem;

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

  for i = 1:size(strats,2)
    playouts = strats{i}{10};
    mean_playouts = mean(playouts);

    # standard error over mean
    num_games = size(strats{i}{10}, 1);
    sem = std(playouts) / sqrt(num_games);

    # 95% confidence intervals
    lower_err = 1.96 * sem;
    upper_err = 1.96 * sem;

    strat_str = strrep(strats{i}{1}, "/", "_");
    filename = [output_dir "/" output_file_prefix "PLAYOUTS_" strat_str ".dat"];

    err = lower_err + upper_err;

    data = [x; mean_playouts; err]';
    dlmwrite(filename, data, "delimiter", " ");
  endfor
endfunction

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

