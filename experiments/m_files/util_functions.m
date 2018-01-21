1;

result_matrix_functions;


#===========================================================
# getStratCellArrayVersion4()
#-----------------------------------------------------------
#
function strat_cell = getStratCellArrayVersion4(strat_result, strat_str, opponent_str)

# usage: [STRAT_CELL] = getStratCellArrayVersion4 (STRAT_RESULT, STRAT_STR, OPPONENT_STR)
#
# Convert an experiment result matrix (output values from a set of
# played games) to a "strat" cell array which has the same information
# plus a string describing the player and a string describing the
# opponents, only a bit more structured.
#
#     STRAT_RESULT : Result matrix from experiment run. This is a Nx72 matrix with
#                    game result data where N is the number of games. See
#                    result_vector_component.m for more info.
#
#     STRAT_STR : String describing the player in the experiment.
#
#     OPPONENT_STR : String describing the opponent in the experiment.
#
#
# The structure of the STRAT_CELL cell array:
#
#   {1} Player String
#
#         A string describing the player
#
#         Example: "MCE-TR/R"
#
#   {2} Opponent String
#
#         A string describing the opponent
#
#         Example: "FG"
#
#   {3} Player Max Score
#
#         The player's maximum final score over all games
#
#   {4} Player Final Scores (Nx1)
#
#         A column vector containing the player's final scores for each game.
#
#   {5} Win-Draw-Loss 1/0/-1 Vector (Nx1)
#
#         A column vector containing 1 (=Win), 0 (=Draw), or -1 (=Loss) for
#         each game.
#
#   {6} Win-Draw-Loss Vector (1x3)
#
#         A 1x3 column vector with the player's total number of wins, draws,
#         and losses over all games.
#
#   {7} Available Moves Each Round (Nx13)
#
#         A Nx13 matrix with the number of available moves each round for each
#         game.
#
#   {8} Available Dominoes in Current Draft (Nx13)
#
#         A Nx13 matrix with the number of available dominoes in the current
#         draft.
#
#   {9} Position of Chosen Domino in Current Draft (Nx13)
#
#         A Nx13 matrix with the position of the chosen domino in the current
#         draft for each round in each game.
#
#   {10} Playouts Per Second (Nx13)
#
#          A Nx13 matrix with the number of playouts per second for each
#          round in each game.
#
#   {11} Player Round Scores (Nx13)
#
#          A Nx13 matrix with the player's score for each round in each
#          game.
#
#   {12} Score Difference (Nx1)
#
#          A Nx1 column vector with the player's final score minus the
#          score of the best opponent for each game.
#
#   {13} Opponent Scores (Nx3)
#
#          A Nx3 matrix with the opponents' scores for each game.
#

  strat_cell = {strat_str, ...                                        # 1:  "TRUE RANDOM"
                opponent_str, ...                                     # 2:  "BASE PLAYER"
                max(getScores(strat_result)), ...                     # 3:  max score
                getScores(strat_result), ...                          # 4:  all end scores
                getWinDrawLossVectorUnprocessed(strat_result), ...    # 5:  [1, 1, 0, -1, 0, -1, ...]
                getWinDrawLossVector(strat_result), ...               # 6:  [893, 1, 6]
                getBranchingFactors(strat_result), ...                # 7:  number of available moves each round
                getAvailableCurrentDraftDominoes(strat_result), ...   # 8:  number of available dominoes in current draft
                getChosenDominoPositions(strat_result), ...           # 9:  chosen domino position in current draft
                getPlayoutsPerSeconds(strat_result), ...              # 10: number of playouts per second (Monte-Carlo searches only)
                getPlayerScores(strat_result), ...                    # 11: player score for each round
                getScoreDiff(strat_result), ...                       # 12: difference in score between player and best opponent
                getOpponentScores(strat_result)};                     # 13: opponent scores
endfunction



#===========================================================
# getStratCellArrayVersion3() - DEPRECATED
#-----------------------------------------------------------
#
function strat_cell = getStratCellArrayVersion3(strat_result, strat_str, opponent_str)

