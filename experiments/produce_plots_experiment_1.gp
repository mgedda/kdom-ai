#
# Gnuplot script for generating plots to article.
#
# usage: $ gnuplot -e "target_path='<path>'; data_files_dir='<dir>'" produce_plots_experiment_1.gp
#
# <Press [Enter] after each plot>
#

#set terminal qt

set lmargin at screen 0.12
set rmargin at screen 0.84
set bmargin 1.0

if (!exists("target_path")) target_path='.'
if (!exists("data_files_dir")) data_files_dir='data_files_experiment_1'

input_dir = target_path."/".data_files_dir
output_dir = "figures"
system "mkdir -p ".output_dir

set for [i=1:9] linetype i dashtype i

set grid xtics lc rgb "#bbbbbb" lw 1 lt 0
set grid ytics lc rgb "#bbbbbb" lw 1 lt 0
set grid mxtics lc rgb "#bbbbbb" lw 1 lt 0
set grid mytics lc rgb "#bbbbbb" lw 1 lt 0

set format xy '\footnotesize{$%g$}'
set format y2 '\footnotesize{$10^%T$}'

#
# EXPERIMENT 1 - Branching factors - double axis
#

set title 'Branching factor per round'
set xlabel '\footnotesize{Round}' offset 0,0.5
set ylabel '\footnotesize{Pre-determined}' offset 2,0 rotate by 90
set y2label '\footnotesize{With draw}' offset -2,0 rotate by 90
set key right top

set xrange [0:14]
set x2range [0:14]
set yrange [0:80]
set y2range [1:100000000]


set logscale y2
set y2tics

set terminal x11
plot input_dir."/BRANCHING_FACTORS_TR.dat" using 1:2:3 w yerrorbars axes x1y1 lt 1 title '\scriptsize{Pre-determined}', \
     '' using 1:2 w lines axes x1y1 lt 1 title '', \
     input_dir."/BRANCHING_FACTORS_WITH_DRAW_TR.dat" using 1:2 axes x2y2 lt 2 title '\scriptsize{With draw}', \
     '' using 1:2 w lines axes x2y2 lt 2 title ''

#pause -1

unset title
set key vertical Left spacing 0.7 width -9
set terminal epslatex size 3.4in,2.4in
set output output_dir."/experiment1_branching_factors.tex"
replot

unset y2label
unset y2tics
unset logscale y2

#
# EXPERIMENT 1 - Scores (static evaluators)
#

set rmargin at screen 0.94

set title "Average scores per round"
set xlabel '\footnotesize{Round}' offset 0,0.5
set ylabel '\footnotesize{Score}' offset 2,0 rotate by 90
set key left
set yrange [0:60]

set terminal x11
plot input_dir."/SCORES_TR.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{TR}', \
     '' using 1:2 w lines lt 1 title '', \
     input_dir."/SCORES_GPRD.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{GPRD}', \
     '' using 1:2 w lines lt 2 title '', \
     input_dir."/SCORES_FG.dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{FG}', \
     '' using 1:2 w lines lt 3 title ''

#pause -1

unset title
set key vertical Left spacing 0.7 width -2.5
set terminal epslatex size 3.4in,2.4in
set output output_dir."/experiment1_scores.tex"
replot

