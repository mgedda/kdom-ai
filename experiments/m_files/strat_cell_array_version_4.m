1;

result_vector_components;

function strat_cell = getStratCellArrayVersion4(strat_result, strat_str, opponent_str)
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
                getPlayerScores(strat_result), ...                    # player score for each round
		getScoreDiff(strat_result), ...                       # difference in score between player and best opponent
		getOpponentScores(strat_result)};                     # opponent scores
endfunction
