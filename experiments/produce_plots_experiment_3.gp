#
# Gnuplot script for generating plots to article.
#
# usage: $ gnuplot -e "target_path='<path>'; data_files_dir='<dir>'" produce_plots_experiment_3.gp
#
# <Press [Enter] after each plot>
#

#set terminal qt

set lmargin at screen 0.12
set rmargin at screen 0.94
set bmargin 1.0

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

set format xy '\footnotesize{$%g$}'

#
# EXPERIMENT 3 - Scores (scoring functions)
#

set title "Average scores per round (450 games)"
set xlabel '\footnotesize{Round}' offset 0,0.5
set ylabel '\footnotesize{Score}' offset 2,0 rotate by 90
set key left
set yrange [0:]

set terminal x11
plot input_dir."/SCORES_MCE-TR_WDL.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{MCE-TR/WDL}', \
	'' using 1:2 w lines lt 1 title '', \
	input_dir."/SCORES_MCE-TR_P.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{MCE-TR/P}', \
	'' using 1:2 w lines lt 2 title '', \
	input_dir."/SCORES_MCE-TR_R.dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{MCE-TR/R}', \
	'' using 1:2 w lines lt 3 title ''

#pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.4in,2.4in
set output output_dir."/experiment3_scores.tex"
replot


