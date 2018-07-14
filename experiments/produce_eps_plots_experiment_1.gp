#
# Gnuplot script for generating plots to article.
#
# usage: $ gnuplot -e "target_path='<path>'; data_files_dir='<dir>'" produce_plots_experiment_1.gp
#
# <Press [Enter] after each plot>
#

#set terminal qt

if (!exists("target_path")) target_path='.'
if (!exists("data_files_dir")) data_files_dir='data_files_experiment_1'

input_dir = target_path."/".data_files_dir
output_dir = "figures_eps"
system "mkdir -p ".output_dir

set for [i=1:9] linetype i dashtype i

set grid xtics lc rgb "#bbbbbb" lw 1 lt 0
set grid ytics lc rgb "#bbbbbb" lw 1 lt 0
set grid mxtics lc rgb "#bbbbbb" lw 1 lt 0
set grid mytics lc rgb "#bbbbbb" lw 1 lt 0

set format xy '%g'
set format y2 '10^%T'

#
# EXPERIMENT 1 - Branching factors - double axis
#

set title 'Branching factor per round'
set xlabel 'Round'
set ylabel 'Pre-determined' rotate by 90
set y2label 'With draw' rotate by 90
set key right top

set xrange [0:14]
set x2range [0:14]
set yrange [0:80]
set y2range [1:100000000]


set logscale y2
set y2tics

set terminal x11
plot input_dir."/BRANCHING_FACTORS_TR.dat" using 1:2:3 w yerrorbars axes x1y1 lt 1 title 'Pre-determined', \
     '' using 1:2 w lines axes x1y1 lt 1 title '', \
     input_dir."/BRANCHING_FACTORS_WITH_DRAW_TR.dat" using 1:2 axes x2y2 lt 2 title 'With draw', \
     '' using 1:2 w lines axes x2y2 lt 2 title ''

#pause -1

unset title
set key vertical Left
set terminal postscript eps size 3.4in,2.4in enhanced color font 'Helvetica,14'
set output output_dir."/experiment1_branching_factors.eps"
replot

unset y2label
unset y2tics
unset logscale y2

#
# EXPERIMENT 1 - Scores (static evaluators)
#

set title "Average scores per round"
set xlabel 'Round'
set ylabel 'Score' rotate by 90
set key left
set yrange [0:60]

set terminal x11
plot input_dir."/SCORES_TR.dat" using 1:2:3 w yerrorbars lt 1 title 'TR', \
     '' using 1:2 w lines lt 1 title '', \
     input_dir."/SCORES_GPRD.dat" using 1:2:3 w yerrorbars lt 2 title 'GPRD', \
     '' using 1:2 w lines lt 2 title '', \
     input_dir."/SCORES_FG.dat" using 1:2:3 w yerrorbars lt 3 title 'FG', \
     '' using 1:2 w lines lt 3 title ''

#pause -1

unset title
set key vertical Left
set terminal postscript eps size 3.4in,2.4in enhanced color font 'Helvetica,14'
set output output_dir."/experiment1_scores.eps"
replot

