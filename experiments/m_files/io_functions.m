# Prevent Octave from thinking that this
# is a function file:

1;


#===========================================================
# writeBranchingFactorsToDatFile()
#-----------------------------------------------------------
#
function writeBranchingFactorsToDatFile(strats, output_path, output_file_prefix)

# usage: writeBranchingFactorsToDatFile (STRATS, OUTPUT_PATH, OUTPUT_FILE_PREFIX)
#
# Each filename will be on the form
#
#   <prefix>BRANCHING_FACTORS_<strat_str>.dat
#
#   Example:
#
#     # Round  Mean Branching Factors   Half confidence interval
#     1        2.1232                   0.9271
#     2        89.5273                  12.2012
#     3        77.1231                  9.1378
#     4        68.3414                  11.1220
#     ...
#

  # Rounds
  x = 1:1:13;

  # Loop over all strats
  for i = 1:size(strats,2)

    # Player branching factors, i.e., available moves each round (Nx13 matrix)
    branching_factors = strats{i}{7};

    # Mean branching factors each round (1x13 matrix)
    mean_branching_factors = mean(branching_factors);

    # Standard error over mean
    num_games = size(strats{i}{7}, 1);
    sem = std(branching_factors) / sqrt(num_games);

    # 95% confidence intervals
    lower_err = 1.96 * sem;
    upper_err = 1.96 * sem;

    # Replace "/" with "_" in strat string so we can use it in a filename.
    strat_str = strrep(strats{i}{1}, "/", "_");

    # Construct filename
    filename = [output_path "/" output_file_prefix "BRANCHING_FACTORS_" strat_str ".dat"];

    err = (lower_err + upper_err) / 2.0;

    # Compose data in file.
    # The data will be on the form
    #
    #    # Round  Mean Branching Factors   Half confidence interval
    #    1        2.1232                   0.9271
    #    2        89.5273                  12.2012
    #    3        77.1231                  9.1378
    #    4        68.3414                  11.1220
    #    ...
    #
    data = [x; mean_branching_factors; err]';

    # Write data to file
    dlmwrite(filename, data, "delimiter", " ");
  endfor
endfunction





#===========================================================
# writePlayerScoresToDatFile()
#-----------------------------------------------------------
#
function writePlayerScoresToDatFile(strats, output_dir, output_file_prefix)

# usage: writePlayerScoresToDatFile (STRATS, OUTPUT_DIR, OUTPUT_FILE_PREFIX)
#
# For each strat in STRATS, write the player mean score for each round to a
# whitespace separated .dat file.
#
# Each filename will be on the form
#
#   <prefix>SCORES_<strat_str>.dat
#
#   Example:
#
#     # Round  Player Mean Score   Half confidence interval
#     1        10                  0.0
#     2        10.5273             0.2012
#     3        13.1231             1.1220
#     4        19.3414             1.1220
#     ...
#

  # Rounds
  x = 1:1:13;

  # Loop over all strats
  for i = 1:size(strats,2)

    # Player round scores (Nx13 matrix).
    player_scores = strats{i}{11};

    # Mean score each round (1x13 matrix).
    player_mean_scores = mean(player_scores);

    # Number of games is equal to the number of rows in the player_scores matrix.
    num_games = size(strats{i}{11}, 1);

    # Standard error over mean
    sem = std(player_scores) / sqrt(num_games);

    # 95% confidence intervals
    lower_err = 1.96 * sem;
    upper_err = 1.96 * sem;

    # Replace "/" with "_" in strat string so we can use it in a filename.
    strat_str = strrep(strats{i}{1}, "/", "_");

    # Construct the filename
    filename = [output_dir "/" output_file_prefix "SCORES_" strat_str ".dat"];

    err = (lower_err + upper_err) / 2.0;

    # Compose data in file.
    # The data will be on the form
    #
    #    # Round  Mean Score   Half confidence interval
    #    1        10           0.0
    #    2        10.5273      0.2012
    #    3        13.1231      1.1220
    #    4        19.3414      1.1220
    #    ...
    #
    data = [x; player_mean_scores; err]';

    # Write data to file
    dlmwrite(filename, data, "delimiter", " ");
  endfor

endfunction




#===========================================================
# writeScoreDiffsToDatFile()
#-----------------------------------------------------------
#
function writeScoreDiffsToDatFile(score_diffs, output_path, x)

