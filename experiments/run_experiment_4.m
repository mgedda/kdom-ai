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

source("kdom_exp-20170403-000741-rev-44154d3-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T0.1_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T0.2_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T0.3_P0.m");
source("kdom_exp-20170403-000741-rev-44154d3-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T0.5_P0.m");
source("kdom_exp-20170401-134843-rev-9365304-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T1_P0.m");
source("kdom_exp-20170401-134843-rev-9365304-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T2_P0.m");
source("kdom_exp-20170401-134843-rev-9365304-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T4_P0.m");
source("kdom_exp-20170401-134843-rev-9365304-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T6_P0.m");
source("kdom_exp-20170401-134843-rev-9365304-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T8_P0.m");
source("kdom_exp-20170401-134843-rev-9365304-cpu-3.20GHz/kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T10_P0.m");

strat1_t0_1_str = "MCE-FG/R (0.1s)";
strat1_t0_2_str = "MCE-FG/R (0.2s)";
strat1_t0_3_str = "MCE-FG/R (0.3s)";
strat1_t0_5_str = "MCE-FG/R (0.5s)";
strat1_t1_str = "MCE-FG/R (1s)";
strat1_t2_str = "MCE-FG/R (2s)";
strat1_t4_str = "MCE-FG/R (4s)";
strat1_t6_str = "MCE-FG/R (6s)";
strat1_t8_str = "MCE-FG/R (8s)";
strat1_t10_str = "MCE-FG/R (10s)";

strat1_t0_1 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T0_1_P0(1:num_games,:);
strat1_t0_2 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T0_2_P0(1:num_games,:);
strat1_t0_3 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T0_3_P0(1:num_games,:);
strat1_t0_5 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T0_5_P0(1:num_games,:);
strat1_t1 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T1_P0(1:num_games,:);
strat1_t2 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T2_P0(1:num_games,:);
strat1_t4 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T4_P0(1:num_games,:);
strat1_t6 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T6_P0(1:num_games,:);
strat1_t8 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T8_P0(1:num_games,:);
strat1_t10 = kdom_exp_MCE_FG_R_vs_FULL_GREEDY_T10_P0(1:num_games,:);

strats_1{1} = getStratCellArrayVersion4(strat1_t0_1, strat1_t0_1_str, opponent_strat_str);
strats_1{2} = getStratCellArrayVersion4(strat1_t0_2, strat1_t0_2_str, opponent_strat_str);
strats_1{3} = getStratCellArrayVersion4(strat1_t0_3, strat1_t0_3_str, opponent_strat_str);
strats_1{4} = getStratCellArrayVersion4(strat1_t0_5, strat1_t0_5_str, opponent_strat_str);
strats_1{5} = getStratCellArrayVersion4(strat1_t1, strat1_t1_str, opponent_strat_str);
strats_1{6} = getStratCellArrayVersion4(strat1_t2, strat1_t2_str, opponent_strat_str);
strats_1{7} = getStratCellArrayVersion4(strat1_t4, strat1_t4_str, opponent_strat_str);
strats_1{8} = getStratCellArrayVersion4(strat1_t6, strat1_t6_str, opponent_strat_str);
strats_1{9} = getStratCellArrayVersion4(strat1_t8, strat1_t8_str, opponent_strat_str);
strats_1{10} = getStratCellArrayVersion4(strat1_t10, strat1_t10_str, opponent_strat_str);


source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T0.1_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T0.2_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T0.3_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T0.5_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T1_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T2_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T4_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T6_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T8_P0.m");
source("kdom_exp-20170402-220909-rev-6056b83-cpu-2.70GHz/kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T10_P0.m");

strat2_t0_1_str = "FG (0.1s)";
strat2_t0_2_str = "FG (0.2s)";
strat2_t0_3_str = "FG (0.3s)";
strat2_t0_5_str = "FG (0.5s)";
strat2_t1_str = "FG (1s)";
strat2_t2_str = "FG (2s)";
strat2_t4_str = "FG (4s)";
strat2_t6_str = "FG (6s)";
strat2_t8_str = "FG (8s)";
strat2_t10_str = "FG (10s)";