# usage: [STRAT_CELL] = getStratCellArrayVersion4 (STRAT_RESULT, STRAT_STR, OPPONENT_STR)
#
#   *DEPRECATED*
#
# Convert an experiment result matrix (output values from a set of
# played games) to a "strat" cell array which has the same information
# plus a string describing the player and a string describing the
# opponents, only a bit more structured.
#
# Note! This is a subset of the strat cell array produced by
#       getStratCEllArrayVersion4() and should only be used
#       for converting results produced prior to version 4!
#
# See getStratCellArrayVersion4() for detailed information.
#

  strat_cell = {strat_str, ...                                        # 1:  "TRUE RANDOM"
                opponent_str, ...                                     # 2:  "BASE PLAYER"
                max(getScores(strat_result)), ...                     # 3:  max score
                getScores(strat_result), ...                          # 4:  all end scores
                getWinDrawLossVectorUnprocessed(strat_result), ...    # 5:  [1, 1, 0, -1, 0, -1, ...]
                getWinDrawLossVector(strat_result), ...               # 6:  [893, 1, 6]
                getBranchingFactors(strat_result), ...                # 7:  number of available moves each round
                getAvailableCurrentDraftDominoes(strat_result), ...   # 8:  number of available dominoes in current draft
                getChosenDominoPositions(strat_result), ...           # 9:  chosen domino position in current draft
                getPlayoutsPerSeconds(strat_result), ...              # 10: number of playouts per second (Monte-Carlo searches only)
                getPlayerScores(strat_result)};                       # 11: player score for each round
endfunction





#===========================================================
# getScoreDiffsCellArray()
#-----------------------------------------------------------
#
function score_diff_cell = getScoreDiffsCellArray(strats, strat_str)

# usage: [SCORE_DIFF_CELL] = getScoreDiffsCellArray (STRATS, STRAT_STR)
#
# Produce a cell array with score diff averages.
#
# The SCORE_DIFF_CELL cell array will be on the form:
#
#   {1} Strat string
#
#   {2} Score diff averages for each strat (1xM matrix, where M
#       is the number of strats)
#
#   {3} Lower error bound for each average (1xM matrix)
#
#   {4} Upper error bound for each average (1xM matrix)
#

  # Average score diff for each strat (1xM matrix, where M is
  # the number of strats).
  avg_score_diff = [];

  lower_err = [];
  upper_err = [];

  # Loop over all strats
  for i = 1:size(strats,2)

    # Score diff to best opponent for each game (Nx1 matrix)
    diffs = strats{i}{12};

    # Add average to list of averages.
    avg_score_diff = [avg_score_diff  mean(diffs)];

    # Number of games is equal to the number of rows in the score diffs matrix.
    num_games = size(diffs, 1);

    # Standard error over mean
    sem = std(diffs) / sqrt(num_games);

    # 95% confidence intervals
    lower_err = [lower_err 1.96 * sem];
    upper_err = [upper_err 1.96 * sem];
  endfor

  # Create cell array with score diffs information.
  #
  # The cell array will be on the form:
  #
  #   {1} Strat string
  #
  #   {2} Score diff averages for each strat (1xM matrix, where M
  #       is the number of strats)
  #
  #   {3} Lower error bound for each average (1xM matrix)
  #
  #   {4} Upper error bound for each average (1xM matrix)
  #
  score_diff_cell = {strat_str, avg_score_diff, lower_err, upper_err};
endfunction




#===========================================================
# getWinPercentagesCellArray()
#-----------------------------------------------------------
#
function win_percentages_cell = getWinPercentagesCellArray(strats, strat_str)

# usage: [WIN_PERCENTAGES_CELL] = getWinPercentagesCellArray (STRATS, STRAT_STR)
#
# Produce a cell array with the win percentages for all strats.
#
# The cell array has the following structure:
#
#   {1} Strat string
#
#   {2} Win percentages (1xM matrix, where M is the number of strats)
#

  percentages = [];

  # Loop over all strats
  for i = 1:size(strats,2)

    # Wins for current strat
    wins = strats{i}{6}(1);

    # Draws for current strat
    draws = strats{i}{6}(2);

    # Losses for current strat
    losses = strats{i}{6}(3);

    # Win percentage
    win_percentage = (wins / (wins + draws + losses)) * 100;

    # Add percentage to list of percentages
    percentages = [percentages  win_percentage];
  endfor

  # Construct win percentages cell array.
  #
  # The cell array has the following structure:
  #
  #   {1} Strat string
  #
  #   {2} Win percentages (1xM matrix, where M is the number of strats)
  #
  win_percentages_cell = {strat_str, percentages};
endfunction