# usage: writeScoreDiffsToDatFile (SCORE_DIFFS, OUTPUT_PATH, X)
#
# Write score diff averages to a whitespace separated values .dat file.
#
#     SCORE_DIFFS : Cell array of score diff cell arrays. Each score diff cell
#                   array contains information on the strat string, score diff
#                   averages, and error bounds. See method
#                   getScoreDiffsCellArray() in util_functions.m for
#                   detailed information.
#
#     OUTPUT_PATH : Directory in which to save the .dat file.
#
#     X : The values in the first column in the .dat file. Typically time
#         limit (s) for player move.
#
# Each filename will be on the form
#
#   SCORE_DIFFS_<strat_str>.dat
#
#   Example:
#
#     # X      Score diff average   Half confidence interval
#     <x(1)>   -12.3252             6.1924
#     <x(2)>   -8.4285              7.2012
#     <x(3)>   -1.1231              9.1220
#     <x(4)>   6.3414               8.9927
#     ...
#

  # Loop over all score diff cell arrays
  for i = 1:size(score_diffs,2)

    # Replace "/" with "_" in strat string so we can use it in a filename.
    strat_str = strrep(score_diffs{i}{1}, "/", "_");

    # Extract score diff average (1xM matrix, where M is the number of
    # strats used when computing the average)
    avg_score_diffs = score_diffs{i}{2};

    lower_err = score_diffs{i}{3};
    upper_err = score_diffs{i}{4};

    # Construct filename
    filename = [output_path "/SCORE_DIFFS_" strat_str ".dat"];

    err = (lower_err + upper_err) / 2.0;

    data = [x; avg_score_diffs; err]';
    dlmwrite(filename, data, "delimiter", " ");
  endfor
endfunction




#===========================================================
# writeScoreDiffsToDatFileInverted()
#-----------------------------------------------------------
#
function writeScoreDiffsToDatFileInverted(score_diffs, time_values, x_values, output_path)

# usage: writeScoreDiffsToDatFileInverted (SCORE_DIFFS, TIME_VALUES, X_VALUES, OUTPUT_PATH))
#
# Write score diff averages to whitespace separated values .dat file.
#
# In contrast to writeScoreDiffsToDatFile(), which creates one file per strat, this
# method will create one file per time value. This is used when some variable needs
# to be examined (its values represented by X_VALUES) and the time limit is kept
# constant.
#
#     SCORE_DIFFS : Cell array of score diff cell arrays. Each score diff cell
#                   array contains information on the strat string, score diff
#                   averages, and error bounds. See method
#                   getScoreDiffsCellArray() in util_functions.m for
#                   detailed information.
#
#     TIME_VALUES : The time limits (1xM matrix). The length of this vector will
#                   determine the number of .dat files.
#
#     X_VALUES : The values in the first column in the .dat file. Typically
#                some constant we want to examine.
#
#     OUTPUT_PATH : Directory in which to save the .dat file.
#
# Each filename will be on the form
#
#   SCORE_DIFFS_TIME_<time_limit>s.dat
#
#   Example:
#
#     # X      Score diff average   Half confidence interval
#     <x(1)>   -12.3252             6.1924
#     <x(2)>   -8.4285              7.2012
#     <x(3)>   -1.1231              9.1220
#     <x(4)>   6.3414               8.9927
#     ...
#

  # Loop over all time values.
  for i = 1:size(time_values,2)

    # Container for file data
    data = [];

    # Loop over all score diff average cell arrays
    for j = 1:size(score_diffs,2)

      # Score diff average for current time_value
      avg_score_diff = score_diffs{j}{2}(i);

      # Error bounds
      lower_err = score_diffs{j}{3}(i);
      upper_err = score_diffs{j}{4}(i);
      err = (lower_err + upper_err) / 2.0;

      # Add row to data container
      data = [data; x_values(j) avg_score_diff err];
    endfor

    t = time_values(i);
    filename = [output_path "/SCORE_DIFFS_TIME_" num2str(t) "s.dat"];

    dlmwrite(filename, data, "delimiter", " ")
  endfor

endfunction



#===========================================================
# writeWinPercentagesToDatFileInverted()
#-----------------------------------------------------------
#
function writeWinPercentagesToDatFileInverted(win_percentages, time_values, x_values, output_path)

