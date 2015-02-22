package com.decmurphy.utils;

import com.decmurphy.spx.mission.Mission;
import static com.decmurphy.spx.servlet.InterfaceServlet.outputPath;
import static com.decmurphy.spx.servlet.InterfaceServlet.imagePath;
import static com.decmurphy.spx.servlet.InterfaceServlet.resourcePath;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author declan
 */
public class GnuplotFileBuilder {

  private String name;
  private String imgPath;
  
  public GnuplotFileBuilder(String id, String phase) {
    this(id, null, phase);
  }

  public GnuplotFileBuilder(String id, Mission mission, String phase) {

    this.name = id + "_" + System.currentTimeMillis() + ".gp";
    this.imgPath = id + "_" + System.currentTimeMillis() + ".png";
    PrintWriter pw = null;
    
    Double t;

    try {
      File outputFile = new File(getPath());
      pw = new PrintWriter(new FileWriter(outputFile, false));

      if (phase.equalsIgnoreCase("landing")) {

        pw.printf("set key off\n");
        pw.printf("unset xtics\n");
        pw.printf("unset ytics\n");
        pw.printf("unset ztics\n");
        pw.printf("unset border\n");
        pw.printf("set xrange[800:1500]\n");
        pw.printf("set yrange[-7000:0]\n");
        pw.printf("set zrange[2900:3700]\n");
        pw.printf("set view 90,0,1.7\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("splot \"%s/%s_Earth.output.txt\" u 1:2:3 w l ls 5, ", outputPath, id);
        pw.printf("\"%s/finecoast.output.txt\" u 1:2:3 w l ls 9, ", resourcePath);
        pw.printf("\"%s/%s_hazard.output.txt\" u 1:2:3 w l ls 9, ", outputPath, id);
        pw.printf("\"%s/%s_BoosterStage.dat\" u 2:3:4 w l ls 8, \"%s/%s_SecondStage.dat\" u 2:3:4 w l ls 9\n", outputPath, id, outputPath, id);

      } else if (phase.equalsIgnoreCase("landing2")) {

        pw.printf("set key off\n");
        pw.printf("unset xtics\n");
        pw.printf("unset ytics\n");
        pw.printf("unset ztics\n");
        pw.printf("unset border\n");
        pw.printf("set xrange[800:2000]\n");
        pw.printf("set yrange[-7000:0]\n");
        pw.printf("set zrange[2500:3500]\n");
        pw.printf("set view 90,0,1.7\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("splot \"%s/%s_Earth.output.txt\" u 1:2:3 w l ls 5, ", outputPath, id);
        pw.printf("\"%s/finecoast.output.txt\" u 1:2:3 w l ls 9, ", resourcePath);
        pw.printf("\"%s/%s_hazard.output.txt\" u 1:2:3 w l ls 9, ", outputPath, id);
        pw.printf("\"%s/%s_BoosterStage.dat\" u 2:3:4 w l ls 8, \"%s/%s_SecondStage.dat\" u 2:3:4 w l ls 9\n", outputPath, id, outputPath, id);

      } else if (phase.equalsIgnoreCase("globe")) {

        pw.printf("set key off\n");
        pw.printf("unset xtics\n");
        pw.printf("unset ytics\n");
        pw.printf("unset ztics\n");
        pw.printf("unset border\n");
        pw.printf("set view 90,0,1.3,1.4\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("splot \"%s/%s_Earth.output.txt\" u 1:2:3 w l ls 5, ", outputPath, id);
        pw.printf("\"%s/coarsecoast.output.txt\" u 1:2:3 w l ls 9, ", resourcePath);
        pw.printf("\"%s/%s_BoosterStage.dat\" u 2:3:4 w l ls 8, \"%s/%s_SecondStage.dat\" u 2:3:4 w l ls 7\n", outputPath, id, outputPath, id);

      } else if (phase.equalsIgnoreCase("velocity1")) {
        
        pw.printf("set key off\n");
        pw.printf("set xlabel \"Time (s)\"\n");
        pw.printf("set ylabel \"Stage 1 Velocity (m/s)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_BoosterStage.dat\" u 1:6 w l, \\\n", outputPath, id);
        pw.printf("\"%s/%s_BoosterStage_events.dat\" u 1:6 w p ls 3\n", outputPath, id);

      } else if (phase.equalsIgnoreCase("velocity2")) {
        
        pw.printf("set key off\n");
        pw.printf("set xlabel \"Time (s)\"\n");
        pw.printf("set ylabel \"Stage 2 Velocity (m/s)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_SecondStage.dat\" u 1:6 w l, \\\n", outputPath, id);
        pw.printf("\"%s/%s_SecondStage_events.dat\" u 1:6 w p ls 3\n", outputPath, id);
                
      } else if (phase.equalsIgnoreCase("altitude1")) {
        
        pw.printf("set key off\n");
        pw.printf("set xlabel \"Time (s)\"\n");
        pw.printf("set ylabel \"Stage 1 Altitude (km)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_BoosterStage.dat\" u 1:5 w l, \\\n", outputPath, id);
        pw.printf("\"%s/%s_BoosterStage_events.dat\" u 1:5 w p ls 3\n", outputPath, id);
        
      } else if (phase.equalsIgnoreCase("altitude2")) {
        
        pw.printf("set key off\n");
        pw.printf("set xlabel \"Time (s)\"\n");
        pw.printf("set ylabel \"Stage 2 Altitude (km)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_SecondStage.dat\" u 1:5 w l, \\\n", outputPath, id);
        pw.printf("\"%s/%s_SecondStage_events.dat\" u 1:5 w p ls 3\n", outputPath, id);

      } else if (phase.equalsIgnoreCase("phase1")) {
        
        pw.printf("set key off\n");
        pw.printf("set xlabel \"Stage 1 Altitude (km)\"\n");
        pw.printf("set ylabel \"Stage 1 Velocity (m/s)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_BoosterStage.dat\" u 5:6 w l, \\\n", outputPath, id);
        pw.printf("\"%s/%s_BoosterStage_events.dat\" u 5:6 w p ls 3\n", outputPath, id);

      } else if (phase.equalsIgnoreCase("phase2")) {
        
        pw.printf("set key off\n");
        pw.printf("set xlabel \"Stage 2 Altitude (km)\"\n");
        pw.printf("set ylabel \"Stage 2 Velocity (m/s)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_SecondStage.dat\" u 5:6 w l, \\\n", outputPath, id);
        pw.printf("\"%s/%s_SecondStage_events.dat\" u 5:6 w p ls 3\n", outputPath, id);

      } else if (phase.equalsIgnoreCase("profile1")) {

        pw.printf("set key off\n");
        pw.printf("set xlabel \"Stage 1 Downrange (km)\"\n");
        pw.printf("set ylabel \"Stage 2 Altitude (km)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_BoosterStage.dat\" u 7:5 w l, \\\n", outputPath, id);        
        pw.printf("\"%s/%s_BoosterStage_events.dat\" u 7:5 w p ls 3\n", outputPath, id);        

      } else if (phase.equalsIgnoreCase("profile2")) {

        pw.printf("set key off\n");
        pw.printf("set xlabel \"Stage 2 Downrange (km)\"\n");
        pw.printf("set ylabel \"Stage 2 Altitude (km)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_SecondStage.dat\" u 7:5 w l, \\\n", outputPath, id);        
        pw.printf("\"%s/%s_SecondStage_events.dat\" u 7:5 w p ls 3\n", outputPath, id);        
            
      } else if (phase.equalsIgnoreCase("q")) {

        pw.printf("set key off\n");
        pw.printf("set xlabel \"Time (s)\"\n");
        pw.printf("set ylabel \"Stage 1 Aerodynamic Pressure (kPa)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_BoosterStage.dat\" u 1:8 w l\n", outputPath, id);        
      
      } else if(phase.equalsIgnoreCase("prop")) {

        pw.printf("set key off\n");
        pw.printf("set xlabel \"Time (s)\"\n");
        pw.printf("set ylabel \"Propellant Mass (tons)\"\n");
        pw.printf("set logscale xy 10\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_BoosterStage.dat\" u 1:9 w l, \"%s/%s_SecondStage.dat\" u 1:9 w l\n", outputPath, id, outputPath, id);
				
			}

    } catch (IOException e) {
    } finally {
      if (pw != null) {
        pw.close();
      }
    }
  }

  public final String getImagePath() {
    return imagePath + "/" + this.imgPath;
  }

  public final String getPath() {
    return outputPath + "/" + this.name;
  }

  public String getImgPath() {
    return "output/" + this.imgPath;
  }

}