strat2_t0_1 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T0_1_P0(1:num_games,:);
strat2_t0_2 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T0_1_P0(1:num_games,:);
strat2_t0_3 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T0_1_P0(1:num_games,:);
strat2_t0_5 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T0_5_P0(1:num_games,:);
strat2_t1 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T1_P0(1:num_games,:);
strat2_t2 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T2_P0(1:num_games,:);
strat2_t4 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T4_P0(1:num_games,:);
strat2_t6 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T6_P0(1:num_games,:);
strat2_t8 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T8_P0(1:num_games,:);
strat2_t10 = kdom_exp_FULL_GREEDY_vs_FULL_GREEDY_T10_P0(1:num_games,:);

strats_2{1} = getStratCellArrayVersion4(strat2_t0_1, strat2_t0_1_str, opponent_strat_str);
strats_2{2} = getStratCellArrayVersion4(strat2_t0_2, strat2_t0_2_str, opponent_strat_str);
strats_2{3} = getStratCellArrayVersion4(strat2_t0_3, strat2_t0_3_str, opponent_strat_str);
strats_2{4} = getStratCellArrayVersion4(strat2_t0_5, strat2_t0_5_str, opponent_strat_str);
strats_2{5} = getStratCellArrayVersion4(strat2_t1, strat2_t1_str, opponent_strat_str);
strats_2{6} = getStratCellArrayVersion4(strat2_t2, strat2_t2_str, opponent_strat_str);
strats_2{7} = getStratCellArrayVersion4(strat2_t4, strat2_t4_str, opponent_strat_str);
strats_2{8} = getStratCellArrayVersion4(strat2_t6, strat2_t6_str, opponent_strat_str);
strats_2{9} = getStratCellArrayVersion4(strat2_t8, strat2_t8_str, opponent_strat_str);
strats_2{10} = getStratCellArrayVersion4(strat2_t10, strat2_t10_str, opponent_strat_str);

source("kdom_exp-20170403-000741-rev-44154d3-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T0.1_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T0.2_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T0.3_P0.m");
source("kdom_exp-20170403-000741-rev-44154d3-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T0.5_P0.m");
source("kdom_exp-20170403-000741-rev-44154d3-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T1_P0.m");
source("kdom_exp-20170403-000741-rev-44154d3-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T2_P0.m");
source("kdom_exp-20170403-000741-rev-44154d3-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T4_P0.m");
source("kdom_exp-20170403-000741-rev-44154d3-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T6_P0.m");
source("kdom_exp-20170403-000741-rev-44154d3-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T8_P0.m");
source("kdom_exp-20170403-000741-rev-44154d3-cpu-3.20GHz/kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T10_P0.m");

strat3_t0_1_str = "MCE-TR/R (0.1s)";
strat3_t0_2_str = "MCE-TR/R (0.2s)";
strat3_t0_3_str = "MCE-TR/R (0.3s)";
strat3_t0_5_str = "MCE-TR/R (0.5s)";
strat3_t1_str = "MCE-TR/R (1s)";
strat3_t2_str = "MCE-TR/R (2s)";
strat3_t4_str = "MCE-TR/R (4s)";
strat3_t6_str = "MCE-TR/R (6s)";
strat3_t8_str = "MCE-TR/R (8s)";
strat3_t10_str = "MCE-TR/R (10s)";

strat3_t0_1 = kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T0_1_P0(1:num_games,:);
strat3_t0_2 = kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T0_2_P0(1:num_games,:);
strat3_t0_3 = kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T0_3_P0(1:num_games,:);
strat3_t0_5 = kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T0_5_P0(1:num_games,:);
strat3_t1 = kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T1_P0(1:num_games,:);
strat3_t2 = kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T2_P0(1:num_games,:);
strat3_t4 = kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T4_P0(1:num_games,:);
strat3_t6 = kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T6_P0(1:num_games,:);
strat3_t8 = kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T8_P0(1:num_games,:);
strat3_t10 = kdom_exp_MCE_TR_R_vs_FULL_GREEDY_T10_P0(1:num_games,:);

