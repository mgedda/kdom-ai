#!/bin/bash

set -e

#
# Setup
#

DATE_DAY=`date +%Y%m%d`
DATE_TIME=`date +%H%M%S`
DATE="$DATE_DAY-$DATE_TIME"

SYSTEM=`uname -s`

if [ "$SYSTEM" == "Darwin" ]; then
    # We are on a Mac
    SYSTEM=Mac
    #echo `sysctl -n machdep.cpu.brand_string`
    CPU_SPEED=`sysctl -n machdep.cpu.brand_string | awk '/GHz/{print $1}' RS=@`
elif [ "$SYSTEM" == "Linux" ]; then
    # We are on Linux
    #echo "Linux"
    CPU_SPEED=`cat /proc/cpuinfo | awk '/GHz/{print $1}' RS=@ | head -1`
else
  echo "Unknown system!"
  exit 0
fi

#CPU=${CPU_SPEED/GHz/""}

REVISION=$(eval "git rev-parse --short HEAD")
TARGET_DIR="kdom_exp-${DATE}-rev-${REVISION}-cpu-${CPU_SPEED}"
mkdir $TARGET_DIR


# Print string to log file
#
# Args:
#
#    $1 - String to print
#    $2 - Log file
#
function print
{
    echo $1 | tee -a $2
}


