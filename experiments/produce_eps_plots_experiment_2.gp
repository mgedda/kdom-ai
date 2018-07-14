#
# Gnuplot script for generating plots to article.
#
# usage: $ gnuplot -e "target_path='<path>'; data_files_dir='<dir>'" produce_plots_experiment_2.gp
#
# <Press [Enter] after each plot>
#

#set terminal qt

if (!exists("target_path")) target_path='.'
if (!exists("data_files_dir")) data_files_dir='data_files_experiment_2'

input_dir = target_path."/".data_files_dir
output_dir = "figures_eps"
system "mkdir -p ".output_dir

set for [i=1:9] linetype i dashtype i

set grid xtics lc rgb "#bbbbbb" lw 1 lt 0
set grid ytics lc rgb "#bbbbbb" lw 1 lt 0
set grid mxtics lc rgb "#bbbbbb" lw 1 lt 0
set grid mytics lc rgb "#bbbbbb" lw 1 lt 0

set format xy '%g'

#
# EXPERIMENT 2 - Scores (static vs statistical evaluators)
#

set title "Average scores per round"
set xlabel 'Round'
set ylabel 'Score' rotate by 90
set key left
set yrange [0:]

set terminal x11
plot input_dir."/SCORES_FG.dat" using 1:2:3 w yerrorbars lt 1 title 'FG', \
	'' using 1:2 w lines lt 1 title '', \
	input_dir."/SCORES_MCE-TR_WDL.dat" using 1:2:3 w yerrorbars lt 2 title 'MCE-TR/WDL', \
	'' using 1:2 w lines lt 2 title '', \
	input_dir."/SCORES_MCE-TR_P.dat" using 1:2:3 w yerrorbars lt 3 title 'MCE-TR/P', \
	'' using 1:2 w lines lt 3 title '', \
	input_dir."/SCORES_MCE-TR_R.dat" using 1:2:3 w yerrorbars lt 4 title 'MCE-TR/R', \
	'' using 1:2 w lines lt 4 title ''

#pause -1

unset title
set key vertical Left
set terminal postscript eps size 3.4in,2.4in enhanced color font 'Helvetica,14'
set output output_dir."/experiment2_scores.eps"
replot
