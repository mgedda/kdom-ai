#!/bin/bash

TARGET_BASE=out
TARGET_PATH=$TARGET_BASE
#TARGET_PATH=out/$(date +"%Y%m%d-%H%M%S")

# Create output directory

COUNTER=0
while [ -d $TARGET_PATH ]
do
    COUNTER=$((COUNTER + 1))
    TARGET_PATH="${TARGET_BASE}.${COUNTER}"
done
mkdir -p $TARGET_PATH


# Process experiment result files

octave --no-gui --norc --silent --eval "process_experiment_1('$TARGET_PATH', 'data_files/experiment_1')" > /dev/null 2>&1
octave --no-gui --norc --silent --eval "process_experiment_2('$TARGET_PATH', 'data_files/experiment_2')" > /dev/null 2>&1
#octave --no-gui --norc --silent --eval "process_experiment_3('$TARGET_PATH', 'data_files/experiment_3')" > /dev/null 2>&1
octave --no-gui --norc --silent --eval "process_experiment_4('$TARGET_PATH', 'data_files/experiment_4')" > /dev/null 2>&1
octave --no-gui --norc --silent --eval "process_experiment_6_prel('$TARGET_PATH', 'data_files/experiment_6_prel')" > /dev/null 2>&1
octave --no-gui --norc --silent --eval "process_experiment_6('$TARGET_PATH', 'data_files/experiment_6')" > /dev/null 2>&1
octave --no-gui --norc --silent --eval "process_experiment_7_prel('$TARGET_PATH', 'data_files/experiment_7_prel')" > /dev/null 2>&1
octave --no-gui --norc --silent --eval "process_experiment_7('$TARGET_PATH', 'data_files/experiment_7')" > /dev/null 2>&1
octave --no-gui --norc --silent --eval "process_experiment_8('$TARGET_PATH', 'data_files/experiment_8')" > /dev/null 2>&1


# Make plots from processed files

gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_1'" produce_plots_experiment_1.gp > /dev/null 2>&1
gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_2'" produce_plots_experiment_2.gp > /dev/null 2>&1
#gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_3'" produce_plots_experiment_3.gp > /dev/null 2>&1
gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_4'" produce_plots_experiment_4.gp > /dev/null 2>&1
gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_6_prel'" produce_plots_experiment_6_prel.gp > /dev/null 2>&1
gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_6'" produce_plots_experiment_6.gp > /dev/null 2>&1
gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_7_prel'" produce_plots_experiment_7_prel.gp > /dev/null 2>&1
gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_7'" produce_plots_experiment_7.gp > /dev/null 2>&1


# Make eps plots from processed files

gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_1'" produce_eps_plots_experiment_1.gp > /dev/null 2>&1
gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_2'" produce_eps_plots_experiment_2.gp > /dev/null 2>&1
gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_4'" produce_eps_plots_experiment_4.gp > /dev/null 2>&1
#gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_6_prel'" produce_eps_plots_experiment_6_prel.gp > /dev/null 2>&1
gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_6'" produce_eps_plots_experiment_6.gp > /dev/null 2>&1
#gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_7_prel'" produce_eps_plots_experiment_7_prel.gp > /dev/null 2>&1
gnuplot -e "target_path='$TARGET_PATH'; data_files_dir='data_files/experiment_7'" produce_eps_plots_experiment_7.gp > /dev/null 2>&1


# Move plots to target path

mv figures ${TARGET_PATH}/figures > /dev/null 2>&1
mv figures_eps ${TARGET_PATH}/figures_eps > /dev/null 2>&1
