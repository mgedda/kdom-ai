#
# Gnuplot script for generating plots to article.
#
# usage: $ gnuplot -e "target_path='<path>'; data_files_dir='<dir>'" produce_plots_experiment_4.gp
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
# EXPERIMENT 4 - Score diffs (playout policies)
#

set title "Score difference to best opponent"
set xlabel "Time per turn (s)"
set ylabel "Score diff" rotate by 90
set key left
set xrange [0.08:12]
set yrange [-30:15]
#unset yrange

#set autoscale x
set logscale x

# Set FG region
#set style rectangle back fc lt -1 fillstyle solid 0.15 noborder
#set object 1 rect from 0.08,-5.681 to 12,-14.649
#set object 1 back lw 1.0 fc 'red' fillstyle default
set style line 1 lc rgb 'red' lt 1 lw 1.0
set style line 2 lc rgb 'red' lt 2 lw 1.0
set arrow 1 from 0.08,-10.165 to 12,-10.165 nohead ls 1
set arrow 2 from 0.08,-5.681 to 12,-5.681 nohead ls 2
set arrow 3 from 0.08,-14.649 to 12,-14.649 nohead ls 2
set label '\scriptsize{FG}' at 6,-18 center

set arrow 4 from 6,-16.3 to 6.5,-11 filled

set terminal x11
plot input_dir."/SCORE_DIFFS_MCE-FG_R.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{MCE-FG/R}', \
	'' using 1:2 w lines lt 1 title '', \
	input_dir."/SCORE_DIFFS_MCE-TR_R.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{MCE-TR/R}', \
	'' using 1:2 w lines lt 2 title ''
#plot input_dir."/SCORE_DIFFS_MCE-FG_R.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{MCE-FG/R}', \
#	'' using 1:2 w lines lt 1 title '', \
#	input_dir."/SCORE_DIFFS_MCE-TR_R.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{MCE-TR/R}', \
#	'' using 1:2 w lines lt 2 title '', \
#	"input_dir."/SCORE_DIFFS_MCE-EG_R.dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{MCE-$\epsilon$G/R}', \
#	'' using 1:2 w lines lt 3 title '', \
#	input_dir."/SCORE_DIFFS_MCE-PG_R.dat" using 1:2:3 w yerrorbars lt 4 title '\scriptsize{MCE-PG/R}', \
#	'' using 1:2 w lines lt 4 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output output_dir."/experiment4_score_diffs.tex"
replot


unset object 1
unset arrow 1
unset arrow 2
unset arrow 3
unset arrow 4
unset label


#
# EXPERIMENT 4 - Playout frequency
#

set title "Playouts/$s$ per round"
set xlabel "Round"
set ylabel "Playout frequency ($s^{-1}$)" rotate by 90
set format y "$10^%T$"

unset xrange
unset yrange
unset logscale x

#set autoscale y
set yrange [10:200000]
set logscale y

set terminal x11
plot input_dir."/PLAYOUTS_MCE-FG_R (10s).dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{MCE-FG/R}', \
	'' using 1:2 w lines lt 1 title '', \
	input_dir."/PLAYOUTS_MCE-TR_R (10s).dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{MCE-TR/R}', \
	'' using 1:2 w lines lt 2 title ''
#plot input_dir."/PLAYOUTS_MCE-FG_R (10s).dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{MCE-FG/R}', \
#	'' using 1:2 w lines lt 1 title '', \
#	input_dir."/PLAYOUTS_MCE-TR_R (10s).dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{MCE-TR/R}', \
#	'' using 1:2 w lines lt 2 title '', \
#	input_dir."/PLAYOUTS_MCE-EG_R (10s).dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{MCE-$\epsilon$G/R}', \
#	'' using 1:2 w lines lt 3 title '', \
#	input_dir."/PLAYOUTS_MCE-PG_R (10s).dat" using 1:2:3 w yerrorbars lt 4 title '\scriptsize{MCE-PG/R}', \
#	'' using 1:2 w lines lt 4 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output output_dir."/experiment4_playouts.tex"
replot


