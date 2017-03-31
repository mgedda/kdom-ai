#!/bin/bash

set -e

NUM_RUNS=1
PLAYER_STRATEGY="FULL_GREEDY"
OPPONENT_STRATEGY="FULL_GREEDY"

MAX_SEARCH_TIME=10      # maximum search time per round (seconds) [<=0: deactivated]
MAX_PLAYOUTS=0          # maximum number of playouts per round    [<=0: deactivated]

MAX_SEARCH_TIME_ESCAPED=${MAX_SEARCH_TIME//[.]/_}

DATE_DAY=`date +%Y%m%d`
DATE_TIME=`date +%H%M%S`
DATE="$DATE_DAY-$DATE_TIME"

REVISION=$(eval "git rev-parse --short HEAD")


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

TARGET_DIR="kdom_exp-${DATE}-rev-${REVISION}-cpu-${CPU_SPEED}"

OUTPUT_FILE="${TARGET_DIR}/kdom_exp_${PLAYER_STRATEGY}_vs_${OPPONENT_STRATEGY}_T${MAX_SEARCH_TIME}_P${MAX_PLAYOUTS}.m"
LOG_FILE="${TARGET_DIR}/kdom_exp_${PLAYER_STRATEGY}_vs_${OPPONENT_STRATEGY}_T${MAX_SEARCH_TIME}_P${MAX_PLAYOUTS}.log"

SCRIPT_STR="java -jar out/kdom-exp.jar ${PLAYER_STRATEGY} ${OPPONENT_STRATEGY} ${OUTPUT_FILE} ${MAX_SEARCH_TIME} ${MAX_PLAYOUTS}"

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
print "| Max search time: $MAX_SEARCH_TIME (s)"
print "| Max playouts: $MAX_PLAYOUTS"
print "| Runs: $NUM_RUNS"
print "| "
print "| System: $SYSTEM $CPU_SPEED"
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
echo ""                                                                                         >> $OUTPUT_FILE
echo "# win, score, num_players, available_moves(13), available_draft(13), chosen_draft_position(13), playouts_per_second(13), scores(13), score_diff, opponent_scores(3)" >> $OUTPUT_FILE
echo "kdom_exp_${PLAYER_STRATEGY}_vs_${OPPONENT_STRATEGY}_T${MAX_SEARCH_TIME_ESCAPED}_P${MAX_PLAYOUTS} = ["  >> $OUTPUT_FILE

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
