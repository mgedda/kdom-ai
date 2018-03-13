#
# Gnuplot script for generating plots to article.
#
# usage: $ gnuplot -e "target_path='<path>'; data_files_dir='<dir>'" produce_plots_experiment_7.gp
#
# <Press [Enter] after each plot>
#

#set terminal qt

set lmargin at screen 0.12
set rmargin at screen 0.94
set bmargin 1.0

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

set format xy '\footnotesize{$%g$}'

#
# EXPERIMENT 7 - Score diffs (Bias factor vs Score diffs)
#

set title "Score difference to best opponent"
set xlabel '\footnotesize{W}' offset 0,0.5
set ylabel '\footnotesize{Victory margin}' offset 2,0 rotate by 90
set key left
set xrange [-0.05:0.65]
set yrange [-40:20]

#set logscale x
#set nologscale y

set format xy '\footnotesize{$%g$}'


set terminal x11
plot input_dir."/SCORE_DIFFS_UCT_W-TR_T0_5.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{UCT\sub{W}-TR ($0.5$s)}', \
	'' using 1:2 w lines lt 1 title '', \
    input_dir."/SCORE_DIFFS_UCT_W-TR_T2_0.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{UCT\sub{W}-TR ($2.0$s)}', \
	'' using 1:2 w lines lt 2 title '', \
    input_dir."/SCORE_DIFFS_UCT_W-FG_T0_5.dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{UCT\sub{W}-FG ($0.5$s)}', \
	'' using 1:2 w lines lt 3 title '', \
    input_dir."/SCORE_DIFFS_UCT_W-FG_T2_0.dat" using 1:2:3 w yerrorbars lt 4 title '\scriptsize{UCT\sub{W}-FG ($2.0$s)}', \
	'' using 1:2 w lines lt 4 title ''

#pause -1

unset title
set key right top
set key vertical Left spacing 0.7 width -8.5
set terminal epslatex size 3.4in,2.4in
set output output_dir."/experiment7_score_diffs.tex"
replot