# usage: writeWinPercentagesToDatFileInverted (WIN_PERCENTAGES, TIME_VALUES, X_VALUES, OUTPUT_PATH)
#
# Write win percentages to whitespace separated values .dat file.
#
# This method will create one file per time value. This is used when some variable needs
# to be examined (its values represented by X_VALUES) and the time limit is kept
# constant.
#
#     WIN_PERCENTAGES : Cell array of win percentage cell arrays. Each win percentage
#                       cell array contains information on the strat string and win
#                       percentages. See method getWinPercentagesCellArray() in
#                       util_functions.m for detailed information.
#
#     TIME_VALUES : The time limits (1xM matrix). The length of this vector will
#                   determine the number of .dat files.
#
#     X_VALUES : The values in the first column in the .dat file. Typically
#                some constant we want to examine.
#
#     OUTPUT_PATH : Directory in which to save the .dat file.
#
# Each filename will be on the form
#
#   WIN_PERCENTAGES_TIME_<time_limit>s.dat
#
#   Example:
#
#     # X      Win percentage
#     <x(1)>   58.3252
#     <x(2)>   52.4285
#     <x(3)>   61.1231
#     <x(4)>   60.3414
#     ...

  # Loop over all time values
  for i = 1:size(time_values,2)

    # Container for file data
    data = [];

    # Loop over all win percentages
    for j = 1:size(win_percentages,2)

      # Extract win percentage for current time limit
      win_percentage = win_percentages{j}{2}(i);

      # Add row to data container
      data = [data; x_values(j) win_percentage];
    endfor

    t = time_values(i);
    filename = [output_path "/WIN_PERCENTAGES_TIME_" num2str(t) "s.dat"];

    dlmwrite(filename, data, "delimiter", " ")
  endfor

endfunction




#===========================================================
# writePlayoutsToDatFile()
#-----------------------------------------------------------
#
function writePlayoutsToDatFile(strats, output_dir, output_file_prefix)

# usage: writePlayoutsToDatFile (STRATS, OUTPUT_DIR, OUTPUT_FILE_PREFIX)
#
# For each strat in STRATS, write the player mean playout frequency for
# each round to a whitespace separated .dat file.
#
# Each filename will be on the form
#
#    <prefix>PLAYOUTS_<strat_str>.dat
#
#   Example:
#
#     # Round  Mean Playouts/s   Confidence interval
#     1        1231.1341         53.1314
#     2        2312.5273         61.2012
#     3        4372.1231         62.1220
#     4        7120.3414         71.0820
#     ...
#

  # Rounds
  x = 1:1:13;

  # Loop over all strats
  for i = 1:size(strats,2)

    # Playouts/s for player (Nx13 matrix)
    playouts = strats{i}{10};

    # Mean playouts/s for player (1x13 matrix)
    mean_playouts = mean(playouts);

    # Number of games is equal to the number of rows in the playouts/s matrix.
    num_games = size(strats{i}{10}, 1);

    # Standard error over mean
    sem = std(playouts) / sqrt(num_games);

    # 95% confidence intervals
    lower_err = 1.96 * sem;
    upper_err = 1.96 * sem;

    # Replace "/" with "_" in strat string so we can use it in a filename.
    strat_str = strrep(strats{i}{1}, "/", "_");

    # Construct the filename
    filename = [output_dir "/" output_file_prefix "PLAYOUTS_" strat_str ".dat"];

    err = lower_err + upper_err;

    # Compose data in file.
    # The data will be on the form
    #
    #     # Round  Mean Playouts/s   Confidence interval
    #     1        1231.1341         53.1314
    #     2        2312.5273         61.2012
    #     3        4372.1231         62.1220
    #     4        7120.3414         71.0820
    #     ...
    #
    data = [x; mean_playouts; err]';

    # Write  data to file.
    dlmwrite(filename, data, "delimiter", " ");
  endfor

endfunction



function p = toPercent(fraction)
  p = round(fraction * 1000) / 10;
endfunction

#===========================================================
# writePlayoutsToDatFile()
#-----------------------------------------------------------
#
function writeWinDrawLossToFile(strats, output_path, output_file_prefix)

# usage: writeWinDrawLossToFile (STRATS, OUTPUT_PATH, OUTPUT_FILE_PREFIX)
#
# For each strat in STRATS, write the win, draw, and loss values plus
# percentages to a whitespace separated .dat file.
#
# Each filename will be on the form
#
#    <prefix>WIN_DRAW_LOSS.dat
#
#   Example:
#
#     # Wins  Win(%)  Draws  Draw(%)  Losses Loss(%)
#     # TR
#     254     25.4    82     8.2      734    73.4
#     # GPRD
#     465     46.5    21     2.1      512    51.2
#     # FG
#     923     92.3    2      0.2      75     7.5
#     ...
#

  # Construct the filename
  filename = [output_path "/" output_file_prefix "WIN_DRAW_LOSS.dat"];

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
