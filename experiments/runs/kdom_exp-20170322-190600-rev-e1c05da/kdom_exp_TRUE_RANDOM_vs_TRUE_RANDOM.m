#======================================================================================
# Kingdomino AI Experiment
#--------------------------------------------------------------------------------------
# 
# Player strategy:   TRUE_RANDOM
# Opponent strategy: TRUE_RANDOM
# 
# Runs: 40
# Output file: kdom_exp-20170322-190600-rev-e1c05da/kdom_exp_TRUE_RANDOM_vs_TRUE_RANDOM.m
# 
# Executing: 'java -jar out/kdom-exp.jar TRUE_RANDOM TRUE_RANDOM kdom_exp-20170322-190600-rev-e1c05da/kdom_exp_TRUE_RANDOM_vs_TRUE_RANDOM.m'
# Git Revision: e1c05da
#--------------------------------------------------------------------------------------

# win, score, num_players, available_moves(13), available_draft(13), chosen_draft_position(13), playouts_per_second(13), scores(13)
kdom_exp_TRUE_RANDOM_vs_TRUE_RANDOM = [
-1, 30, 4, 3, 24, 32, 64, 36, 24, 6, 27, 12, 8, 12, 2, 1, 3, 1, 2, 4, 2, 4, 3, 3, 1, 2, 3, 2, 0, 4, 3, 1, 3, 1, 2, 2, 4, 3, 2, 3, 1, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 13, 15, 16, 16, 18, 19, 15, 18, 26, 30, 30;
-1, 16, 4, 4, 96, 64, 10, 72, 9, 12, 4, 3, 4, 9, 1, 1, 4, 4, 4, 1, 3, 1, 3, 1, 3, 4, 3, 1, 0, 1, 1, 4, 2, 4, 2, 4, 2, 1, 2, 4, 4, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 10, 12, 2, 4, 5, 8, 13, 13, 15, 16, 16;
-1, 22, 4, 1, 96, 48, 8, 38, 51, 40, 18, 12, 12, 4, 4, 1, 1, 4, 3, 1, 2, 3, 4, 3, 3, 3, 4, 4, 0, 1, 2, 4, 3, 2, 1, 2, 2, 2, 1, 1, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 11, 13, 13, 17, 17, 22, 22, 22, 22, 22, 22;
-1, 20, 4, 4, 24, 72, 120, 24, 44, 10, 16, 32, 12, 4, 2, 1, 4, 1, 4, 4, 4, 4, 1, 4, 4, 3, 4, 1, 0, 4, 1, 1, 1, 1, 4, 1, 1, 2, 1, 4, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 12, 12, 12, 12, 15, 11, 11, 12, 17, 17, 20, 20;
-1, 21, 4, 3, 48, 66, 24, 26, 16, 68, 10, 28, 21, 6, 12, 1, 3, 2, 3, 3, 2, 1, 4, 1, 4, 3, 3, 3, 0, 3, 2, 2, 3, 4, 1, 4, 1, 2, 2, 2, 4, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 12, 12, 14, 7, 10, 14, 15, 15, 19, 21, 21;
-1, 22, 4, 3, 48, 42, 60, 54, 9, 16, 11, 56, 8, 3, 3, 1, 3, 2, 2, 3, 2, 1, 2, 1, 4, 4, 1, 3, 0, 3, 3, 2, 3, 4, 3, 4, 1, 1, 4, 2, 2, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 11, 2, 2, 4, 7, 12, 14, 18, 22, 22, 22;
-1, 18, 4, 3, 96, 62, 24, 14, 84, 60, 44, 7, 6, 1, 3, 1, 3, 4, 2, 2, 1, 3, 4, 4, 1, 3, 1, 1, 0, 1, 3, 3, 4, 2, 1, 1, 4, 2, 4, 4, 4, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 11, 13, 4, 4, 7, 8, 9, 12, 16, 18, 18;
1, 27, 4, 4, 72, 112, 8, 57, 5, 9, 13, 6, 16, 1, 4, 1, 4, 3, 4, 1, 3, 1, 1, 1, 3, 4, 1, 4, 0, 2, 1, 4, 2, 4, 4, 4, 2, 1, 4, 1, 2, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 11, 13, 14, 16, 9, 14, 19, 25, 27, 27, 27;
1, 25, 4, 3, 48, 16, 54, 30, 13, 44, 56, 6, 15, 2, 8, 1, 3, 2, 1, 3, 3, 1, 4, 4, 3, 3, 1, 4, 0, 3, 4, 2, 2, 4, 1, 1, 2, 2, 4, 1, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 12, 13, 13, 14, 15, 17, 18, 20, 23, 25, 25;
-1, 18, 4, 2, 72, 81, 10, 27, 42, 12, 6, 9, 4, 6, 4, 2, 2, 3, 3, 1, 3, 3, 3, 3, 1, 4, 2, 2, 0, 2, 2, 4, 2, 2, 2, 2, 4, 1, 3, 3, 2, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 11, 13, 14, 15, 17, 19, 15, 15, 16, 18, 18;
-1, 26, 4, 4, 72, 18, 40, 14, 64, 48, 18, 24, 2, 10, 4, 1, 4, 3, 1, 4, 1, 4, 3, 3, 2, 2, 2, 2, 0, 2, 4, 1, 4, 1, 2, 2, 3, 3, 3, 3, 4, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 12, 13, 16, 16, 17, 18, 18, 18, 24, 26, 26;
-1, 25, 4, 3, 72, 64, 29, 88, 16, 36, 4, 24, 10, 4, 6, 2, 3, 3, 4, 1, 4, 4, 3, 1, 4, 1, 4, 2, 0, 2, 1, 4, 1, 1, 2, 4, 1, 4, 1, 3, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 10, 11, 11, 11, 11, 13, 13, 17, 17, 23, 25;
-1, 17, 4, 3, 96, 93, 48, 23, 40, 40, 5, 5, 18, 4, 4, 2, 3, 4, 3, 2, 1, 4, 4, 1, 1, 3, 4, 4, 0, 1, 2, 3, 4, 1, 1, 4, 4, 2, 1, 1, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 0, 1, 3, 3, 6, 9, 11, 13, 13, 13, 17;
-1, 24, 4, 1, 48, 66, 24, 20, 20, 28, 30, 24, 10, 9, 4, 1, 1, 2, 3, 1, 1, 1, 2, 3, 3, 2, 3, 4, 0, 3, 2, 4, 4, 4, 3, 2, 2, 3, 2, 1, 4, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 11, 12, 13, 17, 8, 11, 15, 21, 24, 24, 24;
1, 29, 4, 1, 48, 64, 39, 38, 30, 56, 2, 5, 16, 2, 8, 2, 1, 2, 4, 3, 2, 3, 4, 1, 1, 4, 1, 2, 0, 3, 1, 2, 3, 2, 1, 4, 4, 1, 4, 3, 1, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 11, 12, 14, 17, 8, 10, 18, 19, 20, 22, 29;
-1, 11, 4, 1, 96, 36, 15, 42, 52, 33, 32, 1, 4, 16, 2, 1, 1, 4, 2, 1, 3, 4, 3, 4, 1, 4, 4, 2, 0, 1, 3, 4, 2, 1, 2, 1, 4, 1, 1, 3, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 11, 13, 14, 15, 6, 6, 10, 10, 11, 11, 11;
-1, 21, 4, 4, 72, 21, 42, 64, 56, 22, 8, 4, 8, 2, 1, 3, 4, 3, 1, 2, 4, 4, 2, 1, 4, 2, 2, 1, 0, 2, 4, 3, 1, 1, 3, 4, 1, 3, 3, 4, 4, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 12, 6, 8, 8, 11, 13, 13, 18, 18, 18, 21;
1, 26, 4, 1, 24, 36, 72, 42, 30, 18, 8, 24, 3, 4, 4, 1, 1, 1, 2, 4, 3, 3, 3, 4, 4, 3, 2, 2, 0, 4, 3, 1, 2, 2, 2, 1, 1, 2, 3, 3, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 12, 13, 13, 14, 15, 15, 15, 18, 18, 20, 24, 26;
1, 21, 4, 1, 24, 22, 39, 14, 11, 18, 6, 6, 12, 1, 2, 2, 1, 1, 1, 3, 1, 1, 2, 2, 1, 3, 1, 1, 0, 4, 4, 2, 4, 4, 3, 3, 4, 2, 4, 4, 1, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 12, 14, 15, 9, 10, 11, 14, 20, 20, 20, 21, 21;
-1, 22, 4, 1, 24, 36, 36, 26, 44, 21, 36, 4, 8, 12, 6, 1, 1, 1, 2, 3, 2, 4, 3, 3, 2, 4, 3, 3, 0, 4, 3, 2, 3, 1, 2, 2, 3, 1, 2, 2, 2, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 12, 13, 13, 14, 15, 16, 17, 20, 22, 20, 22, 22;
-1, 17, 4, 4, 96, 16, 10, 8, 20, 18, 1, 10, 32, 20, 16, 1, 4, 4, 1, 1, 1, 2, 2, 1, 2, 4, 4, 4, 0, 1, 4, 4, 4, 3, 3, 4, 3, 1, 1, 1, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 11, 12, 14, 14, 9, 12, 16, 16, 17, 17, 17;
-1, 18, 4, 2, 72, 64, 26, 13, 36, 26, 12, 18, 4, 8, 1, 1, 2, 3, 4, 2, 1, 3, 2, 3, 3, 2, 2, 1, 0, 2, 1, 3, 4, 2, 3, 2, 2, 3, 3, 4, 2, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 12, 13, 15, 15, 6, 7, 9, 11, 13, 18, 18;
1, 20, 4, 3, 24, 63, 72, 44, 36, 30, 28, 4, 24, 4, 8, 1, 3, 1, 3, 3, 2, 2, 2, 2, 2, 4, 2, 4, 0, 4, 2, 2, 3, 3, 3, 3, 3, 1, 3, 1, 4, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 13, 14, 16, 18, 19, 11, 14, 16, 18, 20, 20;
-1, 27, 4, 1, 24, 16, 42, 13, 36, 32, 16, 4, 18, 8, 12, 2, 1, 1, 1, 3, 1, 3, 4, 4, 4, 3, 2, 4, 0, 4, 4, 2, 4, 2, 1, 1, 1, 2, 3, 1, 1, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 12, 13, 14, 15, 16, 18, 20, 20, 22, 24, 25, 27;
-1, 17, 4, 1, 48, 66, 52, 64, 16, 10, 32, 24, 8, 6, 6, 1, 1, 2, 3, 4, 4, 2, 1, 4, 3, 4, 2, 2, 0, 3, 2, 1, 1, 3, 4, 1, 2, 1, 3, 3, 2, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 12, 3, 5, 7, 9, 9, 11, 11, 12, 17, 17;
1, 41, 4, 3, 48, 63, 80, 112, 33, 4, 1, 16, 18, 48, 12, 2, 3, 2, 3, 4, 4, 3, 4, 1, 2, 3, 4, 3, 0, 3, 2, 1, 1, 2, 1, 4, 3, 2, 1, 2, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 12, 12, 13, 12, 12, 12, 16, 19, 25, 37, 41;
-1, 14, 4, 3, 24, 66, 112, 30, 104, 38, 32, 2, 24, 4, 2, 1, 3, 1, 3, 4, 1, 4, 2, 4, 2, 4, 2, 2, 0, 4, 2, 1, 4, 1, 3, 1, 3, 1, 3, 3, 2, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 12, 2, 10, 10, 11, 11, 12, 12, 14, 14, 14;
-1, 35, 4, 2, 48, 81, 14, 56, 14, 36, 16, 4, 24, 8, 4, 2, 2, 2, 3, 1, 4, 1, 2, 2, 1, 3, 4, 4, 0, 3, 2, 4, 1, 4, 3, 3, 4, 2, 1, 1, 4, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 12, 13, 16, 18, 21, 22, 24, 25, 30, 32, 32, 35;
-1, 13, 4, 1, 72, 104, 48, 72, 40, 7, 36, 16, 4, 2, 4, 1, 1, 3, 4, 2, 3, 2, 1, 3, 4, 1, 2, 2, 0, 2, 1, 3, 2, 3, 4, 2, 1, 4, 3, 3, 1, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 10, 11, 11, 12, 5, 9, 9, 13, 13, 13, 13;
-1, 12, 4, 2, 72, 84, 24, 28, 11, 11, 42, 12, 8, 4, 4, 1, 2, 3, 4, 3, 2, 1, 1, 3, 2, 2, 4, 2, 0, 2, 1, 2, 3, 4, 4, 2, 3, 3, 1, 3, 1, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 10, 10, 11, 4, 6, 8, 9, 10, 10, 12, 12;
1, 31, 4, 2, 48, 32, 54, 33, 10, 11, 48, 9, 4, 1, 1, 1, 2, 2, 2, 3, 3, 1, 1, 4, 1, 1, 1, 1, 0, 3, 3, 2, 2, 4, 4, 1, 4, 4, 4, 4, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 12, 14, 15, 17, 19, 13, 16, 19, 31, 31, 31;
1, 29, 4, 2, 24, 64, 28, 64, 30, 36, 40, 21, 12, 6, 3, 1, 2, 1, 4, 2, 4, 3, 2, 4, 3, 2, 3, 3, 0, 4, 1, 3, 1, 2, 3, 1, 2, 3, 2, 2, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 11, 13, 15, 15, 18, 21, 22, 27, 27, 29, 29;
-1, 20, 4, 2, 24, 54, 40, 4, 10, 16, 21, 24, 2, 1, 1, 1, 2, 1, 3, 2, 1, 2, 2, 3, 4, 1, 1, 1, 0, 4, 2, 3, 4, 3, 3, 2, 1, 4, 4, 4, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 12, 13, 13, 15, 19, 11, 13, 13, 14, 20, 20, 20;
1, 25, 4, 4, 96, 18, 12, 54, 56, 40, 42, 1, 12, 3, 6, 2, 4, 4, 1, 1, 3, 4, 4, 3, 1, 3, 1, 2, 0, 1, 4, 4, 2, 1, 1, 2, 4, 2, 4, 3, 1, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 11, 13, 16, 10, 10, 14, 14, 20, 22, 25, 25;
-1, 12, 4, 3, 72, 42, 28, 30, 72, 36, 24, 16, 1, 20, 3, 2, 3, 3, 2, 2, 2, 4, 3, 4, 4, 1, 4, 3, 0, 2, 3, 3, 3, 1, 2, 1, 1, 4, 1, 2, 1, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 2, 2, 3, 3, 4, 4, 5, 5, 10, 10, 12;
1, 32, 4, 2, 72, 66, 25, 48, 38, 2, 3, 48, 40, 30, 24, 3, 2, 3, 3, 1, 2, 2, 2, 3, 3, 4, 3, 4, 0, 2, 2, 4, 3, 3, 3, 2, 2, 1, 2, 1, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 12, 14, 15, 16, 16, 16, 22, 22, 25, 25, 32;
-1, 18, 4, 3, 24, 54, 42, 60, 54, 12, 28, 2, 7, 6, 3, 1, 3, 1, 3, 3, 3, 3, 1, 4, 1, 1, 3, 3, 0, 4, 2, 2, 2, 2, 4, 1, 4, 4, 2, 2, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 11, 12, 13, 13, 14, 15, 18, 11, 16, 18, 18;
-1, 18, 4, 3, 24, 48, 88, 24, 20, 7, 16, 8, 24, 3, 1, 4, 3, 1, 3, 4, 2, 2, 1, 4, 2, 4, 3, 1, 0, 4, 2, 1, 3, 3, 4, 1, 3, 1, 2, 4, 4, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 11, 11, 11, 13, 18, 9, 11, 16, 16, 16, 16, 18;
-1, 26, 4, 1, 96, 64, 14, 39, 8, 36, 48, 7, 8, 8, 4, 1, 1, 4, 4, 1, 3, 4, 3, 4, 1, 2, 4, 4, 0, 1, 1, 4, 2, 1, 2, 1, 4, 3, 1, 1, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 11, 12, 13, 13, 16, 16, 20, 24, 26, 26, 26;
-1, 17, 4, 1, 96, 16, 14, 45, 10, 18, 9, 6, 15, 8, 4, 1, 1, 4, 1, 1, 3, 1, 2, 1, 3, 3, 4, 4, 0, 1, 4, 4, 2, 4, 3, 4, 2, 2, 1, 1, 3, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10, 10, 12, 3, 4, 7, 8, 10, 12, 17, 17, 17, 17];