strats_3{1} = getStratCellArrayVersion4(strat3_t0_1, strat3_t0_1_str, opponent_strat_str);
strats_3{2} = getStratCellArrayVersion4(strat3_t0_2, strat3_t0_2_str, opponent_strat_str);
strats_3{3} = getStratCellArrayVersion4(strat3_t0_3, strat3_t0_3_str, opponent_strat_str);
strats_3{4} = getStratCellArrayVersion4(strat3_t0_5, strat3_t0_5_str, opponent_strat_str);
strats_3{5} = getStratCellArrayVersion4(strat3_t1, strat3_t1_str, opponent_strat_str);
strats_3{6} = getStratCellArrayVersion4(strat3_t2, strat3_t2_str, opponent_strat_str);
strats_3{7} = getStratCellArrayVersion4(strat3_t4, strat3_t4_str, opponent_strat_str);
strats_3{8} = getStratCellArrayVersion4(strat3_t6, strat3_t6_str, opponent_strat_str);
strats_3{9} = getStratCellArrayVersion4(strat3_t8, strat3_t8_str, opponent_strat_str);
strats_3{10} = getStratCellArrayVersion4(strat3_t10, strat3_t10_str, opponent_strat_str);

source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T0.1_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T0.2_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T0.3_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T0.5_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T1_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T2_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T4_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T6_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T8_P0.m");
source("kdom_exp-20170404-215341-rev-66b041f-cpu-3.20GHz/kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T10_P0.m");

strat4_t0_1_str = "MCE-EG/R (0.1s)";
strat4_t0_2_str = "MCE-EG/R (0.2s)";
strat4_t0_3_str = "MCE-EG/R (0.3s)";
strat4_t0_5_str = "MCE-EG/R (0.5s)";
strat4_t1_str = "MCE-EG/R (1s)";
strat4_t2_str = "MCE-EG/R (2s)";
strat4_t4_str = "MCE-EG/R (4s)";
strat4_t6_str = "MCE-EG/R (6s)";
strat4_t8_str = "MCE-EG/R (8s)";
strat4_t10_str = "MCE-EG/R (10s)";

strat4_t0_1 = kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T0_1_P0(1:num_games,:);
strat4_t0_2 = kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T0_2_P0(1:num_games,:);
strat4_t0_3 = kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T0_3_P0(1:num_games,:);
strat4_t0_5 = kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T0_5_P0(1:num_games,:);
strat4_t1 = kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T1_P0(1:num_games,:);
strat4_t2 = kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T2_P0(1:num_games,:);
strat4_t4 = kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T4_P0(1:num_games,:);
strat4_t6 = kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T6_P0(1:num_games,:);
strat4_t8 = kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T8_P0(1:num_games,:);
strat4_t10 = kdom_exp_MCE_EG_R_vs_FULL_GREEDY_T10_P0(1:num_games,:);

strats_4{1} = getStratCellArrayVersion4(strat4_t0_1, strat4_t0_1_str, opponent_strat_str);
strats_4{2} = getStratCellArrayVersion4(strat4_t0_2, strat4_t0_2_str, opponent_strat_str);
strats_4{3} = getStratCellArrayVersion4(strat4_t0_3, strat4_t0_3_str, opponent_strat_str);
strats_4{4} = getStratCellArrayVersion4(strat4_t0_5, strat4_t0_5_str, opponent_strat_str);
strats_4{5} = getStratCellArrayVersion4(strat4_t1, strat4_t1_str, opponent_strat_str);
strats_4{6} = getStratCellArrayVersion4(strat4_t2, strat4_t2_str, opponent_strat_str);
strats_4{7} = getStratCellArrayVersion4(strat4_t4, strat4_t4_str, opponent_strat_str);
strats_4{8} = getStratCellArrayVersion4(strat4_t6, strat4_t6_str, opponent_strat_str);
strats_4{9} = getStratCellArrayVersion4(strat4_t8, strat4_t8_str, opponent_strat_str);
strats_4{10} = getStratCellArrayVersion4(strat4_t10, strat4_t10_str, opponent_strat_str);


source("kdom_exp-20170410-131854-rev-13d6a7c-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T0.1_P0.m");
source("kdom_exp-20170410-131854-rev-13d6a7c-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T0.2_P0.m");
source("kdom_exp-20170410-131854-rev-13d6a7c-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T0.3_P0.m");
source("kdom_exp-20170410-131854-rev-13d6a7c-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T0.5_P0.m");
source("kdom_exp-20170410-131854-rev-13d6a7c-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T1_P0.m");
source("kdom_exp-20170410-131854-rev-13d6a7c-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T2_P0.m");
source("kdom_exp-20170410-131854-rev-13d6a7c-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T4_P0.m");
source("kdom_exp-20170410-131854-rev-13d6a7c-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T6_P0.m");
source("kdom_exp-20170410-131854-rev-13d6a7c-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T8_P0.m");
source("kdom_exp-20170410-131854-rev-13d6a7c-cpu-3.20GHz/kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T10_P0.m");

