package com.decmurphy.spx.space;

import java.io.*;
import static com.decmurphy.spx.physics.Globals.*;

public class Planet {

  protected String name;
  protected double[] pos = new double[3];
  protected double radius;
  protected double mass;

  public Planet(String name, double x, double y, double z, double radius, double mass) {
    this.name = name;
    this.pos[0] = x;
    this.pos[1] = y;
    this.pos[2] = z;
    this.radius = radius;
    this.mass = mass;

    draw();
    System.out.printf(this.name + " created.\n");

    if (this.name.equals("Earth")) {
      drawEarth();
      drawHazard();
    }
  }

  private void draw() {
    PrintWriter pw = null;

    try {
      File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
      File tempFile = new File(catalinaBase, "webapps/tmp");
      if (!tempFile.exists()) {
        tempFile.mkdirs();
      }
      File outputFile = new File(tempFile, "/" + name + ".output.txt");
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
  }

  private void drawEarth() {
    PrintWriter pw = null;
    BufferedReader br;

    double theta, psi;
    double R = radiusOfEarth * 1e-3;

    try {
      String line;
      File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
      File tempFile = new File(catalinaBase, "webapps/tmp");
      if (!tempFile.exists()) {
        tempFile.mkdirs();
      }
      File outputFile = new File(tempFile, "/coast.output.txt");
      pw = new PrintWriter(new FileWriter(outputFile, false));

      File earthFile = new File(tempFile, "/coast3.txt");
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

  private void drawHazard() {
    PrintWriter pw = null;
    BufferedReader br;

    double theta, psi;
    double R = radiusOfEarth * 1e-3;

    try {
      String line;
      File catalinaBase = new File(System.getProperty("catalina.base")).getAbsoluteFile();
      File tempFile = new File(catalinaBase, "webapps/tmp");
      if (!tempFile.exists()) {
        tempFile.mkdirs();
      }
      File outputFile = new File(tempFile, "/hazard.output.txt");
      pw = new PrintWriter(new FileWriter(outputFile, false));

      if (!flightCode.isEmpty()) {
        File hazardFile = new File(tempFile, "/" + flightCode + ".hazard.txt");
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
