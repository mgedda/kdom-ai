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

set title "Branching factor per round"
set xlabel "Round"
set ylabel "Branching factor" rotate by 90
set key left
set yrange [0:]

set terminal x11
plot "run_experiment_1.out/BRANCHING_FACTORS_TR.dat" using 1:2:3 w yerrorbars lt 1 title '', \
     '' using 1:2 w lines lt 1 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output outputdir."/experiment1_branching_factors.tex"
replot


#
# EXPERIMENT 1 - Scores (static evaluators)
#

set title "Average scores per round"
set xlabel "Round"
set ylabel "Score" rotate by 90
set key left
set yrange [0:]

set terminal x11
plot "run_experiment_1.out/SCORES_TR.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{TR}', \
     '' using 1:2 w lines lt 1 title '', \
     "run_experiment_1.out/SCORES_GPRD.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{GPRD}', \
     '' using 1:2 w lines lt 2 title '', \
     "run_experiment_1.out/SCORES_FG.dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{FG}', \
     '' using 1:2 w lines lt 3 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output outputdir."/experiment1_scores.tex"
replot



#
# EXPERIMENT 2 - Scores (static vs statistical evaluators)
#

set title "Average scores per round"
set xlabel "Round"
set ylabel "Score" rotate by 90
set key left
set yrange [0:]

set terminal x11
plot "run_experiment_2.out/SCORES_FG.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{FG}', \
	'' using 1:2 w lines lt 1 title '', \
	"run_experiment_2.out/SCORES_MCE-TR_WDL.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{MCE-TR/WDL}', \
	'' using 1:2 w lines lt 2 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output outputdir."/experiment2_scores.tex"
replot



#
# EXPERIMENT 3 - Scores (scoring functions)
#

set title "Average scores per round (450 games)"
set xlabel "Round"
set ylabel "Score" rotate by 90
set key left
set yrange [0:]

set terminal x11
plot "run_experiment_2.out/SCORES_MCE-TR_WDL.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{MCE-TR/WDL}', \
	'' using 1:2 w lines lt 1 title '', \
	"run_experiment_3.out/SCORES_MCE-TR_P.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{MCE-TR/P}', \
	'' using 1:2 w lines lt 2 title '', \
	"run_experiment_3.out/SCORES_MCE-TR_R.dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{MCE-TR/R}', \
	'' using 1:2 w lines lt 3 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output outputdir."/experiment3_scores.tex"
replot



#
# EXPERIMENT 4 - Score diffs (playout policies)
#

set title "Score difference to best opponent"
set xlabel "Time per turn (s)"
set ylabel "Score diff" rotate by 90
set key left
set xrange [0.08:12]
#set yrange [-20:15]
unset yrange

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
plot "run_experiment_4.out/SCORE_DIFFS_MCE-FG_R.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{MCE-FG/R}', \
	'' using 1:2 w lines lt 1 title '', \
	"run_experiment_4.out/SCORE_DIFFS_MCE-TR_R.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{MCE-TR/R}', \
	'' using 1:2 w lines lt 2 title '', \
	"run_experiment_4.out/SCORE_DIFFS_MCE-EG_R.dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{MCE-$\epsilon$G/R}', \
	'' using 1:2 w lines lt 3 title '', \
	"run_experiment_4.out/SCORE_DIFFS_MCE-PG_R.dat" using 1:2:3 w yerrorbars lt 4 title '\scriptsize{MCE-PG/R}', \
	'' using 1:2 w lines lt 4 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output outputdir."/experiment4_score_diffs.tex"
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
plot "run_experiment_4.out/PLAYOUTS_MCE-FG_R (10s).dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{MCE-FG/R}', \
	'' using 1:2 w lines lt 1 title '', \
	"run_experiment_4.out/PLAYOUTS_MCE-TR_R (10s).dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{MCE-TR/R}', \
	'' using 1:2 w lines lt 2 title '', \
	"run_experiment_4.out/PLAYOUTS_MCE-EG_R (10s).dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{MCE-$\epsilon$G/R}', \
	'' using 1:2 w lines lt 3 title '', \
	"run_experiment_4.out/PLAYOUTS_MCE-PG_R (10s).dat" using 1:2:3 w yerrorbars lt 4 title '\scriptsize{MCE-PG/R}', \
	'' using 1:2 w lines lt 4 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output outputdir."/experiment4_playouts.tex"
replot


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
plot "run_experiment_6.out/SCORE_DIFFS_C0.1.dat" using 1:2:3 w yerrorbars lt 1 title '\scriptsize{$C=0.1$}', \
	'' using 1:2 w lines lt 1 title '', \
	"run_experiment_6.out/SCORE_DIFFS_C0.2.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{$C=0.2}', \
	'' using 1:2 w lines lt 2 title '', \
	"run_experiment_6.out/SCORE_DIFFS_C0.3.dat" using 1:2:3 w yerrorbars lt 3 title '\scriptsize{$C=0.3}', \
	'' using 1:2 w lines lt 3 title '', \
	"run_experiment_6.out/SCORE_DIFFS_C0.4.dat" using 1:2:3 w yerrorbars lt 4 title '\scriptsize{$C=0.4}', \
	'' using 1:2 w lines lt 4 title '', \
	"run_experiment_6.out/SCORE_DIFFS_C0.5.dat" using 1:2:3 w yerrorbars lt 5 title '\scriptsize{$C=0.5}', \
	'' using 1:2 w lines lt 5 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output outputdir."/experiment6_score_diffs.tex"
replot

#
# EXPERIMENT 6 - Score diffs (UCB constant vs Score diffs)
#

set title "Score difference to best opponent"
set xlabel "C"
set ylabel "Score diff" rotate by 90
set key left
set xrange [0:0.6]
unset yrange

set nologscale x
set nologscale y

set format xy '$%g$'


set terminal x11
plot "run_experiment_6.out/SCORE_DIFFS_TIME_2s.dat" using 1:2:3 w yerrorbars lt 2 title '\scriptsize{$t=2.0s$}', \
	'' using 1:2 w lines lt 2 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output outputdir."/experiment6_score_diffs.tex"
replot

#
# EXPERIMENT 6 - Win percentages (UCB constant vs win percentage)
#

set title "Win percentages"
set xlabel "C"
set ylabel "Win percentage" rotate by 90
set key left
set xrange [0:0.6]
unset yrange

set nologscale x
set nologscale y

set format x '$%g$'
set format y '$%.0f%%$'


set terminal x11
plot "run_experiment_6.out/WIN_PERCENTAGES_TIME_2s.dat" using 1:2 lt 2 title '\scriptsize{$t=2.0s$}', \
	'' using 1:2 w lines lt 2 title ''

pause -1

unset title
set key vertical Left spacing 0.7 width -4
set terminal epslatex size 3.5in,2.625in
set output outputdir."/experiment6_win_percentages.tex"
replot

