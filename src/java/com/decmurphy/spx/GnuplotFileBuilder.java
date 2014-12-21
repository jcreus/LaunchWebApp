package com.decmurphy.spx;

import static com.decmurphy.spx.InterfaceServlet.outputPath;
import static com.decmurphy.spx.InterfaceServlet.resourcePath;
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

  public GnuplotFileBuilder(String name) {

    this.name = name + ".gp";
    this.imgPath = name + ".png";
    PrintWriter pw = null;

    try {
      File outputFile = new File(outputPath + "/" + this.name);
      pw = new PrintWriter(new FileWriter(outputFile, false));

      pw.printf("set key off\n");
      pw.printf("unset xtics\n");
      pw.printf("unset ytics\n");
      pw.printf("unset ztics\n");
      pw.printf("unset border\n");
      pw.printf("set xrange[5000:5880]\n");
      pw.printf("set yrange[0:1920]\n");
      pw.printf("set zrange[2900:3500]\n");
      pw.printf("set view 90,125\n");
      pw.printf("set term png\n");
      pw.printf("set output \"%s\"\n", getImagePath());
      pw.printf("splot \"%s/BoosterStage.dat\" u 2:3:4 w l ls 8, ", outputPath);
      pw.printf("\"%s/SecondStage.dat\" u 2:3:4 w l ls 9, ", outputPath);
      pw.printf("\"%s/coast.output.txt\" u 1:2:3 w l ls 9, ", outputPath);
      pw.printf("\"%s/Earth.output.txt\" u 1:2:3 w l ls 5, ", outputPath);
      pw.printf("\"%s/hazard.output.txt\" u 1:2:3 w l ls 9\n", outputPath);

    } catch (IOException e) {
    } finally {
      if (pw != null) {
        pw.close();
      }
    }
  }

  public final String getImagePath() {
    return outputPath + "/" + this.imgPath;
  }

  public String getPath() {
    return outputPath + "/" + this.name;
  }
  
}
