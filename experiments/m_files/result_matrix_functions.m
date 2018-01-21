# Prevent Octave from thinking that this
# is a function file:

1;


# The result matrix (called a 'strat') is a Nx72 matrix with result
# data from N played games.
#
# Each row represents the result data of one (1) game. The following
# table shows the description of each index in a row.
#
#   Index    Description
#   -----    -----------
#     1      Win (1), draw (0), or loss (-1)
#     2      Player final score
#     3      Number of players
#    4-16    Available moves each round
#    17-29   Available dominoes in current draft each round
#    30-42   Position of chosen domino in current draft each round
#    43-55   Playouts/s each round (search strategies only)
#    56-68   Player score each round
#     69     Difference in final score between player and best opponent
#    70-72   Final score for opponent 1-3 (default: -1)
#

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

function score_diff = getScoreDiff(strat_result)
  score_diff = strat_result(:, 69);
endfunction

function opponent_scores = getOpponentScores(strat_result)
  opponent_scores = strat_result(:, 70:72);
endfunction
