set key off
unset xtics
unset ytics
unset ztics
unset border
set xrange[5000:5880]
set yrange[0:1920]
set zrange[2900:3500]
set view 90,125
set term png
set output "/home/declan/NetBeansProjects/SpXDevelWeb/build/web/output/newtest.png"
splot "/home/declan/NetBeansProjects/SpXDevelWeb/build/web/output/BoosterStage.dat" u 2:3:4 w l ls 8, "/home/declan/NetBeansProjects/SpXDevelWeb/build/web/output/SecondStage.dat" u 2:3:4 w l ls 9, "/home/declan/NetBeansProjects/SpXDevelWeb/build/web/output/coast.output.txt" u 1:2:3 w l ls 9, "/home/declan/NetBeansProjects/SpXDevelWeb/build/web/output/Earth.output.txt" u 1:2:3 w l ls 5, "/home/declan/NetBeansProjects/SpXDevelWeb/build/web/output/hazard.output.txt" u 1:2:3 w l ls 9
