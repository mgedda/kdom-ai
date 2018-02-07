#
# Gnuplot script for generating plots to article.
#
# usage: $ gnuplot -e "target_path='<path>'; data_files_dir='<dir>'" produce_plots_experiment_6_prel.gp
#
# <Press [Enter] after each plot>
#

#set terminal qt

if (!exists("target_path")) target_path='.'
if (!exists("data_files_dir")) data_files_dir='data_files_experiment_4'

input_dir = target_path."/".data_files_dir
output_dir = "figures"
system "mkdir -p ".output_dir

set for [i=1:9] linetype i dashtype i

set grid xtics lc rgb "#bbbbbb" lw 1 lt 0
set grid ytics lc rgb "#bbbbbb" lw 1 lt 0
set grid mxtics lc rgb "#bbbbbb" lw 1 lt 0
set grid mytics lc rgb "#bbbbbb" lw 1 lt 0

set format xy '$%g$'

#
# EXPERIMENT 6 - Score diffs (UCB constant)
#

set title "Score difference to best opponent"
set xlabel "Time per turn (s)"
set ylabel "Score diff" rotate by 90
set key left
set xrange [0.08:12]
unset yrange

set logscale x
set nologscale y

set format xy '$%g$'


set terminal x11
plot input_dir."/SCORE_DIFFS_C0.1.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{$C=0.1$}', \
	'' using 1:2 w lines lt 1 title '', \
	input_dir."/SCORE_DIFFS_C0.2.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{$C=0.2}', \
	'' using 1:2 w lines lt 2 title '', \
	input_dir."/SCORE_DIFFS_C0.3.dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{$C=0.3}', \
	'' using 1:2 w lines lt 3 title '', \
	input_dir."/SCORE_DIFFS_C0.4.dat" using 1:2:3 w yerrorbars lt 4 title '\scriptsize{$C=0.4}', \
	'' using 1:2 w lines lt 4 title '', \
	input_dir."/SCORE_DIFFS_C0.5.dat" using 1:2:3 w yerrorbars lt 5 title '\scriptsize{$C=0.5}', \
	'' using 1:2 w lines lt 5 title '', \
	input_dir."/SCORE_DIFFS_C0.6.dat" using 1:2:3 w yerrorbars lt 6 title '\scriptsize{$C=0.6}', \
	'' using 1:2 w lines lt 6 title '', \
	input_dir."/SCORE_DIFFS_C1.0.dat" using 1:2:3 w yerrorbars lt 7 title '\scriptsize{$C=1.0}', \
	'' using 1:2 w lines lt 7 title '', \
	input_dir."/SCORE_DIFFS_C1.5.dat" using 1:2:3 w yerrorbars lt 8 title '\scriptsize{$C=1.5}', \
	'' using 1:2 w lines lt 8 title '', \
	input_dir."/SCORE_DIFFS_C2.0.dat" using 1:2:3 w yerrorbars lt 9 title '\scriptsize{$C=2.0}', \
	'' using 1:2 w lines lt 9 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output output_dir."/experiment6_prel_score_diffs.tex"
replot

#
# EXPERIMENT 6 - Score diffs (UCB constant vs Score diffs)
#

set title "Score difference to best opponent"
set xlabel "C"
set ylabel "Score diff" rotate by 90
set key left
set xrange [0.05:2.1]
set yrange [-30:15]

set nologscale x
set nologscale y

set format xy '$%g$'


set terminal x11
plot input_dir."/SCORE_DIFFS_TIME_0.5s.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{$t=0.5s$}', \
	'' using 1:2 w lines lt 1 title '', \
	input_dir."/SCORE_DIFFS_TIME_1s.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{$t=1.0s$}', \
	'' using 1:2 w lines lt 2 title '', \
	input_dir."/SCORE_DIFFS_TIME_2s.dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{$t=2.0s$}', \
	'' using 1:2 w lines lt 3 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output output_dir."/experiment6_prel_score_diffs.tex"
replot

#
# EXPERIMENT 6 - Win percentages (UCB constant vs win percentage)
#

set title "Win percentages"
set xlabel "C"
set ylabel "Win percentage" rotate by 90
set key left
set xrange [0.05:2.1]
unset yrange

set nologscale x
set nologscale y

set format x '$%g$'
set format y '$%.0f%%$'


set terminal x11
plot input_dir."/WIN_PERCENTAGES_TIME_0.5s.dat" using 1:2 lt 1 title '\scriptsize{$t=0.5s$}', \
	'' using 1:2 w lines lt 1 title '', \
    input_dir."/WIN_PERCENTAGES_TIME_1s.dat" using 1:2 lt 2 title '\scriptsize{$t=1.0s$}', \
	'' using 1:2 w lines lt 2 title '', \
    input_dir."/WIN_PERCENTAGES_TIME_2s.dat" using 1:2 lt 3 title '\scriptsize{$t=2.0s$}', \
	'' using 1:2 w lines lt 3 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output output_dir."/experiment6_prel_win_percentages.tex"
replot

