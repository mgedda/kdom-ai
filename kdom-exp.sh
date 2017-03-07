#!/bin/bash

set -e

NUM_RUNS=1
PLAYER_STRATEGY="RANDOM"
OPPONENT_STRATEGY="RANDOM"

DATE=`date +%Y%m%d-%H%M%S`

OUTPUT_FILE="kdom-exp-${DATE}-[${PLAYER_STRATEGY}-vs-${OPPONENT_STRATEGY}].out"
LOG_FILE="kdom-exp-${DATE}-[${PLAYER_STRATEGY}-vs-${OPPONENT_STRATEGY}].log"
SCRIPT_STR="java -jar out/kdom-exp.jar ${PLAYER_STRATEGY} ${OPPONENT_STRATEGY} ${OUTPUT_FILE}"


if [ -f $LOG_FILE ];
then
    rm $LOG_FILE
fi

if [ -f $OUTPUT_FILE ];
then
    rm $OUTPUT_FILE
fi


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
echo "#--------------------------------------------------------------------------------------"  >> $OUTPUT_FILE
echo ""                                                                                         >> $OUTPUT_FILE
echo "# win, score, num_players, available_moves(13), available_draft(13)"                      >> $OUTPUT_FILE


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
