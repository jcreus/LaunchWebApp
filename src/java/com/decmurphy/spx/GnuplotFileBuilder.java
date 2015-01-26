package com.decmurphy.spx;

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

    this.name = id + "_" + System.currentTimeMillis() + ".gp";
    this.imgPath = id + "_" + System.currentTimeMillis() + ".png";
    PrintWriter pw = null;

    try {
      File outputFile = new File(getPath());
      pw = new PrintWriter(new FileWriter(outputFile, false));

      if (phase.equalsIgnoreCase("landing")) {

        pw.printf("set key off\n");
        pw.printf("unset xtics\n");
        pw.printf("unset ytics\n");
        pw.printf("unset ztics\n");
        pw.printf("unset border\n");
        pw.printf("set xrange[5000:5880]\n");
        pw.printf("set yrange[800:1500]\n");
        pw.printf("set zrange[2900:3500]\n");
        pw.printf("set view 98,107,1.7\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("splot \"%s/%s_BoosterStage.dat\" u 2:3:4 w l ls 8, \"%s/%s_SecondStage.dat\" u 2:3:4 w l ls 9, ", outputPath, id, outputPath, id);
        pw.printf("\"%s/finecoast.output.txt\" u 1:2:3 w l ls 9, ", resourcePath);
        pw.printf("\"%s/%s_Earth.output.txt\" u 1:2:3 w l ls 5, ", outputPath, id);
        pw.printf("\"%s/%s_hazard.output.txt\" u 1:2:3 w l ls 9\n", outputPath, id);

      } else if (phase.equalsIgnoreCase("landing2")) {

        pw.printf("set key off\n");
        pw.printf("unset xtics\n");
        pw.printf("unset ytics\n");
        pw.printf("unset ztics\n");
        pw.printf("unset border\n");
        pw.printf("set xrange[5000:5880]\n");
        pw.printf("set yrange[800:1900]\n");
        pw.printf("set zrange[2700:3400]\n");
        pw.printf("set view 85,75,1.7\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("splot \"%s/%s_BoosterStage.dat\" u 2:3:4 w l ls 8, \"%s/%s_SecondStage.dat\" u 2:3:4 w l ls 9, ", outputPath, id, outputPath, id);
        pw.printf("\"%s/finecoast.output.txt\" u 1:2:3 w l ls 9, ", resourcePath);
        pw.printf("\"%s/%s_Earth.output.txt\" u 1:2:3 w l ls 5, ", outputPath, id);
        pw.printf("\"%s/%s_hazard.output.txt\" u 1:2:3 w l ls 9\n", outputPath, id);

      } else if (phase.equalsIgnoreCase("aoa")) {

        pw.printf("set key off\n");
        pw.printf("set xlabel \"Time (s)\"\n");
        pw.printf("set ylabel \"Angle of Attack (rads)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_BoosterStage.dat\" u 1:7 w l, \"%s/%s_SecondStage.dat\" u 1:7 w l\n", outputPath, id, outputPath, id);
        

      } else if (phase.equalsIgnoreCase("globe")) {

        pw.printf("set key off\n");
        pw.printf("unset xtics\n");
        pw.printf("unset ytics\n");
        pw.printf("unset ztics\n");
        pw.printf("unset border\n");
        pw.printf("set view 90,90,1.7\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("splot \"%s/%s_BoosterStage.dat\" u 2:3:4 w l ls 8, \"%s/%s_SecondStage.dat\" u 2:3:4 w l ls 7, ", outputPath, id, outputPath, id);
        pw.printf("\"%s/coarsecoast.output.txt\" u 1:2:3 w l ls 9, ", resourcePath);
        pw.printf("\"%s/%s_Earth.output.txt\" u 1:2:3 w l ls 5\n", outputPath, id);

      } else if (phase.equalsIgnoreCase("velocity")) {
        
        pw.printf("set key off\n");
        pw.printf("set xlabel \"Time (s)\"\n");
        pw.printf("set ylabel \"Velocity (m/s)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_BoosterStage.dat\" u 1:6 w l, \"%s/%s_SecondStage.dat\" u 1:6 w l\n", outputPath, id, outputPath, id);
        
      } else if (phase.equalsIgnoreCase("altitude")) {
        
        pw.printf("set key off\n");
        pw.printf("set xlabel \"Time (s)\"\n");
        pw.printf("set ylabel \"Altitude (km)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_BoosterStage.dat\" u 1:5 w l, \"%s/%s_SecondStage.dat\" u 1:5 w l\n", outputPath, id, outputPath, id);

      } else if (phase.equalsIgnoreCase("phase")) {
        
        pw.printf("set key off\n");
        pw.printf("set xlabel \"Altitude (km)\"\n");
        pw.printf("set ylabel \"Velocity (m/s)\"\n");
        pw.printf("set term pngcairo\n");
        pw.printf("set output \"%s\"\n", getImagePath());
        pw.printf("p \"%s/%s_BoosterStage.dat\" u 5:6 w l, \"%s/%s_SecondStage.dat\" u 5:6 w l\n", outputPath, id, outputPath, id);

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