# Play n games
#
# Args:
#
#    $1 - Number of games
#    $2 - Player game strategy
#    $3 - Opponent game strategy
#    $4 - Maximum time per turn  (only for search strategies) (<=0: deactivated)
#    $5 - Maximum number of playouts  (only for search strategies) (<=0: deactivated)
#
#    $6 - Target directory
#    $7 - System (Mac/Linux)
#    $8 - CPU speed
#    $9 - Git revision
#
function playGames
{
    NUM_RUNS=$1
    PLAYER_STRATEGY=$2
    OPPONENT_STRATEGY=$3
    MAX_SEARCH_TIME=$4
    MAX_PLAYOUTS=$5
    TARGET_DIR=$6
    SYSTEM=$7
    CPU_SPEED=$8
    REVISION=$9

    MAX_SEARCH_TIME_ESCAPED=${MAX_SEARCH_TIME//[.]/_}

    OUTPUT_FILE="${TARGET_DIR}/kdom_exp_${PLAYER_STRATEGY}_vs_${OPPONENT_STRATEGY}_G${NUM_RUNS}_T${MAX_SEARCH_TIME}_P${MAX_PLAYOUTS}.dat"
    OUTPUT_FILE_TMP="${TARGET_DIR}/kdom_exp_${PLAYER_STRATEGY}_vs_${OPPONENT_STRATEGY}_G${NUM_RUNS}_T${MAX_SEARCH_TIME}_P${MAX_PLAYOUTS}.tmp"
    LOG_FILE="${TARGET_DIR}/kdom_exp_${PLAYER_STRATEGY}_vs_${OPPONENT_STRATEGY}_G${NUM_RUNS}_T${MAX_SEARCH_TIME}_P${MAX_PLAYOUTS}.log"

    SCRIPT_STR="java -jar out/kdom-exp.jar ${PLAYER_STRATEGY} ${OPPONENT_STRATEGY} ${MAX_SEARCH_TIME} ${MAX_PLAYOUTS} ${OUTPUT_FILE_TMP}"

    print "=======================================================================================" $LOG_FILE
    print "| Kingdomino AI Experiment"                                                              $LOG_FILE
    print "|--------------------------------------------------------------------------------------" $LOG_FILE
    print "| "                                                                                      $LOG_FILE
    print "| Player strategy:   $PLAYER_STRATEGY"                                                   $LOG_FILE
    print "| Opponent strategy: $OPPONENT_STRATEGY"                                                 $LOG_FILE
    print "| "                                                                                      $LOG_FILE
    print "| Max search time: $MAX_SEARCH_TIME (s)"                                                 $LOG_FILE
    print "| Max playouts: $MAX_PLAYOUTS"                                                           $LOG_FILE
    print "| Runs: $NUM_RUNS"                                                                       $LOG_FILE
    print "| "                                                                                      $LOG_FILE
    print "| System: $SYSTEM $CPU_SPEED"                                                            $LOG_FILE
    print "| Output file: $OUTPUT_FILE"                                                             $LOG_FILE
    print "| "                                                                                      $LOG_FILE
    print "| Executing: '$SCRIPT_STR'"                                                              $LOG_FILE
    print "| Git Revision: $REVISION"                                                               $LOG_FILE
    print "| "                                                                                      $LOG_FILE
    print "| Start time: `date +%Y-%m-%d_%H:%M:%S`"                                                 $LOG_FILE
    print "---------------------------------------------------------------------------------------" $LOG_FILE
    print ""                                                                                        $LOG_FILE

    echo "#======================================================================================"  >> $OUTPUT_FILE
    echo "# Kingdomino AI Experiment"                                                               >> $OUTPUT_FILE
    echo "#--------------------------------------------------------------------------------------"  >> $OUTPUT_FILE
    echo "# "                                                                                       >> $OUTPUT_FILE
    echo "# Player strategy:   $PLAYER_STRATEGY"                                                    >> $OUTPUT_FILE
    echo "# Opponent strategy: $OPPONENT_STRATEGY"                                                  >> $OUTPUT_FILE
    echo "# "                                                                                       >> $OUTPUT_FILE
    echo "# Max search time: $MAX_SEARCH_TIME (s)"                                                  >> $OUTPUT_FILE
    echo "# Max playouts: $MAX_PLAYOUTS"                                                            >> $OUTPUT_FILE
    echo "# Runs: $NUM_RUNS"                                                                        >> $OUTPUT_FILE
    echo "# "                                                                                       >> $OUTPUT_FILE
    echo "# System: $SYSTEM $CPU_SPEED"                                                             >> $OUTPUT_FILE
    echo "# Output file: $OUTPUT_FILE"                                                              >> $OUTPUT_FILE
    echo "# "                                                                                       >> $OUTPUT_FILE
    echo "# Executing: '$SCRIPT_STR'"                                                               >> $OUTPUT_FILE
    echo "# Git Revision: $REVISION"                                                                >> $OUTPUT_FILE
    echo "#--------------------------------------------------------------------------------------"  >> $OUTPUT_FILE
    echo "#"                                                                                         >> $OUTPUT_FILE
    echo "# win, score, num_players, available_moves(13), available_draft(13), chosen_draft_position(13), playouts_per_second(13), scores(13), score_diff, opponent_scores(3)" >> $OUTPUT_FILE

    for ((i=1; i <= $NUM_RUNS; i++))
    do
        print "---------------------------------------------------------------------------------------" $LOG_FILE
        print "| Experiment Run $i/$NUM_RUNS [`date +%Y-%m-%d_%H:%M:%S`]"                               $LOG_FILE
        print "---------------------------------------------------------------------------------------" $LOG_FILE
        print ""                                                                                        $LOG_FILE

        eval ${SCRIPT_STR} || true | tee -a $LOG_FILE

        print ""                                                                                        $LOG_FILE
    done

    # Format temporary output file into neat columns
    #cat ${OUTPUT_FILE_TMP} | column -t -s" " >> $OUTPUT_FILE
    cat ${OUTPUT_FILE_TMP} >> $OUTPUT_FILE
    rm ${OUTPUT_FILE_TMP}

    print "---------------------------------------------------------------------------------------"  $LOG_FILE
    print "| Experiment done! [`date +%Y-%m-%d_%H:%M:%S`]"                                           $LOG_FILE
    print "---------------------------------------------------------------------------------------"  $LOG_FILE
}


#
# Experiments
#


#
# 1) Impact of domain knowledge
#
#playGames 1000 "TRUE_RANDOM"                   "TRUE_RANDOM" 0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 1000 "GREEDY_PLACEMENT_RANDOM_DRAFT" "TRUE_RANDOM" 0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 1000 "FULL_GREEDY"                   "TRUE_RANDOM" 0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION


#
# 2) Statistical evaluator versus static evaluator (500 games, 5 s thinking time)
#
#playGames 500 "MCE_TR_WDL" "FULL_GREEDY" 5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
playGames 500 "FULL_GREEDY" "FULL_GREEDY" 5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION


#
# 3) Effect of scoring functions (500 games, 5 s thinking time)
#    (Use MCE-TR/WDL from (2))
#
#playGames 500 "MCE_TR_P" "FULL_GREEDY" 5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 500 "MCE_TR_R" "FULL_GREEDY" 5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION


#
# 4) Enhanced playout policies (200 games, various thinking times)
#
#playGames 200 "MCE_TR_R" "FULL_GREEDY" 0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_TR_R" "FULL_GREEDY" 0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_TR_R" "FULL_GREEDY" 0.3 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_TR_R" "FULL_GREEDY" 0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_TR_R" "FULL_GREEDY"   1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_TR_R" "FULL_GREEDY"   2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_TR_R" "FULL_GREEDY"   4 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_TR_R" "FULL_GREEDY"   6 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_TR_R" "FULL_GREEDY"   8 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_TR_R" "FULL_GREEDY"  10 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION

#playGames 200 "MCE_PG_R" "FULL_GREEDY" 0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_PG_R" "FULL_GREEDY" 0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_PG_R" "FULL_GREEDY" 0.3 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_PG_R" "FULL_GREEDY" 0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_PG_R" "FULL_GREEDY"   1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_PG_R" "FULL_GREEDY"   2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_PG_R" "FULL_GREEDY"   4 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_PG_R" "FULL_GREEDY"   6 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_PG_R" "FULL_GREEDY"   8 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_PG_R" "FULL_GREEDY"  10 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION

#playGames 200 "MCE_EG_R" "FULL_GREEDY" 0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_EG_R" "FULL_GREEDY" 0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_EG_R" "FULL_GREEDY" 0.3 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_EG_R" "FULL_GREEDY" 0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_EG_R" "FULL_GREEDY"   1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_EG_R" "FULL_GREEDY"   2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_EG_R" "FULL_GREEDY"   4 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_EG_R" "FULL_GREEDY"   6 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_EG_R" "FULL_GREEDY"   8 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_EG_R" "FULL_GREEDY"  10 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION

#playGames 200 "MCE_FG_R" "FULL_GREEDY" 0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_FG_R" "FULL_GREEDY" 0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_FG_R" "FULL_GREEDY" 0.3 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_FG_R" "FULL_GREEDY" 0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_FG_R" "FULL_GREEDY"   1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_FG_R" "FULL_GREEDY"   2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_FG_R" "FULL_GREEDY"   4 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_FG_R" "FULL_GREEDY"   6 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_FG_R" "FULL_GREEDY"   8 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "MCE_FG_R" "FULL_GREEDY"  10 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION

#
# 5) Tree search
#

#
# a) Find UCB constant C (200 games, 0.1 - 0.5 s thinking time).
#
#playGames 200 "UCT_TR_C0_1" "FULL_GREEDY"   0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_2" "FULL_GREEDY"   0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_3" "FULL_GREEDY"   0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_4" "FULL_GREEDY"   0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_5" "FULL_GREEDY"   0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_6" "FULL_GREEDY"   0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION

#playGames 200 "UCT_TR_C0_1" "FULL_GREEDY"   0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_2" "FULL_GREEDY"   0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_3" "FULL_GREEDY"   0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_4" "FULL_GREEDY"   0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_5" "FULL_GREEDY"   0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_6" "FULL_GREEDY"   0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION

#playGames 200 "UCT_TR_C0_1" "FULL_GREEDY"   0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_2" "FULL_GREEDY"   0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_3" "FULL_GREEDY"   0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_4" "FULL_GREEDY"   0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_5" "FULL_GREEDY"   0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_TR_C0_6" "FULL_GREEDY"   0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION

#playGames 200 "UCT_FG_C0_1" "FULL_GREEDY"   0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_2" "FULL_GREEDY"   0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_3" "FULL_GREEDY"   0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_4" "FULL_GREEDY"   0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_5" "FULL_GREEDY"   0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_6" "FULL_GREEDY"   0.1 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION

#playGames 200 "UCT_FG_C0_1" "FULL_GREEDY"   0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_2" "FULL_GREEDY"   0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_3" "FULL_GREEDY"   0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_4" "FULL_GREEDY"   0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_5" "FULL_GREEDY"   0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_6" "FULL_GREEDY"   0.2 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION

#playGames 200 "UCT_FG_C0_1" "FULL_GREEDY"   0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_2" "FULL_GREEDY"   0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_3" "FULL_GREEDY"   0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_4" "FULL_GREEDY"   0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_5" "FULL_GREEDY"   0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION
#playGames 200 "UCT_FG_C0_6" "FULL_GREEDY"   0.5 0 $TARGET_DIR $SYSTEM $CPU_SPEED $REVISION

#
# b) Find progressive bias constant W.
#



