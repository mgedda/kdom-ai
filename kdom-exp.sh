#!/bin/bash

set -e

NUM_RUNS=1000
PLAYER_STRATEGY="MC_OPPONENT_GREEDY_PLACEMENT_RANDOM_DRAFT"
OPPONENT_STRATEGY="BASE_PLAYER"

DATE_DAY=`date +%Y%m%d`
DATE_TIME=`date +%H%M%S`
DATE="$DATE_DAY-$DATE_TIME"

REVISION=$(eval "git rev-parse --short HEAD")

TARGET_DIR="kdom_exp-${DATE}-rev-${REVISION}"

OUTPUT_FILE="${TARGET_DIR}/kdom_exp_${PLAYER_STRATEGY}_vs_${OPPONENT_STRATEGY}.m"
LOG_FILE="${TARGET_DIR}/kdom_exp_${PLAYER_STRATEGY}_vs_${OPPONENT_STRATEGY}.log"

SCRIPT_STR="java -jar out/kdom-exp.jar ${PLAYER_STRATEGY} ${OPPONENT_STRATEGY} ${OUTPUT_FILE}"

mkdir $TARGET_DIR


function print
{
    echo $1 | tee -a $LOG_FILE
}


print "======================================================================================="
print "| Kingdomino AI Experiment"
print "|--------------------------------------------------------------------------------------"
print "| "
print "| Player strategy:   $PLAYER_STRATEGY"
print "| Opponent strategy: $OPPONENT_STRATEGY"
print "| "
print "| Runs: $NUM_RUNS"
print "| Output file: $OUTPUT_FILE"
print "| "
print "| Executing: '$SCRIPT_STR'"
print "| Git Revision: $REVISION"
print "---------------------------------------------------------------------------------------"
print ""

echo "#======================================================================================"  >> $OUTPUT_FILE
echo "# Kingdomino AI Experiment"                                                               >> $OUTPUT_FILE
echo "#--------------------------------------------------------------------------------------"  >> $OUTPUT_FILE
echo "# "                                                                                       >> $OUTPUT_FILE
echo "# Player strategy:   $PLAYER_STRATEGY"                                                    >> $OUTPUT_FILE
echo "# Opponent strategy: $OPPONENT_STRATEGY"                                                  >> $OUTPUT_FILE
echo "# "                                                                                       >> $OUTPUT_FILE
echo "# Runs: $NUM_RUNS"                                                                        >> $OUTPUT_FILE
echo "# Output file: $OUTPUT_FILE"                                                              >> $OUTPUT_FILE
echo "# "                                                                                       >> $OUTPUT_FILE
echo "# Executing: '$SCRIPT_STR'"                                                               >> $OUTPUT_FILE
echo "# Git Revision: $REVISION"                                                                >> $OUTPUT_FILE
echo "#--------------------------------------------------------------------------------------"  >> $OUTPUT_FILE
echo ""                                                                                         >> $OUTPUT_FILE
echo "# win, score, num_players, available_moves(13), available_draft(13), chosen_draft_position(13)" >> $OUTPUT_FILE
echo "kdom_exp_${PLAYER_STRATEGY}_vs_${OPPONENT_STRATEGY} = ["                                  >> $OUTPUT_FILE

for ((i=1; i <= $NUM_RUNS; i++))
do
    print "---------------------------------------------------------------------------------------"
    print "| Experiment Run $i/$NUM_RUNS"
    print "---------------------------------------------------------------------------------------"
    print ""

    eval ${SCRIPT_STR} || true | tee -a $LOG_FILE

    print ""
done

print "---------------------------------------------------------------------------------------"
print "| Experiment done!"
print "---------------------------------------------------------------------------------------"
