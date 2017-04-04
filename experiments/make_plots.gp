#
# Gnuplot script for generating plots to article.
#
# usage: $ gnuplot make_plots.gp
#
# <Press [Enter] after each plot>
#

#set terminal qt

outputdir = "figures"
system "mkdir ".outputdir

set for [i=1:9] linetype i dashtype i

set format xy '$%g$'

set grid xtics lc rgb "#bbbbbb" lw 1 lt 0
set grid ytics lc rgb "#bbbbbb" lw 1 lt 0
set grid mxtics lc rgb "#bbbbbb" lw 1 lt 0
set grid mytics lc rgb "#bbbbbb" lw 1 lt 0


#
# EXPERIMENT 1 - Branching factors
#

set title "Branching factor per round (1500 games)"
set xlabel "Round"
set ylabel "Branching factor" rotate by 90
set key left
set yrange [0:]

set terminal x11
plot "run_experiment_1.out/BRANCHING_FACTORS_TR.dat" using 1:2:3 w yerrorbars lt 1 title '', \
     '' using 1:2 w lines lt 1 title ''

pause -1

set terminal epslatex
set output outputdir."/experiment1_branching_factors.tex"
replot


#
# EXPERIMENT 1 - Scores
#

set title "Average scores per round (1500 games)"
set xlabel "Round"
set ylabel "Score" rotate by 90
set key left
set yrange [0:]

set terminal x11
plot "run_experiment_1.out/SCORES_TR.dat" using 1:2:3 w yerrorbars lt 1 title 'TR', \
     '' using 1:2 w lines lt 1 title '', \
     "run_experiment_1.out/SCORES_GPRD.dat" using 1:2:3 w yerrorbars lt 2 title 'GPRD', \
     '' using 1:2 w lines lt 2 title '', \
     "run_experiment_1.out/SCORES_FG.dat" using 1:2:3 w yerrorbars lt 3 title 'FG', \
     '' using 1:2 w lines lt 3 title ''

pause -1

set terminal epslatex
set output outputdir."/experiment1_scores.tex"
#set output 'experiment1_scores.tex'
replot



#
# EXPERIMENT 2 - Scores
#

set title "Average scores per round (450 games)"
set xlabel "Round"
set ylabel "Score" rotate by 90
set key left
set yrange [0:]

set terminal x11
plot "run_experiment_2.out/SCORES_FG.dat" using 1:2:3 w yerrorbars lt 1 title 'FG', \
	'' using 1:2 w lines lt 1 title '', \
	"run_experiment_2.out/SCORES_MCE-TR_WDL.dat" using 1:2:3 w yerrorbars lt 2 title 'MCE-TR/WDL', \
	'' using 1:2 w lines lt 2 title ''

pause -1

set terminal epslatex
set output outputdir."/experiment2_scores.tex"
#set output 'experiment2_scores.tex'
replot



#
# EXPERIMENT 3 - Scores
#

set title "Average scores per round (450 games)"
set xlabel "Round"
set ylabel "Score" rotate by 90
set key left
set yrange [0:]

set terminal x11
plot "run_experiment_2.out/SCORES_MCE-TR_WDL.dat" using 1:2:3 w yerrorbars lt 1 title 'MCE-TR/WDL', \
	'' using 1:2 w lines lt 1 title '', \
	"run_experiment_3.out/SCORES_MCE-TR_P.dat" using 1:2:3 w yerrorbars lt 2 title 'MCE-TR/P', \
	'' using 1:2 w lines lt 2 title '', \
	"run_experiment_3.out/SCORES_MCE-TR_R.dat" using 1:2:3 w yerrorbars lt 3 title 'MCE-TR/R', \
	'' using 1:2 w lines lt 3 title ''

pause -1

set terminal epslatex
set output outputdir."/experiment3_scores.tex"
replot



#
# EXPERIMENT 4 - Score diffs
#

set title "Average score difference to best opponent (200 games)"
set xlabel "Max time per turn (s)"
set ylabel "Score difference" rotate by 90
set key left
set xrange [0.08:12]
#set yrange [-20:15]
unset yrange

#set autoscale x
set logscale x

set terminal x11
plot "run_experiment_4.out/SCORE_DIFFS_FG.dat" using 1:2:3 w yerrorbars lt 1 title 'FG', \
	'' using 1:2 w lines lt 1 title '', \
	"run_experiment_4.out/SCORE_DIFFS_MCE-FG_R.dat" using 1:2:3 w yerrorbars lt 2 title 'MCE-FG/R', \
	'' using 1:2 w lines lt 2 title '', \
	"run_experiment_4.out/SCORE_DIFFS_MCE-TR_R.dat" using 1:2:3 w yerrorbars lt 3 title 'MCE-TR/R', \
	'' using 1:2 w lines lt 3 title ''

pause -1

set terminal epslatex
set output outputdir."/experiment4_score_diffs.tex"
replot




#
# EXPERIMENT 4 - Playouts
#

set title "Average playouts per second per round (200 games)"
set xlabel "Round"
set ylabel "Playouts/s" rotate by 90

unset xrange
unset yrange
unset logscale x

set autoscale y
set logscale y

set terminal x11
plot "run_experiment_4.out/PLAYOUTS_MCE-TR_R (10s).dat" using 1:2:3 w yerrorbars lt 1 title 'MCE-TR/R', \
	'' using 1:2 w lines lt 1 title '', \
	"run_experiment_4.out/PLAYOUTS_MCE-FG_R (10s).dat" using 1:2:3 w yerrorbars lt 2 title 'MCE-FG/R', \
	'' using 1:2 w lines lt 2 title ''

pause -1

set terminal epslatex
set output outputdir."/experiment4_playouts.tex"
replot

