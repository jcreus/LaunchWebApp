package com.decmurphy.spx.space;

import static com.decmurphy.spx.InterfaceServlet.outputPath;
import static com.decmurphy.spx.InterfaceServlet.resourcePath;
import com.decmurphy.spx.physics.Globals;
import static com.decmurphy.spx.physics.Globals.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author declan
 */
public final class Earth extends Planet {

  public Earth(double x, double y, double z) {
    super("Earth", x, y, z, radiusOfEarth, massOfEarth);
    drawHazard();
  }

  @Override
  protected void draw() {

    PrintWriter pw = null;

    try {

      File outputFile = new File(outputPath, "/" + name + ".output.txt");
      pw = new PrintWriter(new FileWriter(outputFile, false));

      double x, y, z;
      double theta, psi, dt = Math.PI / 36, dp = Math.PI / 36;

      for (theta = 0; theta < Math.PI; theta += dt) {
        z = radius * Math.cos(theta);
        for (psi = 0; psi < 2 * Math.PI; psi += dp) {
          x = radius * Math.sin(theta) * Math.sin(psi);
          y = radius * Math.sin(theta) * Math.cos(psi);

          pw.print(x * 1e-3 + "\t" + y * 1e-3 + "\t" + z * 1e-3 + "\n");
        }
        pw.print("\n");
      }
    } catch (IOException e) {
    } finally {
      if (pw != null) {
        pw.close();
      }
    }

    pw = null;
    BufferedReader br;

    double theta, psi;
    double R = radiusOfEarth * 1e-3;

    try {
      String line;
      File outputFile = new File(outputPath, "/coast.output.txt");
      pw = new PrintWriter(new FileWriter(outputFile, false));

      File earthFile = new File(resourcePath, Globals.coastMap);
      br = new BufferedReader(new FileReader(earthFile));

      while ((line = br.readLine()) != null) {
        if (!line.isEmpty()) {
          String[] parts = line.split("\\s+");

          psi = (360 - Double.parseDouble(parts[0])) * Math.PI / 180;
          theta = (90 - Double.parseDouble(parts[1])) * Math.PI / 180;
          pw.print(R * Math.sin(theta) * Math.sin(psi) + "\t" + R * Math.sin(theta) * Math.cos(psi) + "\t" + R * Math.cos(theta) + "\n");
        } else {
          pw.print("\n");
        }
      }
    } catch (IOException e) {
    } finally {
      if (pw != null) {
        pw.close();
      }
      System.out.printf("Earth coast drawn.\n");
    }
  }

  protected void drawHazard() {
    PrintWriter pw = null;
    BufferedReader br;

    double theta, psi;
    double R = radiusOfEarth * 1e-3;

    try {
      String line;
      File outputFile = new File(outputPath, "/hazard.output.txt");
      pw = new PrintWriter(new FileWriter(outputFile, false));

      File hazardFile = new File(resourcePath, "/" + Globals.flightCode + ".hazard.txt");
      br = new BufferedReader(new FileReader(hazardFile));

      while ((line = br.readLine()) != null) {
        if (!line.isEmpty()) {
          String[] parts = line.split("\\s+");

          psi = (360 - Double.parseDouble(parts[0])) * Math.PI / 180;
          theta = (90 - Double.parseDouble(parts[1])) * Math.PI / 180;
          pw.print(R * Math.sin(theta) * Math.sin(psi) + "\t" + R * Math.sin(theta) * Math.cos(psi) + "\t" + R * Math.cos(theta) + "\n");
        } else {
          pw.print("\n");
        }
      }
    } catch (IOException e) {
    } finally {
      if (pw != null) {
        pw.close();
      }
      System.out.printf("Hazard map drawn.\n");
    }
  }
}