strat5_t0_1_str = "MCE-PG/R (0.1s)";
strat5_t0_2_str = "MCE-PG/R (0.2s)";
strat5_t0_3_str = "MCE-PG/R (0.3s)";
strat5_t0_5_str = "MCE-PG/R (0.5s)";
strat5_t1_str = "MCE-PG/R (1s)";
strat5_t2_str = "MCE-PG/R (2s)";
strat5_t4_str = "MCE-PG/R (4s)";
strat5_t6_str = "MCE-PG/R (6s)";
strat5_t8_str = "MCE-PG/R (8s)";
strat5_t10_str = "MCE-PG/R (10s)";

strat5_t0_1 = kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T0_1_P0(1:num_games,:);
strat5_t0_2 = kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T0_2_P0(1:num_games,:);
strat5_t0_3 = kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T0_3_P0(1:num_games,:);
strat5_t0_5 = kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T0_5_P0(1:num_games,:);
strat5_t1 = kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T1_P0(1:num_games,:);
strat5_t2 = kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T2_P0(1:num_games,:);
strat5_t4 = kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T4_P0(1:num_games,:);
strat5_t6 = kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T6_P0(1:num_games,:);
strat5_t8 = kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T8_P0(1:num_games,:);
strat5_t10 = kdom_exp_MCE_PG_R_vs_FULL_GREEDY_T10_P0(1:num_games,:);

strats_5{1} = getStratCellArrayVersion4(strat5_t0_1, strat5_t0_1_str, opponent_strat_str);
strats_5{2} = getStratCellArrayVersion4(strat5_t0_2, strat5_t0_2_str, opponent_strat_str);
strats_5{3} = getStratCellArrayVersion4(strat5_t0_3, strat5_t0_3_str, opponent_strat_str);
strats_5{4} = getStratCellArrayVersion4(strat5_t0_5, strat5_t0_5_str, opponent_strat_str);
strats_5{5} = getStratCellArrayVersion4(strat5_t1, strat5_t1_str, opponent_strat_str);
strats_5{6} = getStratCellArrayVersion4(strat5_t2, strat5_t2_str, opponent_strat_str);
strats_5{7} = getStratCellArrayVersion4(strat5_t4, strat5_t4_str, opponent_strat_str);
strats_5{8} = getStratCellArrayVersion4(strat5_t6, strat5_t6_str, opponent_strat_str);
strats_5{9} = getStratCellArrayVersion4(strat5_t8, strat5_t8_str, opponent_strat_str);
strats_5{10} = getStratCellArrayVersion4(strat5_t10, strat5_t10_str, opponent_strat_str);


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
  x = [0.1 0.2 0.3 0.5 1 2 4 6 8 10];

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
  x = [0.1 0.2 0.3 0.5 1 2 4 6 8 10];
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

score_diffs{3} = getScoreDiffsCellArray(strats_1, "MCE-FG/R");
score_diffs{1} = getScoreDiffsCellArray(strats_2, "FG");
score_diffs{2} = getScoreDiffsCellArray(strats_3, "MCE-TR/R");
score_diffs{4} = getScoreDiffsCellArray(strats_4, "MCE-EG/R");
score_diffs{5} = getScoreDiffsCellArray(strats_5, "MCE-PG/R");

plotScoreDiffs(score_diffs, num_games);
writeScoreDiffsToDatFile(score_diffs, output_dir);


# Playouts per second
#

playout_strats{1} = strats_1{10};
playout_strats{2} = strats_3{10};
playout_strats{3} = strats_4{10};
playout_strats{4} = strats_5{10};

plotPlayoutsPerSecondOverlap(playout_strats);
writePlayoutsToDatFile(playout_strats, output_dir, output_file_prefix);

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


# Number of dominoes to chose from in chosen draft.
#
plotAvailableDraftDominoes(strats);

# Chosen domino position.
#
plotChosenDominoPositions(strats);



