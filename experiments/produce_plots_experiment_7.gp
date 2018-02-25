#
# Gnuplot script for generating plots to article.
#
# usage: $ gnuplot -e "target_path='<path>'; data_files_dir='<dir>'" produce_plots_experiment_7.gp
#
# <Press [Enter] after each plot>
#

#set terminal qt

if (!exists("target_path")) target_path='.'
if (!exists("data_files_dir")) data_files_dir='data_files_experiment_7'

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
# EXPERIMENT 7 - Score diffs (Bias factor vs Score diffs)
#

set title "Score difference to best opponent"
set xlabel "W"
set ylabel "Score diff" rotate by 90
set key left
set xrange [-0.05:0.65]
set yrange [-40:20]

#set logscale x
#set nologscale y

set format xy '$%g$'


set terminal x11
plot input_dir."/SCORE_DIFFS_UCT_W-TR_T0_5.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{UCT\sub{W}-TR ($0.5$s)}', \
	'' using 1:2 w lines lt 1 title '', \
    input_dir."/SCORE_DIFFS_UCT_W-TR_T2_0.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{UCT\sub{W}-TR ($2.0$s)}', \
	'' using 1:2 w lines lt 2 title '', \
    input_dir."/SCORE_DIFFS_UCT_W-FG_T0_5.dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{UCT\sub{W}-FG ($0.5$s)}', \
	'' using 1:2 w lines lt 3 title '', \
    input_dir."/SCORE_DIFFS_UCT_W-FG_T2_0.dat" using 1:2:3 w yerrorbars lt 4 title '\scriptsize{UCT\sub{W}-FG ($2.0$s)}', \
	'' using 1:2 w lines lt 4 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output output_dir."/experiment7_score_diffs.tex"
replot

