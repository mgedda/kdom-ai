1;

result_vector_components;

function strat_cell = getStratCellArrayVersion4(strat_result, strat_str, opponent_str)
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
