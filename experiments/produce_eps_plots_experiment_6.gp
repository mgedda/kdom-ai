#
# Gnuplot script for generating plots to article.
#
# usage: $ gnuplot -e "target_path='<path>'; data_files_dir='<dir>'" produce_plots_experiment_6.gp
#
# <Press [Enter] after each plot>
#

#set terminal qt

if (!exists("target_path")) target_path='.'
if (!exists("data_files_dir")) data_files_dir='data_files_experiment_4'

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
# EXPERIMENT 6 - Score diffs (UCB constant vs Score diffs)
#

set title "Score difference to best opponent"
set xlabel 'C'
set ylabel 'Victory margin' rotate by 90
set key left
set xrange [-0.1:2.1]
set yrange [-40:20]

set nologscale x
set nologscale y

set format xy '%g'


set terminal x11
plot input_dir."/SCORE_DIFFS_UCT-TR_T0_2.dat" using 1:2:3 w yerrorbars lt 1 title 'UCT-TR (0.2s)', \
	'' using 1:2 w lines lt 1 title '', \
    input_dir."/SCORE_DIFFS_UCT-TR_T0_5.dat" using 1:2:3 w yerrorbars lt 2 title 'UCT-TR (0.5s)', \
	'' using 1:2 w lines lt 2 title '', \
    input_dir."/SCORE_DIFFS_UCT-FG_T0_2.dat" using 1:2:3 w yerrorbars lt 3 title 'UCT-FG (0.2s)', \
	'' using 1:2 w lines lt 3 title '', \
    input_dir."/SCORE_DIFFS_UCT-FG_T0_5.dat" using 1:2:3 w yerrorbars lt 4 title 'UCT-FG (0.5s)', \
	'' using 1:2 w lines lt 4 title '', \
    input_dir."/SCORE_DIFFS_UCT-FG_T2_0.dat" using 1:2:3 w yerrorbars lt 5 title 'UCT-FG (2.0s)', \
	'' using 1:2 w lines lt 5 title ''

#pause -1

unset title
set key right top
set key vertical Left
set terminal postscript eps size 3.4in,2.4in enhanced color font 'Helvetica,14'
set output output_dir."/experiment6_score_diffs.eps"
replot

#
# EXPERIMENT 6 - Win percentages (UCB constant vs win percentage)
#

#set title "Win percentages"
#set xlabel "C"
#set ylabel "Win percentage" rotate by 90
#set key left
#set xrange [0.05:0.65]
#unset yrange

#set nologscale x
#set nologscale y

#set format x '$%g$'
#set format y '$%.0f%%$'


#set terminal x11
#plot input_dir."/WIN_PERCENTAGES_TIME_1s.dat" using 1:2 lt 1 title '\scriptsize{$t=1.0s$}', \
#	'' using 1:2 w lines lt 1 title '', \
#    input_dir."/WIN_PERCENTAGES_TIME_2s.dat" using 1:2 lt 2 title '\scriptsize{$t=2.0s$}', \
#	'' using 1:2 w lines lt 2 title '', \
#    input_dir."/WIN_PERCENTAGES_TIME_4s.dat" using 1:2 lt 3 title '\scriptsize{$t=4.0s$}', \
#	'' using 1:2 w lines lt 3 title ''

#pause -1

#unset title
#set key vertical Left spacing 0.7 width -4
#set terminal epslatex size 3.5in,2.625in
#set output output_dir."/experiment6_win_percentages.tex"
#replot

